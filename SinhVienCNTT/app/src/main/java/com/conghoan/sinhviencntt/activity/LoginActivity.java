package com.conghoan.sinhviencntt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.ApiResponse;
import com.conghoan.sinhviencntt.model.LoginResponse;
import com.conghoan.sinhviencntt.network.ApiClient;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtMsv, edtPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupAnimations();
        setupClickListeners();
    }

    private void initViews() {
        edtMsv = findViewById(R.id.edt_msv);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void setupAnimations() {
        CardView cardLogin = findViewById(R.id.card_login);
        cardLogin.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        TextView btnTaoTaiKhoan = findViewById(R.id.btn_tao_tai_khoan);
        btnTaoTaiKhoan.setOnClickListener(v -> showTaoTaiKhoanDialog());
    }

    private void showTaoTaiKhoanDialog() {
        EditText input = new EditText(this);
        input.setHint("Nhập mã sinh viên (VD: A38200)");
        input.setPadding(40, 20, 40, 20);
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

        new AlertDialog.Builder(this)
                .setTitle("Tạo tài khoản")
                .setMessage("Nhập mã sinh viên, mật khẩu sẽ được gửi về email của bạn.")
                .setView(input)
                .setPositiveButton("Tạo", (dialog, which) -> {
                    String msv = input.getText().toString().trim();
                    if (msv.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập mã sinh viên", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    taoTaiKhoan(msv);
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void taoTaiKhoan(String msv) {
        Map<String, String> body = new HashMap<>();
        body.put("maSinhVien", msv);

        Toast.makeText(this, "Đang tạo tài khoản...", Toast.LENGTH_SHORT).show();

        ApiClient.getApiService(this).taoTaiKhoan(body).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 0) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Thành công")
                            .setMessage(response.body().getData())
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    String msg = "Tạo tài khoản thất bại";
                    if (response.body() != null) {
                        msg = response.body().getMessage();
                    } else if (response.errorBody() != null) {
                        try {
                            ApiResponse<?> err = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                            if (err != null && err.getMessage() != null) msg = err.getMessage();
                        } catch (Exception ignored) {}
                    }
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin() {
        String msv = edtMsv.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(msv)) {
            edtMsv.setError(getString(R.string.error_empty_msv));
            edtMsv.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError(getString(R.string.error_empty_password));
            edtPassword.requestFocus();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        Map<String, String> body = new HashMap<>();
        body.put("maSinhVien", msv);
        body.put("password", password);

        ApiClient.getApiService(this).login(body).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText(getString(R.string.btn_login));

                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 0) {
                    LoginResponse data = response.body().getData();
                    SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putString("msv", data.getMaSinhVien())
                            .putString("student_name", data.getTen())
                            .putString("token", data.getToken())
                            .putString("lop", data.getLopChuyenNganh())
                            .putString("nganh", data.getNganh())
                            .putString("khoa", data.getKhoa())
                            .apply();

                    Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, 0);
                    finish();
                } else {
                    String msg = "Đăng nhập thất bại";
                    if (response.body() != null) {
                        msg = response.body().getMessage();
                    } else if (response.errorBody() != null) {
                        try {
                            ApiResponse<?> err = new Gson().fromJson(response.errorBody().string(), ApiResponse.class);
                            if (err != null && err.getMessage() != null) msg = err.getMessage();
                        } catch (Exception ignored) {}
                    }
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText(getString(R.string.btn_login));
                // Fallback: đăng nhập offline nếu backend không khả dụng
                loginOffline(msv);
            }
        });
    }

    private void loginOffline(String msv) {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        prefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("msv", msv)
                .putString("student_name", prefs.getString("student_name_" + msv, "Sinh viên"))
                .apply();

        Toast.makeText(this, "Đăng nhập offline (backend không khả dụng)", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, 0);
        finish();
    }
}

package com.conghoan.sinhviencntt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.ApiResponse;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtMsv, edtFullname, edtEmail;
    private Button btnRegister;
    private ImageView btnBack;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupAnimations();
        setupClickListeners();
    }

    private void initViews() {
        edtMsv = findViewById(R.id.edt_msv);
        edtFullname = findViewById(R.id.edt_fullname);
        edtEmail = findViewById(R.id.edt_email);
        btnRegister = findViewById(R.id.btn_register);
        btnBack = findViewById(R.id.btn_back);
        tvLogin = findViewById(R.id.tv_login);
    }

    private void setupAnimations() {
        CardView cardRegister = findViewById(R.id.card_register);
        cardRegister.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
    }

    private void setupClickListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());

        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        tvLogin.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });
    }

    private void attemptRegister() {
        String msv = edtMsv.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (TextUtils.isEmpty(msv)) {
            edtMsv.setError(getString(R.string.error_empty_msv));
            edtMsv.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(fullname)) {
            edtFullname.setError("Vui lòng nhập họ và tên");
            edtFullname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_empty_email));
            edtEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.error_invalid_email));
            edtEmail.requestFocus();
            return;
        }

        btnRegister.setEnabled(false);
        btnRegister.setText("Đang đăng ký...");

        // Gọi API đăng ký - dùng MSV làm password mặc định ban đầu
        Map<String, String> body = new HashMap<>();
        body.put("maSinhVien", msv);
        body.put("password", email); // Dùng email làm password khi đăng ký

        ApiClient.getApiService(this).register(body).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText(getString(R.string.btn_register));

                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 0) {
                    // Lưu tên cho hiển thị
                    SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
                    prefs.edit().putString("student_name_" + msv, fullname).apply();

                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Mật khẩu là email của bạn.", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(0, R.anim.slide_out_left);
                } else {
                    String msg = "Đăng ký thất bại";
                    if (response.body() != null) msg = response.body().getMessage();
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText(getString(R.string.btn_register));
                // Fallback offline
                SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
                prefs.edit().putString("student_name_" + msv, fullname).apply();
                Toast.makeText(RegisterActivity.this, "Đã lưu offline. Kết nối backend để hoàn tất đăng ký.", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

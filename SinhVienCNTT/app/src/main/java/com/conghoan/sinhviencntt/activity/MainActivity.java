package com.conghoan.sinhviencntt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.conghoan.sinhviencntt.model.ApiResponse;
import com.conghoan.sinhviencntt.model.DanhMucModel;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.adapter.DashboardAdapter;
import com.conghoan.sinhviencntt.model.DashboardItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDashboard;
    private TextView tvStudentName, tvStudentMsv;
    private ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        loadUserInfo();
        setupDashboard();
        setupLogout();
        setupChangePassword();
    }

    private void initViews() {
        rvDashboard = findViewById(R.id.rv_dashboard);
        tvStudentName = findViewById(R.id.tv_student_name);
        tvStudentMsv = findViewById(R.id.tv_student_msv);
        btnLogout = findViewById(R.id.btn_logout);
    }

    private void loadUserInfo() {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        String msv = prefs.getString("msv", "");
        String name = prefs.getString("student_name", "Sinh viên");

        tvStudentName.setText(name);
        tvStudentMsv.setText("MSV: " + msv);
    }

    // Khi khởi động app
    // Lấy danh sách các item đã được lưu ở bộ nhớ của điện thoại (SharePreference) và hiển thị lên MainActivity
    // Sau đó sẽ load dữ liệu từ backend và hiển thị lại danh sách mới (danh sách các danh mục đang hoạt động)
    // ví dụ: Danh sách danh muc ban đầu có 6 danh mục, khi admin thay đổi (xóa hoặc ẩn 3 danh mục) -> danh sách còn 3 danh mục
    // thì khi khởi động ứng dụng, MainActivity ban đầu sẽ hiển thị 6 danh mục sau đó refresh, hiển thị 3 danh mục đang dang sử dụng (active)
    private void setupDashboard() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvDashboard.setLayoutManager(layoutManager);
        rvDashboard.setHasFixedSize(true);

        List<DashboardItem> cached = loadDashboardCache();
        if (cached != null && !cached.isEmpty()) {
            rvDashboard.setAdapter(new DashboardAdapter(this, cached));
        }

        // Lấy các danh mục đang hoạt động (active) từ database
        // và hiển thị lên Activity, sau đó lưu danh sách này vào SharePreference
        ApiClient.getApiService(this).getDanhMucActive().enqueue(
                new Callback<List<DanhMucModel>>() {
            @Override
            public void onResponse(Call<List<DanhMucModel>> call,
                                   Response<List<DanhMucModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DashboardItem> items = mapToDashboardItems(response.body());
                    rvDashboard.setAdapter(new DashboardAdapter(MainActivity.this, items));
                    saveDashboardCache(response.body()); // lưu danh sách vào SharePreference
                } else if (cached == null || cached.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Không tải được danh mục", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<DanhMucModel>> call, Throwable t) {
                if (cached == null || cached.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Chuyển đổi List<DanhMucModel thành List<DashboardItem)
    // để hiển thị lên MainActivity
    private List<DashboardItem> mapToDashboardItems(List<DanhMucModel> list) {
        int[] bgColors = {R.color.card_profile, R.color.card_tkb, R.color.card_bangdiem,
                R.color.card_khoa, R.color.card_phongdaotao, R.color.card_truong,
                R.color.card_thongtin, R.color.card_hoidap};
        int[] iconColors = {R.color.card_profile_icon, R.color.card_tkb_icon, R.color.card_bangdiem_icon,
                R.color.card_khoa_icon, R.color.card_phongdaotao_icon, R.color.card_truong_icon,
                R.color.card_thongtin_icon, R.color.card_hoidap_icon};

        List<DashboardItem> items = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DanhMucModel dm = list.get(i);
            int bg = bgColors[i % bgColors.length];
            int ic = iconColors[i % iconColors.length];
            int fallback = fallbackIconFor(dm.getMaManHinh());
            items.add(new DashboardItem(
                    dm.getTenDayDu(),
                    dm.getTenVietTat() == null ? "" : dm.getTenVietTat(),
                    dm.getLinkAnh(),
                    fallback,
                    bg,
                    ic,
                    dm.getLoai(),
                    dm.getMaManHinh(),
                    dm.getLinkTruyCap()
            ));
        }
        return items;
    }

    private int fallbackIconFor(String maMH) {
        if (maMH == null) return R.drawable.ic_info;
        switch (maMH) {
            case "PROFILE":  return R.drawable.ic_profile;
            case "TKB":      return R.drawable.ic_schedule;
            case "BANGDIEM": return R.drawable.ic_grade;
            case "KHOA":     return R.drawable.ic_faculty;
            case "PDT":      return R.drawable.ic_training;
            case "TRUONG":   return R.drawable.ic_university;
            case "THONGTIN": return R.drawable.ic_info;
            case "HOIDAP":   return R.drawable.ic_qa;
            default:         return R.drawable.ic_info;
        }
    }

    private void saveDashboardCache(List<DanhMucModel> list) {
        try {
            String json = new com.google.gson.Gson().toJson(list);
            getSharedPreferences("SinhVienCNTT", MODE_PRIVATE)
                    .edit().putString("dashboard_cache", json).apply();
        } catch (Exception ignored) {}
    }

    // Lấy danh sách item hiển thị trên MainActivity (DashboardItem)
    // từ SharedPreference
    private List<DashboardItem> loadDashboardCache() {
        try {
            String json = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE).getString("dashboard_cache", null); // lấy chuỗi json SharedPreference
            if (json == null) return null;
            // Định nghĩa type List<DanhMuc>
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<DanhMucModel>>(){}.getType();
            // Gson là đối tượng dùng để chuyển đổi JSON thành các đối tượng Java
            List<DanhMucModel> list = new com.google.gson.Gson().fromJson(json, type); // chuyển đổi chuỗi json thành List<DanhMucModel>
            return list == null ? null : mapToDashboardItems(list);
        } catch (Exception e) {
            return null;
        }
    }

    // Khi logout tài khoản, xóa tất cả các thông tin được lưu trong SharedPreference
    private void setupLogout() {
        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
                        prefs.edit()
                                .putBoolean("is_logged_in", false)
                                .remove("msv")
                                .remove("student_name")
                                .remove("token")
                                .remove("lop")
                                .remove("nganh")
                                .remove("khoa")
                                .apply();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, 0);
                        finish();
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    private void setupChangePassword() {
        ImageView btnChangePass = findViewById(R.id.btn_change_password);
        btnChangePass.setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 20, 50, 0);

            EditText edtNewPass = new EditText(this);
            edtNewPass.setHint("Mật khẩu mới");
            edtNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(edtNewPass);

            EditText edtConfirm = new EditText(this);
            edtConfirm.setHint("Xác nhận mật khẩu");
            edtConfirm.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(edtConfirm);

            new AlertDialog.Builder(this)
                    .setTitle("Đổi mật khẩu")
                    .setView(layout)
                    .setPositiveButton("Đổi", (dialog, which) -> {
                        String newPass = edtNewPass.getText().toString().trim();
                        String confirm = edtConfirm.getText().toString().trim();

                        if (newPass.isEmpty() || newPass.length() < 6) {
                            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!newPass.equals(confirm)) {
                            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Map<String, String> body = new HashMap<>();
                        body.put("newPassword", newPass);

                        ApiClient.getApiService(this).changePassword(body).enqueue(new Callback<ApiResponse<String>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 0) {
                                    Toast.makeText(MainActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }
}

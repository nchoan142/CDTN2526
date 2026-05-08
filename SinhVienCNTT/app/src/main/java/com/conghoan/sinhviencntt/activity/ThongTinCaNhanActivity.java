package com.conghoan.sinhviencntt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThongTinCaNhanActivity extends AppCompatActivity {

    private LinearLayout infoContainer;
    private TextView tvLoading, tvProfileName, tvProfileMsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtincanhan);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        infoContainer = findViewById(R.id.info_container);
        tvLoading = findViewById(R.id.tv_loading);
        tvProfileName = findViewById(R.id.tv_profile_name);
        tvProfileMsv = findViewById(R.id.tv_profile_msv);

        loadThongTin();
    }

    private void loadThongTin() {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        String msv = prefs.getString("msv", "");

        ApiClient.getApiService(this).getSinhVien(msv).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayInfo(response.body());
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                showError();
            }
        });
    }

    private void displayInfo(Map<String, Object> data) {
        tvLoading.setVisibility(View.GONE);

        String hoTenDem = getStringValue(data, "hoTenDem");
        String ten = getStringValue(data, "ten");
        String fullName = (hoTenDem + " " + ten).trim();
        String maSinhVien = getStringValue(data, "maSinhVien");

        tvProfileName.setText(fullName.isEmpty() ? "---" : fullName);
        tvProfileMsv.setText("MSV: " + (maSinhVien.isEmpty() ? "---" : maSinhVien));

        addInfoRow("Mã sinh viên", maSinhVien);
        addInfoRow("Họ và tên", fullName);
        addInfoRow("Ngày sinh", getStringValue(data, "ngaySinh"));
        addInfoRow("Lớp", getStringValue(data, "lop"));
        addInfoRow("Lớp chuyên ngành", getStringValue(data, "lopChuyenNganh"));
        addInfoRow("Ngành", getStringValue(data, "nganh"));
        addInfoRow("Khoa", getStringValue(data, "khoa"));
        addInfoRow("Khoá nhập học", getStringValue(data, "khoaNhapHoc"));
        addInfoRow("Năm nhập học", getStringValue(data, "nhapHoc"));
        addInfoRow("Email 1", getStringValue(data, "email1"));
        addInfoRow("Email 2", getStringValue(data, "email2"));
        addInfoRow("Điện thoại 1", getStringValue(data, "dienThoai1"));
        addInfoRow("Điện thoại 2", getStringValue(data, "dienThoai2"));
        addInfoRow("Ghi chú", getStringValue(data, "ghiChu"));

        // Load thông tin CVHT
        String msv = getStringValue(data, "maSinhVien");
        if (!msv.isEmpty()) {
            loadCvht(msv);
        }
    }

    private void loadCvht(String msv) {
        ApiClient.getApiService(this).getCvhtOfSinhVien(msv).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Map<String, String> cvht = response.body();
                    String maGv = cvht.get("maGiangVien");
                    String tenGv = cvht.get("tenGiangVien");
                    String display = "";
                    if (tenGv != null && !tenGv.isEmpty()) display = tenGv;
                    if (maGv != null && !maGv.isEmpty()) display += " (" + maGv + ")";
                    if (!display.isEmpty()) {
                        addInfoRow("Cố vấn học tập", display.trim());
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {}
        });
    }

    private void addInfoRow(String label, String value) {
        if (value == null || value.isEmpty() || value.equals("null")) return;

        View row = getLayoutInflater().inflate(R.layout.item_profile_row, infoContainer, false);
        ((TextView) row.findViewById(R.id.tv_label)).setText(label);
        ((TextView) row.findViewById(R.id.tv_value)).setText(value);
        infoContainer.addView(row);
    }

    private String getStringValue(Map<String, Object> data, String key) {
        Object val = data.get(key);
        if (val == null) return "";
        return val.toString();
    }

    private void showError() {
        tvLoading.setText("Không thể tải thông tin. Vui lòng thử lại sau.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

package com.conghoan.sinhviencntt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.BangDiemModel;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BangDiemActivity extends AppCompatActivity {

    private LinearLayout gradeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bangdiem);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        gradeContainer = findViewById(R.id.grade_container);
        loadBangDiem();
    }

    private void loadBangDiem() {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        String msv = prefs.getString("msv", "");

        ApiClient.getApiService(this).getBangDiem(msv).enqueue(new Callback<List<BangDiemModel>>() {
            @Override
            public void onResponse(Call<List<BangDiemModel>> call, Response<List<BangDiemModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayGrades(response.body());
                } else {
                    showEmptyDataMessage();
                }
            }

            @Override
            public void onFailure(Call<List<BangDiemModel>> call, Throwable t) {
                showEmptyDataMessage();
            }
        });
    }

    // Hiển thị TextView empty khi sinh viên không có dữ liệu bảng điểm
    private void showEmptyDataMessage() {
        if (gradeContainer == null) return;

        gradeContainer.removeAllViews();

        TextView tvEmpty = new TextView(this);
        tvEmpty.setText("Không có dữ liệu bảng điểm");
        tvEmpty.setTextSize(16);
        tvEmpty.setPadding(0, 50, 0, 50);
        tvEmpty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvEmpty.setTextColor(getResources().getColor(android.R.color.darker_gray));

        gradeContainer.addView(tvEmpty);
    }

    private void displayGrades(List<BangDiemModel> grades) {
        if (gradeContainer == null) {
            showEmptyDataMessage();
            return;
        }
        gradeContainer.removeAllViews();

        double tongDiem = 0;
        int tongTC = 0;
        int completed = 0;

        for (BangDiemModel bd : grades) {
            View row = getLayoutInflater().inflate(R.layout.item_grade_row, gradeContainer, false);
            ((TextView) row.findViewById(R.id.tv_subject_name)).setText(bd.getTenHocPhan());
            ((TextView) row.findViewById(R.id.tv_credits)).setText(bd.getSoTinChi() + " TC");

            Double diem = bd.getDiemTongKet();
            Log.d("Diem", "Diem cac mon: " + bd.getDiemTongKet());
            String diemStr = diem != null && diem >= 0 ? String.format("%.1f", diem) : "-"; // Điểm hệ 10, chuyển sang dạng String
            String gpaStr = diem != null && diem >= 0 ? String.format("%.1f", diem / 2.5) : "-"; // Điểm hệ 4, chuyển sang dạng String
            String grade = getLetterGrade(diem);
            ((TextView) row.findViewById(R.id.tv_score_detail)).setText("Mã HP: " + bd.getMaHocPhan() + "  |  Điểm hệ 4: " + gpaStr + " (" + grade + ")");
            TextView tvGrade = row.findViewById(R.id.tv_grade_letter); // TextView điểm tổng kết hệ số 10
//            tvGrade.setText(grade);
            tvGrade.setText(diemStr);

            int color;
            if (grade.startsWith("A")) {
                color = getResources().getColor(R.color.card_khoa_icon);
            } else if (grade.startsWith("B")) {
                color = getResources().getColor(R.color.accent);
            } else if (grade.equals("-")) {
                color = getResources().getColor(android.R.color.darker_gray);
            } else {
                color = getResources().getColor(R.color.card_bangdiem_icon);
            }
            tvGrade.setTextColor(color);

            gradeContainer.addView(row);

            if (diem != null && diem >= 0 && bd.getSoTinChi() != null) {
                tongDiem += diem * bd.getSoTinChi();
                tongTC += bd.getSoTinChi();
                if (diem >= 4.0) completed += bd.getSoTinChi();
            }
        }

        // Update GPA card
        TextView tvGpa = findViewById(R.id.tv_gpa);
        TextView tvCredits = findViewById(R.id.tv_credits_info);
        if (tvGpa != null) {
            if (tongTC > 0) {
//                double gpa = (tongDiem / tongTC) / 2.5;
                double gpa = (tongDiem / tongTC);
//                tvGpa.setText(String.format("%.2f", Math.min(gpa, 4.0)));
                tvGpa.setText(String.format("%.2f", gpa));
            } else {
                tvGpa.setText("0.00");
            }
        }
        if (tvCredits != null) {
            tvCredits.setText(completed + "/" + tongTC);
        }
    }

    private String getLetterGrade(Double diem) {
        if (diem == null || diem < 0) return "-";
        if (diem >= 8.5) return "A";
        if (diem >= 7.0) return "B+";
        if (diem >= 5.5) return "B";
        if (diem >= 4.0) return "C";
        return "F";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

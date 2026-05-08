package com.conghoan.sinhviencntt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.ApiResponse;
import com.conghoan.sinhviencntt.model.HoiDapModel;
import com.conghoan.sinhviencntt.network.ApiClient;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoiDapActivity extends AppCompatActivity {

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoidap);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        container = findViewById(R.id.hoidap_container);

        ExtendedFloatingActionButton fabAsk = findViewById(R.id.fab_ask);
        fabAsk.setOnClickListener(v -> showAskDialog());

        loadHoiDap();
    }

    private void loadHoiDap() {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        String msv = prefs.getString("msv", "");
        ApiClient.getApiService(this).getHoiDapByMsv(msv).enqueue(new Callback<List<HoiDapModel>>() {
            @Override
            public void onResponse(Call<List<HoiDapModel>> call, Response<List<HoiDapModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayHoiDap(response.body());
                }
                // Nếu API rỗng hoặc lỗi, giữ mock data XML
            }

            @Override
            public void onFailure(Call<List<HoiDapModel>> call, Throwable t) {
                // Giữ mock data XML
            }
        });
    }

    private void displayHoiDap(List<HoiDapModel> list) {
        container.removeAllViews();

        for (HoiDapModel hd : list) {
            CardView card = new CardView(this);
            CardView.LayoutParams cardLp = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            cardLp.bottomMargin = 24;
            card.setLayoutParams(cardLp);
            card.setRadius(36);
            card.setCardElevation(8);
            card.setContentPadding(36, 36, 36, 36);
            card.setCardBackgroundColor(getResources().getColor(R.color.bg_card));

            LinearLayout inner = new LinearLayout(this);
            inner.setOrientation(LinearLayout.VERTICAL);

            // Badge Q + tên SV
            LinearLayout qRow = new LinearLayout(this);
            qRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
            qRow.setPadding(0, 0, 0, 16);

            TextView qBadge = new TextView(this);
            qBadge.setText("Q");
            qBadge.setTextColor(0xFFFFFFFF);
            qBadge.setTextSize(12);
            qBadge.setGravity(android.view.Gravity.CENTER);
            qBadge.setBackgroundResource(R.drawable.bg_qa_badge_q);
            LinearLayout.LayoutParams badgeLp = new LinearLayout.LayoutParams(48, 48);
            badgeLp.rightMargin = 16;
            qBadge.setLayoutParams(badgeLp);
            qRow.addView(qBadge);

            TextView tvName = new TextView(this);
            tvName.setText(hd.getTenSinhVien() != null ? hd.getTenSinhVien() : hd.getMaSinhVien());
            tvName.setTextColor(getResources().getColor(R.color.text_hint));
            tvName.setTextSize(11);
            qRow.addView(tvName);

            // Trạng thái
            if (!Boolean.TRUE.equals(hd.getDaDuyet())) {
                TextView badge = new TextView(this);
                badge.setText("  Chờ duyệt");
                badge.setTextColor(getResources().getColor(R.color.card_bangdiem_icon));
                badge.setTextSize(10);
                qRow.addView(badge);
            }

            inner.addView(qRow);

            // Câu hỏi
            TextView tvQuestion = new TextView(this);
            tvQuestion.setText(hd.getCauHoi());
            tvQuestion.setTextColor(getResources().getColor(R.color.text_primary));
            tvQuestion.setTextSize(14);
            tvQuestion.setTypeface(null, android.graphics.Typeface.BOLD);
            tvQuestion.setPadding(0, 0, 0, 20);
            inner.addView(tvQuestion);

            // Câu trả lời
            if (hd.getCauTraLoi() != null && !hd.getCauTraLoi().isEmpty()) {
                LinearLayout aRow = new LinearLayout(this);
                aRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
                aRow.setPadding(0, 0, 0, 12);

                TextView aBadge = new TextView(this);
                aBadge.setText("A");
                aBadge.setTextColor(0xFFFFFFFF);
                aBadge.setTextSize(12);
                aBadge.setGravity(android.view.Gravity.CENTER);
                aBadge.setBackgroundResource(R.drawable.bg_qa_badge_a);
                LinearLayout.LayoutParams aBadgeLp = new LinearLayout.LayoutParams(48, 48);
                aBadgeLp.rightMargin = 16;
                aBadge.setLayoutParams(aBadgeLp);
                aRow.addView(aBadge);

                TextView tvAnswerer = new TextView(this);
                tvAnswerer.setText(hd.getNguoiTraLoi() != null ? hd.getNguoiTraLoi() : "Admin");
                tvAnswerer.setTextColor(getResources().getColor(R.color.accent));
                tvAnswerer.setTextSize(11);
                tvAnswerer.setTypeface(null, android.graphics.Typeface.BOLD);
                aRow.addView(tvAnswerer);

                inner.addView(aRow);

                TextView tvAnswer = new TextView(this);
                tvAnswer.setText(hd.getCauTraLoi());
                tvAnswer.setTextColor(getResources().getColor(R.color.text_secondary));
                tvAnswer.setTextSize(13);
                tvAnswer.setPadding(64, 0, 0, 0);
                inner.addView(tvAnswer);
            } else {
                TextView tvWait = new TextView(this);
                tvWait.setText("Chờ phản hồi...");
                tvWait.setTextColor(getResources().getColor(R.color.text_hint));
                tvWait.setTextSize(12);
                tvWait.setTypeface(null, android.graphics.Typeface.ITALIC);
                inner.addView(tvWait);
            }

            card.addView(inner);
            container.addView(card);
        }
    }

    private void showAskDialog() {
        EditText input = new EditText(this);
        input.setHint("Nhập câu hỏi của bạn...");
        input.setMinLines(3);
        input.setPadding(40, 20, 40, 20);

        new AlertDialog.Builder(this)
                .setTitle("Đặt câu hỏi")
                .setView(input)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    String cauHoi = input.getText().toString().trim();
                    if (cauHoi.isEmpty()) {
                        Toast.makeText(this, "Vui lòng nhập câu hỏi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendQuestion(cauHoi);
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    private void sendQuestion(String cauHoi) {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        String msv = prefs.getString("msv", "");
        String ten = prefs.getString("student_name", "Sinh viên");

        Map<String, String> body = new HashMap<>();
        body.put("maSinhVien", msv);
        body.put("tenSinhVien", ten);
        body.put("cauHoi", cauHoi);

        ApiClient.getApiService(this).guiCauHoi(body).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 0) {
                    Toast.makeText(HoiDapActivity.this, "Câu hỏi đã được gửi, chờ duyệt!", Toast.LENGTH_SHORT).show();
                    loadHoiDap(); // Reload danh sách
                } else {
                    Toast.makeText(HoiDapActivity.this, "Gửi thất bại, thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(HoiDapActivity.this, "Lỗi kết nối, thử lại sau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

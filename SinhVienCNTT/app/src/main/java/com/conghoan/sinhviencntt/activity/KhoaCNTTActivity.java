package com.conghoan.sinhviencntt.activity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.GiangVienModel;
import com.conghoan.sinhviencntt.model.ThongBaoModel;
import com.conghoan.sinhviencntt.network.ApiClient;

import androidx.cardview.widget.CardView;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhoaCNTTActivity extends AppCompatActivity {

    private LinearLayout teacherContainer;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khoa);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        teacherContainer = findViewById(R.id.teacher_container);
        setupContacts();
        loadGiangVien();
        loadSinhVienCount();
        loadTinTuc();
    }

    private void setupContacts() {
        setupContactRow(R.id.contact_phone, R.drawable.ic_info, "Điện thoại", "0964930762", R.color.card_phongdaotao);
        setupContactRow(R.id.contact_email, R.drawable.ic_email, "Email", "cntt@tlu.edu.vn", R.color.card_tkb);
        setupContactRow(R.id.contact_address, R.drawable.ic_university, "Địa chỉ", "Tầng 2, Đường Nghiêm Xuân Yêm, Phường Định Công, Thành phố Hà Nội", R.color.card_khoa);
        
    }

    private void loadSinhVienCount() {
        ApiClient.getApiService(this).getSinhVienCount().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Object total = response.body().get("total");
                    TextView tvStatSv = findViewById(R.id.tv_stat_sv);
                    if (tvStatSv != null && total != null) {
                        int count = ((Number) total).intValue();
                        tvStatSv.setText(count > 1000 ? String.format("%,d+", count) : String.valueOf(count));
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
        });
    }

    private void loadGiangVien() {
        ApiClient.getApiService(this).getGiangVien().enqueue(new Callback<List<GiangVienModel>>() {
            @Override
            public void onResponse(Call<List<GiangVienModel>> call, Response<List<GiangVienModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayGiangVien(response.body());
                } else {
                    showEmptyDataMessage();
                }
            }

            @Override
            public void onFailure(Call<List<GiangVienModel>> call, Throwable t) {
                showEmptyDataMessage();
            }
        });
    }

    private void showEmptyDataMessage() {
        if (teacherContainer == null) return;

        teacherContainer.removeAllViews();

        TextView tvEmpty = new TextView(this);
        tvEmpty.setText("Không có dữ liệu bảng điểm");
        tvEmpty.setTextSize(16);
        tvEmpty.setPadding(0, 50, 0, 50);
        tvEmpty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvEmpty.setTextColor(getResources().getColor(android.R.color.darker_gray));

        teacherContainer.addView(tvEmpty);
    }

    private void displayGiangVien(List<GiangVienModel> list) {
        // Cập nhật số thống kê
        TextView tvStatGv = findViewById(R.id.tv_stat_gv);
        if (tvStatGv != null) tvStatGv.setText(String.valueOf(list.size()));

        // Hiển thị danh sách GV động
        teacherContainer.removeAllViews();

        int[] colors = {R.color.card_tkb, R.color.card_khoa, R.color.card_phongdaotao,
                R.color.card_bangdiem, R.color.card_hoidap, R.color.card_thongtin};

        for (int i = 0; i < list.size(); i++) {
            GiangVienModel gv = list.get(i);
            View row = getLayoutInflater().inflate(R.layout.item_teacher_row, teacherContainer, false);

            ((TextView) row.findViewById(R.id.tv_teacher_name)).setText(gv.getFullName());
            String gvInfo = gv.getDonVi() != null ? gv.getDonVi() : "Giảng viên";
            if (gv.getEmail1() != null && !gv.getEmail1().isEmpty()) {
                gvInfo += " | " + gv.getEmail1() + " | " + gv.getDienThoai();
            }
            ((TextView) row.findViewById(R.id.tv_teacher_role)).setText(gvInfo);

            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);
            bg.setColor(ContextCompat.getColor(this, colors[i % colors.length]));
            row.findViewById(R.id.teacher_avatar_bg).setBackground(bg);

            teacherContainer.addView(row);
        }
    }

    private void loadTinTuc() {
        ApiClient.getApiService(this).getTinTuc().enqueue(new Callback<List<ThongBaoModel>>() {
            @Override
            public void onResponse(Call<List<ThongBaoModel>> call, Response<List<ThongBaoModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayTinTuc(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoModel>> call, Throwable t) {}
        });
    }

    private void displayTinTuc(List<ThongBaoModel> list) {
        LinearLayout newsContainer = findViewById(R.id.news_container);
        if (newsContainer == null) return;
        newsContainer.removeAllViews();

        for (ThongBaoModel tt : list) {
            CardView card = new CardView(this);
            CardView.LayoutParams cardLp = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
            cardLp.bottomMargin = 20;
            card.setLayoutParams(cardLp);
            card.setRadius(28);
            card.setCardElevation(6);
            card.setContentPadding(32, 28, 32, 28);
            card.setCardBackgroundColor(getResources().getColor(R.color.bg_card));

            LinearLayout inner = new LinearLayout(this);
            inner.setOrientation(LinearLayout.VERTICAL);

            TextView tvTitle = new TextView(this);
            tvTitle.setText(tt.getTieuDe());
            tvTitle.setTextColor(getResources().getColor(R.color.text_primary));
            tvTitle.setTextSize(14);
            tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            tvTitle.setPadding(0, 0, 0, 8);
            inner.addView(tvTitle);

            TextView tvContent = new TextView(this);
            tvContent.setText(tt.getNoiDung());
            tvContent.setTextColor(getResources().getColor(R.color.text_secondary));
            tvContent.setTextSize(13);
            tvContent.setPadding(0, 0, 0, 12);
            inner.addView(tvContent);

            LinearLayout metaRow = new LinearLayout(this);
            metaRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
            TextView tvMeta = new TextView(this);
            String meta = (tt.getNguoiDang() != null ? tt.getNguoiDang() : "Khoa CNTT");
            if (tt.getNgayGui() != null) meta += " | " + tt.getNgayGui().substring(0, Math.min(10, tt.getNgayGui().length()));
            tvMeta.setText(meta);
            tvMeta.setTextColor(getResources().getColor(R.color.text_hint));
            tvMeta.setTextSize(11);
            metaRow.addView(tvMeta);
            inner.addView(metaRow);

            card.addView(inner);
            newsContainer.addView(card);
        }
    }

    private void setupContactRow(int viewId, int iconRes, String label, String value, int bgColorRes) {
        View row = findViewById(viewId);
        ((ImageView) row.findViewById(R.id.iv_contact_icon)).setImageResource(iconRes);
        ((TextView) row.findViewById(R.id.tv_contact_label)).setText(label);
        ((TextView) row.findViewById(R.id.tv_contact_value)).setText(value);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(ContextCompat.getColor(this, bgColorRes));
        row.findViewById(R.id.contact_icon_bg).setBackground(bg);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

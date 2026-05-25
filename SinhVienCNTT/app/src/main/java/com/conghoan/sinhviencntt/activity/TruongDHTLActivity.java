package com.conghoan.sinhviencntt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.ThongBaoModel;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TruongDHTLActivity extends AppCompatActivity {

    private LinearLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truong);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        contentContainer = findViewById(R.id.content_container);
        loadThongBao();
    }

    private void loadThongBao() {
        ApiClient.getApiService(this).getThongBao().enqueue(new Callback<List<ThongBaoModel>>() {
            @Override
            public void onResponse(Call<List<ThongBaoModel>> call, Response<List<ThongBaoModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    displayThongBao(response.body());
                } else {
                    showEmpty();
                }
            }

            @Override
            public void onFailure(Call<List<ThongBaoModel>> call, Throwable t) {
                showEmpty();
            }
        });
    }

    private void displayThongBao(List<ThongBaoModel> list) {
        if (contentContainer == null) return;
        contentContainer.removeAllViews();

        for (ThongBaoModel tb : list) {
            CardView card = new CardView(this);
            card.setRadius(36);
            card.setCardElevation(4);
            card.setUseCompatPadding(true);
            card.setContentPadding(32, 24, 32, 24);

            LinearLayout inner = new LinearLayout(this);
            inner.setOrientation(LinearLayout.VERTICAL);

            if (Boolean.TRUE.equals(tb.getGhim())) {
                TextView pin = new TextView(this);
                pin.setText("📌 Ghim");
                pin.setTextSize(11);
                pin.setTextColor(getResources().getColor(R.color.card_bangdiem_icon));
                inner.addView(pin);
            }

            TextView title = new TextView(this);
            title.setText(tb.getTieuDe());
            title.setTextSize(15);
            title.setTextColor(getResources().getColor(R.color.text_primary));
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            inner.addView(title);

            TextView content = new TextView(this);
            content.setText(tb.getNoiDung());
            content.setTextSize(13);
            content.setTextColor(getResources().getColor(R.color.text_secondary));
            content.setPadding(0, 12, 0, 8);
            inner.addView(content);

            TextView meta = new TextView(this);
            String metaText = (tb.getNguoiGui() != null ? tb.getNguoiGui() : "") +
                    (tb.getNgayGui() != null ? " | " + tb.getNgayGui() : "");
            meta.setText(metaText);
            meta.setTextSize(11);
            meta.setTextColor(getResources().getColor(R.color.text_hint));
            inner.addView(meta);

            card.addView(inner);
            contentContainer.addView(card);
        }
    }

    private void showEmpty() {
        if (contentContainer == null) return;
        contentContainer.removeAllViews();
        TextView empty = new TextView(this);
        empty.setText("Chưa có tin tức / thông báo");
        empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        empty.setPadding(0, 80, 0, 80);
        empty.setTextColor(getResources().getColor(R.color.text_hint));
        contentContainer.addView(empty);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

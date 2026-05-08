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
import com.conghoan.sinhviencntt.model.ThongBaoModel;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhongDaoTaoActivity extends AppCompatActivity {

    private LinearLayout formContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phongdaotao);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        formContainer = findViewById(R.id.form_container);
        loadThongBao();
    }

    private void loadThongBao() {
        ApiClient.getApiService(this).getThongBao().enqueue(new Callback<List<ThongBaoModel>>() {
            @Override
            public void onResponse(Call<List<ThongBaoModel>> call, Response<List<ThongBaoModel>> response) {
                List<ThongBaoModel> filtered = filterPDT(response.body());
                if (!filtered.isEmpty()) {
                    display(filtered);
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

    private List<ThongBaoModel> filterPDT(List<ThongBaoModel> all) {
        List<ThongBaoModel> out = new ArrayList<>();
        if (all == null) return out;
        for (ThongBaoModel tb : all) {
            String nguoi = tb.getNguoiDang() == null ? "" : tb.getNguoiDang().toLowerCase();
            if (nguoi.contains("phòng đào tạo") || nguoi.contains("phong dao tao") || nguoi.contains("pdt")) {
                out.add(tb);
            }
        }
        return out;
    }

    private void display(List<ThongBaoModel> list) {
        if (formContainer == null) return;
        formContainer.removeAllViews();

        int[] icons = {R.drawable.ic_training, R.drawable.ic_info, R.drawable.ic_schedule};
        int[] colors = {R.color.card_phongdaotao, R.color.card_thongtin, R.color.card_tkb};

        for (int i = 0; i < list.size(); i++) {
            ThongBaoModel tb = list.get(i);
            View row = getLayoutInflater().inflate(R.layout.item_form_row, formContainer, false);
            ((ImageView) row.findViewById(R.id.iv_form_icon)).setImageResource(icons[i % icons.length]);
            ((TextView) row.findViewById(R.id.tv_form_name)).setText(tb.getTieuDe());
            ((TextView) row.findViewById(R.id.tv_form_desc)).setText(tb.getNoiDung());

            GradientDrawable bg = new GradientDrawable();
            bg.setShape(GradientDrawable.OVAL);
            bg.setColor(ContextCompat.getColor(this, colors[i % colors.length]));
            row.findViewById(R.id.form_icon_bg).setBackground(bg);

            formContainer.addView(row);
        }
    }

    private void showEmpty() {
        if (formContainer == null) return;
        formContainer.removeAllViews();
        TextView tv = new TextView(this);
        tv.setText("Chưa có thông báo từ Phòng Đào tạo");
        tv.setPadding(40, 80, 40, 80);
        tv.setTextSize(16);
        formContainer.addView(tv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

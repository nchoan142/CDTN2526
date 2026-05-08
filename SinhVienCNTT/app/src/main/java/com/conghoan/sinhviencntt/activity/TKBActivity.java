package com.conghoan.sinhviencntt.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.conghoan.sinhviencntt.R;
import com.conghoan.sinhviencntt.model.ThoiKhoaBieuModel;
import com.conghoan.sinhviencntt.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TKBActivity extends AppCompatActivity {

    private LinearLayout tkbContainer;
    private int selectedThu = 2;
    private List<ThoiKhoaBieuModel> allData = new ArrayList<>();
    private TextView[] tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkb);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        tkbContainer = findViewById(R.id.tkb_container);
        setupDayTabs();
        loadTKB();
    }

    private void setupDayTabs() {
        tabs = new TextView[]{
                findViewById(R.id.tab_t2), findViewById(R.id.tab_t3),
                findViewById(R.id.tab_t4), findViewById(R.id.tab_t5),
                findViewById(R.id.tab_t6), findViewById(R.id.tab_t7)
        };
        int[] thuValues = {2, 3, 4, 5, 6, 7};

        for (int i = 0; i < tabs.length; i++) {
            final int thu = thuValues[i];
            tabs[i].setOnClickListener(v -> {
                selectedThu = thu;
                updateTabUI();
                filterAndDisplay();
            });
        }
    }

    private void updateTabUI() {
        for (int i = 0; i < tabs.length; i++) {
            int thu = i + 2;
            if (thu == selectedThu) {
                tabs[i].setBackgroundResource(R.drawable.bg_tab_active);
                tabs[i].setTextColor(0xFFFFFFFF);
                tabs[i].setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                tabs[i].setBackgroundResource(R.drawable.bg_tab_inactive);
                tabs[i].setTextColor(getResources().getColor(R.color.text_secondary));
                tabs[i].setTypeface(null, android.graphics.Typeface.NORMAL);
            }
        }
    }

    private void loadTKB() {
        SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
        String msv = prefs.getString("msv", "");

        ApiClient.getApiService(this).getTKB(msv).enqueue(new Callback<List<ThoiKhoaBieuModel>>() {
            @Override
            public void onResponse(Call<List<ThoiKhoaBieuModel>> call, Response<List<ThoiKhoaBieuModel>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    allData = response.body();
                    filterAndDisplay();
                } else {
                    setupMockData();
                }
            }

            @Override
            public void onFailure(Call<List<ThoiKhoaBieuModel>> call, Throwable t) {
                setupMockData();
            }
        });
    }

    private void filterAndDisplay() {
        List<ThoiKhoaBieuModel> filtered = new ArrayList<>();
        for (ThoiKhoaBieuModel tkb : allData) {
            if (tkb.getThu() != null && tkb.getThu() == selectedThu) {
                filtered.add(tkb);
            }
        }
        displayTKB(filtered);
    }

    private void displayTKB(List<ThoiKhoaBieuModel> list) {
        tkbContainer.removeAllViews();

        if (list.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Không có lịch học ngày này");
            empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            empty.setPadding(0, 60, 0, 60);
            empty.setTextColor(getResources().getColor(R.color.text_hint));
            tkbContainer.addView(empty);
            return;
        }

        for (ThoiKhoaBieuModel tkb : list) {
            View slot = getLayoutInflater().inflate(R.layout.item_tkb_slot, tkbContainer, false);
            ((TextView) slot.findViewById(R.id.tv_subject)).setText(tkb.getTenHocPhan());
            ((TextView) slot.findViewById(R.id.tv_teacher)).setText(tkb.getTenGiangVien());
            ((TextView) slot.findViewById(R.id.tv_room)).setText(tkb.getPhongHoc());
            ((TextView) slot.findViewById(R.id.tv_period_num)).setText(tkb.getTietBatDau() + "-" + tkb.getTietKetThuc());
            ((TextView) slot.findViewById(R.id.tv_time)).setText(tkb.getGioBatDau());
            tkbContainer.addView(slot);
        }
    }

    private void setupMockData() {
        tkbContainer.removeAllViews();
        addMockSlot("Lập trình Java nâng cao", "ThS. Nguyễn Văn Hùng", "Phòng A3-302", "1-3", "7:00");
        addMockSlot("Cơ sở dữ liệu", "TS. Trần Thị Mai", "Phòng B2-105", "4-6", "9:30");
        addMockSlot("Mạng máy tính", "ThS. Lê Đức Anh", "Phòng C1-201", "7-9", "13:00");
    }

    private void addMockSlot(String subject, String teacher, String room, String period, String time) {
        View slot = getLayoutInflater().inflate(R.layout.item_tkb_slot, tkbContainer, false);
        ((TextView) slot.findViewById(R.id.tv_subject)).setText(subject);
        ((TextView) slot.findViewById(R.id.tv_teacher)).setText(teacher);
        ((TextView) slot.findViewById(R.id.tv_room)).setText(room);
        ((TextView) slot.findViewById(R.id.tv_period_num)).setText(period);
        ((TextView) slot.findViewById(R.id.tv_time)).setText(time);
        tkbContainer.addView(slot);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

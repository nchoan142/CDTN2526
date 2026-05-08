package com.conghoan.sinhviencntt.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.conghoan.sinhviencntt.R;

public abstract class BaseDetailActivity extends AppCompatActivity {

    protected TextView tvPageTitle, tvPageName, tvPageDescription;
    protected ImageView ivPageIcon, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvPageTitle = findViewById(R.id.tv_page_title);
        tvPageName = findViewById(R.id.tv_page_name);
        tvPageDescription = findViewById(R.id.tv_page_description);
        ivPageIcon = findViewById(R.id.iv_page_icon);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            finish();
            overridePendingTransition(0, R.anim.slide_out_left);
        });

        setupContent();
    }

    protected abstract void setupContent();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_left);
    }
}

package com.conghoan.sinhviencntt.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.conghoan.sinhviencntt.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Animate splash content
        LinearLayout splashContent = findViewById(R.id.splash_content);
        splashContent.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_in));

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Check if user is logged in
            SharedPreferences prefs = getSharedPreferences("SinhVienCNTT", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            Intent intent;
            if (isLoggedIn) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, 0);
            finish();
        }, SPLASH_DELAY);
    }
}

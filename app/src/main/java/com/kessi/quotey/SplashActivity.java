package com.kessi.quotey;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.kessi.quotey.util.Animatee;
import com.kessi.quotey.util.KSUtil;
import com.kessi.quotey.util.Render;
import com.kessi.quotey.util.SharedPrefs;
import com.kessi.quotey.util.Utills;

public class SplashActivity extends AppCompatActivity {

    ImageView icon, bg;
    LinearLayout iconLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppCompatDelegate.setDefaultNightMode(SharedPrefs.getAppNightDayMode(this));

        if (Utills.getStatusBarHeight(SplashActivity.this) > Utills.convertDpToPixel(24)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        icon = findViewById(R.id.icon);
        Glide.with(this)
                .load(R.drawable.sp_logo)
                .into(icon);

        bg = findViewById(R.id.bg);
        Glide.with(this)
                .load(R.drawable.sp_bg)
                .into(bg);


        iconLay = findViewById(R.id.iconLay);
        new Handler().postDelayed(() -> {
            iconLay.setVisibility(View.VISIBLE);
            bg.setVisibility(View.VISIBLE);
            // Set Animation
            Render render = new Render(SplashActivity.this);
            render.setAnimation(KSUtil.In(iconLay));
            render.start();
        },2000);


        new Handler().postDelayed(() -> {
            // Set Animation
            Render render = new Render(SplashActivity.this);
            render.setAnimation(KSUtil.InDown(bg));
            render.start();
        },1000);

        new Handler().postDelayed(() -> {
            // Set Animation
            Render render = new Render(SplashActivity.this);
            render.setAnimation(KSUtil.Tada(iconLay));
            render.start();
        },3000);


        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            Animatee.animateSlideUp(SplashActivity.this);
            finish();
        }, 4000);
    }

    @Override
    public void onBackPressed() {

    }
}

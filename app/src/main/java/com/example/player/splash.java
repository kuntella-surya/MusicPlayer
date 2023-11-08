package com.example.player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.loadinganimation.LoadingAnimation;

public class splash extends AppCompatActivity {
    Handler handler;
    LoadingAnimation loadingAnim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadingAnim = findViewById(R.id.loadingAnim);
        loadingAnim.setProgressVector(getResources().getDrawable(com.example.loadinganimation.R.drawable.color_capsule));
        loadingAnim.setTextViewVisibility(true);
        loadingAnim.setTextStyle(true);
        loadingAnim.setTextColor(Color.YELLOW);
        loadingAnim.setTextSize(12F);
        loadingAnim.setTextMsg("Please Wait");
        loadingAnim.setEnlarge(5);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            int start = Color.parseColor("#3498db");
            int end = Color.parseColor("#e74c3c");
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR,new int[]{start,end});
            LayerDrawable layerDrawable =new LayerDrawable(new Drawable[]{gradientDrawable});
            Drawable drawable = getResources().getDrawable(R.drawable.music_bg);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setBackgroundDrawable(layerDrawable);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashintent = new Intent(splash.this,MainActivity.class);
                startActivity(splashintent);
            }
        },500);
    }
}
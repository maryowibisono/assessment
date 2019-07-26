package com.maryow.assessment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.maryow.assessment.R;
import com.maryow.assessment.config.Config;

public class SplashScreenActivity extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onPrepare() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, Config.SPLASH_TIME_OUT);
    }
}
package com.maryow.assessment.activity.news;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.view.news.SplashScreenView;

public class SplashScreenActivity extends BaseActivity<SplashScreenView> {

    @Override
    public SplashScreenView setViewHolder(View parent) {
        return new SplashScreenView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_splash_screen;
    }

    @Override
    protected void onPrepare(SplashScreenView holder) {
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

package com.maryow.assessment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.maryow.assessment.R;

public class NewsActivity extends BaseActivity {


    @Override
    protected int initLayout() {
        return R.layout.activity_news;
    }

    @Override
    protected void onPrepare() {
        Toast.makeText(this, getIntent().getStringExtra("source"), Toast.LENGTH_LONG).show();

    }
}

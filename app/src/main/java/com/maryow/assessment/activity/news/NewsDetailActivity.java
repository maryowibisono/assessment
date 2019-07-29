package com.maryow.assessment.activity.news;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.component.Loading;

public class NewsDetailActivity extends BaseActivity {

    WebView wvNewsDetail;
    ImageButton  ibtnBack;

    @Override
    protected int initLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void onPrepare() {
        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Loading.showLoading(this);
        wvNewsDetail =  findViewById(R.id.wvNewsDetail);
        wvNewsDetail.getSettings().setJavaScriptEnabled(true);
        wvNewsDetail.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Loading.cancelLoading();
            }
        });
        String url = getIntent().getStringExtra("url");
        wvNewsDetail.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(wvNewsDetail.canGoBack()){
            wvNewsDetail.goBack();
        }else{
            super.onBackPressed();
        }

    }
}

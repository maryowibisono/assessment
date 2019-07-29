package com.maryow.assessment.activity.news;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.view.news.NewsDetail;

public class NewsDetailActivity extends BaseActivity<NewsDetail> {

    @Override
    public NewsDetail setViewHolder(View parent) {
        return new NewsDetail(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void onPrepare(NewsDetail holder) {
        holder.ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Loading.showLoading(this);
        holder.wvNewsDetail =  findViewById(R.id.wvNewsDetail);
        holder.wvNewsDetail.getSettings().setJavaScriptEnabled(true);
        holder.wvNewsDetail.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                Loading.cancelLoading();
            }
        });
        String url = getIntent().getStringExtra("url");
        holder.wvNewsDetail.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(viewHolder.wvNewsDetail.canGoBack()){
            viewHolder.wvNewsDetail.goBack();
        }else{
            super.onBackPressed();
        }

    }
}

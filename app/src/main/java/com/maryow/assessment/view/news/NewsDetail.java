package com.maryow.assessment.view.news;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.maryow.assessment.R;
import com.maryow.assessment.view.BaseView;

public class NewsDetail extends BaseView {
    public WebView wvNewsDetail;

    public NewsDetail(View view) {
        super(view);
        wvNewsDetail =  view.findViewById(R.id.wvNewsDetail);
    }
}

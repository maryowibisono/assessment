package com.maryow.assessment.view.news;

import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.news.NewsActivity;
import com.maryow.assessment.adapter.news.NewsAdapter;
import com.maryow.assessment.view.BaseView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class NewsView extends BaseView {
    public View ftView;
    public Handler mHandler;
    public ListView lvNews;
    public NewsAdapter newsAdapter;
    public int page = 1;
    public boolean isLoading = false;
    public SwipeRefreshLayout srlNews;
    public MaterialSearchView msvSearch;
    public String domain, query;
    public LinearLayout llEmptyNews;

    public NewsView(View view) {
        super(view);
        lvNews = view.findViewById(R.id.lvNews);
        srlNews = view.findViewById(R.id.srlNews);
        msvSearch =  view.findViewById(R.id.msvSearch);
        llEmptyNews =  view.findViewById(R.id.llEmptyNews);
    }
}

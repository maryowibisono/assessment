package com.maryow.assessment.view.news;

import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.news.SourceActivity;
import com.maryow.assessment.adapter.news.SourceAdapter;
import com.maryow.assessment.view.BaseView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class SourceView extends BaseView {
    public View ftView;
    public Handler mHandler;
    public ListView lvSource;
    public SourceAdapter sourceAdapter;
    public int page = 1;
    public boolean isLoading = false;
    public SwipeRefreshLayout srlSource;
    public MaterialSearchView msvSource;

    public SourceView(View view) {
        super(view);
        lvSource = view.findViewById(R.id.lvSource);
        srlSource = view.findViewById(R.id.srlSource);
        msvSource =  view.findViewById(R.id.msvSource);
    }
}

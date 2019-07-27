package com.maryow.assessment.activity;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.maryow.assessment.R;
import com.maryow.assessment.adapter.SourceAdapter;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.model.news.Source;
import com.maryow.assessment.util.CommonUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class SourceActivity extends BaseActivity {

    View ftView;
    Handler mHandler;
    ListView lvSource;
    SourceAdapter sourceAdapter;
    int page = 1;
    boolean isLoading = false;
    SwipeRefreshLayout srlSource;
    ImageButton ibtnBack;
    MaterialSearchView msvSource;

    @Override
    protected int initLayout() {
        return R.layout.activity_source;
    }

    @Override
    protected void onPrepare() {
        loadSourceByCategory();
    }

    private void loadSourceByCategory() {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.adapter_source_loading, null);
        mHandler = new MyHandler();
        lvSource = findViewById(R.id.lvSource);
        srlSource = findViewById(R.id.srlSource);
        ibtnBack = findViewById(R.id.ibtnBack);
        msvSource =  findViewById(R.id.msvSource);
        btnListener();
        getLoadData();
        pullToRefresh();
    }

    private void btnListener() {
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lvSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Source source = (Source) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(SourceActivity.this, NewsActivity.class);
                intent.putExtra("source", source.getId());
                startActivity(intent);
            }
        });
    }

    private void pullToRefresh() {

        srlSource.setColorSchemeResources(R.color.colorPrimary);
        srlSource.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getLoadData();
            }
        });

    }

    private void getLoadData() {
        Loading.showLoading(this);
        lvSource.setAdapter(null);
        NewsApi.sourcesByCategory(this, getIntent().getStringExtra("category"), new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                sourceAdapter = new SourceAdapter(SourceActivity.this, CommonUtil.getPage(form.getSources(), page, 10));
                lvSource.setAdapter(sourceAdapter);
                lvSource.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        final int lastItem = firstVisibleItem + visibleItemCount;
                        if (lastItem == totalItemCount && isLoading == false) {
                            isLoading = true;
                            loadMoreData();
                        }
                    }
                });

                srlSource.setRefreshing(false);
                Loading.cancelLoading();
            }


            @Override
            public void onFailed(Form form) {

            }
        });

    }

    private void loadMoreData() {
        mHandler.sendEmptyMessage(0);
        NewsApi.sourcesByCategory(SourceActivity.this, getIntent().getStringExtra("category"), new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = mHandler.obtainMessage(1, CommonUtil.getPage(form.getSources(), page + 1, 10));
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {

            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lvSource.addFooterView(ftView);
                    break;
                case 1:
                    List<Source> listInbox = (List<Source>) msg.obj;
                    if (listInbox != null && listInbox.size() > 0) {
                        sourceAdapter.addListItemToAdapter((List<Source>) msg.obj);
                        lvSource.removeFooterView(ftView);
                        page++;
                        isLoading = false;
                    } else {
                        lvSource.removeFooterView(ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}

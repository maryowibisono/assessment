package com.maryow.assessment.activity.news;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.adapter.news.SourceAdapter;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.model.news.Source;
import com.maryow.assessment.util.CommonUtil;
import com.maryow.assessment.view.news.SourceView;

import java.util.List;

public class SourceActivity extends BaseActivity<SourceView> {

    @Override
    public SourceView setViewHolder(View parent) {
        return new SourceView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_source;
    }

    @Override
    protected void onPrepare(SourceView holder) {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder.ftView = li.inflate(R.layout.adapter_source_loading, null);
        holder.mHandler = new SourceActivity.MyHandler();
        btnListener();
        getLoadData();
        pullToRefresh();

    }

    private void btnListener() {
        viewHolder.ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewHolder.lvSource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Source source = (Source) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(SourceActivity.this, NewsActivity.class);
                intent.putExtra("source", source.getUrl());
                startActivity(intent);
            }
        });
    }

    private void pullToRefresh() {
        viewHolder.srlSource.setColorSchemeResources(R.color.colorPrimary);
        viewHolder.srlSource.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewHolder.page = 1;
                getLoadData();
            }
        });

    }

    private void getLoadData() {
        Loading.showLoading(this);
        viewHolder.lvSource.setAdapter(null);
        NewsApi.sourcesByCategory(this, getIntent().getStringExtra("category"), new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                viewHolder.sourceAdapter = new SourceAdapter(SourceActivity.this,
                        CommonUtil.getPage(form.getSources(), viewHolder.page, 10));
                viewHolder.lvSource.setAdapter(viewHolder.sourceAdapter);
                viewHolder.lvSource.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        final int lastItem = firstVisibleItem + visibleItemCount;
                        if (lastItem == totalItemCount && viewHolder.isLoading == false) {
                            viewHolder.isLoading = true;
                            loadMoreData();
                        }
                    }
                });

                viewHolder.srlSource.setRefreshing(false);
                Loading.cancelLoading();
            }


            @Override
            public void onFailed(Form form) {
                onError(SourceActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });

    }

    private void loadMoreData() {
        viewHolder.mHandler.sendEmptyMessage(0);
        NewsApi.sourcesByCategory(SourceActivity.this, getIntent().getStringExtra("category"), new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = viewHolder.mHandler.obtainMessage(1, CommonUtil.getPage(form.getSources(), viewHolder.page + 1, 10));
                viewHolder.mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {
                onError(SourceActivity.this, form.getErrDesc());
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    viewHolder.lvSource.addFooterView(viewHolder.ftView);
                    break;
                case 1:
                    List<Source> sourceList = (List<Source>) msg.obj;
                    if (sourceList != null && sourceList.size() > 0) {
                        viewHolder.sourceAdapter.addListItemToAdapter((List<Source>) msg.obj);
                        viewHolder.lvSource.removeFooterView(viewHolder.ftView);
                        viewHolder.page++;
                        viewHolder.isLoading = false;
                    } else {
                        viewHolder.lvSource.removeFooterView(viewHolder.ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}

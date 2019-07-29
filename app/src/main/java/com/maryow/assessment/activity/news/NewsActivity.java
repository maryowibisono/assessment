package com.maryow.assessment.activity.news;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.adapter.news.NewsAdapter;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.news.Article;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.util.CommonUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class NewsActivity extends BaseActivity {
    View ftView;
    Handler mHandler;
    ListView lvNews;
    NewsAdapter newsAdapter;
    int page = 1;
    boolean isLoading = false;
    SwipeRefreshLayout srlNews;
    ImageButton ibtnBack, ibtnSearch;
    MaterialSearchView msvSearch;
    String domain, query;
    LinearLayout llEmptyNews;

    @Override
    protected int initLayout() {
        return R.layout.activity_news;
    }

    @Override
    protected void onPrepare() {
        loadNewsByDomain();
    }

    private void loadNewsByDomain() {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.adapter_source_loading, null);
        mHandler = new MyHandler();
        lvNews = findViewById(R.id.lvNews);
        srlNews = findViewById(R.id.srlNews);
        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnSearch = findViewById(R.id.ibtnSearch);
        msvSearch =  findViewById(R.id.msvSearch);
        llEmptyNews =  findViewById(R.id.llEmptyNews);
        btnListener();
        loadDataArticle(null);
        pullToRefresh();
    }

    private void pullToRefresh() {
        srlNews.setColorSchemeResources(R.color.colorPrimary);
        srlNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadDataArticle(null);
            }
        });
    }

    private void loadDataArticle(String query) {
        Loading.showLoading(this);
        lvNews.setAdapter(null);
        if(getIntent().getStringExtra("query") != null){
            domain = "";
            if (query == null) {
                this.query = getIntent().getStringExtra("query");
            }else{
                this.query = query;
            }
        }else{
            domain = CommonUtil.removeHttp(getIntent().getStringExtra("source"));
            this.query = query;
        }

        NewsApi.everythingByDomainAndQuery(this, domain, this.query, new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getArticles().size() > 0) {
                    newsAdapter = new NewsAdapter(NewsActivity.this, CommonUtil.getPage(form.getArticles(), page, 10));
                    lvNews.setAdapter(newsAdapter);
                    lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                    lvNews.setVisibility(View.VISIBLE);
                    llEmptyNews.setVisibility(View.GONE);
                }else{
                    lvNews.setVisibility(View.GONE);
                    llEmptyNews.setVisibility(View.VISIBLE);
                }


                srlNews.setRefreshing(false);
                Loading.cancelLoading();
            }


            @Override
            public void onFailed(Form form) {

            }
        });
    }

    private void loadMoreData() {
        mHandler.sendEmptyMessage(0);
        NewsApi.everythingByDomainAndQuery(NewsActivity.this, domain, query, new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = mHandler.obtainMessage(1, CommonUtil.getPage(form.getArticles(), page + 1, 10));
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {

            }
        });
    }

    private void btnListener() {
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msvSearch.showSearch();
            }
        });

        msvSearch.setSubmitOnClick(true);
        msvSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadDataArticle(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lvNews.addFooterView(ftView);
                    break;
                case 1:
                    List<Article> listArticle = (List<Article>) msg.obj;
                    if (listArticle != null && listArticle.size() > 0) {
                        newsAdapter.addListItemToAdapter((List<Article>) msg.obj);
                        lvNews.removeFooterView(ftView);
                        page++;
                        isLoading = false;
                    } else {
                        lvNews.removeFooterView(ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

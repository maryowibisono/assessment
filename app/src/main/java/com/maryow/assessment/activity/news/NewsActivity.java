package com.maryow.assessment.activity.news;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.activity.movie.MovieReviewActivity;
import com.maryow.assessment.adapter.news.NewsAdapter;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.news.Article;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.util.CommonUtil;
import com.maryow.assessment.view.news.NewsView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class NewsActivity extends BaseActivity<NewsView> {

    @Override
    public NewsView setViewHolder(View parent) {
        return new NewsView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_news;
    }

    @Override
    protected void onPrepare(NewsView holder) {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder.ftView = li.inflate(R.layout.adapter_source_loading, null);
        holder.mHandler = new NewsActivity.MyHandler();
        btnListener();
        loadDataArticle(null);
        pullToRefresh(holder);
    }


    private void pullToRefresh(final NewsView holder) {
        holder.srlNews.setColorSchemeResources(R.color.colorPrimary);
        holder.srlNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                holder.page = 1;
                loadDataArticle(null);
            }
        });
    }

    private void loadDataArticle(String queryStr) {
        Loading.showLoading(this);
        viewHolder.lvNews.setAdapter(null);
        if (getIntent().getStringExtra("query") != null) {
            getViewHolder().domain = "";
            if (queryStr == null) {
                viewHolder.query = getIntent().getStringExtra("query");
            } else {
                viewHolder.query = queryStr;
            }
        } else {
            viewHolder.domain = CommonUtil.removeHttp(getIntent().getStringExtra("source"));
            viewHolder.query = queryStr;
        }

        NewsApi.everythingByDomainAndQuery(this, viewHolder.domain, viewHolder.query, new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getArticles().size() > 0) {
                    viewHolder.newsAdapter = new NewsAdapter(NewsActivity.this, CommonUtil.getPage(form.getArticles(),  viewHolder.page, 10));
                    viewHolder.lvNews.setAdapter(viewHolder.newsAdapter);
                    viewHolder.lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView absListView, int i) {

                        }

                        @Override
                        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            final int lastItem = firstVisibleItem + visibleItemCount;
                            if (lastItem == totalItemCount && viewHolder.isLoading == false) {
                                viewHolder.isLoading = true;
                                loadMoreData();
                            }
                        }
                    });

                    viewHolder.lvNews.setVisibility(View.VISIBLE);
                    viewHolder.llEmptyNews.setVisibility(View.GONE);
                } else {
                    viewHolder.lvNews.setVisibility(View.GONE);
                    viewHolder.llEmptyNews.setVisibility(View.VISIBLE);
                }


                viewHolder.srlNews.setRefreshing(false);
                Loading.cancelLoading();
            }


            @Override
            public void onFailed(Form form) {
                onError(NewsActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });
    }

    private void loadMoreData() {
        viewHolder.mHandler.sendEmptyMessage(0);
        NewsApi.everythingByDomainAndQuery(NewsActivity.this, viewHolder.domain, viewHolder.query, new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = viewHolder.mHandler.obtainMessage(1, CommonUtil.getPage(form.getArticles(), viewHolder.page + 1, 10));
                viewHolder.mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {
                onError(NewsActivity.this, form.getErrDesc());
            }
        });
    }

    private void btnListener() {
        viewHolder.ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        viewHolder.ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.msvSearch.showSearch();
            }
        });

        viewHolder.msvSearch.setSubmitOnClick(true);
        viewHolder.msvSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
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
                    viewHolder.lvNews.addFooterView(viewHolder.ftView);
                    break;
                case 1:
                    List<Article> listArticle = (List<Article>) msg.obj;
                    if (listArticle != null && listArticle.size() > 0) {
                        viewHolder.newsAdapter.addListItemToAdapter((List<Article>) msg.obj);
                        viewHolder.lvNews.removeFooterView(viewHolder.ftView);
                        viewHolder.page++;
                        viewHolder.isLoading = false;
                    } else {
                        viewHolder.lvNews.removeFooterView(viewHolder.ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

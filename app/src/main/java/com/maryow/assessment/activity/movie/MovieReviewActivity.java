package com.maryow.assessment.activity.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
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
import com.maryow.assessment.activity.news.SourceActivity;
import com.maryow.assessment.adapter.movie.ReviewAdapter;
import com.maryow.assessment.adapter.news.SourceAdapter;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.MovieReview;
import com.maryow.assessment.model.news.Source;
import com.maryow.assessment.util.CommonUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class MovieReviewActivity extends BaseActivity {
    View ftView;
    Handler mHandler;
    ListView lvReview;
    ReviewAdapter reviewAdapter;
    int page = 1;
    boolean isLoading = false;
    SwipeRefreshLayout srlReview;
    ImageButton ibtnBack;
    String movieId;
    LinearLayout llEmptyReview;


    @Override
    protected int initLayout() {
        return R.layout.activity_movie_review;
    }

    @Override
    protected void onPrepare() {
        loadReview();
    }

    private void loadReview() {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.adapter_source_loading, null);
        mHandler = new MovieReviewActivity.MyHandler();
        lvReview = findViewById(R.id.lvReview);
        srlReview = findViewById(R.id.srlReview);
        ibtnBack = findViewById(R.id.ibtnBack);
        llEmptyReview = findViewById(R.id.llEmptyReview);
        movieId = getIntent().getStringExtra("movieId");
        btnListener();
        loadData();
        pullToRefresh();
    }

    private void pullToRefresh() {
        srlReview.setColorSchemeResources(R.color.colorPrimary);
        srlReview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadData();
            }
        });
    }

    private void loadData() {

        Loading.showLoading(this);
        lvReview.setAdapter(null);
        MovieApi.movieReview(this, movieId, page, new MovieApi.Response() {
            @Override
            public void onSuccess(com.maryow.assessment.model.movie.Form form) {
                if (form.getReviews().size() > 0) {
                    reviewAdapter = new ReviewAdapter(MovieReviewActivity.this, form.getReviews());
                    lvReview.setAdapter(reviewAdapter);
                    lvReview.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                    lvReview.setVisibility(View.VISIBLE);
                    llEmptyReview.setVisibility(View.GONE);
                }else{
                    lvReview.setVisibility(View.GONE);
                    llEmptyReview.setVisibility(View.VISIBLE);
                }


                srlReview.setRefreshing(false);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                Loading.cancelLoading();
            }
        });
    }

    private void loadMoreData() {
        mHandler.sendEmptyMessage(0);
        MovieApi.movieReview(MovieReviewActivity.this, movieId, page + 1, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = mHandler.obtainMessage(1, form.getReviews());
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {
                Loading.cancelLoading();
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

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lvReview.addFooterView(ftView);
                    break;
                case 1:
                    List<MovieReview> movieReviews = (List<MovieReview>) msg.obj;
                    if (movieReviews != null && movieReviews.size() > 0) {
                        reviewAdapter.addListItemToAdapter((List<MovieReview>) msg.obj);
                        lvReview.removeFooterView(ftView);
                        page++;
                        isLoading = false;
                    } else {
                        lvReview.removeFooterView(ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

package com.maryow.assessment.activity.movie;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.adapter.movie.ReviewAdapter;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.MovieReview;
import com.maryow.assessment.view.movie.MovieReviewView;

import java.util.List;

public class MovieReviewActivity extends BaseActivity<MovieReviewView> {

    @Override
    public MovieReviewView setViewHolder(View parent) {
        return new MovieReviewView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_movie_review;
    }

    @Override
    protected void onPrepare(MovieReviewView holder) {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder.ftView = li.inflate(R.layout.adapter_source_loading, null);
        holder.mHandler = new MovieReviewActivity.MyHandler();
        holder.movieId = getIntent().getStringExtra("movieId");
        btnListener();
        loadData();
        pullToRefresh();
    }

    private void pullToRefresh() {
        viewHolder.srlReview.setColorSchemeResources(R.color.colorPrimary);
        viewHolder.srlReview.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewHolder.page = 1;
                loadData();
            }
        });
    }

    private void loadData() {
        Loading.showLoading(this);
        viewHolder.lvReview.setAdapter(null);
        MovieApi.movieReview(this, viewHolder.movieId, viewHolder.page, new MovieApi.Response() {
            @Override
            public void onSuccess(com.maryow.assessment.model.movie.Form form) {
                if (form.getReviews().size() > 0) {
                    viewHolder.reviewAdapter = new ReviewAdapter(MovieReviewActivity.this, form.getReviews());
                    viewHolder.lvReview.setAdapter(viewHolder.reviewAdapter);
                    viewHolder.lvReview.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                    viewHolder.lvReview.setVisibility(View.VISIBLE);
                    viewHolder.llEmptyReview.setVisibility(View.GONE);
                }else{
                    viewHolder.lvReview.setVisibility(View.GONE);
                    viewHolder.llEmptyReview.setVisibility(View.VISIBLE);
                }

                viewHolder.srlReview.setRefreshing(false);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieReviewActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });
    }

    private void loadMoreData() {
        viewHolder.mHandler.sendEmptyMessage(0);
        MovieApi.movieReview(MovieReviewActivity.this, viewHolder.movieId, viewHolder.page + 1, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = viewHolder.mHandler.obtainMessage(1, form.getReviews());
                viewHolder.mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieReviewActivity.this, form.getErrDesc());

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

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    viewHolder.lvReview.addFooterView(viewHolder.ftView);
                    break;
                case 1:
                    List<MovieReview> movieReviews = (List<MovieReview>) msg.obj;
                    if (movieReviews != null && movieReviews.size() > 0) {
                        viewHolder.reviewAdapter.addListItemToAdapter((List<MovieReview>) msg.obj);
                        viewHolder.lvReview.removeFooterView(viewHolder.ftView);
                        viewHolder.page++;
                        viewHolder.isLoading = false;
                    } else {
                        viewHolder.lvReview.removeFooterView(viewHolder.ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

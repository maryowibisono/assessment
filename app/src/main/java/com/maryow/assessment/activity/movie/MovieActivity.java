package com.maryow.assessment.activity.movie;

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
import com.maryow.assessment.adapter.movie.MovieAdapter;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.Movie;
import com.maryow.assessment.view.movie.MovieView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class MovieActivity extends BaseActivity<MovieView> {

    @Override
    public MovieView setViewHolder(View parent) {
        return new MovieView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_movie;
    }

    @Override
    protected void onPrepare(MovieView holder) {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holder.ftView = li.inflate(R.layout.adapter_source_loading, null);
        holder.mHandler = new MovieActivity.MyHandler();
        btnListener();
        loadMovie(null);
        pullToRefresh();
    }

    private void pullToRefresh() {
        viewHolder.srlMovie.setColorSchemeResources(R.color.colorPrimary);
        viewHolder.srlMovie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewHolder.page = 1;
                loadMovie(null);
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
                loadMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        viewHolder.lvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (Movie) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                intent.putExtra("movieId", String.valueOf(movie.getId()));
                startActivity(intent);
            }
        });
    }

    private void loadMovie(String query) {
        if (getIntent().getStringExtra("query") != null) {
            if (query == null) {
                viewHolder.query = getIntent().getStringExtra("query");
            } else {
                viewHolder.query = query;
            }
        } else {
            viewHolder.query = query;
        }
        Loading.showLoading(this);
        viewHolder.lvMovie.setAdapter(null);
        String genreStr = getIntent().getStringExtra("genre");
        if (genreStr != null) {
            viewHolder.genre = Integer.parseInt(genreStr);
        }
        MovieApi.movieByGenre(MovieActivity.this, viewHolder.genre, viewHolder.page, viewHolder.query, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getMovies().size() > 0) {
                    viewHolder.movieAdapter = new MovieAdapter(MovieActivity.this, form.getMovies());
                    viewHolder.lvMovie.setAdapter(viewHolder.movieAdapter);
                    viewHolder.lvMovie.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    viewHolder.lvMovie.setVisibility(View.VISIBLE);
                    viewHolder.llEmptyMovie.setVisibility(View.GONE);
                } else {
                    viewHolder.lvMovie.setVisibility(View.GONE);
                    viewHolder.llEmptyMovie.setVisibility(View.VISIBLE);
                }
                viewHolder.srlMovie.setRefreshing(false);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });
    }

    private void loadMoreData() {
        viewHolder.mHandler.sendEmptyMessage(0);
        MovieApi.movieByGenre(MovieActivity.this, viewHolder.genre, viewHolder.page + 1, viewHolder.query, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = viewHolder.mHandler.obtainMessage(1, form.getMovies());
                viewHolder.mHandler.sendMessage(msg);
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieActivity.this, form.getErrDesc());
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    viewHolder.lvMovie.addFooterView(viewHolder.ftView);
                    break;
                case 1:
                    List<Movie> movieList = (List<Movie>) msg.obj;
                    if (movieList != null && movieList.size() > 0) {
                        viewHolder.movieAdapter.addListItemToAdapter((List<Movie>) msg.obj);
                        viewHolder.lvMovie.removeFooterView(viewHolder.ftView);
                        viewHolder.page++;
                        viewHolder.isLoading = false;
                    } else {
                        viewHolder.lvMovie.removeFooterView(viewHolder.ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

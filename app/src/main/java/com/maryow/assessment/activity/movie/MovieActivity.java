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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.adapter.movie.MovieAdapter;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.Movie;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class MovieActivity extends BaseActivity {
    View ftView;
    Handler mHandler;
    ListView lvMovie;
    MovieAdapter movieAdapter;
    int page = 1;
    boolean isLoading = false;
    SwipeRefreshLayout srlMovie;
    ImageButton ibtnBack, ibtnSearch;
    MaterialSearchView msvSearch;
    LinearLayout llEmptyMovie;
    int genre;
    String query;

    @Override
    protected int initLayout() {
        return R.layout.activity_movie;
    }

    @Override
    protected void onPrepare() {
        loadMovieByGenre();
      }

    private void loadMovieByGenre() {
        LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.adapter_source_loading, null);
        mHandler = new MovieActivity.MyHandler();
        lvMovie = findViewById(R.id.lvMovie);
        srlMovie = findViewById(R.id.srlMovie);
        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnSearch = findViewById(R.id.ibtnSearch);
        msvSearch =  findViewById(R.id.msvSearch);
        llEmptyMovie =  findViewById(R.id.llEmptyMovie);
        btnListener();
        loadMovie(null);
        pullToRefresh();
    }

    private void pullToRefresh() {
        srlMovie.setColorSchemeResources(R.color.colorPrimary);
        srlMovie.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadMovie(null);
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
                loadMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        lvMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        this.query = query;
        Loading.showLoading(this);
        lvMovie.setAdapter(null);
        String genreStr = getIntent().getStringExtra("genre");
        genre = Integer.parseInt(genreStr);
        MovieApi.movieByGenre(MovieActivity.this, genre, page, query, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getMovies().size() > 0) {
                movieAdapter = new MovieAdapter(MovieActivity.this, form.getMovies());
                lvMovie.setAdapter(movieAdapter);
                lvMovie.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    lvMovie.setVisibility(View.VISIBLE);
                    llEmptyMovie.setVisibility(View.GONE);
                }else{
                    lvMovie.setVisibility(View.GONE);
                    llEmptyMovie.setVisibility(View.VISIBLE);
                }
                srlMovie.setRefreshing(false);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {

            }
        });
    }

    private void loadMoreData() {
        mHandler.sendEmptyMessage(0);
        MovieApi.movieByGenre(MovieActivity.this, genre, page + 1, query, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                Message msg = mHandler.obtainMessage(1, form.getMovies());
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
                    lvMovie.addFooterView(ftView);
                    break;
                case 1:
                    List<Movie> movieList = (List<Movie>) msg.obj;
                    if (movieList != null && movieList.size() > 0) {
                        movieAdapter.addListItemToAdapter((List<Movie>) msg.obj);
                        lvMovie.removeFooterView(ftView);
                        page++;
                        isLoading = false;
                    } else {
                        lvMovie.removeFooterView(ftView);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

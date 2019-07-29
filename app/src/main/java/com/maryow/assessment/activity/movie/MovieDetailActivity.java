package com.maryow.assessment.activity.movie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

public class MovieDetailActivity extends BaseActivity {
    YouTubePlayerView youtubePlayer;
    String movieId;
    Video youtubeVideo;
    TextView tvMovieTitle, tvMovieOverview, tvMovieRate, tvRateCount, tvMovieRuntime, tvMovieLanguage,
            tvSeeAll, tvReview, tvAuthor, tvAuthorBy;
    ImageView ivMovie;
    SwipeRefreshLayout srlMovieDetail;
    ImageButton ibtnBack;


    @Override
    protected int initLayout() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void onPrepare() {
        getDataView();
        getBtnAction();
        loadVideo();
        loadDetailMovie();
        loadFirstReview();
        pullToRefresh();
    }

    private void getBtnAction() {
        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this, MovieReviewActivity.class);
                intent.putExtra("movieId", movieId);
                startActivity(intent);
            }
        });
    }

    private void pullToRefresh() {
        srlMovieDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVideo();
                loadDetailMovie();
                loadFirstReview();
            }
        });
    }


    private void getDataView() {
        movieId = getIntent().getStringExtra("movieId");
        youtubePlayer = findViewById(R.id.youtubePlayer);
        getLifecycle().addObserver(youtubePlayer);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieOverview = findViewById(R.id.tvMovieOverview);
        tvMovieRate = findViewById(R.id.tvMovieRate);
        tvRateCount = findViewById(R.id.tvRateCount);
        tvMovieRuntime = findViewById(R.id.tvMovieRuntime);
        tvMovieLanguage = findViewById(R.id.tvMovieLanguage);
        ivMovie = findViewById(R.id.ivMovie);
        tvSeeAll = findViewById(R.id.tvSeeAll);
        tvReview = findViewById(R.id.tvReview);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvAuthorBy = findViewById(R.id.tvAuthorBy);
        srlMovieDetail = findViewById(R.id.srlMovieDetail);
        ibtnBack = findViewById(R.id.ibtnBack);
    }

    private void loadVideo() {
        Loading.showLoading(this);
        MovieApi.movieVideo(this, movieId, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if(form.getVideos().size() > 0){

                    for(Video v : form.getVideos()){
                        if(v.getSite().equalsIgnoreCase("YouTube")){
                            youtubeVideo = v;
                            break;
                        }
                    }


                    youtubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                            String videoId = youtubeVideo.getKey();
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });


                    youtubePlayer.setVisibility(View.VISIBLE);
                }else{

                    youtubePlayer.setVisibility(View.GONE);
                }



                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                Loading.cancelLoading();
            }
        });
    }

    private void loadDetailMovie() {
        Loading.showLoading(this);
        MovieApi.movieDetail(this, movieId, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                tvMovieTitle.setText(form.getMovie().getTitle());
                tvMovieOverview.setText(form.getMovie().getOverview());
                tvMovieRate.setText(String.valueOf(form.getMovie().getVoteAverage()).concat("/10"));
                tvRateCount.setText(String.valueOf(form.getMovie().getVoteCount()));
                tvMovieRuntime.setText(String.valueOf(form.getMovie().getRuntime()).concat(" Minutes"));
                tvMovieLanguage.setText(form.getMovie().getSpokenLanguage().get(0).getName());
                String imageUrl = Config.MOVIE_IMAGE_URL + form.getMovie().getPosterPath();
                Glide.with(MovieDetailActivity.this).load(imageUrl).placeholder(R.drawable.ic_movie_filter_black_24dp).into(ivMovie);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                Loading.cancelLoading();
            }
        });
    }

    private void loadFirstReview() {
        Loading.showLoading(this);
        MovieApi.movieReview(this, movieId, 1, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getReviews().size() > 0){
                    tvReview.setText(form.getReviews().get(0).getContent());
                    tvAuthor.setText(form.getReviews().get(0).getAuthor());
                }else{
                    tvReview.setText("no review");
                    tvAuthor.setVisibility(View.GONE);
                    tvAuthorBy.setVisibility(View.GONE);
                }
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                Loading.cancelLoading();
            }
        });

        srlMovieDetail.setRefreshing(false);
    }
}

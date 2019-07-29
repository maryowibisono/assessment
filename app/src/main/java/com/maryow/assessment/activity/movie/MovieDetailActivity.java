package com.maryow.assessment.activity.movie;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.Video;
import com.maryow.assessment.view.movie.MovieDetailView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import org.jetbrains.annotations.NotNull;

public class MovieDetailActivity extends BaseActivity<MovieDetailView> {

    @Override
    public MovieDetailView setViewHolder(View parent) {
        return new MovieDetailView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_movie_detail;
    }

    @Override
    protected void onPrepare(MovieDetailView holder) {
        holder.movieId = getIntent().getStringExtra("movieId");
        getLifecycle().addObserver(holder.youtubePlayer);
        btnAction();
        loadVideo();
        loadDetailMovie();
        loadFirstReview();
        pullToRefresh();
    }

    private void btnAction() {
        viewHolder.ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewHolder.tvSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieDetailActivity.this, MovieReviewActivity.class);
                intent.putExtra("movieId", viewHolder.movieId);
                startActivity(intent);
            }
        });
    }

    private void pullToRefresh() {
        viewHolder.srlMovieDetail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadVideo();
                loadDetailMovie();
                loadFirstReview();
            }
        });
    }

    private void loadVideo() {
        Loading.showLoading(this);
        MovieApi.movieVideo(this, viewHolder.movieId, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getVideos().size() > 0) {
                    for (Video v : form.getVideos()) {
                        if (v.getSite().equalsIgnoreCase("YouTube")) {
                            viewHolder.youtubeVideo = v;
                            break;
                        }
                    }
                    viewHolder.youtubePlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                            String videoId = viewHolder.youtubeVideo.getKey();
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });
                    viewHolder.youtubePlayer.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.youtubePlayer.setVisibility(View.GONE);
                }
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieDetailActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });
    }

    private void loadDetailMovie() {
        Loading.showLoading(this);
        MovieApi.movieDetail(this, viewHolder.movieId, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                viewHolder.tvMovieTitle.setText(form.getMovie().getTitle());
                viewHolder.tvMovieOverview.setText(form.getMovie().getOverview());
                viewHolder.tvMovieRate.setText(String.valueOf(form.getMovie().getVoteAverage()).concat("/10"));
                viewHolder.tvRateCount.setText(String.valueOf(form.getMovie().getVoteCount()));
                viewHolder.tvMovieRuntime.setText(String.valueOf(form.getMovie().getRuntime()).concat(" Minutes"));
                viewHolder.tvMovieLanguage.setText(form.getMovie().getSpokenLanguage().get(0).getName());
                String imageUrl = Config.MOVIE_IMAGE_URL + form.getMovie().getPosterPath();
                Glide.with(MovieDetailActivity.this).load(imageUrl)
                        .placeholder(R.drawable.ic_movie_filter_black_24dp).into(viewHolder.ivMovie);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieDetailActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });
    }

    private void loadFirstReview() {
        Loading.showLoading(this);
        MovieApi.movieReview(this, viewHolder.movieId, 1, new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                if (form.getReviews().size() > 0) {
                    viewHolder.tvReview.setText(form.getReviews().get(0).getContent());
                    viewHolder.tvAuthor.setText(form.getReviews().get(0).getAuthor());
                } else {
                    viewHolder.tvReview.setText("no review");
                    viewHolder.tvAuthor.setVisibility(View.GONE);
                    viewHolder.tvAuthorBy.setVisibility(View.GONE);
                }
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                onError(MovieDetailActivity.this, form.getErrDesc());
                Loading.cancelLoading();
            }
        });

        viewHolder.srlMovieDetail.setRefreshing(false);
    }
}

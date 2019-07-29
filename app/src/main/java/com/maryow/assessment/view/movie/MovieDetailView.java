package com.maryow.assessment.view.movie;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maryow.assessment.R;
import com.maryow.assessment.model.movie.Video;
import com.maryow.assessment.view.BaseView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MovieDetailView extends BaseView {
    public YouTubePlayerView youtubePlayer;
    public String movieId;
    public Video youtubeVideo;
    public TextView tvMovieTitle, tvMovieOverview, tvMovieRate, tvRateCount, tvMovieRuntime, tvMovieLanguage,
             tvSeeAll, tvReview, tvAuthor, tvAuthorBy;
    public ImageView ivMovie;
    public SwipeRefreshLayout srlMovieDetail;

    public MovieDetailView(View view) {
        super(view);

        youtubePlayer = view.findViewById(R.id.youtubePlayer);
        tvMovieTitle =  view.findViewById(R.id.tvMovieTitle);
        tvMovieOverview =  view.findViewById(R.id.tvMovieOverview);
        tvMovieRate =  view.findViewById(R.id.tvMovieRate);
        tvRateCount =  view.findViewById(R.id.tvRateCount);
        tvMovieRuntime =  view.findViewById(R.id.tvMovieRuntime);
        tvMovieLanguage =  view.findViewById(R.id.tvMovieLanguage);
        ivMovie =  view.findViewById(R.id.ivMovie);
        tvSeeAll =  view.findViewById(R.id.tvSeeAll);
        tvReview =  view.findViewById(R.id.tvReview);
        tvAuthor =  view.findViewById(R.id.tvAuthor);
        tvAuthorBy =  view.findViewById(R.id.tvAuthorBy);
        srlMovieDetail =  view.findViewById(R.id.srlMovieDetail);
        ibtnBack =  view.findViewById(R.id.ibtnBack);
    }
}

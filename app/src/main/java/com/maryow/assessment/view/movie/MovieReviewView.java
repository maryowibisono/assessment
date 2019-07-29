package com.maryow.assessment.view.movie;

import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maryow.assessment.R;
import com.maryow.assessment.adapter.movie.ReviewAdapter;
import com.maryow.assessment.view.BaseView;

public class MovieReviewView extends BaseView {
    public View ftView;
    public Handler mHandler;
    public ListView lvReview;
    public ReviewAdapter reviewAdapter;
    public int page = 1;
    public boolean isLoading = false;
    public SwipeRefreshLayout srlReview;
    public String movieId;
    public LinearLayout llEmptyReview;

    public MovieReviewView(View view) {
        super(view);
        lvReview = view.findViewById(R.id.lvReview);
        srlReview = view.findViewById(R.id.srlReview);
        ibtnBack = view.findViewById(R.id.ibtnBack);
        llEmptyReview = view.findViewById(R.id.llEmptyReview);
    }
}

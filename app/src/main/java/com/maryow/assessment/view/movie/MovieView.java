package com.maryow.assessment.view.movie;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.maryow.assessment.R;
import com.maryow.assessment.adapter.movie.MovieAdapter;
import com.maryow.assessment.view.BaseView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MovieView extends BaseView {
    public View ftView;
    public Handler mHandler;
    public ListView lvMovie;
    public MovieAdapter movieAdapter;
    public int page = 1;
    public boolean isLoading = false;
    public SwipeRefreshLayout srlMovie;
    public MaterialSearchView msvSearch;
    public LinearLayout llEmptyMovie;
    public int genre;
    public String query;

    public MovieView(View view) {
        super(view);
        lvMovie = view.findViewById(R.id.lvMovie);
        srlMovie = view.findViewById(R.id.srlMovie);
        msvSearch = view.findViewById(R.id.msvSearch);
        llEmptyMovie = view.findViewById(R.id.llEmptyMovie);
    }
}

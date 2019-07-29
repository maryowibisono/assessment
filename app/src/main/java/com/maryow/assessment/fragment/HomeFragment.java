package com.maryow.assessment.fragment;


import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.news.MainActivity;

public class HomeFragment extends BaseFragment {
    LinearLayout llNews, llMovie;

    @Override
    public int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onPrepare(ViewGroup viewGroup) {
        llMovie = viewGroup.findViewById(R.id.llMovie);
        llNews = viewGroup.findViewById(R.id.llNews);

        llNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(R.id.nav_news);
            }
        });

        llMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).goToFragment(R.id.nav_movie);
            }
        });
    }
}

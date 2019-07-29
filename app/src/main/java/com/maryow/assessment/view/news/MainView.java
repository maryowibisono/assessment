package com.maryow.assessment.view.news;

import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maryow.assessment.R;
import com.maryow.assessment.view.BaseView;


public class MainView extends BaseView {
    public BottomNavigationView bottomNavigationView;

    public MainView(View view) {
        super(view);
        bottomNavigationView = view.findViewById(R.id.bnNavigation);
    }
}

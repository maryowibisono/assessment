package com.maryow.assessment.view.news;

import android.view.View;
import android.widget.TextView;

import com.maryow.assessment.R;
import com.maryow.assessment.view.BaseView;

public class SplashScreenView extends BaseView {

    public TextView tvVersion;

    public SplashScreenView(View view) {
        super(view);
        tvVersion = view.findViewById(R.id.tvVersion);
    }
}

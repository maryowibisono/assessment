package com.maryow.assessment.fragment;


import android.util.Log;
import android.view.ViewGroup;

import com.maryow.assessment.R;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.model.news.Form;


public class HomeFragment extends BaseFragment {

    @Override
    public int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onPrepare(ViewGroup viewGroup) {

    }
}

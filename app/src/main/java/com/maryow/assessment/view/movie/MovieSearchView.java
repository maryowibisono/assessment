package com.maryow.assessment.view.movie;

import android.view.View;

import com.maryow.assessment.R;
import com.maryow.assessment.view.BaseView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class MovieSearchView extends BaseView {
    public MaterialSearchView msvSearch;

    public MovieSearchView(View view) {
        super(view);
        msvSearch = view.findViewById(R.id.msvSearch);
    }
}

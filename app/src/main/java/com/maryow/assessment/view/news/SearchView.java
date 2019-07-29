package com.maryow.assessment.view.news;

import android.view.View;

import com.maryow.assessment.R;
import com.maryow.assessment.view.BaseView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class SearchView extends BaseView {
    public MaterialSearchView msvSearch;

    public SearchView(View view) {
        super(view);
        msvSearch = view.findViewById(R.id.msvSearch);
    }
}

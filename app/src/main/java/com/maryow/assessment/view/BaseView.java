package com.maryow.assessment.view;

import android.view.View;
import android.widget.ImageButton;

import com.maryow.assessment.R;

public abstract class BaseView {
    public ImageButton ibtnBack, ibtnSearch;


    public BaseView(View view) {
        ibtnBack = view.findViewById(R.id.ibtnBack);
        ibtnSearch = view.findViewById(R.id.ibtnSearch);
    }
}

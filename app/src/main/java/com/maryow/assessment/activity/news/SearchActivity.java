package com.maryow.assessment.activity.news;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

public class SearchActivity extends BaseActivity {
    MaterialSearchView msvSearch;

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void onPrepare() {
        msvSearch = findViewById(R.id.msvSearch);
        ViewTreeObserver vto = msvSearch.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                msvSearch.setSubmitOnClick(true);
                msvSearch.showSearch();


                msvSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("query",query);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

                msvSearch.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                    @Override
                    public void onSearchViewShown() {
                        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }

                    @Override
                    public void onSearchViewClosed() {
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(msvSearch.getWindowToken(), 0);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED,returnIntent);
                        finish();
                    }
                });
            }
        });


    }


}

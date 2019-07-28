package com.maryow.assessment.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maryow.assessment.R;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.fragment.HomeFragment;
import com.maryow.assessment.fragment.NewsFragment;

public class MainActivity extends BaseActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPrepare() {
        bottomViewAction();

    }

    @Override
    protected void onResume() {
        super.onResume();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void bottomViewAction() {
        bottomNavigationView = findViewById(R.id.bnNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_news);
        changeFragment(new NewsFragment());


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_news : {
                        changeFragment(new NewsFragment());
                        return true;
                    }
                    case R.id.nav_home : {
                        changeFragment(new HomeFragment());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frmLayoutFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case Config.REQ_CODE_SEARCH : {
                if (resultCode == Activity.RESULT_OK){
                    Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                    intent.putExtra("query", data.getStringExtra("query"));
                    startActivity(intent);
                }
            }
            default:{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }

    }
}

package com.maryow.assessment.activity;


import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maryow.assessment.R;
import com.maryow.assessment.fragment.HomeFragment;
import com.maryow.assessment.fragment.NewsFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPrepare() {
        bottomViewAction();

    }



    private void bottomViewAction() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnNavigation);
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


}

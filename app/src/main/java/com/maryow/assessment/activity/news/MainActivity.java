package com.maryow.assessment.activity.news;


import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.BaseActivity;
import com.maryow.assessment.activity.movie.MovieActivity;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.fragment.HomeFragment;
import com.maryow.assessment.fragment.MovieFragment;
import com.maryow.assessment.fragment.NewsFragment;
import com.maryow.assessment.view.news.MainView;

public class MainActivity extends BaseActivity<MainView> {

    @Override
    public MainView setViewHolder(View parent) {
        return new MainView(parent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onPrepare(MainView holder) {
        bottomNavigationAction(holder);
    }

    private void bottomNavigationAction(final MainView holder) {
        holder.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        changeFragment(new HomeFragment());
        holder.bottomNavigationView.setVisibility(View.GONE);
        holder.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news: {
                        holder.bottomNavigationView.setVisibility(View.VISIBLE);
                        changeFragment(new NewsFragment());
                        return true;
                    }
                    case R.id.nav_home: {
                        holder.bottomNavigationView.setVisibility(View.GONE);
                        changeFragment(new HomeFragment());
                        return true;
                    }
                    case R.id.nav_movie: {
                        holder.bottomNavigationView.setVisibility(View.VISIBLE);
                        changeFragment(new MovieFragment());
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void goToFragment(int menu) {
        switch (menu) {
            case R.id.nav_news: {
                viewHolder.bottomNavigationView.setVisibility(View.VISIBLE);
                changeFragment(new NewsFragment());
                break;
            }
            case R.id.nav_movie: {
                viewHolder.bottomNavigationView.setVisibility(View.VISIBLE);
                changeFragment(new MovieFragment());
                break;
            }
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frmLayoutFragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Config.REQ_CODE_SEARCH: {
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                    intent.putExtra("query", data.getStringExtra("query"));
                    startActivity(intent);
                }
                break;
            }
            case Config.REQ_CODE_MOVIE_SEARCH: {
                if (resultCode == Activity.RESULT_OK) {
                    Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                    intent.putExtra("query", data.getStringExtra("query"));
                    startActivity(intent);
                }
                break;
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
                break;
            }
        }

    }
}

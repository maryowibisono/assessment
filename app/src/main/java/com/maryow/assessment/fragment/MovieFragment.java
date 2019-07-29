package com.maryow.assessment.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.movie.MovieActivity;
import com.maryow.assessment.activity.movie.SearchMovieActivity;
import com.maryow.assessment.activity.news.SearchActivity;
import com.maryow.assessment.activity.news.SourceActivity;
import com.maryow.assessment.api.MovieApi;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.component.TagLayout;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.Genre;

import java.util.List;

public class MovieFragment extends BaseFragment {
    RelativeLayout rlSearch;

    @Override
    public int initLayout() {
        return R.layout.fragment_movie;
    }

    @Override
    public void onPrepare(ViewGroup viewGroup) {
        loadMovieGenre(viewGroup);
        searchMovieAction(viewGroup);
    }

    private void searchMovieAction(ViewGroup viewGroup) {
        rlSearch = viewGroup.findViewById(R.id.rlSearch);
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchMovieActivity.class);
                getActivity().startActivityForResult(i, Config.REQ_CODE_MOVIE_SEARCH);
            }
        });
    }

    private void loadMovieGenre(final ViewGroup viewGroup) {
        Loading.showLoading(getActivity());
        MovieApi.genres(getActivity(), new MovieApi.Response() {
            @Override
            public void onSuccess(Form form) {
                renderToView(viewGroup, form.getGenres());
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                onError(getActivity(), form.getErrDesc());
                Loading.cancelLoading();
            }
        });
    }

    private void renderToView(ViewGroup viewGroup, List<Genre> genres) {
        TagLayout tagLayout = viewGroup.findViewById(R.id.tagLayout);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        for (Genre genre : genres) {
            View tagView = layoutInflater.inflate(R.layout.adapter_genre_item, null, false);
            TextView tvGenreId = tagView.findViewById(R.id.tvGenreId);
            TextView tvGenreName = tagView.findViewById(R.id.tvGenreName);
            tvGenreName.setText(genre.getName());
            tvGenreId.setText(String.valueOf(genre.getId()));
            tagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tvGenreId = view.findViewById(R.id.tvGenreId);
                    Intent intent = new Intent(getActivity(), MovieActivity.class);
                    intent.putExtra("genre", tvGenreId.getText());
                    startActivity(intent);
                }
            });
            tagLayout.addView(tagView);
        }
    }
}

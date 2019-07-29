package com.maryow.assessment.adapter.movie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.maryow.assessment.R;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.model.movie.Movie;
import java.util.List;

public class MovieAdapter extends BaseAdapter {
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public void addListItemToAdapter(List<Movie> list) {
        movies.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_movie_item, null);
        ImageView ivMovie = v.findViewById(R.id.ivMovie);
        TextView tvMovieTitle = v.findViewById(R.id.tvMovieTitle);
        TextView tvMovieReleaseDate = v.findViewById(R.id.tvMovieReleaseDate);
        TextView tvMovieRate = v.findViewById(R.id.tvMovieRate);
        Movie movie = movies.get(i);
        if(movie.getReleaseDate().equalsIgnoreCase("")){
            tvMovieReleaseDate.setText("-");
        }else{
            tvMovieReleaseDate.setText(movie.getReleaseDate());
        }

        String imageUrl = Config.MOVIE_IMAGE_URL + movie.getPosterPath();
        Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_movie_filter_black_24dp).into(ivMovie);
        tvMovieTitle.setText(movie.getTitle());
        tvMovieRate.setText(String.valueOf(movie.getVoteAverage()));

        return v;
    }
}




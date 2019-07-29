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
import com.maryow.assessment.model.movie.MovieReview;
import com.maryow.assessment.model.news.Source;

import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    Context context;
    List<MovieReview> reviews;

    public ReviewAdapter(Context context, List<MovieReview> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    public void addListItemToAdapter(List<MovieReview> list) {
        reviews.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int i) {
        return reviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_review_item, null);

        TextView tvReview = v.findViewById(R.id.tvReview);
        TextView tvAuthor = v.findViewById(R.id.tvAuthor);

        MovieReview movieReview = reviews.get(i);
        tvReview.setText(movieReview.getContent());
        tvAuthor.setText(movieReview.getAuthor());


        return v;
    }
}

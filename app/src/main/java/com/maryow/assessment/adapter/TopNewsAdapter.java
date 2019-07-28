package com.maryow.assessment.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.NewsDetailActivity;
import com.maryow.assessment.model.news.Article;

import java.util.List;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.TopNewsViewHolder> {
    Context context;
    List<Article> articles;

    public TopNewsAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public TopNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_top_news_item, parent, false);
        return new TopNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopNewsViewHolder holder, int position) {
       final Article article = articles.get(position);
        holder.title.setText(article.getTitle());
        holder.content.setText(article.getDescription());
        Glide.with(context).load(article.getUrlToImage()).placeholder(R.drawable.defaultnews).into(holder.image);
        holder.cvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("url", article.getUrl());
                context.startActivity(intent);
            }
        });
        holder.ibShareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = article.getUrl();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent, "share"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class TopNewsViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView content;
        public ImageView image;
        public CardView cvNews;
        public ImageButton ibShareNews;

        public TopNewsViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.tvNewsTitle);
            content = view.findViewById(R.id.tvNewsContent);
            image = view.findViewById(R.id.ivNews);
            cvNews = view.findViewById(R.id.cvNews);
            ibShareNews = view.findViewById(R.id.ibShareNews);
        }
    }
}

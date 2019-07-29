package com.maryow.assessment.adapter.news;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.maryow.assessment.R;
import com.maryow.assessment.activity.news.NewsDetailActivity;
import com.maryow.assessment.model.news.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends BaseAdapter {
    Context context;
    List<Article> articles;


    public NewsAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    public void addListItemToAdapter(List<Article> list) {
        articles.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int i) {
        return articles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_news_item, null);
        try {

            TextView tvAuthor = v.findViewById(R.id.tvAuthor);
            TextView tvPublishAt = v.findViewById(R.id.tvPublishAt);
            ImageView ivNews = v.findViewById(R.id.ivNews);
            TextView tvNewsTitle = v.findViewById(R.id.tvNewsTitle);
            TextView tvNewsContent = v.findViewById(R.id.tvNewsContent);
            ImageButton ibShareNews = v.findViewById(R.id.ibShareNews);
            CardView cvNews =  v.findViewById(R.id.cvNews);


            final Article article = articles.get(i);
            ibShareNews.setOnClickListener(new View.OnClickListener() {
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

            cvNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewsDetailActivity.class);
                    intent.putExtra("url", article.getUrl());
                    context.startActivity(intent);
                }
            });

            tvAuthor.setText(article.getAuthor() == null || article.getAuthor().equalsIgnoreCase("") ? "Anonymous" : article.getAuthor());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            Date date = new Date();
            date = simpleDateFormat.parse(article.getPublishedAt());
            SimpleDateFormat reformat = new SimpleDateFormat("EEE MMM dd yyyy  hh:mm:ss");

            tvPublishAt.setText(reformat.format(date));
            Glide.with(context).load(article.getUrlToImage()).placeholder(R.drawable.defaultnews).into(ivNews);
            tvNewsTitle.setText(article.getTitle());
            tvNewsContent.setText(article.getDescription());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return v;
    }
}

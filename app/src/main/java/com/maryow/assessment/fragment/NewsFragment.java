package com.maryow.assessment.fragment;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.news.SearchActivity;
import com.maryow.assessment.activity.news.SourceActivity;
import com.maryow.assessment.adapter.news.TopNewsAdapter;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.common.Config;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.component.TagLayout;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.model.news.Source;
import com.maryow.assessment.util.CommonUtil;

import java.util.ArrayList;


public class NewsFragment extends BaseFragment {
    RecyclerView rvTopNews;
    RelativeLayout rlSearch;

    @Override
    public int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void onPrepare(ViewGroup viewGroup) {
        loadCategoryNews(viewGroup);
        loadTopNews(viewGroup);
        searchNewsAction(viewGroup);
    }

    private void searchNewsAction(ViewGroup viewGroup) {
        rlSearch = viewGroup.findViewById(R.id.rlSearch);
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivityForResult(i, Config.REQ_CODE_SEARCH);
            }
        });

    }

    private void loadTopNews(ViewGroup viewGroup) {
        rvTopNews =viewGroup.findViewById(R.id.rvTopNews);
        rvTopNews.setHasFixedSize(true);

        NewsApi.topHeadlinesByCountry(getActivity(), "us", new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                LinearLayoutManager linearLayoutManager = new  LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                TopNewsAdapter topNewsAdapter = new TopNewsAdapter(getActivity(),form.getArticles());
                rvTopNews.setAdapter(topNewsAdapter);
                rvTopNews.setLayoutManager(linearLayoutManager);
            }

            @Override
            public void onFailed(Form form) {

            }
        });


    }

    private void loadCategoryNews(final ViewGroup viewGroup) {
        Loading.showLoading(getActivity());
        NewsApi.sources(getActivity(), new NewsApi.Response() {
            @Override
            public void onSuccess(Form form) {
                ArrayList<String> listCategory = new ArrayList<>();
                for(Source s: form.getSources()){
                   listCategory.add(s.getCategory());
                }
                listCategory = CommonUtil.distinctListString(listCategory);
                renderToView(viewGroup, listCategory);
                Loading.cancelLoading();
            }

            @Override
            public void onFailed(Form form) {
                Loading.cancelLoading();
            }
        });
    }

    private void renderToView(ViewGroup viewGroup, ArrayList<String> listCategory) {
        TagLayout tagLayout =  viewGroup.findViewById(R.id.tagLayout);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        for (String s : listCategory) {
            View tagView = layoutInflater.inflate(R.layout.adapter_category_item, null, false);
            TextView tagTextView =  tagView.findViewById(R.id.tvCategoryItem);
            tagTextView.setText(s);
            tagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView tagTextView =  view.findViewById(R.id.tvCategoryItem);
                    Intent intent = new Intent(getActivity(), SourceActivity.class);
                    intent.putExtra("category", tagTextView.getText());
                    startActivity(intent);
                }
            });
            tagLayout.addView(tagView);
        }
    }
}

package com.maryow.assessment.fragment;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.SourceActivity;
import com.maryow.assessment.api.NewsApi;
import com.maryow.assessment.component.Loading;
import com.maryow.assessment.component.TagLayout;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.model.news.Source;
import com.maryow.assessment.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment {

    @Override
    public int initLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void onPrepare(ViewGroup viewGroup) {
        loadCategoryNews(viewGroup);
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

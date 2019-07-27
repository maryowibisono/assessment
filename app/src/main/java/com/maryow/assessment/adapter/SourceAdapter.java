package com.maryow.assessment.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maryow.assessment.R;
import com.maryow.assessment.model.news.Source;

import java.util.List;


public class SourceAdapter extends BaseAdapter{
    Context context;
    List<Source> sourceList;
    List<Source> allSourceList;

    public SourceAdapter(Context context, List<Source> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
    }

    public void addListItemToAdapter(List<Source> list) {
        sourceList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return sourceList.size();
    }

    @Override
    public Object getItem(int i) {
        return sourceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.adapter_source_item, null);
        TextView tvSourceName = v.findViewById(R.id.tvSourceName);
        TextView tvSourceDesc = v.findViewById(R.id.tvSourceDesc);

        tvSourceName.setText(sourceList.get(i).getName());
        tvSourceDesc.setText(sourceList.get(i).getDescription());

        return v;
    }
}
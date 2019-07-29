package com.maryow.assessment.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.maryow.assessment.activity.news.SplashScreenActivity;
import com.maryow.assessment.component.CustomDialog;
import com.maryow.assessment.view.BaseView;

public abstract class BaseActivity<VH extends BaseView> extends AppCompatActivity {
    public VH viewHolder;

    public abstract VH setViewHolder(View parent);

    protected abstract int initLayout();

    protected abstract void onPrepare(VH holder);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        viewHolder = setViewHolder(this.findViewById(android.R.id.content));
        onPrepare(viewHolder);
    }

    public VH getViewHolder() {
        return viewHolder;
    }

    public void onError(Activity activity, String message){
        new CustomDialog(activity, "Terjadi Kesalahan", message,
                new CustomDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(CustomDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }
}

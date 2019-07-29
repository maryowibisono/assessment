package com.maryow.assessment.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.maryow.assessment.component.CustomDialog;

public abstract class BaseFragment extends Fragment {

    public abstract int initLayout();

    public abstract void onPrepare(ViewGroup viewGroup);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(initLayout(), container, false);
        onPrepare(rootView);
        return rootView;
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

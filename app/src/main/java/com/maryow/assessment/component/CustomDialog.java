package com.maryow.assessment.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.maryow.assessment.R;
import com.maryow.assessment.activity.news.SplashScreenActivity;

public class CustomDialog extends Dialog {

    static TextView tvMessage;
    static TextView tvTitle;
    static TextView tvSubTitle;
    static Button btnOK;
    static SingleButtonCallback singleButtonCallback;
    static String title;
    static String message;

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(this);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(this);
    }

    public CustomDialog(Activity activity, String title, String message, SingleButtonCallback singleButtonCallback) {
        super(activity);
        this.singleButtonCallback = singleButtonCallback;
        this.title = title;
        this.message = message;
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init(this);
    }

    private void init(CustomDialog customDialog) {
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setCancelable(true);
        customDialog.setContentView(R.layout.dialog_custom);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        customDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        customDialog.getWindow().setLayout(width, height);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setViewHolder(customDialog);
    }

    private void setViewHolder(CustomDialog customDialog) {
        tvTitle = customDialog.findViewById(R.id.tvTitle);
        tvSubTitle = customDialog.findViewById(R.id.tvSubTitle);
        tvMessage = customDialog.findViewById(R.id.tvMessage);
        btnOK = customDialog.findViewById(R.id.btnOK);
    }

    public void show() {
        final CustomDialog customDialog = this;
        tvTitle.setText(title);
        tvMessage.setText(message);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleButtonCallback.onClick(customDialog);
            }
        });
        super.show();
    }

    public interface SingleButtonCallback {
        void onClick(CustomDialog dialog);
    }
}

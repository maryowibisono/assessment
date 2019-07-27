package com.maryow.assessment.component;

import android.app.ProgressDialog;
import android.content.Context;

import com.maryow.assessment.R;

import java.util.Timer;
import java.util.TimerTask;

public class Loading {
    private static ProgressDialog progressDialog;
    private static Timer timer;
    private static int idleTime = (60 * 1000) * 6;

    private Loading() {
    }

    public static void showLoading(Context context) {
        showLoading(context, context.getResources().getString(R.string.loading));
    }

    public static void showLoading(Context context, String message) {
        showLoading(context, message, new ILoadingListener() {
            public void onTimerOut() {
                Loading.cancelLoading();
            }
        }, idleTime);
    }

    public static void showLoading(Context context, String message, ILoadingListener iLoadingListener, int idleTime) {
        cancelLoading();
        progressTimer(iLoadingListener, idleTime);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle((CharSequence)null);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private static void progressTimer(final ILoadingListener iLoadingListener, int idleTime) {
        timer = new Timer();
        TimerTask var2 = new TimerTask() {
            public void run() {
                Loading.cancelLoading();
                if(iLoadingListener != null) {
                    iLoadingListener.onTimerOut();
                }

            }
        };
        timer.schedule(var2, (long)idleTime);
    }

    public static void cancelLoading() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }

        try {
            if(progressDialog != null) {
                progressDialog.cancel();
                progressDialog = null;
            }
        } catch (IllegalArgumentException var1) {
            progressDialog = null;
        }

    }

    public static boolean isShowingLoading() {
        return progressDialog != null && progressDialog.isShowing();
    }

    public interface ILoadingListener {
        void onTimerOut();
    }
}

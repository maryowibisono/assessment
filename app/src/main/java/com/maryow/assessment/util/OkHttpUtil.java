package com.maryow.assessment.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public void get(String url, final OkHttpResponse okHttpResponse) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        okHttpResponse.response(false, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        okHttpResponse.response(true, response.body().string());
                    } catch (Exception e) {
                        okHttpResponse.response(false, e.getMessage());
                    }
                } else {
                    okHttpResponse.response(false, response.message());
                }
            }
        });

    }

    public void post(String url, String param, final OkHttpResponse okHttpResponse) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(param, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        okHttpResponse.response(false, e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                okHttpResponse.response(true, response.body().string());
                            } catch (IOException e) {
                                okHttpResponse.response(false, e.getMessage());
                            }
                        } else {
                            okHttpResponse.response(false, response.message());
                        }
                    }
                });
            }
        });
    }
}

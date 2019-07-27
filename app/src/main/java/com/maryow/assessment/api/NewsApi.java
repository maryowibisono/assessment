package com.maryow.assessment.api;

import android.content.Context;

import com.maryow.assessment.common.Config;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.util.GsonUtil;
import com.maryow.assessment.util.OkHttpResponse;
import com.maryow.assessment.util.OkHttpUtil;

public class NewsApi {

    public static void sources(Context context, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "sources?apiKey=" + Config.NEWS_API_KEY;
        callNewsApi(context, url, newApiResponse);
    }

    public static void sourcesByCategory(Context context, String category, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "sources?apiKey=" + Config.NEWS_API_KEY +
                "&category=" + category;
        callNewsApi(context, url, newApiResponse);
    }

    public static void topHeadlinesByCountry(Context context, String country, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "top-headlines?apiKey=" + Config.NEWS_API_KEY +
                "&country=" + country;
        callNewsApi(context, url, newApiResponse);
    }

    public static void topHeadlinesBySource(Context context, String source, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "top-headlines?apiKey=" + Config.NEWS_API_KEY +
                "&sources=" + source;
        callNewsApi(context, url, newApiResponse);
    }

    public static void topHeadlinesByCountryAndCategory(Context context, String country, String category, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "top-headlines?apiKey=" + Config.NEWS_API_KEY +
                "&country=" + country + "&category=" + category;
        callNewsApi(context, url, newApiResponse);
    }

    public static void topHeadlinesByQuery(Context context, String query, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "top-headlines?apiKey=" + Config.NEWS_API_KEY +
                "&q=" + query;
        callNewsApi(context, url, newApiResponse);
    }

    public static void everythingByQuery(Context context, String query, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "everything?apiKey=" + Config.NEWS_API_KEY +
                "&q=" + query;
        callNewsApi(context, url, newApiResponse);
    }


    public static void everything(Context context, Form form, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "everything?apiKey=" + Config.NEWS_API_KEY +
                "&q=" + form.getQ() + "&from=" + form.getFrom() + "&to=" + form.getTo() + "&sortBy=" + form.getSortBy();
        callNewsApi(context, url, newApiResponse);
    }

    public static void everythingByDomain(Context context, String domain, final Response newApiResponse) {
        String url = Config.NEWS_API_URL + "everything?apiKey=" + Config.NEWS_API_KEY +
                "&domain=" + domain;
        callNewsApi(context, url, newApiResponse);
    }


    public static void callNewsApi(Context context, String url, final Response newApiResponse) {
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(context, url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if (status) {
                    GsonUtil<Form> result = new GsonUtil<>(Form.class);
                    Form formResult = result.parsetToObject(response);
                    if (formResult.getStatus().equalsIgnoreCase("ok")) {
                        formResult.setErrCode("000");
                        formResult.setErrDesc("SUCCESS");
                        newApiResponse.onSuccess(formResult);
                    } else {
                        formResult.setErrCode("000");
                        formResult.setErrDesc(formResult.getStatus());
                        newApiResponse.onSuccess(formResult);
                    }
                } else {
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    newApiResponse.onFailed(f);
                }
            }
        });
    }


    public interface Response {
        void onSuccess(Form form);

        void onFailed(Form form);
    }
}

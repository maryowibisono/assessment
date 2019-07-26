package com.maryow.assessment.api;

import com.maryow.assessment.config.Config;
import com.maryow.assessment.model.news.Form;
import com.maryow.assessment.util.GsonUtil;
import com.maryow.assessment.util.OkHttpResponse;
import com.maryow.assessment.util.OkHttpUtil;

public class NewsApi {

    public static void sources(final Response newApiResponse){
        String url = Config.NEWS_API_URL+"sources?apiKey="+Config.NEWS_API_KEY;
        callNewsApi(url, newApiResponse);
    }

    public static void sourcesByLang(String lang, final Response newApiResponse){
        String url = Config.NEWS_API_URL+"sources?apiKey="+Config.NEWS_API_KEY+
                "&language="+lang;
        callNewsApi(url, newApiResponse);
    }

    public static void sourcesByLangAndCountry(String lang, String country, final Response newApiResponse){
        String url = Config.NEWS_API_URL+"sources?apiKey="+Config.NEWS_API_KEY+
                "&language="+lang+"&country="+country;
        callNewsApi(url, newApiResponse);
    }

    public static void topHeadlinesByCountry(String country, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"top-headlines?apiKey="+Config.NEWS_API_KEY+
                "&country="+country;
        callNewsApi(url, newApiResponse);
    }

    public static void topHeadlinesBySource(String source, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"top-headlines?apiKey="+Config.NEWS_API_KEY+
                "&sources="+source;
        callNewsApi(url, newApiResponse);
    }

    public static void topHeadlinesByCountryAndCategory(String country, String category, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"top-headlines?apiKey="+Config.NEWS_API_KEY+
                "&country="+country+"&category="+category;
        callNewsApi(url, newApiResponse);
    }

    public static void topHeadlinesByQuery(String query, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"top-headlines?apiKey="+Config.NEWS_API_KEY+
                "&q="+query;
        callNewsApi(url, newApiResponse);
    }

    public static void everythingByQuery(String query, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"everything?apiKey="+Config.NEWS_API_KEY+
                "&q="+query;
        callNewsApi(url, newApiResponse);
    }


    public static void everything(Form form, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"everything?apiKey="+Config.NEWS_API_KEY+
                "&q="+form.getQ()+"&from="+form.getFrom()+"&to="+form.getTo()+"&sortBy="+form.getSortBy();
        callNewsApi(url, newApiResponse);
    }

    public static void everythingByDomain(String domain, final  Response newApiResponse){
        String url = Config.NEWS_API_URL+"everything?apiKey="+Config.NEWS_API_KEY+
                "&domain="+domain;
        callNewsApi(url, newApiResponse);
    }


    public static void callNewsApi(String url, final  Response newApiResponse){
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if(status){
                    GsonUtil<Form> result = new GsonUtil<>(Form.class);
                    Form formResult = result.parsetToObject(response);
                    if (formResult.getStatus().equalsIgnoreCase("ok")) {
                        formResult.setErrCode("000");
                        formResult.setErrDesc("SUCCESS");
                        newApiResponse.onSuccess(formResult);
                    }else{
                        formResult.setErrCode("000");
                        formResult.setErrDesc(formResult.getStatus());
                        newApiResponse.onSuccess(formResult);
                    }
                }else{
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    newApiResponse.onFailed(f);
                }
            }
        });
    }



    public interface Response{
        void onSuccess(Form form);
        void onFailed(Form form);
    }
}

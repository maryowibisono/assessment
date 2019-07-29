package com.maryow.assessment.api;

import android.content.Context;

import com.maryow.assessment.common.Config;
import com.maryow.assessment.model.movie.Form;
import com.maryow.assessment.model.movie.GenreResponse;
import com.maryow.assessment.model.movie.Movie;
import com.maryow.assessment.model.movie.MovieResponse;
import com.maryow.assessment.model.movie.MovieReviewResponse;
import com.maryow.assessment.model.movie.MovieVideoResponse;
import com.maryow.assessment.util.GsonUtil;
import com.maryow.assessment.util.OkHttpResponse;
import com.maryow.assessment.util.OkHttpUtil;


public class MovieApi {
    public static void genres(Context context, final MovieApi.Response movieApiResponse) {
        String url = Config.MOVIE_API_URL + "genre/movie/list?language=en-US&api_key=" + Config.MOVIE_API_KEY;
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(context, url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if (status) {
                    GsonUtil<GenreResponse> genreResponseJson = new GsonUtil<>(GenreResponse.class);
                    GenreResponse genreResponse = genreResponseJson.parsetToObject(response);
                    Form form = new Form();
                    form.setPage(genreResponse.getPage());
                    form.setTotalPages(genreResponse.getTotalPages());
                    form.setTotalResults(genreResponse.getTotalResults());
                    form.setGenres(genreResponse.getGenres());
                    movieApiResponse.onSuccess(form);
                } else {
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    movieApiResponse.onFailed(f);
                }
            }
        });
    }

    public static void movieByGenre(Context context, int genre, int page,String query, final MovieApi.Response movieApiResponse) {
        String url = Config.MOVIE_API_URL + "discover/movie?api_key=" + Config.MOVIE_API_KEY +
                "&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=true&page=" +
                page + "&with_genres=" + genre;
        if (query != null){
            url = Config.MOVIE_API_URL + "search/movie?api_key=" + Config.MOVIE_API_KEY +
                    "&language=en-US&include_adult=false&page=" +
                    page +"&query="+query;
        }
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(context, url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if (status) {
                    GsonUtil<MovieResponse> movieResponseJson = new GsonUtil<>(MovieResponse.class);
                    MovieResponse movieResponse = movieResponseJson.parsetToObject(response);
                    Form form = new Form();
                    form.setPage(movieResponse.getPage());
                    form.setTotalPages(movieResponse.getTotalPages());
                    form.setTotalResults(movieResponse.getTotalResults());
                    form.setMovies(movieResponse.getResults());
                    movieApiResponse.onSuccess(form);
                } else {
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    movieApiResponse.onFailed(f);
                }
            }
        });
    }

    public static void movieVideo(Context context, String movieId, final MovieApi.Response movieApiResponse) {
        String url = Config.MOVIE_API_URL + "movie/"+movieId+"/videos?api_key=" + Config.MOVIE_API_KEY +
                "&language=en-US";
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(context, url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if (status) {
                    GsonUtil<MovieVideoResponse> movieVideoResponseJson = new GsonUtil<>(MovieVideoResponse.class);
                    MovieVideoResponse movieVideoResponse = movieVideoResponseJson.parsetToObject(response);
                    Form form = new Form();
                    form.setVideos(movieVideoResponse.getResults());
                    movieApiResponse.onSuccess(form);
                } else {
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    movieApiResponse.onFailed(f);
                }
            }
        });
    }

    public static void movieDetail(Context context, String movieId, final MovieApi.Response movieApiResponse) {
        String url = Config.MOVIE_API_URL + "movie/"+movieId+"?api_key=" + Config.MOVIE_API_KEY +
                "&language=en-US";
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(context, url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if (status) {
                    GsonUtil<Movie> movieJson = new GsonUtil<>(Movie.class);
                    Movie movieResponse = movieJson.parsetToObject(response);
                    Form form = new Form();
                    form.setMovie(movieResponse);
                    movieApiResponse.onSuccess(form);
                } else {
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    movieApiResponse.onFailed(f);
                }
            }
        });
    }

    public static void movieReview(Context context, String movieId, int page, final MovieApi.Response movieApiResponse) {
        String url = Config.MOVIE_API_URL + "movie/"+movieId+"/reviews?api_key=" + Config.MOVIE_API_KEY +
                "&language=en-US&page="+page;
        OkHttpUtil okHttpUtil = new OkHttpUtil();
        okHttpUtil.get(context, url, new OkHttpResponse() {
            @Override
            public void response(boolean status, String response) {
                if (status) {
                    GsonUtil<MovieReviewResponse> movieReviewResponseJson = new GsonUtil<>(MovieReviewResponse.class);
                    MovieReviewResponse movieReviewResponse = movieReviewResponseJson.parsetToObject(response);
                    Form form = new Form();
                    form.setPage(movieReviewResponse.getPage());
                    form.setTotalPages(movieReviewResponse.getTotalPages());
                    form.setTotalResults(movieReviewResponse.getTotalResults());
                    form.setReviews(movieReviewResponse.getResults());
                    movieApiResponse.onSuccess(form);
                } else {
                    Form f = new Form();
                    f.setErrCode("999");
                    f.setErrDesc(response);
                    movieApiResponse.onFailed(f);
                }
            }
        });
    }


    public interface Response {
        void onSuccess(Form form);

        void onFailed(Form form);
    }
}

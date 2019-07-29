package com.maryow.assessment.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie {
    @SerializedName("vote_count")
    private double voteCount;

    private int id;

    @SerializedName("vote_average")
    private double voteAverage;

    private String title;
    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    private String overview;

    @SerializedName("release_date")
    private String releaseDate;

    private int runtime;
    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguage;


    public double getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(double voteCount) {
        this.voteCount = voteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public List<SpokenLanguage> getSpokenLanguage() {
        return spokenLanguage;
    }

    public void setSpokenLanguage(List<SpokenLanguage> spokenLanguage) {
        this.spokenLanguage = spokenLanguage;
    }


}
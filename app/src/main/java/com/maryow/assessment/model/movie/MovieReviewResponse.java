package com.maryow.assessment.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewResponse {
    private List<MovieReview> results;
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public List<MovieReview> getResults() {
        return results;
    }

    public void setResults(List<MovieReview> results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

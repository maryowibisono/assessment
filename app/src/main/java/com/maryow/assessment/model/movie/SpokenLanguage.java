package com.maryow.assessment.model.movie;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {
    @SerializedName("iso_639_1")
    private String iso639;
    private String name;

    public String getIso639() {
        return iso639;
    }

    public void setIso639(String iso639) {
        this.iso639 = iso639;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

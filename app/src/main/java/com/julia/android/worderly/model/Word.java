package com.julia.android.worderly.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Word {

    @SerializedName("word")
    private String word;

    @SerializedName("results")
    private List<Result> results = null;


    public String getWord() {
        return word;
    }


    public void setWord(String word) {
        this.word = word;
    }


    public List<Result> getResults() {
        return results;
    }


    public void setResults(List<Result> results) {
        this.results = results;
    }

}

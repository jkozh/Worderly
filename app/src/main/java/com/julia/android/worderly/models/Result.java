package com.julia.android.worderly.models;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("definition")
    private String definition;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

}

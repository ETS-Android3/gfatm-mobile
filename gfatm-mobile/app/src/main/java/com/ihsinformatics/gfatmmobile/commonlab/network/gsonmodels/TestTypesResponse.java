package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestTypesResponse implements Serializable {

    @SerializedName("results")
    @Expose
    private List<TestType> results = null;

    public List<TestType> getResults() {
        return results;
    }

    public void setResults(List<TestType> results) {
        this.results = results;
    }

}

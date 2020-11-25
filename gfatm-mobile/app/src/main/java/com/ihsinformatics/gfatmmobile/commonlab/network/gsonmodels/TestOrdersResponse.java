package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TestOrdersResponse implements Serializable {

    @SerializedName("results")
    @Expose
    private List<TestOrder> results = null;

    public List<TestOrder> getResults() {
        return results;
    }

    public void setResults(List<TestOrder> results) {
        this.results = results;
    }

}

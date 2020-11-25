package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EncountersResponse implements Serializable {

    @SerializedName("results")
    @Expose
    private List<Encounter> results = null;

    public List<Encounter> getResults() {
        return results;
    }

    public void setResults(List<Encounter> results) {
        this.results = results;
    }

}

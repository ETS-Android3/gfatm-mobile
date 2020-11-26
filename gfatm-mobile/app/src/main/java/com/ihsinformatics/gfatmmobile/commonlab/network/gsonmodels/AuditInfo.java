package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuditInfo implements Serializable {

    @SerializedName("creator")
    @Expose
    private Creator creator;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("changedBy")
    @Expose
    private Object changedBy;
    @SerializedName("dateChanged")
    @Expose
    private String dateChanged;

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Object getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Object changedBy) {
        this.changedBy = changedBy;
    }

    public String getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(String dateChanged) {
        this.dateChanged = dateChanged;
    }

}

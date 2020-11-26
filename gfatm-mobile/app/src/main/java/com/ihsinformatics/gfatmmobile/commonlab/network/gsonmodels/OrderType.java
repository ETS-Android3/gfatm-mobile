package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderType implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("javaClassName")
    @Expose
    private String javaClassName;
    @SerializedName("retired")
    @Expose
    private Boolean retired;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("conceptClasses")
    @Expose
    private List<Object> conceptClasses = null;
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaClassName() {
        return javaClassName;
    }

    public void setJavaClassName(String javaClassName) {
        this.javaClassName = javaClassName;
    }

    public Boolean getRetired() {
        return retired;
    }

    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Object> getConceptClasses() {
        return conceptClasses;
    }

    public void setConceptClasses(List<Object> conceptClasses) {
        this.conceptClasses = conceptClasses;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @Override
    public String toString() {
        return "OrderType{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

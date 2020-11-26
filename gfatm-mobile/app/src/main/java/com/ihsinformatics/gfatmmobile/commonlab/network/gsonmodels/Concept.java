package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Concept implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("datatype")
    @Expose
    private Datatype datatype;
    @SerializedName("conceptClass")
    @Expose
    private ConceptClass conceptClass;
    @SerializedName("set")
    @Expose
    private Boolean set;
    @SerializedName("version")
    @Expose
    private Object version;
    @SerializedName("retired")
    @Expose
    private Boolean retired;
    @SerializedName("names")
    @Expose
    private List<Name> names = null;
    @SerializedName("setMembers")
    @Expose
    private List<Object> setMembers = null;
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

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Datatype getDatatype() {
        return datatype;
    }

    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }

    public ConceptClass getConceptClass() {
        return conceptClass;
    }

    public void setConceptClass(ConceptClass conceptClass) {
        this.conceptClass = conceptClass;
    }

    public Boolean getSet() {
        return set;
    }

    public void setSet(Boolean set) {
        this.set = set;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public Boolean getRetired() {
        return retired;
    }

    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    public List<Name> getNames() {
        return names;
    }

    public void setNames(List<Name> names) {
        this.names = names;
    }

    public List<Object> getSetMembers() {
        return setMembers;
    }

    public void setSetMembers(List<Object> setMembers) {
        this.setMembers = setMembers;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @Override
    public String toString() {
        return "Concept{" +
                "uuid='" + uuid + '\'' +
                ", display='" + display + '\'' +
                ", name=" + name +
                '}';
    }
}

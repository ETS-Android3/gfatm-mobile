package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

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
    @SerializedName("answers")
    @Expose
    private List<Concept> answers = null;
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

    public List<Concept> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Concept> answers) {
        this.answers = answers;
    }

    public static ConceptEntity copyProperties(ConceptEntity dbEntity, Concept c) {

        dbEntity.setUuid(c.uuid);
        dbEntity.setDisplay(c.display);

        return dbEntity;
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

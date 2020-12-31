package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

import java.io.Serializable;

public class AttributeType implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("datatypeConfig")
    @Expose
    private String datatypeConfig;
    @SerializedName("labTestType")
    @Expose
    private LabTestType labTestType;
    @SerializedName("datatypeClassname")
    @Expose
    private String datatypeClassname;

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

    public LabTestType getLabTestType() {
        return labTestType;
    }

    public void setLabTestType(LabTestType labTestType) {
        this.labTestType = labTestType;
    }

    public String getDatatypeClassname() {
        return datatypeClassname;
    }

    public void setDatatypeClassname(String datatypeClassname) {
        this.datatypeClassname = datatypeClassname;
    }

    public String getDatatypeConfig() {
        return datatypeConfig;
    }

    public void setDatatypeConfig(String datatypeConfig) {
        this.datatypeConfig = datatypeConfig;
    }

    public static AttributeTypeEntity copyProperties(AttributeTypeEntity dbEntity, AttributeType t, TestTypeEntity testType) {

        dbEntity.setUuid(t.uuid);
        dbEntity.setDisplay(t.display);
        dbEntity.setDatatypeClassname(t.datatypeClassname);
        dbEntity.setDatatypeConfig(t.getDatatypeConfig());
        // dbTestTypeEntity.setId(t.);
        dbEntity.setName(t.name);
        dbEntity.setTestType(testType);
        return dbEntity;
    }

}

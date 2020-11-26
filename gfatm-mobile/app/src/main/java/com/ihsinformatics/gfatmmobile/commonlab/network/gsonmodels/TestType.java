package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

import java.io.Serializable;

public class TestType implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("testGroup")
    @Expose
    private String testGroup;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("requiresSpecimen")
    @Expose
    private Boolean requiresSpecimen;
    @SerializedName("referenceConcept")
    @Expose
    private Concept referenceConcept;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("auditInfo")
    @Expose
    private AuditInfo auditInfo;
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;

    public static TestTypeEntity copyProperties(TestTypeEntity dbTestTypeEntity, TestType t) {

        dbTestTypeEntity.setUuid(t.uuid);
        dbTestTypeEntity.setDescription(t.description);
        dbTestTypeEntity.setDisplay(t.display);
        // dbTestTypeEntity.setId(t.);
        dbTestTypeEntity.setName(t.name);
        dbTestTypeEntity.setRequiresSpecimen(t.requiresSpecimen);
        dbTestTypeEntity.setShortName(t.shortName);
        dbTestTypeEntity.setTestGroup(t.testGroup);
        return dbTestTypeEntity;
    }

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

    public String getTestGroup() {
        return testGroup;
    }

    public void setTestGroup(String testGroup) {
        this.testGroup = testGroup;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Boolean getRequiresSpecimen() {
        return requiresSpecimen;
    }

    public void setRequiresSpecimen(Boolean requiresSpecimen) {
        this.requiresSpecimen = requiresSpecimen;
    }

    public Concept getReferenceConcept() {
        return referenceConcept;
    }

    public void setReferenceConcept(Concept referenceConcept) {
        this.referenceConcept = referenceConcept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

}

package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

public class TestOrder implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("order")
    @Expose
    private Order order;
    @SerializedName("labTestType")
    @Expose
    private LabTestType labTestType;
    @SerializedName("labReferenceNumber")
    @Expose
    private String labReferenceNumber;
    @SerializedName("labTestSamples")
    @Expose
    private List<Object> labTestSamples = null;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes = null;
    @SerializedName("auditInfo")
    @Expose
    private AuditInfo auditInfo;
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;



    public static TestOrderEntity copyProperties(TestOrderEntity dbEntity, TestOrder t) {
        dbEntity.setUuid(t.uuid);
        dbEntity.setDisplay(t.display);
        dbEntity.setOrderNumber(t.order.getOrderNumber());
        dbEntity.setLabReferenceNumber(t.labReferenceNumber);
        dbEntity.setEncounterUUID(t.order.getEncounter().getUuid());
        dbEntity.setCaresettingUUID(t.order.getCareSetting().getUuid());
        dbEntity.setConceptUUID(t.order.getConcept().getUuid());
        dbEntity.setOrdererUUID(t.order.getOrderer().getUuid());
        dbEntity.setOrder(t.order.toString());
        dbEntity.setPatientUUID(t.order.getPatient().getUuid());
        dbEntity.setCreator(t.auditInfo.getCreator().getDisplay());
        dbEntity.setDateCreated(t.auditInfo.getDateCreated());
        return dbEntity;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public LabTestType getLabTestType() {
        return labTestType;
    }

    public void setLabTestType(LabTestType labTestType) {
        this.labTestType = labTestType;
    }

    public String getLabReferenceNumber() {
        return labReferenceNumber;
    }

    public void setLabReferenceNumber(String labReferenceNumber) {
        this.labReferenceNumber = labReferenceNumber;
    }

    public List<Object> getLabTestSamples() {
        return labTestSamples;
    }

    public void setLabTestSamples(List<Object> labTestSamples) {
        this.labTestSamples = labTestSamples;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
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
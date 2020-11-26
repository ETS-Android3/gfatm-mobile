package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Order implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("accessionNumber")
    @Expose
    private Object accessionNumber;
    @SerializedName("patient")
    @Expose
    private Patient patient;
    @SerializedName("concept")
    @Expose
    private Concept concept;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("careSetting")
    @Expose
    private CareSetting careSetting;
    @SerializedName("previousOrder")
    @Expose
    private Object previousOrder;
    @SerializedName("dateActivated")
    @Expose
    private String dateActivated;
    @SerializedName("scheduledDate")
    @Expose
    private Object scheduledDate;
    @SerializedName("dateStopped")
    @Expose
    private Object dateStopped;
    @SerializedName("autoExpireDate")
    @Expose
    private Object autoExpireDate;
    @SerializedName("encounter")
    @Expose
    private Encounter encounter;
    @SerializedName("orderer")
    @Expose
    private Orderer orderer;
    @SerializedName("orderReason")
    @Expose
    private Object orderReason;
    @SerializedName("orderReasonNonCoded")
    @Expose
    private Object orderReasonNonCoded;
    @SerializedName("orderType")
    @Expose
    private OrderType orderType;
    @SerializedName("urgency")
    @Expose
    private String urgency;
    @SerializedName("instructions")
    @Expose
    private Object instructions;
    @SerializedName("commentToFulfiller")
    @Expose
    private Object commentToFulfiller;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Object getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(Object accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public CareSetting getCareSetting() {
        return careSetting;
    }

    public void setCareSetting(CareSetting careSetting) {
        this.careSetting = careSetting;
    }

    public Object getPreviousOrder() {
        return previousOrder;
    }

    public void setPreviousOrder(Object previousOrder) {
        this.previousOrder = previousOrder;
    }

    public String getDateActivated() {
        return dateActivated;
    }

    public void setDateActivated(String dateActivated) {
        this.dateActivated = dateActivated;
    }

    public Object getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Object scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Object getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(Object dateStopped) {
        this.dateStopped = dateStopped;
    }

    public Object getAutoExpireDate() {
        return autoExpireDate;
    }

    public void setAutoExpireDate(Object autoExpireDate) {
        this.autoExpireDate = autoExpireDate;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public Orderer getOrderer() {
        return orderer;
    }

    public void setOrderer(Orderer orderer) {
        this.orderer = orderer;
    }

    public Object getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(Object orderReason) {
        this.orderReason = orderReason;
    }

    public Object getOrderReasonNonCoded() {
        return orderReasonNonCoded;
    }

    public void setOrderReasonNonCoded(Object orderReasonNonCoded) {
        this.orderReasonNonCoded = orderReasonNonCoded;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public Object getInstructions() {
        return instructions;
    }

    public void setInstructions(Object instructions) {
        this.instructions = instructions;
    }

    public Object getCommentToFulfiller() {
        return commentToFulfiller;
    }

    public void setCommentToFulfiller(Object commentToFulfiller) {
        this.commentToFulfiller = commentToFulfiller;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @Override
    public String toString() {
        return "Order{" +
                "uuid='" + uuid + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", accessionNumber=" + accessionNumber +
                ", patient=" + patient +
                ", concept=" + concept +
                ", careSetting=" + careSetting +
                ", encounter=" + encounter +
                ", orderer=" + orderer +
                ", orderType=" + orderType +
                ", display='" + display + '\'' +
                '}';
    }
}
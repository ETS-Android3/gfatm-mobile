package com.ihsinformatics.gfatmmobile.medication.gson_pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.CareSetting;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Encounter;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Orderer;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Patient;

public class DrugOrder {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("patient")
    @Expose
    private Patient patient;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
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
    @SerializedName("dateStopped")
    @Expose
    private Object dateStopped;
    @SerializedName("autoExpireDate")
    @Expose
    private String autoExpireDate;
    @SerializedName("encounter")
    @Expose
    private Encounter encounter;
    @SerializedName("orderer")
    @Expose
    private Orderer orderer;
    @SerializedName("orderReason")
    @Expose
    private MedicationOrderReason orderReason;
    @SerializedName("orderReasonNonCoded")
    @Expose
    private String orderReasonNonCoded;
    @SerializedName("instructions")
    @Expose
    private Object instructions;
    @SerializedName("drug")
    @Expose
    private Drug drug;
    @SerializedName("dose")
    @Expose
    private Double dose;
    @SerializedName("doseUnits")
    @Expose
    private MedicationDoseUnit doseUnits;
    @SerializedName("frequency")
    @Expose
    private MedicationFrequency frequency;
    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("quantityUnits")
    @Expose
    private MedicationDoseUnit quantityUnits;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("durationUnits")
    @Expose
    private MedicationDuration durationUnits;
    @SerializedName("route")
    @Expose
    private MedicationRoute route;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public Object getDateStopped() {
        return dateStopped;
    }

    public void setDateStopped(Object dateStopped) {
        this.dateStopped = dateStopped;
    }

    public String getAutoExpireDate() {
        return autoExpireDate;
    }

    public void setAutoExpireDate(String autoExpireDate) {
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

    public MedicationOrderReason getOrderReason() {
        return orderReason;
    }

    public void setOrderReason(MedicationOrderReason orderReason) {
        this.orderReason = orderReason;
    }

    public String getOrderReasonNonCoded() {
        return orderReasonNonCoded;
    }

    public void setOrderReasonNonCoded(String orderReasonNonCoded) {
        this.orderReasonNonCoded = orderReasonNonCoded;
    }

    public Object getInstructions() {
        return instructions;
    }

    public void setInstructions(Object instructions) {
        this.instructions = instructions;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public MedicationDoseUnit getDoseUnits() {
        return doseUnits;
    }

    public void setDoseUnits(MedicationDoseUnit doseUnits) {
        this.doseUnits = doseUnits;
    }

    public MedicationFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(MedicationFrequency frequency) {
        this.frequency = frequency;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public MedicationDoseUnit getQuantityUnits() {
        return quantityUnits;
    }

    public void setQuantityUnits(MedicationDoseUnit quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public MedicationDuration getDurationUnits() {
        return durationUnits;
    }

    public void setDurationUnits(MedicationDuration durationUnits) {
        this.durationUnits = durationUnits;
    }

    public MedicationRoute getRoute() {
        return route;
    }

    public void setRoute(MedicationRoute route) {
        this.route = route;
    }

}

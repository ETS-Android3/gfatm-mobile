package com.ihsinformatics.gfatmmobile.medication.gson_pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;

public class DrugOrderPostModel {

    @SerializedName("patient")
    @Expose
    private String patient;
    @SerializedName("concept")
    @Expose
    private String concept;
    @SerializedName("careSetting")
    @Expose
    private String careSetting;
    @SerializedName("orderer")
    @Expose
    private String orderer;
    @SerializedName("encounter")
    @Expose
    private String encounter;
    @SerializedName("drug")
    @Expose
    private String drug;
    @SerializedName("dose")
    @Expose
    private Double dose;
    @SerializedName("doseUnits")
    @Expose
    private String doseUnits;
    @SerializedName("quantityUnits")
    @Expose
    private String quantityUnits;
    @SerializedName("quantity")
    @Expose
    private Double quantity;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("frequency")
    @Expose
    private String frequency;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("durationUnits")
    @Expose
    private String durationUnits;
    @SerializedName("asNeeded")
    @Expose
    private Boolean asNeeded;
    @SerializedName("numRefills")
    @Expose
    private Integer numRefills;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("action")
    @Expose
    private String action;

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getCareSetting() {
        return careSetting;
    }

    public void setCareSetting(String careSetting) {
        this.careSetting = careSetting;
    }

    public String getOrderer() {
        return orderer;
    }

    public void setOrderer(String orderer) {
        this.orderer = orderer;
    }

    public String getEncounter() {
        return encounter;
    }

    public void setEncounter(String encounter) {
        this.encounter = encounter;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public Double getDose() {
        return dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public String getDoseUnits() {
        return doseUnits;
    }

    public void setDoseUnits(String doseUnits) {
        this.doseUnits = doseUnits;
    }

    public String getQuantityUnits() {
        return quantityUnits;
    }

    public void setQuantityUnits(String quantityUnits) {
        this.quantityUnits = quantityUnits;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationUnits() {
        return durationUnits;
    }

    public void setDurationUnits(String durationUnits) {
        this.durationUnits = durationUnits;
    }

    public Boolean getAsNeeded() {
        return asNeeded;
    }

    public void setAsNeeded(Boolean asNeeded) {
        this.asNeeded = asNeeded;
    }

    public Integer getNumRefills() {
        return numRefills;
    }

    public void setNumRefills(Integer numRefills) {
        this.numRefills = numRefills;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DrugOrderPostModel inflateWithDbEntity(DrugOrderEntity entity) {

        this.patient = entity.getPatientUUID();
        this.concept = entity.getConceptUUID();
        this.careSetting = entity.getCareSettingUUID();
        this.orderer = entity.getOrdererUUID();
        this.encounter = entity.getEncounterUUID();
        this.drug = entity.getDrugUUID();
        this.dose = entity.getDose();
        this.doseUnits = entity.getDoseUnitsUUID();
        this.quantityUnits = entity.getQuantityUnitsUUID();
        this.quantity = entity.getQuantity();
        this.route = entity.getRouteUUID();
        this.frequency = entity.getFrequencyUUID();
        this.duration = entity.getDuration();
        this.durationUnits = entity.getDurationUnitsUUID();
        this.asNeeded = false;
        this.numRefills = 0;
        this.type = "drugorder";
        this.action = entity.getAction();


        return this;
    }
}
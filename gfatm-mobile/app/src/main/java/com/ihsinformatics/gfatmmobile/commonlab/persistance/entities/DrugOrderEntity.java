package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.CareSetting;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Encounter;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Orderer;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Patient;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.Drug;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationDuration;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationOrderReason;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.MedicationRoute;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;

@Entity(nameInDb = "drug_order")
public class DrugOrderEntity implements Serializable {

    public static final long serialVersionUID = 1l;
    @Id
    private Long id;

    @Unique
    private String uuid;

    private boolean toUpload = false;

    private String uploadReason;
    
    private String patientUUID;

    private String conceptUUID;

    private String orderNumber;

    private String action;

    private String careSettingUUID;

    private String previousOrderUUID;

    private String dateActivated;

    private String dateStopped;

    private String autoExpireDate;

    private String encounterUUID;

    private String ordererUUID;

    private String orderReasonUUID;

    private String orderReasonNonCoded;

    private String instructions;

    /*@NotNull
    private Long drugId;
    @ToOne(joinProperty = "drugId")
    private MedicationDrug drug;*/

    private String drugUUID;

    private Double dose;

    private String doseUnitsUUID;

    private String frequencyUUID;

    private Double quantity;

    private String quantityUnitsUUID;

    private Integer duration;

    private String durationUnitsUUID;

    private String routeUUID;

    @Generated(hash = 1923176312)
    public DrugOrderEntity(Long id, String uuid, boolean toUpload,
            String uploadReason, String patientUUID, String conceptUUID,
            String orderNumber, String action, String careSettingUUID,
            String previousOrderUUID, String dateActivated, String dateStopped,
            String autoExpireDate, String encounterUUID, String ordererUUID,
            String orderReasonUUID, String orderReasonNonCoded, String instructions,
            String drugUUID, Double dose, String doseUnitsUUID,
            String frequencyUUID, Double quantity, String quantityUnitsUUID,
            Integer duration, String durationUnitsUUID, String routeUUID) {
        this.id = id;
        this.uuid = uuid;
        this.toUpload = toUpload;
        this.uploadReason = uploadReason;
        this.patientUUID = patientUUID;
        this.conceptUUID = conceptUUID;
        this.orderNumber = orderNumber;
        this.action = action;
        this.careSettingUUID = careSettingUUID;
        this.previousOrderUUID = previousOrderUUID;
        this.dateActivated = dateActivated;
        this.dateStopped = dateStopped;
        this.autoExpireDate = autoExpireDate;
        this.encounterUUID = encounterUUID;
        this.ordererUUID = ordererUUID;
        this.orderReasonUUID = orderReasonUUID;
        this.orderReasonNonCoded = orderReasonNonCoded;
        this.instructions = instructions;
        this.drugUUID = drugUUID;
        this.dose = dose;
        this.doseUnitsUUID = doseUnitsUUID;
        this.frequencyUUID = frequencyUUID;
        this.quantity = quantity;
        this.quantityUnitsUUID = quantityUnitsUUID;
        this.duration = duration;
        this.durationUnitsUUID = durationUnitsUUID;
        this.routeUUID = routeUUID;
    }

    @Generated(hash = 227895774)
    public DrugOrderEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPatientUUID() {
        return this.patientUUID;
    }

    public void setPatientUUID(String patientUUID) {
        this.patientUUID = patientUUID;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCareSettingUUID() {
        return this.careSettingUUID;
    }

    public void setCareSettingUUID(String careSettingUUID) {
        this.careSettingUUID = careSettingUUID;
    }

    public String getDateActivated() {
        return this.dateActivated;
    }

    public void setDateActivated(String dateActivated) {
        this.dateActivated = dateActivated;
    }

    public String getDateStopped() {
        return this.dateStopped;
    }

    public void setDateStopped(String dateStopped) {
        this.dateStopped = dateStopped;
    }

    public String getAutoExpireDate() {
        return this.autoExpireDate;
    }

    public void setAutoExpireDate(String autoExpireDate) {
        this.autoExpireDate = autoExpireDate;
    }

    public String getEncounterUUID() {
        return this.encounterUUID;
    }

    public void setEncounterUUID(String encounterUUID) {
        this.encounterUUID = encounterUUID;
    }

    public String getOrdererUUID() {
        return this.ordererUUID;
    }

    public void setOrdererUUID(String ordererUUID) {
        this.ordererUUID = ordererUUID;
    }

    public String getOrderReasonUUID() {
        return this.orderReasonUUID;
    }

    public void setOrderReasonUUID(String orderReasonUUID) {
        this.orderReasonUUID = orderReasonUUID;
    }

    public String getOrderReasonNonCoded() {
        return this.orderReasonNonCoded;
    }

    public void setOrderReasonNonCoded(String orderReasonNonCoded) {
        this.orderReasonNonCoded = orderReasonNonCoded;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDrugUUID() {
        return this.drugUUID;
    }

    public void setDrugUUID(String drugUUID) {
        this.drugUUID = drugUUID;
    }

    public Double getDose() {
        return this.dose;
    }

    public void setDose(Double dose) {
        this.dose = dose;
    }

    public String getDoseUnitsUUID() {
        return this.doseUnitsUUID;
    }

    public void setDoseUnitsUUID(String doseUnitsUUID) {
        this.doseUnitsUUID = doseUnitsUUID;
    }

    public String getFrequencyUUID() {
        return this.frequencyUUID;
    }

    public void setFrequencyUUID(String frequencyUUID) {
        this.frequencyUUID = frequencyUUID;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnitsUUID() {
        return this.quantityUnitsUUID;
    }

    public void setQuantityUnitsUUID(String quantityUnitsUUID) {
        this.quantityUnitsUUID = quantityUnitsUUID;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDurationUnitsUUID() {
        return this.durationUnitsUUID;
    }

    public void setDurationUnitsUUID(String durationUnitsUUID) {
        this.durationUnitsUUID = durationUnitsUUID;
    }

    public String getRouteUUID() {
        return this.routeUUID;
    }

    public void setRouteUUID(String routeUUID) {
        this.routeUUID = routeUUID;
    }

    public String getPreviousOrderUUID() {
        return this.previousOrderUUID;
    }

    public void setPreviousOrderUUID(String previousOrderUUID) {
        this.previousOrderUUID = previousOrderUUID;
    }

    public boolean getToUpload() {
        return this.toUpload;
    }

    public void setToUpload(boolean toUpload) {
        this.toUpload = toUpload;
    }

    public String getConceptUUID() {
        return this.conceptUUID;
    }

    public void setConceptUUID(String conceptUUID) {
        this.conceptUUID = conceptUUID;
    }

    public String getUploadReason() {
        return this.uploadReason;
    }

    public void setUploadReason(String uploadReason) {
        this.uploadReason = uploadReason;
    }

}

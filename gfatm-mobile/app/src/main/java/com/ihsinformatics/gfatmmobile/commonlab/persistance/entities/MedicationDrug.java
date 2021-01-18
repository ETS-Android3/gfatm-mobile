package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Concept;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "medication_drug")
public class MedicationDrug {


    private String uuid;

    private String name;

    private Double maximumDailyDose;

    private Double minimumDailyDose;

    private String strength;

    private String conceptUUID;
    private String conceptName;

    @Generated(hash = 263588571)
    public MedicationDrug(String uuid, String name, Double maximumDailyDose,
            Double minimumDailyDose, String strength, String conceptUUID,
            String conceptName) {
        this.uuid = uuid;
        this.name = name;
        this.maximumDailyDose = maximumDailyDose;
        this.minimumDailyDose = minimumDailyDose;
        this.strength = strength;
        this.conceptUUID = conceptUUID;
        this.conceptName = conceptName;
    }
    @Generated(hash = 1072998763)
    public MedicationDrug() {
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getMaximumDailyDose() {
        return this.maximumDailyDose;
    }
    public void setMaximumDailyDose(Double maximumDailyDose) {
        this.maximumDailyDose = maximumDailyDose;
    }
    public Double getMinimumDailyDose() {
        return this.minimumDailyDose;
    }
    public void setMinimumDailyDose(Double minimumDailyDose) {
        this.minimumDailyDose = minimumDailyDose;
    }
    public String getStrength() {
        return this.strength;
    }
    public void setStrength(String strength) {
        this.strength = strength;
    }
    public String getConceptUUID() {
        return this.conceptUUID;
    }
    public void setConceptUUID(String conceptUUID) {
        this.conceptUUID = conceptUUID;
    }
    public String getConceptName() {
        return this.conceptName;
    }
    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

}

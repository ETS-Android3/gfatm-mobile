package com.ihsinformatics.gfatmmobile.medication.gson_pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Concept;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestType;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

public class Drug {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("maximumDailyDose")
    @Expose
    private Double maximumDailyDose;
    @SerializedName("minimumDailyDose")
    @Expose
    private Double minimumDailyDose;
    @SerializedName("strength")
    @Expose
    private String strength;
    @SerializedName("concept")
    @Expose
    private Concept concept;
    /*@SerializedName("dosageForm")
    @Expose
    private DosageForm dosageForm;*/

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaximumDailyDose() {
        return maximumDailyDose;
    }

    public void setMaximumDailyDose(Double maximumDailyDose) {
        this.maximumDailyDose = maximumDailyDose;
    }

    public Double getMinimumDailyDose() {
        return minimumDailyDose;
    }

    public void setMinimumDailyDose(Double minimumDailyDose) {
        this.minimumDailyDose = minimumDailyDose;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }
    public static MedicationDrug copyProperties(MedicationDrug dbTestTypeEntity, Drug t) {

        dbTestTypeEntity.setUuid(t.uuid);
        dbTestTypeEntity.setConceptUUID(t.getConcept().getUuid());
        dbTestTypeEntity.setConceptName(t.getConcept().getDisplay());
        dbTestTypeEntity.setName(t.name);
        dbTestTypeEntity.setStrength(t.strength);
        dbTestTypeEntity.setMaximumDailyDose(t.maximumDailyDose);
        dbTestTypeEntity.setMinimumDailyDose(t.minimumDailyDose);

        return dbTestTypeEntity;
    }
    /*public DosageForm getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(DosageForm dosageForm) {
        this.dosageForm = dosageForm;
    }*/

}

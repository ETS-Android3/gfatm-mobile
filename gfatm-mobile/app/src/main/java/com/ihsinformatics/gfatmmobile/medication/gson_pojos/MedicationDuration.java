package com.ihsinformatics.gfatmmobile.medication.gson_pojos;

public class MedicationDuration {

    private String uuid;
    private String display;

    public MedicationDuration(String uuid, String name) {
        this.uuid = uuid;
        this.display = name;
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

    public static com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency copyProperties(com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency dbTestTypeEntity, MedicationDuration t) {

        dbTestTypeEntity.setUuid(t.uuid);
        dbTestTypeEntity.setDisplay(t.getDisplay());

        return dbTestTypeEntity;
    }
}

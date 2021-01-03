package com.ihsinformatics.gfatmmobile.medication.gson_pojos;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

public class MedicationRoute {

    private String uuid;
    private String display;

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

    public static com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute copyProperties(com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute dbTestTypeEntity, MedicationRoute t) {

        dbTestTypeEntity.setUuid(t.uuid);
        dbTestTypeEntity.setDisplay(t.getDisplay());

        return dbTestTypeEntity;
    }
}

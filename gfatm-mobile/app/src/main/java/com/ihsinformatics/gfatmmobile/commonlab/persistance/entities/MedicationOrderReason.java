package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "medication_order_reason")
public class MedicationOrderReason {
    @Id
    private Long id;

    private String uuid;
    private String display;
    @Generated(hash = 1963430225)
    public MedicationOrderReason(Long id, String uuid, String display) {
        this.id = id;
        this.uuid = uuid;
        this.display = display;
    }
    @Generated(hash = 1313505727)
    public MedicationOrderReason() {
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
    public String getDisplay() {
        return this.display;
    }
    public void setDisplay(String display) {
        this.display = display;
    }
}

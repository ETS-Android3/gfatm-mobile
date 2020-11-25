package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.AttributeType;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "test_type")
public class TestTypeEntity {
    @Id
    private Long id;
    @Unique
    private String uuid;
    private String display;
    private String testGroup;
    private String shortName;
    private Boolean requiresSpecimen;
    private String name;
    private String description;
    @Generated(hash = 1972171081)
    public TestTypeEntity(Long id, String uuid, String display, String testGroup,
            String shortName, Boolean requiresSpecimen, String name,
            String description) {
        this.id = id;
        this.uuid = uuid;
        this.display = display;
        this.testGroup = testGroup;
        this.shortName = shortName;
        this.requiresSpecimen = requiresSpecimen;
        this.name = name;
        this.description = description;
    }
    @Generated(hash = 1374718881)
    public TestTypeEntity() {
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
    public String getTestGroup() {
        return this.testGroup;
    }
    public void setTestGroup(String testGroup) {
        this.testGroup = testGroup;
    }
    public String getShortName() {
        return this.shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public Boolean getRequiresSpecimen() {
        return this.requiresSpecimen;
    }
    public void setRequiresSpecimen(Boolean requiresSpecimen) {
        this.requiresSpecimen = requiresSpecimen;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}

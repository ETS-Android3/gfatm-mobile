package com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class Attribute implements Serializable {

    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("attributeType")
    @Expose
    private AttributeType attributeType;
    @SerializedName("valueReference")
    @Expose
    private String valueReference;
    @SerializedName("resourceVersion")
    @Expose
    private String resourceVersion;

    public static AttributeEntity copyProperties(AttributeEntity dbEntity, Attribute t, TestOrderEntity order, AttributeTypeEntity attributeType) {
        dbEntity.setUuid(t.uuid);
        dbEntity.setDisplay(t.display);
        dbEntity.setAttributeType(attributeType);
        dbEntity.setTestOrder(order);
        dbEntity.setValueReference(t.valueReference);

        return dbEntity;
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

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public String getValueReference() {
        return valueReference;
    }

    public void setValueReference(String valueReference) {
        this.valueReference = valueReference;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

}

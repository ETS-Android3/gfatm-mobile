package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Attribute;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.AuditInfo;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.LabTestType;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Order;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "test_order")
public class TestOrderEntity {
    @Id
    private Long id;
    @Unique
    private String uuid;
    private String orderNumber;
    private String display;
    private String order;
    private String patientUUID;
    private String EncounterUUID;
    private String conceptUUID;
    private String ordererUUID;
    private String caresettingUUID;
    private String creator;
    private String dateCreated;

    private Long testTypeId;
    @ToOne(joinProperty = "testTypeId")
    private TestTypeEntity labTestType;

    private String labReferenceNumber;
    private transient List<AttributeEntity> attributes;

    public List<AttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeEntity> attributes) {
        this.attributes = attributes;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1022366904)
    private transient TestOrderEntityDao myDao;

    @Generated(hash = 1538902939)
    public TestOrderEntity(Long id, String uuid, String orderNumber, String display,
            String order, String patientUUID, String EncounterUUID, String conceptUUID,
            String ordererUUID, String caresettingUUID, String creator, String dateCreated,
            Long testTypeId, String labReferenceNumber) {
        this.id = id;
        this.uuid = uuid;
        this.orderNumber = orderNumber;
        this.display = display;
        this.order = order;
        this.patientUUID = patientUUID;
        this.EncounterUUID = EncounterUUID;
        this.conceptUUID = conceptUUID;
        this.ordererUUID = ordererUUID;
        this.caresettingUUID = caresettingUUID;
        this.creator = creator;
        this.dateCreated = dateCreated;
        this.testTypeId = testTypeId;
        this.labReferenceNumber = labReferenceNumber;
    }

    @Generated(hash = 721530207)
    public TestOrderEntity() {
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

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPatientUUID() {
        return this.patientUUID;
    }

    public void setPatientUUID(String patientUUID) {
        this.patientUUID = patientUUID;
    }

    public Long getTestTypeId() {
        return this.testTypeId;
    }

    public void setTestTypeId(Long testTypeId) {
        this.testTypeId = testTypeId;
    }

    public String getLabReferenceNumber() {
        return this.labReferenceNumber;
    }

    public void setLabReferenceNumber(String labReferenceNumber) {
        this.labReferenceNumber = labReferenceNumber;
    }

    @Generated(hash = 316978290)
    private transient Long labTestType__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1677453188)
    public TestTypeEntity getLabTestType() {
        Long __key = this.testTypeId;
        if (labTestType__resolvedKey == null
                || !labTestType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TestTypeEntityDao targetDao = daoSession.getTestTypeEntityDao();
            TestTypeEntity labTestTypeNew = targetDao.load(__key);
            synchronized (this) {
                labTestType = labTestTypeNew;
                labTestType__resolvedKey = __key;
            }
        }
        return labTestType;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 837030285)
    public void setLabTestType(TestTypeEntity labTestType) {
        synchronized (this) {
            this.labTestType = labTestType;
            testTypeId = labTestType == null ? null : labTestType.getId();
            labTestType__resolvedKey = testTypeId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1971036474)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTestOrderEntityDao() : null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEncounterUUID() {
        return this.EncounterUUID;
    }

    public void setEncounterUUID(String EncounterUUID) {
        this.EncounterUUID = EncounterUUID;
    }

    public String getConceptUUID() {
        return this.conceptUUID;
    }

    public void setConceptUUID(String conceptUUID) {
        this.conceptUUID = conceptUUID;
    }

    public String getOrdererUUID() {
        return this.ordererUUID;
    }

    public void setOrdererUUID(String ordererUUID) {
        this.ordererUUID = ordererUUID;
    }

    public String getCaresettingUUID() {
        return this.caresettingUUID;
    }

    public void setCaresettingUUID(String caresettingUUID) {
        this.caresettingUUID = caresettingUUID;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    
}
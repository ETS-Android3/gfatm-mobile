package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.AttributeType;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(nameInDb = "attribute")
public class AttributeEntity {
    @Id
    private Long id;
    @Unique
    private String uuid;

    private String display;

    @NotNull
    private Long attributeTypeId;
    @ToOne(joinProperty = "attributeTypeId")
    private AttributeTypeEntity attributeType;

    @NotNull
    private Long testOrderId;
    @ToOne(joinProperty = "testOrderId")
    private TestOrderEntity testOrder;

    private String valueReference;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 231044304)
    private transient AttributeEntityDao myDao;

    @Generated(hash = 725280038)
    public AttributeEntity(Long id, String uuid, String display, @NotNull Long attributeTypeId,
            @NotNull Long testOrderId, String valueReference) {
        this.id = id;
        this.uuid = uuid;
        this.display = display;
        this.attributeTypeId = attributeTypeId;
        this.testOrderId = testOrderId;
        this.valueReference = valueReference;
    }

    @Generated(hash = 1923243845)
    public AttributeEntity() {
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

    public Long getAttributeTypeId() {
        return this.attributeTypeId;
    }

    public void setAttributeTypeId(Long attributeTypeId) {
        this.attributeTypeId = attributeTypeId;
    }

    public String getValueReference() {
        return this.valueReference;
    }

    public void setValueReference(String valueReference) {
        this.valueReference = valueReference;
    }

    @Generated(hash = 746015902)
    private transient Long attributeType__resolvedKey;
    @Generated(hash = 454578034)
    private transient Long testOrder__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1081705403)
    public AttributeTypeEntity getAttributeType() {
        Long __key = this.attributeTypeId;
        if (attributeType__resolvedKey == null
                || !attributeType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttributeTypeEntityDao targetDao = daoSession
                    .getAttributeTypeEntityDao();
            AttributeTypeEntity attributeTypeNew = targetDao.load(__key);
            synchronized (this) {
                attributeType = attributeTypeNew;
                attributeType__resolvedKey = __key;
            }
        }
        return attributeType;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2099414326)
    public void setAttributeType(@NotNull AttributeTypeEntity attributeType) {
        if (attributeType == null) {
            throw new DaoException(
                    "To-one property 'attributeTypeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.attributeType = attributeType;
            attributeTypeId = attributeType.getId();
            attributeType__resolvedKey = attributeTypeId;
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
    @Generated(hash = 1661622883)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttributeEntityDao() : null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestOrderId() {
        return this.testOrderId;
    }

    public void setTestOrderId(Long testOrderId) {
        this.testOrderId = testOrderId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1286610450)
    public TestOrderEntity getTestOrder() {
        Long __key = this.testOrderId;
        if (testOrder__resolvedKey == null || !testOrder__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TestOrderEntityDao targetDao = daoSession.getTestOrderEntityDao();
            TestOrderEntity testOrderNew = targetDao.load(__key);
            synchronized (this) {
                testOrder = testOrderNew;
                testOrder__resolvedKey = __key;
            }
        }
        return testOrder;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 135428832)
    public void setTestOrder(@NotNull TestOrderEntity testOrder) {
        if (testOrder == null) {
            throw new DaoException(
                    "To-one property 'testOrderId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.testOrder = testOrder;
            testOrderId = testOrder.getId();
            testOrder__resolvedKey = testOrderId;
        }
    }
}

package com.ihsinformatics.gfatmmobile.commonlab.persistance.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
@Entity(nameInDb = "attribute_type")
public class AttributeTypeEntity {

    @Id
    private Long id;
    @Unique
    private String uuid;
    private String display;
    private String name;

    @NotNull
    private Long testTypeId;
    @ToOne(joinProperty = "testTypeId")
    private TestTypeEntity testType;

    private String datatypeClassname;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1835635765)
    private transient AttributeTypeEntityDao myDao;

    @Generated(hash = 1697946297)
    public AttributeTypeEntity(Long id, String uuid, String display, String name,
            @NotNull Long testTypeId, String datatypeClassname) {
        this.id = id;
        this.uuid = uuid;
        this.display = display;
        this.name = name;
        this.testTypeId = testTypeId;
        this.datatypeClassname = datatypeClassname;
    }

    @Generated(hash = 1442950855)
    public AttributeTypeEntity() {
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTestTypeId() {
        return this.testTypeId;
    }

    public void setTestTypeId(Long testTypeId) {
        this.testTypeId = testTypeId;
    }

    public String getDatatypeClassname() {
        return this.datatypeClassname;
    }

    public void setDatatypeClassname(String datatypeClassname) {
        this.datatypeClassname = datatypeClassname;
    }

    @Generated(hash = 632788717)
    private transient Long testType__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1120643838)
    public TestTypeEntity getTestType() {
        Long __key = this.testTypeId;
        if (testType__resolvedKey == null || !testType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TestTypeEntityDao targetDao = daoSession.getTestTypeEntityDao();
            TestTypeEntity testTypeNew = targetDao.load(__key);
            synchronized (this) {
                testType = testTypeNew;
                testType__resolvedKey = __key;
            }
        }
        return testType;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2057529799)
    public void setTestType(@NotNull TestTypeEntity testType) {
        if (testType == null) {
            throw new DaoException(
                    "To-one property 'testTypeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.testType = testType;
            testTypeId = testType.getId();
            testType__resolvedKey = testTypeId;
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
    @Generated(hash = 1572364138)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttributeTypeEntityDao() : null;
    }

}

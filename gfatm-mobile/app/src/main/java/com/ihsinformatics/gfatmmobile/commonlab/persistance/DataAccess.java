package com.ihsinformatics.gfatmmobile.commonlab.persistance;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntityDao;
import com.ihsinformatics.gfatmmobile.util.App;

import java.util.List;

public class DataAccess {

    public static DataAccess instance;

    public static synchronized DataAccess getInstance() {
        if(instance == null) {
            instance = new DataAccess();
        }
        return instance;
    }

    public TestTypeEntity getTestTypeByUUID(String uuid) {
        TestTypeEntityDao  dao= App.daoSession.getTestTypeEntityDao();
        TestTypeEntity testTypeEntity = dao.queryBuilder()
                .where(TestTypeEntityDao.Properties.Uuid.eq(uuid))
                .unique();

        return testTypeEntity;
    }

    public void insertAll(List<AttributeTypeEntity> dbEntities) {
        AttributeTypeEntityDao dao= com.ihsinformatics.gfatmmobile.util.App.daoSession.getAttributeTypeEntityDao();
        dao.insertOrReplaceInTx(dbEntities);
    }
}

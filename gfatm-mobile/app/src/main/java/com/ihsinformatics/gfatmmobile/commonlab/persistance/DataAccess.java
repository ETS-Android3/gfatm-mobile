package com.ihsinformatics.gfatmmobile.commonlab.persistance;

import android.content.Context;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnitDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrugDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequencyDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRouteDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntityDao;
import com.ihsinformatics.gfatmmobile.shared.Metadata;
import com.ihsinformatics.gfatmmobile.util.App;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;

import java.util.ArrayList;
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
        TestTypeEntityDao  dao= App.commonlabDAOSession.getTestTypeEntityDao();
        TestTypeEntity testTypeEntity = dao.queryBuilder()
                .where(TestTypeEntityDao.Properties.Uuid.eq(uuid))
                .unique();

        return testTypeEntity;
    }

    public TestOrderEntity getTestOrderByUUID(String uuid) {
        TestOrderEntityDao  dao= App.commonlabDAOSession.getTestOrderEntityDao();
        TestOrderEntity testOrderEntity = dao.queryBuilder()
                .where(TestOrderEntityDao.Properties.Uuid.eq(uuid))
                .unique();

        return testOrderEntity;
    }

    public List<TestOrderEntity> getTestOrderByPatientUUID(String uuid) {
        TestOrderEntityDao  dao= App.commonlabDAOSession.getTestOrderEntityDao();
        List<TestOrderEntity> testOrderEntity = dao.queryBuilder()
                .where(TestOrderEntityDao.Properties.PatientUUID.eq(uuid))
                .list();

        return testOrderEntity;
    }

    public AttributeTypeEntity getAttributeTypeByUUID(String uuid) {
        AttributeTypeEntityDao  dao= App.commonlabDAOSession.getAttributeTypeEntityDao();
        AttributeTypeEntity attributeTypeEntity = dao.queryBuilder()
                .where(AttributeTypeEntityDao.Properties.Uuid.eq(uuid))
                .unique();

        return attributeTypeEntity;
    }

    public List<AttributeTypeEntity> getAttributeTypesByTestType(Long id) {
        AttributeTypeEntityDao  dao= App.commonlabDAOSession.getAttributeTypeEntityDao();
        List<AttributeTypeEntity> attributeTypeEntities = dao.queryBuilder()
                .where(AttributeTypeEntityDao.Properties.TestTypeId.eq(id))
                .list();

        return attributeTypeEntities;
    }

    public List<AttributeEntity> getAttributesByTestOrder(Long id) {
        AttributeEntityDao  dao= App.commonlabDAOSession.getAttributeEntityDao();
        List<AttributeEntity> testOrderEntity = dao.queryBuilder()
                .where(AttributeEntityDao.Properties.TestOrderId.eq(id))
                .list();

        return testOrderEntity;
    }

    public List<TestTypeEntity> getAllTestTypes() {
        TestTypeEntityDao  dao= App.commonlabDAOSession.getTestTypeEntityDao();
        List<TestTypeEntity> testTypeEntity = dao.queryBuilder()
                .list();

        return testTypeEntity;
    }

    public List<MedicationDrug> getAllDrugs() {
        MedicationDrugDao  dao= App.commonlabDAOSession.getMedicationDrugDao();
        List<MedicationDrug> testTypeEntity = dao.queryBuilder()
                .list();

        return testTypeEntity;
    }

    public Object[][] getEncountersByPatient(Context context, String patientId) {
        DatabaseUtil dbUtil = new DatabaseUtil(context);
        Object[][] encounter = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation, dateCreated, uuid from " + Metadata.ENCOUNTER + " where patientId='" + patientId + "' order by encounterDatetime DESC, dateCreated DESC");
        return encounter;
    }

    public Object[][] getEncountersByUUID(Context context, String uuid) {
        DatabaseUtil dbUtil = new DatabaseUtil(context);
        Object[][] encounter = dbUtil.getFormTableData("select encounterType, encounter_id, patientId, encounterDatetime, encounterLocation, dateCreated, uuid from " + Metadata.ENCOUNTER + " where uuid='" + uuid + "' order by encounterDatetime DESC, dateCreated DESC");
        return encounter;
    }

    public void insertAllTestTypes(List<TestTypeEntity> dbTestTypeEntities) {
        TestTypeEntityDao testTypeDAO = com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getTestTypeEntityDao();
        List<TestTypeEntity> savables = new ArrayList<>();
        List<TestTypeEntity> updatables = new ArrayList<>();
        for(TestTypeEntity e: dbTestTypeEntities) {
            TestTypeEntity te = getTestTypeByUUID(e.getUuid());
            if(te == null) {
                savables.add(e);
            } else {
                e.setId(te.getId());
                updatables.add(e);
            }
        }

        testTypeDAO.insertInTx(savables);
        testTypeDAO.updateInTx(updatables);
    }

    public void insertAll(List<AttributeTypeEntity> dbEntities) {
        AttributeTypeEntityDao dao = com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getAttributeTypeEntityDao();
        List<AttributeTypeEntity> savables = new ArrayList<>();
        List<AttributeTypeEntity> updatables = new ArrayList<>();
        for(AttributeTypeEntity e: dbEntities) {
            AttributeTypeEntity te = getAttributeTypeByUUID(e.getUuid());
            if(te == null) {
                savables.add(e);
            } else {
                e.setId(te.getId());
                updatables.add(e);
            }
        }

        dao.insertInTx(savables);
        dao.updateInTx(updatables);
    }

    public void insertAllAttributes(List<AttributeEntity> dbEntities) {
        AttributeEntityDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getAttributeEntityDao();
        dao.insertOrReplaceInTx(dbEntities);
    }

    public void insertAllDrugs(List<MedicationDrug> dbEntities) {
        MedicationDrugDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getMedicationDrugDao();
        dao.insertOrReplaceInTx(dbEntities);
    }

    public void insertAllDoses(List<MedicationDoseUnit> dbEntities) {
        MedicationDoseUnitDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getMedicationDoseUnitDao();
        dao.insertOrReplaceInTx(dbEntities);
    }

    public void insertAllFrequencies(List<MedicationFrequency> dbEntities) {
        MedicationFrequencyDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getMedicationFrequencyDao();
        dao.insertOrReplaceInTx(dbEntities);
    }

    public void insertAllRoutes(List<MedicationRoute> dbEntities) {
        MedicationRouteDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getMedicationRouteDao();
        dao.insertOrReplaceInTx(dbEntities);
    }

    public long insertConcept(ConceptEntity dbEntity) {
        ConceptEntityDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getConceptEntityDao();
        return dao.insertOrReplace(dbEntity);
    }

    public void insertAllConcepts(List<ConceptEntity> dbEntities) {
        ConceptEntityDao dao= com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getConceptEntityDao();
        dao.insertOrReplaceInTx(dbEntities);
    }

    public ConceptEntity getConceptByUUID(String id) {
        ConceptEntityDao  dao= App.commonlabDAOSession.getConceptEntityDao();
        return dao.queryBuilder()
                .where(ConceptEntityDao.Properties.Uuid.eq(id))
                .unique();
    }

    public void insertAllOrders(List<TestOrderEntity> dbOrders) {
        TestOrderEntityDao dao = com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getTestOrderEntityDao();
        dao.insertOrReplaceInTx(dbOrders);
    }
}

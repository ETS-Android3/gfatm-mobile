package com.ihsinformatics.gfatmmobile.commonlab.persistance;

import android.content.Context;

import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.AttributeTypeEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.ConceptEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnitDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrugDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDuration;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequencyDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationOrderReason;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRouteDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntityDao;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntityDao;
import com.ihsinformatics.gfatmmobile.model.Concept;
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

    public MedicationDrug getDrugByUUID(String uuid) {
        MedicationDrugDao  dao= App.commonlabDAOSession.getMedicationDrugDao();
        MedicationDrug attributeTypeEntity = dao.queryBuilder()
                .where(MedicationDrugDao.Properties.Uuid.eq(uuid))
                .unique();

        return attributeTypeEntity;
    }

    public MedicationDrug getDrugByName(String name) {
        MedicationDrugDao  dao= App.commonlabDAOSession.getMedicationDrugDao();
        MedicationDrug attributeTypeEntity = dao.queryBuilder()
                .where(MedicationDrugDao.Properties.Name.eq(name))
                .unique();

        return attributeTypeEntity;
    }

    public List<MedicationDoseUnit> getAllDoses() {
        MedicationDoseUnitDao  dao= App.commonlabDAOSession.getMedicationDoseUnitDao();
        List<MedicationDoseUnit> testTypeEntity = dao.queryBuilder()
                .list();

        return testTypeEntity;
    }

    public List<MedicationFrequency> getAllFrequencies() {
        MedicationFrequencyDao  dao= App.commonlabDAOSession.getMedicationFrequencyDao();
        List<MedicationFrequency> testTypeEntity = dao.queryBuilder()
                .list();

        return testTypeEntity;
    }

    public List<MedicationRoute> getAllRoutes() {
        MedicationRouteDao  dao= App.commonlabDAOSession.getMedicationRouteDao();
        List<MedicationRoute> testTypeEntity = dao.queryBuilder()
                .list();

        return testTypeEntity;
    }

    public MedicationDoseUnit getDoseUnitByName(String name) {
        MedicationDoseUnitDao  dao= App.commonlabDAOSession.getMedicationDoseUnitDao();
        MedicationDoseUnit attributeTypeEntity = dao.queryBuilder()
                .where(MedicationDoseUnitDao.Properties.Display.eq(name))
                .unique();

        return attributeTypeEntity;
    }

    public MedicationDoseUnit getDoseUnitByUUID(String uuid) {
        MedicationDoseUnitDao  dao= App.commonlabDAOSession.getMedicationDoseUnitDao();
        MedicationDoseUnit attributeTypeEntity = dao.queryBuilder()
                .where(MedicationDoseUnitDao.Properties.Uuid.eq(uuid))
                .unique();

        return attributeTypeEntity;
    }

    public MedicationFrequency getFrequencyByName(String s) {
        MedicationFrequencyDao  dao= App.commonlabDAOSession.getMedicationFrequencyDao();
        MedicationFrequency attributeTypeEntity = dao.queryBuilder()
                .where(MedicationFrequencyDao.Properties.Display.eq(s))
                .unique();

        return attributeTypeEntity;
    }

    public MedicationFrequency getFrequencyByUUID(String s) {
        MedicationFrequencyDao  dao= App.commonlabDAOSession.getMedicationFrequencyDao();
        MedicationFrequency attributeTypeEntity = dao.queryBuilder()
                .where(MedicationFrequencyDao.Properties.Uuid.eq(s))
                .unique();

        return attributeTypeEntity;
    }

    public MedicationRoute getRouteByName(String s) {
        MedicationRouteDao  dao= App.commonlabDAOSession.getMedicationRouteDao();
        MedicationRoute attributeTypeEntity = dao.queryBuilder()
                .where(MedicationRouteDao.Properties.Display.eq(s))
                .unique();

        return attributeTypeEntity;
    }

    public MedicationRoute getRouteByUUID(String s) {
        MedicationRouteDao  dao= App.commonlabDAOSession.getMedicationRouteDao();
        MedicationRoute attributeTypeEntity = dao.queryBuilder()
                .where(MedicationRouteDao.Properties.Uuid.eq(s))
                .unique();

        return attributeTypeEntity;
    }

    public List<MedicationDuration> getAllDurations() {
        List<MedicationDuration> durations = new ArrayList<>();
        durations.add(new MedicationDuration(1l, "1072AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "DAYS"));
        durations.add(new MedicationDuration(2l, "1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "WEEKS"));
        durations.add(new MedicationDuration(3l, "1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "MONTHS"));

        return durations;
    }

    public MedicationDuration getDurationByName(String name) {
        if(name.equalsIgnoreCase("days"))
            return new MedicationDuration(1l, "1072AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "DAYS");
        else if(name.equalsIgnoreCase("weeks"))
            return new MedicationDuration(2l, "1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "WEEKS");
        else if(name.equalsIgnoreCase("months"))
            return new MedicationDuration(3l, "1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "MONTHS");

        return null;
    }

    public MedicationDuration getDurationByUUID(String uuid) {
        if(uuid.equalsIgnoreCase("1072AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))
            return new MedicationDuration(1l, "1072AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "DAYS");
        else if(uuid.equalsIgnoreCase("1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))
            return new MedicationDuration(2l, "1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "WEEKS");
        else if(uuid.equalsIgnoreCase("1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))
            return new MedicationDuration(3l, "1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "MONTHS");

        return null;
    }

    public List<MedicationOrderReason> getAllOrderReasons() {
        List<MedicationOrderReason> durations = new ArrayList<>();
        durations.add(new MedicationOrderReason(1l, "d8126c9d-824e-4946-a015-df16239e31c5", "PLANNED CHANGE"));
        durations.add(new MedicationOrderReason(2l, "7d97c483-935a-4a0b-ac4d-d7ac7ff682f0", "ADVERSE EVENTS"));
        durations.add(new MedicationOrderReason(3l, "151685AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "DRUG RESISTANCE"));
        durations.add(new MedicationOrderReason(4l, "58a15157-e917-453f-b4d8-0a99553e6e27", "DRUG SUPPLY AND ADMINISTRATION ISSUE"));
        durations.add(new MedicationOrderReason(5l, "a80d2fa1-3d10-415a-82f8-7df4f8fdf49b", "REINTRODUCTION/REPLACEMENT OF STOPPED DRUG"));
        durations.add(new MedicationOrderReason(6l, "e581e00d-b795-44d1-a01d-03487400f0a1", "OTHER REASON DRUG STOPPED"));

        return durations;
    }

    public MedicationOrderReason getOrderReasonByUUID(String name) {
        if(name.equalsIgnoreCase("d8126c9d-824e-4946-a015-df16239e31c5"))
            return new MedicationOrderReason(1l, "d8126c9d-824e-4946-a015-df16239e31c5", "PLANNED CHANGE");
        else if(name.equalsIgnoreCase("7d97c483-935a-4a0b-ac4d-d7ac7ff682f0"))
            return new MedicationOrderReason(2l, "7d97c483-935a-4a0b-ac4d-d7ac7ff682f0", "ADVERSE EVENTS");
        else if(name.equalsIgnoreCase("151685AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"))
            return new MedicationOrderReason(3l, "151685AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "DRUG RESISTANCE");
        else if(name.equalsIgnoreCase("58a15157-e917-453f-b4d8-0a99553e6e27"))
            return new MedicationOrderReason(4l, "58a15157-e917-453f-b4d8-0a99553e6e27", "DRUG SUPPLY AND ADMINISTRATION ISSUE");
        else if(name.equalsIgnoreCase("a80d2fa1-3d10-415a-82f8-7df4f8fdf49b"))
            return new MedicationOrderReason(5l, "a80d2fa1-3d10-415a-82f8-7df4f8fdf49b", "REINTRODUCTION/REPLACEMENT OF STOPPED DRUG");
        else if(name.equalsIgnoreCase("e581e00d-b795-44d1-a01d-03487400f0a1"))
            return new MedicationOrderReason(6l, "e581e00d-b795-44d1-a01d-03487400f0a1", "OTHER REASON DRUG STOPPED");

        return null;
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

    public void insertAllDrugOrders(List<DrugOrderEntity> dbOrders) {
        DrugOrderEntityDao dao = com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getDrugOrderEntityDao();
        dao.insertOrReplaceInTx(dbOrders);
    }

    public void updateDrugOrder(DrugOrderEntity dbOrder) {
        DrugOrderEntityDao dao = com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getDrugOrderEntityDao();
        dao.update(dbOrder);
    }

    public void saveDrugOrder(DrugOrderEntity dbOrder) {
        DrugOrderEntityDao dao = com.ihsinformatics.gfatmmobile.util.App.commonlabDAOSession.getDrugOrderEntityDao();
        dao.save(dbOrder);
    }

    public List<DrugOrderEntity> getDrugOrdersByPatientUUID(String uuid) {
        DrugOrderEntityDao  dao = App.commonlabDAOSession.getDrugOrderEntityDao();
        List<DrugOrderEntity> drugOrderEntities = dao.queryBuilder()
                .where(DrugOrderEntityDao.Properties.PatientUUID.eq(uuid))
                .list();

        return drugOrderEntities;
    }

    public DrugOrderEntity getDrugOrderByID(long id) {
        DrugOrderEntityDao  dao = App.commonlabDAOSession.getDrugOrderEntityDao();
        DrugOrderEntity drugOrderEntities = dao.queryBuilder()
                .where(DrugOrderEntityDao.Properties.Id.eq(id))
                .unique();

        return drugOrderEntities;
    }

}

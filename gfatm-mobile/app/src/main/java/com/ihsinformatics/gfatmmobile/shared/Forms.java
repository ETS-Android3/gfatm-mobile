package com.ihsinformatics.gfatmmobile.shared;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesBloodSugarForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesCreatinineTestForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesEducationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesMellitusScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesHbA1CForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesLipidTestForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesPatientInformationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesUrineDetailedReportForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesUrineMicroalbuminForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesVitalsForm;
import com.ihsinformatics.gfatmmobile.fast.FastAfbSmearMicroscopyOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastGeneXpertResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastGpxSpecimenCollectionForm;
import com.ihsinformatics.gfatmmobile.fast.FastPatientLocationForm;
import com.ihsinformatics.gfatmmobile.fast.FastPresumptiveForm;
import com.ihsinformatics.gfatmmobile.fast.FastPresumptiveInformationForm;
import com.ihsinformatics.gfatmmobile.fast.FastPromptForm;
import com.ihsinformatics.gfatmmobile.fast.FastScreeningChestXrayOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.AdverseEventForm;
import com.ihsinformatics.gfatmmobile.pet.BaselineCounsellingForm;
import com.ihsinformatics.gfatmmobile.pet.BaselineScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.ClinicianContactScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.ClinicianFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.ContactRegistryForm;
import com.ihsinformatics.gfatmmobile.pet.CounsellingFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.EndOfFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.IncentiveDisbursementForm;
import com.ihsinformatics.gfatmmobile.pet.IndexPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pet.InfectionTreatmentEligibilityForm;
import com.ihsinformatics.gfatmmobile.pet.MonthlyHomeFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.SocioecnomicDataForm;
import com.ihsinformatics.gfatmmobile.pet.TreatmentAdherenceForm;
import com.ihsinformatics.gfatmmobile.pet.TreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtProviderRegistration;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/20/2016.
 */

public class Forms{

    /********************************
     * PET
     ********************************/
    public static final String PET_INDEX_PATIENT_REGISTRATION = "Index Patient Registration";
    public static final String PET_CONTACT_REGISTRY = "Contact Registry";
    public static final String PET_BASELINE_SCREENING = "Baseline Screening";
    public static final String PET_SOCIO_ECNOMICS_DATA = "Socioeconomic Data";
    public static final String PET_CLINICIAN_CONTACT_SCREENING = "Clinician Contact Screening";
    public static final String PET_INFECTION_TREATMENT_ELIGIBILITY = "Infection Treatment Eligibility";
    public static final String PET_TREATMENT_INITIATION = "Treatment Initiation";
    public static final String PET_BASELINE_COUNSELLING = "Baseline Counselling";
    public static final String PET_TREATMENT_ADHERENCE = "Treatment Adherence";
    public static final String PET_MONTHLY_HOME_FOLLOWUP = "Monthly Home Follow-Up";
    public static final String PET_CLINICIAN_FOLLOWUP = "Clinician Follow-Up";
    public static final String PET_COUNSELLING_FOLLOWUP = "Counselling Follow-Up";
    public static final String PET_ADVERSE_EVENTS = "Adverse Events";
    public static final String PET_INCENTIVE_DISBURSEMENT = "Incentive Disbursement";
    public static final String PET_END_FOLLOWOUP = "End of Follow-up";
    public static final FormsObject pet_indexPatientRegistration = new FormsObject(PET_INDEX_PATIENT_REGISTRATION, IndexPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject pet_contactRegistry = new FormsObject(PET_CONTACT_REGISTRY, ContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject pet_baselineScreening = new FormsObject(PET_BASELINE_SCREENING, BaselineScreeningForm.class, R.drawable.pet_baseline_screening, FormTypeColor.SCREENING_FORM);
    public static final FormsObject pet_socioEcnomicData = new FormsObject(PET_SOCIO_ECNOMICS_DATA, SocioecnomicDataForm.class, R.drawable.pet_socio_ecnomic_data, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject pet_clinicianContactScreening = new FormsObject(PET_CLINICIAN_CONTACT_SCREENING, ClinicianContactScreeningForm.class, R.drawable.pet_clinician_contact_screening, FormTypeColor.SCREENING_FORM);
    public static final FormsObject pet_infectionTreatmenEligibility = new FormsObject(PET_INFECTION_TREATMENT_ELIGIBILITY, InfectionTreatmentEligibilityForm.class, R.drawable.pet_infection_treatment_eligibility, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject pet_treatmentInitiation = new FormsObject(PET_TREATMENT_INITIATION, TreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject pet_baselineCounselling = new FormsObject(PET_BASELINE_COUNSELLING, BaselineCounsellingForm.class, R.drawable.pet_baseline_counselling, FormTypeColor.SCREENING_FORM);
    public static final FormsObject pet_treatmentAdherence = new FormsObject(PET_TREATMENT_ADHERENCE, TreatmentAdherenceForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject pet_monthlyHomeFollowup = new FormsObject(PET_MONTHLY_HOME_FOLLOWUP, MonthlyHomeFollowupForm.class, R.drawable.pet_monthly_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject pet_clinicianFollowup = new FormsObject(PET_CLINICIAN_FOLLOWUP, ClinicianFollowupForm.class, R.drawable.pet_clinician_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject pet_counsellingFollowup = new FormsObject(PET_COUNSELLING_FOLLOWUP, CounsellingFollowupForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject pet_adverseEvents = new FormsObject(PET_ADVERSE_EVENTS, AdverseEventForm.class, R.drawable.pet_adverse_events, FormTypeColor.OTHER_FORM);
    public static final FormsObject pet_incentiveDisbursement = new FormsObject(PET_INCENTIVE_DISBURSEMENT, IncentiveDisbursementForm.class, R.drawable.pet_incentive_disbursement, FormTypeColor.OTHER_FORM);
    public static final FormsObject pet_endOfFollowup = new FormsObject(PET_END_FOLLOWOUP, EndOfFollowupForm.class, R.drawable.pet_followup_end, FormTypeColor.FOLLOWUP_FORM);

    /********************************
     * FAST
     ********************************/
    //Fast
    public static final String FAST_SCREENING_FORM = "Screening Form";
    public static final String FAST_PRESUMPTIVE_FORM = "Presumptive Form";
    public static final String FAST_PROMPT_FORM = "Sputum Container and X-Ray Voucher Form";
    public static final String FAST_PATIENT_LOCATION_FORM = "Patient Location Form";
    public static final String FAST_GXP_SPECIMEN_COLLECTION_FORM = "GXP Specimen Collection Form";
    public static final String FAST_PRESUMPTIVE_INFORMATION_FORM = "Presumptive Information Form";
    public static final String FAST_GENEXPERT_RESULT_FORM = "GeneXpert Result Form";
    public static final String FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM = "Screening Chest X-Ray(CAD4TB) Order and Result Form";
    public static final String FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM = "AFB Smear Microscopy Order and Result Form";
    public static final FormsObject fastScreeningForm = new FormsObject(FAST_SCREENING_FORM, FastScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPromptForm = new FormsObject(FAST_PROMPT_FORM, FastPromptForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPresumptiveForm = new FormsObject(FAST_PRESUMPTIVE_FORM, FastPresumptiveForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPatientLocationForm = new FormsObject(FAST_PATIENT_LOCATION_FORM, FastPatientLocationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastGpxSpecimenCollectionForm = new FormsObject(FAST_GXP_SPECIMEN_COLLECTION_FORM, FastGpxSpecimenCollectionForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPresumptiveInformationForm = new FormsObject(FAST_PRESUMPTIVE_INFORMATION_FORM, FastPresumptiveInformationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastGeneXpertResultForm = new FormsObject(FAST_GENEXPERT_RESULT_FORM, FastGeneXpertResultForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastScreeningChestXrayOrderAndResultForm = new FormsObject(FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM, FastScreeningChestXrayOrderAndResultForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastAfbSmearMicroscopyOrderAndResultForm = new FormsObject(FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM, FastAfbSmearMicroscopyOrderAndResultForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

    /********************************
     * COMORBIDITIES
     ********************************/
    public static final String COMORBIDITIES_INDEX_PATIENT_REGISTRATION = "Index Patient Registration";
    public static final String COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM = "Screening Form Mental Health";
    public static final String COMORBIDITIES_DIABETES_MELLITUS_SCREENING_FORM = "Screening Form Diabetes Mellitus";
    public static final String COMORBIDITIES_HbA1C_FORM = "HbA1C Form";
    public static final String COMORBIDITIES_BLOOD_SUGAR_FORM = "Blood Sugar Form";
    public static final String COMORBIDITIES_VITALS_FORM = "Vitals Form";
    public static final String COMORBIDITIES_DIABETES_TREATMENT_INITIATION = "Diabetes Mellitus Treatment Initiation";
    public static final String COMORBIDITIES_URINE_DR_FORM = "Urine DR Form";
    public static final String COMORBIDITIES_CREATININE_TEST_FORM = "Creatinine Test Form";
    public static final String COMORBIDITIES_LIPID_TEST_FORM = "Lipid Test Form";
    public static final String COMORBIDITIES_MICROALBUMIN_TEST_FORM = "Urine Microalbumin Form";
    public static final String COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM = "Diabetes Treatment Follow-up Form";
    public static final String COMORBIDITIES_DIABETES_EDUCATION_FORM = "Diabetes Education Form";
    /********************************
     * PMDT
     ********************************/
    public static final FormsObject indexPatientRegistration = new FormsObject("Providers Registration Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String PMDT_PATIENT_REGISTRAITON = "Patient Registration";
    public static final String PMDT_PROVIDER_REGISTRAITON = "Provider Registration";
    public static final FormsObject patientRegistration = new FormsObject(PMDT_PATIENT_REGISTRAITON, PmdtPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject providerRegistration = new FormsObject(PMDT_PROVIDER_REGISTRAITON, PmdtProviderRegistration.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_indexPatientRegistration = new FormsObject("Patient Information Form", ComorbiditiesPatientInformationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_mentalHealthScreening = new FormsObject("Mental Health Screening", ComorbiditiesMentalHealthScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesMellitusScreening = new FormsObject("Diabetes Mellitus Screening", ComorbiditiesDiabetesMellitusScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_hbA1cForm = new FormsObject("HbA1C Test Form", ComorbiditiesHbA1CForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_bloodSugarForm = new FormsObject("Blood Sugar Test Form", ComorbiditiesBloodSugarForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_vitalsForm = new FormsObject("Vitals Form", ComorbiditiesVitalsForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesTreatmentInitiationForm = new FormsObject("Diabetes Treatment Initiation", ComorbiditiesDiabetesTreatmentInitiationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_urineDRForm = new FormsObject("Urine Test DR", ComorbiditiesUrineDetailedReportForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_creatinineTestForm = new FormsObject("Creatinine Test Form", ComorbiditiesCreatinineTestForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_lipidTestForm = new FormsObject("Lipid Test Form", ComorbiditiesLipidTestForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_microalbuminTestForm = new FormsObject("Urine Microalbumin Form", ComorbiditiesUrineMicroalbuminForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesTreatmentFollowupForm = new FormsObject("Diabetes Treatment Follow-up Form", ComorbiditiesDiabetesTreatmentFollowupForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesEducationForm = new FormsObject("Diabetes Education Form", ComorbiditiesDiabetesEducationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

    /********************************************************************************************************************************/
    public static ArrayList<FormsObject> getPETFormList() {

        ArrayList<FormsObject> petList = new ArrayList<>();

        petList.add(pet_indexPatientRegistration);
        petList.add(pet_contactRegistry);
        petList.add(pet_baselineScreening);
        petList.add(pet_socioEcnomicData);
        petList.add(pet_clinicianContactScreening);
        petList.add(pet_infectionTreatmenEligibility);
        petList.add(pet_treatmentInitiation);
        petList.add(pet_baselineCounselling);
        petList.add(pet_treatmentAdherence);
        petList.add(pet_monthlyHomeFollowup);
        petList.add(pet_clinicianFollowup);
        petList.add(pet_counsellingFollowup);
        petList.add(pet_adverseEvents);
        petList.add(pet_incentiveDisbursement);
        petList.add(pet_endOfFollowup);

        return petList;

    }

    public static ArrayList<FormsObject> getFASTFormList() {

        ArrayList<FormsObject> fastList = new ArrayList<>();

        fastList.add(fastScreeningForm);
        fastList.add(fastPromptForm);
        fastList.add(fastPresumptiveForm);
        fastList.add(fastPatientLocationForm);
        fastList.add(fastGpxSpecimenCollectionForm);
        fastList.add(fastPresumptiveInformationForm);
        fastList.add(fastGeneXpertResultForm);
        fastList.add(fastScreeningChestXrayOrderAndResultForm);
        fastList.add(fastAfbSmearMicroscopyOrderAndResultForm);

        return fastList;

    }

    /********************************************************************************************************************************/

    public static ArrayList<FormsObject> getCommorbiditiesFormList() {

        ArrayList<FormsObject> comorbiditiesList = new ArrayList<>();

        comorbiditiesList.add(comorbidities_indexPatientRegistration);
        comorbiditiesList.add(comorbidities_mentalHealthScreening);
        comorbiditiesList.add(comorbidities_diabetesMellitusScreening);
        comorbiditiesList.add(comorbidities_hbA1cForm);
        comorbiditiesList.add(comorbidities_bloodSugarForm);
        comorbiditiesList.add(comorbidities_vitalsForm);
        comorbiditiesList.add(comorbidities_diabetesTreatmentInitiationForm);
        comorbiditiesList.add(comorbidities_urineDRForm);
        comorbiditiesList.add(comorbidities_creatinineTestForm);
        comorbiditiesList.add(comorbidities_lipidTestForm);
        comorbiditiesList.add(comorbidities_microalbuminTestForm);
        comorbiditiesList.add(comorbidities_diabetesTreatmentFollowupForm);
        comorbiditiesList.add(comorbidities_diabetesEducationForm);

        return comorbiditiesList;

    }

    public static ArrayList<FormsObject> getPMDTFormList() {

        ArrayList<FormsObject> pmdtList = new ArrayList<>();

        pmdtList.add(patientRegistration);
        pmdtList.add(providerRegistration);

        return pmdtList;

    }
    /********************************************************************************************************************************/

    /******************************** CHILDHOOD TB ********************************/
    public static ArrayList<FormsObject> getChildhoodTBFormList() {

        ArrayList<FormsObject> childhoodtbList = new ArrayList<>();

        childhoodtbList.add(indexPatientRegistration);


        return childhoodtbList;

    }
    /********************************************************************************************************************************/



}

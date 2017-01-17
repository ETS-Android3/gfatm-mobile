package com.ihsinformatics.gfatmmobile.shared;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.comorbidities.BloodSugarForm;
import com.ihsinformatics.gfatmmobile.comorbidities.HbA1CForm;
import com.ihsinformatics.gfatmmobile.comorbidities.PatientInformationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ScreeningFormMHDM;
import com.ihsinformatics.gfatmmobile.fast.ScreeningForm;
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
    public static final String PET_SOCIO_ECNOMICS_DATA = "Socio Economics Data";
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
    public static final String FAST_SCREENING_FORM = "Screening Form";

    /********************************************************************************************************************************/
    public static final FormsObject fast_screeningForm = new FormsObject(FAST_SCREENING_FORM, ScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject contactRegistry = new FormsObject("Presumptive Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject baselineScreening = new FormsObject("Sputum Container and X-Ray Voucher Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    /********************************
     * COMORBIDITIES
     ********************************/
    public static final String COMORBIDITIES_INDEX_PATIENT_REGISTRATION = "Patient Information Form";
    public static final String COMORBIDITIES_SCREENING_FORM = "Screening Form Mental Health \n and Diabetes Mellitus";
    /********************************************************************************************************************************/
    public static final String COMORBIDITIES_HbA1C_FORM = "HbA1C Form";
    public static final String COMORBIDITIES_BLOOD_SUGAR_FORM = "Blood Sugar Form";
    public static final FormsObject comorbidities_indexPatientRegistration = new FormsObject(COMORBIDITIES_INDEX_PATIENT_REGISTRATION, PatientInformationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject comorbidities_screeningForm = new FormsObject(COMORBIDITIES_SCREENING_FORM, ScreeningFormMHDM.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject comorbidities_hbA1CForm = new FormsObject(COMORBIDITIES_HbA1C_FORM, HbA1CForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject comorbidities_bloodSugarForm = new FormsObject(COMORBIDITIES_BLOOD_SUGAR_FORM, BloodSugarForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    /********************************
     * PMDT
     ********************************/
    public static final FormsObject indexPatientRegistration = new FormsObject("Providers Registration Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

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

        fastList.add(fast_screeningForm);
        fastList.add(contactRegistry);
        fastList.add(baselineScreening);

        return fastList;

    }

    /********************************************************************************************************************************/

    public static ArrayList<FormsObject> getCommorbiditiesFormList() {

        ArrayList<FormsObject> comorbiditiesList = new ArrayList<>();

        comorbiditiesList.add(comorbidities_indexPatientRegistration);
        comorbiditiesList.add(comorbidities_screeningForm);
        comorbiditiesList.add(comorbidities_hbA1CForm);
        comorbiditiesList.add(comorbidities_bloodSugarForm);

        return comorbiditiesList;

    }

    public static ArrayList<FormsObject> getPMDTFormList() {

        ArrayList<FormsObject> pmdtList = new ArrayList<>();

        pmdtList.add(indexPatientRegistration);
        pmdtList.add(contactRegistry);
        pmdtList.add(baselineScreening);

        return pmdtList;

    }
    /********************************************************************************************************************************/

    /******************************** CHILDHOOD TB ********************************/
    public static ArrayList<FormsObject> getChildhoodTBFormList() {

        ArrayList<FormsObject> childhoodtbList = new ArrayList<>();

        childhoodtbList.add(indexPatientRegistration);
        childhoodtbList.add(contactRegistry);
        childhoodtbList.add(baselineScreening);

        return childhoodtbList;

    }
    /********************************************************************************************************************************/



}

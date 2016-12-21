package com.ihsinformatics.gfatmmobile.shared;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.comorbidities.PatientInformationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ScreeningFormMHDM;
import com.ihsinformatics.gfatmmobile.pet.IndexPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pet.test;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/20/2016.
 */

public class Forms {

    public static final String INDEX_PATIENT_REGISTRATION = "Index Patient Registration";
    public static final String COMORBIDITIES_SCREENING_FORM = "Screening Form Mental Health And DM";

    public static ArrayList<FormsObject> getPETFormList() {

        ArrayList<FormsObject> petList = new ArrayList<>();

        FormsObject indexPatientRegistration = new FormsObject(INDEX_PATIENT_REGISTRATION, IndexPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject contactRegistry = new FormsObject("Contact Registry", test.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM);
        FormsObject baselineScreening = new FormsObject("Baseline Screening", AbstractFormActivity.class, R.drawable.pet_baseline_screening, FormTypeColor.SCREENING_FORM);
        FormsObject clinicianContactScreening = new FormsObject("Clinician Contact Screening", AbstractFormActivity.class, R.drawable.pet_clinician_contact_screening, FormTypeColor.SCREENING_FORM);
        FormsObject infectionTreatmenEligibility = new FormsObject("Infection Treatment Eligibility", AbstractFormActivity.class, R.drawable.pet_infection_treatment_eligibility, FormTypeColor.TREATMENT_FORM);
        FormsObject treatmentInitiation = new FormsObject("Treatment Initiation", AbstractFormActivity.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM);
        FormsObject baselineCounselling = new FormsObject("Baseline Counselling", AbstractFormActivity.class, R.drawable.pet_baseline_counselling, FormTypeColor.SCREENING_FORM);
        FormsObject treatmentAdherence = new FormsObject("Treatment Adherence", AbstractFormActivity.class, R.drawable.pet_treatment_adherence, FormTypeColor.TREATMENT_FORM);
        FormsObject monthlyHomeFollowup = new FormsObject("Monthly Home Follow-Up", AbstractFormActivity.class, R.drawable.pet_monthly_followup, FormTypeColor.FOLLOWUP_FORM);
        FormsObject clinicianFollowup = new FormsObject("Clinician Follow-Up", AbstractFormActivity.class, R.drawable.pet_clinician_followup, FormTypeColor.FOLLOWUP_FORM);
        FormsObject counsellingFollowup = new FormsObject("Counselling Follow-up", AbstractFormActivity.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
        FormsObject adverseEvents = new FormsObject("Adverse Events", AbstractFormActivity.class, R.drawable.pet_adverse_events, FormTypeColor.OTHER_FORM);
        FormsObject incentiveDisbursement = new FormsObject("Incentive Disbursement", AbstractFormActivity.class, R.drawable.pet_incentive_disbursement, FormTypeColor.OTHER_FORM);
        FormsObject endOfFollowup = new FormsObject("End of Follow-up", AbstractFormActivity.class, R.drawable.pet_followup_end, FormTypeColor.FOLLOWUP_FORM);

        petList.add(indexPatientRegistration);
        petList.add(contactRegistry);
        petList.add(baselineScreening);
        petList.add(clinicianContactScreening);
        petList.add(infectionTreatmenEligibility);
        petList.add(treatmentInitiation);
        petList.add(baselineCounselling);
        petList.add(treatmentAdherence);
        petList.add(monthlyHomeFollowup);
        petList.add(clinicianFollowup);
        petList.add(counsellingFollowup);
        petList.add(adverseEvents);
        petList.add(incentiveDisbursement);
        petList.add(endOfFollowup);

        return petList;

    }

    public static ArrayList<FormsObject> getPMDTFormList() {

        ArrayList<FormsObject> pmdtList = new ArrayList<>();

        FormsObject indexPatientRegistration = new FormsObject("Providers Registration Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject contactRegistry = new FormsObject("Treatment Coordinator BMU Visit Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject baselineScreening = new FormsObject("Treatment Coordinator BMU Visit Outcome Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

        pmdtList.add(indexPatientRegistration);
        pmdtList.add(contactRegistry);
        pmdtList.add(baselineScreening);

        return pmdtList;

    }

    public static ArrayList<FormsObject> getFASTFormList() {

        ArrayList<FormsObject> fastList = new ArrayList<>();

        FormsObject indexPatientRegistration = new FormsObject("Screening Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject contactRegistry = new FormsObject("Presumptive Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject baselineScreening = new FormsObject("Sputum Container and X-Ray Voucher Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

        fastList.add(indexPatientRegistration);
        fastList.add(contactRegistry);
        fastList.add(baselineScreening);

        return fastList;

    }

    public static ArrayList<FormsObject> getChildhoodTBFormList() {

        ArrayList<FormsObject> childhoodtbList = new ArrayList<>();

        FormsObject indexPatientRegistration = new FormsObject("Verbal Screening Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject contactRegistry = new FormsObject("Screening Location Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject baselineScreening = new FormsObject("Patient Registration Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

        childhoodtbList.add(indexPatientRegistration);
        childhoodtbList.add(contactRegistry);
        childhoodtbList.add(baselineScreening);

        return childhoodtbList;

    }


    public static ArrayList<FormsObject> getCommorbiditiesFormList() {

        ArrayList<FormsObject> comorbiditiesList = new ArrayList<>();

        FormsObject indexPatientRegistration = new FormsObject("Patient Information Form", PatientInformationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject contactRegistry = new FormsObject("Screening Mental Health and DM", ScreeningFormMHDM.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
        FormsObject baselineScreening = new FormsObject("HbA1C Form", AbstractFormActivity.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

        comorbiditiesList.add(indexPatientRegistration);
        comorbiditiesList.add(contactRegistry);
        comorbiditiesList.add(baselineScreening);

        return comorbiditiesList;

    }

}

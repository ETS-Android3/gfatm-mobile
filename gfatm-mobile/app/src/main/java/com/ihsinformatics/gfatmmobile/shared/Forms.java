package com.ihsinformatics.gfatmmobile.shared;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbAntibioticFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbIPTFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbPPAScore;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbPresumptiveCaseConfirmation;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbSupplementDispersement;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbTestIndicationForm;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbTreatmentFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbTreatmentInitiation;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbVerbalScreeningForm;
import com.ihsinformatics.gfatmmobile.common.AfbSmearOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.common.BaselineCounselingForm;
import com.ihsinformatics.gfatmmobile.common.CTScanOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.common.ClinicianEvaluation;
import com.ihsinformatics.gfatmmobile.common.ContactRegistryForm;
import com.ihsinformatics.gfatmmobile.common.DSTOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.common.DeceasedPatientSummary;
import com.ihsinformatics.gfatmmobile.common.EndOfFollowupForm;
import com.ihsinformatics.gfatmmobile.common.FollowupCounselingForm;
import com.ihsinformatics.gfatmmobile.common.GeneXpertResultForm;
import com.ihsinformatics.gfatmmobile.common.GeneralCounsellingForm;
import com.ihsinformatics.gfatmmobile.common.GpxSpecimenCollectionForm;
import com.ihsinformatics.gfatmmobile.common.HistopathologyOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.common.MantouxOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.common.MissedVisitFollowupForm;
import com.ihsinformatics.gfatmmobile.common.PatientInformationForm;
import com.ihsinformatics.gfatmmobile.common.ReferralAndTransferForm;
import com.ihsinformatics.gfatmmobile.common.ScreeningChestXrayOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.common.TreatmentAdherenceForm;
import com.ihsinformatics.gfatmmobile.common.UltrasoundTestOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesEndOfTreatmentMentalHealthForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthAssessmentForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthScreening;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsAFBCultureResultForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsBloodSampleCollectionForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsChildScreeningForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsEnumerationForm;
import com.ihsinformatics.gfatmmobile.fast.FastPresumptiveForm;
import com.ihsinformatics.gfatmmobile.fast.FastPromptForm;
import com.ihsinformatics.gfatmmobile.fast.FastScreeningForm;
import com.ihsinformatics.gfatmmobile.fast.FastTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.fast.FastTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.pet.PETAKUADForm;
import com.ihsinformatics.gfatmmobile.pet.PETHomeVisitForm;
import com.ihsinformatics.gfatmmobile.pet.PetAdverseEventForm;
import com.ihsinformatics.gfatmmobile.pet.PetBaselineCounsellingForm;
import com.ihsinformatics.gfatmmobile.pet.PetBaselineScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.PetClinicianContactScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.PetClinicianFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetCounsellingFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetIncentiveDisbursementForm;
import com.ihsinformatics.gfatmmobile.pet.PetIndexPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pet.PetInfectionTreatmentEligibilityForm;
import com.ihsinformatics.gfatmmobile.pet.PetMonthlyHomeFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetRefusalForm;
import com.ihsinformatics.gfatmmobile.pet.PetRetrivelForm;
import com.ihsinformatics.gfatmmobile.pet.PetSocioecnomicDataForm;
import com.ihsinformatics.gfatmmobile.pet.PetTestIndicationForm;
import com.ihsinformatics.gfatmmobile.pet.PetTreatmentAdherenceForm;
import com.ihsinformatics.gfatmmobile.pet.PetTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsGeneXpertResultForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsPresumptiveInformationChildForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsPresumptiveInformationForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsSampleCollectionForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsScreeningCXR;
import com.ihsinformatics.gfatmmobile.ztts.ZttsScreeningForm;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/20/2016.
 */

public class Forms{

    /********************************
     * Common
     ********************************/
    public static final String PATIENT_INFORMATION_FORM = "Patient Information";
    public static final String GXP_SPECIMEN_COLLECTION_FORM = "GXP Specimen Collection";
    public static final String GENEXPERT_RESULT_FORM = "GeneXpert Result";
    public static final String SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM = "CXR Order and Result";
    public static final String REFERRAL_AND_TRANSFER_FORM = "Referral and Transfer";
    public static final String CONTACT_REGISTRY = "Contact Registry";
    public static final String END_OF_FOLLOWUP = "End of Followup";
    public static final String AFB_SMEAR_ORDER_AND_RESULT_FORM = "AFB Smear Order & Result";
    public static final String DST_CULTURE_TEST = "DST Order & Result";
    public static final String ULTRASOUND_TEST = "Ultrasound Order & Result";
    public static final String CT_SCAN_TEST = "CT Scan Order & Result";
    public static final String MANTOUX_TEST = "Mantoux Order & Result";
    public static final String HISTOPATHOLOGY_TEST = "Histopathology Order & Result";
    public static final String MISSED_VISIT_FOLLOWUP = "Missed Visit Followup";
    public static final String CLINICIAN_EVALUATION_FORM = "Clinician Evaluation";
    public static final String GENERAL_COUNSELLING = "CC - General Counselling";
    public static final String TREATMENT_ADHERENCE = "CC-Treatment Adherence";
    public static final String FOLLOWUP_COUNSELING = "Follow-up Counselling";
    public static final String BASELINE_COUNSELING = "Baseline Counselling";
    public static final String DECEASED_PATIENT_SUMMARY = "Deceased Patient Summary";

    public static final FormsObject patientInformationForm = new FormsObject(PATIENT_INFORMATION_FORM, PatientInformationForm.class, R.drawable.fast_presumptive_information_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ALL}, -1, -1);
    public static final FormsObject gxpSpecimenCollectionForm = new FormsObject(GXP_SPECIMEN_COLLECTION_FORM, GpxSpecimenCollectionForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_LAB_TECHNICIAN, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_LAB_TECHNICIAN, Roles.CHILDHOODTB_MEDICAL_OFFICER,
            Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER}, -1, -1);
    public static final FormsObject geneXpertResultForm = new FormsObject(GENEXPERT_RESULT_FORM, GeneXpertResultForm.class, R.drawable.fast_result_form, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_LAB_TECHNICIAN, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_LAB_TECHNICIAN, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject screeningChestXrayOrderAndResultForm = new FormsObject(SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM, ScreeningChestXrayOrderAndResultForm.class, R.drawable.ctb_xray, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_LAB_TECHNICIAN, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER
    }, -1, -1);
    public static final FormsObject referralAndTransferForm = new FormsObject(REFERRAL_AND_TRANSFER_FORM, ReferralAndTransferForm.class, R.drawable.ctb_reffered_transfer, FormTypeColor.OTHER_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject contactRegistryForm = new FormsObject(CONTACT_REGISTRY, ContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER, Roles.PET_PSYCHOLOGIST,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_FACILITATOR, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_NURSE, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject endOfFollowup = new FormsObject(END_OF_FOLLOWUP, EndOfFollowupForm.class, R.drawable.pet_followup_end, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_CLINICIAN, Roles.PET_PSYCHOLOGIST,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject afbSmearOrderAndResultForm = new FormsObject(AFB_SMEAR_ORDER_AND_RESULT_FORM, AfbSmearOrderAndResultForm.class, R.drawable.ctb_afb_smear, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN,
            Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject dst_order_and_result = new FormsObject(DST_CULTURE_TEST, DSTOrderAndResultForm.class, R.drawable.ctb_dst_culture, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject ultrasound_order_and_result = new FormsObject(ULTRASOUND_TEST, UltrasoundTestOrderAndResultForm.class, R.drawable.ctb_ultrasound, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER,
            Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject ct_scan_order_and_result = new FormsObject(CT_SCAN_TEST, CTScanOrderAndResultForm.class, R.drawable.ctb_ct_scan, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER,
            Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject mantoux_order_and_result = new FormsObject(MANTOUX_TEST, MantouxOrderAndResultForm.class, R.drawable.ctb_mantoux, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER,
            Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject histopathology_order_and_result = new FormsObject(HISTOPATHOLOGY_TEST, HistopathologyOrderAndResultForm.class, R.drawable.ctb_histopathology, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER,
            Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject missedVisitFollowup = new FormsObject(MISSED_VISIT_FOLLOWUP, MissedVisitFollowupForm.class, R.drawable.ctb_missed_followup, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_HEALTH_WORKER, Roles.PET_FIELD_SUPERVISOR,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE}, -1, -1);
    public static final FormsObject clinicianEvaluationForm = new FormsObject(CLINICIAN_EVALUATION_FORM, ClinicianEvaluation.class, R.drawable.pet_clinician_contact_screening, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ALL}, -1, -1);
    public static final FormsObject generalCounselling = new FormsObject(GENERAL_COUNSELLING, GeneralCounsellingForm.class, R.drawable.cc_counseling, FormTypeColor.TREATMENT_FORM, new String[]{}, -1, -1);
    public static final FormsObject treatmentAdherence = new FormsObject(TREATMENT_ADHERENCE, TreatmentAdherenceForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TREATMENT_FORM, new String[]{}, -1, -1);
    public static final FormsObject followupCounseling = new FormsObject(FOLLOWUP_COUNSELING, FollowupCounselingForm.class, R.drawable.pet_counselling_followup, FormTypeColor.TREATMENT_FORM, new String[]{}, -1, -1);
    public static final FormsObject baselineCounseling = new FormsObject(BASELINE_COUNSELING, BaselineCounselingForm.class, R.drawable.pet_baseline_counselling, FormTypeColor.TREATMENT_FORM, new String[]{}, -1, -1);
    public static final FormsObject deceasedPatientSummary = new FormsObject(DECEASED_PATIENT_SUMMARY, DeceasedPatientSummary.class, R.drawable.ic_summary, FormTypeColor.OTHER_FORM, new String[]{}, -1, -1);

    /********************************
     * ZTTS
     ********************************/
    public static final String ZTTS_ENUMERATION = "ZTTS-Enumeration";
    public static FormsObject ztts_enumerationForm = new FormsObject(ZTTS_ENUMERATION, ZttsEnumerationForm.class, R.drawable.ztts_ennumeration, FormTypeColor.OTHER_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, -1, -1);

    public static final String ZTTS_SCREENING = "ZTTS-Adult Screening";
    public static FormsObject ztts_screeningForm = new FormsObject(ZTTS_SCREENING, ZttsScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_PRESUMPTIVE_INFORMATION = "ZTTS-Presumptive Information";
    public static FormsObject ztts_presumptiveInformationForm = new FormsObject(ZTTS_PRESUMPTIVE_INFORMATION, ZttsPresumptiveInformationForm.class, R.drawable.fast_presumptive_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_SAMPLE_COLLECTION = "ZTTS-Sample Collection";
    public static FormsObject ztts_sampleCollectionForm = new FormsObject(ZTTS_SAMPLE_COLLECTION, ZttsSampleCollectionForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_SCREENING_CXR = "ZTTS-Screening CXR";
    public static FormsObject ztts_screeningCXRForm = new FormsObject(ZTTS_SCREENING_CXR, ZttsScreeningCXR.class, R.drawable.ctb_xray, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_GENEEXPERT_RESULT = "ZTTS-GeneXpert Result";
    public static FormsObject ztts_geneXpertResultForm = new FormsObject(ZTTS_GENEEXPERT_RESULT, ZttsGeneXpertResultForm.class, R.drawable.fast_result_form, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_AFB_CULTURE_RESULT = "ZTTS-AFB Culture Result";
    public static FormsObject ztts_afbCultureResultForm = new FormsObject(ZTTS_AFB_CULTURE_RESULT, ZttsAFBCultureResultForm.class, R.drawable.ctb_dst_culture, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_BLOOD_SAMPLE_COLLECTION_CHILD = "ZTTS-Blood Sample Collection";
    public static FormsObject ztts_bloodSampleCollecitonChild = new FormsObject(ZTTS_BLOOD_SAMPLE_COLLECTION_CHILD, ZttsBloodSampleCollectionForm.class, R.drawable.blood_test, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 2, 4);

    public static final String ZTTS_CHILD_SCREENING = "ZTTS-Child Screening";
    public static FormsObject ztts_childScreeningForm = new FormsObject(ZTTS_CHILD_SCREENING, ZttsChildScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 2, 4);

    public static final String ZTTS_PRESUMPTIVE_INFORMATION_CHILD = "ZTTS-Presumptive Information Child";
    public static FormsObject ztts_presumptiveInformationChildForm = new FormsObject(ZTTS_PRESUMPTIVE_INFORMATION_CHILD, ZttsPresumptiveInformationChildForm.class, R.drawable.fast_presumptive_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 2, 4);

    /********************************
     * PET
     ********************************/
    public static final String PET_INDEX_PATIENT_REGISTRATION = "PET-Index Patient Registration";
    public static final String PET_BASELINE_SCREENING = "PET-Baseline Screening";
    public static final String PET_SOCIO_ECNOMICS_DATA = "PET-Socioeconomic Data";
    public static final String PET_CLINICIAN_CONTACT_SCREENING = "PET-Clinician Contact Screening";
    public static final String PET_INFECTION_TREATMENT_ELIGIBILITY = "PET-Infection Treatment Eligibility";
    public static final String PET_TREATMENT_INITIATION = "PET-Treatment Initiation";
    public static final String PET_BASELINE_COUNSELLING = "PET-Baseline Counselling";
    public static final String PET_TREATMENT_ADHERENCE = "PET-Treatment Adherence";
    public static final String PET_HOME_FOLLOWUP = "PET-Home Follow-up";
    public static final String PET_CLINICIAN_FOLLOWUP = "PET-Clinician Follow-up";
    public static final String PET_COUNSELLING_FOLLOWUP = "PET-Counselling Follow-up";
    public static final String PET_ADVERSE_EVENTS = "PET-Adverse Events";
    public static final String PET_INCENTIVE_DISBURSEMENT = "PET-Incentive Disbursement";
    public static final String PET_AKUAD = "PET-AKUADS";
    public static final String PET_TEST_INDICATION_FORM = "PET-Test Indication";
    public static final String PET_REFUSAL = "PET-Refusal";
    public static final String PET_HOME_VISIT = "PET-Home Visit";
    public static final String PET_RETRIVEL_FORM = "PET-Retrieval";

    public static final FormsObject pet_indexPatientRegistration = new FormsObject(PET_INDEX_PATIENT_REGISTRATION, PetIndexPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER}, -1, -1);
    public static final FormsObject pet_baselineScreening = new FormsObject(PET_BASELINE_SCREENING, PetBaselineScreeningForm.class, R.drawable.pet_baseline_screening, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_HEALTH_WORKER, Roles.PET_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject pet_socioEcnomicData = new FormsObject(PET_SOCIO_ECNOMICS_DATA, PetSocioecnomicDataForm.class, R.drawable.pet_socio_ecnomic_data, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_clinicianContactScreening = new FormsObject(PET_CLINICIAN_CONTACT_SCREENING, PetClinicianContactScreeningForm.class, R.drawable.pet_clinician_contact_screening, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_infectionTreatmenEligibility = new FormsObject(PET_INFECTION_TREATMENT_ELIGIBILITY, PetInfectionTreatmentEligibilityForm.class, R.drawable.pet_infection_treatment_eligibility, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_treatmentInitiation = new FormsObject(PET_TREATMENT_INITIATION, PetTreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_baselineCounselling = new FormsObject(PET_BASELINE_COUNSELLING, PetBaselineCounsellingForm.class, R.drawable.pet_baseline_counselling, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_akuad = new FormsObject(PET_AKUAD, PETAKUADForm.class, R.drawable.comorbidities_mental_health, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_treatmentAdherence = new FormsObject(PET_TREATMENT_ADHERENCE, PetTreatmentAdherenceForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_homeFollowup = new FormsObject(PET_HOME_FOLLOWUP, PetMonthlyHomeFollowupForm.class, R.drawable.pet_monthly_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_HEALTH_WORKER, Roles.PET_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject pet_clinicianFollowup = new FormsObject(PET_CLINICIAN_FOLLOWUP, PetClinicianFollowupForm.class, R.drawable.pet_clinician_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_counsellingFollowup = new FormsObject(PET_COUNSELLING_FOLLOWUP, PetCounsellingFollowupForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_adverseEvents = new FormsObject(PET_ADVERSE_EVENTS, PetAdverseEventForm.class, R.drawable.pet_adverse_events, FormTypeColor.OTHER_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_incentiveDisbursement = new FormsObject(PET_INCENTIVE_DISBURSEMENT, PetIncentiveDisbursementForm.class, R.drawable.pet_incentive_disbursement, FormTypeColor.OTHER_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER}, -1, -1);
    public static final FormsObject pet_test_indication_form = new FormsObject(PET_TEST_INDICATION_FORM, PetTestIndicationForm.class, R.drawable.ctb_test_indication_form, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_refusal = new FormsObject(PET_REFUSAL, PetRefusalForm.class, R.drawable.pet_refusal, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_home_visit_form = new FormsObject(PET_HOME_VISIT, PETHomeVisitForm.class, R.drawable.pet_house_visit, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER,Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER,Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_retrivel_form = new FormsObject(PET_RETRIVEL_FORM, PetRetrivelForm.class, R.drawable.pet_retrieval, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER,Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER,Roles.PET_PSYCHOLOGIST}, -1, -1);

    /********************************
     * FAST
     ********************************/
    public static final String FAST_SCREENING_FORM = "FAST-Screening";
    public static final String FAST_PRESUMPTIVE_FORM = "FAST-Presumptive";
    public static final String FAST_PROMPT_FORM = "FAST-Sputum Container and X-Ray Voucher";
    public static final String FAST_TREATMENT_INITIATION_FORM = "FAST-Treatment Initiation";
    public static final String FAST_TREATMENT_FOLLOWUP_FORM = "FAST-Treatment Followup";

    public static final FormsObject fastScreeningForm = new FormsObject(FAST_SCREENING_FORM, FastScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER}, -1, -1);
    public static final FormsObject fastPromptForm = new FormsObject(FAST_PROMPT_FORM, FastPromptForm.class, R.drawable.fast_prompt_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastPresumptiveForm = new FormsObject(FAST_PRESUMPTIVE_FORM, FastPresumptiveForm.class, R.drawable.fast_presumptive_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER}, -1, -1);
    public static final FormsObject fastTreatmentInitiationForm = new FormsObject(FAST_TREATMENT_INITIATION_FORM, FastTreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastTreatmentFollowupForm = new FormsObject(FAST_TREATMENT_FOLLOWUP_FORM, FastTreatmentFollowupForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);

    /********************************
     * CHILDHOOD TB
     ********************************/
    public static final String CHILDHOODTB_VERBAL_SCREENING = "Childhood TB-Verbal Screening";
    public static final FormsObject childhoodTb_verbalScreeningForm = new FormsObject(CHILDHOODTB_VERBAL_SCREENING, ChildhoodTbVerbalScreeningForm.class, R.drawable.ctb_screening, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE}, -1, -1);
   public static final String CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION = "Childhood TB-Presumptive Case Confirmation";
    public static final FormsObject childhoodTb_presumptive_case_confirmation = new FormsObject(CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION, ChildhoodTbPresumptiveCaseConfirmation.class, R.drawable.ctb_confirmation, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_PPA_SCORE = "Childhood TB-PPA Score";
    public static final FormsObject childhoodTb_ppa_score = new FormsObject(CHILDHOODTB_PPA_SCORE, ChildhoodTbPPAScore.class, R.drawable.ctb_score, FormTypeColor.OTHER_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_TEST_INDICATION_FORM = "Childhood TB-Test Indication";
    public static final FormsObject childhoodTb_test_indication_form = new FormsObject(CHILDHOODTB_TEST_INDICATION_FORM, ChildhoodTbTestIndicationForm.class, R.drawable.ctb_test_indication_form, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);

    public static final String CHILDHOODTB_TREATMENT_INITIATION = "Childhood TB-Treatment Initiation";
    public static final FormsObject childhoodTb_treatment_intiation = new FormsObject(CHILDHOODTB_TREATMENT_INITIATION, ChildhoodTbTreatmentInitiation.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
   public static final String CHILDHOODTB_TB_TREATMENT_FOLLOWUP = "Childhood TB-TB Treatment Followup";
    public static final FormsObject childhoodTb_tb_treatment_followup = new FormsObject(CHILDHOODTB_TB_TREATMENT_FOLLOWUP, ChildhoodTbTreatmentFollowup.class, R.drawable.pet_treatment_adherence, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_IPT_FOLLOWUP = "Childhood TB-Isoniazid Preventive Therapy Followup";
    public static final FormsObject childhoodTb_isoniazid_preventive_therapy_followup = new FormsObject(CHILDHOODTB_IPT_FOLLOWUP, ChildhoodTbIPTFollowup.class, R.drawable.ctb_isoniazid_preventive, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_ANTIBIOTIC_FOLLOWUP = "Childhood TB-Antibiotic Followup";
    public static final FormsObject childhoodTb_antibiotic_followup_form = new FormsObject(CHILDHOODTB_ANTIBIOTIC_FOLLOWUP, ChildhoodTbAntibioticFollowup.class, R.drawable.ctb_antibiotic, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_SUPPLEMENT_DISBURSEMENT = "Childhood TB-Supplement Disbursement";
    public static final FormsObject childhoodTb_supplement_disbursement = new FormsObject(CHILDHOODTB_SUPPLEMENT_DISBURSEMENT, ChildhoodTbSupplementDispersement.class, R.drawable.comorbidities_drugs_disbursement, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);

    /********************************************************************************************************************************/


    public static ArrayList<FormsObject> getCommonFormList() {

        ArrayList<FormsObject> commonList = new ArrayList<>();

        commonList.add(patientInformationForm);
        commonList.add(contactRegistryForm);
        commonList.add(missedVisitFollowup);
        commonList.add(referralAndTransferForm);
        commonList.add(endOfFollowup);
        commonList.add(ztts_enumerationForm);
        commonList.add(ztts_screeningForm);
        commonList.add(ztts_childScreeningForm);
        commonList.add(ztts_presumptiveInformationForm);
        commonList.add(ztts_presumptiveInformationChildForm);
        commonList.add(ztts_screeningCXRForm);
        commonList.add(ztts_sampleCollectionForm);
        commonList.add(ztts_geneXpertResultForm);
        commonList.add(ztts_afbCultureResultForm);
        commonList.add(ztts_bloodSampleCollecitonChild);
        commonList.add(deceasedPatientSummary);

        return commonList;

    }

    public static  ArrayList<FormsObject> getScreeningFormList(){

        ArrayList<FormsObject> screeningFormList = new ArrayList<>();

        screeningFormList.add(fastScreeningForm);
        screeningFormList.add(fastPresumptiveForm);
        screeningFormList.add(fastPromptForm);
        screeningFormList.add(childhoodTb_verbalScreeningForm);
        //screeningFormList.add(childhoodTb_presumptive_case_confirmation);
        screeningFormList.add(pet_baselineScreening);
        screeningFormList.add(pet_indexPatientRegistration);
        //screeningFormList.add(pet_clinicianContactScreening);
        screeningFormList.add(clinicianEvaluationForm);

        return screeningFormList;


    }

    public static  ArrayList<FormsObject> getTestFormList(){

        ArrayList<FormsObject> testFormList = new ArrayList<>();

        testFormList.add(childhoodTb_test_indication_form);
        testFormList.add(pet_test_indication_form);
        testFormList.add(gxpSpecimenCollectionForm);
        testFormList.add(geneXpertResultForm);
        testFormList.add(screeningChestXrayOrderAndResultForm);
        testFormList.add(afbSmearOrderAndResultForm);
        testFormList.add(dst_order_and_result);
        testFormList.add(ultrasound_order_and_result);
        testFormList.add(ct_scan_order_and_result);
        testFormList.add(mantoux_order_and_result);
        testFormList.add(histopathology_order_and_result);

        return testFormList;


    }

    public static  ArrayList<FormsObject> getTreatmentFormList(){

        ArrayList<FormsObject> treatmentFormList = new ArrayList<>();

        treatmentFormList.add(fastTreatmentInitiationForm);
        treatmentFormList.add(fastTreatmentFollowupForm);
        treatmentFormList.add(childhoodTb_ppa_score);
        treatmentFormList.add(childhoodTb_treatment_intiation);
        treatmentFormList.add(childhoodTb_tb_treatment_followup);
        treatmentFormList.add(childhoodTb_antibiotic_followup_form);
        treatmentFormList.add(childhoodTb_supplement_disbursement);
        treatmentFormList.add(childhoodTb_isoniazid_preventive_therapy_followup);
        treatmentFormList.add(pet_socioEcnomicData);
        treatmentFormList.add(pet_infectionTreatmenEligibility);
        treatmentFormList.add(pet_treatmentInitiation);
        treatmentFormList.add(pet_baselineCounselling);
        treatmentFormList.add(pet_akuad);
        treatmentFormList.add(pet_homeFollowup);
        treatmentFormList.add(pet_treatmentAdherence);
        treatmentFormList.add(pet_clinicianFollowup);
        treatmentFormList.add(pet_counsellingFollowup);
        treatmentFormList.add(pet_adverseEvents);
        //treatmentFormList.add(pet_incentiveDisbursement);
        treatmentFormList.add(pet_refusal);
        treatmentFormList.add(pet_retrivel_form);
        treatmentFormList.add(pet_home_visit_form);
        treatmentFormList.add(baselineCounseling);
        treatmentFormList.add(followupCounseling);
        treatmentFormList.add(generalCounselling);
        treatmentFormList.add(treatmentAdherence);

        return treatmentFormList;

    }

    public static final String COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM = "Mental Health Screening";
    public static FormsObject comorbidities_mentalHealthScreening = new FormsObject(COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, ComorbiditiesMentalHealthScreening.class, R.drawable.comorbidities_mental_health, FormTypeColor.REGISTRATION_FORM, new String[]{}, -1, -1);

    public static final String COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM = "Treatment Followup Mental Health";
    public static FormsObject comorbidities_treatmentFollowupMHForm = new FormsObject(COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM, ComorbiditiesMentalHealthTreatmentFollowupForm.class, R.drawable.comorbidities_mental_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{}, -1, -1);

    public static final String COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH = "Assessment Form Mental Health";
    public static FormsObject comorbidities_assessmentFormMH = new FormsObject(COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH, ComorbiditiesMentalHealthAssessmentForm.class, R.drawable.comorbidities_mental_assessment, FormTypeColor.FOLLOWUP_FORM, new String[]{}, -1, -1);

    public static final String COMORBIDITIES_END_OF_TREATMENT_MENTAL_HEALTH = "End of Treatment Mental Health";
    public static FormsObject comorbidities_endOfTreatmentFormMH = new FormsObject(COMORBIDITIES_END_OF_TREATMENT_MENTAL_HEALTH, ComorbiditiesEndOfTreatmentMentalHealthForm.class, R.drawable.comorbidities_end_mental_health, FormTypeColor.TREATMENT_FORM, new String[]{}, -1, -1);

    public static  ArrayList<FormsObject> getComorbiditiesFormList(){

        ArrayList<FormsObject> comorbiditiesFormList = new ArrayList<>();

        comorbiditiesFormList.add(comorbidities_mentalHealthScreening);
        comorbiditiesFormList.add(comorbidities_treatmentFollowupMHForm);
        comorbiditiesFormList.add(comorbidities_assessmentFormMH);
        comorbiditiesFormList.add(comorbidities_endOfTreatmentFormMH);

        return comorbiditiesFormList;

    }

}

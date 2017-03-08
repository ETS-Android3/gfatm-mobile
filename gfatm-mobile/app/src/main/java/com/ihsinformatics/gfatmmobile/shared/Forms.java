package com.ihsinformatics.gfatmmobile.shared;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbAFBSmearTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbCTScanTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbCXRScreeningTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbContactRegistry;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbDSTCultureTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbGXPSpecimenCollection;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbGXPTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbHistopathologySite;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbIPTFollowup;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbMantouxTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbMissedVisitFollowup;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbPPAScore;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbPatientRegistration;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbPresumptiveCaseConfirmation;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbReferral;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbScreeningLocation;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbTestIndicationForm;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbUltrasoundTest;
import com.ihsinformatics.gfatmmobile.childhoodTb.ChildhoodTbVerbalScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesBloodSugarForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesCreatinineTestForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesEducationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesEyeScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesFootScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesMellitusScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDrugDisbursement;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesHbA1CForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesLipidTestForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthAssessmentForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesPatientInformationForm1;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesUrineDetailedReportForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesUrineMicroalbuminForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesVitalsForm;
import com.ihsinformatics.gfatmmobile.fast.FastAfbSmearMicroscopyOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastContactRegistryForm;
import com.ihsinformatics.gfatmmobile.fast.FastDSTOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastEndOfFollowupForm;
import com.ihsinformatics.gfatmmobile.fast.FastGeneXpertResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastGpxSpecimenCollectionForm;
import com.ihsinformatics.gfatmmobile.fast.FastMissedFollowupForm;
import com.ihsinformatics.gfatmmobile.fast.FastPatientLocationForm;
import com.ihsinformatics.gfatmmobile.fast.FastPresumptiveForm;
import com.ihsinformatics.gfatmmobile.fast.FastPresumptiveInformationForm;
import com.ihsinformatics.gfatmmobile.fast.FastPromptForm;
import com.ihsinformatics.gfatmmobile.fast.FastReferralAndTransferForm;
import com.ihsinformatics.gfatmmobile.fast.FastScreeningChestXrayOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.fast.FastScreeningForm;
import com.ihsinformatics.gfatmmobile.fast.FastTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.fast.FastTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.pet.PetAdverseEventForm;
import com.ihsinformatics.gfatmmobile.pet.PetBaselineCounsellingForm;
import com.ihsinformatics.gfatmmobile.pet.PetBaselineScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.PetClinicianContactScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.PetClinicianFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetContactRegistryForm;
import com.ihsinformatics.gfatmmobile.pet.PetCounsellingFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetEndOfFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetIncentiveDisbursementForm;
import com.ihsinformatics.gfatmmobile.pet.PetIndexPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pet.PetInfectionTreatmentEligibilityForm;
import com.ihsinformatics.gfatmmobile.pet.PetMonthlyHomeFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetSocioecnomicDataForm;
import com.ihsinformatics.gfatmmobile.pet.PetTreatmentAdherenceForm;
import com.ihsinformatics.gfatmmobile.pet.PetTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtBasicManagementUnitVistForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtDailyTreatmentMonitoringForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientAssignmentForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientDissociationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtProviderRegistration;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtSocialSupportFoodBasketForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtTreatmentCoordinatorMonitoringForm;

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
    public static final String PET_MONTHLY_HOME_FOLLOWUP = "Monthly Home Follow-up";
    public static final String PET_CLINICIAN_FOLLOWUP = "Clinician Follow-up";
    public static final String PET_COUNSELLING_FOLLOWUP = "Counselling Follow-up";
    public static final String PET_ADVERSE_EVENTS = "Adverse Events";
    public static final String PET_INCENTIVE_DISBURSEMENT = "Incentive Disbursement";
    public static final String PET_END_FOLLOWOUP = "End of Follow-up";
    public static final FormsObject pet_indexPatientRegistration = new FormsObject(PET_INDEX_PATIENT_REGISTRATION, PetIndexPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject pet_contactRegistry = new FormsObject(PET_CONTACT_REGISTRY, PetContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject pet_baselineScreening = new FormsObject(PET_BASELINE_SCREENING, PetBaselineScreeningForm.class, R.drawable.pet_baseline_screening, FormTypeColor.TEST_FORM);
    public static final FormsObject pet_socioEcnomicData = new FormsObject(PET_SOCIO_ECNOMICS_DATA, PetSocioecnomicDataForm.class, R.drawable.pet_socio_ecnomic_data, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject pet_clinicianContactScreening = new FormsObject(PET_CLINICIAN_CONTACT_SCREENING, PetClinicianContactScreeningForm.class, R.drawable.pet_clinician_contact_screening, FormTypeColor.TEST_FORM);
    public static final FormsObject pet_infectionTreatmenEligibility = new FormsObject(PET_INFECTION_TREATMENT_ELIGIBILITY, PetInfectionTreatmentEligibilityForm.class, R.drawable.pet_infection_treatment_eligibility, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject pet_treatmentInitiation = new FormsObject(PET_TREATMENT_INITIATION, PetTreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject pet_baselineCounselling = new FormsObject(PET_BASELINE_COUNSELLING, PetBaselineCounsellingForm.class, R.drawable.pet_baseline_counselling, FormTypeColor.TEST_FORM);
    public static final FormsObject pet_treatmentAdherence = new FormsObject(PET_TREATMENT_ADHERENCE, PetTreatmentAdherenceForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject pet_monthlyHomeFollowup = new FormsObject(PET_MONTHLY_HOME_FOLLOWUP, PetMonthlyHomeFollowupForm.class, R.drawable.pet_monthly_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject pet_clinicianFollowup = new FormsObject(PET_CLINICIAN_FOLLOWUP, PetClinicianFollowupForm.class, R.drawable.pet_clinician_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject pet_counsellingFollowup = new FormsObject(PET_COUNSELLING_FOLLOWUP, PetCounsellingFollowupForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject pet_adverseEvents = new FormsObject(PET_ADVERSE_EVENTS, PetAdverseEventForm.class, R.drawable.pet_adverse_events, FormTypeColor.OTHER_FORM);
    public static final FormsObject pet_incentiveDisbursement = new FormsObject(PET_INCENTIVE_DISBURSEMENT, PetIncentiveDisbursementForm.class, R.drawable.pet_incentive_disbursement, FormTypeColor.OTHER_FORM);
    public static final FormsObject pet_endOfFollowup = new FormsObject(PET_END_FOLLOWOUP, PetEndOfFollowupForm.class, R.drawable.pet_followup_end, FormTypeColor.FOLLOWUP_FORM);

    /********************************
     * FAST
     ********************************/
    public static final String FAST_SCREENING_FORM = "Screening Form";
    public static final String FAST_PRESUMPTIVE_FORM = "Presumptive Form";
    public static final String FAST_PROMPT_FORM = "Sputum Container and X-Ray Voucher Form";
    public static final String FAST_PATIENT_LOCATION_FORM = "Patient Location Form";
    public static final String FAST_GXP_SPECIMEN_COLLECTION_FORM = "GXP Specimen Collection Form";
    public static final String FAST_PRESUMPTIVE_INFORMATION_FORM = "Presumptive Information Form";
    public static final String FAST_GENEXPERT_RESULT_FORM = "GeneXpert Result Form";
    public static final String FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM = "Screening Chest X-Ray(CAD4TB) Order and Result Form";
    public static final String FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM = "AFB Smear Microscopy Order and Result Form";
    public static final String FAST_DST_ORDER_AND_RESULT_FORM = "DST Order And Result Form";
    public static final String FAST_CONTACT_REGISTRY_FORM = "Contact Registry Form";
    public static final String FAST_REFERRAL_AND_TRANSFER_FORM = "Referral and Transfer Form";
    public static final String FAST_TREATMENT_INITIATION_FORM = "Treatment Initiation Form";
    public static final String FAST_TREATMENT_FOLLOWUP_FORM = "Treatment Followup Form";
    public static final String FAST_MISSED_VISIT_FOLLOWUP_FORM = "Missed Visit Follow-up";
    public static final String FAST_END_OF_FOLLOWUP_FORM = "End of Follow up Form";

    public static final FormsObject fastScreeningForm = new FormsObject(FAST_SCREENING_FORM, FastScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPromptForm = new FormsObject(FAST_PROMPT_FORM, FastPromptForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPresumptiveForm = new FormsObject(FAST_PRESUMPTIVE_FORM, FastPresumptiveForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastPatientLocationForm = new FormsObject(FAST_PATIENT_LOCATION_FORM, FastPatientLocationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastGpxSpecimenCollectionForm = new FormsObject(FAST_GXP_SPECIMEN_COLLECTION_FORM, FastGpxSpecimenCollectionForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM);
    public static final FormsObject fastPresumptiveInformationForm = new FormsObject(FAST_PRESUMPTIVE_INFORMATION_FORM, FastPresumptiveInformationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastGeneXpertResultForm = new FormsObject(FAST_GENEXPERT_RESULT_FORM, FastGeneXpertResultForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM);
    public static final FormsObject fastScreeningChestXrayOrderAndResultForm = new FormsObject(FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM, FastScreeningChestXrayOrderAndResultForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastAfbSmearMicroscopyOrderAndResultForm = new FormsObject(FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM, FastAfbSmearMicroscopyOrderAndResultForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastDstOrderAndResultForm = new FormsObject(FAST_DST_ORDER_AND_RESULT_FORM, FastDSTOrderAndResultForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastContactRegistryForm = new FormsObject(FAST_CONTACT_REGISTRY_FORM, FastContactRegistryForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastReferralTransferForm = new FormsObject(FAST_REFERRAL_AND_TRANSFER_FORM, FastReferralAndTransferForm.class, R.drawable.pet_registration, FormTypeColor.TREATMENT_FORM);
    public static final FormsObject fastTreatmentInitiationForm = new FormsObject(FAST_TREATMENT_INITIATION_FORM, FastTreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject fastTreatmentFollowupForm = new FormsObject(FAST_TREATMENT_FOLLOWUP_FORM, FastTreatmentFollowupForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastMissedVisitFollowupForm = new FormsObject(FAST_MISSED_VISIT_FOLLOWUP_FORM, FastMissedFollowupForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject fastEndOfFollowupForm = new FormsObject(FAST_END_OF_FOLLOWUP_FORM, FastEndOfFollowupForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

    /********************************
     * COMORBIDITIES
     ********************************/
    public static final String COMORBIDITIES_PATIENT_INFORMATION_FORM = "Patient Information";
    public static final String COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM = "Mental Health Screening";
    public static final String COMORBIDITIES_DIABETES_MELLITUS_SCREENING_FORM = "Diabetes Mellitus Screening";
    public static final String COMORBIDITIES_HbA1C_FORM = "HbA1C Test";
    public static final String COMORBIDITIES_BLOOD_SUGAR_FORM = "Blood Sugar Test";
    public static final String COMORBIDITIES_VITALS_FORM = "Vitals";
    public static final String COMORBIDITIES_DIABETES_TREATMENT_INITIATION = "Diabetes Treatment Initiation";
    public static final String COMORBIDITIES_URINE_DR_FORM = "Urine Detailed Report";
    public static final String COMORBIDITIES_LIPID_TEST_FORM = "Lipid Test";
    public static final String COMORBIDITIES_CREATININE_TEST_FORM = "Creatinine Test";
    public static final String COMORBIDITIES_MICROALBUMIN_TEST_FORM = "Urine Microalbumin Test";
    public static final String COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM = "Diabetes Treatment Followup";
    public static final String COMORBIDITIES_DIABETES_EDUCATION_FORM = "Diabetes Education";
    public static final String COMORBIDITIES_DIABETES_FOOT_SCREENING_FORM = "Diabetes Foot Screening";
    public static final String COMORBIDITIES_DIABETES_EYE_SCREENING_FORM = "Diabetes Eye Screening";
    public static final String COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM = "Treatment Followup Mental Health";
    public static final String COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH = "Assessment Form Mental Health";
    public static final String COMORBIDITIES_END_OF_TREATMENT_MENTAL_HEALTH = "End of Treatment Mental Health";
    public static final String COMORBIDITIES_DRUG_DISBURSEMENT = "Drug Disbursement";
    /********************************
     * PMDT
     ********************************/
    public static final String PMDT_PATIENT_REGISTRAITON = "Patient Registration";
    public static final String PMDT_PROVIDER_REGISTRAITON = "Provider Registration";
    public static final String PMDT_BASIC_MANAGEMENT_UNIT_VISIT = "Basic Management Unit Visit";
    public static final String PMDT_PATIENT_ASSIGNMENT = "Patient Assignment";
    public static final String PMDT_PATIENT_DISSOCIATION = "Patient Disscociation";
    public static final String PMDT_DAILY_TREATMENT_MONITORING = "Daily Treatment Monitoring";
    public static final String PMDT_TREATMENT_COORDINATOR_MONITORING = "TC Monitoring";
    public static final String PMDT_SOCIAL_SUPPORT_FOOD_BASKET = "Social Support Food Basket";
    public static final FormsObject patientRegistration = new FormsObject(PMDT_PATIENT_REGISTRAITON, PmdtPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject providerRegistration = new FormsObject(PMDT_PROVIDER_REGISTRAITON, PmdtProviderRegistration.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final FormsObject basicManagementUnitVisit = new FormsObject(PMDT_BASIC_MANAGEMENT_UNIT_VISIT, PmdtBasicManagementUnitVistForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.OTHER_FORM);
    public static final FormsObject patientAssignment = new FormsObject(PMDT_PATIENT_ASSIGNMENT, PmdtPatientAssignmentForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject patientDissociation = new FormsObject(PMDT_PATIENT_DISSOCIATION, PmdtPatientDissociationForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject dailyTreatmentMonitoring = new FormsObject(PMDT_DAILY_TREATMENT_MONITORING, PmdtDailyTreatmentMonitoringForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject treatmentCoordinatorcMonitoring = new FormsObject(PMDT_TREATMENT_COORDINATOR_MONITORING, PmdtTreatmentCoordinatorMonitoringForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    public static final FormsObject socialSupportFoodBasket = new FormsObject(PMDT_SOCIAL_SUPPORT_FOOD_BASKET, PmdtSocialSupportFoodBasketForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM);
    /********************************
     * CHILDHOOD TB
     ********************************/
    public static final String CHILDHOODTB_VERBAL_SCREENING = "VERBAL SCREENING";
    public static final FormsObject childhoodTb_verbalScreeningForm = new FormsObject(CHILDHOODTB_VERBAL_SCREENING, ChildhoodTbVerbalScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOOD_SCREENING_LOCATION = "CHILDHOOD SCREENING LOCATION";
    public static final FormsObject childhoodTb_screeningLocation = new FormsObject(CHILDHOOD_SCREENING_LOCATION, ChildhoodTbScreeningLocation.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_PATIENT_REGISTRATION = "PATIENT REGISTRATION";
    public static final FormsObject childhoodTb_patientRegistration = new FormsObject(CHILDHOODTB_PATIENT_REGISTRATION, ChildhoodTbPatientRegistration.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION = "PRESUMPTIVE CASE CONFIRMATION";
    public static final FormsObject childhoodTb_presumptive_case_confirmation = new FormsObject(CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION, ChildhoodTbPresumptiveCaseConfirmation.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_CONTACT_REGISTRY = "CONTACT REGISTRY";
    public static final FormsObject childhoodTb_contact_registry = new FormsObject(CHILDHOODTB_CONTACT_REGISTRY, ChildhoodTbContactRegistry.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_PPA_SCORE = "PPA SCORE";
    public static final FormsObject childhoodTb_ppa_score = new FormsObject(CHILDHOODTB_PPA_SCORE, ChildhoodTbPPAScore.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_TEST_INDICATION_FORM = "TEST INDICATION FORM";
    public static final FormsObject childhoodTb_test_indication_form = new FormsObject(CHILDHOODTB_TEST_INDICATION_FORM, ChildhoodTbTestIndicationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_GXP_SPECIMEN_COLLECTION_FORM = "GXP SPECIMEN COLLECTION FORM";
    public static final FormsObject childhoodTb_gxp_specimen_form = new FormsObject(CHILDHOODTB_GXP_SPECIMEN_COLLECTION_FORM, ChildhoodTbGXPSpecimenCollection.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_GXP_TEST = "GXP TEST";
    public static final FormsObject childhoodTb_gxp_test = new FormsObject(CHILDHOODTB_GXP_TEST, ChildhoodTbGXPTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_CXR_SCREENING_TEST = "CXR SCREENING TEST";
    public static final FormsObject childhoodTb_cxr_screening_test = new FormsObject(CHILDHOODTB_CXR_SCREENING_TEST, ChildhoodTbCXRScreeningTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_AFB_SMEAR_ORDER_AND_RESULT = "AFB SMEAR ORDER & RESULT FORM";
    public static final FormsObject childhoodTb_afb_smear_order_and_result = new FormsObject(CHILDHOODTB_AFB_SMEAR_ORDER_AND_RESULT, ChildhoodTbAFBSmearTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_DST_CULTURE_TEST = "DST ORDER & RESULT FORM";
    public static final FormsObject childhoodTb_dst_order_and_result = new FormsObject(CHILDHOODTB_DST_CULTURE_TEST, ChildhoodTbDSTCultureTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_ULTRASOUND_TEST = "ULTRASOUND ORDER & RESULT FORM";
    public static final FormsObject childhoodTb_ultrasound_order_and_result = new FormsObject(CHILDHOODTB_ULTRASOUND_TEST, ChildhoodTbUltrasoundTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_CT_SCAN_TEST = "CT SCAN ORDER & RESULT FORM";
    public static final FormsObject childhoodTb_ct_scan_order_and_result = new FormsObject(CHILDHOODTB_CT_SCAN_TEST, ChildhoodTbCTScanTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_MANTOUX_TEST = "MANTOUX ORDER & RESULT FORM";
    public static final FormsObject childhoodTb_mantoux_order_and_result = new FormsObject(CHILDHOODTB_MANTOUX_TEST, ChildhoodTbMantouxTest.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_HISTOPATHOLOGY_TEST = "HISTOPATHOLOGY ORDER & RESULT FORM";
    public static final FormsObject childhoodTb_histopathology_order_and_result = new FormsObject(CHILDHOODTB_HISTOPATHOLOGY_TEST, ChildhoodTbHistopathologySite.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_IPT_FOLLOWUP = "ISONIAZID PREVENTIVE THERAPY FOLLOWUP";
    public static final FormsObject childhoodTb_isoniazid_preventive_therapy_followup = new FormsObject(CHILDHOODTB_IPT_FOLLOWUP, ChildhoodTbIPTFollowup.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_MISSED_VISIT_FOLLOWUP = "MISSED VISIT FOLLOWUP";
    public static final FormsObject childhoodTb_missed_visit_followup = new FormsObject(CHILDHOODTB_MISSED_VISIT_FOLLOWUP, ChildhoodTbMissedVisitFollowup.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static final String CHILDHOODTB_REFERRAL = "REFERRAL AND TRANSFER FORM";
    public static final FormsObject childhoodTb_referral_and_transfer_form = new FormsObject(CHILDHOODTB_REFERRAL, ChildhoodTbReferral.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_indexPatientRegistration = new FormsObject(COMORBIDITIES_PATIENT_INFORMATION_FORM, ComorbiditiesPatientInformationForm1.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_mentalHealthScreening = new FormsObject(COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, ComorbiditiesMentalHealthScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesMellitusScreening = new FormsObject(COMORBIDITIES_DIABETES_MELLITUS_SCREENING_FORM, ComorbiditiesDiabetesMellitusScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_hbA1cForm = new FormsObject(COMORBIDITIES_HbA1C_FORM, ComorbiditiesHbA1CForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_bloodSugarForm = new FormsObject(COMORBIDITIES_BLOOD_SUGAR_FORM, ComorbiditiesBloodSugarForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_vitalsForm = new FormsObject(COMORBIDITIES_VITALS_FORM, ComorbiditiesVitalsForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesTreatmentInitiationForm = new FormsObject(COMORBIDITIES_DIABETES_TREATMENT_INITIATION, ComorbiditiesDiabetesTreatmentInitiationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_urineDRForm = new FormsObject(COMORBIDITIES_URINE_DR_FORM, ComorbiditiesUrineDetailedReportForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_lipidTestForm = new FormsObject(COMORBIDITIES_LIPID_TEST_FORM, ComorbiditiesLipidTestForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_creatinineTestForm = new FormsObject(COMORBIDITIES_CREATININE_TEST_FORM, ComorbiditiesCreatinineTestForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_microalbuminTestForm = new FormsObject(COMORBIDITIES_MICROALBUMIN_TEST_FORM, ComorbiditiesUrineMicroalbuminForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesTreatmentFollowupForm = new FormsObject(COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM, ComorbiditiesDiabetesTreatmentFollowupForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesEducationForm = new FormsObject(COMORBIDITIES_DIABETES_EDUCATION_FORM, ComorbiditiesDiabetesEducationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesFootScreeningForm = new FormsObject(COMORBIDITIES_DIABETES_FOOT_SCREENING_FORM, ComorbiditiesDiabetesFootScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_diabetesEyeScreeningForm = new FormsObject(COMORBIDITIES_DIABETES_EYE_SCREENING_FORM, ComorbiditiesDiabetesEyeScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_treatmentFollowupMHForm = new FormsObject(COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM, ComorbiditiesMentalHealthTreatmentFollowupForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_assessmentFormMH = new FormsObject(COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH, ComorbiditiesMentalHealthAssessmentForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    //public static FormsObject comorbidities_EndOfTreatmentFormMH = new FormsObject(COMORBIDITIES_END_OF_TREATMENT_MENTAL_HEALTH, ComorbiditiesEndOfTreatmentMentalHealthForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);
    public static FormsObject comorbidities_DrugDisbursementForm = new FormsObject(COMORBIDITIES_DRUG_DISBURSEMENT, ComorbiditiesDrugDisbursement.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM);

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
        petList.add(pet_monthlyHomeFollowup);
        petList.add(pet_treatmentAdherence);
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
        fastList.add(fastDstOrderAndResultForm);
        fastList.add(fastContactRegistryForm);
        fastList.add(fastReferralTransferForm);
        fastList.add(fastTreatmentInitiationForm);
        fastList.add(fastTreatmentFollowupForm);
        fastList.add(fastMissedVisitFollowupForm);
        fastList.add(fastEndOfFollowupForm);

        return fastList;

    }

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
        comorbiditiesList.add(comorbidities_diabetesFootScreeningForm);
        comorbiditiesList.add(comorbidities_diabetesEyeScreeningForm);
        comorbiditiesList.add(comorbidities_treatmentFollowupMHForm);
        comorbiditiesList.add(comorbidities_assessmentFormMH);
        //comorbiditiesList.add(comorbidities_EndOfTreatmentFormMH);
        comorbiditiesList.add(comorbidities_DrugDisbursementForm);

        return comorbiditiesList;

    }

    public static ArrayList<FormsObject> getPMDTFormList() {

        ArrayList<FormsObject> pmdtList = new ArrayList<>();

        pmdtList.add(patientRegistration);
        pmdtList.add(providerRegistration);
        pmdtList.add(basicManagementUnitVisit);
        pmdtList.add(patientAssignment);
        pmdtList.add(patientDissociation);
        pmdtList.add(dailyTreatmentMonitoring);
        pmdtList.add(treatmentCoordinatorcMonitoring);
        pmdtList.add(socialSupportFoodBasket);

        return pmdtList;


    }

    public static ArrayList<FormsObject> getChildhoodTBFormList() {

        ArrayList<FormsObject> childhoodtbList = new ArrayList<>();

        childhoodtbList.add(childhoodTb_verbalScreeningForm);
        childhoodtbList.add(childhoodTb_screeningLocation);
        childhoodtbList.add(childhoodTb_patientRegistration);
        childhoodtbList.add(childhoodTb_presumptive_case_confirmation);
        childhoodtbList.add(childhoodTb_contact_registry);
        childhoodtbList.add(childhoodTb_ppa_score);
        childhoodtbList.add(childhoodTb_test_indication_form);
        childhoodtbList.add(childhoodTb_gxp_specimen_form);
        childhoodtbList.add(childhoodTb_gxp_test);
        childhoodtbList.add(childhoodTb_cxr_screening_test);
        childhoodtbList.add(childhoodTb_afb_smear_order_and_result);
        childhoodtbList.add(childhoodTb_dst_order_and_result);
        childhoodtbList.add(childhoodTb_ultrasound_order_and_result);
        childhoodtbList.add(childhoodTb_ct_scan_order_and_result);
        childhoodtbList.add(childhoodTb_mantoux_order_and_result);
        childhoodtbList.add(childhoodTb_histopathology_order_and_result);
        childhoodtbList.add(childhoodTb_isoniazid_preventive_therapy_followup);
        childhoodtbList.add(childhoodTb_missed_visit_followup);
        childhoodtbList.add(childhoodTb_referral_and_transfer_form);

        return childhoodtbList;

    }



}

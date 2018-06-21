package com.ihsinformatics.gfatmmobile.shared;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbAFBSmearTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbAntibioticFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbCTScanTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbCXRScreeningTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbContactRegistry;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbDSTCultureTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbEndOfFollowUp;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbSpecimenCollection;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbGXPTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbHistopathologySite;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbIPTFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbMantouxTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbMissedVisitFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbPPAScore;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbPatientRegistration;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbPresumptiveCaseConfirmation;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbReferral;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbScreeningLocation;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbSupplementDispersement;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbTestIndicationForm;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbTreatmentFollowup;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbTreatmentInitiation;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbUltrasoundTest;
import com.ihsinformatics.gfatmmobile.childhoodtb.ChildhoodTbVerbalScreeningForm;
import com.ihsinformatics.gfatmmobile.common.ContactRegistryForm;
import com.ihsinformatics.gfatmmobile.common.EndOfFollowupForm;
import com.ihsinformatics.gfatmmobile.common.GeneXpertResultForm;
import com.ihsinformatics.gfatmmobile.common.GpxSpecimenCollectionForm;
import com.ihsinformatics.gfatmmobile.common.PatientInformationForm;
import com.ihsinformatics.gfatmmobile.common.ReferralAndTransferForm;
import com.ihsinformatics.gfatmmobile.common.ScreeningChestXrayOrderAndResultForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesBloodSugarForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesCOPDScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesCreatinineTestForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesEducationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesEyeScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesFootScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesMellitusScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDiabetesTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesDrugDisbursement;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesEndOfTreatmentMentalHealthForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsAFBCultureResultForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsBloodSampleCollectionForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsChildScreeningForm;
import com.ihsinformatics.gfatmmobile.ztts.ZttsEnumerationForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesHbA1CForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesLipidTestForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthAssessmentForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthScreeningForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMentalHealthTreatmentFollowupForm;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesMissedVisitFollowUp;
import com.ihsinformatics.gfatmmobile.comorbidities.ComorbiditiesPatientInformationForm;
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
import com.ihsinformatics.gfatmmobile.pet.PETAKUADForm;
import com.ihsinformatics.gfatmmobile.pet.PETHomeVisitForm;
import com.ihsinformatics.gfatmmobile.pet.PetAFBSmearTestForm;
import com.ihsinformatics.gfatmmobile.pet.PetAdverseEventForm;
import com.ihsinformatics.gfatmmobile.pet.PetBaselineCounsellingForm;
import com.ihsinformatics.gfatmmobile.pet.PetBaselineScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.PetCTScanTestForm;
import com.ihsinformatics.gfatmmobile.pet.PetCXRScreeningTestForm;
import com.ihsinformatics.gfatmmobile.pet.PetClinicianContactScreeningForm;
import com.ihsinformatics.gfatmmobile.pet.PetClinicianFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetContactRegistryForm;
import com.ihsinformatics.gfatmmobile.pet.PetCounsellingFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetDSTCultureTestForm;
import com.ihsinformatics.gfatmmobile.pet.PetEndOfFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetGXPTestForm;
import com.ihsinformatics.gfatmmobile.pet.PetIncentiveDisbursementForm;
import com.ihsinformatics.gfatmmobile.pet.PetIndexPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pet.PetInfectionTreatmentEligibilityForm;
import com.ihsinformatics.gfatmmobile.pet.PetMantouxTestForm;
import com.ihsinformatics.gfatmmobile.pet.PetMissedVisitFollowup;
import com.ihsinformatics.gfatmmobile.pet.PetMonthlyHomeFollowupForm;
import com.ihsinformatics.gfatmmobile.pet.PetRefusalForm;
import com.ihsinformatics.gfatmmobile.pet.PetRetrivelForm;
import com.ihsinformatics.gfatmmobile.pet.PetSocioecnomicDataForm;
import com.ihsinformatics.gfatmmobile.pet.PetSpecimenCollectionForm;
import com.ihsinformatics.gfatmmobile.pet.PetTestIndicationForm;
import com.ihsinformatics.gfatmmobile.pet.PetTreatmentAdherenceForm;
import com.ihsinformatics.gfatmmobile.pet.PetTreatmentInitiationForm;
import com.ihsinformatics.gfatmmobile.pet.PetUltrasoundTestForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtBasicManagementUnitVistForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtContactBaselineScreening;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtContactRegistryForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtConveyanceAllowanceForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtDailyTreatmentMonitoringForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtNutritionalSupportForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtNutritionalSupportReceivingForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientAssignmentForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientDissociationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtPatientRegistrationForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtProviderRegistration;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtSocialSupportFoodBasketForm;
import com.ihsinformatics.gfatmmobile.pmdt.PmdtTreatmentCoordinatorMonitoringForm;
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
    public static final FormsObject referralAndTransferForm = new FormsObject(REFERRAL_AND_TRANSFER_FORM, ReferralAndTransferForm.class, R.drawable.ctb_xray, FormTypeColor.OTHER_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject contactRegistryForm = new FormsObject(CONTACT_REGISTRY, ContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER, Roles.PET_PSYCHOLOGIST,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_FACILITATOR, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_NURSE, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final FormsObject endOfFollowup = new FormsObject(END_OF_FOLLOWUP, EndOfFollowupForm.class, R.drawable.pet_followup_end, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_CLINICIAN, Roles.PET_PSYCHOLOGIST,
            Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR,
            Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);

    /********************************
     * ZTTS
     ********************************/
    public static final String ZTTS_ENUMERATION = "Enumeration";
    public static FormsObject ztts_enumerationForm = new FormsObject(ZTTS_ENUMERATION, ZttsEnumerationForm.class, R.drawable.ztts_ennumeration, FormTypeColor.OTHER_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, -1, -1);

    public static final String ZTTS_SCREENING = "Adult Screening";
    public static FormsObject ztts_screeningForm = new FormsObject(ZTTS_SCREENING, ZttsScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_PRESUMPTIVE_INFORMATION = "Presumptive Information";
    public static FormsObject ztts_presumptiveInformationForm = new FormsObject(ZTTS_PRESUMPTIVE_INFORMATION, ZttsPresumptiveInformationForm.class, R.drawable.fast_presumptive_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_SAMPLE_COLLECTION = "Sample Collection";
    public static FormsObject ztts_sampleCollectionForm = new FormsObject(ZTTS_SAMPLE_COLLECTION, ZttsSampleCollectionForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_SCREENING_CXR = "Screening CXR";
    public static FormsObject ztts_screeningCXRForm = new FormsObject(ZTTS_SCREENING_CXR, ZttsScreeningCXR.class, R.drawable.ctb_xray, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_GENEEXPERT_RESULT = "GeneXpert Result";
    public static FormsObject ztts_geneXpertResultForm = new FormsObject(ZTTS_GENEEXPERT_RESULT, ZttsGeneXpertResultForm.class, R.drawable.fast_result_form, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_AFB_CULTURE_RESULT = "AFB Culture Result";
    public static FormsObject ztts_afbCultureResultForm = new FormsObject(ZTTS_AFB_CULTURE_RESULT, ZttsAFBCultureResultForm.class, R.drawable.ctb_dst_culture, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 15, -1);

    public static final String ZTTS_BLOOD_SAMPLE_COLLECTION_CHILD = "Blood Sample Collection";
    public static FormsObject ztts_bloodSampleCollecitonChild = new FormsObject(ZTTS_BLOOD_SAMPLE_COLLECTION_CHILD, ZttsBloodSampleCollectionForm.class, R.drawable.blood_test, FormTypeColor.TEST_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 2, 4);

    public static final String ZTTS_CHILD_SCREENING = "Child Screening";
    public static FormsObject ztts_childScreeningForm = new FormsObject(ZTTS_CHILD_SCREENING, ZttsChildScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 2, 4);

    public static final String ZTTS_PRESUMPTIVE_INFORMATION_CHILD = "Presumptive Information Child";
    public static FormsObject ztts_presumptiveInformationChildForm = new FormsObject(ZTTS_PRESUMPTIVE_INFORMATION_CHILD, ZttsPresumptiveInformationChildForm.class, R.drawable.fast_presumptive_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.ZTTS_PROGRAM_MANAGER}, 2, 4);

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
    public static final String PET_HOME_FOLLOWUP = "Home Follow-up";
    public static final String PET_CLINICIAN_FOLLOWUP = "Clinician Follow-up";
    public static final String PET_COUNSELLING_FOLLOWUP = "Counselling Follow-up";
    public static final String PET_ADVERSE_EVENTS = "Adverse Events";
    public static final String PET_INCENTIVE_DISBURSEMENT = "Incentive Disbursement";
    public static final String PET_END_FOLLOWOUP = "End of Follow-up";
    public static final String PET_AKUAD = "AKUADS";
    public static final String PET_TEST_INDICATION_FORM = "Test Indication";
    public static final String PET_SPECIMEN_COLLECTION_FORM = "GeneXpert Specimen Collection";
    public static final String PET_GXP_TEST = "GeneXpert Result";
    public static final String PET_CXR_SCREENING_TEST = "CXR Screening Test Order & Result";
    public static final String PET_AFB_SMEAR_ORDER_AND_RESULT = "AFB Smear Order & Result";
    public static final String PET_DST_CULTURE_TEST = "DST Order & Result";
    public static final String PET_ULTRASOUND_TEST = "Ultrasound Order & Result";
    public static final String PET_CT_SCAN_TEST = "CT Scan Order & Result";
    public static final String PET_MANTOUX_TEST = "Mantoux Order & Result";
    public static final String PET_HISTOPATHOLOGY_TEST = "Histopathology Order & Result";
    public static final String PET_MISSED_VISIT_FOLLOWUP = "Missed Visit Followup";
    public static final String PET_REFUSAL = "Refusal Form";
    public static final String PET_HOME_VISIT = "Home Visit";
    public static final String PET_RETRIVEL_FORM = "Retrieval Form";

    public static final FormsObject pet_indexPatientRegistration = new FormsObject(PET_INDEX_PATIENT_REGISTRATION, PetIndexPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER}, -1, -1);
    public static final FormsObject pet_contactRegistry = new FormsObject(PET_CONTACT_REGISTRY, PetContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER, Roles.PET_PSYCHOLOGIST}, -1, -1);
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
    public static final FormsObject pet_endOfFollowup = new FormsObject(PET_END_FOLLOWOUP, PetEndOfFollowupForm.class, R.drawable.pet_followup_end, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_CLINICIAN, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_test_indication_form = new FormsObject(PET_TEST_INDICATION_FORM, PetTestIndicationForm.class, R.drawable.ctb_test_indication_form, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_gxp_specimen_form = new FormsObject(PET_SPECIMEN_COLLECTION_FORM, PetSpecimenCollectionForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER}, -1, -1);
    public static final FormsObject pet_gxp_test = new FormsObject(PET_GXP_TEST, PetGXPTestForm.class, R.drawable.fast_result_form, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_cxr_screening_test = new FormsObject(PET_CXR_SCREENING_TEST, PetCXRScreeningTestForm.class, R.drawable.ctb_xray, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_afb_smear_order_and_result = new FormsObject(PET_AFB_SMEAR_ORDER_AND_RESULT, PetAFBSmearTestForm.class, R.drawable.ctb_afb_smear, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_dst_order_and_result = new FormsObject(PET_DST_CULTURE_TEST, PetDSTCultureTestForm.class, R.drawable.ctb_dst_culture, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_ultrasound_order_and_result = new FormsObject(PET_ULTRASOUND_TEST, PetUltrasoundTestForm.class, R.drawable.ctb_ultrasound, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_ct_scan_order_and_result = new FormsObject(PET_CT_SCAN_TEST, PetCTScanTestForm.class, R.drawable.ctb_ct_scan, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_mantoux_order_and_result = new FormsObject(PET_MANTOUX_TEST, PetMantouxTestForm.class, R.drawable.ctb_mantoux, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_histopathology_order_and_result = new FormsObject(PET_HISTOPATHOLOGY_TEST, ChildhoodTbHistopathologySite.class, R.drawable.ctb_histopathology, FormTypeColor.TEST_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_CLINICIAN}, -1, -1);
    public static final FormsObject pet_missed_visit_followup = new FormsObject(PET_MISSED_VISIT_FOLLOWUP, PetMissedVisitFollowup.class, R.drawable.ctb_missed_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_HEALTH_WORKER, Roles.PET_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject pet_refusal = new FormsObject(PET_REFUSAL, PetRefusalForm.class, R.drawable.pet_refusal, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER, Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_home_visit_form = new FormsObject(PET_HOME_VISIT, PETHomeVisitForm.class, R.drawable.pet_house_visit, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER,Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER,Roles.PET_PSYCHOLOGIST}, -1, -1);
    public static final FormsObject pet_retrivel_form = new FormsObject(PET_RETRIVEL_FORM, PetRetrivelForm.class, R.drawable.pet_retrieval, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PET_PROGRAM_MANAGER,Roles.PET_FIELD_SUPERVISOR, Roles.PET_HEALTH_WORKER,Roles.PET_PSYCHOLOGIST}, -1, -1);

    /********************************
     * FAST
     ********************************/
    public static final String FAST_SCREENING_FORM = "1. Screening Form";
    public static final String FAST_PRESUMPTIVE_FORM = "2. Presumptive Form";
    public static final String FAST_PROMPT_FORM = "Sputum Container and X-Ray Voucher Form";
    public static final String FAST_PATIENT_LOCATION_FORM = "3. Patient Location Form";
    public static final String FAST_GXP_SPECIMEN_COLLECTION_FORM = "5. GXP Specimen Collection Form";
    public static final String FAST_PRESUMPTIVE_INFORMATION_FORM = "4. Presumptive Information Form";
    public static final String FAST_GENEXPERT_RESULT_FORM = "GeneXpert Result Form";
    public static final String FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM = "6. Screening CXR(CAD4TB) Order and Result Form";
    public static final String FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM = "AFB Smear Microscopy Order and Result Form";
    public static final String FAST_DST_ORDER_AND_RESULT_FORM = "DST Order And Result Form";
    public static final String FAST_CONTACT_REGISTRY_FORM = "Contact Registry Form";
    public static final String FAST_REFERRAL_AND_TRANSFER_FORM = "Referral and Transfer Form";
    public static final String FAST_TREATMENT_INITIATION_FORM = "Treatment Initiation Form";
    public static final String FAST_TREATMENT_FOLLOWUP_FORM = "Treatment Followup Form";
    public static final String FAST_MISSED_VISIT_FOLLOWUP_FORM = "Missed Visit Follow-up";
    public static final String FAST_END_OF_FOLLOWUP_FORM = "End of Follow up Form";

    public static final FormsObject fastScreeningForm = new FormsObject(FAST_SCREENING_FORM, FastScreeningForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER}, -1, -1);
    public static final FormsObject fastPromptForm = new FormsObject(FAST_PROMPT_FORM, FastPromptForm.class, R.drawable.fast_prompt_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastPresumptiveForm = new FormsObject(FAST_PRESUMPTIVE_FORM, FastPresumptiveForm.class, R.drawable.fast_presumptive_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER}, -1, -1);
    public static final FormsObject fastPatientLocationForm = new FormsObject(FAST_PATIENT_LOCATION_FORM, FastPatientLocationForm.class, R.drawable.fast_patient_location, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER}, -1, -1);
    public static final FormsObject fastGpxSpecimenCollectionForm = new FormsObject(FAST_GXP_SPECIMEN_COLLECTION_FORM, FastGpxSpecimenCollectionForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_LAB_TECHNICIAN, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastPresumptiveInformationForm = new FormsObject(FAST_PRESUMPTIVE_INFORMATION_FORM, FastPresumptiveInformationForm.class, R.drawable.fast_presumptive_information_form, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastGeneXpertResultForm = new FormsObject(FAST_GENEXPERT_RESULT_FORM, FastGeneXpertResultForm.class, R.drawable.fast_result_form, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_LAB_TECHNICIAN, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastScreeningChestXrayOrderAndResultForm = new FormsObject(FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM, FastScreeningChestXrayOrderAndResultForm.class, R.drawable.ctb_xray, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SCREENER, Roles.FAST_LAB_TECHNICIAN, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastAfbSmearMicroscopyOrderAndResultForm = new FormsObject(FAST_AFB_SMEAR_MICROSCOPY_ORDER_AND_RESULT_FORM, FastAfbSmearMicroscopyOrderAndResultForm.class, R.drawable.ctb_afb_smear, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastDstOrderAndResultForm = new FormsObject(FAST_DST_ORDER_AND_RESULT_FORM, FastDSTOrderAndResultForm.class, R.drawable.ctb_dst_culture, FormTypeColor.TEST_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastContactRegistryForm = new FormsObject(FAST_CONTACT_REGISTRY_FORM, FastContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_FACILITATOR, Roles.FAST_SITE_MANAGER,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastReferralTransferForm = new FormsObject(FAST_REFERRAL_AND_TRANSFER_FORM, FastReferralAndTransferForm.class, R.drawable.pet_registration, FormTypeColor.TREATMENT_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastTreatmentInitiationForm = new FormsObject(FAST_TREATMENT_INITIATION_FORM, FastTreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastTreatmentFollowupForm = new FormsObject(FAST_TREATMENT_FOLLOWUP_FORM, FastTreatmentFollowupForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastMissedVisitFollowupForm = new FormsObject(FAST_MISSED_VISIT_FOLLOWUP_FORM, FastMissedFollowupForm.class, R.drawable.ctb_missed_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);
    public static final FormsObject fastEndOfFollowupForm = new FormsObject(FAST_END_OF_FOLLOWUP_FORM, FastEndOfFollowupForm.class, R.drawable.pet_followup_end, FormTypeColor.TREATMENT_FORM, new String[]{Roles.FAST_PROGRAM_MANAGER, Roles.FAST_SITE_MANAGER, Roles.FAST_FACILITATOR,  Roles.FAST_FIELD_SUPERVISOR}, -1, -1);

    /********************************
     * COMORBIDITIES
     ********************************/
    public static final String COMORBIDITIES_PATIENT_INFORMATION_FORM = "Patient Information";
    public static final String COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM = "Mental Health Screening";
    public static final String COMORBIDITIES_DIABETES_MELLITUS_SCREENING_FORM = "Co-morbid Conditions Screening";
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
    public static final String COMORBIDITIES_COPD_SCREENING = "COPD Screening Form";
    public static final String COMORBIDITIES_MISSED_VISIT_FOLLOW_UP = "Missed Visit Followup";

    public static FormsObject comorbidities_indexPatientRegistration = new FormsObject(COMORBIDITIES_PATIENT_INFORMATION_FORM, ComorbiditiesPatientInformationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_mentalHealthScreening = new FormsObject(COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, ComorbiditiesMentalHealthScreeningForm.class, R.drawable.comorbidities_mental_health, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_PSYCHOLOGIST}, -1, -1);
    public static FormsObject comorbidities_diabetesMellitusScreening = new FormsObject(COMORBIDITIES_DIABETES_MELLITUS_SCREENING_FORM, ComorbiditiesDiabetesMellitusScreeningForm.class, R.drawable.comorbidities_diabetes_mellitus, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_DIABETES_EDUCATOR}, -1, -1);
    public static FormsObject comorbidities_hbA1cForm = new FormsObject(COMORBIDITIES_HbA1C_FORM, ComorbiditiesHbA1CForm.class, R.drawable.comorbidities_hba1c, FormTypeColor.TEST_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_bloodSugarForm = new FormsObject(COMORBIDITIES_BLOOD_SUGAR_FORM, ComorbiditiesBloodSugarForm.class, R.drawable.comorbidities_blood_sugar, FormTypeColor.TEST_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_vitalsForm = new FormsObject(COMORBIDITIES_VITALS_FORM, ComorbiditiesVitalsForm.class, R.drawable.comorbidities_vitals, FormTypeColor.TREATMENT_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_EYE_SCREENER, Roles.COMORBIDITIES_FOOT_SCREENER}, -1, -1);
    public static FormsObject comorbidities_diabetesTreatmentInitiationForm = new FormsObject(COMORBIDITIES_DIABETES_TREATMENT_INITIATION, ComorbiditiesDiabetesTreatmentInitiationForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_urineDRForm = new FormsObject(COMORBIDITIES_URINE_DR_FORM, ComorbiditiesUrineDetailedReportForm.class, R.drawable.comorbidities_urine_detail, FormTypeColor.TEST_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_lipidTestForm = new FormsObject(COMORBIDITIES_LIPID_TEST_FORM, ComorbiditiesLipidTestForm.class, R.drawable.comorbidities_lipid, FormTypeColor.TEST_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_creatinineTestForm = new FormsObject(COMORBIDITIES_CREATININE_TEST_FORM, ComorbiditiesCreatinineTestForm.class, R.drawable.comorbodities_creatinine, FormTypeColor.TEST_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_microalbuminTestForm = new FormsObject(COMORBIDITIES_MICROALBUMIN_TEST_FORM, ComorbiditiesUrineMicroalbuminForm.class, R.drawable.comorbidities_urine_microalbumin, FormTypeColor.TEST_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_diabetesTreatmentFollowupForm = new FormsObject(COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM, ComorbiditiesDiabetesTreatmentFollowupForm.class, R.drawable.comorbidities_diabetes_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_diabetesEducationForm = new FormsObject(COMORBIDITIES_DIABETES_EDUCATION_FORM, ComorbiditiesDiabetesEducationForm.class, R.drawable.comorbidities_diabetes_education, FormTypeColor.OTHER_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST, Roles.COMORBIDITIES_PSYCHOLOGIST}, -1, -1);
    public static FormsObject comorbidities_diabetesFootScreeningForm = new FormsObject(COMORBIDITIES_DIABETES_FOOT_SCREENING_FORM, ComorbiditiesDiabetesFootScreeningForm.class, R.drawable.comorbidities_foot_screening, FormTypeColor.OTHER_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_FOOT_SCREENER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_diabetesEyeScreeningForm = new FormsObject(COMORBIDITIES_DIABETES_EYE_SCREENING_FORM, ComorbiditiesDiabetesEyeScreeningForm.class, R.drawable.comorbidities_eye_screening, FormTypeColor.OTHER_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_EYE_SCREENER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST, Roles.COMORBIDITIES_HEALTH_WORKER}, -1, -1);
    public static FormsObject comorbidities_treatmentFollowupMHForm = new FormsObject(COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM, ComorbiditiesMentalHealthTreatmentFollowupForm.class, R.drawable.comorbidities_mental_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_PSYCHOLOGIST}, -1, -1);
    public static FormsObject comorbidities_assessmentFormMH = new FormsObject(COMORBIDITIES_ASSESSMENT_FORM_MENTAL_HEALTH, ComorbiditiesMentalHealthAssessmentForm.class, R.drawable.comorbidities_mental_assessment, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_PSYCHOLOGIST}, -1, -1);
    public static FormsObject comorbidities_endOfTreatmentFormMH = new FormsObject(COMORBIDITIES_END_OF_TREATMENT_MENTAL_HEALTH, ComorbiditiesEndOfTreatmentMentalHealthForm.class, R.drawable.comorbidities_end_mental_health, FormTypeColor.TREATMENT_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_PSYCHOLOGIST}, -1, -1);
    public static FormsObject comorbidities_drugDisbursementForm = new FormsObject(COMORBIDITIES_DRUG_DISBURSEMENT, ComorbiditiesDrugDisbursement.class, R.drawable.comorbidities_drugs_disbursement, FormTypeColor.OTHER_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);
    public static FormsObject comorbidities_copdScreeningForm = new FormsObject(COMORBIDITIES_COPD_SCREENING, ComorbiditiesCOPDScreeningForm.class, R.drawable.comorbidities_drugs_disbursement, FormTypeColor.OTHER_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_DIABETES_EDUCATOR}, -1, -1);
    public static FormsObject comorbidities_missedVisitFollowUpForm = new FormsObject(COMORBIDITIES_MISSED_VISIT_FOLLOW_UP, ComorbiditiesMissedVisitFollowUp.class, R.drawable.comorbidities_drugs_disbursement, FormTypeColor.OTHER_FORM, new String[]{Roles.COMORBIDITIES_PROGRAM_MANAGER, Roles.COMORBIDITIES_HEALTH_WORKER, Roles.COMORBIDITIES_COUNSELOR, Roles.COMORBIDITIES_DIABETES_EDUCATOR, Roles.COMORBIDITIES_ASSOCIATE_DIABETOLOGIST}, -1, -1);

    /********************************
     * PMDT
     ********************************/
    public static final String PMDT_PATIENT_REGISTRAITON = "Patient Registration";
    public static final String PMDT_PROVIDER_REGISTRAITON = "Provider Registration";
    public static final String PMDT_BASIC_MANAGEMENT_UNIT_VISIT = "Basic Management Unit Visit";
    public static final String PMDT_PATIENT_ASSIGNMENT = "Patient Assignment";
    public static final String PMDT_PATIENT_DISSOCIATION = "Patient Disscociation";
    public static final String PMDT_DAILY_TREATMENT_MONITORING = "Daily Treatment Monitoring";
    public static final String PMDT_TREATMENT_COORDINATOR_MONITORING = "Treatment Coordinator Monitoring";
    public static final String PMDT_SOCIAL_SUPPORT_FOOD_BASKET = "Social Support Food Basket";
    public static final String PMDT_CONVEYANCE_ALLOWANCE = "Conveyance Allowance";
    public static final String PMDT_NUTRITIONAL_SUPPORT = "Nutritional Support";
    public static final String PMDT_NUTRITIONAL_SUPPORT_RECEIVING = "Nutritional Support Receiving";
    public static final String PMDT_CONTACT_REGISTRY = "Contact Registry";
    public static final String PMDT_CONTACT_BASELINE_SCREENING = "Contact Baseline Screening";

    // Form names created for HTML form based encounters
    public static final String PMDT_TREATMENT_INITIATION = "Treatment Initiation";

    public static final FormsObject pmdtPatientRegistration = new FormsObject(PMDT_PATIENT_REGISTRAITON, PmdtPatientRegistrationForm.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtProviderRegistration = new FormsObject(PMDT_PROVIDER_REGISTRAITON, PmdtProviderRegistration.class, R.drawable.pet_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtBasicManagementUnitVisit = new FormsObject(PMDT_BASIC_MANAGEMENT_UNIT_VISIT, PmdtBasicManagementUnitVistForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.OTHER_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtPatientAssignment = new FormsObject(PMDT_PATIENT_ASSIGNMENT, PmdtPatientAssignmentForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtPatientDissociation = new FormsObject(PMDT_PATIENT_DISSOCIATION, PmdtPatientDissociationForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtDailyTreatmentMonitoring = new FormsObject(PMDT_DAILY_TREATMENT_MONITORING, PmdtDailyTreatmentMonitoringForm.class, R.drawable.pet_infection_treatment_eligibility, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtTreatmentCoordinatorcMonitoring = new FormsObject(PMDT_TREATMENT_COORDINATOR_MONITORING, PmdtTreatmentCoordinatorMonitoringForm.class, R.drawable.pet_treatment_adherence, FormTypeColor.TREATMENT_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtSocialSupportFoodBasket = new FormsObject(PMDT_SOCIAL_SUPPORT_FOOD_BASKET, PmdtSocialSupportFoodBasketForm.class, R.drawable.pet_counselling_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtConveyanceAllowance = new FormsObject(PMDT_CONVEYANCE_ALLOWANCE, PmdtConveyanceAllowanceForm.class, R.drawable.pet_incentive_disbursement, FormTypeColor.OTHER_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtNutritionalSupport = new FormsObject(PMDT_NUTRITIONAL_SUPPORT, PmdtNutritionalSupportForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.OTHER_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtNutritionalSupportReceiving = new FormsObject(PMDT_NUTRITIONAL_SUPPORT_RECEIVING, PmdtNutritionalSupportReceivingForm.class, R.drawable.pet_treatment_initiation, FormTypeColor.OTHER_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtContactRegistry = new FormsObject(PMDT_CONTACT_REGISTRY, PmdtContactRegistryForm.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);
    public static final FormsObject pmdtContactBaselineScreening = new FormsObject(PMDT_CONTACT_BASELINE_SCREENING, PmdtContactBaselineScreening.class, R.drawable.pet_baseline_screening, FormTypeColor.TEST_FORM, new String[]{Roles.PMDT_PROGRAM_MANAGER}, -1, -1);

    /********************************
     * CHILDHOOD TB
     ********************************/
    public static final String CHILDHOODTB_VERBAL_SCREENING = "Verbal Screening";
    public static final FormsObject childhoodTb_verbalScreeningForm = new FormsObject(CHILDHOODTB_VERBAL_SCREENING, ChildhoodTbVerbalScreeningForm.class, R.drawable.ctb_screening, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE}, -1, -1);
    public static final String CHILDHOOD_SCREENING_LOCATION = "Childhood Screening Location";
    public static final FormsObject childhoodTb_screeningLocation = new FormsObject(CHILDHOOD_SCREENING_LOCATION, ChildhoodTbScreeningLocation.class, R.drawable.fast_patient_location, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_PATIENT_REGISTRATION = "Patient Registration";
    public static final FormsObject childhoodTb_patientRegistration = new FormsObject(CHILDHOODTB_PATIENT_REGISTRATION, ChildhoodTbPatientRegistration.class, R.drawable.ctb_registration, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION = "Presumptive Case Confirmation";
    public static final FormsObject childhoodTb_presumptive_case_confirmation = new FormsObject(CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION, ChildhoodTbPresumptiveCaseConfirmation.class, R.drawable.ctb_confirmation, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_CONTACT_REGISTRY = "Contact Registry";
    public static final FormsObject childhoodTb_contact_registry = new FormsObject(CHILDHOODTB_CONTACT_REGISTRY, ChildhoodTbContactRegistry.class, R.drawable.pet_contact, FormTypeColor.REGISTRATION_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_PPA_SCORE = "PPA Score";
    public static final FormsObject childhoodTb_ppa_score = new FormsObject(CHILDHOODTB_PPA_SCORE, ChildhoodTbPPAScore.class, R.drawable.ctb_score, FormTypeColor.OTHER_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_TEST_INDICATION_FORM = "Test Indication Form";
    public static final FormsObject childhoodTb_test_indication_form = new FormsObject(CHILDHOODTB_TEST_INDICATION_FORM, ChildhoodTbTestIndicationForm.class, R.drawable.ctb_test_indication_form, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_SPECIMEN_COLLECTION_FORM = "GeneXpert Specimen Collection Form";
    public static final FormsObject childhoodTb_gxp_specimen_form = new FormsObject(CHILDHOODTB_SPECIMEN_COLLECTION_FORM, ChildhoodTbSpecimenCollection.class, R.drawable.pet_treatment_adherence, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_LAB_TECHNICIAN, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_GXP_TEST = "GeneXpert Result";
    public static final FormsObject childhoodTb_gxp_test = new FormsObject(CHILDHOODTB_GXP_TEST, ChildhoodTbGXPTest.class, R.drawable.fast_result_form, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_LAB_TECHNICIAN, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_CXR_SCREENING_TEST = "CXR Screening Test";
    public static final FormsObject childhoodTb_cxr_screening_test = new FormsObject(CHILDHOODTB_CXR_SCREENING_TEST, ChildhoodTbCXRScreeningTest.class, R.drawable.ctb_xray, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_AFB_SMEAR_ORDER_AND_RESULT = "AFB Smear Order & Result Form";
    public static final FormsObject childhoodTb_afb_smear_order_and_result = new FormsObject(CHILDHOODTB_AFB_SMEAR_ORDER_AND_RESULT, ChildhoodTbAFBSmearTest.class, R.drawable.ctb_afb_smear, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_DST_CULTURE_TEST = "DST Order & Result Form";
    public static final FormsObject childhoodTb_dst_order_and_result = new FormsObject(CHILDHOODTB_DST_CULTURE_TEST, ChildhoodTbDSTCultureTest.class, R.drawable.ctb_dst_culture, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_ULTRASOUND_TEST = "Ultrasound Order & Result Form";
    public static final FormsObject childhoodTb_ultrasound_order_and_result = new FormsObject(CHILDHOODTB_ULTRASOUND_TEST, ChildhoodTbUltrasoundTest.class, R.drawable.ctb_ultrasound, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_CT_SCAN_TEST = "CT Scan Order & Result Form";
    public static final FormsObject childhoodTb_ct_scan_order_and_result = new FormsObject(CHILDHOODTB_CT_SCAN_TEST, ChildhoodTbCTScanTest.class, R.drawable.ctb_ct_scan, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_MANTOUX_TEST = "Mantoux Order & Result Form";
    public static final FormsObject childhoodTb_mantoux_order_and_result = new FormsObject(CHILDHOODTB_MANTOUX_TEST, ChildhoodTbMantouxTest.class, R.drawable.ctb_mantoux, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_HISTOPATHOLOGY_TEST = "Histopathology Order & Result Form";
    public static final FormsObject childhoodTb_histopathology_order_and_result = new FormsObject(CHILDHOODTB_HISTOPATHOLOGY_TEST, ChildhoodTbHistopathologySite.class, R.drawable.ctb_histopathology, FormTypeColor.TEST_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);

    public static final String CHILDHOODTB_TREATMENT_INITIATION = "Treatment Intiation Form";
    public static final FormsObject childhoodTb_treatment_intiation = new FormsObject(CHILDHOODTB_TREATMENT_INITIATION, ChildhoodTbTreatmentInitiation.class, R.drawable.pet_treatment_initiation, FormTypeColor.TREATMENT_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    //public static final String CHILDHOODTB_DRUG_DISPERSAL = "Drug Dsipersal";
    //public static final FormsObject childhoodTb_drug_dispersal = new FormsObject(CHILDHOODTB_DRUG_DISPERSAL, ChildhoodTbDrugDrugDispersal.class, R.drawable.ctb_drug_dispersal, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE});
    public static final String CHILDHOODTB_TB_TREATMENT_FOLLOWUP = "TB Treatment Followup";
    public static final FormsObject childhoodTb_tb_treatment_followup = new FormsObject(CHILDHOODTB_TB_TREATMENT_FOLLOWUP, ChildhoodTbTreatmentFollowup.class, R.drawable.pet_treatment_adherence, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_IPT_FOLLOWUP = "Isoniazid Preventive Therapy Followup";
    public static final FormsObject childhoodTb_isoniazid_preventive_therapy_followup = new FormsObject(CHILDHOODTB_IPT_FOLLOWUP, ChildhoodTbIPTFollowup.class, R.drawable.ctb_isoniazid_preventive, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_MISSED_VISIT_FOLLOWUP = "Missed Visit Followup";
    public static final FormsObject childhoodTb_missed_visit_followup = new FormsObject(CHILDHOODTB_MISSED_VISIT_FOLLOWUP, ChildhoodTbMissedVisitFollowup.class, R.drawable.ctb_missed_followup, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE}, -1, -1);
    public static final String CHILDHOODTB_REFERRAL = "Referral and Transfer Form";
    public static final FormsObject childhoodTb_referral_and_transfer_form = new FormsObject(CHILDHOODTB_REFERRAL, ChildhoodTbReferral.class, R.drawable.ctb_reffered_transfer, FormTypeColor.OTHER_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_ANTIBIOTIC_FOLLOWUP = "Antibiotic Followup Form";
    public static final FormsObject childhoodTb_antibiotic_followup_form = new FormsObject(CHILDHOODTB_ANTIBIOTIC_FOLLOWUP, ChildhoodTbAntibioticFollowup.class, R.drawable.ctb_antibiotic, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_END_OF_FOLLOWUP = "End Of Followup";
    public static final FormsObject childhoodTb_end_of_followup = new FormsObject(CHILDHOODTB_END_OF_FOLLOWUP, ChildhoodTbEndOfFollowUp.class, R.drawable.pet_followup_end, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER, Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);
    public static final String CHILDHOODTB_SUPPLEMENT_DISBURSEMENT = "Supplement Disbursement Form";
    public static final FormsObject childhoodTb_supplement_disbursement = new FormsObject(CHILDHOODTB_SUPPLEMENT_DISBURSEMENT, ChildhoodTbSupplementDispersement.class, R.drawable.comorbidities_drugs_disbursement, FormTypeColor.FOLLOWUP_FORM, new String[]{Roles.CHILDHOODTB_PROGRAM_MANAGER,Roles.CHILDHOODTB_NURSE,Roles.CHILDHOODTB_MEDICAL_OFFICER}, -1, -1);

    /********************************************************************************************************************************/

    public static ArrayList<FormsObject> getPETFormList() {

        ArrayList<FormsObject> petList = new ArrayList<>();

        petList.add(pet_indexPatientRegistration);
        //petList.add(pet_contactRegistry);
        petList.add(pet_baselineScreening);
        petList.add(pet_socioEcnomicData);
        petList.add(pet_clinicianContactScreening);
        petList.add(pet_infectionTreatmenEligibility);
        petList.add(pet_treatmentInitiation);
        petList.add(pet_baselineCounselling);
        petList.add(pet_akuad);
        petList.add(pet_homeFollowup);
        petList.add(pet_missed_visit_followup);
        petList.add(pet_treatmentAdherence);
        petList.add(pet_clinicianFollowup);
        petList.add(pet_counsellingFollowup);
        petList.add(pet_adverseEvents);
        petList.add(pet_incentiveDisbursement);
        //petList.add(pet_endOfFollowup);
        petList.add(pet_refusal);
        petList.add(pet_retrivel_form);
        petList.add(pet_home_visit_form);
        petList.add(pet_test_indication_form);
        //petList.add(pet_gxp_specimen_form);
        //petList.add(pet_gxp_test);
        //petList.add(pet_cxr_screening_test);
        petList.add(pet_afb_smear_order_and_result);
        petList.add(pet_dst_order_and_result);
        petList.add(pet_ultrasound_order_and_result);
        petList.add(pet_ct_scan_order_and_result);
        petList.add(pet_mantoux_order_and_result);
        petList.add(pet_histopathology_order_and_result);

        return petList;

    }


    public static ArrayList<FormsObject> getFASTFormList() {

        ArrayList<FormsObject> fastList = new ArrayList<>();

        fastList.add(fastScreeningForm);
        fastList.add(fastPresumptiveForm);
        //fastList.add(fastPatientLocationForm);
        //fastList.add(fastPresumptsiveInformationForm);
        //fastList.add(fastGpxSpecimenCollectionForm);
        //fastList.add(fastScreeningChestXrayOrderAndResultForm);
        fastList.add(fastPromptForm);
        //fastList.add(fastGeneXpertResultForm);
        fastList.add(fastAfbSmearMicroscopyOrderAndResultForm);
        fastList.add(fastDstOrderAndResultForm);
        //fastList.add(fastContactRegistryForm);
        //fastList.add(fastReferralTransferForm);
        fastList.add(fastTreatmentInitiationForm);
        fastList.add(fastTreatmentFollowupForm);
        fastList.add(fastMissedVisitFollowupForm);
        //fastList.add(fastEndOfFollowupForm);

        return fastList;

    }

    public static ArrayList<FormsObject> getCommorbiditiesFormList() {

        ArrayList<FormsObject> comorbiditiesList = new ArrayList<>();

        comorbiditiesList.add(comorbidities_indexPatientRegistration);
        comorbiditiesList.add(comorbidities_mentalHealthScreening);
        comorbiditiesList.add(comorbidities_diabetesMellitusScreening);
        comorbiditiesList.add(comorbidities_copdScreeningForm);
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
        comorbiditiesList.add(comorbidities_endOfTreatmentFormMH);
        comorbiditiesList.add(comorbidities_drugDisbursementForm);
        comorbiditiesList.add(comorbidities_missedVisitFollowUpForm);

        return comorbiditiesList;
    }

    public static ArrayList<FormsObject> getZTTSFormList() {

        ArrayList<FormsObject> zttsList = new ArrayList<>();

        zttsList.add(ztts_enumerationForm);
        zttsList.add(ztts_screeningForm);
        zttsList.add(ztts_childScreeningForm);
        zttsList.add(ztts_presumptiveInformationForm);
        zttsList.add(ztts_presumptiveInformationChildForm);
        zttsList.add(ztts_screeningCXRForm);
        zttsList.add(ztts_sampleCollectionForm);
        zttsList.add(ztts_geneXpertResultForm);
        zttsList.add(ztts_afbCultureResultForm);
        zttsList.add(ztts_bloodSampleCollecitonChild);

        return zttsList;
    }

    public static ArrayList<FormsObject> getPMDTFormList() {

        ArrayList<FormsObject> pmdtList = new ArrayList<>();

        pmdtList.add(pmdtPatientRegistration);
        pmdtList.add(pmdtProviderRegistration);
        pmdtList.add(pmdtBasicManagementUnitVisit);
        pmdtList.add(pmdtPatientAssignment);
        pmdtList.add(pmdtPatientDissociation);
        pmdtList.add(pmdtDailyTreatmentMonitoring);
        pmdtList.add(pmdtTreatmentCoordinatorcMonitoring);
        pmdtList.add(pmdtSocialSupportFoodBasket);
        pmdtList.add(pmdtConveyanceAllowance);
        pmdtList.add(pmdtNutritionalSupport);
        pmdtList.add(pmdtNutritionalSupportReceiving);
        //pmdtList.add(pmdtContactRegistry);
        pmdtList.add(pmdtContactBaselineScreening);

        return pmdtList;


    }

    public static ArrayList<FormsObject> getChildhoodTBFormList() {

        ArrayList<FormsObject> childhoodtbList = new ArrayList<>();

        childhoodtbList.add(childhoodTb_verbalScreeningForm);
        //childhoodtbList.add(childhoodTb_screeningLocation);
        //childhoodtbList.add(childhoodTb_patientRegistration);
        childhoodtbList.add(childhoodTb_presumptive_case_confirmation);
        //childhoodtbList.add(childhoodTb_contact_registry);
        childhoodtbList.add(childhoodTb_ppa_score);
        childhoodtbList.add(childhoodTb_test_indication_form);
        //childhoodtbList.add(childhoodTb_gxp_specimen_form);
        //childhoodtbList.add(childhoodTb_gxp_test);
        //childhoodtbList.add(childhoodTb_cxr_screening_test);
        childhoodtbList.add(childhoodTb_afb_smear_order_and_result);
        childhoodtbList.add(childhoodTb_dst_order_and_result);
        childhoodtbList.add(childhoodTb_ultrasound_order_and_result);
        childhoodtbList.add(childhoodTb_ct_scan_order_and_result);
        childhoodtbList.add(childhoodTb_mantoux_order_and_result);
        childhoodtbList.add(childhoodTb_histopathology_order_and_result);
        childhoodtbList.add(childhoodTb_treatment_intiation);
        childhoodtbList.add(childhoodTb_tb_treatment_followup);
        childhoodtbList.add(childhoodTb_antibiotic_followup_form);
        childhoodtbList.add(childhoodTb_supplement_disbursement);
        //childhoodtbList.add(childhoodTb_drug_dispersal);
        childhoodtbList.add(childhoodTb_isoniazid_preventive_therapy_followup);
        childhoodtbList.add(childhoodTb_missed_visit_followup);
        //childhoodtbList.add(childhoodTb_referral_and_transfer_form);
        //childhoodtbList.add(childhoodTb_end_of_followup);

        return childhoodtbList;

    }

    public static ArrayList<FormsObject> getCommonFormList() {

        ArrayList<FormsObject> commonList = new ArrayList<>();

        commonList.add(gxpSpecimenCollectionForm);
        commonList.add(geneXpertResultForm);
        commonList.add(screeningChestXrayOrderAndResultForm);
        commonList.add(patientInformationForm);
        commonList.add(contactRegistryForm);
        commonList.add(referralAndTransferForm);
        commonList.add(endOfFollowup);

        return commonList;

    }



}

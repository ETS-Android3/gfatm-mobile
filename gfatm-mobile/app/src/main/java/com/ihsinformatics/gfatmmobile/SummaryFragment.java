package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.shared.FormTypeColor;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.shared.Roles;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Date;


public class SummaryFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    Context context;

    ServerService serverService;

    ScrollView mainView;
    Button generalPatientView;
    Button fastPatientView;
    Button childhoodtbPatientView;
    Button dailyStaffView;

    Button petIncentiveDispatchView;
    Button petCounselorPaientView;
    Button petClinicianPaientView;

    LinearLayout contentView;
    TextView backButton;
    TextView heading;
    LinearLayout content;
    LinearLayout buttonLayout;

    TextView nameAndDate;

    ImageView refersh;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mainContent = inflater.inflate(
                R.layout.summary_fragment, container, false);

        context = mainContent.getContext();

        serverService = new ServerService(context.getApplicationContext());

        generalPatientView = (Button) mainContent.findViewById(R.id.genralPatientView);
        DrawableCompat.setTint(generalPatientView.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), FormTypeColor.FOLLOWUP_FORM));
        dailyStaffView = (Button) mainContent.findViewById(R.id.dailyStaffView);
        DrawableCompat.setTint(dailyStaffView.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), FormTypeColor.OTHER_FORM));
        mainView = (ScrollView) mainContent.findViewById(R.id.mainView);
        contentView = (LinearLayout) mainContent.findViewById(R.id.contentView);
        backButton = (TextView) mainContent.findViewById(R.id.backButton);
        heading = (TextView) mainContent.findViewById(R.id.heading);
        content = (LinearLayout) mainContent.findViewById(R.id.content);
        buttonLayout = (LinearLayout) mainContent.findViewById(R.id.buttonsLayout);
        nameAndDate = (TextView) mainContent.findViewById(R.id.nameanddate);

        refersh = (ImageView) mainContent.findViewById(R.id.refresh);
        refersh.setOnTouchListener(this);

        generalPatientView.setOnClickListener(this);
        dailyStaffView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        updateSummaryFragment();

        return mainContent;
    }

    public void updateSummaryFragment(){

        int color = App.getColor(this.context, R.attr.colorRegistration);

        Date date = new Date();
        String todayDate = App.getSqlDate(date);

        nameAndDate.setText("(forms submitted to openmrs by " + App.getUsername() + " with form date " + todayDate + ")");

        buttonLayout.removeAllViews();
        generalPatientView.setVisibility(View.VISIBLE);
        dailyStaffView.setVisibility(View.VISIBLE);

        if(App.getPatient() == null) {
            generalPatientView.setVisibility(View.GONE);
        }
        else {

            if(App.getRoles().contains(Roles.DEVELOPER) || App.getRoles().contains(Roles.FAST_PROGRAM_MANAGER) || App.getRoles().contains(Roles.FAST_FIELD_SUPERVISOR) || App.getRoles().contains(Roles.FAST_FACILITATOR)
                    || App.getRoles().contains(Roles.FAST_LAB_TECHNICIAN) || App.getRoles().contains(Roles.FAST_SCREENER) || App.getRoles().contains(Roles.FAST_SITE_MANAGER)) {
                fastPatientView = createButton(getResources().getString(R.string.fast_patient_view), color);
                buttonLayout.addView(fastPatientView);
            }

            if(App.getRoles().contains(Roles.DEVELOPER) || App.getRoles().contains(Roles.CHILDHOODTB_PROGRAM_MANAGER) || App.getRoles().contains(Roles.CHILDHOODTB_MEDICAL_OFFICER) || App.getRoles().contains(Roles.CHILDHOODTB_LAB_TECHNICIAN)
                    || App.getRoles().contains(Roles.CHILDHOODTB_MONITOR) || App.getRoles().contains(Roles.CHILDHOODTB_NURSE) || App.getRoles().contains(Roles.CHILDHOODTB_PROGRAM_ASSISTANT)) {
                childhoodtbPatientView = createButton(getResources().getString(R.string.childhood_tb_patient_view), color);
                buttonLayout.addView(childhoodtbPatientView);
            }

            if(App.getRoles().contains(Roles.DEVELOPER) || App.getRoles().contains(Roles.PET_PROGRAM_MANAGER)
                    || App.getRoles().contains(Roles.PET_FIELD_SUPERVISOR)) {
                petIncentiveDispatchView = createButton(getResources().getString(R.string.pet_incentive_dispatch), color);
                buttonLayout.addView(petIncentiveDispatchView);
            }

            if(App.getRoles().contains(Roles.DEVELOPER) || App.getRoles().contains(Roles.PET_PROGRAM_MANAGER)
                    || App.getRoles().contains(Roles.PET_PSYCHOLOGIST)) {
                petCounselorPaientView = createButton(getResources().getString(R.string.pet_counselor_view), color);
                buttonLayout.addView(petCounselorPaientView);
            }

            if(App.getRoles().contains(Roles.DEVELOPER) || App.getRoles().contains(Roles.PET_PROGRAM_MANAGER)
                    || App.getRoles().contains(Roles.PET_CLINICIAN)) {
                petClinicianPaientView = createButton(getResources().getString(R.string.pet_clinician_view), color);
                buttonLayout.addView(petClinicianPaientView);
            }

        }

        mainView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);

    }

    public void setMainContentVisible(Boolean visible) {
        if (visible) {
            mainView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        } else {
            mainView.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }

    }

    public boolean isViewVisible() {

        if (mainView.getVisibility() == View.GONE)
            return true;

        else
            return false;

    }

    @Override
    public void onClick(View v) {

        nameAndDate.setVisibility(View.GONE);

        if(v == generalPatientView){
            setMainContentVisible(false);
            heading.setText(getResources().getString(R.string.general_patient_view));
            content.removeAllViews();
            fillGeneralPatientView();
        } else if(v == fastPatientView){
            setMainContentVisible(false);
            heading.setText(getString(R.string.fast_patient_view));
            content.removeAllViews();
            fillFastPatientView();
        } else if(v == childhoodtbPatientView){
            setMainContentVisible(false);
            heading.setText(getString(R.string.childhood_tb_patient_view));
            content.removeAllViews();
            fillChildhoodTbPatientView();
        }else if(v == dailyStaffView){
            nameAndDate.setVisibility(View.VISIBLE);
            setMainContentVisible(false);
            heading.setText(getString(R.string.staff_view));
            content.removeAllViews();
            fillStaffView();
        } else if(v == backButton){
            setMainContentVisible(true);
        }  else if(v == petIncentiveDispatchView){
            setMainContentVisible(false);
            heading.setText(getString(R.string.pet_incentive_dispatch));
            content.removeAllViews();
            fillPetIncentiveDispatchView();
        } else if (v == petCounselorPaientView){
            setMainContentVisible(false);
            heading.setText(getString(R.string.pet_counselor_view));
            content.removeAllViews();
            fillPetCounselorPatientView();
        } else if(v == petClinicianPaientView){
            setMainContentVisible(false);
            heading.setText(getString(R.string.pet_clinician_view));
            content.removeAllViews();
            fillPetClinicianPatientView();
        }
    }

    public void fillGeneralPatientView(){

        String externalId =  serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "CONTACT EXTERNAL ID");
        if(externalId == null)
            externalId =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Verbal Screening", "CONTACT EXTERNAL ID");
        if(externalId == null)
            externalId = "-";
        else
            externalId = App.convertToTitleCase(externalId);

        String screeningFacility =  serverService.getEncounterLocation(App.getPatientId(), "FAST-Presumptive");
        if(screeningFacility == null)
            screeningFacility =  serverService.getEncounterLocation(App.getPatientId(), "Childhood TB-Verbal Screening");
        if(screeningFacility == null)
            screeningFacility= "-";

        String presumptive = serverService.getLatestObsValue(App.getPatientId(), "FAST-Presumptive", "PRESUMPTIVE TUBERCULOSIS");
        if(presumptive == null) {
            presumptive = serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "CONCLUSION");
            if(presumptive != null) {
                if (presumptive.equalsIgnoreCase("TB PRESUMPTIVE CONFIRMED"))
                    presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "Clinician Evaluation");
            }
        }  else{
            if(presumptive.equalsIgnoreCase("YES"))
                presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Presumptive");
        }
        if(presumptive == null || presumptive.equalsIgnoreCase("No"))
            presumptive = "-";

        String xpertResult = "";
        String rifResult = "";
        String sputumSubmissionDate = "";
        String xpertResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "GXP Specimen Collection");
        if(xpertResultDate != null){
            String xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "GXP Specimen Collection" , "TEST CONTEXT STATUS", "BASELINE REPEAT", "ORDER ID");
            if(xpertResultOderId == null){
                xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "GXP Specimen Collection", "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
                if(xpertResultOderId == null)
                    xpertResult = null;
                else {
                    xpertResult = serverService.getObsValueByObs(App.getPatientId(), "GeneXpert Result", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                    rifResult = serverService.getObsValueByObs(App.getPatientId(), "GeneXpert Result", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");
                    sputumSubmissionDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "GXP Specimen Collection", "ORDER ID", xpertResultOderId);
                }
            }
            else {
                xpertResult = serverService.getObsValueByObs(App.getPatientId(), "GeneXpert Result", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                rifResult = serverService.getObsValueByObs(App.getPatientId(), "GeneXpert Result", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");
                sputumSubmissionDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "GXP Specimen Collection", "ORDER ID", xpertResultOderId);
            }
        }
        if(xpertResult != null && !xpertResult.equals(""))
            xpertResult = App.convertToTitleCase(xpertResult);
        else
            xpertResult = "-";
        String xpertHighlight = null;
        if(rifResult != null && rifResult.equals("DETECTED"))
            xpertHighlight = "Highlight";

        if(sputumSubmissionDate == null || sputumSubmissionDate.equals(""))
            sputumSubmissionDate = "-";

        String xrayResultOderId = "";
        String xrayResult = "";
        String xrayResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "CXR Screening Test Order");
        if(xrayResultDate != null){
            String[] reasonForXray = serverService.getAllObsValues(App.getPatientId(), "CXR Screening Test Order", "REASON FOR X-RAY");
            String reason = "";
            for(String str : reasonForXray){
                if(str.equals("IDENTIFIED PATIENT THROUGH SCREENING") || str.equals("BASELINE DIAGNOSTIC")) {
                    reason = str;
                    xrayResultOderId = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order" , "REASON FOR X-RAY", reason, "ORDER ID");
                    break;
                } else if(str.equals("FOLLOW UP")){
                    xrayResultOderId = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order" , "FOLLOW-UP MONTH", "0", "ORDER ID");
                    if(xrayResultOderId == null)
                        xrayResultOderId = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order" , "FOLLOW-UP MONTH", "0.0", "ORDER ID");

                    if (xrayResultOderId != null) break;
                }
            }
            if(xrayResultOderId != null) {
                xrayResult = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", xrayResultOderId, "CHEST X-RAY SCORE");
                if (xrayResult == null)
                    xrayResult = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", xrayResultOderId, "RADIOLOGICAL DIAGNOSIS");
            }
        }
        if(xrayResult != null && !xrayResult.equals(""))
            xrayResult = App.convertToTitleCase(xrayResult);
        else
            xrayResult = "-";

        String diagnoseOn = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation","CONFIRMED DIAGNOSIS");
        if(diagnoseOn == null)
            diagnoseOn = "-";
        else
            diagnoseOn = App.convertToTitleCase(diagnoseOn);

        String diagnosisType = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation","TUBERCULOSIS DIAGNOSIS METHOD");
        if(diagnosisType == null)
            diagnosisType = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TUBERCULOSIS DIAGNOSIS METHOD");
        if(diagnosisType == null)
            diagnosisType = "-";
        else
            diagnosisType = App.convertToTitleCase(diagnosisType);

        String tbType =  serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation", "SITE OF TUBERCULOSIS DISEASE");
        if(tbType == null)
            tbType =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "SITE OF TUBERCULOSIS DISEASE");
        if(tbType == null)
            tbType= "-";
        else
            tbType = App.convertToTitleCase(tbType);

        String treatmentInitiationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-TB Treatment Initiation");
        if(treatmentInitiationDate == null)
            treatmentInitiationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Treatment Initiation");
        if(treatmentInitiationDate == null)
            treatmentInitiationDate= "-";

        String registrationNo = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup", "TB REGISTRATION NUMBER");
        if(registrationNo == null)
            registrationNo = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup", "TB REGISTRATION NUMBER");
        if(registrationNo == null)
            registrationNo= "-";

        String infectionTreatmentInitiationDate =  serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_TREATMENT_INITIATION);
        if(infectionTreatmentInitiationDate == null)
            infectionTreatmentInitiationDate = "-";

        String infectionTreatmentRegimen =  serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
        if(infectionTreatmentRegimen == null)
            infectionTreatmentRegimen = "-";
        else
            infectionTreatmentRegimen = App.convertToTitleCase(infectionTreatmentRegimen);

        String treatmentFacility =  serverService.getEncounterLocation(App.getPatientId(), "FAST-Treatment Initiation");
        if(treatmentFacility == null)
            treatmentFacility =  serverService.getEncounterLocation(App.getPatientId(), "Childhood TB-TB Treatment Initiation");
        if(treatmentFacility == null)
            treatmentFacility= "-";

        String nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup", "RETURN VISIT DATE");
        if (nextFollowupDate == null) {
            nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation", "RETURN VISIT DATE");
            if(nextFollowupDate == null){
                nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup", "RETURN VISIT DATE");
                if(nextFollowupDate == null)
                    nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "RETURN VISIT DATE");
            }
        }
        if(nextFollowupDate == null)
            nextFollowupDate = "-";

        String lastSmearResult = null;
        String lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "AFB Smear Test Order");
        if(lastSmearDate != null) {
            String orderId = serverService.getLatestObsValue(App.getPatientId(), "AFB Smear Test Order", "ORDER ID");
            if(orderId != null)
                lastSmearResult = serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", orderId, "SPUTUM FOR ACID FAST BACILLI");
        }

        if(lastSmearDate == null)
            lastSmearDate= "-";
        if(lastSmearResult == null)
            lastSmearResult= "-";
        else
            lastSmearResult = App.convertToTitleCase(lastSmearResult);

        String lastXrayDate = "";
        String lastXrayResult = "";
        String lastXrayOrderDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "CXR Screening Test Order");
        if(lastXrayOrderDate != null){
            String month = serverService.getLatestObsValue(App.getPatientId(), "CXR Screening Test Order","FOLLOW-UP MONTH");
            if(month == null || month.equals("0") || month.equals("0.0")){

                String[] followupMonths = serverService.getAllObsValues(App.getPatientId(), "CXR Screening Test Order", "FOLLOW-UP MONTH");
                Double followupMonthDouble = 0.0;
                if(followupMonths != null) {
                    for (String followupMonth : followupMonths) {
                        if (followupMonth.equals("0") || followupMonth.equals("0.0"))
                            continue;

                        double value = Double.parseDouble(followupMonth);
                        if (value > 0.0) {
                            if (value > followupMonthDouble)
                                followupMonthDouble = value;
                        }
                    }
                }
                if(followupMonthDouble != 0.0) {
                    lastXrayDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "CXR Screening Test Order","FOLLOW-UP MONTH", String.valueOf(followupMonthDouble));
                    if(lastXrayDate != null) {
                        String orderId = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Order", "FOLLOW-UP MONTH", String.valueOf(followupMonthDouble), "ORDER ID");
                        lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", orderId, "CHEST X-RAY SCORE");
                        if (lastXrayResult == null)
                            lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", orderId, "RADIOLOGICAL DIAGNOSIS");
                    }
                }

            } else {

                lastXrayDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "CXR Screening Test Result");
                if(lastXrayDate != null) {
                    String orderId = serverService.getLatestObsValue(App.getPatientId(), "CXR Screening Test Order", "ORDER ID");
                    if(orderId != null) {
                        lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", orderId, "CHEST X-RAY SCORE");
                        if (lastXrayResult == null)
                            lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", orderId, "RADIOLOGICAL DIAGNOSIS");
                    }
                }

            }
        }

        if(lastXrayDate == null || lastXrayDate.equals(""))
            lastXrayDate = "-";
        if(lastXrayResult == null || lastXrayResult.equals(""))
            lastXrayResult = "-";
        else
            lastXrayResult = App.convertToTitleCase(lastXrayResult);

        String treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","TREATMENT PLAN");
        if(treatmentPlan == null)
            treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup","TREATMENT PLAN");
        if(treatmentPlan == null)
            treatmentPlan= "-";
        else
            treatmentPlan = App.convertToTitleCase(treatmentPlan);

        String treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(), "End of Followup","TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutcome == null)
            treatmentOutcome= "-";
        else
            treatmentOutcome = App.convertToTitleCase(treatmentOutcome);

        String[][] dataset = { {getString(R.string.patient_id), App.getPatient().getPatientId(),null},
                {getString(R.string.external_id),externalId,null},
                {getString(R.string.screening_facility),screeningFacility,null},
                {getString(R.string.presumptive_date),presumptive,null},
                {getString(R.string.sample_submission_date),sputumSubmissionDate,null},
                {getString(R.string.xpert_result),xpertResult,xpertHighlight},
                {getString(R.string.xray_result),xrayResult,null},
                {getString(R.string.diagnosed_on),diagnoseOn,null},
                {getString(R.string.diagnosed_type),diagnosisType,null},
                {getString(R.string.tb_type),tbType,null},
                {getString(R.string.tb_initiation_date),treatmentInitiationDate,null},
                {getString(R.string.tb_registration_no),registrationNo,null},
                {getString(R.string.infection_tb_initiation_date),infectionTreatmentInitiationDate,null},
                {getString(R.string.tb_regimen),infectionTreatmentRegimen,null},
                {getString(R.string.treatment_facility),treatmentFacility,null},
                {getString(R.string.next_followup_tb_date),nextFollowupDate,null},
                {getString(R.string.date_last_smear_done),lastSmearDate,null},
                {getString(R.string.result_last_smear),lastSmearResult,null},
                {getString(R.string.date_last_xray_done),lastXrayDate,null},
                {getString(R.string.result_last_xray),lastXrayResult,null},
                {getString(R.string.treatment_plan),treatmentPlan,null},
                {getString(R.string.final_treatment_outcome),treatmentOutcome,null}};

        fillContent(dataset);

    }

    public void fillFastPatientView(){
        String externalId =  serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "CONTACT EXTERNAL ID");
        if(externalId == null)
            externalId = "-";
        else
            externalId = App.convertToTitleCase(externalId);

        String screeningFacility =  serverService.getEncounterLocation(App.getPatientId(), "FAST-Presumptive");
        if(screeningFacility == null)
            screeningFacility= "-";

        String presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Presumptive");
        if(presumptive == null)
            presumptive= "-";

        String gxpSputumDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "GXP Specimen Collection");
        if(gxpSputumDate==null){
            gxpSputumDate= "-";
        }

        String gxpResult=null,rifResult=null;
        String xpertResultOderId = serverService.getLatestObsValue(App.getPatientId(), "GXP Specimen Collection","ORDER ID");
        if (xpertResultOderId != null) {
            gxpResult = serverService.getObsValueByObs(App.getPatientId(), "GeneXpert Result", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
            rifResult = serverService.getObsValueByObs(App.getPatientId(), "GeneXpert Result", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");
            if(gxpResult==null){
                gxpResult = "-";
            }
            if(rifResult==null){
                rifResult = "-";
            }
        }
        else{
            gxpResult = "-";
            rifResult = "-";
        }



        String cxrResultOrderID = null;
        String cad4TbScore=null,radioLogicalDiagnosis=null;
        String cxrDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "CXR Screening Test Order");
        if(cxrDate!=null) {
            cxrResultOrderID = serverService.getLatestObsValue(App.getPatientId(), "CXR Screening Test Order","ORDER ID");
            if (cxrResultOrderID != null) {
                cad4TbScore = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", cxrResultOrderID, "CHEST X-RAY SCORE");
                radioLogicalDiagnosis = serverService.getObsValueByObs(App.getPatientId(), "CXR Screening Test Result", "ORDER ID", cxrResultOrderID, "RADIOLOGICAL DIAGNOSIS");
                if (cad4TbScore == null) {
                    cad4TbScore = "-";
                }
                if(radioLogicalDiagnosis==null){
                    radioLogicalDiagnosis="-";
                }
            } else {
                cad4TbScore = "-";
                radioLogicalDiagnosis="-";
            }
        }else{
            cxrDate= "-";
            cad4TbScore = "-";
            radioLogicalDiagnosis="-";
        }


        String sputumResultOrderID = null;
        String smearResult=null;
        String sputumDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "AFB Smear Test Order");
        if(sputumDate!=null) {
            sputumResultOrderID = serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
            if (sputumResultOrderID != null) {
                smearResult = serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", sputumResultOrderID, "SPUTUM FOR ACID FAST BACILLI");
                if (smearResult == null) {
                    smearResult = "-";
                }

            } else {
                smearResult = "-";
            }
        }else{
            sputumDate= "-";
            smearResult = "-";
        }

        String referredTransfer = serverService.getLatestObsValue(App.getPatientId(), "Referral and Transfer","PATIENT BEING REFEREED OUT OR TRANSFERRED OUT");
        if(referredTransfer==null){
            referredTransfer="-";
        }

        String referralSite = serverService.getLatestObsValue(App.getPatientId(), "Referral and Transfer","REFERRING FACILITY NAME");
        if(referralSite==null){
            referralSite="-";
        }

        String tbPatient = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","PATIENT HAVE TB");
        if(tbPatient==null){
            tbPatient="-";
        }

        String diagnosisType = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TUBERCULOSIS DIAGNOSIS METHOD");
        if(diagnosisType==null){
            diagnosisType="-";
        }

        String typeOfTB = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","SITE OF TUBERCULOSIS DISEASE");
        if(typeOfTB==null){
            typeOfTB="-";
        }

        String extraPulmonarySite = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","EXTRA PULMONARY SITE");
        if(extraPulmonarySite==null){
            extraPulmonarySite="-";
        }

        String patientType = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TB PATIENT TYPE");
        if(patientType==null){
            patientType="-";
        }

        String tbCategory = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TB CATEGORY");
        if(tbCategory==null){
            tbCategory="-";
        }

        String treatmentInitiated = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TREATMENT INITIATED");
        if(treatmentInitiated==null){
            treatmentInitiated="-";
        }

        String reasonTreatmentNotInitiated = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TREATMENT NOT STARTED");
        if(reasonTreatmentNotInitiated==null){
            reasonTreatmentNotInitiated="-";
        }


        String tbRegisrationNo = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TB REGISTRATION NUMBER");
        if(tbRegisrationNo==null){
            tbRegisrationNo="-";
        }

        String returnVisitDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","RETURN VISIT DATE");
        if(returnVisitDate==null){
            returnVisitDate="-";
        }

        String followupDate = serverService.getLatestEncounterDateTime(App.getPatientId(),"FAST-Treatment Followup");
        if(followupDate==null) {
            followupDate = "-";
        }



        String smearResult2=null;

        String afbSmearOrderId2 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "2", "ORDER ID");
        if(afbSmearOrderId2==null){
            afbSmearOrderId2 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "2.0", "ORDER ID");
            if(afbSmearOrderId2==null) {
                smearResult2 = "-";
            }
            else{
                smearResult2 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId2, "SPUTUM FOR ACID FAST BACILLI");
                if(smearResult2==null){
                    smearResult2="-";
                }
            }
        }else{
            smearResult2 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId2, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult2==null){
                smearResult2="-";
            }
        }

        String afbSmearOrderId3 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "3", "ORDER ID");
        String smearResult3=null;
        if(afbSmearOrderId3==null){
            afbSmearOrderId3 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "3.0", "ORDER ID");
            if(afbSmearOrderId3==null) {
                smearResult3 = "-";
            }
            else{
                smearResult3 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId3, "SPUTUM FOR ACID FAST BACILLI");
                if(smearResult3==null){
                    smearResult3="-";
                }
            }
        }else{
            smearResult3 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId3, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult3==null){
                smearResult3="-";
            }
        }

        String smearResult5=null;
        String afbSmearOrderId5 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "5", "ORDER ID");
        if(afbSmearOrderId5==null){
            afbSmearOrderId5 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "5.0", "ORDER ID");
            if(afbSmearOrderId5==null) {
                smearResult5 = "-";
            }
            else{
                smearResult5 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId5, "SPUTUM FOR ACID FAST BACILLI");
                if(smearResult5==null){
                    smearResult5="-";
                }
            }
        }else{
            smearResult5 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId5, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult5==null){
                smearResult5="-";
            }
        }

        String smearResult6=null;
        String afbSmearOrderId6 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "6", "ORDER ID");
        if(afbSmearOrderId6==null){
            afbSmearOrderId6 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "6.0", "ORDER ID");
            if(afbSmearOrderId6==null) {
                smearResult6 = "-";
            }
            else{
                smearResult6 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId6, "SPUTUM FOR ACID FAST BACILLI");
                if(smearResult6==null){
                    smearResult6="-";
                }
            }
        }else{
            smearResult6 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId6, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult6==null){
                smearResult6="-";
            }
        }

        String smearResult7=null;
        String afbSmearOrderId7 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "7", "ORDER ID");
        if(afbSmearOrderId7==null){
            afbSmearOrderId7 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "7.0", "ORDER ID");
            if(afbSmearOrderId7==null) {
                smearResult7 = "-";
            }
            else{
                smearResult7 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId7, "SPUTUM FOR ACID FAST BACILLI");
                if(smearResult7==null){
                    smearResult7="-";
                }
            }
        }else{
            smearResult7 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId7, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult7==null){
                smearResult7="-";
            }
        }

        String smearResult8=null;
        String afbSmearOrderId8 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "8", "ORDER ID");
        if(afbSmearOrderId8==null){
            afbSmearOrderId8 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Order", "FOLLOW-UP MONTH", "8.0", "ORDER ID");
            if(afbSmearOrderId8==null) {
                smearResult8 = "-";
            }
            else{
                smearResult8 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId8, "SPUTUM FOR ACID FAST BACILLI");
                if(smearResult8==null){
                    smearResult8="-";
                }
            }
        }else{
            smearResult8 =  serverService.getObsValueByObs(App.getPatientId(), "AFB Smear Test Result", "ORDER ID", afbSmearOrderId8, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult8==null){
                smearResult8="-";
            }
        }

        String patientTreatmentPlan = serverService.getLatestObsValue(App.getPatientId(),"Treatment Followup","TREATMENT PLAN");
        if(patientTreatmentPlan==null) {
            patientTreatmentPlan = "-";
        }

        String treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(),"End of Followup","TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutcome==null) {
            treatmentOutcome = "-";
        }




        String[][] dataset = {{getResources().getString(R.string.patient_id), App.getPatient().getPatientId(), null},
                {getResources().getString(R.string.external_id), externalId, null},
                {getResources().getString(R.string.screening_facility), screeningFacility, null},
                {getResources().getString(R.string.presumptive_date), presumptive, null},
                {getResources().getString(R.string.sputum_submission_date_gxp), gxpSputumDate, null},
                {getResources().getString(R.string.gxp_result), App.convertToTitleCase(gxpResult), null},
                {getResources().getString(R.string.rif_result), App.convertToTitleCase(rifResult), null},
                {getResources().getString(R.string.cxr_date), cxrDate, null},
                {getResources().getString(R.string.cxr_cad4tb_score),App.convertToTitleCase(cad4TbScore), null},

                {getResources().getString(R.string.radiological_diagnosis), App.convertToTitleCase(radioLogicalDiagnosis), null},
                {getResources().getString(R.string.sputum_submission_date_afb), sputumDate, null},
                {getResources().getString(R.string.smear_result), App.convertToTitleCase(smearResult), null},
                {getResources().getString(R.string.referred_transferred), App.convertToTitleCase(referredTransfer),null},
                {getResources().getString(R.string.referral_site), referralSite, null},
                {getResources().getString(R.string.tb_patient), App.convertToTitleCase(tbPatient), null},
                {getResources().getString(R.string.diagnosis_type), App.convertToTitleCase(diagnosisType), null},
                {getResources().getString(R.string.type_of_tb), App.convertToTitleCase(typeOfTB), null},
                {getResources().getString(R.string.extra_pulmonary_site), App.convertToTitleCase(extraPulmonarySite), null},
                {getResources().getString(R.string.patient_type), App.convertToTitleCase(patientType), null},
                {getResources().getString(R.string.tb_category), App.convertToTitleCase(tbCategory), null},


                {getResources().getString(R.string.treatment_initiated), App.convertToTitleCase(treatmentInitiated), null},
                {getResources().getString(R.string.reason_not_initiated),App.convertToTitleCase(reasonTreatmentNotInitiated), null},
                {getResources().getString(R.string.tb_registration_no), tbRegisrationNo, null},
                {getResources().getString(R.string.return_visit_date), returnVisitDate, null},
                {getResources().getString(R.string.followup_date), App.convertToTitleCase(followupDate), null},

                {getResources().getString(R.string.smear_result_2), App.convertToTitleCase(smearResult2), null},

                {getResources().getString(R.string.smear_result_3), App.convertToTitleCase(smearResult3), null},

                {getResources().getString(R.string.smear_result_5), App.convertToTitleCase(smearResult5), null},

                {getResources().getString(R.string.smear_result_6), App.convertToTitleCase(smearResult6), null},

                {getResources().getString(R.string.smear_result_7), App.convertToTitleCase(smearResult7), null},

                {getResources().getString(R.string.smear_result_8), App.convertToTitleCase(smearResult8), null},

                {getResources().getString(R.string.patient_treatment_plan), App.convertToTitleCase(patientTreatmentPlan), null},
                {getResources().getString(R.string.treatment_outcome), App.convertToTitleCase(treatmentOutcome), null}};

        fillContent(dataset);
    }

    public void fillPetPatientView(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillChildhoodTbPatientView(){

        String externalId = App.getPatient().getExternalId();
        if(externalId==null) {
            serverService.getLatestObsValue(App.getPatientId(), "Patient Information", "CONTACT EXTERNAL ID");
        }

        if(externalId == null)
            externalId = "-";
        else
            externalId = App.convertToTitleCase(externalId);

        String screeningFacility =  serverService.getEncounterLocation(App.getPatientId(), "Childhood TB-Verbal Screening");
        if(screeningFacility == null)
            screeningFacility= "-";

        String weight =  serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "WEIGHT (KG)");
        if(weight == null)
            weight = "-";

        String height =  serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "HEIGHT (CM)");
        if(height == null)
            height = "-";

        String percentile =  serverService.getLatestObsValue(App.getPatientId(), "Clinician Evaluation", "WEIGHT PERCENTILE GROUP");
        if(percentile == null)
            percentile = "-";

        String ppaScore =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-PPA Score", "PPA SCORE");
        if(ppaScore == null)
            ppaScore = "-";

        String testConfirmingDiagnosis =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "CONFIRMED DIAGNOSIS");
        if(testConfirmingDiagnosis == null)
            testConfirmingDiagnosis = "-";
        else
            testConfirmingDiagnosis = App.convertToTitleCase(testConfirmingDiagnosis);

        String typeOfDiagnosis =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "TUBERCULOSIS DIAGNOSIS METHOD");
        if(typeOfDiagnosis == null)
            typeOfDiagnosis = "-";
        else
            typeOfDiagnosis = App.convertToTitleCase(typeOfDiagnosis);


        String typeOfTB =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "SITE OF TUBERCULOSIS DISEASE");
        if(typeOfTB == null)
            typeOfTB = "-";
        else
            typeOfTB = App.convertToTitleCase(typeOfTB);

        String extraPulmonarySite =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "EXTRA PULMONARY SITE");
        if(extraPulmonarySite == null)
            extraPulmonarySite = "-";
        else
            extraPulmonarySite = App.convertToTitleCase(extraPulmonarySite);

        String patientType =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "TB PATIENT TYPE");
        if(patientType == null)
            patientType = "-";
        else
            patientType = App.convertToTitleCase(patientType);


        String tbCategory =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "TB CATEGORY");
        if(tbCategory == null)
            tbCategory = "-";
        else
            tbCategory = App.convertToTitleCase(tbCategory);

        String treatmentInitiated =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "TREATMENT INITIATED");
        if(treatmentInitiated == null)
            treatmentInitiated = "-";
        else
            treatmentInitiated = App.convertToTitleCase(treatmentInitiated);

        String reasonNotInitiated =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "TREATMENT NOT INITIATED REASON");
        if(reasonNotInitiated == null)
            reasonNotInitiated = "-";
        else
            reasonNotInitiated = App.convertToTitleCase(reasonNotInitiated);

        String additionalTreatment =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "ADDITIONAL TREATMENT TO TB PATIENT");
        if(additionalTreatment == null)
            additionalTreatment = "-";
        else
            additionalTreatment = App.convertToTitleCase(additionalTreatment);

        String tbRegistrationNumber =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Initiation", "TB REGISTRATION NUMBER");
        if(tbRegistrationNumber == null)
            tbRegistrationNumber = "-";


        String treatmentPlan2=null,conclusionFollowup2=null;
        String followUpDate2 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "2");
        if(followUpDate2!=null){
            treatmentPlan2 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "2","TREATMENT PLAN");
            conclusionFollowup2 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "2","CONCLUSION OF TREATMENT FOLLOW UP");

            if(treatmentPlan2==null){
                treatmentPlan2 = "-";
            }
            if(conclusionFollowup2==null){
                conclusionFollowup2 = "-";
            }
        }else{
            followUpDate2 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "2.0");
            if(followUpDate2!=null) {
                treatmentPlan2 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup", "FOLLOW-UP MONTH", "2.0", "TREATMENT PLAN");
                conclusionFollowup2 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "2.0","CONCLUSION OF TREATMENT FOLLOW UP");

                if (treatmentPlan2 == null) {
                    treatmentPlan2 = "-";
                }
                if (conclusionFollowup2 == null) {
                    conclusionFollowup2 = "-";
                }
            }else{
                followUpDate2 = "-";
                treatmentPlan2 = "-";
                conclusionFollowup2 = "-";
            }
        }


        String treatmentPlan3=null,conclusionFollowup3=null;
        String followUpDate3 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "3");
        if(followUpDate3!=null){
            treatmentPlan3 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "3","TREATMENT PLAN");
            conclusionFollowup3 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "3","CONCLUSION OF TREATMENT FOLLOW UP");

            if(treatmentPlan3==null){
                treatmentPlan3 = "-";
            }
            if(conclusionFollowup3==null){
                conclusionFollowup3 = "-";
            }
        }else{
            followUpDate3 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "3.0");
            if(followUpDate3!=null) {
                treatmentPlan3 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup", "FOLLOW-UP MONTH", "3.0", "TREATMENT PLAN");
                conclusionFollowup3 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "3.0","CONCLUSION OF TREATMENT FOLLOW UP");

                if (treatmentPlan3 == null) {
                    treatmentPlan3 = "-";
                }
                if (conclusionFollowup3 == null) {
                    conclusionFollowup3 = "-";
                }
            }else{
                followUpDate3 = "-";
                treatmentPlan3 = "-";
                conclusionFollowup3 = "-";
            }
        }


        String treatmentPlan5=null,conclusionFollowup5=null;
        String followUpDate5 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "5");
        if(followUpDate5!=null){
            treatmentPlan5 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "5","TREATMENT PLAN");
            conclusionFollowup5 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "5","CONCLUSION OF TREATMENT FOLLOW UP");

            if(treatmentPlan5==null){
                treatmentPlan5 = "-";
            }
            if(conclusionFollowup5==null){
                conclusionFollowup5 = "-";
            }
        }else{
            followUpDate5 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "5.0");
            if(followUpDate5!=null) {
                treatmentPlan5 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup", "FOLLOW-UP MONTH", "5.0", "TREATMENT PLAN");
                conclusionFollowup5 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "5.0","CONCLUSION OF TREATMENT FOLLOW UP");

                if (treatmentPlan5 == null) {
                    treatmentPlan5 = "-";
                }
                if (conclusionFollowup5 == null) {
                    conclusionFollowup5 = "-";
                }
            }else{
                treatmentPlan5 = "-";
                conclusionFollowup5 = "-";
                followUpDate5 = "-";
            }
        }


        String treatmentPlan6=null,conclusionFollowup6=null;
        String followUpDate6 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "6");
        if(followUpDate6!=null){
            treatmentPlan6 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "6","TREATMENT PLAN");
            conclusionFollowup6 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "6","CONCLUSION OF TREATMENT FOLLOW UP");

            if(treatmentPlan6==null){
                treatmentPlan6 = "-";
            }
            if(conclusionFollowup6==null){
                conclusionFollowup6 = "-";
            }
        }else{
            followUpDate6 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "6.0");
            if(followUpDate6!=null) {
                treatmentPlan6 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup", "FOLLOW-UP MONTH", "6.0", "TREATMENT PLAN");
                conclusionFollowup6 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "6.0","CONCLUSION OF TREATMENT FOLLOW UP");

                if (treatmentPlan6 == null) {
                    treatmentPlan6 = "-";
                }
                if (conclusionFollowup6 == null) {
                    conclusionFollowup6 = "-";
                }
            }else{
                treatmentPlan6 = "-";
                conclusionFollowup6 = "-";
                followUpDate6 = "-";
            }
        }

        String treatmentPlan7=null,conclusionFollowup7=null;
        String followUpDate7 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "7");
        if(followUpDate7!=null){
            treatmentPlan7 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "7","TREATMENT PLAN");
            conclusionFollowup7 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "7","CONCLUSION OF TREATMENT FOLLOW UP");

            if(treatmentPlan7==null){
                treatmentPlan7 = "-";
            }
            if(conclusionFollowup7==null){
                conclusionFollowup7 = "-";
            }
        }else{
            followUpDate7 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "7.0");
            if(followUpDate7!=null) {
                treatmentPlan7 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup", "FOLLOW-UP MONTH", "7.0", "TREATMENT PLAN");
                conclusionFollowup7 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "7.0","CONCLUSION OF TREATMENT FOLLOW UP");

                if (treatmentPlan7 == null) {
                    treatmentPlan7 = "-";
                }
                if (conclusionFollowup7 == null) {
                    conclusionFollowup7 = "-";
                }
            }else{
                treatmentPlan7 = "-";
                conclusionFollowup7 = "-";
                followUpDate7 = "-";
            }
        }

        String treatmentPlan8=null,conclusionFollowup8=null;
        String followUpDate8 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "8");
        if(followUpDate8!=null){
            treatmentPlan8 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "8","TREATMENT PLAN");
            conclusionFollowup8 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "8","CONCLUSION OF TREATMENT FOLLOW UP");

            if(treatmentPlan8==null){
                treatmentPlan8 = "-";
            }
            if(conclusionFollowup8==null){
                conclusionFollowup8 = "-";
            }
        }else{
            followUpDate8 = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "8.0");
            if(followUpDate8!=null) {
                treatmentPlan8 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup", "FOLLOW-UP MONTH", "8.0", "TREATMENT PLAN");
                conclusionFollowup8 = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-TB Treatment Followup","FOLLOW-UP MONTH", "8.0","CONCLUSION OF TREATMENT FOLLOW UP");

                if (treatmentPlan8 == null) {
                    treatmentPlan8 = "-";
                }
                if (conclusionFollowup8 == null) {
                    conclusionFollowup8 = "-";
                }
            }else{
                followUpDate8 = "-";
                conclusionFollowup8 = "-";
                treatmentPlan8 = "-";

            }
        }


        String treatmentOutCome =  serverService.getLatestObsValue(App.getPatientId(), "End of Followup", "TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutCome == null)
            treatmentOutCome = "-";
        else
            treatmentOutCome = App.convertToTitleCase(treatmentOutCome);

        String[][] dataset = {
                {getResources().getString(R.string.external_id), externalId, null},
                {getResources().getString(R.string.screening_facility), screeningFacility, null},
                {getResources().getString(R.string.weight), weight, null},
                {getResources().getString(R.string.height), height, null},
                {getResources().getString(R.string.percentile), percentile, null},
                {getResources().getString(R.string.ppa_score), App.convertToTitleCase(ppaScore), null},
                {getResources().getString(R.string.test_confirming_diagnosis), testConfirmingDiagnosis, null},
                {getResources().getString(R.string.type_of_diagnosis),typeOfDiagnosis, null},

                {getResources().getString(R.string.type_of_tb), typeOfTB, null},
                {getResources().getString(R.string.extra_pulmonary_site), extraPulmonarySite, null},
                {getResources().getString(R.string.patient_type), patientType, null},
                {getResources().getString(R.string.tb_category), tbCategory,null},
                {getResources().getString(R.string.treatment_initiated), treatmentInitiated, null},
                {getResources().getString(R.string.reason_not_initiated), reasonNotInitiated, null},
                {getResources().getString(R.string.additional_treatment), additionalTreatment, null},
                {getResources().getString(R.string.tb_registration_no), tbRegistrationNumber, null},

                {getResources().getString(R.string.followup_date_2), followUpDate2, null},
                {getResources().getString(R.string.patient_treatment_plan_2), App.convertToTitleCase(treatmentPlan2), null},
                {getResources().getString(R.string.conclusion_of_tx_followup_2), App.convertToTitleCase(conclusionFollowup2), null},


                {getResources().getString(R.string.followup_date_3), followUpDate3, null},
                {getResources().getString(R.string.patient_treatment_plan_3), App.convertToTitleCase(treatmentPlan3), null},
                {getResources().getString(R.string.conclusion_of_tx_followup_3), App.convertToTitleCase(conclusionFollowup3), null},

                {getResources().getString(R.string.followup_date_5),followUpDate5, null},
                {getResources().getString(R.string.patient_treatment_plan_5), App.convertToTitleCase(treatmentPlan5), null},
                {getResources().getString(R.string.conclusion_of_tx_followup_5), App.convertToTitleCase(conclusionFollowup5), null},

                {getResources().getString(R.string.followup_date_6), followUpDate6, null},
                {getResources().getString(R.string.patient_treatment_plan_6), App.convertToTitleCase(treatmentPlan6), null},
                {getResources().getString(R.string.conclusion_of_tx_followup_6), App.convertToTitleCase(conclusionFollowup6), null},

                {getResources().getString(R.string.followup_date_7), followUpDate7, null},
                {getResources().getString(R.string.patient_treatment_plan_7), App.convertToTitleCase(treatmentPlan7), null},
                {getResources().getString(R.string.conclusion_of_tx_followup_7), App.convertToTitleCase(conclusionFollowup7), null},

                {getResources().getString(R.string.followup_date_8), followUpDate8, null},
                {getResources().getString(R.string.patient_treatment_plan_8), App.convertToTitleCase(treatmentPlan8), null},
                {getResources().getString(R.string.conclusion_of_tx_followup_8), App.convertToTitleCase(conclusionFollowup8), null},


                {getResources().getString(R.string.treatment_outcome), treatmentOutCome, null},
        };



        fillContent(dataset);

    }

    public void fillComorbiditiesPatientView(){


        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillStaffView(){

        Date date = new Date();
        String todayDate = App.getSqlDate(date);
        ArrayList<String[]> list = new ArrayList<>();

        ArrayList<FormsObject> forms = Forms.getCommonFormList();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if(form.getName().equalsIgnoreCase("ZTTS-Enumeration"))
                continue;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                int count = serverService.getOnlineEncounterCountForDate(todayDate, form.getName());
                String[] dataset = {form.getName(), String.valueOf(count), null};
                list.add(dataset);

            }

        }

        forms = Forms.getScreeningFormList();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                if(form.getName().equalsIgnoreCase("FAST-Screening")){

                    int countScreening =  serverService.getOnlineGwtAppFormCount(todayDate, "fast_screening");
                    if(countScreening == -1) countScreening = 0;
                    String[] dataset = {form.getName(), String.valueOf(countScreening), null };
                    list.add(dataset);

                } else if(form.getName().equalsIgnoreCase("FAST-Sputum Container and X-Ray Voucher")){

                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "FAST-Prompt");
                    String[] dataset = {form.getName(), String.valueOf(count), null};
                    list.add(dataset);

                } else {

                    int count = serverService.getOnlineEncounterCountForDate(todayDate, form.getName());
                    String[] dataset = {form.getName(), String.valueOf(count), null};
                    list.add(dataset);

                }
            }

        }

        forms = Forms.getTestFormList();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                if(form.getName().equals("GXP Specimen Collection") || form.getName().equals("GeneXpert Result") ) {
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, form.getName());
                    String[] dataset = {form.getName(), String.valueOf(count), null};
                    list.add(dataset);
                }else if(form.getName().equals("CXR Order and Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "CXR Screening Test Order");
                    String[] dataset = {"CXR Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "CXR Screening Test Result");
                    String[] dataset1 = {"CXR Result", String.valueOf(count), null};
                    list.add(dataset1);
                }else if(form.getName().equals("AFB Smear Order & Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "AFB Smear Test Order");
                    String[] dataset = {"AFB Smear Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "AFB Smear Test Result");
                    String[] dataset1 = {"AFB Smear Result", String.valueOf(count), null};
                    list.add(dataset1);
                }else if(form.getName().equals("DST Order & Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "DST Culture Test Order");
                    String[] dataset = {"DST Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "DST Culture Test Result");
                    String[] dataset1 = {"DST Result", String.valueOf(count), null};
                    list.add(dataset1);
                }else if(form.getName().equals("Ultrasound Order & Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "Ultrasound Test Order");
                    String[] dataset = {"Ultrasound Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "Ultrasound Test Result");
                    String[] dataset1 = {"Ultrasound Result", String.valueOf(count), null};
                    list.add(dataset1);
                }else if(form.getName().equals("CT Scan Order & Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "CT Scan Test Order");
                    String[] dataset = {"CT Scan Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "CT Scan Test Result");
                    String[] dataset1 = {"CT Scan Result", String.valueOf(count), null};
                    list.add(dataset1);
                }else if(form.getName().equals("Mantoux Order & Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "Mantoux Test Order");
                    String[] dataset = {"Mantoux Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "Mantoux Test Result");
                    String[] dataset1 = {"Mantoux Result", String.valueOf(count), null};
                    list.add(dataset1);
                }else if(form.getName().equals("Histopathology Order & Result")){
                    int count = serverService.getOnlineEncounterCountForDate(todayDate, "Histopathology Test Order");
                    String[] dataset = {"Histopathology Order", String.valueOf(count), null};
                    list.add(dataset);
                    count = serverService.getOnlineEncounterCountForDate(todayDate, "Histopathology Test Result");
                    String[] dataset1 = {"Histopathology Result", String.valueOf(count), null};
                    list.add(dataset1);
                }

            }

        }

        forms = Forms.getTreatmentFormList();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                int count = serverService.getOnlineEncounterCountForDate(todayDate, form.getName());
                String[] dataset = {form.getName(), String.valueOf(count), null};
                list.add(dataset);

            }

        }

        forms = Forms.getComorbiditiesFormList();
        for (int i = 0; i < forms.size(); i++) {
            final FormsObject form = forms.get(i);

            Boolean add = false;

            if (!(App.getRoles().contains(Roles.DEVELOPER))) {

                String pr = App.getPrivileges();
                if(pr.contains("Add "+form.getName()))
                    add = true;

            } else
                add = true;

            if(add){

                int count = serverService.getOnlineEncounterCountForDate(todayDate, "Comorbidities-"+form.getName());
                String[] dataset = {form.getName(), String.valueOf(count), null};
                list.add(dataset);


            }

        }

        fillContent(list);

    }



    public void fillPetClinicianPatientView(){

        String clinicianScreeningDate = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM);
        if(clinicianScreeningDate == null) clinicianScreeningDate = "-";

        String weight = serverService.getLatestObsValue(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM, "WEIGHT (KG)");
        if(weight == null) weight = "-";

        String height = serverService.getLatestObsValue(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM, "HEIGHT (CM)");
        if(height == null) height = "-";

        String bmi = serverService.getLatestObsValue(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM, "BODY MASS INDEX");
        if(bmi == null) bmi = "-";

        String indexIdHighlight = null;
        String indexId = serverService.getLatestObsValue(App.getPatientId(), "PATIENT ID OF INDEX CASE");
        String drugSensitivity = "-";
        String indexDSTCulture = "-";
        if (!(indexId == null || indexId.equals(""))) {
            String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
            if(id == null) {
                drugSensitivity = "- Missing " + indexId + " details -";
                indexDSTCulture = "- Missing " + indexId + " details -";
                indexIdHighlight = "Note";
            }
            else {
                drugSensitivity = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS INFECTION TYPE");
                indexDSTCulture = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "RESISTANT TO ANTI-TUBERCULOSIS DRUGS");
            }
        }
        if(drugSensitivity == null) drugSensitivity = "-";
        if(indexDSTCulture == null) indexDSTCulture = "-";

        String afbSmearResult = serverService.getLatestObsValue(App.getPatientId(), "AFB Smear Test Result", "SPUTUM FOR ACID FAST BACILLI");
        if(afbSmearResult == null) afbSmearResult = "-";

        String cxrResult = serverService.getLatestObsValue(App.getPatientId(), "CXR Screening Test Result", "CHEST X-RAY SCORE");
        if(cxrResult == null) cxrResult = "-";

        String radiologicalDiagnosis = serverService.getLatestObsValue(App.getPatientId(), "CXR Screening Test Result", "RADIOLOGICAL DIAGNOSIS");
        if(radiologicalDiagnosis == null) radiologicalDiagnosis = "-";

        String gxpResult = serverService.getLatestObsValue(App.getPatientId(), Forms.GENEXPERT_RESULT_FORM, "GENEXPERT MTB/RIF RESULT");
        if(gxpResult == null) gxpResult = "-";

        String dstCultureTest = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.CLINICIAN_EVALUATION_FORM);
        if(dstCultureTest == null) dstCultureTest = "-";
        else {

            dstCultureTest  = "-";
            String resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "ISONIAZID 0.2 µg/ml RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Isoniazid 0.2 µg/ml";
                else
                    dstCultureTest = dstCultureTest + ", Isoniazid 0.2 µg/ml";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "ISONIAZID 1 µg/ml RESISTANT RESULT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Isoniazid 1 µg/ml";
                else
                    dstCultureTest = dstCultureTest + ", Isoniazid 1 µg/ml";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "RIFAMPICIN RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Rifampicin";
                else
                    dstCultureTest = dstCultureTest + ", Rifampicin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "ETHAMBUTOL RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Ethambutol";
                else
                    dstCultureTest = dstCultureTest + ", Ethambutol";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "STREPTOMYCIN RESISITANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Streptomycin";
                else
                    dstCultureTest = dstCultureTest + ", Streptomycin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "PYRAZINAMIDE RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Pyrazinamide";
                else
                    dstCultureTest = dstCultureTest + ", Pyrazinamide";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "OFLOAXCIN RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Ofloaxcin";
                else
                    dstCultureTest = dstCultureTest + ", Ofloaxcin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "LEVOFLOXACIN RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Levofloxacin";
                else
                    dstCultureTest = dstCultureTest + ", Levofloxacin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "MOXIFLOXACIN 0.5 µg/ml RESISTANT RESULT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Moxifloxacin 0.5 µg/ml";
                else
                    dstCultureTest = dstCultureTest + ", Moxifloxacin 0.5 µg/ml";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "MOXIFLOXACIN 2 µg/ml RESISTANT RESULT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Moxifloxacin 2 µg/ml";
                else
                    dstCultureTest = dstCultureTest + ", Moxifloxacin 2 µg/ml";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "AMIKACIN RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Amikacin";
                else
                    dstCultureTest = dstCultureTest + ", Amikacin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "KANAMYCIN RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Kanamycin";
                else
                    dstCultureTest = dstCultureTest + ", Kanamycin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "CAPREOMYCIN RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Capreomycin";
                else
                    dstCultureTest = dstCultureTest + ", Capreomycin";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Clture Test Result", "ETHIONAMIDE RESISTANT Ethionamide");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Ethionamide";
                else
                    dstCultureTest = dstCultureTest + ", Ethionamide";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "CYCLOSERINE RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Cycloserine";
                else
                    dstCultureTest = dstCultureTest + ", Cycloserine";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "P AMINOSALICYLIC ACID RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "P Amisonsalicylic acid";
                else
                    dstCultureTest = dstCultureTest + ", P Amisonsalicylic acid";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "BEDAQUILINE RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Bedaquiline";
                else
                    dstCultureTest = dstCultureTest + ", Bedaquiline";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "DELAMANID RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Delamanid";
                else
                    dstCultureTest = dstCultureTest + ", Delamanid";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "LINEZOLID RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Linezolid";
                else
                    dstCultureTest = dstCultureTest + ", Linezolid";
            }

            resistant = serverService.getLatestObsValue(App.getPatientId(), "DST Culture Test Result", "CLOFAZAMINE RESISTANT");
            if(resistant != null && resistant.equalsIgnoreCase("RESISTANT")) {

                if(dstCultureTest.equals("-"))
                    dstCultureTest = "Clofazamine";
                else
                    dstCultureTest = dstCultureTest + ", Clofazamine";
            }

        }

        String treatmentInitiationDate = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "TREATMENT START DATE");
        if(treatmentInitiationDate == null) treatmentInitiationDate = "-";

        String treatmentRegimen = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
        if(treatmentRegimen == null) treatmentRegimen = "-";

        String isonazidDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ISONIAZID DOSE");
        if(isonazidDose == null) isonazidDose = "-";

        String riapentineDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "RIFAPENTINE DOSE");
        if(riapentineDose == null) riapentineDose = "-";

        String levofloxacinDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "LEVOFLOXACIN DOSE");
        if(levofloxacinDose == null) levofloxacinDose = "-";

        String ethinomideDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ETHIONAMIDE DOSE");
        if(ethinomideDose == null) ethinomideDose = "-";

        String ancillaryDrugs = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "ANCILLARY DRUGS");
        if(ancillaryDrugs == null) ancillaryDrugs = "-";

        String followupUpMonth = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_COUNSELLING_FOLLOWUP, "FOLLOW-UP MONTH");
        if(followupUpMonth == null) followupUpMonth = "-";

        String returnVisitDate = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RETURN VISIT DATE");
        if(returnVisitDate == null) returnVisitDate = "-";

        String missedDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH");
        if(missedDose == null) missedDose = "-";

        String actionPlan = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ACTION PLAN");
        if(actionPlan == null) actionPlan = "-";

        String clinicianNote = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "CLINICIAN NOTES (TEXT)");
        if(clinicianNote == null) clinicianNote = "-";


        String[][] dataset = { {getResources().getString(R.string.pet_clinician_view_screening_date), clinicianScreeningDate, null},
                {getResources().getString(R.string.pet_clinician_view_weight),weight, null},
                {getResources().getString(R.string.pet_clinician_view_height),height, null},
                {getResources().getString(R.string.pet_clinician_view_bmi),bmi, null},
                {getResources().getString(R.string.pet_clinician_view_index_patient_type),App.convertToTitleCase(drugSensitivity), indexIdHighlight},
                {getResources().getString(R.string.pet_clinician_view_index_dst_result),App.convertToTitleCase(indexDSTCulture), null},
                {getResources().getString(R.string.pet_clinician_view_afb_smear_result),App.convertToTitleCase(afbSmearResult), null},
                {getResources().getString(R.string.pet_clinician_view_xray_result),App.convertToTitleCase(cxrResult), null},
                {getResources().getString(R.string.pet_clinician_view_radialogical_diagnosis),App.convertToTitleCase(radiologicalDiagnosis), null},
                {getResources().getString(R.string.pet_clinician_view_genexpertResult),App.convertToTitleCase(gxpResult), null},
                {getResources().getString(R.string.pet_clinician_view_dst_culture),dstCultureTest, null},
                {getResources().getString(R.string.pet_clinician_view_treatment_initiation_date),treatmentInitiationDate, null},
                {getResources().getString(R.string.pet_clinician_view_treatment_regimen),App.convertToTitleCase(treatmentRegimen), null},
                {getResources().getString(R.string.pet_clinician_view_isoniazid_dose),isonazidDose, null},
                {getResources().getString(R.string.pet_clinician_view_rifapentine_dose),riapentineDose, null},
                {getResources().getString(R.string.pet_clinician_view_levofloxacin_dose),levofloxacinDose, null},
                {getResources().getString(R.string.pet_clinician_view_ethionamide_dose),ethinomideDose, null},
                {getResources().getString(R.string.pet_clinician_view_followup_month),followupUpMonth, null},
                {getResources().getString(R.string.pet_clinician_view_return_visit_date),returnVisitDate, null},
                {getResources().getString(R.string.pet_clinician_view_missed_dose),missedDose, null},
                {getResources().getString(R.string.pet_clinician_view_action_plan),App.convertToTitleCase(actionPlan), null},
                {getResources().getString(R.string.pet_clinician_view_clinician_note),clinicianNote, null}};

        fillContent(dataset);

    }

    public void fillPetCounselorPatientView(){

        String indexIdHighlight = null;
        String indexId = serverService.getLatestObsValue(App.getPatientId(), "PATIENT ID OF INDEX CASE");
        String drugSensitivity = "-";
        String tbType = "-";
        if (!(indexId == null || indexId.equals(""))) {
            String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
            if(id == null) {
                drugSensitivity = "- Missing " + indexId + " details -";

                tbType = "- Missing " + indexId + " details -";
                indexIdHighlight = "Note";
            }
            else {
                drugSensitivity = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS INFECTION TYPE");
                tbType = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "SITE OF TUBERCULOSIS DISEASE");
            }
        }
        if(drugSensitivity == null) drugSensitivity = "-";
        if(tbType == null) tbType = "-";

        String treatmentInitiationDate = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "TREATMENT START DATE");
        if(treatmentInitiationDate == null) treatmentInitiationDate = "-";

        String treatmentRegimen = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
        if(treatmentRegimen == null) treatmentRegimen = "-";

        String treatmentWeek = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_ADHERENCE, "NUMBER OF WEEKS ON TREATMENT");
        if(treatmentWeek == null) treatmentWeek = "-";

        String adverseEventReported = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_ADHERENCE, "ADVERSE EVENTS");
        if(adverseEventReported == null) adverseEventReported = "-";

        String psychologistComment = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_ADHERENCE, "CLINICIAN NOTES (TEXT)");
        if(psychologistComment == null) psychologistComment = "-";

        String treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_ADHERENCE, "TREATMENT PLAN (TEXT)");
        if(treatmentPlan == null) treatmentPlan = "-";

        String returnVisitDate = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RETURN VISIT DATE");
        if(returnVisitDate == null) returnVisitDate = "-";

        String missedDose = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "NUMBER OF MISSED MEDICATION DOSES IN LAST MONTH");
        if(missedDose == null) missedDose = "-";

        String actionPlan = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "ACTION PLAN");
        if(actionPlan == null) actionPlan = "-";

        String[][] dataset = { {getResources().getString(R.string.pet_counselor_view_index_patient_type), App.convertToTitleCase(drugSensitivity), indexIdHighlight},
                {getResources().getString(R.string.pet_counselor_view_tb_type),App.convertToTitleCase(tbType), indexIdHighlight},
                {getResources().getString(R.string.pet_counselor_view_treatment_initiation_date),treatmentInitiationDate, null},
                {getResources().getString(R.string.pet_counselor_view_treatment_regimen),App.convertToTitleCase(treatmentRegimen), null},
                {getResources().getString(R.string.pet_counselor_view_treatment_week),treatmentWeek, null},
                {getResources().getString(R.string.pet_counselor_view_adverse_events),App.convertToTitleCase(adverseEventReported), null},
                {getResources().getString(R.string.pet_counselor_view_comment_by_psycologist),psychologistComment, null},
                {getResources().getString(R.string.pet_counselor_view_treatment_plan),treatmentPlan, null},
                {getResources().getString(R.string.pet_counselor_view_return_visit_date),returnVisitDate, null},
                {getResources().getString(R.string.pet_counselor_view_missed_dose),missedDose, null},
                {getResources().getString(R.string.pet_counselor_view_action_plan),App.convertToTitleCase(actionPlan), null}};

        fillContent(dataset);

    }

    public void fillPetIncentiveDispatchView(){

        String indexIdHighlight = null;
        String indexId = serverService.getLatestObsValue(App.getPatientId(),"PATIENT ID OF INDEX CASE");
        String drugSensitivity = "-";
        if (!(indexId == null || indexId.equals(""))) {
            String id = serverService.getPatientSystemIdByIdentifierLocalDB(indexId);
            if(id == null) {
                drugSensitivity = "- Missing " + indexId + " details -";
                indexIdHighlight = "Note";
            }
            else
                drugSensitivity = serverService.getLatestObsValue(id, Forms.PET_INDEX_PATIENT_REGISTRATION, "TUBERCULOSIS INFECTION TYPE");
        }
        if(drugSensitivity == null) drugSensitivity = "-";

        String investigationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "CXR Screening Test Order");
        if(investigationDate == null) investigationDate = "-";

        String treatmentInitiationDate = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "TREATMENT START DATE");
        if(treatmentInitiationDate == null) treatmentInitiationDate = "-";

        String followupUpMonth = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_COUNSELLING_FOLLOWUP, "FOLLOW-UP MONTH");
        if(followupUpMonth == null) followupUpMonth = "-";

        String returnVisitDate = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "RETURN VISIT DATE");
        if(returnVisitDate == null) returnVisitDate = "-";

        String[] amounts = serverService.getAllObsValues(App.getPatientId(), Forms.PET_INCENTIVE_DISBURSEMENT, "INCENTIVE AMOUNT");
        Double totalAmount = 0.0;
        if(amounts != null)
            for(String amount: amounts)
                totalAmount = totalAmount + Double.parseDouble(amount);

        String[][] dataset = { {getResources().getString(R.string.pet_incentive_dispatch_index_patient_type), App.convertToTitleCase(drugSensitivity), indexIdHighlight},
                {getResources().getString(R.string.pet_incentive_dispatch_investigation_date),investigationDate, null},
                {getResources().getString(R.string.pet_incentive_dispatch_treatment_initiation_date),treatmentInitiationDate, null},
                {getResources().getString(R.string.pet_incentive_dispatch_followup_month),followupUpMonth, null},
                {getResources().getString(R.string.pet_incentive_dispatch_return_visit_date),returnVisitDate, null},
                {getResources().getString(R.string.pet_incentive_dispatch_total_amount_paid),String.valueOf(totalAmount), null}};

        fillContent(dataset);

    }

    public void fillContent(ArrayList<String[]> arrayList){

        int color = App.getColor(context, R.attr.colorPrimaryDark);

        for (int j = 0; j < arrayList.size(); j++) {

            String[] dataset = arrayList.get(j);

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView question = new TextView(context);
            question.setText(dataset[0]);
            question.setTextSize(getResources().getDimension(R.dimen.medium));
            question.setTextColor(color);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.weight = 1;
            question.setLayoutParams(p);
            linearLayout.addView(question);
            question.setPadding(0, 10, 20, 10);

            TextView answer = new TextView(context);
            answer.setText(dataset[1]);
            if(dataset[2] != null && dataset[2].equals("Highlight")){
                answer.setTextColor(Color.RED);
                answer.setTypeface(null, Typeface.BOLD);
            } else if(dataset[2] != null && dataset[2].equals("Note")){
                answer.setTextColor(Color.RED);
                answer.setTextSize(getResources().getDimension(R.dimen.tiny));
            }

            answer.setTextSize(getResources().getDimension(R.dimen.medium));
            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p1.weight = 1;
            answer.setLayoutParams(p1);
            linearLayout.addView(answer);
            answer.setPadding(0, 10, 0, 10);

            content.addView(linearLayout);
        }

    }


    public void fillContent(String[][] dataset){

        int color = App.getColor(context, R.attr.colorPrimaryDark);

        for (int j = 0; j < dataset.length; j++) {

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView question = new TextView(context);
            question.setText(dataset[j][0]);
            question.setTextSize(getResources().getDimension(R.dimen.medium));
            question.setTextColor(color);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.weight = 1;
            question.setLayoutParams(p);
            linearLayout.addView(question);
            question.setPadding(0, 10, 20, 10);

            TextView answer = new TextView(context);
            answer.setText(dataset[j][1]);
            if(dataset[j][2] != null && dataset[j][2].equals("Highlight")){
                answer.setTextColor(Color.RED);
                answer.setTypeface(null, Typeface.BOLD);
            } else if(dataset[j][2] != null && dataset[j][2].equals("Note")){
                answer.setTextColor(Color.RED);
                answer.setTextSize(getResources().getDimension(R.dimen.tiny));
            }

            answer.setTextSize(getResources().getDimension(R.dimen.medium));
            LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p1.weight = 1;
            answer.setLayoutParams(p1);
            linearLayout.addView(answer);
            answer.setPadding(0, 10, 0, 10);

            content.addView(linearLayout);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                content.removeAllViews();
                String viewName = heading.getText().toString();
                if(viewName.equals(getString(R.string.general_patient_view)))
                    fillGeneralPatientView();
                else if(viewName.equals(getString(R.string.staff_view)))
                    fillStaffView();
                else if(viewName.equals(getString(R.string.fast_patient_view)))
                    fillFastPatientView();
                else if(viewName.equals(getString(R.string.childhood_tb_patient_view)))
                    fillChildhoodTbPatientView();
                else if(viewName.equals(getString(R.string.comorbidities_patient_view)))
                    fillComorbiditiesPatientView();
                else if(viewName.equals(getString(R.string.pet_patient_view)))
                    fillPetPatientView();
                else if(viewName.equals(getString(R.string.pet_incentive_dispatch)))
                    fillPetIncentiveDispatchView();
                else if(viewName.equals(getString(R.string.pet_counselor_view)))
                    fillPetCounselorPatientView();
                else if(viewName.equals(getString(R.string.pet_clinician_view)))
                    fillPetClinicianPatientView();

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }

    private Button createButton(String name, int color){

        Button button = new Button(this.context);
        button.setText(name);
        button.setBackgroundResource(R.drawable.summary_button);
        Drawable top = getResources().getDrawable(R.drawable.ic_summary_view);
        top.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        button.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
        button.setCompoundDrawablePadding((int)getResources().getDimension(R.dimen.medium));
        float padding = getResources().getDimension(R.dimen.large);
        button.setPadding((int)padding,(int)padding,(int)padding,(int)padding);
        button.setTextColor(color);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, (int)getResources().getDimension(R.dimen.medium));
        button.setLayoutParams(params);

        button.setOnClickListener(this);

        return button;
    }
}
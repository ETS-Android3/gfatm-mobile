package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.shared.FormTypeColor;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.ServerService;


public class SummaryFragment extends Fragment implements View.OnClickListener {

    Context context;

    ServerService serverService;

    ScrollView mainView;
    Button patientAttribute;
    Button generalPatientView;
    Button interventionPatientView;
    Button interventionStaffView;

    LinearLayout contentView;
    TextView backButton;
    TextView heading;
    LinearLayout content;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View mainContent = inflater.inflate(
                R.layout.summary_fragment, container, false);

        context = mainContent.getContext();

        serverService = new ServerService(context.getApplicationContext());

        patientAttribute = (Button) mainContent.findViewById(R.id.patientAttribute);
        DrawableCompat.setTint(patientAttribute.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), FormTypeColor.REGISTRATION_FORM));
        generalPatientView = (Button) mainContent.findViewById(R.id.genralPatientView);
        DrawableCompat.setTint(generalPatientView.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), FormTypeColor.FOLLOWUP_FORM));
        interventionPatientView = (Button) mainContent.findViewById(R.id.patientView);
        DrawableCompat.setTint(interventionPatientView.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), FormTypeColor.FOLLOWUP_FORM));
        interventionStaffView = (Button) mainContent.findViewById(R.id.dailyStaffView);
        DrawableCompat.setTint(interventionStaffView.getCompoundDrawables()[1], App.getColor(mainContent.getContext(), FormTypeColor.OTHER_FORM));
        mainView = (ScrollView) mainContent.findViewById(R.id.mainView);
        contentView = (LinearLayout) mainContent.findViewById(R.id.contentView);
        backButton = (TextView) mainContent.findViewById(R.id.backButton);
        heading = (TextView) mainContent.findViewById(R.id.heading);
        content = (LinearLayout) mainContent.findViewById(R.id.content);

        patientAttribute.setOnClickListener(this);
        generalPatientView.setOnClickListener(this);
        interventionPatientView.setOnClickListener(this);
        interventionStaffView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        updateSummaryFragment();

        return mainContent;
    }

    public void updateSummaryFragment(){

        if(App.getPatient() == null) {
            patientAttribute.setVisibility(View.GONE);
            generalPatientView.setVisibility(View.GONE);
            interventionPatientView.setVisibility(View.GONE);
        }
        else {
            patientAttribute.setVisibility(View.VISIBLE);
            generalPatientView.setVisibility(View.VISIBLE);
            interventionPatientView.setVisibility(View.VISIBLE);
        }

        if(App.getProgram().equals(getResources().getString(R.string.fast))){
            interventionPatientView.setText(getString(R.string.fast_patient_view));
            interventionStaffView.setText(getString(R.string.fast_staff_view));
        } else if(App.getProgram().equals(getResources().getString(R.string.pet))){
            interventionPatientView.setText(getString(R.string.pet_patient_view));
            interventionStaffView.setText(getString(R.string.pet_staff_view));
        } else if(App.getProgram().equals(getResources().getString(R.string.childhood_tb))){
            interventionPatientView.setText(getString(R.string.childhood_tb_patient_view));
            interventionStaffView.setText(getString(R.string.childhood_tb_staff_view));
        } else if(App.getProgram().equals(getResources().getString(R.string.comorbidities))){
            interventionPatientView.setText(getString(R.string.comorbidities_patient_view));
            interventionStaffView.setText(getString(R.string.comorbidities_patient_view));
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

        if(v == patientAttribute){
            setMainContentVisible(false);
            heading.setText(getResources().getString(R.string.patient_attribute));
            content.removeAllViews();
            fillPatientAttribute();
        } else if(v == generalPatientView){
            setMainContentVisible(false);
            heading.setText(getResources().getString(R.string.general_patient_view));
            content.removeAllViews();
            fillGeneralPatientView();
        } else if(v == interventionPatientView){
            setMainContentVisible(false);
            if(App.getProgram().equals(getResources().getString(R.string.fast))){
                heading.setText(getString(R.string.fast_patient_view));
                content.removeAllViews();
            } else if(App.getProgram().equals(getResources().getString(R.string.pet))){
                heading.setText(getString(R.string.pet_patient_view));
                content.removeAllViews();
            } else if(App.getProgram().equals(getResources().getString(R.string.childhood_tb))){
                heading.setText(getString(R.string.childhood_tb_patient_view));
                content.removeAllViews();
            } else if(App.getProgram().equals(getResources().getString(R.string.comorbidities))){
                heading.setText(getString(R.string.comorbidities_patient_view));
                content.removeAllViews();
            }
        } else if(v == interventionStaffView){
            setMainContentVisible(false);
            if(App.getProgram().equals(getResources().getString(R.string.fast))){
                heading.setText(getString(R.string.fast_staff_view));
                content.removeAllViews();
                fillFastPatientView();
            } else if(App.getProgram().equals(getResources().getString(R.string.pet))){
                heading.setText(getString(R.string.pet_staff_view));
                content.removeAllViews();
                fillPetPatientView();
            } else if(App.getProgram().equals(getResources().getString(R.string.childhood_tb))){
                heading.setText(getString(R.string.childhood_tb_staff_view));
                content.removeAllViews();
                fillChildhoodTbPatientView();
            } else if(App.getProgram().equals(getResources().getString(R.string.comorbidities))){
                heading.setText(getString(R.string.comorbidities_patient_view));
                content.removeAllViews();
                fillComorbiditiesPatientView();
            }
        } else if(v == backButton){
            setMainContentVisible(true);
        }
    }

    public void fillGeneralPatientView(){

        String externalId =  serverService.getLatestObsValue(App.getPatientId(), "FAST-Presumptive Information", "CONTACT EXTERNAL ID");
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

        String intervention =  serverService.getLatestObsValue(App.getPatientId(), "PET-" + Forms.PET_BASELINE_SCREENING, "INTERVENTION");
        if(intervention == null)
            intervention = "-";

        String presumptive = serverService.getLatestObsValue(App.getPatientId(), "FAST-Presumptive", "PRESUMPTIVE TUBERCULOSIS");
        if(presumptive == null) {
            presumptive = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Presumptive Case Confirmation", "CONCLUSION");
            if(presumptive != null) {
                if (presumptive.equalsIgnoreCase("TB PRESUMPTIVE CONFIRMED"))
                    presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-Presumptive Case Confirmation");
            }
        }  else{
            if(presumptive.equalsIgnoreCase("YES"))
                presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Presumptive");
        }
        if(presumptive == null || presumptive.equalsIgnoreCase("No"))
            presumptive = "-";

        String diagnoseOn = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Treatment Initiation","TUBERCULOSIS DIAGNOSIS METHOD");
        if(diagnoseOn == null)
            diagnoseOn = "-";
        else
            diagnoseOn = App.convertToTitleCase(diagnoseOn);

        String diagnosisType = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Treatment Initiation","TUBERCULOSIS DIAGNOSIS METHOD");
        if(diagnosisType == null)
            diagnosisType = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TUBERCULOSIS DIAGNOSIS METHOD");
        if(diagnosisType == null)
            diagnosisType = "-";
        else
            diagnosisType = App.convertToTitleCase(diagnosisType);

        String xpertResult = "";
        String rifResult = "";
        String xpertResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-GXP Specimen Collection");
        if(xpertResultDate != null){
            String xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-GXP Specimen Collection" , "TEST CONTEXT STATUS", "BASELINE REPEAT", "ORDER ID");
            if(xpertResultOderId == null){
                xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-GXP Specimen Collection", "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
                if(xpertResultOderId == null)
                    xpertResult = null;
                else {
                    xpertResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-GXP Test", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                    rifResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-GXP Test", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");
                }
            }
            else {
                xpertResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-GXP Test", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                rifResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-GXP Test", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");
            }
        } else {
            xpertResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-GXP Specimen Collection");
            if(xpertResultDate != null) {
                String xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Specimen Collection", "TEST CONTEXT STATUS", "BASELINE REPEAT", "ORDER ID");
                if (xpertResultOderId == null) {
                    xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Specimen Collection", "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
                    if (xpertResultOderId == null)
                        xpertResult = null;
                    else {
                        xpertResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                        rifResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");

                    }
                }
                else {
                    xpertResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                    rifResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");

                }

            }
        }
        if(xpertResult != null && !xpertResult.equals(""))
            xpertResult = App.convertToTitleCase(xpertResult);
        else
            xpertResult = "-";
        String xpertHighlight = null;
        if(rifResult != null && rifResult.equals("DETECTED"))
            xpertHighlight = "Highlight";

        String xrayResult = "";
        String xrayResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-CXR Screening Test Order");
        if(xrayResultDate != null){
            String xrayResultOderId = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Order" , "FOLLOW-UP MONTH", "0", "ORDER ID");
            if(xrayResultOderId == null)
                xrayResult = null;
            else {
                xrayResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "ORDER ID", xrayResultOderId, "CHEST X-RAY SCORE");
                if (xrayResult== null)
                    xrayResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "ORDER ID", xrayResultOderId, "RADIOLOGICAL DIAGNOSIS");
            }
        } else {
            xrayResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Screening CXR Test Order");
            if(xrayResultDate != null) {
                String xrayResultOderId = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Order", "FOLLOW-UP MONTH", "0", "ORDER ID");
                if (xrayResultOderId == null)
                    xrayResult = null;
                else {
                    xrayResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", xrayResultOderId, "CHEST X-RAY SCORE");
                    if (xrayResult== null)
                        xrayResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", xrayResultOderId, "RADIOLOGICAL DIAGNOSIS");
                }
            }
        }
        if(xrayResult != null && !xrayResult.equals(""))
            xrayResult = App.convertToTitleCase(xrayResult);
        else
            xrayResult = "-";

        String tbType =  serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation", "SITE OF TUBERCULOSIS DISEASE");
        if(tbType == null)
           tbType =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Treatment Initiation", "SITE OF TUBERCULOSIS DISEASE");
        if(tbType == null)
            tbType= "-";
        else
            tbType = App.convertToTitleCase(tbType);

        String treatmentInitiationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-Treatment Initiation");
        if(treatmentInitiationDate == null)
            treatmentInitiationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Treatment Initiation");
        if(treatmentInitiationDate == null)
            treatmentInitiationDate= "-";

        String registrationNo = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup", "TB REGISTRATION NUMBER");
        if(registrationNo == null)
            registrationNo = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup", "TB REGISTRATION NUMBER");
        if(registrationNo == null)
            registrationNo= "-";

        String infectionTreatmentInitiationDate =  serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-" + Forms.PET_TREATMENT_INITIATION);
        if(infectionTreatmentInitiationDate == null)
            infectionTreatmentInitiationDate = "-";

        String infectionTreatmentRegimen =  serverService.getLatestObsValue(App.getPatientId(), "PET-" + Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
        if(infectionTreatmentRegimen == null)
            infectionTreatmentRegimen = "-";
        else
            infectionTreatmentRegimen = App.convertToTitleCase(infectionTreatmentRegimen);

        String treatmentFacility =  serverService.getEncounterLocation(App.getPatientId(), "FAST-Treatment Initiation");
        if(treatmentFacility == null)
            treatmentFacility =  serverService.getEncounterLocation(App.getPatientId(), "Childhood TB-Treatment Initiation");
        if(treatmentFacility == null)
            treatmentFacility= "-";
        else
            treatmentFacility = App.convertToTitleCase(treatmentFacility);

        String nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup" + Forms.FAST_TREATMENT_FOLLOWUP_FORM, "RETURN VISIT DATE");
        if (nextFollowupDate == null) {
            nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation" + Forms.FAST_TREATMENT_INITIATION_FORM, "RETURN VISIT DATE");
            if(nextFollowupDate == null){
                nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP, "RETURN VISIT DATE");
                if(nextFollowupDate == null)
                    nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-Treatment Initiation" + Forms.CHILDHOODTB_TREATMENT_INITIATION, "RETURN VISIT DATE");
            }
        }
        if(nextFollowupDate == null)
            nextFollowupDate = "-";

        String lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-AFB Smear Test Result");
        if(lastSmearDate == null)
            lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-AFB Smear Test Result");
        if(lastSmearDate == null)
            lastSmearDate= "-";

        String lastSmearResult = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-AFB Smear Test Result", "SPUTUM FOR ACID FAST BACILLI");
        if(lastSmearResult == null)
            lastSmearResult = serverService.getLatestObsValue(App.getPatientId(), "FAST-AFB Smear Test Result", "SPUTUM FOR ACID FAST BACILLI");
        if(lastSmearResult == null)
            lastSmearResult= "-";
        else
            lastSmearResult = App.convertToTitleCase(lastSmearResult);

        String lastXrayDate = "";
        String lastXrayResult = "";
        String lastXrayOrderDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-CXR Screening Test Order");
        if(lastXrayOrderDate != null){
            String month = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-CXR Screening Test Order","FOLLOW-UP MONTH");
            if(!month.equals("0")){
                lastXrayDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-CXR Screening Test Result");
                if(lastXrayDate != null) {
                    String orderId = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-CXR Screening Test Order", "ORDER ID");
                    if(orderId != null) {
                        lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "ORDER ID", orderId, "CHEST X-RAY SCORE");
                        if (lastXrayResult == null)
                            lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "ORDER ID", orderId, "RADIOLOGICAL DIAGNOSIS");
                    }
                }
            } else {
                String[] followupMonths = serverService.getAllObsValues(App.getPatientId(), "Childhood TB-CXR Screening Test Order", "FOLLOW-UP MONTH");
                int followupMonthInt = 0;
                for(String followupMonth: followupMonths){
                    if(followupMonth.equals("0"))
                        continue;

                    int value = Integer.parseInt(followupMonth);
                    if(value > 0){
                        if(value > followupMonthInt)
                            followupMonthInt = value;
                    }
                }
                if(followupMonthInt != 0) {
                    lastXrayDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-CXR Screening Test Result","FOLLOW-UP MONTH", String.valueOf(followupMonthInt));
                    if(lastXrayDate != null) {
                        String orderId = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Order", "FOLLOW-UP MONTH", String.valueOf(followupMonthInt), "ORDER ID");
                        lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "ORDER ID", orderId, "CHEST X-RAY SCORE");
                        if (lastXrayResult == null)
                            lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "ORDER ID", orderId, "RADIOLOGICAL DIAGNOSIS");
                    }
                }
            }
        } else {
            lastXrayOrderDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Screening CXR Test Order");
            if(lastXrayOrderDate != null){
                String month = serverService.getLatestObsValue(App.getPatientId(), "FAST-Screening CXR Test Order","FOLLOW-UP MONTH");
                if(!month.equals("0")){
                    lastXrayDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Screening CXR Test Result");
                    if(lastXrayDate != null) {
                        String orderId = serverService.getLatestObsValue(App.getPatientId(), "FAST-Screening CXR Test Order", "ORDER ID");
                        lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", orderId, "CHEST X-RAY SCORE");
                        if(lastXrayResult == null)
                            lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", orderId, "RADIOLOGICAL DIAGNOSIS");
                    }
                } else {
                    String[] followupMonths = serverService.getAllObsValues(App.getPatientId(), "FAST-Screening CXR Test Order", "FOLLOW-UP MONTH");
                    int followupMonthInt = 0;
                    for(String followupMonth: followupMonths){
                        if(followupMonth.equals("0"))
                            continue;

                        int value = Integer.parseInt(followupMonth);
                        if(value > 0){
                            if(value > followupMonthInt)
                                followupMonthInt = value;
                        }
                    }
                    if(followupMonthInt != 0) {
                        lastXrayDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "FAST-Screening CXR Test Result","FOLLOW-UP MONTH", String.valueOf(followupMonthInt));
                        if(lastXrayDate != null) {
                            String orderId = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Order", "FOLLOW-UP MONTH", String.valueOf(followupMonthInt), "ORDER ID");
                            lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", orderId, "CHEST X-RAY SCORE");
                            if (lastXrayResult == null)
                                lastXrayResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", orderId, "RADIOLOGICAL DIAGNOSIS");
                        }
                    }
                }
            }
        }

        if(lastXrayDate == null || lastXrayDate.equals(""))
            lastXrayDate = "N/A";
        if(lastXrayResult == null || lastXrayResult.equals(""))
            lastXrayResult = "N/A";
        else
            lastXrayResult = App.convertToTitleCase(lastXrayResult);

        String treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-TB Treatment Followup","TREATMENT PLAN");
        if(treatmentPlan == null)
            treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup","TREATMENT PLAN");
        if(treatmentPlan == null)
            treatmentPlan= "-";
        else
            treatmentPlan = App.convertToTitleCase(treatmentPlan);

        String akuadsScreeningDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM);
        if(akuadsScreeningDate == null)
            akuadsScreeningDate = "-";

        String akuadsScore = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, "AKUADS SCORE");
        if(akuadsScore == null)
            akuadsScore = "-";
        else
            akuadsScore = App.convertToTitleCase(akuadsScore);

        String depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-", "RETURN VISIT DATE");
        if(depressionNextFollowupDate == null) {
            depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-", "RETURN VISIT DATE");
            if(depressionNextFollowupDate == null){
                depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Followup", "RETURN VISIT DATE");
                if(depressionNextFollowupDate == null)
                    depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation", "RETURN VISIT DATE");
            }
        }
        if(depressionNextFollowupDate == null)
            depressionNextFollowupDate = "-";

        String diabetesStatus =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-HbA1C Test Result", "DIABETES MELLITUS");
        if (diabetesStatus == null)
            diabetesStatus =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-HbA1C Test Result", "PREVIOUSLY DIAGNOSED DIABETES");
        if(diabetesStatus == null)
            diabetesStatus = "-";
        else
            diabetesStatus = App.convertToTitleCase(diabetesStatus);

        String diabetesFollowupDate =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_DIABETES_TREATMENT_INITIATION, "RETURN VISIT DATE");
        if (diabetesFollowupDate == null)
            diabetesFollowupDate =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM, "RETURN VISIT DATE");
        if(diabetesFollowupDate == null)
            diabetesFollowupDate = "-";

        String treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-End of Followup","TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutcome == null)
            treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(), "FAST-End of Followup","TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutcome == null)
            treatmentOutcome= "-";
        else
            treatmentOutcome = App.convertToTitleCase(treatmentOutcome);

        String[][] dataset = { {getString(R.string.patient_id), App.getPatient().getPatientId(),null},
                {getString(R.string.external_id),externalId,null},
                {getString(R.string.screening_facility),screeningFacility,null,null},
                {getString(R.string.pet_sci_intervention),intervention,null},
                {getString(R.string.presumptive_date),presumptive,null},
                {getString(R.string.diagnosed_on),diagnoseOn,null},
                {getString(R.string.diagnosed_type),diagnosisType,null},
                {getString(R.string.xpert_result),xpertResult,"Highlight"},
                {getString(R.string.xray_result),xrayResult,null},
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
                {getString(R.string.akuads_screening_date),akuadsScreeningDate,null},
                {getString(R.string.akuads_score),akuadsScore,null},
                {getString(R.string.depression_followup_date),depressionNextFollowupDate,null},
                {getString(R.string.diabetes_status),diabetesStatus,null},
                {getString(R.string.diabetes_followup_date),diabetesFollowupDate,null},
                {getString(R.string.final_treatment_outcome),treatmentOutcome,null}};

        fillContent(dataset);

    }

    public void fillPatientAttribute(){

        String[][] dataset = { {"rabbia", "hassan"},
                {"hadi","hassan"},
                {"mohammad","hassan"},
                {"farzana","hassan"}};

        fillContent(dataset);

    }

    public void fillFastPatientView(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan"},
                {"mohammad","hassan"},
                {"farzana","hassan"}};

        fillContent(dataset);

    }

    public void fillPetPatientView(){

        String[][] dataset = { {"rabbia", "hassan"},
                {"hadi","hassan"},
                {"mohammad","hassan"},
                {"farzana","hassan"}};

        fillContent(dataset);

    }

    public void fillChildhoodTbPatientView(){

        String[][] dataset = { {"rabbia", "hassan"},
                {"hadi","hassan"},
                {"mohammad","hassan"},
                {"farzana","hassan"}};

        fillContent(dataset);

    }

    public void fillComorbiditiesPatientView(){

        String[][] dataset = { {"rabbia", "hassan"},
                {"hadi","hassan"},
                {"mohammad","hassan"},
                {"farzana","hassan"}};

        fillContent(dataset);

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

}

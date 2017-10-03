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

import java.util.Date;


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

        interventionStaffView.setVisibility(View.GONE);
        patientAttribute.setVisibility(View.GONE);
        interventionPatientView.setVisibility(View.GONE);
        generalPatientView.setVisibility(View.VISIBLE);
        interventionStaffView.setVisibility(View.VISIBLE);

        if(App.getPatient() == null) {
            patientAttribute.setVisibility(View.GONE);
            generalPatientView.setVisibility(View.GONE);
            interventionPatientView.setVisibility(View.GONE);

            if(App.getProgram().equals(getResources().getString(R.string.fast))) {
                interventionStaffView.setText(getString(R.string.fast_staff_view));
                interventionStaffView.setVisibility(View.VISIBLE);
            }


        }
        else {

            if(App.getProgram().equals(getResources().getString(R.string.fast))){
                interventionPatientView.setText(getString(R.string.fast_patient_view));
                interventionStaffView.setText(getString(R.string.fast_staff_view));
                interventionStaffView.setVisibility(View.VISIBLE);
                interventionPatientView.setVisibility(View.VISIBLE);
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
                fillFastPatientView();
            } else if(App.getProgram().equals(getResources().getString(R.string.pet))){
                heading.setText(getString(R.string.pet_patient_view));
                content.removeAllViews();
                fillPetPatientView();
            } else if(App.getProgram().equals(getResources().getString(R.string.childhood_tb))){
                heading.setText(getString(R.string.childhood_tb_patient_view));
                content.removeAllViews();
                fillChildhoodTbPatientView();
            } else if(App.getProgram().equals(getResources().getString(R.string.comorbidities))){
                heading.setText(getString(R.string.comorbidities_patient_view));
                content.removeAllViews();
                fillComorbiditiesPatientView();
            }
        } else if(v == interventionStaffView){
            setMainContentVisible(false);
            if(App.getProgram().equals(getResources().getString(R.string.fast))){
                heading.setText(getString(R.string.fast_staff_view));
                content.removeAllViews();
                fillFastStaffView();
            } else if(App.getProgram().equals(getResources().getString(R.string.pet))){
                heading.setText(getString(R.string.pet_staff_view));
                content.removeAllViews();
                fillPetStaffView();
            } else if(App.getProgram().equals(getResources().getString(R.string.childhood_tb))){
                heading.setText(getString(R.string.childhood_tb_staff_view));
                content.removeAllViews();
                fillChildhoodTbStaffView();
            } else if(App.getProgram().equals(getResources().getString(R.string.comorbidities))){
                heading.setText(getString(R.string.comorbidities_patient_view));
                content.removeAllViews();
                fillComorbiditiesStaffView();
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

        String lastSmearResult = null;
        String lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-AFB Smear Test Order");
        if(lastSmearDate != null) {
            String orderId = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-AFB Smear Test Order", "ORDER ID");
            if(orderId != null)
                lastSmearResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-AFB Smear Test Result", "ORDER ID", orderId, "SPUTUM FOR ACID FAST BACILLI");
        }
        else {
            lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-AFB Smear Test Order");
            if(lastSmearDate != null) {
                String orderId = serverService.getLatestObsValue(App.getPatientId(), "FAST-AFB Smear Test Order", "ORDER ID");
                if(orderId != null)
                    lastSmearResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", orderId, "SPUTUM FOR ACID FAST BACILLI");
            }
        }
        if(lastSmearDate == null)
            lastSmearDate= "-";
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
                    lastXrayDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "Childhood TB-CXR Screening Test Order","FOLLOW-UP MONTH", String.valueOf(followupMonthInt));
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
                        lastXrayDate = serverService.getEncounterDateTimeByObsValue(App.getPatientId(), "FAST-Screening CXR Test Order","FOLLOW-UP MONTH", String.valueOf(followupMonthInt));
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

        String akuadsScreeningDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM);
        if(akuadsScreeningDate == null)
            akuadsScreeningDate = "-";

        String akuadsScoreHighlight = null;
        String akuadsScore = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, "AKUADS SCORE");
        if(akuadsScore == null)
            akuadsScore = "-";
        else {
            akuadsScore = App.convertToTitleCase(akuadsScore);
            akuadsScore = akuadsScore.replace(" ", "");
            Float as = Float.parseFloat(akuadsScore);
            if(as >=21)
                akuadsScoreHighlight = "Highlight";
        }

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
                {getString(R.string.xpert_result),xpertResult,xpertHighlight},
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
                {getString(R.string.akuads_score),akuadsScore,akuadsScoreHighlight},
                {getString(R.string.depression_followup_date),depressionNextFollowupDate,null},
                {getString(R.string.diabetes_status),diabetesStatus,null},
                {getString(R.string.diabetes_followup_date),diabetesFollowupDate,null},
                {getString(R.string.final_treatment_outcome),treatmentOutcome,null}};

        fillContent(dataset);

    }

    public void fillPatientAttribute(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillFastPatientView(){
        String externalId =  serverService.getLatestObsValue(App.getPatientId(), "FAST-Presumptive Information", "CONTACT EXTERNAL ID");
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

        String gxpSputumDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-GXP Specimen Collection");
        if(gxpSputumDate==null){
            gxpSputumDate= "-";
        }

        String gxpResult=null,rifResult=null;
        String xpertResultOderId = serverService.getLatestObsValue(App.getPatientId(), "FAST-GXP Specimen Collection","ORDER ID");
        if (xpertResultOderId != null) {
            gxpResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
            rifResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "RIF RESISTANCE RESULT");
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
        String cxrDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-Screening CXR Test Order");
        if(cxrDate!=null) {
            cxrResultOrderID = serverService.getLatestObsValue(App.getPatientId(), "FAST-Screening CXR Test Order","ORDER ID");
            if (cxrResultOrderID != null) {
                cad4TbScore = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", cxrResultOrderID, "CHEST X-RAY SCORE");
                radioLogicalDiagnosis = serverService.getObsValueByObs(App.getPatientId(), "FAST-Screening CXR Test Result", "ORDER ID", cxrResultOrderID, "RADIOLOGICAL DIAGNOSIS");
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
        String sputumDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-AFB Smear Test Order");
        if(sputumDate!=null) {
            sputumResultOrderID = serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
            if (sputumResultOrderID != null) {
                smearResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", xpertResultOderId, "SPUTUM FOR ACID FAST BACILLI");
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

        String referredTransfer = serverService.getLatestObsValue(App.getPatientId(), "FAST-Referral Form","PATIENT BEING REFEREED OUT OR TRANSFERRED OUT");
        if(referredTransfer==null){
            referredTransfer="-";
        }

        String referralSite = serverService.getLatestObsValue(App.getPatientId(), "FAST-Referral Form","REFERRING FACILITY NAME");
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

        String reasonTreatmentNotInitiated = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation","TREATMENT NOT INITIATED REASON");
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

        String afbSmearOrderId2 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "FOLLOW-UP MONTH", "2", "ORDER ID");
        if(afbSmearOrderId2==null){
            smearResult2="-";
        }else{
            smearResult2 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", afbSmearOrderId2, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult2==null){
                smearResult2="-";
            }
        }

        String afbSmearOrderId3 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "FOLLOW-UP MONTH", "3", "ORDER ID");
        String smearResult3=null;
        if(afbSmearOrderId3==null){
            smearResult3="-";
        }else{
            smearResult3 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", afbSmearOrderId3, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult3==null){
                smearResult3="-";
            }
        }

        String smearResult5=null;
        String afbSmearOrderId5 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "FOLLOW-UP MONTH", "5", "ORDER ID");
        if(afbSmearOrderId5==null){
            smearResult5="-";
        }else{
            smearResult5 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", afbSmearOrderId5, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult5==null){
                smearResult5="-";
            }
        }

        String smearResult6=null;
        String afbSmearOrderId6 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "FOLLOW-UP MONTH", "6", "ORDER ID");
        if(afbSmearOrderId6==null){
            smearResult6="-";
        }else{
            smearResult6 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", afbSmearOrderId6, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult6==null){
                smearResult6="-";
            }
        }

        String smearResult7=null;
        String afbSmearOrderId7 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "FOLLOW-UP MONTH", "7", "ORDER ID");
        if(afbSmearOrderId7==null){
            smearResult7="-";
        }else{
            smearResult7 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", afbSmearOrderId7, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult7==null){
                smearResult7="-";
            }
        }

        String smearResult8=null;
        String afbSmearOrderId8 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Order", "FOLLOW-UP MONTH", "8", "ORDER ID");
        if(afbSmearOrderId8==null){
            smearResult8="-";
        }else{
            smearResult8 =  serverService.getObsValueByObs(App.getPatientId(), "FAST-AFB Smear Test Result", "ORDER ID", afbSmearOrderId8, "SPUTUM FOR ACID FAST BACILLI");
            if(smearResult8==null){
                smearResult8="-";
            }
        }

        String patientTreatmentPlan = serverService.getLatestObsValue(App.getPatientId(),"FAST-Treatment Followup","TREATMENT PLAN");
        if(patientTreatmentPlan==null) {
            patientTreatmentPlan = "-";
        }

        String treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(),"FAST-End of Followup","TUBERCULOUS TREATMENT OUTCOME");
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

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillComorbiditiesPatientView(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillFastStaffView(){

        Date date = new Date();
        String todayDate = App.getSqlDate(date);

        int countScreening =  serverService.getGwtAppFormCount(todayDate, "fast_screening");
        if(countScreening == -1) countScreening = 0;
        int countPresumptive =  serverService.getEncounterCountForDate(todayDate, "FAST-Presumptive");
        int countPatientLocation =  serverService.getEncounterCountForDate(todayDate, "FAST-Patient Location");
        int countPresumptiveInformation =  serverService.getEncounterCountForDate(todayDate, "FAST-Presumptive Information");
        int countCxrOrder =  serverService.getEncounterCountForDate(todayDate, "FAST-Screening CXR Test Order");
        int countCxrResult =  serverService.getEncounterCountForDate(todayDate, "FAST-Screening CXR Test Result");
        int countSpecimenCollection =  serverService.getEncounterCountForDate(todayDate, "FAST-GXP Specimen Collection");
        int countGxpResultTest =  serverService.getEncounterCountForDate(todayDate, "FAST-GXP Test");
        int countAfbOrder =  serverService.getEncounterCountForDate(todayDate, "FAST-AFB Smear Test Order");
        int countAfbResult =  serverService.getEncounterCountForDate(todayDate, "FAST-AFB Smear Test Result");
        int countTreatmentInitiation =  serverService.getEncounterCountForDate(todayDate, "FAST-Treatment Initiation");
        int countTreatmentFollowup =  serverService.getEncounterCountForDate(todayDate, "FAST-Treatment Followup");
        int countReferal =  serverService.getEncounterCountForDate(todayDate, "FAST-Referral Form");
        int countContactRegistry =  serverService.getEncounterCountForDate(todayDate, "FAST-Contact Registry");

        String[][] dataset = { {getString(R.string.screening_forms), String.valueOf(countScreening), null},
                {getString(R.string.presumptive_forms),String.valueOf(countPresumptive), null},
                {getString(R.string.patient_location_forms),String.valueOf(countPatientLocation), null},
                {getString(R.string.presumptive_information_forms),String.valueOf(countPresumptiveInformation), null},
                {getString(R.string.screnning_cxr_order_forms),String.valueOf(countCxrOrder), null},
                {getString(R.string.screening_cxr_result_forms),String.valueOf(countCxrResult), null},
                {getString(R.string.specimen_collection_forms),String.valueOf(countSpecimenCollection), null},
                {getString(R.string.gxp_result_forms),String.valueOf(countGxpResultTest), null},
                {getString(R.string.afb_order_forms),String.valueOf(countAfbOrder), null},
                {getString(R.string.afb_result_forms),String.valueOf(countAfbResult), null},
                {getString(R.string.treatment_initiation_forms),String.valueOf(countTreatmentInitiation), null},
                {getString(R.string.treatment_followup_forms),String.valueOf(countTreatmentFollowup), null},
                {getString(R.string.referal_forms),String.valueOf(countReferal), null},
                {getString(R.string.contact_registry_forms), String.valueOf(countContactRegistry), null},};

        fillContent(dataset);

    }

    public void fillPetStaffView(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillChildhoodTbStaffView(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

        fillContent(dataset);

    }

    public void fillComorbiditiesStaffView(){

        String[][] dataset = { {"rabbia", "hassan", null},
                {"hadi","hassan", null},
                {"mohammad","hassan", null},
                {"farzana","hassan", null}};

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

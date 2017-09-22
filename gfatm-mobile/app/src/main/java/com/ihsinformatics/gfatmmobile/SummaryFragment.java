package com.ihsinformatics.gfatmmobile;

/**
 * Created by Rabbia on 11/10/2016.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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

        String screeningFacility =  serverService.getEncounterLocation(App.getPatientId(), "FAST-" + Forms.FAST_PRESUMPTIVE_FORM);
        if(screeningFacility == null)
            screeningFacility =  serverService.getEncounterLocation(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_VERBAL_SCREENING);
        if(screeningFacility == null)
            screeningFacility= "N/A";

        String intervention =  serverService.getLatestObsValue(App.getPatientId(), "PET-" + Forms.PET_BASELINE_SCREENING, "INTERVENTION");
        if(intervention == null)
            intervention = "N/A";

        String presumptive = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_PRESUMPTIVE_FORM, "PRESUMPTIVE TUBERCULOSIS");
        if(presumptive == null) {
            presumptive = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION, "CONCLUSION");
            if(presumptive != null) {
                if (presumptive.equalsIgnoreCase("TB PRESUMPTIVE CONFIRMED"))
                    presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION);
            }
        }  else{
            if(presumptive.equalsIgnoreCase("YES"))
                presumptive = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-" + Forms.FAST_PRESUMPTIVE_FORM);
        }
        if(presumptive == null)
            presumptive = "N/A";

        String diagnoseOn = "";

        String disgnosisType = "";

        String xpertResult = "";
        String xpertResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-" + Forms.FAST_GXP_SPECIMEN_COLLECTION_FORM);
        if(xpertResultDate != null){
            String xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-" + Forms.FAST_GXP_SPECIMEN_COLLECTION_FORM, "TEST CONTEXT STATUS", "BASELINE REPEAT", "ORDER ID");
            if(xpertResultOderId == null){
                xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-" + Forms.FAST_GXP_SPECIMEN_COLLECTION_FORM, "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
                if(xpertResultOderId == null)
                    xpertResult = null;
                else
                    xpertResult = serverService.getObsValueByObs(App.getPatientId(), "Childhood TB-" + Forms.FAST_GENEXPERT_RESULT_FORM, "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
            }
        } else {
            xpertResultDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-GXP Specimen Collection");
            if(xpertResultDate != null) {
                String xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Specimen Collection", "TEST CONTEXT STATUS", "BASELINE REPEAT", "ORDER ID");
                if (xpertResultOderId == null) {
                    xpertResultOderId = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Specimen Collection", "TEST CONTEXT STATUS", "BASELINE", "ORDER ID");
                    if (xpertResultOderId == null)
                        xpertResult = null;
                    else
                        xpertResult = serverService.getObsValueByObs(App.getPatientId(), "FAST-GXP Test", "ORDER ID", xpertResultOderId, "GENEXPERT MTB/RIF RESULT");
                }
            }
        }
        if(xpertResult == null  || xpertResult.equals(""))
            xpertResult = "N/A";


        String xrayResult = "";

        String tbType =  serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_INITIATION_FORM, "SITE OF TUBERCULOSIS DISEASE");
        //if(tbType == null)
           // tbType =  serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TREATMENT_INITIATION, "");

        if(tbType == null)
            tbType= "N/A";

        String treatmentInitiationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TREATMENT_INITIATION);
        if(treatmentInitiationDate == null)
            treatmentInitiationDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_INITIATION_FORM);
        if(treatmentInitiationDate == null)
            treatmentInitiationDate= "N/A";

        String registrationNo = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_FOLLOWUP_FORM, "TB REGISTRATION NUMBER");
        if(registrationNo == null)
            registrationNo = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP, "TB REGISTRATION NUMBER");
        if(registrationNo == null)
            registrationNo= "N/A";

        String infectionTreatmentInitiationDate =  serverService.getLatestEncounterDateTime(App.getPatientId(), "PET-" + Forms.PET_TREATMENT_INITIATION);
        if(infectionTreatmentInitiationDate == null)
            infectionTreatmentInitiationDate = "N/A";

        String infectionTreatmentRegimen =  serverService.getLatestObsValue(App.getPatientId(), "PET-" + Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
        if(infectionTreatmentRegimen == null)
            infectionTreatmentRegimen = "N/A";

        String treatmentFacility =  serverService.getEncounterLocation(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_INITIATION_FORM);
        if(treatmentFacility == null)
            treatmentFacility =  serverService.getEncounterLocation(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TREATMENT_INITIATION);
        if(treatmentFacility == null)
            treatmentFacility= "N/A";

        String nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_FOLLOWUP_FORM, "RETURN VISIT DATE");
        if (nextFollowupDate == null) {
            nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_INITIATION_FORM, "RETURN VISIT DATE");
            if(nextFollowupDate == null){
                nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP, "RETURN VISIT DATE");
                if(nextFollowupDate == null)
                    nextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TREATMENT_INITIATION, "RETURN VISIT DATE");
            }
        }
        if(nextFollowupDate == null)
            nextFollowupDate = "N/A";

        String lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-AFB Smear Test Result");
        if(lastSmearDate == null)
            lastSmearDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-AFB Smear Test Result");
        if(lastSmearDate == null)
            lastSmearDate= "N/A";

        String lastSmearResult = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-AFB Smear Test Result", "SPUTUM FOR ACID FAST BACILLI");
        if(lastSmearResult == null)
            lastSmearResult = serverService.getLatestObsValue(App.getPatientId(), "FAST-AFB Smear Test Result", "SPUTUM FOR ACID FAST BACILLI");
        if(lastSmearResult == null)
            lastSmearResult= "N/A";

        String lastXrayDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_CXR_SCREENING_TEST);
        if(lastXrayDate == null)
            lastXrayDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "FAST-" + Forms.FAST_SCREENING_CHEST_XRAY_ORDER_AND_RESULT_FORM);
        if(lastXrayDate == null)
            lastXrayDate= "N/A";

        String lastXrayResult = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-CXR Screening Test Result","CHEST X-RAY SCORE");
        if(lastXrayResult == null) {
            lastXrayResult = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-CXR Screening Test Result", "RADIOLOGICAL DIAGNOSIS");
            if(lastXrayResult == null)
                lastXrayResult = serverService.getLatestObsValue(App.getPatientId(), "FAST-Screening CXR Test Result", "CHEST X-RAY SCORE");
                if(lastXrayResult == null)
                    lastXrayResult = serverService.getLatestObsValue(App.getPatientId(), "FAST-Screening CXR Test Result", "RADIOLOGICAL DIAGNOSIS");
        }
        if(lastXrayResult == null)
            lastXrayResult= "N/A";

        String treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_TB_TREATMENT_FOLLOWUP,"TREATMENT PLAN");
        if(treatmentPlan == null)
            treatmentPlan = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_FOLLOWUP_FORM,"TREATMENT PLAN");
        if(treatmentPlan == null)
            treatmentPlan= "N/A";

        String akuadsScreeningDate = serverService.getLatestEncounterDateTime(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM);
        if(akuadsScreeningDate == null)
            akuadsScreeningDate = "N/A";

        String akuadsScore = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, "AKUADS SCORE");
        if(akuadsScore == null)
            akuadsScore = "N/A";

        String depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM, "RETURN VISIT DATE");
        if(depressionNextFollowupDate == null) {
            depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-" + Forms.COMORBIDITIES_MENTAL_HEALTH_SCREENING_FORM, "RETURN VISIT DATE");
            if(depressionNextFollowupDate == null){
                depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_FOLLOWUP_FORM, "RETURN VISIT DATE");
                if(depressionNextFollowupDate == null)
                    depressionNextFollowupDate = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_TREATMENT_INITIATION_FORM, "RETURN VISIT DATE");
            }
        }
        if(depressionNextFollowupDate == null)
            depressionNextFollowupDate = "N/A";

        String diabetesStatus =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_HbA1C_FORM, "DIABETES MELLITUS");
        if (diabetesStatus == null)
            diabetesStatus =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_HbA1C_FORM, "PREVIOUSLY DIAGNOSED DIABETES");
        if(diabetesStatus == null)
            diabetesStatus = "N/A";

        String diabetesFollowupDate =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_DIABETES_TREATMENT_INITIATION, "RETURN VISIT DATE");
        if (diabetesFollowupDate == null)
            diabetesFollowupDate =  serverService.getLatestObsValue(App.getPatientId(), "Comorbidities-"+ Forms.COMORBIDITIES_DIABETES_TREATMENT_FOLLOWUP_FORM, "RETURN VISIT DATE");
        if(diabetesFollowupDate == null)
            diabetesFollowupDate = "N/A";

        String treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(), "Childhood TB-" + Forms.CHILDHOODTB_END_OF_FOLLOWUP,"TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutcome == null)
            treatmentOutcome = serverService.getLatestObsValue(App.getPatientId(), "FAST-" + Forms.FAST_END_OF_FOLLOWUP_FORM,"TUBERCULOUS TREATMENT OUTCOME");
        if(treatmentOutcome == null)
            treatmentOutcome= "N/A";

        String[][] dataset = { {getString(R.string.patient_id), App.getPatient().getPatientId()},
                {getString(R.string.external_id),App.getPatient().getExternalId()},
                {getString(R.string.screening_facility),screeningFacility},
                {getString(R.string.pet_sci_intervention),intervention},
                {getString(R.string.presumptive_date),presumptive},
                {getString(R.string.diagnosed_on),diagnoseOn},
                {getString(R.string.diagnosed_type),disgnosisType},
                {getString(R.string.xpert_result),xpertResult},
                {getString(R.string.xray_result),xrayResult},
                {getString(R.string.tb_type),tbType},
                {getString(R.string.tb_initiation_date),treatmentInitiationDate},
                {getString(R.string.tb_registration_no),registrationNo},
                {getString(R.string.infection_tb_initiation_date),infectionTreatmentInitiationDate},
                {getString(R.string.tb_regimen),infectionTreatmentRegimen},
                {getString(R.string.treatment_facility),treatmentFacility},
                {getString(R.string.next_followup_tb_date),nextFollowupDate},
                {getString(R.string.date_last_smear_done),lastSmearDate},
                {getString(R.string.result_last_smear),lastSmearResult},
                {getString(R.string.date_last_xray_done),lastXrayDate},
                {getString(R.string.result_last_xray),lastXrayResult},
                {getString(R.string.treatment_plan),treatmentPlan},
                {getString(R.string.akuads_screening_date),akuadsScreeningDate},
                {getString(R.string.akuads_score),akuadsScore},
                {getString(R.string.depression_followup_date),depressionNextFollowupDate},
                {getString(R.string.diabetes_status),diabetesStatus},
                {getString(R.string.diabetes_followup_date),diabetesFollowupDate},
                {getString(R.string.final_treatment_outcome),treatmentOutcome}};

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

        String[][] dataset = { {"rabbia", "hassan"},
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
            question.setText(String.valueOf(dataset[j][0]));
            question.setTextSize(getResources().getDimension(R.dimen.medium));
            question.setTextColor(color);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.weight = 1;
            question.setLayoutParams(p);
            linearLayout.addView(question);
            question.setPadding(0, 10, 20, 10);

            TextView answer = new TextView(context);
            answer.setText(String.valueOf(String.valueOf(dataset[j][1])));
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

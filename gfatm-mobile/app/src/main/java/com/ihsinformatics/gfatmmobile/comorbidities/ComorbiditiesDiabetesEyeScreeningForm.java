package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 06-Feb-17.
 */

public class ComorbiditiesDiabetesEyeScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledSpinner diabetesEyeScreeningMonthOfTreatment;
    //TitledRadioGroup diabetesEyeScreeningEyeStatus;
    TitledRadioGroup diabetesEyeScreeningRightEyeDiagnosed;
    TitledRadioGroup diabetesEyeScreeningLeftEyeDiagnosed;
    //TitledRadioGroup diabetesEyeScreeningEvidenceEye;
    TitledCheckBoxes diabetesEyeScreeningEvidenceEye;
    TitledEditText diabetesEyeScreeningEvidenceEyeOther;
    TitledRadioGroup diabetesEyeScreeningDiabetecRetinopathy;
    TitledRadioGroup diabetesEyeScreeningMildDiabetecRetinopathy;
    TitledRadioGroup diabetesEyeScreeningModerateDiabetecRetinopathy;
    TitledRadioGroup diabetesEyeScreeningSevereDiabetecRetinopathy;
    TitledRadioGroup diabetesEyeScreeningProliferativeDiabetecRetinopathy;
    TitledRadioGroup diabetesEyeScreeningDiabeticMacularEdema;
    //TitledRadioGroup diabeteEyeScreeningClinicalDME;
    TitledEditText diabetesEyeScreeningOther;
    TitledRadioGroup diabetesEyeScreeningVisionloss;
    TitledRadioGroup diabetesEyeScreeningRecommendations;

    /**
     * CHANGE PAGE_COUNT and FORM_NAME Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.COMORBIDITIES_DIABETES_EYE_SCREENING_FORM;
        FORM =  Forms.comorbidities_diabetesTreatmentFollowupForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDiabetesEyeScreeningForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = PAGE_COUNT - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        diabetesEyeScreeningMonthOfTreatment = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_urinedr_month_of_treatment), getResources().getStringArray(R.array.comorbidities_followup_month), "0", App.HORIZONTAL);
        //diabetesEyeScreeningEyeStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_eye_status), getResources().getStringArray(R.array.comorbidities_eye_screening_eye_status_options), "", App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningRightEyeDiagnosed = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_right_eye_diagnosed), getResources().getStringArray(R.array.comorbidities_eye_screening_eye_diagnosed_options), "", App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningLeftEyeDiagnosed = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_left_eye_diagnosed), getResources().getStringArray(R.array.comorbidities_eye_screening_eye_diagnosed_options), "", App.VERTICAL, App.VERTICAL);
        //diabetesEyeScreeningEvidenceEye = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_evidence_eye), getResources().getStringArray(R.array.comorbidities_eye_screening_evidence_eye_options), "", App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningEvidenceEye = new TitledCheckBoxes(context, null, getResources().getString(R.string.comorbidities_eye_screening_evidence_eye), getResources().getStringArray(R.array.comorbidities_eye_screening_evidence_eye_options), new Boolean[]{false, false, false, false}, App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningEvidenceEyeOther  = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diabetesEyeScreeningDiabetecRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningMildDiabetecRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_mild_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_eye_screening_diabetic_retinopathy_options), getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningModerateDiabetecRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_moderate_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_eye_screening_diabetic_retinopathy_options), getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningSevereDiabetecRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_severe_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_eye_screening_diabetic_retinopathy_options), getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningProliferativeDiabetecRetinopathy = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_proliferative_diabetic_retinopathy), getResources().getStringArray(R.array.comorbidities_eye_screening_diabetic_retinopathy_options), getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningDiabeticMacularEdema = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_diabetic_macular_edema), getResources().getStringArray(R.array.comorbidities_eye_screening_diabetic_retinopathy_options), getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none), App.VERTICAL, App.VERTICAL);
        //diabeteEyeScreeningClinicalDME = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_clinical_dme), getResources().getStringArray(R.array.comorbidities_eye_screening_diabetic_retinopathy_options), getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningOther  = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_eye_screening_other), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        diabetesEyeScreeningVisionloss = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_visionloss), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        diabetesEyeScreeningRecommendations = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_eye_screening_recommendations), getResources().getStringArray(R.array.comorbidities_eye_screening_recommendations_options), getResources().getString(R.string.comorbidities_eye_screening_recommendations_options_management), App.VERTICAL, App.VERTICAL);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), diabetesEyeScreeningMonthOfTreatment.getSpinner(), /*diabetesEyeScreeningEyeStatus.getRadioGroup(),*/ diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup(), diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup(), diabetesEyeScreeningEvidenceEye, /*diabetesEyeScreeningEvidenceEye.getRadioGroup(),*/
                diabetesEyeScreeningEvidenceEyeOther.getEditText(), diabetesEyeScreeningDiabetecRetinopathy.getRadioGroup(), diabetesEyeScreeningMildDiabetecRetinopathy.getRadioGroup(), diabetesEyeScreeningModerateDiabetecRetinopathy.getRadioGroup(),
                diabetesEyeScreeningSevereDiabetecRetinopathy.getRadioGroup(), diabetesEyeScreeningProliferativeDiabetecRetinopathy.getRadioGroup(), diabetesEyeScreeningDiabeticMacularEdema.getRadioGroup(),
                /*diabeteEyeScreeningClinicalDME.getRadioGroup(),*/ diabetesEyeScreeningOther.getEditText(), diabetesEyeScreeningVisionloss.getRadioGroup(), diabetesEyeScreeningRecommendations.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, diabetesEyeScreeningMonthOfTreatment, /*diabetesEyeScreeningEyeStatus,*/ diabetesEyeScreeningRightEyeDiagnosed, diabetesEyeScreeningLeftEyeDiagnosed, diabetesEyeScreeningEvidenceEye, diabetesEyeScreeningEvidenceEyeOther,
                        diabetesEyeScreeningDiabetecRetinopathy, diabetesEyeScreeningMildDiabetecRetinopathy, diabetesEyeScreeningModerateDiabetecRetinopathy,
                        diabetesEyeScreeningSevereDiabetecRetinopathy, diabetesEyeScreeningProliferativeDiabetecRetinopathy, diabetesEyeScreeningDiabeticMacularEdema,
                        /*diabeteEyeScreeningClinicalDME,*/ diabetesEyeScreeningOther, diabetesEyeScreeningVisionloss, diabetesEyeScreeningRecommendations}};

        formDate.getButton().setOnClickListener(this);
        //diabetesEyeScreeningEyeStatus.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup().setOnCheckedChangeListener(this);
        //diabetesEyeScreeningEvidenceEye.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningDiabetecRetinopathy.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningMildDiabetecRetinopathy.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningModerateDiabetecRetinopathy.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningSevereDiabetecRetinopathy.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningProliferativeDiabetecRetinopathy.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningDiabeticMacularEdema.getRadioGroup().setOnCheckedChangeListener(this);
        //diabeteEyeScreeningClinicalDME.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningVisionloss.getRadioGroup().setOnCheckedChangeListener(this);
        diabetesEyeScreeningRecommendations.getRadioGroup().setOnCheckedChangeListener(this);

        for (CheckBox cb : diabetesEyeScreeningEvidenceEye.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            return false;
        }

        return true;
    }

    @Override
    public boolean submit() {

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(diabetesEyeScreeningMonthOfTreatment)});
        observations.add(new String[]{"RIGHT EYE EXAMINATION", App.get(diabetesEyeScreeningRightEyeDiagnosed).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) ? "YES" :
                (App.get(diabetesEyeScreeningRightEyeDiagnosed).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) ? "EYE DEFORMITY" : "PROSTHETIC EYE")});
        observations.add(new String[]{"LEFT EYE EXAMINATION", App.get(diabetesEyeScreeningLeftEyeDiagnosed).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) ? "YES" :
                (App.get(diabetesEyeScreeningLeftEyeDiagnosed).equals(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) ? "EYE DEFORMITY" : "PROSTHETIC EYE")});

        if(App.get(diabetesEyeScreeningRightEyeDiagnosed).equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes)) ||
                App.get(diabetesEyeScreeningLeftEyeDiagnosed).equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes))) {
            String diabetesEyeScreeningEvidenceEyeString = "";
            for (CheckBox cb : diabetesEyeScreeningEvidenceEye.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_cataract)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "CATARACT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_papillitis)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "OPTIC PAPILLITIS" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_ischemic)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "ISCHAEMIC OPTIC NEUROPATHY" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_other)))
                    diabetesEyeScreeningEvidenceEyeString = diabetesEyeScreeningEvidenceEyeString + "OTHER EYE PROBLEMS" + " ; ";
            }
            observations.add(new String[]{"EYE EXAMINATION FINDINGS", diabetesEyeScreeningEvidenceEyeString});

            observations.add(new String[]{"OTHER EYE PROBLEMS", App.get(diabetesEyeScreeningEvidenceEyeOther)});
            observations.add(new String[]{"DIABETIC RETINOPATHY", App.get(diabetesEyeScreeningDiabetecRetinopathy).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

            final String diabetesEyeScreeningMildDiabetecRetinopathyString = App.get(diabetesEyeScreeningMildDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(diabetesEyeScreeningMildDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(diabetesEyeScreeningMildDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"MILD NON PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningMildDiabetecRetinopathyString});

            final String diabetesEyeScreeningModerateDiabetecRetinopathyString = App.get(diabetesEyeScreeningModerateDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(diabetesEyeScreeningModerateDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(diabetesEyeScreeningModerateDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"MODERATE NON PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningModerateDiabetecRetinopathyString});

            final String diabetesEyeScreeningSevereDiabetecRetinopathyString = App.get(diabetesEyeScreeningSevereDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(diabetesEyeScreeningSevereDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(diabetesEyeScreeningSevereDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"SEVERE NON PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningSevereDiabetecRetinopathyString});

            final String diabetesEyeScreeningProliferativeDiabetecRetinopathyString = App.get(diabetesEyeScreeningProliferativeDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(diabetesEyeScreeningProliferativeDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(diabetesEyeScreeningProliferativeDiabetecRetinopathy).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"PROLIFERATIVE DIABETIC RETINOPATHY", diabetesEyeScreeningProliferativeDiabetecRetinopathyString});

            final String diabetesEyeScreeningDiabeticMacularEdemaString = App.get(diabetesEyeScreeningDiabeticMacularEdema).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_none)) ? "NONE" :
                    (App.get(diabetesEyeScreeningDiabeticMacularEdema).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)) ? "IN RIGHT EYE" :
                            (App.get(diabetesEyeScreeningDiabeticMacularEdema).equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)) ? "IN LEFT EYE" : "IN BOTH EYES"));
            observations.add(new String[]{"DIABETIC MACULAR EDEMA", diabetesEyeScreeningDiabeticMacularEdemaString});

            observations.add(new String[]{"VISION LOSS", App.get(diabetesEyeScreeningVisionloss).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        }

        observations.add(new String[]{"OTHER FINDINGS", App.get(diabetesEyeScreeningOther)});
        observations.add(new String[]{"DIABETES RECOMMENDATAION", App.get(diabetesEyeScreeningRecommendations).equals(getResources().getString(R.string.comorbidities_eye_screening_recommendations_options_referral)) ? "PATIENT REFERRED" : "DIABETES MANAGEMENT"});

        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                String result = "";
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {
                    resetViews();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_submitted));
                    Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                    alertDialog.setIcon(submitIcon);
                    int color = App.getColor(context, R.attr.colorAccent);
                    DrawableCompat.setTint(submitIcon, color);
                    alertDialog.setTitle(getResources().getString(R.string.title_completed));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    String message = getResources().getString(R.string.insert_error) + "\n\n (" + result + ")";
                    alertDialog.setMessage(message);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }


            }
        };
        submissionFormTask.execute("");

        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int encounterId) {

    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Boolean flag = false;
        for (CheckBox cb : diabetesEyeScreeningEvidenceEye.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_evidence_eye_options_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            diabetesEyeScreeningEvidenceEyeOther.setVisibility(View.VISIBLE);
        else
            diabetesEyeScreeningEvidenceEyeOther.setVisibility(View.GONE);

    }

    @Override
    public void resetViews() {
        super.resetViews();

        //displayReasonForNonCompliance();
        //displayIfOther();
        diabetesEyeScreeningEvidenceEyeOther.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        /*if(radioGroup == diabetesFollowupHasPrescribedMedication.getRadioGroup()) {
            displayReasonForNonCompliance();
            displayIfOther();
        }*/

        if(radioGroup == diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup()) {
            hideIfEyesNotExamined();
        }

        if(radioGroup == diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup()) {
            hideIfEyesNotExamined();
        }

    }

    void hideIfEyesNotExamined () {
        if((diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) || diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_prosthetic)))
                && (diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) || diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_prosthetic)))) {

            diabetesEyeScreeningEvidenceEye.setVisibility(View.GONE);
            if(diabetesEyeScreeningEvidenceEyeOther.getVisibility()==View.VISIBLE) {
                diabetesEyeScreeningEvidenceEyeOther.setVisibility(View.GONE);
            }
            diabetesEyeScreeningDiabetecRetinopathy.setVisibility(View.GONE);
            diabetesEyeScreeningMildDiabetecRetinopathy.setVisibility(View.GONE);
            diabetesEyeScreeningModerateDiabetecRetinopathy.setVisibility(View.GONE);
            diabetesEyeScreeningSevereDiabetecRetinopathy.setVisibility(View.GONE);
            diabetesEyeScreeningProliferativeDiabetecRetinopathy.setVisibility(View.GONE);
            diabetesEyeScreeningDiabeticMacularEdema.setVisibility(View.GONE);
            //diabeteEyeScreeningClinicalDME.setVisibility(View.GONE);
            diabetesEyeScreeningVisionloss.setVisibility(View.GONE);
        }
        else {
            diabetesEyeScreeningEvidenceEye.setVisibility(View.VISIBLE);
            diabetesEyeScreeningDiabetecRetinopathy.setVisibility(View.VISIBLE);
            diabetesEyeScreeningMildDiabetecRetinopathy.setVisibility(View.VISIBLE);
            diabetesEyeScreeningModerateDiabetecRetinopathy.setVisibility(View.VISIBLE);
            diabetesEyeScreeningSevereDiabetecRetinopathy.setVisibility(View.VISIBLE);
            diabetesEyeScreeningProliferativeDiabetecRetinopathy.setVisibility(View.VISIBLE);
            diabetesEyeScreeningDiabeticMacularEdema.setVisibility(View.VISIBLE);
            //diabeteEyeScreeningClinicalDME.setVisibility(View.VISIBLE);
            diabetesEyeScreeningVisionloss.setVisibility(View.VISIBLE);
        }

        if(((diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes))))
                && (diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) || diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_prosthetic))))
        {
            ArrayList<RadioButton> rbs = diabetesEyeScreeningMildDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs1 = diabetesEyeScreeningModerateDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs1) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs2 = diabetesEyeScreeningSevereDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs2) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs3 = diabetesEyeScreeningProliferativeDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs3) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs4 = diabetesEyeScreeningDiabeticMacularEdema.getRadioGroup().getButtons();

            for (RadioButton rb : rbs4) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            /*ArrayList<RadioButton> rbs5 = diabeteEyeScreeningClinicalDME.getRadioGroup().getButtons();

            for (RadioButton rb : rbs5) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_left)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }*/

        }
        /*else
        {
            ArrayList<RadioButton> rbs = diabetesEyeScreeningMildDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs) {
                    rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs1 = diabetesEyeScreeningModerateDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs1) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs2 = diabetesEyeScreeningSevereDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs2) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs3 = diabetesEyeScreeningProliferativeDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs3) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs4 = diabetesEyeScreeningDiabeticMacularEdema.getRadioGroup().getButtons();

            for (RadioButton rb : rbs4) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs5 = diabeteEyeScreeningClinicalDME.getRadioGroup().getButtons();

            for (RadioButton rb : rbs5) {
                rb.setVisibility(View.VISIBLE);
            }
        }*/

        else if((diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_deformity)) || diabetesEyeScreeningRightEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_no_prosthetic)))
                    && (diabetesEyeScreeningLeftEyeDiagnosed.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_eye_screening_eye_diagnosed_options_yes))))
        {
            ArrayList<RadioButton> rbs = diabetesEyeScreeningMildDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs1 = diabetesEyeScreeningModerateDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs1) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs2 = diabetesEyeScreeningSevereDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs2) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs3 = diabetesEyeScreeningProliferativeDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs3) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            ArrayList<RadioButton> rbs4 = diabetesEyeScreeningDiabeticMacularEdema.getRadioGroup().getButtons();

            for (RadioButton rb : rbs4) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }

            /*ArrayList<RadioButton> rbs5 = diabeteEyeScreeningClinicalDME.getRadioGroup().getButtons();

            for (RadioButton rb : rbs5) {
                if (rb.getText().equals(getResources().getString(R.string.comorbidities_eye_screening_diabetic_retinopathy_options_right)))
                    rb.setVisibility(View.GONE);
                else {
                    rb.setChecked(false);
                    rb.setVisibility(View.VISIBLE);
                }
            }*/

        }
        else
        {
            ArrayList<RadioButton> rbs = diabetesEyeScreeningMildDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs1 = diabetesEyeScreeningModerateDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs1) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs2 = diabetesEyeScreeningSevereDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs2) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs3 = diabetesEyeScreeningProliferativeDiabetecRetinopathy.getRadioGroup().getButtons();

            for (RadioButton rb : rbs3) {
                rb.setVisibility(View.VISIBLE);
            }

            ArrayList<RadioButton> rbs4 = diabetesEyeScreeningDiabeticMacularEdema.getRadioGroup().getButtons();

            for (RadioButton rb : rbs4) {
                rb.setVisibility(View.VISIBLE);
            }

            /*ArrayList<RadioButton> rbs5 = diabeteEyeScreeningClinicalDME.getRadioGroup().getButtons();

            for (RadioButton rb : rbs5) {
                rb.setVisibility(View.VISIBLE);
            }*/
        }

    }

    /*void displayReasonForNonCompliance() {
        if (diabetesFollowupHasPrescribedMedication.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no))) {
            diabetesFollowupReasonsForNonCompliance.setVisibility(View.VISIBLE);
        } else {
            diabetesFollowupReasonsForNonCompliance.setVisibility(View.GONE);
        }
    }

    void displayIfOther() {

        String text = diabetesFollowupReasonsForNonCompliance.getSpinner().getSelectedItem().toString();
        if (diabetesFollowupHasPrescribedMedication.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.no)) && text.equalsIgnoreCase(getResources().getString(R.string.comorbidities_diabetes_followup_non_compliance_options_other))) {
            diabetesFollowupIfOther.setVisibility(View.VISIBLE);
        } else {
            diabetesFollowupIfOther.setVisibility(View.GONE);
        }
    }*/

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Object instantiateItem(View container, int position) {

            ViewGroup viewGroup = groups.get(position);
            ((ViewPager) container).addView(viewGroup, 0);

            return viewGroup;
        }

        @Override
        public void destroyItem(View container, int position, Object obj) {
            ((ViewPager) container).removeView((View) obj);
        }

        @Override
        public boolean isViewFromObject(View container, Object obj) {
            return container == obj;
        }

    }
}





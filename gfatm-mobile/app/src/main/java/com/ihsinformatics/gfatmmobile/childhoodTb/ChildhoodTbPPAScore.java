package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbPPAScore extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;


    Snackbar snackbar;
    ScrollView scrollView;

    TitledRadioGroup ageScore;
    TitledRadioGroup closeContactStatus;
    TitledRadioGroup closeContactScore;
    TitledRadioGroup pemSam;
    TitledRadioGroup pemSamScore;
    TitledRadioGroup historyMeaslesCough;
    TitledRadioGroup historyMeaslesCoughScore;
    TitledRadioGroup hivStatus;
    TitledRadioGroup hivScore;
    TitledRadioGroup immunoCompromisedStatus;
    TitledRadioGroup immunoCompromisedScore;
    TitledRadioGroup clinicalManifestationStatus;
    TitledRadioGroup clinicalManifestationScore;
    TitledRadioGroup radioDiagnostiImagingStatus;
    TitledRadioGroup radioDiagnosticImagingScore;
    TitledRadioGroup tuberculinSkinPpdTestResult;
    TitledRadioGroup tuberculinSkinPpdTestResultScore;
    TitledRadioGroup gxpTestResult;
    TitledRadioGroup gxpTestResultScore;
    TitledEditText ppaScore;
    TitledRadioGroup ppaScoreInterpretation;



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
        FORM_NAME = Forms.CHILDHOODTB_PPA_SCORE;
        FORM = Forms.childhoodTb_ppa_score;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = PAGE_COUNT - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        ageScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_age_score),getResources().getStringArray(R.array.ctb_0_to_1_list),null,App.HORIZONTAL,App.VERTICAL,true);
        closeContactStatus = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_close_contact_status),getResources().getStringArray(R.array.ctb_close_contact_status_list),null,App.VERTICAL,App.VERTICAL,true);
        closeContactScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_close_contact_score),getResources().getStringArray(R.array.ctb_0_to_3_list),null,App.HORIZONTAL,App.VERTICAL,true);
        pemSam = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_pem_sam),getResources().getStringArray(R.array.ctb_pem_sam_list),null,App.VERTICAL,App.VERTICAL,true);
        pemSamScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_pem_sam_score),getResources().getStringArray(R.array.ctb_0_to_2_list),null,App.HORIZONTAL,App.VERTICAL,true);
        historyMeaslesCough = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_history_of_measles),getResources().getStringArray(R.array.ctb_history_measles_whooping_cough_list),null,App.HORIZONTAL,App.VERTICAL,true);
        historyMeaslesCoughScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_measles_cough_score),getResources().getStringArray(R.array.ctb_0_to_2_list),null,App.HORIZONTAL,App.VERTICAL,true);
        hivStatus = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_hiv_status),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);
        hivScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_hiv_score),getResources().getStringArray(R.array.ctb_hiv_score_list),null,App.HORIZONTAL,App.VERTICAL,true);
        immunoCompromisedStatus = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_immuno_compromised_status),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL,true);
        immunoCompromisedScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_immuno_compromised_score),getResources().getStringArray(R.array.ctb_0_to_1_list),null,App.HORIZONTAL,App.VERTICAL,true);
        clinicalManifestationStatus = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_clinical_manifestation_status),getResources().getStringArray(R.array.ctb_clinical_manifestation_status_list),null,App.VERTICAL,App.VERTICAL,true);
        clinicalManifestationScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_clinical_manifestation_score),getResources().getStringArray(R.array.ctb_clinical_manifestation_score_list),null,App.HORIZONTAL,App.VERTICAL,true);
        radioDiagnostiImagingStatus = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_radio_diagnostic_imaging_status),getResources().getStringArray(R.array.ctb_radio_diagnostic_image_status),null,App.VERTICAL,App.VERTICAL,true);
        radioDiagnosticImagingScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_radio_diagnostic_score),getResources().getStringArray(R.array.ctb_0_to_3_list),null,App.HORIZONTAL,App.VERTICAL,true);
        tuberculinSkinPpdTestResult = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_tuberculin_skin_ppd_result),getResources().getStringArray(R.array.ctb_tuberculin_skin_test_list),null,App.HORIZONTAL,App.VERTICAL,true);
        tuberculinSkinPpdTestResultScore = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_tuberculin_skin_ppd_result_score),getResources().getStringArray(R.array.ctb_tuberculin_ppd_score_list),null,App.HORIZONTAL,App.VERTICAL,true);

        gxpTestResult = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_gxp_test_result),getResources().getStringArray(R.array.ctb_gxp_test_result_list),null,App.VERTICAL,App.VERTICAL,true);
        gxpTestResultScore= new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_gxp_test_result_score),getResources().getStringArray(R.array.ctb_gxp_test_score),null,App.HORIZONTAL,App.VERTICAL,true);

        ppaScore = new TitledEditText(context,null,getResources().getString(R.string.ctb_ppa_score),"","",2,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,true);
        ppaScoreInterpretation = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_ppa_score_interpretation),getResources().getStringArray(R.array.ctb_gxp_test_score),null,App.HORIZONTAL,App.VERTICAL,true);






        ppaScore = new TitledEditText(context, null, getResources().getString(R.string.ctb_ppa_score), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        views = new View[]{formDate.getButton(),ppaScore.getEditText(),
                ageScore.getRadioGroup(),
                closeContactStatus.getRadioGroup(),
                closeContactScore.getRadioGroup(),
                pemSam.getRadioGroup(),
                pemSamScore.getRadioGroup(),
                historyMeaslesCough.getRadioGroup(),
                historyMeaslesCoughScore.getRadioGroup(),
                hivStatus.getRadioGroup(),
                hivScore.getRadioGroup(),
                immunoCompromisedStatus.getRadioGroup(),
                immunoCompromisedScore.getRadioGroup(),
                clinicalManifestationStatus.getRadioGroup(),
                clinicalManifestationScore.getRadioGroup(),
                radioDiagnostiImagingStatus.getRadioGroup(),
                radioDiagnosticImagingScore.getRadioGroup(),
                tuberculinSkinPpdTestResult.getRadioGroup(),
                tuberculinSkinPpdTestResultScore.getRadioGroup(),
                gxpTestResult.getRadioGroup(),
                gxpTestResultScore.getRadioGroup(),
                ppaScoreInterpretation.getRadioGroup()
        };
        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,  ageScore,
                        closeContactStatus,
                        closeContactScore,
                        pemSam,
                        pemSamScore,
                        historyMeaslesCough,
                        historyMeaslesCoughScore,
                        hivStatus,
                        hivScore,
                        immunoCompromisedStatus,
                        immunoCompromisedScore,
                        clinicalManifestationStatus,
                        clinicalManifestationScore,
                        radioDiagnostiImagingStatus,
                        radioDiagnosticImagingScore,
                        tuberculinSkinPpdTestResult,
                        tuberculinSkinPpdTestResultScore,
                        gxpTestResult,
                        gxpTestResultScore,
                        ppaScore,
                        ppaScoreInterpretation}};

        formDate.getButton().setOnClickListener(this);
        ageScore.getRadioGroup().setOnCheckedChangeListener(this);
        closeContactStatus.getRadioGroup().setOnCheckedChangeListener(this);
        closeContactScore.getRadioGroup().setOnCheckedChangeListener(this);
        pemSam.getRadioGroup().setOnCheckedChangeListener(this);
        pemSamScore.getRadioGroup().setOnCheckedChangeListener(this);
        historyMeaslesCough.getRadioGroup().setOnCheckedChangeListener(this);
        historyMeaslesCoughScore.getRadioGroup().setOnCheckedChangeListener(this);
        hivStatus.getRadioGroup().setOnCheckedChangeListener(this);
        hivScore.getRadioGroup().setOnCheckedChangeListener(this);
        immunoCompromisedStatus.getRadioGroup().setOnCheckedChangeListener(this);
        immunoCompromisedScore.getRadioGroup().setOnCheckedChangeListener(this);
        clinicalManifestationStatus.getRadioGroup().setOnCheckedChangeListener(this);
        clinicalManifestationScore.getRadioGroup().setOnCheckedChangeListener(this);
        radioDiagnostiImagingStatus.getRadioGroup().setOnCheckedChangeListener(this);
        radioDiagnosticImagingScore.getRadioGroup().setOnCheckedChangeListener(this);
        tuberculinSkinPpdTestResult.getRadioGroup().setOnCheckedChangeListener(this);
        tuberculinSkinPpdTestResultScore.getRadioGroup().setOnCheckedChangeListener(this);
        gxpTestResult.getRadioGroup().setOnCheckedChangeListener(this);
        gxpTestResultScore.getRadioGroup().setOnCheckedChangeListener(this);
        ppaScoreInterpretation.getRadioGroup().setOnCheckedChangeListener(this);

        ppaScore.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 15) {
                        ppaScore.getEditText().setError("Error, enter value between 0-15");
                    } else {
                        ppaScore.getEditText().setError(null);
                    }
                }
            }
        });

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            }  else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        boolean error = false;
        if (App.get(ppaScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            ppaScore.getEditText().setError(getString(R.string.empty_field));
            ppaScore.getEditText().requestFocus();
            error = true;
        }
        else if(!App.get(ppaScore).isEmpty()){
            int ppaScoreInt = Integer.parseInt(App.get(ppaScore));
            if(ppaScoreInt<0 || ppaScoreInt>15){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                ppaScore.getEditText().setError(getString(R.string.ctb_ppa_score_validate));
                ppaScore.getEditText().requestFocus();
                error = true;
            }
        }
        if(closeContactStatus.getVisibility()==View.VISIBLE && App.get(closeContactStatus).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            closeContactStatus.getQuestionView().setError(getString(R.string.empty_field));
            closeContactStatus.getRadioGroup().requestFocus();
            error = true;
        }
        if(closeContactScore.getVisibility()==View.VISIBLE && App.get(closeContactScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            closeContactScore.getQuestionView().setError(getString(R.string.empty_field));
            closeContactScore.getRadioGroup().requestFocus();
            error = true;
        }
        if(pemSam.getVisibility()==View.VISIBLE && App.get(pemSam).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            pemSam.getQuestionView().setError(getString(R.string.empty_field));
            pemSam.getRadioGroup().requestFocus();
            error = true;
        }
        if(pemSamScore.getVisibility()==View.VISIBLE && App.get(pemSamScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            pemSamScore.getQuestionView().setError(getString(R.string.empty_field));
            pemSamScore.getRadioGroup().requestFocus();
            error = true;
        }
        if(historyMeaslesCough.getVisibility()==View.VISIBLE && App.get(historyMeaslesCough).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            historyMeaslesCough.getQuestionView().setError(getString(R.string.empty_field));
            historyMeaslesCough.getRadioGroup().requestFocus();
            error = true;
        }
        if(historyMeaslesCoughScore.getVisibility()==View.VISIBLE && App.get(historyMeaslesCoughScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            historyMeaslesCoughScore.getQuestionView().setError(getString(R.string.empty_field));
            historyMeaslesCoughScore.getRadioGroup().requestFocus();
            error = true;
        }

        if(hivStatus.getVisibility()==View.VISIBLE && App.get(hivStatus).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hivStatus.getQuestionView().setError(getString(R.string.empty_field));
            hivStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if(hivScore.getVisibility()==View.VISIBLE && App.get(hivScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hivScore.getQuestionView().setError(getString(R.string.empty_field));
            hivScore.getRadioGroup().requestFocus();
            error = true;
        }

        if(immunoCompromisedStatus.getVisibility()==View.VISIBLE && App.get(immunoCompromisedStatus).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            immunoCompromisedStatus.getQuestionView().setError(getString(R.string.empty_field));
            immunoCompromisedStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if(immunoCompromisedScore.getVisibility()==View.VISIBLE && App.get(immunoCompromisedScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            immunoCompromisedScore.getQuestionView().setError(getString(R.string.empty_field));
            immunoCompromisedScore.getRadioGroup().requestFocus();
            error = true;
        }

        if(clinicalManifestationStatus.getVisibility()==View.VISIBLE && App.get(clinicalManifestationStatus).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            clinicalManifestationStatus.getQuestionView().setError(getString(R.string.empty_field));
            clinicalManifestationStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if(clinicalManifestationScore.getVisibility()==View.VISIBLE && App.get(clinicalManifestationScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            clinicalManifestationScore.getQuestionView().setError(getString(R.string.empty_field));
            clinicalManifestationScore.getRadioGroup().requestFocus();
            error = true;
        }


        if(radioDiagnostiImagingStatus.getVisibility()==View.VISIBLE && App.get(radioDiagnostiImagingStatus).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radioDiagnostiImagingStatus.getQuestionView().setError(getString(R.string.empty_field));
            radioDiagnostiImagingStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if(radioDiagnosticImagingScore.getVisibility()==View.VISIBLE && App.get(radioDiagnosticImagingScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radioDiagnosticImagingScore.getQuestionView().setError(getString(R.string.empty_field));
            radioDiagnosticImagingScore.getRadioGroup().requestFocus();
            error = true;
        }


        if(tuberculinSkinPpdTestResult.getVisibility()==View.VISIBLE && App.get(tuberculinSkinPpdTestResult).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tuberculinSkinPpdTestResult.getQuestionView().setError(getString(R.string.empty_field));
            tuberculinSkinPpdTestResult.getRadioGroup().requestFocus();
            error = true;
        }

        if(tuberculinSkinPpdTestResultScore.getVisibility()==View.VISIBLE && App.get(tuberculinSkinPpdTestResultScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tuberculinSkinPpdTestResultScore.getQuestionView().setError(getString(R.string.empty_field));
            tuberculinSkinPpdTestResultScore.getRadioGroup().requestFocus();
            error = true;
        }


        if(gxpTestResult.getVisibility()==View.VISIBLE && App.get(gxpTestResult).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            gxpTestResult.getQuestionView().setError(getString(R.string.empty_field));
            gxpTestResult.getRadioGroup().requestFocus();
            error = true;
        }

        if(gxpTestResultScore.getVisibility()==View.VISIBLE && App.get(gxpTestResultScore).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            gxpTestResultScore.getQuestionView().setError(getString(R.string.empty_field));
            gxpTestResultScore.getRadioGroup().requestFocus();
            error = true;
        }

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
        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            }else {
                endTime = new Date();
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
            observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        }

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"PPA SCORE", App.get(ppaScore)});

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

                String result = serverService.saveEncounterAndObservation("PPA Score", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("SUCCESS")) {
                    MainActivity.backToMainMenu();
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

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
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("PPA SCORE")) {
                ppaScore.getEditText().setText(obs[0][1]);
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
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
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        if(App.getPatient().getPerson().getAge()<5){
            ageScore.getRadioGroup().getButtons().get(1).setChecked(true);
        }else{
            ageScore.getRadioGroup().getButtons().get(0).setChecked(true);
        }

        for (RadioButton rb : ageScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);

            } else bundle.putBoolean("save", false);

        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == closeContactStatus.getRadioGroup()) {
            closeContactStatus.getQuestionView().setError(null);
        } else if (group == closeContactScore.getRadioGroup()) {
            closeContactScore.getQuestionView().setError(null);
        } else if (group == pemSam.getRadioGroup()) {
            pemSam.getQuestionView().setError(null);
        }  else if (group == pemSamScore.getRadioGroup()) {
            pemSamScore.getQuestionView().setError(null);
        }  else if (group == historyMeaslesCough.getRadioGroup()) {
            historyMeaslesCough.getQuestionView().setError(null);
        } else  if (group == historyMeaslesCoughScore.getRadioGroup()) {
            historyMeaslesCoughScore.getQuestionView().setError(null);
        }  else if (group == hivStatus.getRadioGroup()) {
            hivStatus.getQuestionView().setError(null);
        }
        else if (group == immunoCompromisedStatus.getRadioGroup()) {
            immunoCompromisedStatus.getQuestionView().setError(null);
        }  else if (group == immunoCompromisedScore.getRadioGroup()) {
            immunoCompromisedScore.getQuestionView().setError(null);
        } else  if (group == clinicalManifestationStatus.getRadioGroup()) {
            clinicalManifestationStatus.getQuestionView().setError(null);
        }
        else if (group == clinicalManifestationScore.getRadioGroup()) {
            clinicalManifestationScore.getQuestionView().setError(null);
        }  else  if (group == radioDiagnostiImagingStatus.getRadioGroup()) {
            radioDiagnostiImagingStatus.getQuestionView().setError(null);
        }  else if (group == radioDiagnosticImagingScore.getRadioGroup()) {
            radioDiagnosticImagingScore.getQuestionView().setError(null);
        } else  if (group == tuberculinSkinPpdTestResult.getRadioGroup()) {
            tuberculinSkinPpdTestResult.getQuestionView().setError(null);
        } else  if (group == tuberculinSkinPpdTestResultScore.getRadioGroup()) {
            tuberculinSkinPpdTestResultScore.getQuestionView().setError(null);
        }
        else  if (group == gxpTestResult.getRadioGroup()) {
            gxpTestResult.getQuestionView().setError(null);
        }  else if (group == gxpTestResultScore.getRadioGroup()) {
            gxpTestResultScore.getQuestionView().setError(null);
        } else  if (group == ppaScoreInterpretation.getRadioGroup()) {
            ppaScoreInterpretation.getQuestionView().setError(null);
        }
    }

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

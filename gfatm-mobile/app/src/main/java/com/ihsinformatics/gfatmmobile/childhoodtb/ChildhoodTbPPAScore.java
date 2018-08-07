package com.ihsinformatics.gfatmmobile.childhoodtb;

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
import android.text.InputType;
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
import android.widget.Toast;

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
    TitledRadioGroup granulomaStatus;
    TitledRadioGroup granulomaScore;
    TitledEditText ppaScore;
    TitledRadioGroup ppaScoreInterpretation;



    /**
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.CHILDHOODTB_PPA_SCORE;
        form = Forms.childhoodTb_ppa_score;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = pageCount - 1; i >= 0; i--) {
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
            for (int i = 0; i < pageCount; i++) {
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
        Toast toast=new Toast(context);
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        ageScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_age_score), getResources().getStringArray(R.array.ctb_0_to_1_list), null, App.HORIZONTAL, App.VERTICAL, true);
        closeContactStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_close_contact_status), getResources().getStringArray(R.array.ctb_close_contact_status_list), null, App.VERTICAL, App.VERTICAL, true);
        closeContactScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_close_contact_score), getResources().getStringArray(R.array.ctb_0_to_3_list), null, App.HORIZONTAL, App.VERTICAL, true);
        pemSam = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pem_sam), getResources().getStringArray(R.array.ctb_pem_sam_list), null, App.VERTICAL, App.VERTICAL, true);
        pemSamScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pem_sam_score), getResources().getStringArray(R.array.ctb_0_to_2_list), null, App.HORIZONTAL, App.VERTICAL, true);
        historyMeaslesCough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_history_of_measles), getResources().getStringArray(R.array.ctb_history_measles_whooping_cough_list), null, App.HORIZONTAL, App.VERTICAL, true);
        historyMeaslesCoughScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_measles_cough_score), getResources().getStringArray(R.array.ctb_0_to_2_list), null, App.HORIZONTAL, App.VERTICAL, true);
        hivStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_hiv_status), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true);
        hivScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_hiv_score), getResources().getStringArray(R.array.ctb_hiv_score_list), null, App.HORIZONTAL, App.VERTICAL, true);
        immunoCompromisedStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_immuno_compromised_status), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true);
        immunoCompromisedScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_immuno_compromised_score), getResources().getStringArray(R.array.ctb_0_to_1_list), null, App.HORIZONTAL, App.VERTICAL, true);
        clinicalManifestationStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_clinical_manifestation_status), getResources().getStringArray(R.array.ctb_clinical_manifestation_status_list), null, App.VERTICAL, App.VERTICAL, true);
        clinicalManifestationScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_clinical_manifestation_score), getResources().getStringArray(R.array.ctb_clinical_manifestation_score_list), null, App.HORIZONTAL, App.VERTICAL, true);
        radioDiagnostiImagingStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_radio_diagnostic_imaging_status), getResources().getStringArray(R.array.ctb_radio_diagnostic_image_status), null, App.VERTICAL, App.VERTICAL, true);
        radioDiagnosticImagingScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_radio_diagnostic_score), getResources().getStringArray(R.array.ctb_0_to_3_list), null, App.HORIZONTAL, App.VERTICAL, true);
        tuberculinSkinPpdTestResult = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tuberculin_skin_ppd_result), getResources().getStringArray(R.array.ctb_tuberculin_skin_test_list), null, App.HORIZONTAL, App.VERTICAL, true);
        tuberculinSkinPpdTestResultScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tuberculin_skin_ppd_result_score), getResources().getStringArray(R.array.ctb_tuberculin_ppd_score_list), null, App.HORIZONTAL, App.VERTICAL, true);

        gxpTestResult = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_gxp_test_result), getResources().getStringArray(R.array.ctb_gxp_test_result_list), null, App.VERTICAL, App.VERTICAL, true);
        gxpTestResultScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_gxp_test_result_score), getResources().getStringArray(R.array.ctb_gxp_test_score), null, App.HORIZONTAL, App.VERTICAL, true);

        granulomaStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_granuloma_status), getResources().getStringArray(R.array.ctb_granuloma_status_list), null, App.HORIZONTAL, App.VERTICAL, true);
        granulomaScore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_granuloma_score), getResources().getStringArray(R.array.ctb_granuloma_score_list), null, App.HORIZONTAL, App.VERTICAL, true);

        ppaScore = new TitledEditText(context, null, getResources().getString(R.string.ctb_ppa_score), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        ppaScoreInterpretation = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ppa_score_interpretation), getResources().getStringArray(R.array.ctb_ppa_interpretation_list), null, App.HORIZONTAL, App.VERTICAL, true);

        views = new View[]{formDate.getButton(), ppaScore.getEditText(),
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
                granulomaStatus.getRadioGroup(),
                granulomaScore.getRadioGroup(),
                ppaScore.getEditText(),
                ppaScoreInterpretation.getRadioGroup()
        };
        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, ageScore,
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
                        granulomaStatus,
                        granulomaScore,
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
        granulomaStatus.getRadioGroup().setOnCheckedChangeListener(this);
        granulomaScore.getRadioGroup().setOnCheckedChangeListener(this);
        ppaScoreInterpretation.getRadioGroup().setOnCheckedChangeListener(this);


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

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
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
        if (closeContactStatus.getVisibility() == View.VISIBLE && App.get(closeContactStatus).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            closeContactStatus.getQuestionView().setError(getString(R.string.empty_field));
            closeContactStatus.getRadioGroup().requestFocus();
            error = true;
        }
        if (closeContactScore.getVisibility() == View.VISIBLE && App.get(closeContactScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            closeContactScore.getQuestionView().setError(getString(R.string.empty_field));
            closeContactScore.getRadioGroup().requestFocus();
            error = true;
        }
        if (pemSam.getVisibility() == View.VISIBLE && App.get(pemSam).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            pemSam.getQuestionView().setError(getString(R.string.empty_field));
            pemSam.getRadioGroup().requestFocus();
            error = true;
        }
        if (pemSamScore.getVisibility() == View.VISIBLE && App.get(pemSamScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            pemSamScore.getQuestionView().setError(getString(R.string.empty_field));
            pemSamScore.getRadioGroup().requestFocus();
            error = true;
        }
        if (historyMeaslesCough.getVisibility() == View.VISIBLE && App.get(historyMeaslesCough).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            historyMeaslesCough.getQuestionView().setError(getString(R.string.empty_field));
            historyMeaslesCough.getRadioGroup().requestFocus();
            error = true;
        }
        if (historyMeaslesCoughScore.getVisibility() == View.VISIBLE && App.get(historyMeaslesCoughScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            historyMeaslesCoughScore.getQuestionView().setError(getString(R.string.empty_field));
            historyMeaslesCoughScore.getRadioGroup().requestFocus();
            error = true;
        }

        if (hivStatus.getVisibility() == View.VISIBLE && App.get(hivStatus).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hivStatus.getQuestionView().setError(getString(R.string.empty_field));
            hivStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if (hivScore.getVisibility() == View.VISIBLE && App.get(hivScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hivScore.getQuestionView().setError(getString(R.string.empty_field));
            hivScore.getRadioGroup().requestFocus();
            error = true;
        }

        if (immunoCompromisedStatus.getVisibility() == View.VISIBLE && App.get(immunoCompromisedStatus).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            immunoCompromisedStatus.getQuestionView().setError(getString(R.string.empty_field));
            immunoCompromisedStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if (immunoCompromisedScore.getVisibility() == View.VISIBLE && App.get(immunoCompromisedScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            immunoCompromisedScore.getQuestionView().setError(getString(R.string.empty_field));
            immunoCompromisedScore.getRadioGroup().requestFocus();
            error = true;
        }

        if (clinicalManifestationStatus.getVisibility() == View.VISIBLE && App.get(clinicalManifestationStatus).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            clinicalManifestationStatus.getQuestionView().setError(getString(R.string.empty_field));
            clinicalManifestationStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if (clinicalManifestationScore.getVisibility() == View.VISIBLE && App.get(clinicalManifestationScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            clinicalManifestationScore.getQuestionView().setError(getString(R.string.empty_field));
            clinicalManifestationScore.getRadioGroup().requestFocus();
            error = true;
        }


        if (radioDiagnostiImagingStatus.getVisibility() == View.VISIBLE && App.get(radioDiagnostiImagingStatus).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radioDiagnostiImagingStatus.getQuestionView().setError(getString(R.string.empty_field));
            radioDiagnostiImagingStatus.getRadioGroup().requestFocus();
            error = true;
        }

        if (radioDiagnosticImagingScore.getVisibility() == View.VISIBLE && App.get(radioDiagnosticImagingScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            radioDiagnosticImagingScore.getQuestionView().setError(getString(R.string.empty_field));
            radioDiagnosticImagingScore.getRadioGroup().requestFocus();
            error = true;
        }


        if (tuberculinSkinPpdTestResult.getVisibility() == View.VISIBLE && App.get(tuberculinSkinPpdTestResult).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tuberculinSkinPpdTestResult.getQuestionView().setError(getString(R.string.empty_field));
            tuberculinSkinPpdTestResult.getRadioGroup().requestFocus();
            error = true;
        }

        if (tuberculinSkinPpdTestResultScore.getVisibility() == View.VISIBLE && App.get(tuberculinSkinPpdTestResultScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            tuberculinSkinPpdTestResultScore.getQuestionView().setError(getString(R.string.empty_field));
            tuberculinSkinPpdTestResultScore.getRadioGroup().requestFocus();
            error = true;
        }


        if (gxpTestResult.getVisibility() == View.VISIBLE && App.get(gxpTestResult).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            gxpTestResult.getQuestionView().setError(getString(R.string.empty_field));
            gxpTestResult.getRadioGroup().requestFocus();
            error = true;
        }

        if (gxpTestResultScore.getVisibility() == View.VISIBLE && App.get(gxpTestResultScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            gxpTestResultScore.getQuestionView().setError(getString(R.string.empty_field));
            gxpTestResultScore.getRadioGroup().requestFocus();
            error = true;
        }
        if (granulomaStatus.getVisibility() == View.VISIBLE && App.get(granulomaStatus).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            granulomaStatus.getQuestionView().setError(getString(R.string.empty_field));
            granulomaStatus.getRadioGroup().requestFocus();
            error = true;
        }
        if (granulomaScore.getVisibility() == View.VISIBLE && App.get(granulomaScore).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            granulomaScore.getQuestionView().setError(getString(R.string.empty_field));
            granulomaScore.getRadioGroup().requestFocus();
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

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
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
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
                observations.add(new String[]{"TIME TAKEN TO FILL FORM", timeTakeToFill});
            } else {
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


        observations.add(new String[]{"AGE SCORE", App.get(ageScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                        "1"});

        if(closeContactStatus.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"CLOSE CONTACT STATUS", App.get(closeContactStatus).equals(getResources().getString(R.string.ctb_tb_suggestive)) ? "SUGGESTIVE OF TB" :
                    (App.get(closeContactStatus).equals(getResources().getString(R.string.ctb_clinically_diagnosed_tb)) ? "CLINICALLY DIAGNOSED, TB" :
                            (App.get(closeContactStatus).equals(getResources().getString(R.string.ctb_none)) ? "NONE" :
                                    "PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY"))});
        }

        if(closeContactScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"CLOSE CONTACT SCORE", App.get(closeContactScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    (App.get(closeContactScore).equals(getResources().getString(R.string.ctb_1)) ? "1" :
                            (App.get(closeContactScore).equals(getResources().getString(R.string.ctb_2)) ? "2" :
                                    "3"))});
        }

        if(pemSam.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"PEM OR SAM", App.get(pemSam).equals(getResources().getString(R.string.no)) ? "NO" :
                    (App.get(pemSam).equals(getResources().getString(R.string.yes)) ? "YES" :
                            "NOT RESPONDING TO NUTRITIONAL REHABILITATION FOR 2 MONTHS")});
        }

        if(pemSamScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"PEM OR SAM SCORE", App.get(pemSamScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    (App.get(pemSamScore).equals(getResources().getString(R.string.ctb_1)) ? "1" :
                            "2")});
        }

        if(historyMeaslesCough.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"HISTORY OF MEASLES OR WHOOPING COUGH", App.get(historyMeaslesCough).equals(getResources().getString(R.string.no)) ? "NO" :
                    (App.get(historyMeaslesCough).equals(getResources().getString(R.string.ctb_less_than_3_month)) ? "LESS THAN 3 MONTHS" :
                            "3 TO 6 MONTHS")});
        }

        if(historyMeaslesCoughScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"MEASLES OR WHOOPING COUGH SCORE", App.get(historyMeaslesCoughScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    (App.get(historyMeaslesCoughScore).equals(getResources().getString(R.string.ctb_1)) ? "1" :
                            "2")});
        }
        if(hivStatus.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"HIV INFECTED", App.get(hivStatus).equals(getResources().getString(R.string.yes)) ? "YES" :
                    "NO"});
        }

        if(hivScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"HIV SCORE", App.get(hivScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    "2"});
        }

        if(immunoCompromisedStatus.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"IMMUNO COMPROMISED STATUS", App.get(immunoCompromisedStatus).equals(getResources().getString(R.string.yes)) ? "YES" :
                    "NO"});
        }

        if(immunoCompromisedScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"IMMUNO COMPROMISED SCORE", App.get(immunoCompromisedScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    "1"});
        }

        if(clinicalManifestationStatus.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"CLINICAL MANIFESTATION STATUS", App.get(clinicalManifestationStatus).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(clinicalManifestationStatus).equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) ? "STRONGLY SUGGESTIVE OF TB" :
                            "NOT SUGGESTIVE OF TB")});
        }

        if(clinicalManifestationScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"CLINICAL MANIFESTATION SCORE", App.get(clinicalManifestationScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    (App.get(clinicalManifestationScore).equals(getResources().getString(R.string.ctb_2)) ? "2" :
                            "4")});
        }

        if(radioDiagnostiImagingStatus.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"RADIO DIAGNOSTIC IMAGING STATUS", App.get(radioDiagnostiImagingStatus).equals(getResources().getString(R.string.ctb_non_specific)) ? "NON SPECIFIC" :
                    (App.get(radioDiagnostiImagingStatus).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                            (App.get(radioDiagnostiImagingStatus).equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) ? "STRONGLY SUGGESTIVE OF TB" :
                                    "NONE"))});
        }

        if(radioDiagnosticImagingScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"RADIO DIAGNOSTIC IMAGING SCORE", App.get(radioDiagnosticImagingScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    (App.get(radioDiagnosticImagingScore).equals(getResources().getString(R.string.ctb_1)) ? "1" :
                            (App.get(radioDiagnosticImagingScore).equals(getResources().getString(R.string.ctb_2)) ? "2" :
                                    "3"))});
        }

        if(tuberculinSkinPpdTestResult.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"TUBERCULIN SKIN TEST RESULT", App.get(tuberculinSkinPpdTestResult).equals(getResources().getString(R.string.ctb_less_than_5mm)) ? "<5 mm" :
                    App.get(tuberculinSkinPpdTestResult).equals(getResources().getString(R.string.ctb_5_to_9mm)) ? "5 - 9 mm" :
                            "≥10 mm"});
        }

        if(tuberculinSkinPpdTestResultScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"TUBERCULIN SKIN TEST RESULT SCORE", App.get(tuberculinSkinPpdTestResultScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                    App.get(tuberculinSkinPpdTestResultScore).equals(getResources().getString(R.string.ctb_1)) ? "1" :
                            "3"});
        }

        if (gxpTestResult.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT MTB/RIF RESULT", App.get(gxpTestResult).equals(getResources().getString(R.string.ctb_mtb_detected)) ? "DETECTED" :
                    (App.get(gxpTestResult).equals(getResources().getString(R.string.ctb_mtb_not_detected)) ? "NOT DETECTED" :
                            (App.get(gxpTestResult).equals(getResources().getString(R.string.ctb_error)) ? "ERROR" :
                                    (App.get(gxpTestResult).equals(getResources().getString(R.string.ctb_invalid)) ? "INVALID" : "NO RESULT")))});
        }

        if(gxpTestResultScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"GENEXPERT MTB/RIF RESULT SCORE", App.get(gxpTestResultScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                            "5"});
        }


        if (granulomaStatus.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"GRANULOMA", App.get(granulomaStatus).equals(getResources().getString(R.string.ctb_non_specific)) ? "NON SPECIFIC" :
                    (App.get(granulomaStatus).equals(getResources().getString(R.string.ctb_no)) ? "NO" :
                            "POSITIVE TB")});
        }

        if(granulomaScore.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"GRANULOMA SCORE", App.get(granulomaScore).equals(getResources().getString(R.string.ctb_1)) ? "1" :
                    (App.get(granulomaScore).equals(getResources().getString(R.string.ctb_0)) ? "0" :
                                        "5")});
        }

        observations.add(new String[]{"PPA SCORE", App.get(ppaScore)});

        if(ppaScoreInterpretation.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"PPA SCORE INTERPRETATION", App.get(ppaScoreInterpretation).equals(getResources().getString(R.string.ctb_unlikely_tb)) ? "UNLIKELY TB" :
                    App.get(ppaScoreInterpretation).equals(getResources().getString(R.string.ctb_possible_tb)) ? "POSSIBLE TB" :
                            "PROBABLE TB"});
        }

        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        popUpAlerts();
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.submitting_form));
                        loading.show();
                    }
                });

                String result = serverService.saveEncounterAndObservation("Childhood TB-PPA Score", form, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        serverService.saveFormLocally(formName, form, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("form START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            }
            else if (obs[0][0].equals("AGE SCORE")) {

                for (RadioButton rb : ageScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                ageScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("CLOSE CONTACT STATUS")) {
                for (RadioButton rb : closeContactStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_tb_suggestive)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_clinically_diagnosed_tb)) && obs[0][1].equals("CLINICALLY DIAGNOSED, TB")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_bacteriologically_positive_tb)) && obs[0][1].equals("PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                closeContactStatus.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("CLOSE CONTACT SCORE")) {

                for (RadioButton rb : closeContactScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                closeContactScore.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("PEM OR SAM")) {

                for (RadioButton rb : pemSam.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_not_responding_nutritional_rehabilation)) && obs[0][1].equals("NOT RESPONDING TO NUTRITIONAL REHABILITATION FOR 2 MONTHS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                pemSam.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("PEM OR SAM SCORE")) {

                for (RadioButton rb : pemSamScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                pemSamScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("HISTORY OF MEASLES OR WHOOPING COUGH")) {

                for (RadioButton rb : historyMeaslesCough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_less_than_3_month)) && obs[0][1].equals("LESS THAN 3 MONTHS")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_3_to_6_months)) && obs[0][1].equals("3 TO 6 MONTHS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                historyMeaslesCough.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("MEASLES OR WHOOPING COUGH SCORE")) {

                for (RadioButton rb : historyMeaslesCoughScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                historyMeaslesCoughScore.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("HIV INFECTED")) {

                for (RadioButton rb : hivStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                hivStatus.setVisibility(View.VISIBLE);
            }


            else if (obs[0][0].equals("HIV SCORE")) {
                for (RadioButton rb : hivScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                hivScore.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("IMMUNO COMPROMISED STATUS")) {

                for (RadioButton rb : immunoCompromisedStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                immunoCompromisedStatus.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("IMMUNO COMPROMISED SCORE")) {

                for (RadioButton rb : immunoCompromisedScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                immunoCompromisedScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("CLINICAL MANIFESTATION STATUS")) {
                for (RadioButton rb : clinicalManifestationStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) && obs[0][1].equals("STRONGLY SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NOT SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                clinicalManifestationStatus.setVisibility(View.VISIBLE);
            }


            else if (obs[0][0].equals("CLINICAL MANIFESTATION SCORE")) {
                for (RadioButton rb : clinicalManifestationScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_4)) && obs[0][1].equals("4")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                clinicalManifestationScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("RADIO DIAGNOSTIC IMAGING STATUS")) {
                for (RadioButton rb : radioDiagnostiImagingStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_non_specific)) && obs[0][1].equals("NON SPECIFIC")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) && obs[0][1].equals("STRONGLY SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_none)) && obs[0][1].equals("NONE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                radioDiagnostiImagingStatus.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("RADIO DIAGNOSTIC IMAGING SCORE")) {
                for (RadioButton rb : radioDiagnosticImagingScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_2)) && obs[0][1].equals("2")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                radioDiagnosticImagingScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("TUBERCULIN SKIN TEST RESULT")) {
                for (RadioButton rb : tuberculinSkinPpdTestResult.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_less_than_5mm)) && obs[0][1].equals("<5 mm")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_5_to_9mm)) && obs[0][1].equals("5 - 9 mm")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_greater_than_10mm)) && obs[0][1].equals("≥10 mm")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tuberculinSkinPpdTestResult.setVisibility(View.VISIBLE);
            }else if (obs[0][0].equals("TUBERCULIN SKIN TEST RESULT SCORE")) {
                for (RadioButton rb : tuberculinSkinPpdTestResultScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_3)) && obs[0][1].equals("3")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                tuberculinSkinPpdTestResultScore.setVisibility(View.VISIBLE);
            }



            else if (obs[0][0].equals("GENEXPERT MTB/RIF RESULT")) {
                for (RadioButton rb : gxpTestResult.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_mtb_detected)) && obs[0][1].equals("DETECTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_mtb_not_detected)) && obs[0][1].equals("NOT DETECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_error)) && obs[0][1].equals("ERROR")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_invalid)) && obs[0][1].equals("INVALID")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_no_result)) && obs[0][1].equals("NO RESULT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                gxpTestResult.setVisibility(View.VISIBLE);
            }

            else if (obs[0][0].equals("GENEXPERT MTB/RIF RESULT SCORE")) {
                for (RadioButton rb : gxpTestResultScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && obs[0][1].equals("5")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                gxpTestResultScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("GRANULOMA")) {
                for (RadioButton rb : granulomaStatus.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_non_specific)) && obs[0][1].equals("NON SPECIFIC")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_positive_tb)) && obs[0][1].equals("POSITIVE TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                granulomaStatus.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("GRANULOMA SCORE")) {
                for (RadioButton rb : granulomaScore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_0)) && obs[0][1].equals("0")) {
                        rb.setChecked(true);
                        break;
                    }
                    else if (rb.getText().equals(getResources().getString(R.string.ctb_1)) && obs[0][1].equals("1")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_5)) && obs[0][1].equals("5")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                granulomaScore.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("PPA SCORE")) {
                ppaScore.getEditText().setText(obs[0][1]);
            }

            else if (obs[0][0].equals("PPA SCORE INTERPRETATION")) {
                for (RadioButton rb : ppaScoreInterpretation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_unlikely_tb)) && obs[0][1].equals("UNLIKELY TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_possible_tb)) && obs[0][1].equals("POSSIBLE TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_probable_tb)) && obs[0][1].equals("PROBABLE TB")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                ppaScoreInterpretation.setVisibility(View.VISIBLE);
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

        closeContactScore.setVisibility(View.GONE);
        pemSamScore.setVisibility(View.GONE);
        historyMeaslesCoughScore.setVisibility(View.GONE);
        hivScore.setVisibility(View.GONE);
        immunoCompromisedScore.setVisibility(View.GONE);
        clinicalManifestationScore.setVisibility(View.GONE);
        radioDiagnosticImagingScore.setVisibility(View.GONE);
        tuberculinSkinPpdTestResultScore.setVisibility(View.GONE);
        gxpTestResultScore.setVisibility(View.GONE);
        granulomaScore.setVisibility(View.GONE);

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        if (App.getPatient().getPerson().getAge() < 5) {
            ageScore.getRadioGroup().getButtons().get(1).setChecked(true);
        } else {
            ageScore.getRadioGroup().getButtons().get(0).setChecked(true);
        }

        getPPAScore();
        //Disabling key listeners for Radiobuttons and EditTexts
        for (RadioButton rb : ageScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : closeContactScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : pemSamScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : historyMeaslesCoughScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : hivScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : immunoCompromisedScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }
        for (RadioButton rb : radioDiagnosticImagingScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }
        for (RadioButton rb : clinicalManifestationScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : tuberculinSkinPpdTestResultScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : gxpTestResultScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }
        ppaScore.getEditText().setKeyListener(null);

        for (RadioButton rb : ppaScoreInterpretation.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }

        for (RadioButton rb : granulomaScore.getRadioGroup().getButtons()) {
            rb.setClickable(false);
        }



        String mantouxResult = serverService.getLatestObsValue(App.getPatientId(), "Mantoux Test Result", "TUBERCULIN SKIN TEST RESULT");
        if(mantouxResult!=null){
            for (RadioButton rb : tuberculinSkinPpdTestResult.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_less_than_5mm)) && mantouxResult.equals("<5 mm")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_5_to_9mm)) && mantouxResult.equals("5 - 9 mm")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_greater_than_10mm)) && mantouxResult.equals("≥10 mm")) {
                    rb.setChecked(true);
                    break;
                }
            }
        }

        String gxpResult = serverService.getLatestObsValue(App.getPatientId(), "GeneXpert Result", "GENEXPERT MTB/RIF RESULT");
        if(gxpResult!=null){
            for (RadioButton rb : gxpTestResult.getRadioGroup().getButtons()) {
                if (rb.getText().equals(getResources().getString(R.string.ctb_mtb_detected)) && gxpResult.equals("DETECTED")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_mtb_not_detected)) && gxpResult.equals("NOT DETECTED")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_error)) && gxpResult.equals("ERROR")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_invalid)) && gxpResult.equals("INVALID")) {
                    rb.setChecked(true);
                    break;
                } else if (rb.getText().equals(getResources().getString(R.string.ctb_no_result)) && gxpResult.equals("NO RESULT")) {
                    rb.setChecked(true);
                    break;
                }
            }
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

    void popUpAlerts(){
        String s = App.get(ppaScore).toString();
        if(closeContactStatus.getVisibility()==View.VISIBLE){
            if(!App.get(closeContactStatus).isEmpty() && !App.get(pemSam).isEmpty() && !App.get(historyMeaslesCough).isEmpty() && !App.get(hivStatus).isEmpty() && !App.get(immunoCompromisedStatus).isEmpty() && !App.get(clinicalManifestationStatus).isEmpty() && !App.get(radioDiagnostiImagingStatus).isEmpty() && !App.get(tuberculinSkinPpdTestResult).isEmpty() && !App.get(gxpTestResult).isEmpty() && !App.get(granulomaStatus).isEmpty()){
                if (Integer.parseInt(s.toString()) >= 0 && Integer.parseInt(s.toString()) <= 2) {

                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_7), Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(s.toString()) >= 3 && Integer.parseInt(s.toString()) <= 4) {
                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_8), Toast.LENGTH_LONG).show();
                }else if (Integer.parseInt(s.toString()) >= 5 && Integer.parseInt(s.toString()) <= 6) {
                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_9), Toast.LENGTH_LONG).show();
                }
                else if (Integer.parseInt(s.toString()) >= 7) {
                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_10), Toast.LENGTH_SHORT).show();
                }
            }
        }else{
            if(!App.get(pemSam).isEmpty() && !App.get(historyMeaslesCough).isEmpty() && !App.get(hivStatus).isEmpty() && !App.get(immunoCompromisedStatus).isEmpty() && !App.get(clinicalManifestationStatus).isEmpty() && !App.get(radioDiagnostiImagingStatus).isEmpty() && !App.get(tuberculinSkinPpdTestResult).isEmpty() && !App.get(gxpTestResult).isEmpty() && !App.get(granulomaStatus).isEmpty()){
                if (Integer.parseInt(s.toString()) >= 0 && Integer.parseInt(s.toString()) <= 2) {

                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_7), Toast.LENGTH_SHORT).show();
                }
                else if (Integer.parseInt(s.toString()) >= 3 && Integer.parseInt(s.toString()) <= 4) {
                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_8), Toast.LENGTH_LONG).show();
                }else if (Integer.parseInt(s.toString()) >= 5 && Integer.parseInt(s.toString()) <= 6) {
                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_9), Toast.LENGTH_LONG).show();
                }
                else if (Integer.parseInt(s.toString()) >= 7) {
                    Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_10), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    void getPPAScore(){
        int score=0;

        int ppaScore0=0,ppaScore1=0,ppaScore2=0,ppaScore3=0,ppaScore4=0,ppaScore5=0,ppaScore6=0,ppaScore7=0,ppaScore8=0,ppaScore9=0,ppaScore10=0;
        if(!App.get(ageScore).isEmpty()){
            ppaScore0 = Integer.parseInt(App.get(ageScore));
        }
        if(!App.get(closeContactScore).isEmpty()){
            ppaScore1 = Integer.parseInt(App.get(closeContactScore));
        }
        if(!App.get(pemSamScore).isEmpty()){
            ppaScore2 = Integer.parseInt(App.get(pemSamScore));
        }
        if(!App.get(historyMeaslesCoughScore).isEmpty()){
            ppaScore3 = Integer.parseInt(App.get(historyMeaslesCoughScore));
        }
        if(!App.get(hivScore).isEmpty()){
            ppaScore4 = Integer.parseInt(App.get(hivScore));
        }
        if(!App.get(immunoCompromisedScore).isEmpty()){
            ppaScore5 = Integer.parseInt(App.get(immunoCompromisedScore));
        }
        if(!App.get(clinicalManifestationScore).isEmpty()){
            ppaScore6 = Integer.parseInt(App.get(clinicalManifestationScore));
        }
        if(!App.get(radioDiagnosticImagingScore).isEmpty()){
            ppaScore7 = Integer.parseInt(App.get(radioDiagnosticImagingScore));
        }
        if(!App.get(tuberculinSkinPpdTestResultScore).isEmpty()){
            ppaScore8 = Integer.parseInt(App.get(tuberculinSkinPpdTestResultScore));
        }
        if(!App.get(gxpTestResultScore).isEmpty()){
            ppaScore9 = Integer.parseInt(App.get(gxpTestResultScore));
        }

        if(!App.get(granulomaScore).isEmpty()){
            ppaScore10 = Integer.parseInt(App.get(granulomaScore));
        }

        score = ppaScore0 + ppaScore1 + ppaScore2 + ppaScore3 + ppaScore4 + ppaScore5 + ppaScore6 + ppaScore7 + ppaScore8 + ppaScore9+ppaScore10;
        if(score>=0 && score<=2){
            ppaScoreInterpretation.getRadioGroup().getButtons().get(0).setChecked(true);
        }else if(score>=3 && score<=6){
            ppaScoreInterpretation.getRadioGroup().getButtons().get(1).setChecked(true);
        }else if(score>=7){
            ppaScoreInterpretation.getRadioGroup().getButtons().get(2).setChecked(true);
        }

        ppaScore.getEditText().setText(String.valueOf(score));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == closeContactStatus.getRadioGroup()) {
            closeContactStatus.getQuestionView().setError(null);
            closeContactScore.setVisibility(View.VISIBLE);
            if (closeContactStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_tb_suggestive))) {
                Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_1), Toast.LENGTH_LONG).show();
                closeContactScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (closeContactStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_clinically_diagnosed_tb))) {
                closeContactScore.getRadioGroup().getButtons().get(2).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (closeContactStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_bacteriologically_positive_tb))) {
                closeContactScore.getRadioGroup().getButtons().get(3).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else {
                closeContactScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }


        } else if (group == closeContactScore.getRadioGroup()) {
            closeContactScore.getQuestionView().setError(null);
        } else if (group == pemSam.getRadioGroup()) {
            pemSam.getQuestionView().setError(null);
            pemSamScore.setVisibility(View.VISIBLE);
            if (pemSam.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                pemSamScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (pemSam.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                pemSamScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (pemSam.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_not_responding_nutritional_rehabilation))) {
                pemSamScore.getRadioGroup().getButtons().get(2).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == pemSamScore.getRadioGroup()) {
            pemSamScore.getQuestionView().setError(null);
        } else if (group == historyMeaslesCough.getRadioGroup()) {
            historyMeaslesCough.getQuestionView().setError(null);
            historyMeaslesCoughScore.setVisibility(View.VISIBLE);
            if (historyMeaslesCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                historyMeaslesCoughScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (historyMeaslesCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_less_than_3_month))) {
                historyMeaslesCoughScore.getRadioGroup().getButtons().get(2).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (historyMeaslesCough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_3_to_6_months))) {
                historyMeaslesCoughScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == historyMeaslesCoughScore.getRadioGroup()) {
            historyMeaslesCoughScore.getQuestionView().setError(null);
        } else if (group == hivStatus.getRadioGroup()) {
            hivStatus.getQuestionView().setError(null);
            hivScore.setVisibility(View.VISIBLE);
            if (hivStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                hivScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (hivStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                hivScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == hivScore.getRadioGroup()) {
            hivScore.getQuestionView().setError(null);
        }else if (group == immunoCompromisedStatus.getRadioGroup()) {
            immunoCompromisedStatus.getQuestionView().setError(null);
            immunoCompromisedScore.setVisibility(View.VISIBLE);
            if (immunoCompromisedStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no))) {
                immunoCompromisedScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (immunoCompromisedStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                immunoCompromisedScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == immunoCompromisedScore.getRadioGroup()) {
            immunoCompromisedScore.getQuestionView().setError(null);
        } else if (group == clinicalManifestationStatus.getRadioGroup()) {
            clinicalManifestationStatus.getQuestionView().setError(null);
            clinicalManifestationScore.setVisibility(View.VISIBLE);
            if (clinicalManifestationStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                clinicalManifestationScore.getRadioGroup().getButtons().get(1).setChecked(true);
                Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_2), Toast.LENGTH_LONG).show();
                getPPAScore();
                popUpAlerts();
            } else if (clinicalManifestationStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_strongly_suggestive_tb))) {
                clinicalManifestationScore.getRadioGroup().getButtons().get(2).setChecked(true);
                Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_3), Toast.LENGTH_LONG).show();
                getPPAScore();
                popUpAlerts();
            } else if (clinicalManifestationStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_not_sugguestive_tb))) {
                clinicalManifestationScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == clinicalManifestationScore.getRadioGroup()) {
            clinicalManifestationScore.getQuestionView().setError(null);
        } else if (group == radioDiagnostiImagingStatus.getRadioGroup()) {
            radioDiagnostiImagingStatus.getQuestionView().setError(null);
            radioDiagnosticImagingScore.setVisibility(View.VISIBLE);
            if (radioDiagnostiImagingStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_non_specific))) {
                Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_4), Toast.LENGTH_LONG).show();
                radioDiagnosticImagingScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (radioDiagnostiImagingStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_suggestive_tb))) {
                Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_5), Toast.LENGTH_LONG).show();
                radioDiagnosticImagingScore.getRadioGroup().getButtons().get(2).setChecked(true);
                popUpAlerts();
                getPPAScore();
            } else if (radioDiagnostiImagingStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_strongly_suggestive_tb))) {
                Toast.makeText(context, getResources().getString(R.string.ctb_ppa_popup_6), Toast.LENGTH_LONG).show();
                radioDiagnosticImagingScore.getRadioGroup().getButtons().get(3).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }else if (radioDiagnostiImagingStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_none))) {
                radioDiagnosticImagingScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == radioDiagnosticImagingScore.getRadioGroup()) {
            radioDiagnosticImagingScore.getQuestionView().setError(null);
        } else if (group == tuberculinSkinPpdTestResult.getRadioGroup()) {
            tuberculinSkinPpdTestResult.getQuestionView().setError(null);
            tuberculinSkinPpdTestResultScore.setVisibility(View.VISIBLE);
            if (tuberculinSkinPpdTestResult.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_less_than_5mm))) {
                tuberculinSkinPpdTestResultScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (tuberculinSkinPpdTestResult.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_5_to_9mm))) {
                tuberculinSkinPpdTestResultScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (tuberculinSkinPpdTestResult.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_greater_than_10mm))) {
                tuberculinSkinPpdTestResultScore.getRadioGroup().getButtons().get(2).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == tuberculinSkinPpdTestResultScore.getRadioGroup()) {
            tuberculinSkinPpdTestResultScore.getQuestionView().setError(null);
        } else if (group == gxpTestResult.getRadioGroup()) {
            gxpTestResult.getQuestionView().setError(null);
            gxpTestResultScore.setVisibility(View.VISIBLE);
            if (gxpTestResult.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_mtb_detected))) {
                gxpTestResultScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else {
                gxpTestResultScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == gxpTestResultScore.getRadioGroup()) {
            gxpTestResultScore.getQuestionView().setError(null);
        }

        else if (group == granulomaStatus.getRadioGroup()) {
            granulomaStatus.getQuestionView().setError(null);
            granulomaScore.setVisibility(View.VISIBLE);
            if (granulomaStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_positive_tb))) {
                granulomaScore.getRadioGroup().getButtons().get(2).setChecked(true);
                getPPAScore();
                popUpAlerts();
            } else if (granulomaStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_non_specific))) {
                granulomaScore.getRadioGroup().getButtons().get(1).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }else {
                granulomaScore.getRadioGroup().getButtons().get(0).setChecked(true);
                getPPAScore();
                popUpAlerts();
            }
        } else if (group == granulomaScore.getRadioGroup()) {
            granulomaScore.getQuestionView().setError(null);
        }




        else if (group == ppaScoreInterpretation.getRadioGroup()) {
            ppaScoreInterpretation.getQuestionView().setError(null);
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageCount;
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

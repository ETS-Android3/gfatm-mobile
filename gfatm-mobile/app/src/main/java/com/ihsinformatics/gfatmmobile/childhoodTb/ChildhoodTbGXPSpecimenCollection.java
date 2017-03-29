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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbGXPSpecimenCollection extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledButton sampleSubmitDate;
    TitledSpinner baselineRepeatFollowup;
    TitledRadioGroup patientCategory;
    TitledSpinner reasonBaselineRepeat;
    TitledRadioGroup specimenType;
    TitledSpinner specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;
    TitledRadioGroup sampleAcceptedByTechnician;
    TitledSpinner whySampleRejcted;
    TitledEditText reasonForRejection;
    TitledEditText cartridgeId;

    Snackbar snackbar;
    ScrollView scrollView;

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
        FORM_NAME = Forms.CHILDHOODTB_GXP_SPECIMEN_COLLECTION_FORM;
        FORM = Forms.childhoodTb_gxp_specimen_form;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        sampleSubmitDate = new TitledButton(context, null, getResources().getString(R.string.ctb_sample_submitted), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        sampleSubmitDate.setTag("sampleSubmitDate");
        baselineRepeatFollowup = new TitledSpinner(context, null, getResources().getString(R.string.ctb_baseline_repeat_followup), getResources().getStringArray(R.array.ctb_ctb_baseline_repeat_followup_list), null, App.VERTICAL);
        patientCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_patient_category), getResources().getStringArray(R.array.ctb_patient_category_list), null, App.HORIZONTAL, App.VERTICAL);
        reasonBaselineRepeat = new TitledSpinner(context, null, getResources().getString(R.string.ctb_reason_for_repeating), getResources().getStringArray(R.array.ctb_reason_for_repeating_list), null, App.VERTICAL);
        specimenType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_specimen_type), getResources().getStringArray(R.array.ctb_specimen_type_list), null, App.HORIZONTAL, App.VERTICAL);
        specimenComeFrom = new TitledSpinner(context, null, getResources().getString(R.string.ctb_speciment_route), getResources().getStringArray(R.array.ctb_speciment_route_list), null, App.VERTICAL);
        otherSpecimentComeFrom = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        sampleAcceptedByTechnician = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_sample_accepted_lab_techician), getResources().getStringArray(R.array.ctb_accepted_by_techician_list), null, App.HORIZONTAL, App.VERTICAL);
        whySampleRejcted = new TitledSpinner(context, null, getResources().getString(R.string.ctb_why_sample_rejected), getResources().getStringArray(R.array.ctb_why_sample_rejected_list), null, App.VERTICAL);
        reasonForRejection = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_reason_rejection), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        cartridgeId = new TitledEditText(context, null, getResources().getString(R.string.ctb_cartridge_id), "", "", 10, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);


        views = new View[]{formDate.getButton(), sampleSubmitDate.getButton(), baselineRepeatFollowup.getSpinner(), patientCategory.getRadioGroup(), reasonBaselineRepeat.getSpinner(),
                specimenType.getRadioGroup(), specimenComeFrom.getSpinner(), sampleAcceptedByTechnician.getRadioGroup(), whySampleRejcted.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, sampleSubmitDate, baselineRepeatFollowup, patientCategory, reasonBaselineRepeat, specimenType, specimenComeFrom, otherSpecimentComeFrom, sampleAcceptedByTechnician
                        , whySampleRejcted, reasonForRejection, cartridgeId}};

        formDate.getButton().setOnClickListener(this);
        sampleSubmitDate.getButton().setOnClickListener(this);
        baselineRepeatFollowup.getSpinner().setOnItemSelectedListener(this);
        patientCategory.getRadioGroup().setOnCheckedChangeListener(this);
        reasonBaselineRepeat.getSpinner().setOnItemSelectedListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getSpinner().setOnItemSelectedListener(this);
        sampleAcceptedByTechnician.getRadioGroup().setOnCheckedChangeListener(this);
        whySampleRejcted.getSpinner().setOnItemSelectedListener(this);


        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


        Date date = new Date();

        if (formDateCalendar.after(date)) {

            formDateCalendar = App.getCalendar(date);

            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

        } else
            formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        if (!sampleSubmitDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                sampleSubmitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }
    }

    @Override
    public boolean validate() {
        boolean error = false;
        if (patientCategory.getVisibility() == View.VISIBLE && App.get(patientCategory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientCategory.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            patientCategory.getRadioGroup().requestFocus();
            error = true;
        } else {
            patientCategory.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            specimenType.getRadioGroup().requestFocus();
            error = true;
        } else {
            specimenType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE && App.get(otherSpecimentComeFrom).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherSpecimentComeFrom.getEditText().setError(getString(R.string.empty_field));
            otherSpecimentComeFrom.getEditText().requestFocus();
            error = true;
        }
        if (reasonForRejection.getVisibility() == View.VISIBLE && App.get(reasonForRejection).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonForRejection.getEditText().setError(getString(R.string.empty_field));
            reasonForRejection.getEditText().requestFocus();
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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"SPECIMEN SUBMISSION DATE", App.getSqlDateTime(secondDateCalendar)});
        observations.add(new String[]{"TEST CONTEXT STATUS", App.get(baselineRepeatFollowup).equals(getResources().getString(R.string.ctb_baseline)) ? "BASELINE" :
                (App.get(baselineRepeatFollowup).equals(getResources().getString(R.string.ctb_baseline_repeat)) ? "BASELINE REPEAT" :
                        "REGULAR FOLLOW UP")});

        if (patientCategory.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TB CATEGORY", App.get(patientCategory).equals(getResources().getString(R.string.ctb_categoryI)) ? "CATEGORY I TUBERCULOSIS" :
                    "CATEGORY II TUBERCULOSIS"});
        }

        if (reasonBaselineRepeat.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"REASON FOR BASELINE REPEAT TEST", App.get(reasonBaselineRepeat).equals(getResources().getString(R.string.ctb_rr_positive)) ? "RIF RESISTANT POSITIVE" :
                    (App.get(reasonBaselineRepeat).equals(getResources().getString(R.string.ctb_error_invalid_no_result)) ? "INVALID" :
                            "INDETERMINATE")});
        }

        observations.add(new String[]{"SPECIMEN TYPE", App.get(specimenType).equals(getResources().getString(R.string.ctb_sputum)) ? "SPUTUM" :
                "EXTRA-PULMONARY TUBERCULOSIS"});

        if (specimenComeFrom.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"SPECIMEN SOURCE", App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_lymph)) ? "LYMPHOCYTES" :
                    (App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_pleural_fluid)) ? "PLEURAL EFFUSION" :
                            (App.get(specimenComeFrom).equals(getResources().getString(R.string.ctb_pus)) ? "PUS" :
                                    "OTHER SPECIMEN SOURCE"))});
        }

        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OTHER SPECIMEN SOURCE", App.get(otherSpecimentComeFrom)});
        }

        observations.add(new String[]{"SPECIMEN ACCEPTED", App.get(sampleAcceptedByTechnician).equals(getResources().getString(R.string.ctb_accepted)) ? "ACCEPTED" :
                "REJECTED"});

        observations.add(new String[]{"SPECIMEN UNSATISFACTORY FOR DIAGNOSIS", App.get(whySampleRejcted).equals(getResources().getString(R.string.ctb_saliva)) ? "SALIVA" :
                (App.get(whySampleRejcted).equals(getResources().getString(R.string.ctb_blood)) ? "BLOOD IN SAMPLE" :
                        (App.get(whySampleRejcted).equals(getResources().getString(R.string.ctb_older_than_3_days)) ? "SAMPLE OLDER THAN 3 DAYS" :
                                (App.get(whySampleRejcted).equals(getResources().getString(R.string.ctb_insufficient_qunatity)) ? "INSUFFICIENT QUANTITY" :
                                        (App.get(whySampleRejcted).equals(getResources().getString(R.string.ctb_food_particles)) ? "FOOD PARTICALS" :
                                                "OTHER REASON OF SAMPLE REJECTION"))))});

        if (reasonForRejection.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"OTHER REASON OF SAMPLE REJECTION", App.get(reasonForRejection)});
        }

        if (cartridgeId.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"CARTRIDGE ID", App.get(cartridgeId)});
        }
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

                String result = serverService.saveEncounterAndObservation("GXP Specimen Collection", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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

            if (obs[0][0].equals("SPECIMEN SUBMISSION DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                sampleSubmitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("TEST CONTEXT STATUS")) {
                String value = obs[0][1].equals("BASELINE") ? getResources().getString(R.string.ctb_baseline) :
                        (obs[0][1].equals("BASELINE REPEAT") ? getResources().getString(R.string.ctb_baseline_repeat) :
                                getResources().getString(R.string.ctb_follow_up_test));
                baselineRepeatFollowup.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("TB CATEGORY")) {
                for (RadioButton rb : patientCategory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_categoryI)) && obs[0][1].equals("CATEGORY I TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_categoryII)) && obs[0][1].equals("CATEGORY II TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON FOR BASELINE REPEAT TEST")) {
                String value = obs[0][1].equals("RIF RESISTANT POSITIVE") ? getResources().getString(R.string.ctb_rr_positive) :
                        (obs[0][1].equals("INVALID") ? getResources().getString(R.string.ctb_error_invalid_no_result) :
                                getResources().getString(R.string.ctb_indeterminate));
                reasonBaselineRepeat.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("SPECIMEN TYPE")) {
                for (RadioButton rb : specimenType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_sputum)) && obs[0][1].equals("SPUTUM")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SPECIMEN SOURCE")) {
                String value = obs[0][1].equals("LYMPHOCYTES") ? getResources().getString(R.string.ctb_lymph) :
                        (obs[0][1].equals("PLEURAL EFFUSION") ? getResources().getString(R.string.ctb_pleural_fluid) :
                                (obs[0][1].equals("PUS") ? getResources().getString(R.string.ctb_pus) :
                                        getResources().getString(R.string.ctb_other_title)));
                specimenComeFrom.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER SPECIMEN SOURCE")) {
                otherSpecimentComeFrom.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("SPECIMEN ACCEPTED")) {
                for (RadioButton rb : sampleAcceptedByTechnician.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_accepted)) && obs[0][1].equals("ACCEPTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rejected)) && obs[0][1].equals("REJECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SPECIMEN UNSATISFACTORY FOR DIAGNOSIS")) {
                String value = obs[0][1].equals("SALIVA") ? getResources().getString(R.string.ctb_saliva) :
                        (obs[0][1].equals("BLOOD IN SAMPLE") ? getResources().getString(R.string.ctb_blood) :
                                (obs[0][1].equals("SAMPLE OLDER THAN 3 DAYS") ? getResources().getString(R.string.ctb_older_than_3_days) :
                                        (obs[0][1].equals("INSUFFICIENT QUANTITY") ? getResources().getString(R.string.ctb_insufficient_qunatity) :
                                                (obs[0][1].equals("FOOD PARTICALS") ? getResources().getString(R.string.ctb_food_particles) :
                                                        getResources().getString(R.string.ctb_other_title)))));
                whySampleRejcted.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER REASON OF SAMPLE REJECTION")) {
                reasonForRejection.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("CARTRIDGE ID")) {
                cartridgeId.getEditText().setText(obs[0][1]);
            }
        }
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
        if (view == sampleSubmitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == baselineRepeatFollowup.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_baseline))) {
                patientCategory.setVisibility(View.VISIBLE);
            } else {
                patientCategory.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_baseline_repeat))) {
                reasonBaselineRepeat.setVisibility(View.VISIBLE);
            } else {
                reasonBaselineRepeat.setVisibility(View.GONE);
            }
        }
        if (spinner == specimenComeFrom.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);
            } else {
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if (spinner == whySampleRejcted.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                reasonForRejection.setVisibility(View.VISIBLE);
            } else {
                reasonForRejection.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        sampleSubmitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        reasonBaselineRepeat.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);
        whySampleRejcted.setVisibility(View.GONE);
        reasonForRejection.setVisibility(View.GONE);
        cartridgeId.setVisibility(View.GONE);
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
        if (group == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                specimenComeFrom.setVisibility(View.VISIBLE);

            } else {
                specimenComeFrom.setVisibility(View.GONE);
            }
        }
        if (group == sampleAcceptedByTechnician.getRadioGroup()) {
            if (sampleAcceptedByTechnician.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rejected))) {
                whySampleRejcted.setVisibility(View.VISIBLE);

            } else {
                whySampleRejcted.setVisibility(View.GONE);
            }
            if (sampleAcceptedByTechnician.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_accepted))) {
                cartridgeId.setVisibility(View.VISIBLE);
                reasonForRejection.setVisibility(View.GONE);


            } else {
                cartridgeId.setVisibility(View.GONE);
            }
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

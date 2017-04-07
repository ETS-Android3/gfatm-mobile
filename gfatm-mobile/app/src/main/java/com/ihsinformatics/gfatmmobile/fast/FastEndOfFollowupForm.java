package com.ihsinformatics.gfatmmobile.fast;

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
 * Created by Haris on 2/21/2017.
 */

public class FastEndOfFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledSpinner treatmentOutcome;
    TitledSpinner transferOutLocations;
    TitledEditText remarks;
    TitledRadioGroup treatmentInitiatedReferralSite;
    TitledSpinner treatmentNotInitiatedReferralSite;
    TitledEditText treatmentNotInitiatedReferralSiteOther;
    TitledRadioGroup drConfirmation;
    TitledEditText enrsId;
    TitledEditText firstName;
    TitledEditText lastName;
    LinearLayout mobileLinearLayout;
    TitledEditText mobile1;
    TitledEditText mobile2;

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
        FORM_NAME = Forms.FAST_END_OF_FOLLOWUP_FORM;
        FORM = Forms.fastEndOfFollowupForm;

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
        treatmentOutcome = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_treatment_outcome), getResources().getStringArray(R.array.fast_treatment_outcome_list), getResources().getString(R.string.fast_cured), App.VERTICAL);
        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(columnName);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }
        transferOutLocations = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_of_transfer_out), locationArray, "", App.VERTICAL);
        remarks = new TitledEditText(context, null, getResources().getString(R.string.fast_other_reason_remarks), "", "", 250, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        treatmentInitiatedReferralSite = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_treatment_initiated_at_transfer_referral_site), getResources().getStringArray(R.array.fast_yes_no_unknown_list), getResources().getString(R.string.fast_dont_know_title), App.VERTICAL, App.VERTICAL);
        treatmentNotInitiatedReferralSite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_treatment_not_initiated_at_referral_site), getResources().getStringArray(R.array.fast_reason_treatment_not_initiated_referral_site_list), getResources().getString(R.string.fast_patient_could_not_be_contacted), App.VERTICAL);
        treatmentNotInitiatedReferralSiteOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        drConfirmation = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_dr_confirmation), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        enrsId = new TitledEditText(context, null, getResources().getString(R.string.fast_enrs_number), "", "", 10, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        firstName = new TitledEditText(context, null, getResources().getString(R.string.fast_first_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        lastName = new TitledEditText(context, null, getResources().getString(R.string.fast_last_name), "", "", 10, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        mobileLinearLayout = new LinearLayout(context);
        mobileLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mobile1 = new TitledEditText(context, null, getResources().getString(R.string.fast_contact_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobile1);
        mobile2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        mobileLinearLayout.addView(mobile2);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), treatmentOutcome.getSpinner(), transferOutLocations.getSpinner(), remarks.getEditText()
                , treatmentInitiatedReferralSite.getRadioGroup(), treatmentNotInitiatedReferralSite.getSpinner(), treatmentNotInitiatedReferralSiteOther.getEditText(),
                drConfirmation.getRadioGroup(), enrsId.getEditText(), firstName.getEditText(), lastName.getEditText(), mobile1.getEditText(), mobile2.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentOutcome, transferOutLocations, remarks, treatmentInitiatedReferralSite, treatmentNotInitiatedReferralSite
                        , treatmentNotInitiatedReferralSiteOther, drConfirmation, enrsId, firstName, lastName, mobileLinearLayout}};

        formDate.getButton().setOnClickListener(this);
        treatmentOutcome.getSpinner().setOnItemSelectedListener(this);
        treatmentNotInitiatedReferralSite.getSpinner().setOnItemSelectedListener(this);
        treatmentInitiatedReferralSite.getRadioGroup().setOnCheckedChangeListener(this);
        drConfirmation.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        }
    }

    @Override
    public boolean validate() {
        Boolean error = false;


        if (remarks.getVisibility() == View.VISIBLE && App.get(remarks).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            remarks.getEditText().setError(getString(R.string.empty_field));
            remarks.getEditText().requestFocus();
            error = true;
        }

        if (enrsId.getVisibility() == View.VISIBLE && App.get(enrsId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            enrsId.getEditText().setError(getString(R.string.empty_field));
            enrsId.getEditText().requestFocus();
            error = true;
        }

        if (firstName.getVisibility() == View.VISIBLE && App.get(firstName).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            firstName.getEditText().setError(getString(R.string.empty_field));
            firstName.getEditText().requestFocus();
            error = true;
        }

        if (lastName.getVisibility() == View.VISIBLE && App.get(lastName).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            lastName.getEditText().setError(getString(R.string.empty_field));
            lastName.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile1).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile1.getEditText().setError(getString(R.string.empty_field));
            mobile1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile2).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.getEditText().setError(getString(R.string.empty_field));
            mobile2.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile1).length() != 4) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile1.getEditText().setError(getString(R.string.length_message));
            mobile1.getEditText().requestFocus();
            error = true;
        }

        if (App.get(mobile2).length() != 7) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.getEditText().setError(getString(R.string.length_message));
            mobile2.getEditText().requestFocus();
            error = true;
        }

        final String mobileNumber = mobile1.getEditText().getText().toString() + mobile2.getEditText().getText().toString();

        if (!RegexUtil.isMobileNumber(mobileNumber)) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            mobile2.getEditText().setError(getString(R.string.incorrect_contact_number));
            mobile2.getEditText().requestFocus();
            error = true;
        } else {
            mobile2.getEditText().setError(null);
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

        final String mobileNumber = mobile1.getEditText().getText().toString() + "-" + mobile2.getEditText().getText().toString();


        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});


        if (treatmentOutcome.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOUS TREATMENT OUTCOME", App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_cured)) ? "CURE, OUTCOME" :
                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_treatment_completed)) ? "TREATMENT COMPLETE" :
                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_died)) ? "DIED" :
                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_transfer_out)) ? "TRANSFERRED OUT" :
                                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_referral)) ? "PATIENT REFERRED" :
                                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.fast_treatment_after_loss_to_follow_up)) ? "LOST TO FOLLOW-UP" : "OTHER TREATMENT OUTCOME"))))))});


        if (transferOutLocations.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TRANSFER OUT LOCATION", App.get(transferOutLocations)});

        if (remarks.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON TO END FOLLOW UP", App.get(remarks)});

        if (treatmentInitiatedReferralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(treatmentInitiatedReferralSite).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(treatmentInitiatedReferralSite).equals(getResources().getString(R.string.fast_no_title)) ? "NO" : "UNKNOWN")});


        if (treatmentNotInitiatedReferralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_could_not_be_contacted)) ? "PATIENT COULD NOT BE CONTACTED" :
                    (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_left_the_city)) ? "PATIENT LEFT THE CITY" :
                            (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                    (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_patient_died)) ? "DIED" :
                                            (App.get(treatmentNotInitiatedReferralSite).equals(getResources().getString(R.string.fast_dr_not_confirmed_by_baseline_repeat_test)) ? "DR NOT CONFIRMED BY BASELINE REPEAT TEST" : "OTHER REASON FOR TREATMENT NOT INITIATED"))))});


        if (treatmentNotInitiatedReferralSiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR TREATMENT NOT INITIATED", App.get(treatmentNotInitiatedReferralSiteOther)});

        if (drConfirmation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DRUG RESISTANCE CONFIRMATION", App.get(drConfirmation).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (firstName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRAL CONTACT FIRST NAME", App.get(firstName)});

        if (lastName.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRAL CONTACT LAST NAME", App.get(lastName)});

        observations.add(new String[]{"REFERRAL CONTACT NUMBER", mobileNumber});

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

                String result = serverService.saveEncounterAndObservation("End of Followup", FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
                if (!result.contains("SUCCESS"))
                    return result;
                else {
                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    if (getEnrsVisibility()) {
                        result = serverService.saveIdentifier("ENRS", App.get(enrsId), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    }
                }

                return "SUCCESS";
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

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
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("TUBERCULOUS TREATMENT OUTCOME")) {
                String value = obs[0][1].equals("CURE, OUTCOME") ? getResources().getString(R.string.fast_cured) :
                        (obs[0][1].equals("TREATMENT COMPLETE") ? getResources().getString(R.string.fast_treatment_completed) :
                                (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.fast_treatment_failure) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_died) :
                                                (obs[0][1].equals("TRANSFERRED OUT") ? getResources().getString(R.string.fast_transfer_out) :
                                                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.fast_referral) :
                                                                (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.fast_treatment_after_loss_to_follow_up) :
                                                                        getResources().getString(R.string.fast_other_title)))))));

                treatmentOutcome.getSpinner().selectValue(value);
                treatmentOutcome.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TRANSFER OUT LOCATION")) {
                transferOutLocations.getSpinner().selectValue(obs[0][1]);
                transferOutLocations.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON TO END FOLLOW UP")) {
                remarks.getEditText().setText(obs[0][1]);
                remarks.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE")) {
                for (RadioButton rb : treatmentInitiatedReferralSite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_dont_know_title)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                treatmentInitiatedReferralSite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE")) {
                String value = obs[0][1].equals("PATIENT COULD NOT BE CONTACTED") ? getResources().getString(R.string.fast_cured) :
                        (obs[0][1].equals("PATIENT LEFT THE CITY") ? getResources().getString(R.string.fast_treatment_completed) :
                                (obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.fast_treatment_failure) :
                                        (obs[0][1].equals("DIED") ? getResources().getString(R.string.fast_died) :
                                                (obs[0][1].equals("DR NOT CONFIRMED BY BASELINE REPEAT TEST") ? getResources().getString(R.string.fast_transfer_out) :
                                                        getResources().getString(R.string.fast_other_title)))));

                treatmentNotInitiatedReferralSite.getSpinner().selectValue(value);
                treatmentNotInitiatedReferralSite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON FOR TREATMENT NOT INITIATED")) {
                treatmentNotInitiatedReferralSiteOther.getEditText().setText(obs[0][1]);
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DRUG RESISTANCE CONFIRMATION")) {
                for (RadioButton rb : drConfirmation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                drConfirmation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("ENRS")) {
                enrsId.getEditText().setText(obs[0][1]);
                enrsId.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRAL CONTACT FIRST NAME")) {
                firstName.getEditText().setText(obs[0][1]);
                firstName.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRAL CONTACT LAST NAME")) {
                lastName.getEditText().setText(obs[0][1]);
                lastName.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRAL CONTACT NUMBER")) {
                String mobileNumArr[] = obs[0][1].split("-");
                mobile1.getEditText().setText(mobileNumArr[0]);
                mobile2.getEditText().setText(mobileNumArr[1]);
                mobile1.setVisibility(View.VISIBLE);
                mobile2.setVisibility(View.VISIBLE);
                mobileLinearLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == treatmentOutcome.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_transfer_out))) {
                transferOutLocations.setVisibility(View.VISIBLE);
            } else {
                transferOutLocations.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_referral_new)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_loss_to_follow_up))) {
                remarks.setVisibility(View.VISIBLE);

            } else {
                remarks.setVisibility(View.GONE);
            }
        } else if (spinner == treatmentNotInitiatedReferralSite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
            } else {
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private boolean getEnrsVisibility(){
      if(enrsId.getVisibility() == View.VISIBLE){
          return true;
      }
        return false;
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        transferOutLocations.setVisibility(View.GONE);
        remarks.setVisibility(View.GONE);
        treatmentNotInitiatedReferralSite.setVisibility(View.GONE);
        treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
        enrsId.setVisibility(View.GONE);

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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == treatmentInitiatedReferralSite.getRadioGroup()) {
            if (treatmentInitiatedReferralSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                treatmentNotInitiatedReferralSite.setVisibility(View.VISIBLE);

            } else {
                treatmentNotInitiatedReferralSite.setVisibility(View.GONE);

            }
        } else if (radioGroup == drConfirmation.getRadioGroup()) {
            if (drConfirmation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                enrsId.setVisibility(View.VISIBLE);
            } else {
                enrsId.setVisibility(View.GONE);
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

package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class FastEndOfFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener{
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
        transferOutLocations = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_of_transfer_out),locationArray, "", App.VERTICAL);
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
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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

        if (!RegexUtil.isContactNumber(mobileNumber)) {
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

        endTime = new Date();
/*
        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});
        if (referralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", App.get(referralTransfer).equals(getResources().getString(R.string.fast_referral)) ? "PATIENT REFERRED" : "PATIENT TRANSFERRED OUT"});


        if (reasonReferralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR REFERRAL OR TRANSFER", App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_patient_choose_another_facility)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_drtb_suspect)) ? "MULTI-DRUG RESISTANT TUBERCULOSIS SUSPECTED" :
                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_drtb)) ? "DRUG RESISTANT TUBERCULOSIS" :
                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_complicated_tb)) ? "COMPLICATED TUBERCULOSIS" :
                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_mycobacterium_other_than_tb)) ? "MYCOBACTERIUM TUBERCULOSIS" : "OTHER TRANSFER OR REFERRAL REASON")))))});

        if (reasonReferralTransferOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER TRANSFER OR REFERRAL REASON", App.get(reasonReferralTransferOther)});

        if (referralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRING FACILITY NAME", referralSite.getSpinner().getSelectedItem().toString()});

        if (referralSiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LOCATION OF REFERRAL OR TRANSFER OTHER", App.get(referralSiteOther)});

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

                String result = serverService.saveEncounterAndObservation("Referral Form", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
        submissionFormTask.execute("");*/

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
    public void refill(int encounterId) {

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
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_referral_new))||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_patient_loss_to_follow_up))) {
                    remarks.setVisibility(View.VISIBLE);

            } else {
                remarks.setVisibility(View.GONE);
            }
        }

        else if(spinner == treatmentNotInitiatedReferralSite.getSpinner()){
            if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))){
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.VISIBLE);
            }
            else{
                treatmentNotInitiatedReferralSiteOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if(radioGroup == treatmentInitiatedReferralSite.getRadioGroup()){
            if(treatmentInitiatedReferralSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))){
                treatmentNotInitiatedReferralSite.setVisibility(View.VISIBLE);

            }
            else{
                treatmentNotInitiatedReferralSite.setVisibility(View.GONE);

            }
        }

        else if(radioGroup == drConfirmation.getRadioGroup()){
            if(drConfirmation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))){
                enrsId.setVisibility(View.VISIBLE);
            }
            else{
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

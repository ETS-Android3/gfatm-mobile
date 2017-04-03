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
import android.widget.CheckBox;
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

import org.openmrs.api.impl.VisitServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbEndOfFollowUp extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledSpinner treatmentOutcome;
    TitledEditText otherReasonRemarks;
    TitledEditText locationOfTransferOutReferral;
    TitledRadioGroup treatmentIntiatedAtReferralTransfer;
    TitledSpinner reasonTreatmentNotIntiated;
    TitledEditText otherReasonTreatmentNotIntiated;
    TitledRadioGroup drConfirmation;
    TitledEditText enrsNumber;
    TitledEditText firstName;
    TitledEditText lastName;
    TitledEditText contactNumber1;
    TitledEditText contactNumber2;
    LinearLayout contactNumberLayout;
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
        FORM_NAME = Forms.CHILDHOODTB_END_OF_FOLLOWUP;
        FORM = Forms.childhoodTb_end_of_followup;

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
        treatmentOutcome = new TitledSpinner(context,null,getResources().getString(R.string.ctb_treatment_outcome),getResources().getStringArray(R.array.ctb_treatment_outcome_list),getResources().getString(R.string.ctb_cured),App.HORIZONTAL,true);
        otherReasonRemarks = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_reason_remarks),"","",250,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        locationOfTransferOutReferral = new TitledEditText(context, null, getResources().getString(R.string.ctb_location_referral_transfer),"","",100,null,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        treatmentIntiatedAtReferralTransfer = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_treatment_intiated_at_referral_transfer),getResources().getStringArray(R.array.yes_no_unknown_options),getResources().getString(R.string.yes),App.HORIZONTAL,App.VERTICAL,true);
        reasonTreatmentNotIntiated = new TitledSpinner(context,null,getResources().getString(R.string.ctb_reason_treatment_not_intiated),getResources().getStringArray(R.array.ctb_reason_not_intiated_list),null,App.VERTICAL,true);
        otherReasonTreatmentNotIntiated = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_reason_not_intiated),"","",500,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        drConfirmation = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_dr_confirmation),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        enrsNumber = new TitledEditText(context,null,getResources().getString(R.string.ctb_enrs_number),"","",RegexUtil.idLength, RegexUtil.ERNS_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        firstName = new TitledEditText(context,getResources().getString(R.string.ctb_name_of_person_provided_details),getResources().getString(R.string.first_name),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        lastName = new TitledEditText(context,null,getResources().getString(R.string.last_name),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        contactNumberLayout = new LinearLayout(context);
        contactNumberLayout.setOrientation(LinearLayout.HORIZONTAL);
        contactNumber1 = new TitledEditText(context, null, getResources().getString(R.string.ctb_contact_number), "", "####", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        contactNumberLayout.addView(contactNumber1);
        contactNumber2 = new TitledEditText(context, null, "-", "", "#######", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        contactNumberLayout.addView(contactNumber2);


        views = new View[]{formDate.getButton(),treatmentOutcome.getSpinner(),otherReasonRemarks.getEditText(),treatmentIntiatedAtReferralTransfer.getRadioGroup(),
                reasonTreatmentNotIntiated.getSpinner(),otherReasonTreatmentNotIntiated.getEditText(),
                drConfirmation.getRadioGroup(),enrsNumber.getEditText(),firstName.getEditText(),lastName.getEditText(),
                contactNumber1.getEditText(),contactNumber2.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,treatmentOutcome,otherReasonRemarks,locationOfTransferOutReferral,treatmentIntiatedAtReferralTransfer,reasonTreatmentNotIntiated,otherReasonTreatmentNotIntiated
                        ,drConfirmation,enrsNumber,firstName,lastName,contactNumberLayout}};

        formDate.getButton().setOnClickListener(this);
        treatmentOutcome.getSpinner().setOnItemSelectedListener(this);
        //locationOfTransferOutReferral.getEditText().setOnKeyListener(null);
        treatmentIntiatedAtReferralTransfer.getRadioGroup().setOnCheckedChangeListener(this);
        reasonTreatmentNotIntiated.getSpinner().setOnItemSelectedListener(this);
        drConfirmation.getRadioGroup().setOnCheckedChangeListener(this);

        locationOfTransferOutReferral.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    treatmentIntiatedAtReferralTransfer.setVisibility(View.VISIBLE);
                }
            }
        });


        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


            Date date = new Date();


        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public boolean validate() {
        boolean error=false;
        if (App.get(contactNumber1).isEmpty() || App.get(contactNumber2).isEmpty() ) {
            contactNumber2.getEditText().setError(getString(R.string.empty_field));
            contactNumber2.getEditText().requestFocus();
            error = true;
        }
        String contactNumber = App.get(contactNumber1) + App.get(contactNumber2);
        if (!RegexUtil.isContactNumber(contactNumber)) {
            contactNumber2.getEditText().setError(getString(R.string.ctb_invalid_number));
            contactNumber2.getEditText().requestFocus();
            error = true;
        }
        if (App.get(firstName).isEmpty()) {
            firstName.getEditText().setError(getString(R.string.empty_field));
            firstName.getEditText().requestFocus();
            error = true;
        }
        if (App.get(lastName).isEmpty()) {
            lastName.getEditText().setError(getString(R.string.empty_field));
            lastName.getEditText().requestFocus();
            error = true;
        }
        if (App.get(drConfirmation).equalsIgnoreCase(getResources().getString(R.string.yes)) && App.get(enrsNumber).isEmpty()) {
            enrsNumber.getEditText().setError(getString(R.string.empty_field));
            enrsNumber.getEditText().requestFocus();
            error = true;
        }
        if(otherReasonTreatmentNotIntiated.getVisibility()==View.VISIBLE && App.get(otherReasonTreatmentNotIntiated).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherReasonTreatmentNotIntiated.getEditText().setError(getString(R.string.empty_field));
            otherReasonTreatmentNotIntiated.getEditText().requestFocus();
            error = true;
        }
        if (App.get(drConfirmation).equalsIgnoreCase(getResources().getString(R.string.yes)) && !App.get(enrsNumber).isEmpty()) {
            if (!RegexUtil.isValidErnsNumber(App.get(enrsNumber))) {
                enrsNumber.getEditText().setError(getString(R.string.invalid_value));
                enrsNumber.getEditText().requestFocus();
                error = true;
            }
            enrsNumber.clearFocus();
        }
        if(otherReasonRemarks.getVisibility()==View.VISIBLE && App.get(otherReasonRemarks).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherReasonRemarks.getEditText().setError(getString(R.string.empty_field));
            otherReasonRemarks.getEditText().requestFocus();
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
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        observations.add(new String[]{"TUBERCULOUS TREATMENT OUTCOME", App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_transfer_out)) ? "TRANSFERRED OUT" :
                (App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_cured)) ? "CURE, OUTCOME" :
                        (App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_treatment_completed)) ? "TREATMENT COMPLETE" :
                                (App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE":
                                     (App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_died)) ? "DIED" :
                                            (App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_referral)) ? "PATIENT REFERRED" :
                                                    (App.get(treatmentOutcome).equals(getResources().getString(R.string.ctb_lost_to_followup)) ? "LOST TO FOLLOW-UP" : "OTHER TREATMENT OUTCOME"))))))});

        if(otherReasonRemarks.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"OTHER REASON TO END FOLLOW UP", App.get(otherReasonRemarks)});
        }
        if(treatmentIntiatedAtReferralTransfer.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(treatmentIntiatedAtReferralTransfer).toUpperCase()});
        }
        if(reasonTreatmentNotIntiated.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE", App.get(reasonTreatmentNotIntiated).equals(getResources().getString(R.string.ctb_patient_can_not_contacted)) ? "PATIENT COULD NOT BE CONTACTED" :
                    (App.get(reasonTreatmentNotIntiated).equals(getResources().getString(R.string.ctb_patient_left_city)) ? "PATIENT LEFT THE CITY" :
                            (App.get(reasonTreatmentNotIntiated).equals(getResources().getString(R.string.ctb_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                    (App.get(reasonTreatmentNotIntiated).equals(getResources().getString(R.string.ctb_patient_died)) ? "DECEASED":
                                            (App.get(reasonTreatmentNotIntiated).equals(getResources().getString(R.string.ctb_dr_not_confirmed)) ? "DR NOT CONFIRMED BY BASELINE REPEAT TEST" :
                                                   "OTHER REASON FOR TREATMENT NOT INITIATED"))))});

        }

        if (otherReasonTreatmentNotIntiated.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"OTHER REASON FOR TREATMENT NOT INITIATED", App.get(otherReasonTreatmentNotIntiated)});
        }

        if (drConfirmation.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"DRUG RESISTANCE CONFIRMATION", App.get(drConfirmation).toUpperCase()});
        }
        if(!App.get(firstName).isEmpty()){
            observations.add(new String[]{"REFERRAL CONTACT FIRST NAME", App.get(firstName)});
        }
        if(!App.get(lastName).isEmpty()){
            observations.add(new String[]{"REFERRAL CONTACT LAST NAME", App.get(lastName)});
        }
        if(!App.get(contactNumber1).isEmpty() && !App.get(contactNumber2).isEmpty()){
            String contactNumber = App.get(contactNumber1) + "-" + App.get(contactNumber2);
            observations.add(new String[]{"REFERRAL CONTACT NUMBER", contactNumber});
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

                String result = serverService.saveEncounterAndObservation("End of Followup", FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    if (App.hasKeyListener(enrsNumber)) {
                        result = serverService.saveIdentifier("ENRS", App.get(enrsNumber), encounterId);
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
            if (obs[0][0].equals("TUBERCULOUS TREATMENT OUTCOME")) {
                String value = obs[0][1].equals("TRANSFERRED OUT") ? getResources().getString(R.string.ctb_transfer_out) :
                        (obs[0][1].equals("CURE, OUTCOME") ? getResources().getString(R.string.ctb_cured) :
                                (obs[0][1].equals("TREATMENT COMPLETE") ? getResources().getString(R.string.ctb_treatment_completed) :
                                        (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.ctb_treatment_failure) :
                                                (obs[0][1].equals("DIED") ? getResources().getString(R.string.ctb_died) :
                                                        (obs[0][1].equals("PATIENT REFERRED") ? getResources().getString(R.string.ctb_referral) :
                                                                (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.ctb_lost_to_followup) :
                                                                    getResources().getString(R.string.ctb_other_title)))))));
                if(value.equals(getResources().getString(R.string.ctb_transfer_out)) || value.equals(getResources().getString(R.string.ctb_referral))){
                    String referralTransferLocation = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Referral", "REFERRING FACILITY NAME");
                    if(referralTransferLocation.equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))){
                        String otherReferralTransferLocation = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Referral", "LOCATION OF REFERRAL OR TRANSFER OTHER");
                        locationOfTransferOutReferral.setVisibility(View.VISIBLE);
                        locationOfTransferOutReferral.getEditText().setText(otherReferralTransferLocation);
                    }
                    else{
                        locationOfTransferOutReferral.setVisibility(View.VISIBLE);
                        locationOfTransferOutReferral.getEditText().setText(referralTransferLocation);
                    }
                    locationOfTransferOutReferral.getEditText().setKeyListener(null);
                }
                treatmentOutcome.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER REASON TO END FOLLOW UP")) {
                otherReasonRemarks.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT INITIATED AT REFERRAL OR TRANSFER SITE")) {
                for (RadioButton rb : treatmentIntiatedAtReferralTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        reasonTreatmentNotIntiated.setVisibility(View.VISIBLE);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT NOT INITIATED AT REFERRAL OR TRANSFER SITE")) {
                String value = obs[0][1].equals("PATIENT COULD NOT BE CONTACTED") ? getResources().getString(R.string.ctb_patient_can_not_contacted) :
                        (obs[0][1].equals("PATIENT LEFT THE CITY") ? getResources().getString(R.string.ctb_patient_left_city) :
                                (obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.ctb_patient_refused_treatment) :
                                        (obs[0][1].equals("DECEASED") ? getResources().getString(R.string.ctb_patient_died) :
                                                (obs[0][1].equals("DR NOT CONFIRMED BY BASELINE REPEAT TEST") ? getResources().getString(R.string.ctb_dr_not_confirmed) :
                                                                        getResources().getString(R.string.ctb_other_title)))));
            } else if (obs[0][0].equals("OTHER REASON FOR TREATMENT NOT INITIATED")) {
                otherReasonTreatmentNotIntiated.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DRUG RESISTANCE CONFIRMATION")) {
                for (RadioButton rb : drConfirmation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("ENRS")) {
                enrsNumber.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("REFERRAL CONTACT FIRST NAME")) {
                firstName.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("REFERRAL CONTACT LAST NAME")) {
                lastName.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("REFERRAL CONTACT NUMBER")) {
                String[] contactNumber = obs[0][1].split("-");
                contactNumber1.getEditText().setText(contactNumber[0]);
                contactNumber2.getEditText().setText(contactNumber[1]);
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
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == treatmentOutcome.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_referral)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_lost_to_followup))){
                otherReasonRemarks.setVisibility(View.VISIBLE);
            } else {
                otherReasonRemarks.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_referral)) || parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_transfer_out))) {
                String referralTransferLocation = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Referral", "REFERRING FACILITY NAME");
                locationOfTransferOutReferral.setVisibility(View.VISIBLE);
                locationOfTransferOutReferral.getEditText().setKeyListener(null);
                if(referralTransferLocation!=null) {
                    if (referralTransferLocation.equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))) {
                        String otherReferralTransferLocation = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Referral", "LOCATION OF REFERRAL OR TRANSFER OTHER");
                        locationOfTransferOutReferral.getEditText().setText(otherReferralTransferLocation);
                    } else {
                        locationOfTransferOutReferral.getEditText().setText(referralTransferLocation);
                    }
                }
            } else {
                locationOfTransferOutReferral.setVisibility(View.GONE);
            }
        }else if(spinner == reasonTreatmentNotIntiated.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherReasonTreatmentNotIntiated.setVisibility(View.VISIBLE);
            } else {
                otherReasonTreatmentNotIntiated.setVisibility(View.GONE);
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
        otherReasonRemarks.setVisibility(View.GONE);
        locationOfTransferOutReferral.setVisibility(View.GONE);
        treatmentIntiatedAtReferralTransfer.setVisibility(View.GONE);
        reasonTreatmentNotIntiated.setVisibility(View.GONE);
        otherReasonTreatmentNotIntiated.setVisibility(View.GONE);
        enrsNumber.setVisibility(View.GONE);
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
        if (group == treatmentIntiatedAtReferralTransfer.getRadioGroup()) {
            if (treatmentIntiatedAtReferralTransfer.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.no)) || treatmentIntiatedAtReferralTransfer.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.unknown))) {
                reasonTreatmentNotIntiated.setVisibility(View.VISIBLE);
            } else {
                reasonTreatmentNotIntiated.setVisibility(View.GONE);
            }
        }
        if (group == drConfirmation.getRadioGroup()) {
            if (drConfirmation.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                enrsNumber.setVisibility(View.VISIBLE);
            } else {
                enrsNumber.setVisibility(View.GONE);
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

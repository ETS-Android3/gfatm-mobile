package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.ihsinformatics.gfatmmobile.model.Address;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.model.User;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.Metadata;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Tahira on 2/28/2017.
 */

public class PmdtPatientAssignmentForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, AutoCompleteTextView.OnClickListener, View.OnTouchListener {

    Context context;
    TitledButton formDate;
    String personUuid = "";

    TitledEditText treatmentSupporterId;
    ImageView validateTreatmentSupporterIdView;
    TitledEditText treatmentSupporterFirstName;
    TitledEditText treatmentSupporterLastName;

    TitledEditText address1;
    TitledEditText address2;
    TitledEditText addressCityVillage;
    TitledEditText addressDistrict;
    TitledEditText addressLandmark;

    TitledRadioGroup isHouseholdMember;
    TitledSpinner relationshipWithPatient;
    TitledEditText otherRelationship;
    TitledRadioGroup reasonHouseholdMember;
    TitledEditText otherReasonHouseholdMember;

    ScrollView scrollView;

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 1;
        FORM_NAME = Forms.PMDT_PATIENT_ASSIGNMENT;
        FORM = Forms.pmdtPatientAssignment;

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        treatmentSupporterId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_treatment_supporter_id), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        treatmentSupporterId.setLayoutParams(param);
        linearLayout.addView(treatmentSupporterId);
        validateTreatmentSupporterIdView = new ImageView(context);
        validateTreatmentSupporterIdView.setImageResource(R.drawable.ic_search);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        validateTreatmentSupporterIdView.setLayoutParams(param1);
        validateTreatmentSupporterIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        validateTreatmentSupporterIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(validateTreatmentSupporterIdView);

        treatmentSupporterFirstName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_first_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName.setFocusableInTouchMode(true);
        treatmentSupporterLastName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_last_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterLastName.setFocusableInTouchMode(true);
        address1 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_address_one), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        address2 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_address_two), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressDistrict = new TitledEditText(context, "", getResources().getString(R.string.pmdt_address_district), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        addressCityVillage = new TitledEditText(context, "", getResources().getString(R.string.pmdt_address_city_village), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        addressLandmark = new TitledEditText(context, "", getResources().getString(R.string.pmdt_address_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        isHouseholdMember = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_treatment_supporter_household_member), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        relationshipWithPatient = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_treatment_supporter_patient_relationship), getResources().getStringArray(R.array.pmdt_treatment_supporter_patient_relation), getResources().getString(R.string.pmdt_father), App.VERTICAL);
        otherRelationship = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_relationship), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonHouseholdMember = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_having_treatment_supporter), getResources().getStringArray(R.array.pmdt_reasons_for_having_treatment_supporter), getResources().getString(R.string.pmdt_family_refused_treatment_supporter), App.VERTICAL, App.VERTICAL);
        otherReasonHouseholdMember = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_reason_having_treatment_supporter), "", "", 255, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        views = new View[]{formDate.getButton(), treatmentSupporterId.getEditText(), treatmentSupporterFirstName.getEditText(), treatmentSupporterLastName.getEditText(),
                address1.getEditText(), address2.getEditText(), addressCityVillage.getEditText(), /* Taluka, if required*/ addressLandmark.getEditText(),
                isHouseholdMember.getRadioGroup(), relationshipWithPatient.getSpinner(), otherRelationship.getEditText(),
                otherReasonHouseholdMember.getEditText(), otherReasonHouseholdMember.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, linearLayout, treatmentSupporterFirstName, treatmentSupporterLastName, address1, address2,
                        addressCityVillage, addressLandmark, isHouseholdMember, relationshipWithPatient,
                        otherRelationship, reasonHouseholdMember, otherReasonHouseholdMember}};

        formDate.getButton().setOnClickListener(this);

        isHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);
        reasonHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);

        treatmentSupporterFirstName.getEditText().setKeyListener(null);
        treatmentSupporterLastName.getEditText().setKeyListener(null);
        address1.getEditText().setKeyListener(null);
        address2.getEditText().setKeyListener(null);
        addressCityVillage.getEditText().setKeyListener(null);
        addressDistrict.getEditText().setKeyListener(null);
        addressLandmark.getEditText().setKeyListener(null);
        validateTreatmentSupporterIdView.setOnTouchListener(this);
        relationshipWithPatient.getSpinner().setOnItemSelectedListener(this);
        reasonHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();

        treatmentSupporterId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
//                    if (treatmentSupporterId.getEditText().getText().length() == 7) {
//                        if (!RegexUtil.isValidTreatmentSupporterId(App.get(treatmentSupporterId))) {
//                            treatmentSupporterId.getEditText().setError("id incorrect");
//                            validateTreatmentSupporterIdView.setVisibility(View.INVISIBLE);
//                        } else {
//                            validateTreatmentSupporterIdView.setVisibility(View.VISIBLE);
//                            validateTreatmentSupporterIdView.setImageResource(R.drawable.ic_search);
//                        }
//                    } else {
//                        validateTreatmentSupporterIdView.setVisibility(View.INVISIBLE);
//                    }


                } catch (NumberFormatException nfe) {
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private AdapterView.OnItemClickListener autoItemSelectedListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


        }
    };

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(treatmentSupporterId).isEmpty()) {
            treatmentSupporterId.getEditText().setError(getResources().getString(R.string.mandatory_field));
            treatmentSupporterId.getEditText().requestFocus();
            error = true;
        }

        if (App.get(treatmentSupporterFirstName).isEmpty()) {
            treatmentSupporterFirstName.getEditText().setError(getResources().getString(R.string.mandatory_field));
            treatmentSupporterFirstName.getEditText().requestFocus();
            error = true;
        }

        if (App.get(treatmentSupporterLastName).isEmpty()) {
            treatmentSupporterLastName.getEditText().setError(getResources().getString(R.string.mandatory_field));
            treatmentSupporterLastName.getEditText().requestFocus();
            error = true;
        }

        if (otherRelationship.getVisibility() == View.VISIBLE && App.get(otherRelationship).isEmpty()) {
            otherRelationship.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherRelationship.getEditText().requestFocus();
            error = true;
        }

        if (otherReasonHouseholdMember.getVisibility() == View.VISIBLE && App.get(otherReasonHouseholdMember).isEmpty()) {
            otherReasonHouseholdMember.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherReasonHouseholdMember.getEditText().requestFocus();
            error = true;

        }

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                            } catch (Exception e) {

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

        observations.add(new String[]{"TREATMENT SUPPORTER ID", App.get(treatmentSupporterId)});

        observations.add(new String[]{"TREATMENT SUPPORTER FIRST NAME", App.get(treatmentSupporterFirstName)});

        observations.add(new String[]{"TREATMENT SUPPORTER LAST NAME", App.get(treatmentSupporterLastName)});


        observations.add(new String[]{"", App.get(isHouseholdMember).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

        observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_mother)) ? "MOTHER" :
                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_father)) ? "FATHER" :
                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_brother)) ? "BROTHER" :
                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_sister)) ? "SISTER" :
                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_son)) ? "SON" :
                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_daughter)) ? "DAUGHTER" :
                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_spouse)) ? "SPOUSE" :
                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_aunt)) ? "AUNT" :
                                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_uncle)) ? "UNCLE" :
                                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_nephew)) ? "NEPHEW" :
                                                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_niece)) ? "NIECE" :
                                                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_grandson)) ? "GRANDSON" :
                                                                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_granddaughter)) ? "GRANDDAUGHTER" :
                                                                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_father_in_law)) ? "FATHER-IN-LAW" :
                                                                                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_mother_in_law)) ? "MOTHER-IN-LAW" :
                                                                                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_daughter_in_law)) ? "DAUGHTER IN LAW" :
                                                                                                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_son_in_law)) ? "SON IN LAW" :
                                                                                                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_brother_in_law)) ? "BROTHER IN LAW" :
                                                                                                                                                                                        (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_sister_in_law)) ? "SISTER IN LAW" :
                                                                                                                                                                                                (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_cousin)) ? "COUSIN" :
                                                                                                                                                                                                        "OTHER FAMILY MEMBER")))))))))))))))))))))))});


        if (otherRelationship.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(otherRelationship)});

        if (reasonHouseholdMember.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR HOUSEHOLD TREATMENT SUPPORTER", App.get(reasonHouseholdMember).equals(getResources().getString(R.string.pmdt_family_refused_treatment_supporter)) ? "FAMILY-REFUSED NON-HOUSEHOLD TREATMENT SUPPORTER" :
                    (App.get(reasonHouseholdMember).equals(getResources().getString(R.string.pmdt_patient_refused_treatment_supporter)) ? "PATIENT-REFUSED NON-HOUSEHOLD TREATMENT SUPPORTER" :
                            (App.get(reasonHouseholdMember).equals(getResources().getString(R.string.pmdt_unable_to_appoint_treatment_supporter)) ? "NO INCENTIVE FOR TREATMENT SUPPORTER" : "OTHER REASON FOR HOUSEHOLD TREATMENT SUPPORTER"))});

        if (otherReasonHouseholdMember.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR HOUSEHOLD TREATMENT SUPPORTER", App.get(otherReasonHouseholdMember)});


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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);

                if (!result.contains("SUCCESS"))
                    return result;
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    String treatmentSupporterName = App.get(treatmentSupporterFirstName) + " " + App.get(treatmentSupporterLastName);

                    result = serverService.savePersonAttributeType("Treatment Supporter", treatmentSupporterName, encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;
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
        return false;
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
    public void refill(int encounterId) {

        OfflineForm offlineForm = serverService.getOfflineFormById(encounterId);
        String date = offlineForm.getFormDate();
        ArrayList<String[][]> obsValue = offlineForm.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("TREATMENT SUPPORTER ID")) {
                treatmentSupporterId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER FIRST NAME")) {
                treatmentSupporterFirstName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER LAST NAME")) {
                treatmentSupporterFirstName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("")) {
                for (RadioButton rb : isHouseholdMember.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pmdt_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.pmdt_father) :
                                (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pmdt_maternal_grandmother) :
                                        (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pmdt_maternal_grandfather) :
                                                (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pmdt_paternal_grandmother) :
                                                        (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pmdt_paternal_grandfather) :
                                                                (obs[0][1].equals("GRANDSON") ? getResources().getString(R.string.pmdt_grandson) :
                                                                        (obs[0][1].equals("GRANDDAUGHTER") ? getResources().getString(R.string.pmdt_granddaughter) :
                                                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.pmdt_brother) :
                                                                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.pmdt_sister) :
                                                                                                (obs[0][1].equals("SON") ? getResources().getString(R.string.pmdt_son) :
                                                                                                        obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.pmdt_daughter) :
                                                                                                                obs[0][1].equals("NEPHEW") ? getResources().getString(R.string.pmdt_nephew) :
                                                                                                                        obs[0][1].equals("NIECE") ? getResources().getString(R.string.pmdt_niece) :
                                                                                                                                obs[0][1].equals("FATHER-IN-LAW") ? getResources().getString(R.string.pmdt_father_in_law) :
                                                                                                                                        obs[0][1].equals("MOTHER-IN-LAW") ? getResources().getString(R.string.pmdt_mother_in_law) :
                                                                                                                                                obs[0][1].equals("DAUGHTER IN LAW") ? getResources().getString(R.string.pmdt_daughter_in_law) :
                                                                                                                                                        obs[0][1].equals("SON IN LAW") ? getResources().getString(R.string.pmdt_son_in_law) :
                                                                                                                                                                obs[0][1].equals("BROTHER IN LAW") ? getResources().getString(R.string.pmdt_brother_in_law) :
                                                                                                                                                                        obs[0][1].equals("SISTER IN LAW") ? getResources().getString(R.string.pmdt_sister_in_law) :
                                                                                                                                                                                obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.pmdt_spouse) :
                                                                                                                                                                                        obs[0][1].equals("COUSIN") ? getResources().getString(R.string.pmdt_cousin) :
                                                                                                                                                                                                obs[0][1].equals("AUNT") ? getResources().getString(R.string.pmdt_aunt) :
                                                                                                                                                                                                        obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pmdt_uncle) : getResources().getString(R.string.pmdt_other)))))))))));

                relationshipWithPatient.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                otherRelationship.getEditText().setText(obs[0][1]);
                otherRelationship.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR HOUSEHOLD TREATMENT SUPPORTER")) {
                for (RadioButton rb : reasonHouseholdMember.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pmdt_family_refused_treatment_supporter)) && obs[0][1].equals("FAMILY-REFUSED NON-HOUSEHOLD TREATMENT SUPPORTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_patient_refused_treatment_supporter)) && obs[0][1].equals("PATIENT-REFUSED NON-HOUSEHOLD TREATMENT SUPPORTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_unable_to_appoint_treatment_supporter)) && obs[0][1].equals("NO INCENTIVE FOR TREATMENT SUPPORTER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_other)) && obs[0][1].equals("OTHER REASON FOR HOUSEHOLD TREATMENT SUPPORTER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER REASON FOR HOUSEHOLD TREATMENT SUPPORTER")) {
                otherReasonHouseholdMember.getEditText().setText(obs[0][1]);
                otherReasonHouseholdMember.setVisibility(View.VISIBLE);
            }
        }
        submitButton.setEnabled(true);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == relationshipWithPatient.getSpinner()) {
            if (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_other)))
                otherRelationship.setVisibility(View.VISIBLE);
            else
                otherRelationship.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == isHouseholdMember.getRadioGroup()) {
            if (App.get(isHouseholdMember).equals(getResources().getString(R.string.yes))) {
                relationshipWithPatient.setVisibility(View.VISIBLE);

                if (App.get(relationshipWithPatient).equals(getResources().getString(R.string.pmdt_other)))
                    otherRelationship.setVisibility(View.VISIBLE);
                else
                    otherRelationship.setVisibility(View.GONE);

                reasonHouseholdMember.setVisibility(View.VISIBLE);

                if (App.get(reasonHouseholdMember).equals(getResources().getString(R.string.pmdt_other)))
                    otherReasonHouseholdMember.setVisibility(View.VISIBLE);
                else
                    otherReasonHouseholdMember.setVisibility(View.GONE);

            } else {
                relationshipWithPatient.setVisibility(View.GONE);
                otherRelationship.setVisibility(View.GONE);

                reasonHouseholdMember.setVisibility(View.GONE);
                otherReasonHouseholdMember.setVisibility(View.GONE);

            }
        } else if (group == reasonHouseholdMember.getRadioGroup()) {
            if (App.get(reasonHouseholdMember).equals(getResources().getString(R.string.pmdt_other)))
                otherReasonHouseholdMember.setVisibility(View.VISIBLE);
            else
                otherReasonHouseholdMember.setVisibility(View.GONE);

        }

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        submitButton.setEnabled(false);

        String assignedTreatmentSupporter = App.getPatient().getPerson().getTreatmentSupporter();

        if (!assignedTreatmentSupporter.isEmpty()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.pmdt_treatment_supporter_already_assigned));
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

                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            submitButton.setEnabled(false);
            return;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean error = false;

                if (App.get(treatmentSupporterId).isEmpty()) {
                    treatmentSupporterId.getEditText().setError(getString(R.string.empty_field));
                    treatmentSupporterId.requestFocus();
                    error = true;
                }
//                else if (!RegexUtil.isValidTreatmentSupporterId(App.get(treatmentSupporterId))) {
//                    treatmentSupporterId.getEditText().setError(getString(R.string.invalid_id));
//                    treatmentSupporterId.requestFocus();
//                    error = true;
//                }

                if (!error) {
                    final String username = App.get(treatmentSupporterId).trim();


                    final AsyncTask<String, String, HashMap<String, String>> autopopulateFormTask = new AsyncTask<String, String, HashMap<String, String>>() {
                        @Override
                        protected HashMap<String, String> doInBackground(String... params) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setInverseBackgroundForced(true);
                                    loading.setIndeterminate(true);
                                    loading.setCancelable(false);
                                    loading.setMessage(getResources().getString(R.string.fetching_data));
                                    loading.show();
                                }
                            });


                            HashMap<String, String> result = new HashMap<String, String>();
                            User treatmentUser = serverService.getUser(username);

                            if (treatmentUser != null && !treatmentUser.getRoles().isEmpty()) {

                                String roles = treatmentUser.getRoles();
                                if (roles.contains("PMDT Treatment Supporter")) {

                                    result.put("TREATMENT SUPPORTER FIRST NAME", treatmentUser.getFullName().split(" ")[0]);
                                    result.put("TREATMENT SUPPORTER LAST NAME", treatmentUser.getFullName().split(" ")[1]);

                                    if (!treatmentUser.getPersonUuid().isEmpty()) {
                                        Address preferredAddress = serverService.getPreferredAddressByPersonUuid(treatmentUser.getPersonUuid());

                                        if (preferredAddress != null) {

                                            result.put("ADDRESS1", preferredAddress.getAddress1());
                                            result.put("ADDRESS2", preferredAddress.getAddress2());
                                            result.put("CITY_VILLAGE", preferredAddress.getCityVillage());
                                            result.put("DISTRICT", preferredAddress.getCountyDistrict());
                                            result.put("LANDMARK", preferredAddress.getAddress3());
                                        }
                                    }
                                }
                            }

                            return result;
                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                        }


                        @Override
                        protected void onPostExecute(HashMap<String, String> result) {
                            super.onPostExecute(result);
                            loading.dismiss();

                            if (result.get("TREATMENT SUPPORTER FIRST NAME") == null || result.get("TREATMENT SUPPORTER FIRST NAME").isEmpty()) {

                                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                                alertDialog.setMessage(getResources().getString(R.string.pmdt_treatment_supporter_missing));
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

                                                }
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

                                submitButton.setEnabled(false);
                                return;
                            }


                            if (result.get("TREATMENT SUPPORTER FIRST NAME") != null || !result.get("TREATMENT SUPPORTER FIRST NAME").isEmpty()) {
                                submitButton.setEnabled(true);
                                treatmentSupporterFirstName.getEditText().setText(result.get("TREATMENT SUPPORTER FIRST NAME"));
                            }

                            if (result.get("TREATMENT SUPPORTER LAST NAME") != null || !result.get("TREATMENT SUPPORTER LAST NAME").isEmpty())
                                treatmentSupporterLastName.getEditText().setText(result.get("TREATMENT SUPPORTER LAST NAME"));

                            if (result.get("ADDRESS1") != null && !result.get("ADDRESS1").isEmpty())
                                address1.getEditText().setText(result.get("ADDRESS1"));

                            if (result.get("ADDRESS2") != null && !result.get("ADDRESS2").isEmpty())
                                address2.getEditText().setText(result.get("ADDRESS2"));

                            if (result.get("DISTRICT") != null && !result.get("DISTRICT").isEmpty())
                                addressDistrict.getEditText().setText(result.get("DISTRICT"));

                            if (result.get("CITY_VILLAGE") != null && !result.get("CITY_VILLAGE").isEmpty())
                                addressCityVillage.getEditText().setText(result.get("CITY_VILLAGE"));

                            if (result.get("LANDMARK") != null && !result.get("LANDMARK").isEmpty())
                                addressLandmark.getEditText().setText(result.get("LANDMARK"));

                        }
                    };
                    autopopulateFormTask.execute("");
                }

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
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

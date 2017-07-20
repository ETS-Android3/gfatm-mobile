package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbScreeningLocation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup screeningReferral;
    TitledSpinner referralSource;
    TitledRadioGroup facilityDepartment;
    TitledEditText otherFacilityDeparment;
    TitledSpinner referralWithinOpd;
    TitledSpinner referralOutsideOpd;
    TitledEditText referralOutsideOther;
    TitledSpinner hearAboutUs;
    TitledEditText hearAboutUsOther;
    TitledRadioGroup patientEnrolledTb;
    TitledCheckBoxes contactIdType;
    TitledEditText contactPatientId;
    TitledEditText contactExternalId;
    TitledEditText contactExternalIdHospital;
    TitledEditText contactTbRegistrationNo;
    boolean screeningReferralBoolean=false;
    Button scanQRCode;
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
        FORM_NAME = Forms.CHILDHOOD_SCREENING_LOCATION;
        FORM = Forms.childhoodTb_screeningLocation;

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

        screeningReferral = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_screening_referral),getResources().getStringArray(R.array.ctb_screening_referral_list),getResources().getString(R.string.ctb_screening),App.HORIZONTAL,App.VERTICAL,true);
        referralSource = new TitledSpinner(context,null,getResources().getString(R.string.ctb_patient_referral_source),getResources().getStringArray(R.array.ctb_patient_referral_source_list),null,App.VERTICAL);
        facilityDepartment = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_facility_department),getResources().getStringArray(R.array.ctb_facility_department_list),getResources().getString(R.string.ctb_opd_clinic),App.HORIZONTAL,App.VERTICAL);
        otherFacilityDeparment = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        referralWithinOpd = new TitledSpinner(context,null,getResources().getString(R.string.ctb_referral_inside),getResources().getStringArray(R.array.ctb_opd_ward_section_list),null,App.VERTICAL);
        referralOutsideOpd = new TitledSpinner(context,null,getResources().getString(R.string.ctb_referral_outside),getResources().getStringArray(R.array.ctb_referral_outside_list),null,App.VERTICAL);
        referralOutsideOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        hearAboutUs = new TitledSpinner(context,null,getResources().getString(R.string.ctb_hear_about_us),getResources().getStringArray(R.array.ctb_hear_about_us_list),null,App.VERTICAL);
        hearAboutUsOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        patientEnrolledTb = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_patient_enrolled_tb),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL);
        contactIdType = new TitledCheckBoxes(context,null,getResources().getString(R.string.ctb_contact_id_type),getResources().getStringArray(R.array.ctb_patient_type_id_list),null,App.VERTICAL,App.VERTICAL);
        contactPatientId = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_patient_id),"","",7,RegexUtil.ID_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        contactExternalId = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_external_id),"","",20,RegexUtil.EXTERNAL_ID_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        contactExternalIdHospital = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_external_id_hospital),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        contactTbRegistrationNo = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_tb_registration_no),"","",20,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);



        views = new View[]{formDate.getButton(),screeningReferral.getRadioGroup(),referralSource.getSpinner(),facilityDepartment.getRadioGroup(),
                otherFacilityDeparment.getEditText(),referralWithinOpd.getSpinner(),referralOutsideOpd.getSpinner(),referralOutsideOther.getEditText(),
                hearAboutUs.getSpinner(),hearAboutUsOther.getEditText(),patientEnrolledTb.getRadioGroup(),contactIdType,contactPatientId.getEditText(),contactExternalId.getEditText(),contactExternalIdHospital.getEditText(),contactTbRegistrationNo.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,screeningReferral,referralSource,facilityDepartment,otherFacilityDeparment,referralWithinOpd,referralOutsideOpd,referralOutsideOther,hearAboutUs,hearAboutUsOther,patientEnrolledTb,contactIdType,contactPatientId,scanQRCode,contactExternalId,contactExternalIdHospital,contactTbRegistrationNo}};

        formDate.getButton().setOnClickListener(this);
        scanQRCode.setOnClickListener(this);
        screeningReferral.getRadioGroup().setOnCheckedChangeListener(this);
        referralSource.getSpinner().setOnItemSelectedListener(this);
        facilityDepartment.getRadioGroup().setOnCheckedChangeListener(this);
        referralWithinOpd.getSpinner().setOnItemSelectedListener(this);
        referralOutsideOpd.getSpinner().setOnItemSelectedListener(this);
        hearAboutUs.getSpinner().setOnItemSelectedListener(this);
        patientEnrolledTb.getRadioGroup().setOnCheckedChangeListener(this);
        for (CheckBox cb : contactIdType.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

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
            }else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public boolean validate() {
        boolean error=false;
        if(contactPatientId.getVisibility()==View.VISIBLE && !App.get(contactPatientId).isEmpty()){
            boolean check = RegexUtil.isValidId(App.get(contactPatientId));
            if(check==false) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                contactPatientId.getEditText().setError(getString(R.string.invalid_id));
                contactPatientId.requestFocus();
                error = true;
            }
            String patientID = App.getPatient().getPatientId();
            if(patientID.equalsIgnoreCase(App.get(contactPatientId))){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                contactPatientId.getEditText().setError(getString(R.string.ctb_duplicate_contact_id));
                contactPatientId.requestFocus();
                error = true;
            }
        }
        if(facilityDepartment.getVisibility()==View.VISIBLE && App.get(facilityDepartment).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            facilityDepartment.getRadioGroup().getButtons().get(2).setError(getString(R.string.empty_field));
            facilityDepartment.getRadioGroup().requestFocus();
            error = true;
        }
        if(contactTbRegistrationNo.getVisibility()==View.VISIBLE && App.get(contactTbRegistrationNo).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactTbRegistrationNo.getEditText().setError(getString(R.string.empty_field));
            contactTbRegistrationNo.getEditText().requestFocus();
            error = true;
        }
        if(contactExternalIdHospital.getVisibility()==View.VISIBLE && App.get(contactExternalIdHospital).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactExternalIdHospital.getEditText().setError(getString(R.string.empty_field));
            contactExternalIdHospital.getEditText().requestFocus();
            error = true;
        }
        if(contactExternalId.getVisibility()==View.VISIBLE && App.get(contactExternalId).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactExternalId.getEditText().setError(getString(R.string.empty_field));
            contactExternalId.getEditText().requestFocus();
            error = true;
        }
        if(contactPatientId.getVisibility()==View.VISIBLE && App.get(contactPatientId).isEmpty()){
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactPatientId.getEditText().setError(getString(R.string.empty_field));
            contactPatientId.getEditText().requestFocus();
            error = true;
        }


        if (otherFacilityDeparment.getVisibility() == View.VISIBLE ) {
            if(App.get(otherFacilityDeparment).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherFacilityDeparment.getEditText().setError(getString(R.string.empty_field));
                otherFacilityDeparment.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(otherFacilityDeparment).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherFacilityDeparment.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherFacilityDeparment.getEditText().requestFocus();
                error = true;
            }
        }
        if (referralOutsideOther.getVisibility() == View.VISIBLE) {
            if(App.get(referralOutsideOther).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                referralOutsideOther.getEditText().setError(getString(R.string.empty_field));
                referralOutsideOther.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(referralOutsideOther).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                referralOutsideOther.getEditText().setError(getString(R.string.ctb_spaces_only));
                referralOutsideOther.getEditText().requestFocus();
                error = true;
            }
        }
        if (hearAboutUsOther.getVisibility() == View.VISIBLE) {
            if(App.get(hearAboutUsOther).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                hearAboutUsOther.getEditText().setError(getString(R.string.empty_field));
                hearAboutUsOther.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(hearAboutUsOther).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                hearAboutUsOther.getEditText().setError(getString(R.string.ctb_spaces_only));
                hearAboutUsOther.getEditText().requestFocus();
                error = true;
            }
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
        observations.add(new String[]{"PATIENT IDENTIFY THROUGH SCREENING OR REFERRAL", App.get(screeningReferral).equals(getResources().getString(R.string.ctb_screening)) ? "IDENTIFIED PATIENT THROUGH SCREENING" : "PATIENT REFERRED"});

        if (referralSource.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"PATIENT REFERRAL SOURCE", App.get(referralSource).equals(getResources().getString(R.string.ctb_doctor_healthworker_in_hospital)) ? "CLINICAL OFFICER/DOCTOR" :
                    (App.get(referralSource).equals(getResources().getString(R.string.ctb_doctor_healthworker_out_hospital)) ? "PRIVATE PRACTIONER" :
                            (App.get(referralSource).equals(getResources().getString(R.string.ctb_child_tested_for_tb)) ? "SELF" : "TUBERCULOSIS CONTACT"))});
        }

        if (facilityDepartment.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"HEALTH FACILITY DEPARTMENT", App.get(facilityDepartment).equals(getResources().getString(R.string.ctb_opd_clinic)) ? "OUTPATIENT DEPARTMENT" :
                    (App.get(facilityDepartment).equals(getResources().getString(R.string.ctb_ward)) ? "OBSERVATION WARD" : "OTHER FACILITY DEPARTMENT")});
        }

        if (otherFacilityDeparment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY DEPARTMENT", App.get(otherFacilityDeparment)});

        if (referralWithinOpd.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OUTPATIENT DEPARTMENT", App.get(referralWithinOpd).equals(getResources().getString(R.string.ctb_general_medicine_filter_clinic)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(referralWithinOpd).equals(getResources().getString(R.string.ctb_chest_tb_clinic_screening)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(referralWithinOpd).equals(getResources().getString(R.string.ctb_paediatrics)) ? "PEDIATRIC SURGERY DEPARTMENT" :
                                    (App.get(referralWithinOpd).equals(getResources().getString(R.string.ctb_gynae_obstetrics)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                          (App.get(referralWithinOpd).equals(getResources().getString(R.string.ctb_er)) ? "EMERGENCY DEPARTMENT": "SURGICAL PROCEDURE"))))});

        if (referralOutsideOpd.getVisibility() == View.VISIBLE) {
            observations.add(new String[]{"TYPE OF HEALTHCARE FACILITY", App.get(referralOutsideOpd).equals(getResources().getString(R.string.ctb_public_hospital)) ? "GOVERNMENT FACILITY" :
                    (App.get(referralOutsideOpd).equals(getResources().getString(R.string.ctb_private_hospital)) ? "PRIVATE FACILITY" :
                            (App.get(referralOutsideOpd).equals(getResources().getString(R.string.ctb_gp)) ? "GENERAL PRACTITIONER" : "REFERRAL OUTSIDE OTHER"))});
        }

        if(referralOutsideOther.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"REFERRAL OUTSIDE OTHER", App.get(referralOutsideOther)});
        }

        if(hearAboutUs.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"HEAR ABOUT US", App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_radio)) ? "RADIO, AS A HOUSEHOLD ITEM" :
                    (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_tv)) ? "TV" :
                            (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_newspaper)) ? "NEWSPAPER" :
                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_billbaord_signboard)) ? "BILLBOARD" :
                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_internet)) ? "INTERNET CONNECTION":
                                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_sms)) ? "SMS" :
                                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_form_a_friend_community_member)) ? "FRIEND" :
                                                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_school_awareness_program)) ? "SCHOOL AWARENESS PROGRAM" :
                                                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.ctb_call_center)) ? "CALL CENTER": "HEAR ABOUT US OTHER"))))))))});

        }

        if(hearAboutUsOther.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"HEAR ABOUT US OTHER", App.get(hearAboutUsOther)});
        }

        if(patientEnrolledTb.getVisibility() == View.VISIBLE && !App.get(patientEnrolledTb).isEmpty()){
            observations.add(new String[]{"PATIENT ENROLLED", App.get(patientEnrolledTb).toUpperCase()});
        }

        if(contactIdType.getVisibility() == View.VISIBLE){
            String contactIdString = "";
            for (CheckBox cb : contactIdType.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_patient_id)))
                    contactIdString = contactIdString + "CONTACT PATIENT ID" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_external_id)))
                    contactIdString = contactIdString + "CONTACT EXTERNAL ID" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_tb_registration_no)))
                    contactIdString = contactIdString + "TB REGISTRATION NUMBER" + " ; ";
            }
            observations.add(new String[]{"CONTACT ID TYPE", contactIdString});
        }
        if(contactPatientId.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"CONTACT PATIENT ID", App.get(contactPatientId)});
        }
        if(contactExternalId.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"CONTACT EXTERNAL ID", App.get(contactExternalId)});
        }
        if(contactExternalIdHospital.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"CONTACT FACILITY NAME", App.get(contactExternalIdHospital)});
        }
        if(contactTbRegistrationNo.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(contactTbRegistrationNo)});
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

                String result = serverService.saveEncounterAndObservation("Screening Location", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if (obs[0][0].equals("PATIENT IDENTIFY THROUGH SCREENING OR REFERRAL")) {
                for (RadioButton rb : screeningReferral.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_screening)) && obs[0][1].equals("IDENTIFIED PATIENT THROUGH SCREENING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_reffered)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        referralSource.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT REFERRAL SOURCE")) {
                String value = obs[0][1].equals("CLINICAL OFFICER/DOCTOR") ? getResources().getString(R.string.ctb_doctor_healthworker_in_hospital) :
                        (obs[0][1].equals("PRIVATE PRACTIONER") ? getResources().getString(R.string.ctb_doctor_healthworker_out_hospital) :
                                (obs[0][1].equals("SELF") ? getResources().getString(R.string.ctb_child_tested_for_tb) :
                                        getResources().getString(R.string.ctb_family_member_tb_patient)));
                referralSource.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("HEALTH FACILITY DEPARTMENT")) {
                for (RadioButton rb : facilityDepartment.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_opd_clinic)) && obs[0][1].equals("OUTPATIENT DEPARTMENT")) {
                        rb.setChecked(true);
                        referralWithinOpd.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_ward)) && obs[0][1].equals("OBSERVATION WARD")) {
                        rb.setChecked(true);
                        referralWithinOpd.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER FACILITY DEPARTMENT")) {
                        rb.setChecked(true);
                        otherFacilityDeparment.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER FACILITY DEPARTMENT")) {
                otherFacilityDeparment.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("OUTPATIENT DEPARTMENT")) {
                String value = obs[0][1].equals("GENERAL MEDICINE DEPARTMENT") ? getResources().getString(R.string.ctb_general_medicine_filter_clinic) :
                        (obs[0][1].equals("CHEST MEDICINE DEPARTMENT") ? getResources().getString(R.string.ctb_chest_tb_clinic_screening) :
                                (obs[0][1].equals("PEDIATRIC SURGERY DEPARTMENT") ? getResources().getString(R.string.ctb_paediatrics) :
                                        (obs[0][1].equals("OBSTETRICS AND GYNECOLOGY DEPARTMENT") ? getResources().getString(R.string.ctb_gynae_obstetrics) :
                                                (obs[0][1].equals("EMERGENCY DEPARTMENT") ? getResources().getString(R.string.ctb_er) :
                                                        getResources().getString(R.string.ctb_surgery)))));
                referralWithinOpd.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("TYPE OF HEALTHCARE FACILITY")) {
                String value = obs[0][1].equals("GOVERNMENT FACILITY") ? getResources().getString(R.string.ctb_public_hospital) :
                        (obs[0][1].equals("PRIVATE FACILITY") ? getResources().getString(R.string.ctb_private_hospital) :
                                (obs[0][1].equals("GENERAL PRACTITIONER") ? getResources().getString(R.string.ctb_gp) :
                                                        getResources().getString(R.string.ctb_other_title)));
                referralOutsideOpd.getSpinner().selectValue(value);
                if(value==getResources().getString(R.string.ctb_other_title)){
                    referralOutsideOther.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("REFERRAL OUTSIDE OTHER")) {
                referralOutsideOther.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HEAR ABOUT US")) {
                String value = obs[0][1].equals("RADIO, AS A HOUSEHOLD ITEM") ? getResources().getString(R.string.ctb_radio) :
                        (obs[0][1].equals("TV") ? getResources().getString(R.string.ctb_tv) :
                                (obs[0][1].equals("NEWSPAPER") ? getResources().getString(R.string.ctb_newspaper) :
                                        (obs[0][1].equals("BILLBOARD") ? getResources().getString(R.string.ctb_billbaord_signboard) :
                                                (obs[0][1].equals("INTERNET CONNECTION") ? getResources().getString(R.string.ctb_internet) :
                                                        (obs[0][1].equals("SMS") ? getResources().getString(R.string.ctb_sms) :
                                                                (obs[0][1].equals("FRIEND") ? getResources().getString(R.string.ctb_form_a_friend_community_member) :
                                                                        (obs[0][1].equals("SCHOOL AWARENESS PROGRAM") ? getResources().getString(R.string.ctb_school_awareness_program) :
                                                                                (obs[0][1].equals("CALL CENTER") ? getResources().getString(R.string.ctb_call_center) :
                                                                                        getResources().getString(R.string.ctb_other_title)))))))));
                hearAboutUs.getSpinner().selectValue(value);
                if(value==getResources().getString(R.string.ctb_other_title)){
                    hearAboutUsOther.setVisibility(View.VISIBLE);
                }
            } else if (obs[0][0].equals("HEAR ABOUT US OTHER")) {
                hearAboutUsOther.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT ENROLLED")) {
                for (RadioButton rb : patientEnrolledTb.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        contactIdType.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CONTACT ID TYPE")) {
                for (CheckBox cb : contactIdType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_patient_id)) && obs[0][1].equals("CONTACT PATIENT ID")) {
                        contactPatientId.setVisibility(View.VISIBLE);
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_external_id)) && obs[0][1].equals("CONTACT EXTERNAL ID")) {
                        contactExternalId.setVisibility(View.VISIBLE);
                        contactExternalIdHospital.setVisibility(View.VISIBLE);
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_tb_registration_no)) && obs[0][1].equals("TB REGISTRATION NUMBER")) {
                        contactTbRegistrationNo.setVisibility(View.VISIBLE);
                        cb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("CONTACT PATIENT ID")) {
                contactPatientId.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("CONTACT EXTERNAL ID")) {
                contactExternalId.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("CONTACT FACILITY NAME")) {
                contactExternalIdHospital.getEditText().setText(obs[0][1]);
            }else if (obs[0][0].equals("TB REGISTRATION NUMBER")) {
                contactTbRegistrationNo.getEditText().setText(obs[0][1]);
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
        }else if (view == scanQRCode) {
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(context, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == referralSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_doctor_healthworker_in_hospital)) && screeningReferralBoolean==true) {
                facilityDepartment.setVisibility(View.VISIBLE);
                if(App.get(facilityDepartment).equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))) {
                    otherFacilityDeparment.setVisibility(View.GONE);
                }
                referralWithinOpd.setVisibility(View.VISIBLE);

                referralOutsideOpd.setVisibility(View.GONE);
                referralOutsideOther.setVisibility(View.GONE);
                hearAboutUs.setVisibility(View.GONE);
                hearAboutUsOther.setVisibility(View.GONE);
                patientEnrolledTb.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegistrationNo.setVisibility(View.GONE);

            } else if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_doctor_healthworker_out_hospital))) {
                referralOutsideOpd.setVisibility(View.VISIBLE);
                if(App.get(referralOutsideOpd).equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))){
                    referralOutsideOther.setVisibility(View.VISIBLE);
                }

                facilityDepartment.setVisibility(View.GONE);
                otherFacilityDeparment.setVisibility(View.GONE);
                referralWithinOpd.setVisibility(View.GONE);
                hearAboutUs.setVisibility(View.GONE);
                hearAboutUsOther.setVisibility(View.GONE);
                patientEnrolledTb.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegistrationNo.setVisibility(View.GONE);
            } else if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_child_tested_for_tb))) {
                hearAboutUs.setVisibility(View.VISIBLE);
                if(App.get(hearAboutUs).equalsIgnoreCase(getResources().getString(R.string.ctb_other_title))){
                    hearAboutUsOther.setVisibility(View.VISIBLE);
                }

                facilityDepartment.setVisibility(View.GONE);
                otherFacilityDeparment.setVisibility(View.GONE);
                referralWithinOpd.setVisibility(View.GONE);
                referralOutsideOpd.setVisibility(View.GONE);
                referralOutsideOther.setVisibility(View.GONE);
                patientEnrolledTb.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegistrationNo.setVisibility(View.GONE);
            } else if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_family_member_tb_patient))) {
                patientEnrolledTb.getRadioGroup().clearCheck();
                patientEnrolledTb.setVisibility(View.VISIBLE);

                facilityDepartment.setVisibility(View.GONE);
                otherFacilityDeparment.setVisibility(View.GONE);
                referralWithinOpd.setVisibility(View.GONE);
                referralOutsideOpd.setVisibility(View.GONE);
                referralOutsideOther.setVisibility(View.GONE);
                hearAboutUs.setVisibility(View.GONE);
                hearAboutUsOther.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegistrationNo.setVisibility(View.GONE);
            }
        }
        if (spinner == referralOutsideOpd.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                referralOutsideOther.setVisibility(View.VISIBLE);
            } else {
                referralOutsideOther.setVisibility(View.GONE);
            }
        }
        if (spinner == hearAboutUs.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title)) & hearAboutUs.getVisibility()==View.VISIBLE) {
                hearAboutUsOther.setVisibility(View.VISIBLE);
            } else {
                hearAboutUsOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : contactIdType.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_patient_id))) {
                if (cb.isChecked()) {
                    contactPatientId.setVisibility(View.VISIBLE);
                    scanQRCode.setVisibility(View.VISIBLE);
                }
                else{
                    contactPatientId.setVisibility(View.GONE);
                    scanQRCode.setVisibility(View.GONE);
                }
            }

            if(App.get(cb).equals(getResources().getString(R.string.ctb_external_id))) {
                if (cb.isChecked()) {
                    contactExternalId.setVisibility(View.VISIBLE);
                    contactExternalIdHospital.setVisibility(View.VISIBLE);
                }
                else{
                    contactExternalId.setVisibility(View.GONE);
                    contactExternalIdHospital.setVisibility(View.GONE);
                }
            }
            if(App.get(cb).equals(getResources().getString(R.string.ctb_tb_registration_no))) {
                if (cb.isChecked()) {
                    contactTbRegistrationNo.setVisibility(View.VISIBLE);
                }
                else{
                    contactTbRegistrationNo.setVisibility(View.GONE);
                }
            }

        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    contactPatientId.getEditText().setText(str);
                    contactPatientId.getEditText().requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    contactPatientId.getEditText().setText("");
                    contactPatientId.getEditText().requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(context, R.attr.colorAccent);

                contactPatientId.getEditText().setText("");
                contactPatientId.getEditText().requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            context.getResources().updateConfiguration(config, null);
        }
    }
    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        referralSource.setVisibility(View.GONE);
        otherFacilityDeparment.setVisibility(View.GONE);
        referralWithinOpd.setVisibility(View.GONE);
        referralOutsideOpd.setVisibility(View.GONE);
        facilityDepartment.setVisibility(View.GONE);
        referralOutsideOther.setVisibility(View.GONE);
        hearAboutUs.setVisibility(View.GONE);
        hearAboutUsOther.setVisibility(View.GONE);
        patientEnrolledTb.setVisibility(View.GONE);
        contactIdType.setVisibility(View.GONE);
        contactPatientId.setVisibility(View.GONE);
        contactExternalId.setVisibility(View.GONE);
        contactExternalIdHospital.setVisibility(View.GONE);
        contactTbRegistrationNo.setVisibility(View.GONE);
        scanQRCode.setVisibility(View.GONE);
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
        if (group == screeningReferral.getRadioGroup()) {
            if(screeningReferral.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_reffered))){
                referralSource.setVisibility(View.VISIBLE);
                facilityDepartment.setVisibility(View.VISIBLE);
                screeningReferralBoolean = true;
                referralSource.getSpinner().selectValue(getResources().getString(R.string.ctb_doctor_healthworker_in_hospital));
                if(App.get(facilityDepartment).equals(getResources().getString(R.string.ctb_opd_clinic)) || App.get(facilityDepartment).equals(getResources().getString(R.string.ctb_ward))){
                    referralWithinOpd.setVisibility(View.VISIBLE);
                }
                if(App.get(facilityDepartment).equals(getResources().getString(R.string.ctb_other_title))){
                    otherFacilityDeparment.setVisibility(View.VISIBLE);
                }

            }
            else{
                referralWithinOpd.setVisibility(View.GONE);
                referralSource.setVisibility(View.GONE);
                facilityDepartment.setVisibility(View.GONE);
                otherFacilityDeparment.setVisibility(View.GONE);
                referralOutsideOpd.setVisibility(View.GONE);
                referralOutsideOther.setVisibility(View.GONE);
                hearAboutUs.setVisibility(View.GONE);
                hearAboutUsOther.setVisibility(View.GONE);
                patientEnrolledTb.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegistrationNo.setVisibility(View.GONE);
                screeningReferralBoolean = false;
            }

        }
        if (group == facilityDepartment.getRadioGroup()) {
            if(facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_opd_clinic)) || facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_ward))){
                referralWithinOpd.setVisibility(View.VISIBLE);
            }
            else{
                referralWithinOpd.setVisibility(View.GONE);
            }

            if(facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))){
                otherFacilityDeparment.setVisibility(View.VISIBLE);
            }
            else{
                otherFacilityDeparment.setVisibility(View.GONE);
            }
        }
        if (group == patientEnrolledTb.getRadioGroup()) {
            if (patientEnrolledTb.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                contactIdType.setVisibility(View.VISIBLE);
            } else {
                contactIdType.setVisibility(View.GONE);
                contactIdType.getCheckedBoxes().get(0).setChecked(false);
                contactIdType.getCheckedBoxes().get(1).setChecked(false);
                contactIdType.getCheckedBoxes().get(2).setChecked(false);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegistrationNo.setVisibility(View.GONE);
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

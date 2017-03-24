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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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
        facilityDepartment = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_facility_department),getResources().getStringArray(R.array.ctb_facility_department_list),null,App.HORIZONTAL,App.VERTICAL);
        otherFacilityDeparment = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        referralWithinOpd = new TitledSpinner(context,null,getResources().getString(R.string.ctb_referral_inside),getResources().getStringArray(R.array.ctb_opd_ward_section_list),null,App.VERTICAL);
        referralOutsideOpd = new TitledSpinner(context,null,getResources().getString(R.string.ctb_referral_outside),getResources().getStringArray(R.array.ctb_referral_outside_list),null,App.VERTICAL);
        referralOutsideOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        hearAboutUs = new TitledSpinner(context,null,getResources().getString(R.string.ctb_hear_about_us),getResources().getStringArray(R.array.ctb_hear_about_us_list),null,App.VERTICAL);
        hearAboutUsOther = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",20,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        patientEnrolledTb = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_patient_enrolled_tb),getResources().getStringArray(R.array.yes_no_options),null,App.HORIZONTAL,App.VERTICAL);
        contactIdType = new TitledCheckBoxes(context,null,getResources().getString(R.string.ctb_contact_id_type),getResources().getStringArray(R.array.ctb_patient_type_id_list),null,App.VERTICAL,App.VERTICAL);
        contactPatientId = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_patient_id),"","",20,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        contactExternalId = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_external_id),"","",20,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        contactExternalIdHospital = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_external_id_hospital),"","",20,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        contactTbRegistrationNo = new TitledEditText(context,null,getResources().getString(R.string.ctb_contact_tb_registration_no),"","",20,RegexUtil.ALPHANUMERIC_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);



        views = new View[]{formDate.getButton(),screeningReferral.getRadioGroup(),referralSource.getSpinner(),facilityDepartment.getRadioGroup(),otherFacilityDeparment.getEditText(),referralWithinOpd.getSpinner(),referralOutsideOpd.getSpinner(),referralOutsideOther.getEditText(),hearAboutUs.getSpinner(),hearAboutUsOther.getEditText(),patientEnrolledTb.getRadioGroup(),contactIdType,contactPatientId.getEditText(),contactExternalId.getEditText(),contactExternalIdHospital.getEditText(),contactTbRegistrationNo.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,screeningReferral,referralSource,facilityDepartment,otherFacilityDeparment,referralWithinOpd,referralOutsideOpd,referralOutsideOther,hearAboutUs,hearAboutUsOther,patientEnrolledTb,contactIdType,contactPatientId,contactExternalId,contactExternalIdHospital,contactTbRegistrationNo}};

        formDate.getButton().setOnClickListener(this);
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

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            Date date = new Date();
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


        if (otherFacilityDeparment.getVisibility() == View.VISIBLE && App.get(otherFacilityDeparment).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherFacilityDeparment.getEditText().setError(getString(R.string.empty_field));
            otherFacilityDeparment.getEditText().requestFocus();
            error = true;
        }
        if (referralOutsideOther.getVisibility() == View.VISIBLE && App.get(referralOutsideOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            referralOutsideOther.getEditText().setError(getString(R.string.empty_field));
            referralOutsideOther.getEditText().requestFocus();
            error = true;
        }
        if (hearAboutUsOther.getVisibility() == View.VISIBLE && App.get(hearAboutUsOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            hearAboutUsOther.getEditText().setError(getString(R.string.empty_field));
            hearAboutUsOther.getEditText().requestFocus();
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

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
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

                String result = serverService.saveEncounterAndObservation("Screening Location", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == referralSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_doctor_healthworker_in_hospital)) && screeningReferralBoolean==true) {
                patientEnrolledTb.getRadioGroup().clearCheck();
                facilityDepartment.setVisibility(View.VISIBLE);
                hearAboutUs.getSpinner().setSelection(0);
                referralOutsideOpd.getSpinner().setSelection(0);
            } else {
                facilityDepartment.setVisibility(View.GONE);
            }
            if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_doctor_healthworker_out_hospital))) {
                patientEnrolledTb.getRadioGroup().clearCheck();
                facilityDepartment.getRadioGroup().clearCheck();
                hearAboutUs.getSpinner().setSelection(0);
                referralOutsideOpd.setVisibility(View.VISIBLE);
            } else {
                referralOutsideOpd.setVisibility(View.GONE);
            }
            if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_child_tested_for_tb))) {
                patientEnrolledTb.getRadioGroup().clearCheck();
                facilityDepartment.getRadioGroup().clearCheck();
                referralOutsideOpd.getSpinner().setSelection(0);
                hearAboutUs.setVisibility(View.VISIBLE);
            } else {
                hearAboutUs.setVisibility(View.GONE);

            }
            if(parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_family_member_tb_patient))) {
                facilityDepartment.getRadioGroup().clearCheck();
                hearAboutUs.getSpinner().setSelection(0);
                referralOutsideOpd.getSpinner().setSelection(0);
                patientEnrolledTb.setVisibility(View.VISIBLE);
            } else {
                patientEnrolledTb.setVisibility(View.GONE);
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
                }
                else{
                    contactPatientId.setVisibility(View.GONE);
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
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        referralSource.setVisibility(View.GONE);
        otherFacilityDeparment.setVisibility(View.GONE);
        referralWithinOpd.setVisibility(View.GONE);
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
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == screeningReferral.getRadioGroup()) {
            if(screeningReferral.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_reffered))){
                referralSource.setVisibility(View.VISIBLE);
                screeningReferralBoolean = true;
                referralSource.getSpinner().setSelection(0);
            }
            else{
                referralSource.setVisibility(View.GONE);
                facilityDepartment.setVisibility(View.GONE);
                otherFacilityDeparment.setVisibility(View.GONE);
                referralWithinOpd.setVisibility(View.GONE);
                referralOutsideOpd.setVisibility(View.GONE);
                referralOutsideOther.setVisibility(View.GONE);
                hearAboutUs.setVisibility(View.GONE);
                hearAboutUsOther.setVisibility(View.GONE);
                patientEnrolledTb.setVisibility(View.GONE);
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

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
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

/**
 * Created by Haris on 1/13/2017.
 */

public class FastPatientLocationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup screening;
    TitledSpinner facilitySection;
    TitledEditText facilitySectionOther;
    TitledSpinner opdWardSection;
    TitledSpinner patientReferralSource;
    TitledEditText otherReferral;
    TitledRadioGroup facilityDepartment;
    TitledEditText facilityDepartmentOther;
    TitledSpinner referralWithinOpd;
    TitledEditText referralWithinOpdOther;
    TitledRadioGroup facilityType;
    TitledSpinner hearAboutUs;
    TitledEditText hearAboutUsOther;
    TitledRadioGroup contactReferral;
    TitledCheckBoxes contactIdType;
    TitledEditText contactPatientId;
    TitledEditText contactExternalId;
    TitledSpinner contactExternalIdHospital;
    TitledEditText contactTbRegisternationNo;
    CheckBox patientIdCheckbox;
    CheckBox externalIdCheckbox;
    CheckBox tbRegisterationNoCheckbbox;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.FAST_PATIENT_LOCATION_FORM;
        FORM = Forms.fastPatientLocationForm;

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
        screening = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_identified_patient_through_screening), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        facilitySection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_hospital_parts_title), getResources().getStringArray(R.array.fast_hospital_parts), getResources().getString(R.string.fast_opdclinicscreening_title), App.VERTICAL);
        facilitySectionOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        opdWardSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_clinic_and_ward_title), getResources().getStringArray(R.array.fast_clinic_and_ward_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        patientReferralSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_source_of_patient_referral), getResources().getStringArray(R.array.fast_source_of_patient_referral_list), getResources().getString(R.string.fast_self_referral), App.VERTICAL);
        otherReferral = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        facilityDepartment = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_within_hospital_was_patient_referred), getResources().getStringArray(R.array.fast_patient_reffered_from_list), getResources().getString(R.string.fast_ward_title), App.VERTICAL, App.VERTICAL);
        facilityDepartmentOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        referralWithinOpd = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_which_opd_clinic_or_ward_has_patient_referred_from), getResources().getStringArray(R.array.fast_opd_clinic_or_ward_patient_reffered_from_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        referralWithinOpdOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        facilityType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_outside_hospital_was_patient_referred), getResources().getStringArray(R.array.fast_outside_hospital_patient_reffered_from), getResources().getString(R.string.fast_private_hospital), App.VERTICAL, App.VERTICAL);
        hearAboutUs = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_where_did_you_hear_about_us), getResources().getStringArray(R.array.fast_hear_about_us_from_list), getResources().getString(R.string.fast_radio), App.VERTICAL);
        hearAboutUsOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_enrolled_in_any_tb_program), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        contactIdType = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_tb_contact_any_identifications_id), getResources().getStringArray(R.array.fast_tb_contact_identification_list), null, App.VERTICAL, App.VERTICAL);
        contactPatientId = new TitledEditText(context, null, getResources().getString(R.string.fast_patient_id), "", "", 6, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactExternalId = new TitledEditText(context, null, getResources().getString(R.string.fast_external_id), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

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

        contactExternalIdHospital = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_external_id_hospital_or_programs), locationArray, "", App.VERTICAL);
        contactTbRegisternationNo = new TitledEditText(context, null, getResources().getString(R.string.fast_tb_registeration_no), "", "", 11, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), facilitySection.getSpinner(), facilitySectionOther.getEditText(),
                opdWardSection.getSpinner(), patientReferralSource.getSpinner(), otherReferral.getEditText(),
                facilityDepartment.getRadioGroup(), facilityDepartmentOther.getEditText(), referralWithinOpd.getSpinner(), referralWithinOpdOther.getEditText(),
                facilityType.getRadioGroup(), hearAboutUs.getSpinner(), hearAboutUsOther.getEditText(), contactReferral.getRadioGroup(), contactIdType, contactPatientId.getEditText(), contactExternalId.getEditText(), contactExternalIdHospital.getSpinner(),
                contactTbRegisternationNo.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screening, facilitySection, facilitySectionOther, opdWardSection, patientReferralSource, otherReferral, facilityDepartment, facilityDepartmentOther, referralWithinOpd, referralWithinOpdOther, facilityType, hearAboutUs, hearAboutUsOther, contactReferral, contactIdType, contactPatientId, contactExternalId, contactExternalIdHospital, contactTbRegisternationNo},};

        formDate.getButton().setOnClickListener(this);
        screening.getRadioGroup().setOnCheckedChangeListener(this);
        facilitySection.getSpinner().setOnItemSelectedListener(this);
        opdWardSection.getSpinner().setOnItemSelectedListener(this);
        patientReferralSource.getSpinner().setOnItemSelectedListener(this);
        facilityDepartment.getRadioGroup().setOnCheckedChangeListener(this);
        referralWithinOpd.getSpinner().setOnItemSelectedListener(this);
        facilityType.getRadioGroup().setOnCheckedChangeListener(this);
        hearAboutUs.getSpinner().setOnItemSelectedListener(this);
        contactReferral.getRadioGroup().setOnCheckedChangeListener(this);


        ArrayList<MyCheckBox> checkBoxList = contactIdType.getCheckedBoxes();
        patientIdCheckbox = checkBoxList.get(0);
        externalIdCheckbox = checkBoxList.get(1);
        tbRegisterationNoCheckbbox = checkBoxList.get(2);

        patientIdCheckbox.setOnCheckedChangeListener(this);
        externalIdCheckbox.setOnCheckedChangeListener(this);
        tbRegisterationNoCheckbbox.setOnCheckedChangeListener(this);

        contactExternalId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!(charSequence.toString().equals("")) && contactIdType.getVisibility() == View.VISIBLE
                        && externalIdCheckbox.isChecked()) {
                    contactExternalIdHospital.setVisibility(View.VISIBLE);
                } else {
                    contactExternalIdHospital.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        resetViews();
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (facilitySectionOther.getVisibility() == View.VISIBLE && App.get(facilitySectionOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            facilitySectionOther.getEditText().setError(getString(R.string.empty_field));
            facilitySectionOther.getEditText().requestFocus();
            error = true;
        }

        if (otherReferral.getVisibility() == View.VISIBLE && App.get(otherReferral).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherReferral.getEditText().setError(getString(R.string.empty_field));
            otherReferral.getEditText().requestFocus();
            error = true;
        }

        if (facilityDepartmentOther.getVisibility() == View.VISIBLE && App.get(facilityDepartmentOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            facilityDepartmentOther.getEditText().setError(getString(R.string.empty_field));
            facilityDepartmentOther.getEditText().requestFocus();
            error = true;
        }

        if (referralWithinOpdOther.getVisibility() == View.VISIBLE && App.get(referralWithinOpd).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            referralWithinOpdOther.getEditText().setError(getString(R.string.empty_field));
            referralWithinOpdOther.getEditText().requestFocus();
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

        if (contactPatientId.getVisibility() == View.VISIBLE && App.get(contactPatientId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactPatientId.getEditText().setError(getString(R.string.empty_field));
            contactPatientId.getEditText().requestFocus();
            error = true;
        }

        if (contactExternalId.getVisibility() == View.VISIBLE && App.get(contactExternalId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactExternalId.getEditText().setError(getString(R.string.empty_field));
            contactExternalId.getEditText().requestFocus();
            error = true;
        }

        if (contactTbRegisternationNo.getVisibility() == View.VISIBLE && App.get(contactTbRegisternationNo).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contactTbRegisternationNo.getEditText().setError(getString(R.string.empty_field));
            contactTbRegisternationNo.getEditText().requestFocus();
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
      /*  observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"IDENTIFIED PATIENT THROUGH SCREENING", App.get(screening).equals(getResources().getString(R.string.fast_patient_title)) ? "YES" : "NO"});

        if (facilitySection.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HOSPITAL SECTION", App.get(facilitySection).equals(getResources().getString(R.string.fast_opdclinicscreening_title)) ? "OPD CLINIC SCREENING" :
                    (App.get(facilitySection).equals(getResources().getString(R.string.fast_wardscreening_title)) ? "WARD SCREENING" :
                            (App.get(facilitySection).equals(getResources().getString(R.string.fast_registrationdesk_title)) ? "REGISTRATION DESK" :
                                    (App.get(facilitySection).equals(getResources().getString(R.string.fast_nexttoxrayvan_title)) ? "X-RAY VAN" : "OTHER FACILITY SECTION")))});

        //need to be resolved
        if (facilitySectionOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY SECTION", App.get(facilitySectionOther)});


        if (opdWardSection.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OUTPATIENT DEPARTMENT", App.get(opdWardSection).equals(getResources().getString(R.string.fast_generalmedicinefilterclinic_title)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(opdWardSection).equals(getResources().getString(R.string.fast_chesttbclinic_title)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(opdWardSection).equals(getResources().getString(R.string.fast_gynaeobstetrics_title)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                    (App.get(opdWardSection).equals(getResources().getString(R.string.fast_surgery_title)) ? "PEDIATRIC SURGERY DEPARTMENT" : "EMERGENCY DEPARTMENT")))});


        if (patientReferralSource.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT REFERRAL SOURCE", App.get(patientReferralSource).equals(getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital)) ? "CLINICAL OFFICER/DOCTOR" :
                    (App.get(patientReferralSource).equals(getResources().getString(R.string.fast_doctor_or_health_worker_outside_the_hospital)) ? "PRIVATE PRACTIONER" :
                            (App.get(patientReferralSource).equals(getResources().getString(R.string.fast_self_referral)) ? "SELF" :
                                    (App.get(patientReferralSource).equals(getResources().getString(R.string.fast_contact_of_tb_patient)) ? "TUBERCULOSIS CONTACT" :
                                            (App.get(patientReferralSource).equals(getResources().getString(R.string.fast_referred_after_xray_cad4tb)) ? "CD4 COUNT" : "OTHER PATIENT REFERRAL SOURCE"))))});

        if (otherReferral.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PATIENT REFERRAL SOURCE", App.get(otherReferral)});


        if (facilityDepartment.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEALTH FACILITY DEPARTMENT", App.get(facilityDepartment).equals(getResources().getString(R.string.fast_ward_title)) ? "OUTPATIENT DEPARTMENT" :
                    (App.get(facilityDepartment).equals(getResources().getString(R.string.fast_opd_clinic)) ? "OBSERVATION WARD" : "OTHER FACILITY DEPARTMENT")});

        if (facilityDepartmentOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY DEPARTMENT", App.get(facilityDepartmentOther)});

        if (referralWithinOpd.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TYPE OF OPD OR WARD PATIENT REFERRED FROM", App.get(referralWithinOpd).equals(getResources().getString(R.string.fast_generalmedicinefilterclinic_title)) ? "GENERAL MEDICINE DEPARTMENT" :
                    (App.get(referralWithinOpd).equals(getResources().getString(R.string.fast_chesttbclinic_title)) ? "CHEST MEDICINE DEPARTMENT" :
                            (App.get(referralWithinOpd).equals(getResources().getString(R.string.fast_gynaeobstetrics_title)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                    (App.get(referralWithinOpd).equals(getResources().getString(R.string.fast_surgery_title)) ? "PEDIATRIC SURGERY DEPARTMENT" :
                                            (App.get(referralWithinOpd).equals(getResources().getString(R.string.fast_emergency_title)) ? "EMERGENCY DEPARTMENT" : "OTHER TYPE OF OPD OR WARD"))))});

        if (referralWithinOpdOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER TYPE OF OPD OR WARD", App.get(referralWithinOpdOther)});

        if (facilityType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TYPE OF HEALTHCARE FACILITY", App.get(facilityType).equals(getResources().getString(R.string.fast_private_hospital)) ? "PRIVATE FACILITY" :
                    (App.get(facilityType).equals(getResources().getString(R.string.fast_public_hospital)) ? "GOVERNMENT FACILITY" : "GENERAL PRACTITIONER")});


        if (hearAboutUs.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEAR ABOUT US", App.get(hearAboutUs).equals(getResources().getString(R.string.fast_radio)) ? "RADIO, AS A HOUSEHOLD ITEM" :
                    (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_tv)) ? "TV" :
                            (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_newspaper)) ? "NEWSPAPER" :
                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_billboard_signboards)) ? "BILLBOARD" :
                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_internet)) ? "INTERNET CONNECTION" :
                                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_sms)) ? "SMS" :
                                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_from_a_friend_community)) ? "FRIEND" :
                                                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_community_camp)) ? "COMMUNITY CAMP" :
                                                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_school_awareness_program)) ? "SCHOOL AWARENESS PROGRAM" :
                                                                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_pamphlet)) ? "Pamphlet" :
                                                                                            (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_work_place_awareness)) ? "WORK PLACE AWARNESS" :
                                                                                                    (App.get(hearAboutUs).equals(getResources().getString(R.string.fast_call_center)) ? "CALL CENTER" : "HEAR ABOUT US OTHER")))))))))))});


        if (hearAboutUsOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEAR ABOUT US OTHER", App.get(hearAboutUsOther)});

        if (contactReferral.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT ENROLLED", App.get(contactReferral).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});

        if (contactPatientId.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT PATIENT ID", App.get(contactPatientId)});

        if (contactExternalId.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT EXTERNAL ID", App.get(contactExternalId)});

        if (contactExternalIdHospital.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FACILITY NAME", App.get(contactExternalIdHospital)});

        if (contactTbRegisternationNo.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB REGISTRATION NUMBER", App.get(contactTbRegisternationNo)});


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

                String result = serverService.saveEncounterAndObservation("Patient Location", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
            } else if (obs[0][0].equals("IDENTIFIED PATIENT THROUGH SCREENING")) {

                for (RadioButton rb : screening.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                screening.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HOSPITAL SECTION")) {
                String value = obs[0][1].equals("OPD CLINIC SCREENING") ? getResources().getString(R.string.fast_opdclinicscreening_title) :
                        (obs[0][1].equals("WARD SCREENING") ? getResources().getString(R.string.fast_wardscreening_title) :
                                (obs[0][1].equals("REGISTRATION DESK") ? getResources().getString(R.string.fast_registrationdesk_title) :
                                        (obs[0][1].equals("X-RAY VAN") ? getResources().getString(R.string.fast_nexttoxrayvan_title) :
                                                getResources().getString(R.string.fast_other_title))));

                facilitySection.getSpinner().selectValue(value);
                facilitySection.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER FACILITY SECTION")) {
                facilitySectionOther.getEditText().setText(obs[0][1]);
                facilitySectionOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OUTPATIENT DEPARTMENT")) {
                String value = obs[0][1].equals("GENERAL MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_generalmedicinefilterclinic_title) :
                        (obs[0][1].equals("CHEST MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_chesttbclinic_title) :
                                (obs[0][1].equals("OBSTETRICS AND GYNECOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_gynaeobstetrics_title) :
                                        (obs[0][1].equals("PEDIATRIC SURGERY DEPARTMENT") ? getResources().getString(R.string.fast_surgery_title) :
                                                getResources().getString(R.string.fast_er_title))));

                opdWardSection.getSpinner().selectValue(value);
                opdWardSection.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PATIENT REFERRAL SOURCE")) {
                String value = obs[0][1].equals("CLINICAL OFFICER/DOCTOR") ? getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital) :
                        (obs[0][1].equals("PRIVATE PRACTIONER") ? getResources().getString(R.string.fast_doctor_or_health_worker_outside_the_hospital) :
                                (obs[0][1].equals("SELF") ? getResources().getString(R.string.fast_self_referral) :
                                        (obs[0][1].equals("TUBERCULOSIS CONTACT") ? getResources().getString(R.string.fast_contact_of_tb_patient) :
                                                (obs[0][1].equals("CD4 COUNT") ? getResources().getString(R.string.fast_referred_after_xray_cad4tb) :
                                                        getResources().getString(R.string.fast_other_title)))));

                patientReferralSource.getSpinner().selectValue(value);
                patientReferralSource.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER PATIENT REFERRAL SOURCE")) {
                otherReferral.getEditText().setText(obs[0][1]);
                otherReferral.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HEALTH FACILITY DEPARTMENT")) {

                for (RadioButton rb : facilityDepartment.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_ward_title)) && obs[0][1].equals("OUTPATIENT DEPARTMENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_opd_clinic)) && obs[0][1].equals("OBSERVATION WARD")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_other_title)) && obs[0][1].equals("OTHER FACILITY DEPARTMENT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                facilityDepartment.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER FACILITY DEPARTMENT")) {
                facilityDepartmentOther.getEditText().setText(obs[0][1]);
                facilityDepartmentOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OUTPATIENT DEPARTMENT")) {
                String value = obs[0][1].equals("GENERAL MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_generalmedicinefilterclinic_title) :
                        (obs[0][1].equals("CHEST MEDICINE DEPARTMENT") ? getResources().getString(R.string.fast_chesttbclinic_title) :
                                (obs[0][1].equals("OBSTETRICS AND GYNECOLOGY DEPARTMENT") ? getResources().getString(R.string.fast_gynaeobstetrics_title) :
                                        (obs[0][1].equals("PEDIATRIC SURGERY DEPARTMENT") ? getResources().getString(R.string.fast_surgery_title) :
                                                (obs[0][1].equals("EMERGENCY DEPARTMENT") ? getResources().getString(R.string.fast_er_title) :
                                                        getResources().getString(R.string.fast_other_title)))));

                referralWithinOpd.getSpinner().selectValue(value);
                referralWithinOpd.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER TYPE OF OPD OR WARD")) {
                referralWithinOpdOther.getEditText().setText(obs[0][1]);
                referralWithinOpdOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TYPE OF HEALTHCARE FACILITY")) {

                for (RadioButton rb : facilityType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_private_hospital)) && obs[0][1].equals("PRIVATE FACILITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_public_hospital)) && obs[0][1].equals("GOVERNMENT FACILITY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_GP)) && obs[0][1].equals("GENERAL PRACTITIONER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                facilityType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HEAR ABOUT US")) {
                String value = obs[0][1].equals("RADIO, AS A HOUSEHOLD ITEM") ? getResources().getString(R.string.fast_radio) :
                        (obs[0][1].equals("TV") ? getResources().getString(R.string.fast_tv) :
                                (obs[0][1].equals("NEWSPAPER") ? getResources().getString(R.string.fast_newspaper) :
                                        (obs[0][1].equals("BILLBOARD") ? getResources().getString(R.string.fast_billboard_signboards) :
                                                (obs[0][1].equals("INTERNET CONNECTION") ? getResources().getString(R.string.fast_internet) :
                                                        (obs[0][1].equals("SMS") ? getResources().getString(R.string.fast_sms) :
                                                                (obs[0][1].equals("FRIEND") ? getResources().getString(R.string.fast_from_a_friend_community) :
                                                                        (obs[0][1].equals("COMMUNITY CAMP") ? getResources().getString(R.string.fast_community_camp) :
                                                                                (obs[0][1].equals("SCHOOL AWARENESS PROGRAM") ? getResources().getString(R.string.fast_school_awareness_program) :
                                                                                        (obs[0][1].equals("Pamphlet") ? getResources().getString(R.string.fast_pamphlet) :
                                                                                                (obs[0][1].equals("WORK PLACE AWARNESS") ? getResources().getString(R.string.fast_work_place_awareness) :
                                                                                                        (obs[0][1].equals("CALL CENTER") ? getResources().getString(R.string.fast_call_center) :
                                                                                                                getResources().getString(R.string.fast_other_title))))))))))));


                hearAboutUs.getSpinner().selectValue(value);
                hearAboutUs.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HEAR ABOUT US OTHER")) {
                hearAboutUsOther.getEditText().setText(obs[0][1]);
                hearAboutUsOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("PATIENT ENROLLED")) {

                for (RadioButton rb : contactReferral.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_yes_title)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_no_title)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                contactReferral.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CONTACT PATIENT ID")) {
                contactPatientId.getEditText().setText(obs[0][1]);
                contactPatientId.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CONTACT EXTERNAL ID")) {
                contactExternalId.getEditText().setText(obs[0][1]);
                contactExternalId.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("FACILITY NAME")) {
                contactExternalIdHospital.getSpinner().selectValue(obs[0][1]);
                contactExternalIdHospital.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TB REGISTRATION NUMBER")) {
                contactTbRegisternationNo.getEditText().setText(obs[0][1]);
                contactTbRegisternationNo.setVisibility(View.VISIBLE);
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
        if (spinner == facilitySection.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                facilitySectionOther.setVisibility(View.VISIBLE);
            } else {
                facilitySectionOther.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_opdclinicscreening_title)) ||
                    parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_wardscreening_title))
                            && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                opdWardSection.setVisibility(View.VISIBLE);
            } else {
                opdWardSection.setVisibility(View.GONE);
            }
        } else if (spinner == patientReferralSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                otherReferral.setVisibility(View.VISIBLE);
            } else {
                otherReferral.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital)) && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                facilityDepartment.setVisibility(View.VISIBLE);
                if (facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_ward_title)) || facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_opd_clinic))) {
                    referralWithinOpd.setVisibility(View.VISIBLE);
                    if (referralWithinOpd.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        referralWithinOpdOther.setVisibility(View.VISIBLE);
                    } else {
                        referralWithinOpdOther.setVisibility(View.GONE);
                    }
                } else {
                    referralWithinOpd.setVisibility(View.GONE);
                    referralWithinOpdOther.setVisibility(View.GONE);
                }
            } else {
                facilityDepartment.setVisibility(View.GONE);
                referralWithinOpd.setVisibility(View.GONE);
                referralWithinOpdOther.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_doctor_or_health_worker_outside_the_hospital)) && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                facilityType.setVisibility(View.VISIBLE);
            } else {
                facilityType.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_self_referral)) && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                hearAboutUs.setVisibility(View.VISIBLE);
            } else {
                hearAboutUs.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_contact_of_tb_patient)) && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                contactReferral.setVisibility(View.VISIBLE);
                if (contactReferral.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    contactIdType.setVisibility(View.VISIBLE);
                    ArrayList<MyCheckBox> myCheckBoxes = contactIdType.getCheckedBoxes();
                    if (myCheckBoxes.get(0).getValue() == true) {
                        contactPatientId.setVisibility(View.VISIBLE);
                    } else {
                        contactPatientId.setVisibility(View.GONE);
                    }
                    if (myCheckBoxes.get(1).getValue() == true) {
                        contactExternalId.setVisibility(View.VISIBLE);
                        if (!(App.get(contactExternalId).equals(""))) {
                            contactExternalIdHospital.setVisibility(View.VISIBLE);
                        } else {
                            contactExternalIdHospital.setVisibility(View.GONE);
                        }
                    } else {
                        contactExternalId.setVisibility(View.GONE);
                        contactExternalIdHospital.setVisibility(View.GONE);
                    }

                    if (myCheckBoxes.get(2).getValue() == true) {
                        contactTbRegisternationNo.setVisibility(View.VISIBLE);
                    } else {
                        contactTbRegisternationNo.setVisibility(View.GONE);
                    }

                } else {
                    contactIdType.setVisibility(View.GONE);
                    contactPatientId.setVisibility(View.GONE);
                    contactExternalId.setVisibility(View.GONE);
                    contactExternalIdHospital.setVisibility(View.GONE);
                    contactTbRegisternationNo.setVisibility(View.GONE);
                }
            } else {
                contactReferral.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegisternationNo.setVisibility(View.GONE);
            }
        } else if (spinner == referralWithinOpd.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) && facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_ward_title))
                    || facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_opd_clinic)) &&
                    patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital))
                    && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                referralWithinOpdOther.setVisibility(View.VISIBLE);
            } else {
                referralWithinOpdOther.setVisibility(View.GONE);
            }
        } else if (spinner == hearAboutUs.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title)) && patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_self_referral)) && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                hearAboutUsOther.setVisibility(View.VISIBLE);
            } else {
                hearAboutUsOther.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == screening.getRadioGroup()) {
            if (screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                facilitySection.setVisibility(View.VISIBLE);
                if (facilitySection.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_opdclinicscreening_title)) || facilitySection.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_wardscreening_title))) {
                    opdWardSection.setVisibility(View.VISIBLE);
                }
                patientReferralSource.setVisibility(View.GONE);
                otherReferral.setVisibility(View.GONE);
                facilityDepartment.setVisibility(View.GONE);
                facilityDepartmentOther.setVisibility(View.GONE);
                referralWithinOpd.setVisibility(View.GONE);
                referralWithinOpdOther.setVisibility(View.GONE);
                facilityType.setVisibility(View.GONE);
                hearAboutUs.setVisibility(View.GONE);
                hearAboutUsOther.setVisibility(View.GONE);
                contactReferral.setVisibility(View.GONE);
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegisternationNo.setVisibility(View.GONE);
            } else {
                facilitySection.setVisibility(View.GONE);
                opdWardSection.setVisibility(View.GONE);
                patientReferralSource.setVisibility(View.VISIBLE);
                if (patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    otherReferral.setVisibility(View.VISIBLE);
                } else if (patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital))) {
                    facilityDepartment.setVisibility(View.VISIBLE);
                    if (facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))) {
                        facilityDepartmentOther.setVisibility(View.VISIBLE);
                    } else {
                        referralWithinOpd.setVisibility(View.VISIBLE);
                        if (referralWithinOpd.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                            referralWithinOpdOther.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_doctor_or_health_worker_outside_the_hospital))) {
                    facilityType.setVisibility(View.VISIBLE);
                } else if (patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_self_referral))) {
                    hearAboutUs.setVisibility(View.VISIBLE);
                    if (hearAboutUs.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                        hearAboutUsOther.setVisibility(View.VISIBLE);
                    }
                } else if (patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_contact_of_tb_patient))) {
                    contactReferral.setVisibility(View.VISIBLE);
                    if (contactReferral.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                        contactIdType.setVisibility(View.VISIBLE);
                        ArrayList<MyCheckBox> myCheckBoxes = contactIdType.getCheckedBoxes();
                        if (myCheckBoxes.get(0).getValue() == true) {
                            contactPatientId.setVisibility(View.VISIBLE);
                        }
                        if (myCheckBoxes.get(1).getValue() == true) {
                            contactExternalId.setVisibility(View.VISIBLE);
                            if (!(contactExternalId.getEditText().getValue().equals(""))) {
                                contactExternalIdHospital.setVisibility(View.VISIBLE);
                            }
                        }
                        if (myCheckBoxes.get(2).getValue() == true) {
                            contactTbRegisternationNo.setVisibility(View.VISIBLE);
                        }
                    } else {
                        contactPatientId.setVisibility(View.GONE);
                        contactExternalId.setVisibility(View.GONE);
                        contactExternalIdHospital.setVisibility(View.GONE);
                        contactTbRegisternationNo.setVisibility(View.GONE);
                    }
                }
            }
        } else if (radioGroup == facilityDepartment.getRadioGroup()) {
            if (facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_other_title))
                    && patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital))
                    && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                facilityDepartmentOther.setVisibility(View.VISIBLE);
            } else {
                facilityDepartmentOther.setVisibility(View.GONE);
            }

            if (facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_ward_title))
                    || facilityDepartment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_opd_clinic)) &&
                    patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_doctor_or_health_worker_within_the_hospital))
                    && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                referralWithinOpd.setVisibility(View.VISIBLE);
                if (referralWithinOpd.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    referralWithinOpdOther.setVisibility(View.VISIBLE);
                } else {
                    referralWithinOpdOther.setVisibility(View.GONE);
                }
            } else {
                referralWithinOpd.setVisibility(View.GONE);
                referralWithinOpdOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == contactReferral.getRadioGroup()) {
            if (contactReferral.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))
                    && patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_contact_of_tb_patient))
                    && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                contactIdType.setVisibility(View.VISIBLE);
                ArrayList<MyCheckBox> myCheckBoxes = contactIdType.getCheckedBoxes();
                if (myCheckBoxes.get(0).getValue() == true) {
                    contactPatientId.setVisibility(View.VISIBLE);
                } else {
                    contactPatientId.setVisibility(View.GONE);
                }
                if (myCheckBoxes.get(1).getValue() == true) {
                    contactExternalId.setVisibility(View.VISIBLE);
                    if (!(App.get(contactExternalId).equals(""))) {
                        contactExternalIdHospital.setVisibility(View.VISIBLE);
                    } else {
                        contactExternalIdHospital.setVisibility(View.GONE);
                    }
                } else {
                    contactExternalId.setVisibility(View.GONE);
                    contactExternalIdHospital.setVisibility(View.GONE);
                }

                if (myCheckBoxes.get(2).getValue() == true) {
                    contactTbRegisternationNo.setVisibility(View.VISIBLE);
                } else {
                    contactTbRegisternationNo.setVisibility(View.GONE);
                }

            } else {
                contactIdType.setVisibility(View.GONE);
                contactPatientId.setVisibility(View.GONE);
                contactExternalId.setVisibility(View.GONE);
                contactExternalIdHospital.setVisibility(View.GONE);
                contactTbRegisternationNo.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (patientIdCheckbox.isChecked() && patientIdCheckbox.getVisibility() == View.VISIBLE) {
            contactPatientId.setVisibility(View.VISIBLE);
        } else {
            contactPatientId.setVisibility(View.GONE);
        }

        if (externalIdCheckbox.isChecked() && externalIdCheckbox.getVisibility() == View.VISIBLE) {
            contactExternalId.setVisibility(View.VISIBLE);
            if (contactExternalId.getEditText().getText().toString().equals("")) {
                contactExternalIdHospital.setVisibility(View.GONE);
            } else {
                contactExternalIdHospital.setVisibility(View.VISIBLE);
            }
        } else {
            contactExternalId.setVisibility(View.GONE);
            contactExternalIdHospital.setVisibility(View.GONE);
        }

        if (tbRegisterationNoCheckbbox.isChecked() && tbRegisterationNoCheckbbox.getVisibility() == View.VISIBLE) {
            contactTbRegisternationNo.setVisibility(View.VISIBLE);
        } else {
            contactTbRegisternationNo.setVisibility(View.GONE);
        }
    }


    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        facilitySectionOther.setVisibility(View.GONE);
        patientReferralSource.setVisibility(View.GONE);
        otherReferral.setVisibility(View.GONE);
        facilityDepartment.setVisibility(View.GONE);
        facilityDepartmentOther.setVisibility(View.GONE);
        referralWithinOpd.setVisibility(View.GONE);
        referralWithinOpdOther.setVisibility(View.GONE);
        facilityType.setVisibility(View.GONE);
        hearAboutUs.setVisibility(View.GONE);
        hearAboutUsOther.setVisibility(View.GONE);
        contactReferral.setVisibility(View.GONE);
        contactIdType.setVisibility(View.GONE);
        contactPatientId.setVisibility(View.GONE);
        contactExternalId.setVisibility(View.GONE);
        contactExternalIdHospital.setVisibility(View.GONE);
        contactTbRegisternationNo.setVisibility(View.GONE);

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

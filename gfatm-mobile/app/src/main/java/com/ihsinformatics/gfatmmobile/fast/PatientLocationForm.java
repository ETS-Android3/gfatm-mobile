package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haris on 1/13/2017.
 */

public class PatientLocationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

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
    TitledEditText contactExternalIdHospital;
    TitledEditText contactTbRegisternationNo;


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.FAST_PATIENT_LOCATION_FORM;

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
        facilitySectionOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        facilitySectionOther.setVisibility(View.GONE);
        opdWardSection = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_clinic_and_ward_title), getResources().getStringArray(R.array.fast_clinic_and_ward_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        patientReferralSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_source_of_patient_referral), getResources().getStringArray(R.array.fast_source_of_patient_referral_list), getResources().getString(R.string.fast_self_referral), App.VERTICAL);
        patientReferralSource.setVisibility(View.GONE);
        otherReferral = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        otherReferral.setVisibility(View.GONE);
        facilityDepartment = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_within_hospital_was_patient_referred), getResources().getStringArray(R.array.fast_patient_reffered_from_list), getResources().getString(R.string.fast_ward_title), App.VERTICAL, App.VERTICAL);
        facilityDepartmentOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        facilityDepartmentOther.setVisibility(View.GONE);
        referralWithinOpd = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_which_opd_clinic_or_ward_has_patient_referred_from), getResources().getStringArray(R.array.fast_source_of_patient_referral_list), getResources().getString(R.string.fast_generalmedicinefilterclinic_title), App.VERTICAL);
        referralWithinOpd.setVisibility(View.GONE);
        referralWithinOpdOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        referralWithinOpdOther.setVisibility(View.GONE);
        facilityType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_outside_hospital_was_patient_referred), getResources().getStringArray(R.array.fast_outside_hospital_patient_reffered_from), getResources().getString(R.string.fast_private_hospital), App.VERTICAL, App.VERTICAL);
        facilityType.setVisibility(View.GONE);
        hearAboutUs = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_where_did_you_hear_about_us), getResources().getStringArray(R.array.fast_hear_about_us_from_list), getResources().getString(R.string.fast_radio), App.VERTICAL);
        hearAboutUsOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        hearAboutUsOther.setVisibility(View.GONE);
        contactReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_enrolled_in_any_tb_program), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_no_title), App.VERTICAL, App.VERTICAL);
        contactIdType = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_tb_contact_any_identifications_id), getResources().getStringArray(R.array.fast_tb_contact_identification_list), null, App.VERTICAL, App.VERTICAL);
        contactIdType.setVisibility(View.GONE);
        contactPatientId = new TitledEditText(context, null, getResources().getString(R.string.fast_patient_id), "", "", 6, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactExternalId = new TitledEditText(context, null, getResources().getString(R.string.fast_external_id), "", "", 20, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactExternalIdHospital = new TitledEditText(context, null, getResources().getString(R.string.fast_if_external_id_hospital_or_programs), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        contactExternalIdHospital.setVisibility(View.GONE);
        contactTbRegisternationNo = new TitledEditText(context, null, getResources().getString(R.string.fast_tb_registeration_no), "", "", 11, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactTbRegisternationNo.setVisibility(View.GONE);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), facilitySection.getSpinner(), facilitySectionOther.getEditText(),
                opdWardSection.getSpinner(), patientReferralSource.getSpinner(), otherReferral.getEditText(),
                facilityDepartment.getRadioGroup(), facilityDepartmentOther.getEditText(), referralWithinOpd.getSpinner(), referralWithinOpdOther.getEditText(),
                facilityType.getRadioGroup(), hearAboutUs.getSpinner(), hearAboutUsOther.getEditText(), contactReferral.getRadioGroup(), contactIdType, contactPatientId.getEditText(), contactExternalId.getEditText(), contactExternalIdHospital.getEditText(),
                contactTbRegisternationNo.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, screening, facilitySection, facilitySectionOther, opdWardSection, patientReferralSource, otherReferral, facilityDepartment, facilityDepartmentOther, referralWithinOpd, referralWithinOpdOther, facilityType, hearAboutUs, hearAboutUsOther, contactReferral, contactIdType, contactPatientId, contactExternalId, contactExternalIdHospital, contactTbRegisternationNo},};

        screening.getRadioGroup().setOnCheckedChangeListener(this);
        facilitySection.getSpinner().setOnItemSelectedListener(this);
        opdWardSection.getSpinner().setOnItemSelectedListener(this);
        patientReferralSource.getSpinner().setOnItemSelectedListener(this);
        facilityDepartment.getRadioGroup().setOnCheckedChangeListener(this);
        referralWithinOpd.getSpinner().setOnItemSelectedListener(this);
        facilityType.getRadioGroup().setOnCheckedChangeListener(this);
        hearAboutUs.getSpinner().setOnItemSelectedListener(this);
        contactReferral.getRadioGroup().setOnCheckedChangeListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        Boolean error = false;

     /*   if (patient_consultation_other.getVisibility() == View.VISIBLE && App.get(patient_consultation_other).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(2);
            else
                gotoPage(0);
            patient_consultation_other.getEditText().setError(getString(R.string.empty_field));
            patient_consultation_other.getEditText().requestFocus();
            error = true;
        }*/

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

        if (validate()) {
            resetViews();
        }

        //resetViews();
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
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
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
            } else {
                facilityDepartment.setVisibility(View.GONE);
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
            } else {
                contactReferral.setVisibility(View.GONE);
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
            } else {
                referralWithinOpd.setVisibility(View.GONE);
            }
        } else if (radioGroup == contactReferral.getRadioGroup()) {
            if (contactReferral.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))
                    && patientReferralSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_contact_of_tb_patient))
                    && screening.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                contactIdType.setVisibility(View.VISIBLE);
            } else {
                contactIdType.setVisibility(View.GONE);
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

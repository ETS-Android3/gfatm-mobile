package com.ihsinformatics.gfatmmobile.pet;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetIncentiveDisbursementForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledEditText indexPatientId;
    Button scanQRCode;
    TitledEditText indexExternalPatientId;
    LinearLayout cnicLayout;
    TitledEditText cnic1;
    TitledEditText cnic2;
    TitledEditText cnic3;
    TitledSpinner cnicOwner;
    TitledEditText otherCnicOwner;
    TitledRadioGroup incentiveOccasion;
    TitledRadioGroup incentiveFor;
    TitledEditText nameTreatmentSupporter;
    TitledEditText contactNumberTreatmentSupporter;
    TitledRadioGroup typeTreatmentSupporter;
    TitledSpinner relationshipTreatmentSuppoter;
    TitledEditText other;
    TitledRadioGroup petRegimen;

    TitledEditText incentiveAmount;
    TitledEditText followupMonth;
    TitledRadioGroup incentiveDisbursalLocation;
    TitledEditText recieverName;
    TitledSpinner recieverRelationWithContact;
    TitledEditText otherRelation;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 2;
        FORM_NAME = Forms.PET_INCENTIVE_DISBURSEMENT;
        FORM = Forms.pet_incentiveDisbursement;

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
                ScrollView scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                ScrollView scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }


    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        cnicLayout = new LinearLayout(context);
        cnicLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new TitledEditText(context, null, getResources().getString(R.string.pet_cnic), "", "XXXXX", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        cnicLayout.addView(cnic1);
        cnic2 = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLayout.addView(cnic2);
        cnic3 = new TitledEditText(context, null, "-", "", "X", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        cnicLayout.addView(cnic3);
        cnicOwner = new TitledSpinner(context, "", getResources().getString(R.string.pet_cnic_owner), getResources().getStringArray(R.array.pet_cnic_owners), getResources().getString(R.string.pet_self), App.VERTICAL, true);
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        incentiveOccasion = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_incentive_occasion), getResources().getStringArray(R.array.pet_incentive_occasions), getResources().getString(R.string.pet_baseline_visit), App.HORIZONTAL, App.VERTICAL, true);
        incentiveFor = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_incentive_for), getResources().getStringArray(R.array.pet_incentive_for), getResources().getString(R.string.pet_contact), App.HORIZONTAL, App.VERTICAL, true);

        nameTreatmentSupporter = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        contactNumberTreatmentSupporter = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_contact_number), "", "", 11, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        typeTreatmentSupporter = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_treatment_supporter_type), getResources().getStringArray(R.array.pet_treatment_supporter_type), getResources().getString(R.string.pet_family_treatment_supporter), App.VERTICAL, App.VERTICAL, true);
        relationshipTreatmentSuppoter = new TitledSpinner(context, "", getResources().getString(R.string.pet_treatment_supporter_relationship), getResources().getStringArray(R.array.pet_household_heads), getResources().getString(R.string.pet_mother), App.VERTICAL, true);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens), "", App.VERTICAL, App.VERTICAL);

        MyLinearLayout linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_incentive_details), App.VERTICAL);
        incentiveAmount = new TitledEditText(context, null, getResources().getString(R.string.pet_incentive_amount), "500", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        followupMonth = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        incentiveDisbursalLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_incentive_disbursal_location), getResources().getStringArray(R.array.pet_locations_of_entry), getResources().getString(R.string.pet_contact_home), App.HORIZONTAL, App.VERTICAL);
        recieverName = new TitledEditText(context, null, getResources().getString(R.string.pet_receiver_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        recieverRelationWithContact = new TitledSpinner(context, "", getResources().getString(R.string.pet_receiver_relation_with_contact), getResources().getStringArray(R.array.pet_cnic_owners), getResources().getString(R.string.pet_self), App.VERTICAL);
        otherRelation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);


        linearLayout.addView(incentiveAmount);
        linearLayout.addView(followupMonth);
        linearLayout.addView(incentiveDisbursalLocation);
        linearLayout.addView(recieverName);
        linearLayout.addView(recieverRelationWithContact);
        linearLayout.addView(otherRelation);

        views = new View[]{formDate.getButton(), indexPatientId.getEditText(), scanQRCode, indexExternalPatientId.getEditText(), cnic1.getEditText(), cnic2.getEditText(), cnic3.getEditText(), cnicOwner.getSpinner(), otherCnicOwner.getEditText(), incentiveOccasion.getRadioGroup(), incentiveFor.getRadioGroup(),
                nameTreatmentSupporter.getEditText(), contactNumberTreatmentSupporter.getEditText(), typeTreatmentSupporter.getRadioGroup(), relationshipTreatmentSuppoter.getSpinner(), other.getEditText(), petRegimen.getRadioGroup(), incentiveAmount.getEditText(),
                followupMonth.getEditText(), incentiveDisbursalLocation.getRadioGroup(), recieverName.getEditText(), recieverRelationWithContact.getSpinner(), otherRelation.getEditText()
        };

        viewGroups = new View[][]{{formDate, indexPatientId, scanQRCode, indexExternalPatientId, cnicLayout, cnicOwner, otherCnicOwner, incentiveOccasion, incentiveFor,
                nameTreatmentSupporter, contactNumberTreatmentSupporter, typeTreatmentSupporter, relationshipTreatmentSuppoter, other, petRegimen},
                {linearLayout}};

        View listenerViewer[] = new View[]{formDate, cnicOwner, incentiveOccasion, relationshipTreatmentSuppoter, recieverRelationWithContact, typeTreatmentSupporter};
        for (View v : listenerViewer) {

            if (v instanceof TitledButton)
                ((TitledButton) v).getButton().setOnClickListener(this);
            else if (v instanceof TitledRadioGroup)
                ((TitledRadioGroup) v).getRadioGroup().setOnCheckedChangeListener(this);
            else if (v instanceof TitledSpinner)
                ((TitledSpinner) v).getSpinner().setOnItemSelectedListener(this);

        }

        incentiveAmount.getEditText().setKeyListener(null);
        otherCnicOwner.setVisibility(View.GONE);
        for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
            if (rb.getText().equals(getResources().getString(R.string.pet_contact)))
                rb.setChecked(true);
            else
                rb.setVisibility(View.GONE);
        }
        nameTreatmentSupporter.setVisibility(View.GONE);
        contactNumberTreatmentSupporter.setVisibility(View.GONE);
        typeTreatmentSupporter.setVisibility(View.GONE);
        relationshipTreatmentSuppoter.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        otherRelation.setVisibility(View.GONE);
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

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

        Boolean error = false;

        if (App.get(otherRelation).isEmpty() && otherRelation.getVisibility() == View.VISIBLE) {
            otherRelation.getEditText().setError(getString(R.string.empty_field));
            otherRelation.getEditText().requestFocus();
            error = true;
        }

        if (App.get(recieverName).isEmpty()) {
            recieverName.getEditText().setError(getString(R.string.empty_field));
            recieverName.getEditText().requestFocus();
            error = true;
        }

        if (App.get(followupMonth).isEmpty()) {
            followupMonth.getEditText().setError(getString(R.string.empty_field));
            followupMonth.getEditText().requestFocus();
            error = true;
        }

        if (App.get(nameTreatmentSupporter).isEmpty() && nameTreatmentSupporter.getVisibility() == View.VISIBLE) {
            nameTreatmentSupporter.getEditText().setError(getString(R.string.empty_field));
            nameTreatmentSupporter.getEditText().requestFocus();
            error = true;
        }

        if (App.get(contactNumberTreatmentSupporter).isEmpty() && contactNumberTreatmentSupporter.getVisibility() == View.VISIBLE) {
            contactNumberTreatmentSupporter.getEditText().setError(getString(R.string.empty_field));
            contactNumberTreatmentSupporter.getEditText().requestFocus();
            error = true;
        } else if (App.get(contactNumberTreatmentSupporter).length() == RegexUtil.mobileNumberLength) {
            contactNumberTreatmentSupporter.getEditText().setError(getString(R.string.invalid_value));
            contactNumberTreatmentSupporter.getEditText().requestFocus();
            error = true;
        }

        if (App.get(other).isEmpty() && other.getVisibility() == View.VISIBLE) {
            other.getEditText().setError(getString(R.string.empty_field));
            other.getEditText().requestFocus();
            error = true;
        }


        if (App.get(otherCnicOwner).isEmpty() && otherCnicOwner.getVisibility() == View.VISIBLE) {
            otherCnicOwner.getEditText().setError(getString(R.string.empty_field));
            otherCnicOwner.getEditText().requestFocus();
            error = true;
        }

        if (App.get(cnic1).isEmpty()) {
            cnic1.getEditText().setError(getResources().getString(R.string.mandatory_field));
            cnic1.getEditText().requestFocus();
            error = true;
        }
        if (App.get(cnic2).isEmpty()) {
            cnic2.getEditText().setError(getResources().getString(R.string.mandatory_field));
            cnic2.getEditText().requestFocus();
            error = true;
        }
        if (App.get(cnic3).isEmpty()) {
            cnic3.getEditText().setError(getResources().getString(R.string.mandatory_field));
            cnic3.getEditText().requestFocus();
            error = true;
        }
        if (App.get(cnic1).length() != 5) {
            cnic1.getEditText().setError(getResources().getString(R.string.invalid_value));
            cnic1.getEditText().requestFocus();
            error = true;
        }
        if (App.get(cnic2).length() != 7) {
            cnic2.getEditText().setError(getResources().getString(R.string.invalid_value));
            cnic2.getEditText().requestFocus();
            error = true;
        }
        if (App.get(cnic3).length() != 1) {
            cnic3.getEditText().setError(getResources().getString(R.string.invalid_value));
            cnic3.getEditText().requestFocus();
            error = true;
        }

        if (App.get(indexPatientId).isEmpty()) {
            indexPatientId.getEditText().setError(getString(R.string.empty_field));
            indexPatientId.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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
            return false;
        }

        return true;
    }

    @Override
    public boolean submit() {
        endTime = new Date();

        final ContentValues values = new ContentValues();
        values.put("formDate", App.getSqlDate(formDateCalendar));
        // start time...
        // end time...
        // gps coordinate...

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        final String cnic = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
        observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnic});
        observations.add(new String[]{"COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(cnicOwner).equals(getResources().getString(R.string.pet_self)) ? "SELF" :
                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_mother)) ? "MOTHER" :
                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER"))))))))))))});
        if (otherCnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(otherCnicOwner)});
        observations.add(new String[]{"TYPE OF VISIT", App.get(incentiveFor).equals(getResources().getString(R.string.pet_baseline_visit)) ? "BASELINE" : "REGULAR FOLLOW UP"});
        observations.add(new String[]{"INCENTIVE BENEFICIARY", App.get(incentiveFor).equals(getResources().getString(R.string.pet_contact)) ? "PATIENT" : "TREATMENT SUPPORTER"});
        if (nameTreatmentSupporter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NAME OF TREATMENT SUPPORTER", App.get(nameTreatmentSupporter)});
        if (contactNumberTreatmentSupporter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TELEPHONE NUMBER OF TREATMENT SUPPORTER", App.get(contactNumberTreatmentSupporter)});
        if (typeTreatmentSupporter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER TYPE", App.get(typeTreatmentSupporter).equals(getResources().getString(R.string.pet_family_treatment_supporter)) ? "FAMILY MEMBER" : "NON-FAMILY MEMBER"});
        if (relationshipTreatmentSuppoter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_mother))) ? "MOTHER" :
                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))))});
        if (other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(other)});
        observations.add(new String[]{"POST-EXPOSURE TREATMENT REGIMEN", App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) ? "ISONIAZID PROPHYLAXIS" :
                (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) ? "ISONIAZID AND RIFAPENTINE" : "LEVOFLOXACIN AND ETHIONAMIDE")});
        observations.add(new String[]{"INCENTIVE AMOUNT", App.get(incentiveAmount)});
        observations.add(new String[]{"MONTH OF INCENTIVE", App.get(followupMonth)});
        observations.add(new String[]{"LOCATION OF EVENT", App.get(incentiveDisbursalLocation).equals(getResources().getString(R.string.pet_contact_home)) ? "HOME" : "HEALTH FACILITY"});
        observations.add(new String[]{"NAME OF INCENTIVE RECEIVER", App.get(recieverName)});
        observations.add(new String[]{"RELATIONSHIP WITH INCENTIVE RECEIVER", App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_self)) ? "SELF" :
                (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_mother)) ? "MOTHER" :
                        (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                                (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                        (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                                (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                        (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                                (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                        (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                                (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                        (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                                (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                        (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER"))))))))))))});
        if (otherRelation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER INCENTIVE RECEIVER", App.get(otherRelation)});


        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.signing_in));
                        loading.show();
                    }
                });

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
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

                Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();

            }
        };
        submissionFormTask.execute("");

        resetViews();
        return false;
    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        otherCnicOwner.setVisibility(View.GONE);
        for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
            if (rb.getText().equals(getResources().getString(R.string.pet_contact)))
                rb.setChecked(true);
            else
                rb.setVisibility(View.GONE);
        }
        nameTreatmentSupporter.setVisibility(View.GONE);
        contactNumberTreatmentSupporter.setVisibility(View.GONE);
        typeTreatmentSupporter.setVisibility(View.GONE);
        relationshipTreatmentSuppoter.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        otherRelation.setVisibility(View.GONE);
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
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == cnicOwner.getSpinner()) {
            if (App.get(cnicOwner).equals(getResources().getString(R.string.pet_other)))
                otherCnicOwner.setVisibility(View.VISIBLE);
            else
                otherCnicOwner.setVisibility(View.GONE);
        } else if (spinner == relationshipTreatmentSuppoter.getSpinner()) {
            if (App.get(relationshipTreatmentSuppoter).equals(getResources().getString(R.string.pet_other)))
                other.setVisibility(View.VISIBLE);
            else
                other.setVisibility(View.GONE);
        } else if (spinner == recieverRelationWithContact.getSpinner()) {
            if (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_other)))
                otherRelation.setVisibility(View.VISIBLE);
            else
                otherRelation.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == incentiveOccasion.getRadioGroup()) {
            if (App.get(incentiveOccasion).equals(getResources().getString(R.string.pet_baseline_visit))) {
                for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_contact)))
                        rb.setChecked(true);
                    else
                        rb.setVisibility(View.GONE);
                }
                nameTreatmentSupporter.setVisibility(View.GONE);
                contactNumberTreatmentSupporter.setVisibility(View.GONE);
                typeTreatmentSupporter.setVisibility(View.GONE);
                relationshipTreatmentSuppoter.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
            } else {
                for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
                    rb.setVisibility(View.VISIBLE);
                }
                nameTreatmentSupporter.setVisibility(View.VISIBLE);
                contactNumberTreatmentSupporter.setVisibility(View.VISIBLE);
                typeTreatmentSupporter.setVisibility(View.VISIBLE);
                relationshipTreatmentSuppoter.setVisibility(View.VISIBLE);
                other.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void refill(int encounterId) {
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

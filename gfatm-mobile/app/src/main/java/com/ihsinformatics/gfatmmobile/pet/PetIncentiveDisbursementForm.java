package com.ihsinformatics.gfatmmobile.pet;

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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
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
    LinearLayout contactNumberTreatmentSupporter;
    TitledEditText phone1a;
    TitledEditText phone1b;
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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
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
        contactNumberTreatmentSupporter = new LinearLayout(context);
        contactNumberTreatmentSupporter.setOrientation(LinearLayout.HORIZONTAL);
        phone1a = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_contact_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        contactNumberTreatmentSupporter.addView(phone1a);
        phone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        contactNumberTreatmentSupporter.addView(phone1b);
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
                nameTreatmentSupporter.getEditText(), phone1a.getEditText(), phone1b.getEditText(), typeTreatmentSupporter.getRadioGroup(), relationshipTreatmentSuppoter.getSpinner(), other.getEditText(), petRegimen.getRadioGroup(), incentiveAmount.getEditText(),
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

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0,10);

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

        if (App.get(otherRelation).isEmpty() && otherRelation.getVisibility() == View.VISIBLE) {
            otherRelation.getEditText().setError(getString(R.string.empty_field));
            otherRelation.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (App.get(recieverName).isEmpty()) {
            recieverName.getEditText().setError(getString(R.string.empty_field));
            recieverName.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (App.get(followupMonth).isEmpty()) {
            followupMonth.getEditText().setError(getString(R.string.empty_field));
            followupMonth.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (App.get(nameTreatmentSupporter).isEmpty() && nameTreatmentSupporter.getVisibility() == View.VISIBLE) {
            nameTreatmentSupporter.getEditText().setError(getString(R.string.empty_field));
            nameTreatmentSupporter.getEditText().requestFocus();
            error = true;
            gotoLastPage();
        }

        if (contactNumberTreatmentSupporter.getVisibility() == View.VISIBLE) {
            if (App.get(phone1a).isEmpty()) {
                phone1a.getEditText().setError(getResources().getString(R.string.mandatory_field));
                phone1a.getEditText().requestFocus();
                error = true;
                gotoLastPage();
            } else if (App.get(phone1b).isEmpty()) {
                phone1b.getEditText().setError(getResources().getString(R.string.mandatory_field));
                phone1b.getEditText().requestFocus();
                error = true;
                gotoLastPage();
            } else if (!RegexUtil.isContactNumber(App.get(phone1a) + App.get(phone1b))) {
                phone1b.getEditText().setError(getResources().getString(R.string.invalid_value));
                phone1b.getEditText().requestFocus();
                error = true;
                gotoLastPage();
            }
        }

        if (App.get(other).isEmpty() && other.getVisibility() == View.VISIBLE) {
            other.getEditText().setError(getString(R.string.empty_field));
            other.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }


        if (App.get(otherCnicOwner).isEmpty() && otherCnicOwner.getVisibility() == View.VISIBLE) {
            otherCnicOwner.getEditText().setError(getString(R.string.empty_field));
            otherCnicOwner.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }

        if (App.get(cnic1).isEmpty()) {
            cnic1.getEditText().setError(getResources().getString(R.string.mandatory_field));
            cnic1.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }
        if (App.get(cnic2).isEmpty()) {
            cnic2.getEditText().setError(getResources().getString(R.string.mandatory_field));
            cnic2.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }
        if (App.get(cnic3).isEmpty()) {
            cnic3.getEditText().setError(getResources().getString(R.string.mandatory_field));
            cnic3.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }
        if (App.get(cnic1).length() != 5) {
            cnic1.getEditText().setError(getResources().getString(R.string.invalid_value));
            cnic1.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }
        if (App.get(cnic2).length() != 7) {
            cnic2.getEditText().setError(getResources().getString(R.string.invalid_value));
            cnic2.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }
        if (App.get(cnic3).length() != 1) {
            cnic3.getEditText().setError(getResources().getString(R.string.invalid_value));
            cnic3.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
        }

        if (App.get(indexPatientId).isEmpty()) {
            indexPatientId.getEditText().setError(getString(R.string.empty_field));
            indexPatientId.getEditText().requestFocus();
            error = true;
            gotoFirstPage();
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
        } else
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
        observations.add(new String[]{"PATIENT ID OF INDEX CASE", App.get(indexPatientId)});
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
                                                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_daughter)) ? "DAUGHTER" :
                                                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_spouse)) ? "SPOUSE" :
                                                                                                (App.get(cnicOwner).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                        (App.get(cnicOwner).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))))))});
        if (otherCnicOwner.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", App.get(otherCnicOwner)});
        observations.add(new String[]{"TYPE OF VISIT", App.get(incentiveFor).equals(getResources().getString(R.string.pet_baseline_visit)) ? "BASELINE" : "REGULAR FOLLOW UP"});
        observations.add(new String[]{"INCENTIVE BENEFICIARY", App.get(incentiveFor).equals(getResources().getString(R.string.pet_contact)) ? "PATIENT" : "TREATMENT SUPPORTER"});
        if (nameTreatmentSupporter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NAME OF TREATMENT SUPPORTER", App.get(nameTreatmentSupporter)});
        if (contactNumberTreatmentSupporter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER CONTACT NUMBER", App.get(phone1a) + "-" + App.get(phone1b)});
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
                                                                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_daughter)) ? "DAUGHTER" :
                                                                                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_spouse)) ? "SPOUSE" :
                                                                                                    (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                            (App.get(recieverRelationWithContact).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER"))))))))))))});
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


                    if (!App.get(indexExternalPatientId).isEmpty() && App.hasKeyListener(indexExternalPatientId)) {
                        result = serverService.saveIdentifier("External ID", App.get(indexExternalPatientId), encounterId);
                        if (!result.equals("SUCCESS"))
                            return result;
                    } else
                        return "SUCCESS";

                }

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

                if (result.equals("SUCCESS")) {
                    MainActivity.backToMainMenu();
                    try {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

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
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

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

        if (App.get(petRegimen).equals("")) {
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

                    String date1 = "";
                    String date2 = "";
                    String date3 = "";
                    String petRegimen1 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_ADVERSE_EVENTS, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen1 == null || petRegimen1.equals("")))
                        date1 = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + Forms.PET_ADVERSE_EVENTS);
                    String petRegimen2 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_CLINICIAN_FOLLOWUP, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen2 == null || petRegimen2.equals("")))
                        date2 = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + Forms.PET_CLINICIAN_FOLLOWUP);
                    String petRegimen3 = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen3 == null || petRegimen3.equals("")))
                        date3 = serverService.getEncounterDateTime(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION);

                    if ((date2 == null || date2.equals("")) && (date3 == null || date3.equals(""))) {
                        if (petRegimen1 == null)
                            petRegimen1 = "";
                        result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen1);
                    } else {
                        if (date2 == null || date2.equals("")) {
                            if (petRegimen3 == null)
                                petRegimen3 = "";
                            result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                        } else if (date3 == null || date3.equals("")) {
                            if (petRegimen2 == null)
                                petRegimen2 = "";
                            result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen2);
                        } else {

                            Date d2 = null;
                            Date d3 = null;
                            if (date2.contains("/")) {
                                d2 = App.stringToDate(date2, "dd/MM/yyyy");
                            } else {
                                d2 = App.stringToDate(date2, "yyyy-MM-dd");
                            }

                            if (date3.contains("/")) {
                                d3 = App.stringToDate(date3, "dd/MM/yyyy");
                            } else {
                                d3 = App.stringToDate(date3, "yyyy-MM-dd");
                            }

                            if (d2.equals(d3)) {
                                if (petRegimen3 == null)
                                    petRegimen3 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                            } else if (d2.after(d3)) {
                                if (petRegimen2 == null)
                                    petRegimen2 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen2);
                            } else {
                                petRegimen3 = "";
                                result.put("POST-EXPOSURE TREATMENT REGIMEN", petRegimen3);
                            }

                        }
                    }

                    String indexId = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");
                    String cnicString = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "NATIONAL IDENTIFICATION NUMBER");
                    String cnicOwner = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                    String otherCnicOwner = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                    String treatmentSupporter = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION, "NAME OF TREATMENT SUPPORTER");
                    String treatmentSupporterContact = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION, "TREATMENT SUPPORTER CONTACT NUMBER");
                    String treatmentSupporterType = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION, "TREATMENT SUPPORTER TYPE");
                    String treatmentSupporterRelationship = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION, "TREATMENT SUPPORTER RELATIONSHIP TO PATIENT");
                    String treatmentSupporterRelationshipOther = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_TREATMENT_INITIATION, "OTHER FAMILY MEMBER");

                    if (indexId == null)
                        indexId = "";
                    result.put("PATIENT ID OF INDEX CASE", indexId);

                    if (cnicString == null)
                        cnicString = "";
                    result.put("NATIONAL IDENTIFICATION NUMBER", cnicString);

                    if (cnicOwner == null)
                        cnicOwner = "";
                    result.put("COMPUTERIZED NATIONAL IDENTIFICATION OWNER", cnicOwner);

                    if (otherCnicOwner == null)
                        otherCnicOwner = "";
                    result.put("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER", otherCnicOwner);

                    if (treatmentSupporter == null)
                        treatmentSupporter = "";
                    result.put("NAME OF TREATMENT SUPPORTER", treatmentSupporter);

                    if (treatmentSupporterContact == null)
                        treatmentSupporterContact = "";
                    result.put("TREATMENT SUPPORTER CONTACT NUMBER", treatmentSupporterContact);

                    if (treatmentSupporterType == null)
                        treatmentSupporterType = "";
                    result.put("TREATMENT SUPPORTER TYPE", treatmentSupporterType);

                    if (treatmentSupporterRelationship == null)
                        treatmentSupporterRelationship = "";
                    result.put("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", treatmentSupporterRelationship);

                    if (treatmentSupporterRelationshipOther == null)
                        treatmentSupporterRelationshipOther = "";
                    result.put("OTHER FAMILY MEMBER", treatmentSupporterRelationshipOther);

                    return result;

                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                ;

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (!result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("")) {
                        for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ISONIAZID PROPHYLAXIS")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("ISONIAZID AND RIFAPENTINE")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) && result.get("POST-EXPOSURE TREATMENT REGIMEN").equals("LEVOFLOXACIN AND ETHIONAMIDE")) {
                                rb.setChecked(true);
                                break;
                            }

                        }
                    }
                    if (!result.get("PATIENT ID OF INDEX CASE").equals("")) {
                        indexPatientId.getEditText().setText(result.get("PATIENT ID OF INDEX CASE"));
                    }
                    if (App.get(indexExternalPatientId).equals("")) {
                        String externalId = App.getPatient().getExternalId();
                        if (externalId.equals("")) {
                            indexExternalPatientId.getEditText().setText("");
                        } else {
                            indexExternalPatientId.getEditText().setText(externalId);
                            indexExternalPatientId.getEditText().setKeyListener(null);
                        }
                    }
                    if (!result.get("NATIONAL IDENTIFICATION NUMBER").equals("")) {
                        String[] cnicArray = result.get("NATIONAL IDENTIFICATION NUMBER").split("-");
                        cnic1.getEditText().setText(cnicArray[0]);
                        cnic2.getEditText().setText(cnicArray[1]);
                        cnic3.getEditText().setText(cnicArray[2]);
                    }
                    if (!result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("")) {
                        String value = result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("SELF") ? getResources().getString(R.string.pet_self) :
                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                                        (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                                        (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                                        (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                                        (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                                        (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                                        (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                                (result.get("COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other))))))))))))));
                        cnicOwner.getSpinner().selectValue(value);
                    }
                    if (!result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER").equals("")) {
                        otherCnicOwner.getEditText().setText(result.get("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER"));
                        otherCnicOwner.setVisibility(View.VISIBLE);
                    }
                    if (!result.get("NAME OF TREATMENT SUPPORTER").equals("")) {
                        nameTreatmentSupporter.getEditText().setText(result.get("NAME OF TREATMENT SUPPORTER"));
                    }
                    if (!result.get("TREATMENT SUPPORTER CONTACT NUMBER").equals("")) {
                        String[] phoneArray = result.get("TREATMENT SUPPORTER CONTACT NUMBER").split("-");
                        phone1a.getEditText().setText(phoneArray[0]);
                        phone1b.getEditText().setText(phoneArray[1]);
                    }
                    if (!result.get("TREATMENT SUPPORTER TYPE").equals("")) {
                        for (RadioButton rb : typeTreatmentSupporter.getRadioGroup().getButtons()) {
                            if (rb.getText().equals(getResources().getString(R.string.pet_family_treatment_supporter)) && result.get("TREATMENT SUPPORTER TYPE").equals("FAMILY MEMBER")) {
                                rb.setChecked(true);
                                break;
                            } else if (rb.getText().equals(getResources().getString(R.string.pet_non_family_treatment_supporter)) && result.get("TREATMENT SUPPORTER TYPE").equals("NON-FAMILY MEMBER")) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }
                    if (!result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("")) {
                        String value = result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                                (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                        (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                                (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                        (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                                (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                        (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                                (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                        (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                                (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                        (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                                (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                        (result.get("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT").equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other)))))))))))));
                        relationshipTreatmentSuppoter.getSpinner().selectValue(value);
                    }
                    if (!result.get("OTHER FAMILY MEMBER").equals("")) {
                        otherRelation.getEditText().setText(result.get("OTHER FAMILY MEMBER"));
                    }
                }
            };
            autopopulateFormTask.execute("");

        }
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
        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("External ID")) {
                indexExternalPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT ID OF INDEX CASE")) {
                indexPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String[] cnicArray = obs[0][1].split("-");
                cnic1.getEditText().setText(cnicArray[0]);
                cnic2.getEditText().setText(cnicArray[1]);
                cnic3.getEditText().setText(cnicArray[2]);
            } else if (obs[0][0].equals("COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                String value = obs[0][1].equals("SELF") ? getResources().getString(R.string.pet_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                        (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                                (obs[0][1].equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                        (obs[0][1].equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                                obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                        obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                                obs[0][1].equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                        obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other))))))))));
                cnicOwner.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER")) {
                otherCnicOwner.getEditText().setText(obs[0][1]);
                otherCnicOwner.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TYPE OF VISIT")) {
                for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_baseline_visit)) && obs[0][1].equals("BASELINE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_followup)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INCENTIVE BENEFICIARY")) {
                for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_contact)) && obs[0][1].equals("PATIENT")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_treatment_supporter)) && obs[0][1].equals("TREATMENT SUPPORTER")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NAME OF TREATMENT SUPPORTER")) {
                nameTreatmentSupporter.getEditText().setText(obs[0][1]);
                nameTreatmentSupporter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER CONTACT NUMBER")) {
                String[] phoneArray = obs[0][1].split("-");
                phone1a.getEditText().setText(phoneArray[0]);
                phone1b.getEditText().setText(phoneArray[1]);
                contactNumberTreatmentSupporter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER TYPE")) {
                for (RadioButton rb : typeTreatmentSupporter.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_family_treatment_supporter)) && obs[0][1].equals("FAMILY MEMBER")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_non_family_treatment_supporter)) && obs[0][1].equals("NON-FAMILY MEMBER")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                typeTreatmentSupporter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER RELATIONSHIP TO PATIENT")) {
                String value = obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                        (obs[0][1].equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                        (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                        (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                        (obs[0][1].equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                (obs[0][1].equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                        obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                        obs[0][1].equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other)))))))));
                relationshipTreatmentSuppoter.getSpinner().selectValue(value);
                relationshipTreatmentSuppoter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                other.getEditText().setText(obs[0][1]);
                other.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("POST-EXPOSURE TREATMENT REGIMEN")) {
                for (RadioButton rb : petRegimen.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && obs[0][1].equals("ISONIAZID PROPHYLAXIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_isoniazid_rifapentine)) && obs[0][1].equals("ISONIAZID AND RIFAPENTINE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_levofloxacin_ethionamide)) && obs[0][1].equals("LEVOFLOXACIN AND ETHIONAMIDE")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                petRegimen.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INCENTIVE AMOUNT")) {
                String amount = obs[0][1].replace(".0", "");
                incentiveAmount.getEditText().setText(amount);
            } else if (obs[0][0].equals("MONTH OF INCENTIVE")) {
                String amount = obs[0][1].replace(".0", "");
                followupMonth.getEditText().setText(amount);
            } else if (obs[0][0].equals("LOCATION OF EVENT")) {
                for (RadioButton rb : incentiveDisbursalLocation.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_contact_home)) && obs[0][1].equals("HOME")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_facility)) && obs[0][1].equals("HEALTH FACILITY")) {
                        rb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("NAME OF INCENTIVE RECEIVER")) {
                recieverName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("RELATIONSHIP WITH INCENTIVE RECEIVER")) {
                String value = (obs[0][1].equals("SELF") ? getResources().getString(R.string.pet_self) :
                        (obs[0][1].equals("MOTHER") ? getResources().getString(R.string.pet_mother) :
                                (obs[0][1].equals("FATHER") ? getResources().getString(R.string.pet_father) :
                                        (obs[0][1].equals("MATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_maternal_grandmother) :
                                                (obs[0][1].equals("MATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_maternal_grandfather) :
                                                        (obs[0][1].equals("PATERNAL GRANDMOTHER") ? getResources().getString(R.string.pet_paternal_grandmother) :
                                                                (obs[0][1].equals("PATERNAL GRANDFATHER") ? getResources().getString(R.string.pet_paternal_grandfather) :
                                                                        (obs[0][1].equals("BROTHER") ? getResources().getString(R.string.pet_brother) :
                                                                                (obs[0][1].equals("SISTER") ? getResources().getString(R.string.pet_sister) :
                                                                                        (obs[0][1].equals("SON") ? getResources().getString(R.string.pet_son) :
                                                                                                obs[0][1].equals("DAUGHTER") ? getResources().getString(R.string.pet_daughter) :
                                                                                                        obs[0][1].equals("SPOUSE") ? getResources().getString(R.string.pet_spouse) :
                                                                                                                obs[0][1].equals("AUNT") ? getResources().getString(R.string.pet_aunt) :
                                                                                                                        obs[0][1].equals("UNCLE") ? getResources().getString(R.string.pet_uncle) : getResources().getString(R.string.pet_other)))))))))));
                recieverRelationWithContact.getSpinner().selectValue(value);
                recieverRelationWithContact.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER INCENTIVE RECEIVER")) {
                otherRelation.getEditText().setText(obs[0][1]);
                otherRelation.setVisibility(View.VISIBLE);
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

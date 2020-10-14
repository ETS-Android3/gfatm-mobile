package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetIncentiveDisbursementForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;


    TitledEditText indexPatientId;
    Button scanQRCode;
    TitledEditText indexExternalPatientId;
    LinearLayout cnicLayout;
    MyEditText cnic1;
    MyEditText cnic2;
    MyEditText cnic3;
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

    TitledButton incentiveDate;
    TitledEditText incentiveAmount;
    TitledEditText followupMonth;
    TitledRadioGroup incentiveDisbursalLocation;
    TitledEditText recieverName;
    TitledSpinner recieverRelationWithContact;
    TitledEditText otherRelation;

    Boolean refillFlag = false;
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 2;
        formName = Forms.PET_INCENTIVE_DISBURSEMENT;
        form = Forms.pet_incentiveDisbursement;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = pageCount - 1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < pageCount; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_payment_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "PATIENT ID OF INDEX CASE");
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        cnicLayout = new LinearLayout(context);
        cnicLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout cnicQuestionLayout = new LinearLayout(context);
        cnicQuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView mandatorycnicSign = new TextView(context);
        mandatorycnicSign.setText(" *");
        mandatorycnicSign.setTextColor(Color.parseColor("#ff0000"));
        cnicQuestionLayout.addView(mandatorycnicSign);
        MyTextView cnic = new MyTextView(context, getResources().getString(R.string.pet_cnic));
        cnicQuestionLayout.addView(cnic);
        cnicLayout.addView(cnicQuestionLayout);
        LinearLayout cnicPartLayout = new LinearLayout(context);
        cnicPartLayout.setOrientation(LinearLayout.HORIZONTAL);
        cnic1 = new MyEditText(context, "", 5, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic1.setHint("XXXXX");
        cnicPartLayout.addView(cnic1);
        MyTextView cnicDash = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash);
        cnic2 = new MyEditText(context, "", 7, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic2.setHint("XXXXXXX");
        cnicPartLayout.addView(cnic2);
        MyTextView cnicDash2 = new MyTextView(context, " - ");
        cnicPartLayout.addView(cnicDash2);
        cnic3 = new MyEditText(context, "", 1, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_PHONE);
        cnic3.setHint("X");
        cnicPartLayout.addView(cnic3);
        cnicLayout.addView(cnicPartLayout);
        cnicOwner = new TitledSpinner(context, "", getResources().getString(R.string.pet_cnic_owner), getResources().getStringArray(R.array.pet_cnic_owners), getResources().getString(R.string.pet_self), App.VERTICAL, true, "COMPUTERIZED NATIONAL IDENTIFICATION OWNER", new String[]{"SELF", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER"});
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
        incentiveOccasion = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_incentive_occasion), getResources().getStringArray(R.array.pet_incentive_occasions), getResources().getString(R.string.pet_baseline_visit), App.HORIZONTAL, App.VERTICAL, true, "TYPE OF VISIT", new String[]{"BASELINE", "REGULAR FOLLOW UP"});
        incentiveFor = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_incentive_for), getResources().getStringArray(R.array.pet_incentive_for), getResources().getString(R.string.pet_contact), App.HORIZONTAL, App.VERTICAL, true, "INCENTIVE BENEFICIARY", new String[]{"PATIENT", "TREATMENT SUPPORTER"});

        nameTreatmentSupporter = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "NAME OF TREATMENT SUPPORTER");
        contactNumberTreatmentSupporter = new LinearLayout(context);
        contactNumberTreatmentSupporter.setOrientation(LinearLayout.HORIZONTAL);
        phone1a = new TitledEditText(context, null, getResources().getString(R.string.pet_treatment_supporter_contact_number), "", "XXXX", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        contactNumberTreatmentSupporter.addView(phone1a);
        phone1b = new TitledEditText(context, null, "-", "", "XXXXXXX", 7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        contactNumberTreatmentSupporter.addView(phone1b);
        typeTreatmentSupporter = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_treatment_supporter_type), getResources().getStringArray(R.array.pet_treatment_supporter_type), getResources().getString(R.string.pet_family_treatment_supporter), App.VERTICAL, App.VERTICAL, true, "TREATMENT SUPPORTER TYPE", new String[]{"FAMILY MEMBER", "NON-FAMILY MEMBER"});
        relationshipTreatmentSuppoter = new TitledSpinner(context, "", getResources().getString(R.string.pet_treatment_supporter_relationship), getResources().getStringArray(R.array.pet_household_heads), getResources().getString(R.string.pet_mother), App.VERTICAL, true, "TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", new String[]{"MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER FAMILY MEMBER"});
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER FAMILY MEMBER");
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens_temp), "", App.VERTICAL, App.VERTICAL, true, "POST-EXPOSURE TREATMENT REGIMEN", new String[]{"ISONIAZID PROPHYLAXIS", "ISONIAZID AND RIFAPENTINE", "LEVOFLOXACIN AND ETHIONAMIDE"});

        MyLinearLayout linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_incentive_details), App.VERTICAL);
        incentiveDate = new TitledButton(context, null, getResources().getString(R.string.pet_incentive_date), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        incentiveAmount = new TitledEditText(context, null, getResources().getString(R.string.pet_incentive_amount), "500", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "INCENTIVE AMOUNT");
        followupMonth = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MONTH OF INCENTIVE");
        incentiveDisbursalLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_incentive_disbursal_location), getResources().getStringArray(R.array.pet_locations_of_entry), getResources().getString(R.string.pet_contact_home), App.HORIZONTAL, App.VERTICAL, true, "LOCATION OF EVENT", new String[]{"HOME", "HEALTH FACILITY"});
        recieverName = new TitledEditText(context, null, getResources().getString(R.string.pet_receiver_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "NAME OF INCENTIVE RECEIVER");
        recieverRelationWithContact = new TitledSpinner(context, "", getResources().getString(R.string.pet_receiver_relation_with_contact), getResources().getStringArray(R.array.pet_cnic_owners), getResources().getString(R.string.pet_self), App.VERTICAL, true, "RELATIONSHIP WITH INCENTIVE RECEIVER", new String[]{"SELF", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER"});
        otherRelation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER INCENTIVE RECEIVER");

        linearLayout.addView(incentiveDate);
        linearLayout.addView(incentiveAmount);
        linearLayout.addView(followupMonth);
        linearLayout.addView(incentiveDisbursalLocation);
        linearLayout.addView(recieverName);
        linearLayout.addView(recieverRelationWithContact);
        linearLayout.addView(otherRelation);

        views = new View[]{formDate.getButton(), incentiveDate.getButton(), indexPatientId.getEditText(), scanQRCode, indexExternalPatientId.getEditText(), cnic1, cnic2, cnic3, cnicOwner.getSpinner(), otherCnicOwner.getEditText(), incentiveOccasion.getRadioGroup(), incentiveFor.getRadioGroup(),
                nameTreatmentSupporter.getEditText(), phone1a.getEditText(), phone1b.getEditText(), typeTreatmentSupporter.getRadioGroup(), relationshipTreatmentSuppoter.getSpinner(), other.getEditText(), petRegimen.getRadioGroup(), incentiveAmount.getEditText(),
                followupMonth.getEditText(), incentiveDisbursalLocation.getRadioGroup(), recieverName.getEditText(), recieverRelationWithContact.getSpinner(), otherRelation.getEditText()

        };

        viewGroups = new View[][]{{formDate, indexPatientId, scanQRCode, indexExternalPatientId, cnicLayout, cnicOwner, otherCnicOwner, incentiveOccasion, incentiveFor,
                nameTreatmentSupporter, contactNumberTreatmentSupporter, typeTreatmentSupporter, relationshipTreatmentSuppoter, other, petRegimen},
                {linearLayout}};

        View listenerViewer[] = new View[]{formDate, incentiveDate, cnicOwner, incentiveOccasion, relationshipTreatmentSuppoter, recieverRelationWithContact, typeTreatmentSupporter};
        for (View v : listenerViewer) {

            if (v instanceof TitledButton)
                ((TitledButton) v).getButton().setOnClickListener(this);
            else if (v instanceof TitledRadioGroup)
                ((TitledRadioGroup) v).getRadioGroup().setOnCheckedChangeListener(this);
            else if (v instanceof TitledSpinner)
                ((TitledSpinner) v).getSpinner().setOnItemSelectedListener(this);

        }

        resetViews();

        cnic1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 5) {
                    cnic2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cnic2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 7) {
                    cnic3.requestFocus();
                }

                if (s.length() == 0) {
                    cnic1.requestFocus();
                    cnic1.setSelection(cnic1.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cnic3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    cnic2.requestFocus();
                    cnic2.setSelection(cnic2.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        indexPatientId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (start == 5 && s.length() == 5) {
                    int i = indexPatientId.getEditText().getSelectionStart();
                    if (i == 5) {
                        indexPatientId.getEditText().setText(indexPatientId.getEditText().getText().toString().substring(0, 4));
                        indexPatientId.getEditText().setSelection(4);
                    }
                } else if (s.length() == 5 && !s.toString().contains("-")) {
                    indexPatientId.getEditText().setText(s + "-");
                    indexPatientId.getEditText().setSelection(6);
                } else if (s.length() == 7 && !RegexUtil.isValidId(App.get(indexPatientId)))
                    indexPatientId.getEditText().setError(getString(R.string.invalid_id));
                else
                    indexPatientId.getEditText().setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = false;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);
        incentiveDate.getButton().setEnabled(true);

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
        incentiveDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        Calendar calendar = new GregorianCalendar(2017, 4, 1);
        if (secondDateCalendar.before(calendar))
            incentiveAmount.getEditText().setText("500");
        else
            incentiveAmount.getEditText().setText("600");


    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = super.validate();



        if (contactNumberTreatmentSupporter.getVisibility() == View.VISIBLE) {
            if (App.get(phone1a).isEmpty()) {
                phone1b.getEditText().setError(getResources().getString(R.string.mandatory_field));
                phone1b.getEditText().requestFocus();
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
            } else {
                phone1b.getEditText().setError(null);
                phone1b.getEditText().clearFocus();
            }
        }





        if (App.get(cnic1).isEmpty()) {
            cnic3.setError(getResources().getString(R.string.mandatory_field));
            cnic3.requestFocus();
            error = true;
            gotoFirstPage();
        } else {
            cnic3.setError(null);
            cnic3.clearFocus();
        }
        if (App.get(cnic2).isEmpty()) {
            cnic3.setError(getResources().getString(R.string.mandatory_field));
            cnic3.requestFocus();
            error = true;
            gotoFirstPage();
        } else {
            cnic3.setError(null);
            cnic3.clearFocus();
        }
        if (App.get(cnic3).isEmpty()) {
            cnic3.setError(getResources().getString(R.string.mandatory_field));
            cnic3.requestFocus();
            error = true;
            gotoFirstPage();
        } else {
            cnic3.setError(null);
            cnic3.clearFocus();
        }
        if (App.get(cnic1).length() != 5) {
            cnic3.setError(getResources().getString(R.string.invalid_value));
            cnic3.requestFocus();
            error = true;
            gotoFirstPage();
        } else {
            cnic3.setError(null);
            cnic3.clearFocus();
        }
        if (App.get(cnic2).length() != 7) {
            cnic3.setError(getResources().getString(R.string.invalid_value));
            cnic3.requestFocus();
            error = true;
            gotoFirstPage();
        } else {
            cnic3.setError(null);
            cnic3.clearFocus();
        }
        if (App.get(cnic3).length() != 1) {
            cnic3.setError(getResources().getString(R.string.invalid_value));
            cnic3.requestFocus();
            error = true;
            gotoFirstPage();
        } else {
            cnic3.setError(null);
            cnic3.clearFocus();
        }

        if (App.get(indexPatientId).isEmpty() && indexPatientId.getVisibility() == View.VISIBLE) {
            indexPatientId.getEditText().setError(getResources().getString(R.string.mandatory_field));
            indexPatientId.getEditText().requestFocus();
            error = true;
        } else if (!RegexUtil.isValidId(App.get(indexPatientId))) {
            indexPatientId.getEditText().setError(getResources().getString(R.string.invalid_id));
            indexPatientId.getEditText().requestFocus();
            error = true;
        } else if (App.getPatient().getPatientId().equals(App.get(indexPatientId))) {
            indexPatientId.getEditText().setError(getResources().getString(R.string.pet_index_contact_id_same_error));
            indexPatientId.getEditText().requestFocus();
            error = true;
        } else {
            indexPatientId.getEditText().setError(null);
            indexPatientId.getEditText().clearFocus();
        }

        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            final View finalView = view;
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            scrollView.post(new Runnable() {
                                public void run() {
                                    if (finalView != null) {
                                        scrollView.scrollTo(0, finalView.getTop());
                                        otherRelation.getEditText().clearFocus();
                                        recieverName.getEditText().clearFocus();
                                        followupMonth.getEditText().clearFocus();
                                        nameTreatmentSupporter.getEditText().clearFocus();
                                        phone1b.clearFocus();
                                        other.getEditText().clearFocus();
                                        otherCnicOwner.getEditText().clearFocus();
                                        cnic3.clearFocus();
                                        indexPatientId.getEditText().clearFocus();
                                    }
                                }
                            });
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

        final ArrayList<String[]> observations = getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if (!flag) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_does_not_exist));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    bundle.putBoolean("save", false);
                                    submit();
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.backToMainMenu();
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
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

                    return false;
                }
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


        final String cnic = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
        observations.add(new String[]{"NATIONAL IDENTIFICATION NUMBER", cnic});
        if (contactNumberTreatmentSupporter.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER CONTACT NUMBER", App.get(phone1a) + "-" + App.get(phone1b)});

        observations.add(new String[]{"INCENTIVE DATE", App.getSqlDate(secondDateCalendar)});

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

                String id = null;
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";

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
        followupMonth.setVisibility(View.GONE);

        Calendar calendar = new GregorianCalendar(2017, 4, 1);
        if (secondDateCalendar.before(calendar))
            incentiveAmount.getEditText().setText("500");
        else
            incentiveAmount.getEditText().setText("600");


        Bundle bundle = this.getArguments();
        Boolean autoFill = false;
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                autoFill = true;
                refill(formId);

            } else bundle.putBoolean("save", false);

        }

        if (!autoFill) {
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

                    String intervention = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "INTERVENTION");
                    String date1 = "";
                    String date2 = "";
                    String date3 = "";
                    String petRegimen1 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_ADVERSE_EVENTS, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen1 == null || petRegimen1.equals("")))
                        date1 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_ADVERSE_EVENTS);
                    String petRegimen2 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen2 == null || petRegimen2.equals("")))
                        date2 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_CLINICIAN_FOLLOWUP);
                    String petRegimen3 = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "POST-EXPOSURE TREATMENT REGIMEN");
                    if (!(petRegimen3 == null || petRegimen3.equals("")))
                        date3 = serverService.getLatestEncounterDateTime(App.getPatientId(), Forms.PET_TREATMENT_INITIATION);

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

                    String indexId = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "PATIENT ID OF INDEX CASE");
                    String cnicString = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "NATIONAL IDENTIFICATION NUMBER");
                    String cnicOwner = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                    String otherCnicOwner = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_BASELINE_SCREENING, "OTHER COMPUTERIZED NATIONAL IDENTIFICATION OWNER");
                    String treatmentSupporter = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "NAME OF TREATMENT SUPPORTER");
                    String treatmentSupporterContact = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "TREATMENT SUPPORTER CONTACT NUMBER");
                    String treatmentSupporterType = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "TREATMENT SUPPORTER TYPE");
                    String treatmentSupporterRelationship = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "TREATMENT SUPPORTER RELATIONSHIP TO PATIENT");
                    String treatmentSupporterRelationshipOther = serverService.getLatestObsValue(App.getPatientId(), Forms.PET_TREATMENT_INITIATION, "OTHER FAMILY MEMBER");

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

                    if (intervention == null)
                        intervention = "";
                    result.put("INTERVENTION", intervention);

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
                        if (cnicArray.length == 3) {
                            cnic1.setText(cnicArray[0]);
                            cnic2.setText(cnicArray[1]);
                            cnic3.setText(cnicArray[2]);
                        }
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
                        if (phoneArray.length == 2) {
                            phone1a.getEditText().setText(phoneArray[0]);
                            phone1b.getEditText().setText(phoneArray[1]);
                        }
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
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
        } else if (view == incentiveDate.getButton()) {
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            incentiveDate.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, false, true, true);

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
                followupMonth.setVisibility(View.GONE);
            } else {
                for (RadioButton rb : incentiveFor.getRadioGroup().getButtons()) {
                    rb.setVisibility(View.VISIBLE);
                }
                nameTreatmentSupporter.setVisibility(View.VISIBLE);
                contactNumberTreatmentSupporter.setVisibility(View.VISIBLE);
                typeTreatmentSupporter.setVisibility(View.VISIBLE);
                relationshipTreatmentSuppoter.setVisibility(View.VISIBLE);
                other.setVisibility(View.VISIBLE);
                followupMonth.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(encounterId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("External ID")) {
                indexExternalPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("PATIENT ID OF INDEX CASE")) {
                indexPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NATIONAL IDENTIFICATION NUMBER")) {
                String[] cnicArray = obs[0][1].split("-");
                cnic1.setText(cnicArray[0]);
                cnic2.setText(cnicArray[1]);
                cnic3.setText(cnicArray[2]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER CONTACT NUMBER")) {
                String[] phoneArray = obs[0][1].split("-");
                phone1a.getEditText().setText(phoneArray[0]);
                phone1b.getEditText().setText(phoneArray[1]);
                contactNumberTreatmentSupporter.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INCENTIVE AMOUNT")) {
                String amount = obs[0][1].replace(".0", "");
                incentiveAmount.getEditText().setText(amount);
            } else if (obs[0][0].equals("TREATMENT START DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                incentiveDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("MONTH OF INCENTIVE")) {
                String amount = obs[0][1].replace(".0", "");
                followupMonth.getEditText().setText(amount);
            }
        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageCount;
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

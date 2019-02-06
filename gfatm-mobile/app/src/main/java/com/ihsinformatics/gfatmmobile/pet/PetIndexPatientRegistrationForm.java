package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
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
import com.ihsinformatics.gfatmmobile.custom.MyEditText;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
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
 * Created by Rabbia on 11/24/2016.
 */

public class PetIndexPatientRegistrationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;

    TitledEditText husbandName;
    TitledEditText indexExternalPatientId;
    TitledEditText ernsNumber;
    TitledCheckBoxes diagonosisType;
    TitledRadioGroup tbType;
    TitledRadioGroup infectionType;
    TitledRadioGroup dstAvailable;
    TitledRadioGroup resistanceType;
    TitledSpinner patientType;
    TitledCheckBoxes dstPattern;
    TitledButton treatmentEnrollmentDate;

    LinearLayout phone1Layout;
    MyEditText phone1a;
    MyEditText phone1b;
    LinearLayout phone2Layout;
    MyEditText phone2a;
    MyEditText phone2b;
    TitledEditText address1;
    MyLinearLayout addressLayout;
    MyTextView townTextView;
    AutoCompleteTextView address2;
    TitledSpinner province;
    TitledSpinner district;
    TitledSpinner city;
    TitledRadioGroup addressType;
    TitledEditText landmark;

    ScrollView scrollView;

    Boolean refillFlag = false;


    /**
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.PET_INDEX_PATIENT_REGISTRATION;
        form = Forms.pet_indexPatientRegistration;

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
                scrollView.setScrollbarFadingEnabled(false);
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.pet_father_husband_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        husbandName.setTag("husbandName");
        husbandName.setFocusableInTouchMode(true);
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        indexExternalPatientId.setFocusableInTouchMode(true);
        ernsNumber = new TitledEditText(context, null, getResources().getString(R.string.pet_erns_number), "", "", RegexUtil.idLength, RegexUtil.ERNS_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, false);
        ernsNumber.setFocusableInTouchMode(true);
        diagonosisType = new TitledCheckBoxes(context, null, getResources().getString(R.string.fast_type_of_diagnosis), getResources().getStringArray(R.array.fast_diagonosis_type_list), null, App.VERTICAL, App.VERTICAL, true);
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_type), getResources().getStringArray(R.array.pet_tb_types), getResources().getString(R.string.pet_ptb), App.HORIZONTAL, App.VERTICAL);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_type), getResources().getStringArray(R.array.pet_infection_types), getResources().getString(R.string.pet_dstb), App.HORIZONTAL, App.VERTICAL);
        dstAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_dst_available), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        resistanceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_resistance_Type), getResources().getStringArray(R.array.pet_resistance_types), getResources().getString(R.string.pet_rr_tb), App.VERTICAL, App.VERTICAL);
        patientType = new TitledSpinner(context, "", getResources().getString(R.string.pet_index_dstb_type), getResources().getStringArray(R.array.pet_patient_types), getResources().getString(R.string.pet_new), App.HORIZONTAL);
        dstPattern = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_dst_pattern), getResources().getStringArray(R.array.pet_dst_patterns), null, App.VERTICAL, App.VERTICAL, true);
        treatmentEnrollmentDate = new TitledButton(context, null, getResources().getString(R.string.pet_treatment_enrollment), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);

        phone1Layout = new LinearLayout(context);
        phone1Layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout phone1QuestionLayout = new LinearLayout(context);
        phone1QuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView mandatoryPhone1Sign = new TextView(context);
        mandatoryPhone1Sign.setText(" *");
        mandatoryPhone1Sign.setTextColor(Color.parseColor("#ff0000"));
        phone1QuestionLayout.addView(mandatoryPhone1Sign);
        MyTextView phone1Text = new MyTextView(context, getResources().getString(R.string.pet_phone_1));
        phone1QuestionLayout.addView(phone1Text);
        phone1Layout.addView(phone1QuestionLayout);
        LinearLayout phone1PartLayout = new LinearLayout(context);
        phone1PartLayout.setOrientation(LinearLayout.HORIZONTAL);
        phone1a = new MyEditText(context,"", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        phone1a.setHint("0XXX");
        phone1PartLayout.addView(phone1a);
        MyTextView phone1Dash = new MyTextView(context, " - ");
        phone1PartLayout.addView(phone1Dash);
        phone1b = new MyEditText(context,"",  7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        phone1b.setHint("XXXXXXX");
        phone1PartLayout.addView(phone1b);
        phone1Layout.addView(phone1PartLayout);
        phone2Layout = new LinearLayout(context);
        phone2Layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout phone2QuestionLayout = new LinearLayout(context);
        phone2QuestionLayout.setOrientation(LinearLayout.HORIZONTAL);
        MyTextView phone2Text = new MyTextView(context, getResources().getString(R.string.pet_phone_2));
        phone2QuestionLayout.addView(phone2Text);
        phone2Layout.addView(phone2QuestionLayout);
        LinearLayout phone2PartLayout = new LinearLayout(context);
        phone2PartLayout.setOrientation(LinearLayout.HORIZONTAL);
        phone2a = new MyEditText(context, "", 4, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        phone2a.setHint("0XXX");
        phone2PartLayout.addView(phone2a);
        MyTextView phone2Dash = new MyTextView(context, " - ");
        phone2PartLayout.addView(phone2Dash);
        phone2b = new MyEditText(context, "",7, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE);
        phone2b.setHint("XXXXXXX");
        phone2PartLayout.addView(phone2b);
        phone2Layout.addView(phone2PartLayout);
        address1 = new TitledEditText(context, null, getResources().getString(R.string.pet_address_1), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        addressLayout = new MyLinearLayout(context, null, App.VERTICAL);
        townTextView = new MyTextView(context, getResources().getString(R.string.pet_address_2));
        address2 = new AutoCompleteTextView(context);
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(20);
        address2.setFilters(fArray);
        addressLayout.addView(townTextView);
        addressLayout.addView(address2);
        province = new TitledSpinner(context, "", getResources().getString(R.string.province), getResources().getStringArray(R.array.provinces), App.getProvince(), App.VERTICAL);
        district = new TitledSpinner(context, "", getResources().getString(R.string.pet_district), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        city = new TitledSpinner(context, "", getResources().getString(R.string.pet_city), getResources().getStringArray(R.array.pet_empty_array), "", App.VERTICAL);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_address_type), getResources().getStringArray(R.array.pet_address_types), getResources().getString(R.string.pet_permanent), App.HORIZONTAL, App.VERTICAL);
        landmark = new TitledEditText(context, null, getResources().getString(R.string.pet_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        views = new View[]{formDate.getButton(), husbandName.getEditText(), indexExternalPatientId.getEditText(), ernsNumber.getEditText(),
                diagonosisType, tbType.getRadioGroup(), infectionType.getRadioGroup(), dstAvailable.getRadioGroup(), resistanceType.getRadioGroup(),
                patientType.getSpinner(), dstPattern, phone1a, phone1b, phone2a, phone2b,
                address1.getEditText(), province.getSpinner(), district.getSpinner(), city.getSpinner(),
                addressType.getRadioGroup(), landmark.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, husbandName, indexExternalPatientId, ernsNumber, diagonosisType, tbType, infectionType, dstAvailable, resistanceType,
                        patientType, dstPattern, treatmentEnrollmentDate,  phone1Layout, phone2Layout, address1, addressLayout, province, district, city, addressType, landmark}};

        formDate.getButton().setOnClickListener(this);
        treatmentEnrollmentDate.getButton().setOnClickListener(this);
        dstAvailable.getRadioGroup().setOnCheckedChangeListener(this);
        infectionType.getRadioGroup().setOnCheckedChangeListener(this);
        district.getSpinner().setOnItemSelectedListener(this);
        province.getSpinner().setOnItemSelectedListener(this);
        for (CheckBox cb : dstPattern.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();

        phone1a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    phone1b.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone1b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    phone1a.requestFocus();
                    phone1a.setSelection(phone1a.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone2a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==4){
                    phone2b.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phone2b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0){
                    phone2a.requestFocus();
                    phone2a.setSelection(phone2a.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = false;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);
        treatmentEnrollmentDate.getButton().setEnabled(true);

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

        String formDa = treatmentEnrollmentDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();

        Date date = new Date();
        if (secondDateCalendar.after(formDateCalendar)) {

            secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.enrollment_date_error), Snackbar.LENGTH_INDEFINITE);
            snackbar.show();

            treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

        } else if (secondDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
            secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
            snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(2);
            snackbar.show();
            treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

        } else
            treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());


    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        if(!(App.get(phone2a).equals("") && App.get(phone2b).equals(""))){
            if (!RegexUtil.isContactNumber(App.get(phone2a) + App.get(phone2b))) {
                phone2b.setError(getResources().getString(R.string.invalid_value));
                phone2b.requestFocus();
                error = true;
            }else {
                phone2b.setError(null);
                phone2b.clearFocus();
            }
        }
        if (App.get(phone1a).isEmpty()) {
            phone1b.setError(getResources().getString(R.string.mandatory_field));
            phone1b.requestFocus();
            error = true;
        } else if (App.get(phone1b).isEmpty()) {
            phone1b.setError(getResources().getString(R.string.mandatory_field));
            phone1b.requestFocus();
            error = true;
        } else if (!RegexUtil.isContactNumber(App.get(phone1a) + App.get(phone1b))) {
            phone1b.setError(getResources().getString(R.string.invalid_value));
            phone1b.requestFocus();
            error = true;
        } else {
            phone1b.setError(null);
            phone1b.clearFocus();
        }

        Boolean flag = false;

        if (dstPattern.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                dstPattern.getQuestionView().setError(getString(R.string.empty_field));
                dstPattern.getQuestionView().requestFocus();
                view = dstPattern;
                error = true;
            }else{
                dstPattern.getQuestionView().setError(null);
                dstPattern.getQuestionView().clearFocus();
            }
        }

        flag = false;
        if (diagonosisType.getVisibility() == View.VISIBLE) {
            for (CheckBox cb : diagonosisType.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                diagonosisType.getQuestionView().setError(getString(R.string.empty_field));
                diagonosisType.getQuestionView().requestFocus();
                view = diagonosisType;
                error = true;
            }else{
                diagonosisType.getQuestionView().setError(null);
                diagonosisType.getQuestionView().clearFocus();
            }
        }

        if (!App.get(ernsNumber).equals("") && !RegexUtil.isValidErnsNumber(App.get(ernsNumber))) {
            ernsNumber.getEditText().setError(getString(R.string.invalid_value));
            ernsNumber.getEditText().requestFocus();
            view = null;
            error = true;
        }else{
            ernsNumber.getEditText().setError(null);
            ernsNumber.getEditText().clearFocus();
        }

        if (App.get(husbandName).isEmpty()) {
            husbandName.getEditText().setError(getString(R.string.empty_field));
            husbandName.getEditText().requestFocus();
            view = null;
            error = true;
        } else if (App.get(husbandName).length() <= 2) {
            husbandName.getEditText().setError(getString(R.string.invalid_value));
            husbandName.getEditText().requestFocus();
            error = true;
            view = null;
        } else{
            husbandName.getEditText().setError(null);
            husbandName.getEditText().clearFocus();
        }

        indexExternalPatientId.clearFocus();

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
                                        husbandName.clearFocus();
                                        indexExternalPatientId.clearFocus();
                                        ernsNumber.clearFocus();
                                        phone1b.clearFocus();
                                        phone2b.clearFocus();
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
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
        final ArrayList<String[]> observations = new ArrayList<String[]>();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        if((App.hasKeyListener(husbandName))){
            observations.add(new String[]{"HUSBAND NAME", App.get(husbandName)});
            personAttribute.put("Guardian Name", App.get(husbandName));
        }
        String diagonosisTypeString = "";
        for (CheckBox cb : diagonosisType.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_bactoriologically_confirmed)))
                diagonosisTypeString = diagonosisTypeString + "PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.fast_clinically_diagnosed)))
                diagonosisTypeString = diagonosisTypeString + "CLINICAL SUSPICION" + " ; ";
        }
        observations.add(new String[]{"TUBERCULOSIS DIAGNOSIS METHOD", diagonosisTypeString});
        observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.pet_ptb)) ? "PULMONARY TUBERCULOSIS" : "EXTRA-PULMONARY TUBERCULOSIS"});
        observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(infectionType).equals(getResources().getString(R.string.pet_dstb)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" : "DRUG-RESISTANT TB"});
        if (dstAvailable.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DST RESULT AVAILABLE", App.get(dstAvailable).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (resistanceType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS DRUG RESISTANCE TYPE", App.get(resistanceType).equals(getResources().getString(R.string.pet_rr_tb)) ? "RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION" :
                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_dr_tb)) ? "MONO DRUG RESISTANT TUBERCULOSIS" :
                            (App.get(resistanceType).equals(getResources().getString(R.string.pet_pdr_tb)) ? "PANDRUG RESISTANT TUBERCULOSIS" :
                                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_mdr_tb)) ? "MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION" :
                                            (App.get(resistanceType).equals(getResources().getString(R.string.pet_xdr_tb)) ? "EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION"  :
                                                    (App.get(resistanceType).equals(getResources().getString(R.string.pet_pre_xdr_fq)) ? "Pre-XDR - FQ"  : "Pre-XDR - INJ")))))});
        observations.add(new String[]{"TB PATIENT TYPE", App.get(patientType).equals(getResources().getString(R.string.pet_new)) ? "NEW TB PATIENT" :
                (App.get(patientType).equals(getResources().getString(R.string.pet_relapse)) ? "RELAPSE" :
                        (App.get(patientType).equals(getResources().getString(R.string.pet_cat1)) ? "FAILURE OF CATEGORY I TREATMENT" :
                                (App.get(patientType).equals(getResources().getString(R.string.pet_cat2))) ? "FAILURE OF CATEGORY II TREATMENT" :
                                        (App.get(patientType).equals(getResources().getString(R.string.pet_mdr))) ? "FAILURE OF MDR-TB TREATMENT" :
                                                (App.get(patientType).equals(getResources().getString(R.string.pet_treatment_failure))) ? "FAILURE OF PREVIOUS TREATMENT" :
                                                        (App.get(patientType).equals(getResources().getString(R.string.pet_loss_of_followup_type))) ? "LOST TO FOLLOW-UP" :
                                                                (App.get(patientType).equals(getResources().getString(R.string.unknown))) ? "UNKNOWN" : "OTHER"))});
        if (dstPattern.getVisibility() == View.VISIBLE) {
            String dstPatternString = "";
            for(CheckBox cb : dstPattern.getCheckedBoxes()){
                if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_isoniazid)))
                    dstPatternString = dstPatternString + "ISONIAZID" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rifampicin)))
                    dstPatternString = dstPatternString + "RIFAMPICIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_amikacin)))
                    dstPatternString = dstPatternString + "AMIKACIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_capreomycin)))
                    dstPatternString = dstPatternString + "CAPREOMYCIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)))
                    dstPatternString = dstPatternString + "STREPTOMYCIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)))
                    dstPatternString = dstPatternString + "OFLOXACIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)))
                    dstPatternString = dstPatternString + "MOXIFLOXACIN" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethambutol)))
                    dstPatternString = dstPatternString + "ETHAMBUTOL" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ethionamide)))
                    dstPatternString = dstPatternString + "ETHIONAMIDE" + " ; ";
                else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)))
                    dstPatternString = dstPatternString + "PYRAZINAMIDE" + " ; ";
            }
            observations.add(new String[]{"RESISTANT TO ANTI-TUBERCULOSIS DRUGS", dstPatternString});
        }
        observations.add(new String[]{"TREATMENT ENROLLMENT DATE", App.getSqlDate(secondDateCalendar)});

        observations.add(new String[]{"CONTACT PHONE NUMBER", App.get(phone1a) + "-" + App.get(phone1b)});
        personAttribute.put("Primary Contact", App.get(phone1a) + "-" + App.get(phone1b));

        if (!App.get(phone2a).equals("")) {
            observations.add(new String[]{"SECONDARY MOBILE NUMBER", App.get(phone2a) + "-" + App.get(phone2b)});
            personAttribute.put("Secondary Contact", App.get(phone2a) + "-" + App.get(phone2b));
        }
        observations.add(new String[]{"ADDRESS (TEXT)", App.get(address1)});
        observations.add(new String[]{"EXTENDED PERMANENT ADDRESS (TEXT)", App.get(address2)});
        observations.add(new String[]{"PROVINCE", App.get(province)});
        observations.add(new String[]{"DISTRICT", App.get(district)});
        observations.add(new String[]{"VILLAGE", App.get(city)});
        observations.add(new String[]{"NEAREST LANDMARK", App.get(landmark)});
        observations.add(new String[]{"TYPE OF ADDRESS", App.get(addressType).equals(getResources().getString(R.string.pet_permanent)) ? "PERMANENT ADDRESS" : "TEMPORARY ADDRESS"});

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
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                if (App.hasKeyListener(ernsNumber)) {
                    result = serverService.saveIdentifier("ENRS", App.get(ernsNumber), id);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

                if (!App.get(indexExternalPatientId).isEmpty() && App.hasKeyListener(indexExternalPatientId)) {
                    result = serverService.saveIdentifier("External ID", App.get(indexExternalPatientId), id);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

                if (!(App.get(address1).equals("") && App.get(address2).equals("") && App.get(district).equals("") && App.get(landmark).equals(""))) {
                    result = serverService.savePersonAddress(App.get(address1), App.get(address2), App.get(city), App.get(district), App.get(province), App.getCountry(), App.getLongitude(), App.getLatitude(), App.get(landmark), id);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

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

                    serverService.addTown(address2.getText().toString());

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
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(husbandName.getTag(), App.get(husbandName));

        return true;
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
            formDate.getButton().setEnabled(false);
        } else if (view == treatmentEnrollmentDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
            treatmentEnrollmentDate.getButton().setEnabled(false);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;

        if (spinner == district.getSpinner()) {

            if(district.getSpinner().getTag() == null) {

                String[] cities = serverService.getCityList(App.get(district));
                city.getSpinner().setSpinnerData(cities);
            }
            else city.getSpinner().setTag(null);

        } else if (spinner == province.getSpinner()) {

            if(province.getSpinner().getTag() == null) {
                String[] districts = serverService.getDistrictList(App.get(province));
                district.getSpinner().setSpinnerData(districts);
            }
            else province.getSpinner().setTag(null);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        address2.setText("");
        Object[][]  towns = serverService.getAllTowns();
        String[] townList = new String[towns.length];

        for (int i = 0; i < towns.length; i++) {
            townList[i] = String.valueOf(towns[i][1]);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, townList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        address2.setAdapter(spinnerArrayAdapter);

        String[] districts = serverService.getDistrictList(App.getProvince());
        district.getSpinner().setSpinnerData(districts);

        String[] cities = serverService.getCityList(App.get(district));
        city.getSpinner().setSpinnerData(cities);

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();

        for (RadioButton rb : rbs) {
            if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb)))
                rb.setVisibility(View.VISIBLE);
            else {
                rb.setChecked(false);
                rb.setVisibility(View.GONE);
            }
        }
        dstPattern.setVisibility(View.GONE);
        dstAvailable.setVisibility(View.GONE);
        resistanceType.setVisibility(View.GONE);

        husbandName.getEditText().requestFocus();

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

            } else
                bundle.putBoolean("save", false);
        }

        if(!autoFill) {
            if (App.get(ernsNumber).equals("")) {
                String enrsId = App.getPatient().getEnrs();

                if (enrsId == null || enrsId.equals("")) {
                    ernsNumber.getEditText().setText("");
                } else {
                    ernsNumber.getEditText().setText(enrsId);
                    ernsNumber.getEditText().setKeyListener(null);
                }
            }

            if (App.get(indexExternalPatientId).equals("")) {
                String externalId = App.getPatient().getExternalId();
                if (externalId == null || externalId.equals("")) {
                    indexExternalPatientId.getEditText().setText("");
                } else {
                    indexExternalPatientId.getEditText().setText(externalId);
                    indexExternalPatientId.getEditText().setKeyListener(null);
                }
            }

            if (App.get(husbandName).equals("")) {
                String husbandNameString = App.getPatient().getPerson().getPersonAttribute("Guardian Name");
                if (husbandNameString == null || husbandNameString.equals("")) {
                    husbandName.getEditText().setText("");
                } else {
                    husbandName.getEditText().setText(husbandNameString);
                    husbandName.getEditText().setKeyListener(null);
                }
            }

            String tbTypeString = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation", "SITE OF TUBERCULOSIS DISEASE");
            String diagnosisTypeString = serverService.getLatestObsValue(App.getPatientId(), "FAST-Treatment Initiation", "TUBERCULOSIS DIAGNOSIS METHOD");

            if (diagnosisTypeString != null) {
                for (CheckBox cb : diagonosisType.getCheckedBoxes()) {
                    if (diagnosisTypeString.contains("PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY") && cb.getText().equals(getResources().getString(R.string.fast_bactoriologically_confirmed)))
                        cb.setChecked(true);
                    if (diagnosisTypeString.contains("CLINICAL SUSPICION") && cb.getText().equals(getResources().getString(R.string.fast_clinically_diagnosed)))
                        cb.setChecked(true);
                }


                /*for (CheckBox rb : diagonosisType.getCheckedBoxes()) {
                    rb.setClickable(false);
                }*/
            }

            if (tbTypeString != null) {
                for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_ptb)) && tbTypeString.equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_eptb)) && tbTypeString.equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                    }
                }

                /*for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    rb.setClickable(false);
                }*/
            }
        }

    }

    /*@Override
    public void onPageSelected(int pageNo) {

        if (!App.isLanguageRTL()) {
            if (pageNo == 3 && dstAvailable.getRadioGroup().getSelectedValue().equalsIgnoreCase(getString(R.string.no))) {

                if (getCurrentPageNo() == 3)
                    pageNo = 4;
                else if (getCurrentPageNo() == 5)
                    pageNo = 2;
            }
        } else {
            if (pageNo == 2 && dstAvailable.getRadioGroup().getSelectedValue().equalsIgnoreCase(getString(R.string.no))) {

                if (getCurrentPageNo() == 3)
                    pageNo = 1;
                else if (getCurrentPageNo() == 5)
                    pageNo = 3;
            }
        }


        gotoPage(pageNo);

    }*/

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == infectionType.getRadioGroup()) {

            ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();
            String value = App.get(infectionType);
            if (value.equals(getResources().getString(R.string.pet_dstb))) {
                dstAvailable.setVisibility(View.GONE);
                resistanceType.setVisibility(View.GONE);
                dstPattern.setVisibility(View.GONE);
            } else {

                dstAvailable.setVisibility(View.VISIBLE);
                value = App.get(dstAvailable);
                resistanceType.setVisibility(View.VISIBLE);
                if (value.equals(getResources().getString(R.string.no))) {

                    for (RadioButton rb : rbs) {

                        if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb))) {
                            rb.setVisibility(View.VISIBLE);
                            rb.setChecked(true);
                        } else {
                            rb.setChecked(false);
                            rb.setVisibility(View.GONE);
                        }

                    }

                    dstPattern.setVisibility(View.GONE);

                } else {

                    for (RadioButton rb : rbs) {

                        rb.setVisibility(View.VISIBLE);

                    }

                    dstPattern.setVisibility(View.VISIBLE);

                }

            }

        } else if (group == dstAvailable.getRadioGroup()) {

            ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();
            String value = App.get(dstAvailable);
            if (value.equals(getResources().getString(R.string.no))) {

                for (RadioButton rb : rbs) {

                    if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb))) {
                        rb.setVisibility(View.VISIBLE);
                        rb.setChecked(true);
                    }
                    else {
                        rb.setChecked(false);
                        rb.setVisibility(View.GONE);
                    }

                }

                dstPattern.setVisibility(View.GONE);

            } else {

                for (RadioButton rb : rbs) {

                    rb.setVisibility(View.VISIBLE);

                }

                dstPattern.setVisibility(View.VISIBLE);

            }

        }

    }

    @Override
    public void refill(int formId) {

        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        province.getSpinner().setOnItemSelectedListener(null);
        district.getSpinner().setOnItemSelectedListener(null);

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }  else if (obs[0][0].equals("External ID")) {
                indexExternalPatientId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ENRS")) {
                ernsNumber.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("Gaurdian Name")) {
                husbandName.getEditText().setText(obs[0][1]);
            }  else if (obs[0][0].equals("TUBERCULOSIS DIAGNOSIS METHOD")) {
                for (CheckBox cb : diagonosisType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.fast_bactoriologically_confirmed)) && obs[0][1].equals("PRIMARY RESPIRATORY TUBERCULOSIS, CONFIRMED BACTERIOLOGICALLY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.fast_clinically_diagnosed)) && obs[0][1].equals("CLINICAL SUSPICION")) {
                        cb.setChecked(true);
                        break;
                    }
                }
                diagonosisType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {

                for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_ptb)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_eptb)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("TUBERCULOSIS INFECTION TYPE")) {
                for (RadioButton rb : infectionType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_dstb)) && obs[0][1].equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_drtb)) && obs[0][1].equals("DRUG-RESISTANT TB")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("DST RESULT AVAILABLE")) {
                for (RadioButton rb : dstAvailable.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);

                        ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();
                        for (RadioButton button : rbs) {

                            button.setVisibility(View.VISIBLE);

                        }

                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);

                        ArrayList<RadioButton> rbs = resistanceType.getRadioGroup().getButtons();
                        for (RadioButton button : rbs) {
                            if (button.getText().equals(getResources().getString(R.string.pet_rr_tb))) {
                                button.setVisibility(View.VISIBLE);
                            } else button.setVisibility(View.GONE);

                        }

                        break;
                    }
                }
                dstAvailable.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TUBERCULOSIS DRUG RESISTANCE TYPE")) {
                for (RadioButton rb : resistanceType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_rr_tb)) && obs[0][1].equals("RIFAMPICIN RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_dr_tb)) && obs[0][1].equals("MONO DRUG RESISTANT TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_pdr_tb)) && obs[0][1].equals("PANDRUG RESISTANT TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_mdr_tb)) && obs[0][1].equals("MULTI-DRUG RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_xdr_tb)) && obs[0][1].equals("EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_pre_xdr_fq)) && obs[0][1].equals("Pre-XDR - FQ")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_pre_xdr_inj)) && obs[0][1].equals("Pre-XDR - INJ")) {
                        rb.setChecked(true);
                        break;
                    }

                }
                resistanceType.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TB PATIENT TYPE")) {
                String value = obs[0][1].equals("NEW TB PATIENT") ? getResources().getString(R.string.pet_new) :
                        (obs[0][1].equals("RELAPSE") ? getResources().getString(R.string.pet_relapse) :
                                (obs[0][1].equals("FAILURE OF CATEGORY I TREATMENT") ? getResources().getString(R.string.pet_cat1) :
                                        (obs[0][1].equals("FAILURE OF MDR-TB TREATMENT") ? getResources().getString(R.string.pet_cat2) :
                                                (obs[0][1].equals("FAILURE OF MDR-TB TREATMENT") ? getResources().getString(R.string.pet_mdr) :
                                                        (obs[0][1].equals("FAILURE OF PREVIOUS TREATMENT") ? getResources().getString(R.string.pet_treatment_failure) :
                                                                (obs[0][1].equals("LOST TO FOLLOW-UP") ? getResources().getString(R.string.pet_loss_of_followup_type) :
                                                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.pet_unknown) : getResources().getString(R.string.pet_previously_treated))))))));
                patientType.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("RESISTANT TO ANTI-TUBERCULOSIS DRUGS")) {
                for (CheckBox cb : dstPattern.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_isoniazid)) && obs[0][1].equals("ISONIAZID")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_rifampicin)) && obs[0][1].equals("RIFAMPICIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_amikacin)) && obs[0][1].equals("AMIKACIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_capreomycin)) && obs[0][1].equals("CAPREOMYCIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_streptpmycin)) && obs[0][1].equals("STREPTOMYCIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ofloxacin)) && obs[0][1].equals("OFLOXACIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_moxifloxacin)) && obs[0][1].equals("MOXIFLOXACIN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ethambutol)) && obs[0][1].equals("ETHAMBUTOL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_ethionamide)) && obs[0][1].equals("ETHIONAMIDE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_pyrazinamide)) && obs[0][1].equals("PYRAZINAMIDE")) {
                        cb.setChecked(true);
                        break;
                    }

                }
                dstPattern.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("TREATMENT ENROLLMENT DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                treatmentEnrollmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("CONTACT PHONE NUMBER")) {
                String[] phoneArray = obs[0][1].split("-");
                phone1a.setText(phoneArray[0]);
                phone1b.setText(phoneArray[1]);
            } else if (obs[0][0].equals("SECONDARY MOBILE NUMBER")) {
                String[] phoneArray = obs[0][1].split("-");
                phone2a.setText(phoneArray[0]);
                phone2b.setText(phoneArray[1]);
            } else if (obs[0][0].equals("NEAREST LANDMARK")) {
                landmark.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ADDRESS (TEXT)")) {
                address1.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("EXTENDED PERMANENT ADDRESS (TEXT)")) {
                address2.setText(obs[0][1]);
            } else if (obs[0][0].equals("PROVINCE")) {
                province.getSpinner().selectValue(obs[0][1]);
            } else if (obs[0][0].equals("DISTRICT")) {
                String[] districts = serverService.getDistrictList(App.get(province));
                district.getSpinner().setSpinnerData(districts);
                district.getSpinner().selectValue(obs[0][1]);
                district.getSpinner().setTag("selected");
            } else if (obs[0][0].equals("VILLAGE")) {
                String[] cities = serverService.getCityList(App.get(district));
                city.getSpinner().setSpinnerData(cities);
                city.getSpinner().selectValue(obs[0][1]);
                city.getSpinner().setTag("selected");
            } else if (obs[0][0].equals("TYPE OF ADDRESS")) {
                for (RadioButton rb : addressType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet_permanent)) && obs[0][1].equals("PERMANENT ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pet_temporary)) && obs[0][1].equals("TEMPORARY ADDRESS")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
        }

        province.getSpinner().setOnItemSelectedListener(this);
        district.getSpinner().setOnItemSelectedListener(this);

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

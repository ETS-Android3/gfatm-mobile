package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.Preferences;
import com.ihsinformatics.gfatmmobile.R;
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
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.ihsinformatics.gfatmmobile.R.id.auto;
import static com.ihsinformatics.gfatmmobile.R.id.selectPatientId;

/**
 * Created by Tahira on 3/28/2017.
 */

public class PmdtContactBaselineScreening extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    Context context;
    TitledButton formDate;
    TitledButton baselineContactScreeningDate;
    TitledEditText indexPatientId;
    ImageView validateIndexPatientIdView;
    Button scanQRCode;
    TitledSpinner relationWithIndex;
    TitledEditText otherRelationWithIndex;
    TitledRadioGroup contactCough;              //title : Contact Verbal Symptom Screening
    TitledSpinner contactCoughDuration;
    TitledRadioGroup contactHaemoptysis;
    TitledRadioGroup contactFever;
    TitledRadioGroup contactWeightLoss;
    TitledRadioGroup contactReducedAppetite;
    TitledRadioGroup contactReducedActivity;
    TitledRadioGroup contactNightSweats;
    TitledRadioGroup contactGlandularSwelling;
    TitledRadioGroup contactReferred;
    LinearLayout facilityLinearLayout;
    TextView referredFacilityText;
    AutoCompleteTextView referredFacilityAutoCompleteList;

    ScrollView scrollView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 2;
        FORM_NAME = Forms.PMDT_CONTACT_BASELINE_SCREENING;
        FORM = Forms.pmdtContactBaselineScreening;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        baselineContactScreeningDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_baseline_screening_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        LinearLayout linearLayout = new LinearLayout(context);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        indexPatientId.setLayoutParams(param);
        linearLayout.addView(indexPatientId);
        validateIndexPatientIdView = new ImageView(context);
        validateIndexPatientIdView.setImageResource(R.drawable.ic_checked);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        validateIndexPatientIdView.setLayoutParams(param1);
        validateIndexPatientIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        validateIndexPatientIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(validateIndexPatientIdView);

        relationWithIndex = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_relation_with_index_patient), getResources().getStringArray(R.array.pmdt_contact_relation_with_index), getResources().getString(R.string.pmdt_other), App.VERTICAL, true);
        otherRelationWithIndex = new TitledEditText(context, null, getResources().getString(R.string.pmdt_relation_with_index_patient_other), "", "", 15, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactCough = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_contact_verbal_symptom_screening), getResources().getString(R.string.pmdt_contact_cough), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactCoughDuration = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_contact_cough_duration), getResources().getStringArray(R.array.pmdt_cough_durations), getResources().getString(R.string.pmdt_less_than_two_weeks), App.VERTICAL);
        contactHaemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_haemoptysis), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactFever = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_fever), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactReducedAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_reduced_appetite), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactWeightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_weight_loss), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactReducedActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_reduced_activity), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactNightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_night_sweats), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactGlandularSwelling = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_glandular_swelling), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        contactReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_referred), getResources().getStringArray(R.array.pmdt_yes_no_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(program);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        referredFacilityText = new TextView(context);
        referredFacilityText.setText(getResources().getString(R.string.pmdt_contact_referred_facility));
        referredFacilityAutoCompleteList = new AutoCompleteTextView(context);
        final ArrayAdapter<String> autoCompleteFacilityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, locationArray);
        referredFacilityAutoCompleteList.setAdapter(autoCompleteFacilityAdapter);
        referredFacilityAutoCompleteList.setHint("Enter facility");
        facilityLinearLayout = new LinearLayout(context);
        facilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        facilityLinearLayout.addView(referredFacilityText);
        facilityLinearLayout.addView(referredFacilityAutoCompleteList);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), baselineContactScreeningDate.getButton(), indexPatientId.getEditText(), relationWithIndex.getSpinner(), otherRelationWithIndex.getEditText(), contactCough.getRadioGroup(), contactCoughDuration.getSpinner(),
                contactHaemoptysis.getRadioGroup(), contactFever.getRadioGroup(), contactWeightLoss.getRadioGroup(), contactReducedAppetite.getRadioGroup(), contactReducedActivity.getRadioGroup(), contactNightSweats.getRadioGroup(),
                contactGlandularSwelling.getRadioGroup(), contactReferred.getRadioGroup(), referredFacilityAutoCompleteList};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, baselineContactScreeningDate, linearLayout, scanQRCode, relationWithIndex, otherRelationWithIndex, contactCough, contactCoughDuration, contactHaemoptysis, contactFever},
                        {contactWeightLoss, contactReducedAppetite, contactReducedActivity, contactNightSweats, contactGlandularSwelling, contactReferred, facilityLinearLayout}};

        formDate.getButton().setOnClickListener(this);
        baselineContactScreeningDate.getButton().setOnClickListener(this);
        scanQRCode.setOnClickListener(this);
        relationWithIndex.getSpinner().setOnItemSelectedListener(this);
        contactCough.getRadioGroup().setOnCheckedChangeListener(this);
        contactReferred.getRadioGroup().setOnCheckedChangeListener(this);
        validateIndexPatientIdView.setOnTouchListener(this);

        indexPatientId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (indexPatientId.getEditText().getText().length() == 7) {
                        if (!RegexUtil.isValidId(App.get(indexPatientId))) {
                            indexPatientId.getEditText().setError("id incorrect");
                            validateIndexPatientIdView.setVisibility(View.INVISIBLE);
                        } else {
                            validateIndexPatientIdView.setVisibility(View.VISIBLE);
                            validateIndexPatientIdView.setImageResource(R.drawable.ic_checked);
                        }
                    } else {
                        validateIndexPatientIdView.setVisibility(View.INVISIBLE);
                    }
                    submitButton.setEnabled(false);


                } catch (NumberFormatException nfe) {
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        resetViews();

    }

    @Override
    public void updateDisplay() {
        baselineContactScreeningDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }

        if (secondDateCalendar.after(formDateCalendar)) {
            baselineContactScreeningDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            Date date1 = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
            secondDateCalendar = App.getCalendar(date1);
        }

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        String facilityText = App.get(referredFacilityAutoCompleteList);
        boolean facilityVisibility = (facilityLinearLayout.getVisibility() == View.VISIBLE) ? true : false;

        if (App.get(otherRelationWithIndex).isEmpty() && otherRelationWithIndex.getVisibility() == View.VISIBLE) {
            otherRelationWithIndex.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherRelationWithIndex.getEditText().requestFocus();
            error = true;
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
            indexPatientId.getEditText().setError(getResources().getString(R.string.pmdt_index_contact_id_same_error));
            indexPatientId.getEditText().requestFocus();
            error = true;
        }
        if ("".equals(App.get(referredFacilityAutoCompleteList)) && facilityLinearLayout.getVisibility() == View.VISIBLE) {
            referredFacilityAutoCompleteList.setError(getResources().getString(R.string.mandatory_field));
            referredFacilityAutoCompleteList.requestFocus();
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

        observations.add(new String[]{"TIME TAKEN TO FILL FORM", String.valueOf(App.getTimeDurationBetween(startTime, endTime))});
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});

        observations.add(new String[]{"PATIENT ID OF INDEX CASE", App.get(indexPatientId)});
        observations.add(new String[]{"BASELINE CONTACT SCREENING DATE", App.getSqlDate(secondDateCalendar)});

        observations.add(new String[]{"FAMILY MEMBER", App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_mother)) ? "MOTHER" :
                (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_father)) ? "FATHER" :
                        (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                        (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                        (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_brother)) ? "BROTHER" :
                                                                (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_sister)) ? "SISTER" :
                                                                        (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_son)) ? "SON" :
                                                                                (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_daughter)) ? "DAUGHTER" :
                                                                                        (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_spouse)) ? "SPOUSE" :
                                                                                                (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_aunt)) ? "AUNT" :
                                                                                                        (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER"))))))))))))});
        if (otherRelationWithIndex.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(otherRelationWithIndex)});

        observations.add(new String[]{"COUGH", App.get(contactCough).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactCough).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactCough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        if (contactCoughDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(contactCoughDuration).equals(getResources().getString(R.string.pmdt_less_than_two_weeks)) ? "COUGH LASTING LESS THAN 2 WEEKS" :
                    (App.get(contactCoughDuration).equals(getResources().getString(R.string.pmdt_two_to_three_weeks)) ? "COUGH LASTING MORE THAN 2 WEEKS" :
                            (App.get(contactCoughDuration).equals(getResources().getString(R.string.pmdt_more_than_three_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                    (App.get(contactCoughDuration).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED")))});

        if (contactHaemoptysis.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEMOPTYSIS", App.get(contactHaemoptysis).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(contactHaemoptysis).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(contactHaemoptysis).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"FEVER", App.get(contactFever).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactFever).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactFever).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"WEIGHT LOSS", App.get(contactWeightLoss).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactWeightLoss).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactWeightLoss).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"LOSS OF APPETITE", App.get(contactReducedAppetite).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactReducedAppetite).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactReducedAppetite).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"REDUCED MOBILITY", App.get(contactReducedActivity).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactReducedActivity).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactReducedActivity).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"NIGHT SWEATS", App.get(contactNightSweats).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactNightSweats).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactNightSweats).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"SWELLING", App.get(contactGlandularSwelling).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactGlandularSwelling).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactGlandularSwelling).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"PATIENT REFERRED", App.get(contactReferred).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactReferred).equals(getResources().getString(R.string.no)) ? "NO" : "UNKNOWN")});

        observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referredFacilityAutoCompleteList)});

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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
                if (!result.contains("SUCCESS"))
                    return result;

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
        return false;
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

                if (App.get(indexPatientId).isEmpty()) {
                    indexPatientId.getEditText().setError(getString(R.string.empty_field));
                    indexPatientId.requestFocus();
                    error = true;
                } else if (!RegexUtil.isValidId(App.get(indexPatientId))) {
                    indexPatientId.getEditText().setError(getString(R.string.invalid_id));
                    indexPatientId.requestFocus();
                    error = true;
                }

                if (!error) getPatient();

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

    public void getPatient() {

        // Authenticate from server
        AsyncTask<String, String, String> getPatientTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.finding_patient));
                        loading.show();
                    }
                });

                String result = serverService.getPatient(App.get(indexPatientId), false);
                return result;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (result.equals("PATIENT_NOT_FOUND")) {
                    String errorPatientNotFound = "";

                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        errorPatientNotFound = getResources().getString(R.string.pmdt_fetch_patient_online_mode);
                    else
                        errorPatientNotFound = getResources().getString(R.string.pmdt_index_case_not_found);


                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(errorPatientNotFound);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                } else if (result.equals("OFFLINE_PATIENT")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.offline_patient));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else if (result.equals("FAIL")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.patient_get_error));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {

                    submitButton.setEnabled(true);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.pmdt_valid_index_case));
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
                }
            }
        };
        getPatientTask.execute("");

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
        } else if (view == baselineContactScreeningDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == scanQRCode) {
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
    public void refill(int encounterId) {
        OfflineForm offlineForm = serverService.getOfflineFormById(encounterId);
        String date = offlineForm.getFormDate();
        // TODO: complete this

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;
        if (spinner == relationWithIndex.getSpinner()) {
            if (App.get(relationWithIndex).equals(getResources().getString(R.string.pmdt_other)))
                otherRelationWithIndex.setVisibility(View.VISIBLE);
            else
                otherRelationWithIndex.setVisibility(View.GONE);
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        baselineContactScreeningDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

        otherRelationWithIndex.setVisibility(View.GONE);
        contactCoughDuration.setVisibility(View.GONE);
        contactHaemoptysis.setVisibility(View.GONE);
        facilityLinearLayout.setVisibility(View.GONE);
        referredFacilityAutoCompleteList.setVisibility(View.GONE);
        submitButton.setEnabled(false);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == contactCough.getRadioGroup()) {
            if (App.get(contactCough).equals(getResources().getString(R.string.yes))) {
                contactCoughDuration.setVisibility(View.VISIBLE);
                contactHaemoptysis.setVisibility(View.VISIBLE);
            } else {
                contactCoughDuration.setVisibility(View.GONE);
                contactHaemoptysis.setVisibility(View.GONE);
            }
        } else if (group == contactReferred.getRadioGroup()) {
            if (App.get(contactReferred).equals(getResources().getString(R.string.yes)))
                facilityLinearLayout.setVisibility(View.VISIBLE);
            else
                facilityLinearLayout.setVisibility(View.GONE);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    indexPatientId.getEditText().setText(str);
                    indexPatientId.getEditText().requestFocus();
                } else {

                    indexPatientId.getEditText().setText("");
                    indexPatientId.getEditText().requestFocus();

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

                indexPatientId.getEditText().setText("");
                indexPatientId.getEditText().requestFocus();

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            context.getResources().updateConfiguration(config, null);
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

package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.Address;
import com.ihsinformatics.gfatmmobile.model.TreatmentUser;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Tahira on 2/28/2017.
 */

public class PmdtPatientAssignmentForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, AutoCompleteTextView.OnClickListener {

    Context context;
    TitledButton formDate;
    TreatmentUser[] treatmentSupporters = null;
    String[] userNames = null;
    LinearLayout userMainLinearLayout;
    ArrayAdapter<String> autoCompleteUserAdapter = null;
    String personUuid = "";
    TextView treatmentSupporterText;
    AutoCompleteTextView treatmentSupporterList;
    TitledEditText treatmentSupporterId;
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        treatmentSupporterText = new TextView(context);
        treatmentSupporterText.setText(getResources().getString(R.string.pmdt_select_treatment_supporter));
        LinearLayout usersFacilityLayout = new LinearLayout(context);
        MyTextView userQuestionRequired = new MyTextView(context, "*");
        treatmentSupporterList = new AutoCompleteTextView(context);
        MyTextView referredFacilityQuestionRequired = new MyTextView(context, "*");
        int color1 = App.getColor(context, R.attr.colorAccent);
        referredFacilityQuestionRequired.setTextColor(color1);
        usersFacilityLayout.setOrientation(LinearLayout.HORIZONTAL);
        usersFacilityLayout.addView(referredFacilityQuestionRequired);
        usersFacilityLayout.addView(treatmentSupporterText);
        treatmentSupporterList = new AutoCompleteTextView(context);

        treatmentSupporterList.setHint(getResources().getString(R.string.pmdt_treatment_supporter_hint));
        userMainLinearLayout = new LinearLayout(context);
        userMainLinearLayout.setOrientation(LinearLayout.VERTICAL);
        userMainLinearLayout.addView(usersFacilityLayout);
        userMainLinearLayout.addView(treatmentSupporterList);


        treatmentSupporterId = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_id), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
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
        otherRelationship = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_relationship), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        reasonHouseholdMember = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_reason_having_treatment_supporter), getResources().getStringArray(R.array.pmdt_reasons_for_having_treatment_supporter), getResources().getString(R.string.pmdt_family_refused_treatment_supporter), App.VERTICAL, App.VERTICAL);
        otherReasonHouseholdMember = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_reason_having_treatment_supporter), "", "", 255, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        views = new View[]{formDate.getButton(), treatmentSupporterId.getEditText(), treatmentSupporterFirstName.getEditText(), treatmentSupporterLastName.getEditText(),
                address1.getEditText(), address2.getEditText(), addressCityVillage.getEditText(), /* Taluka, if required*/ addressLandmark.getEditText(),
                isHouseholdMember.getRadioGroup(), relationshipWithPatient.getSpinner(), otherRelationship.getEditText(),
                otherReasonHouseholdMember.getEditText(), otherReasonHouseholdMember.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, userMainLinearLayout, treatmentSupporterId, treatmentSupporterFirstName, treatmentSupporterLastName, address1, address2,
                        addressCityVillage, addressLandmark, isHouseholdMember, relationshipWithPatient,
                        otherRelationship, reasonHouseholdMember, otherReasonHouseholdMember}};

        formDate.getButton().setOnClickListener(this);

        isHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);
        reasonHouseholdMember.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentSupporterList.setOnItemClickListener(autoItemSelectedListner);

        treatmentSupporterId.getEditText().setKeyListener(null);
        treatmentSupporterFirstName.getEditText().setKeyListener(null);
        treatmentSupporterLastName.getEditText().setKeyListener(null);
        address1.getEditText().setKeyListener(null);
        address2.getEditText().setKeyListener(null);
        addressCityVillage.getEditText().setKeyListener(null);
        addressDistrict.getEditText().setKeyListener(null);
        addressLandmark.getEditText().setKeyListener(null);

        resetViews();
    }

    private AdapterView.OnItemClickListener autoItemSelectedListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            //extract selected username
            String selectedUsername = autoCompleteUserAdapter.getItem(arg2);

            // converting back to full username for searching the selected object in TreatmentUser array
            selectedUsername = selectedUsername.replace(" ", ".");


            for (int i = 0; i < treatmentSupporters.length; i++) {
                if (treatmentSupporters[i].getUsername().equals(selectedUsername)) {

                    treatmentSupporterId.getEditText().setText(treatmentSupporters[i].getUsername());
                    treatmentSupporterFirstName.getEditText().setText(treatmentSupporters[i].getFullName().split(" ")[0]);
                    treatmentSupporterLastName.getEditText().setText(treatmentSupporters[i].getFullName().split(" ")[1]);
                    personUuid = treatmentSupporters[i].getPersonUuid();

                    break;
                }

            }

            if (!personUuid.isEmpty()) {

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
                        Address preferredAddress = serverService.getPreferredAddressByPersonUuid(personUuid);

                        if (preferredAddress != null) {

                            result.put("ADDRESS1", preferredAddress.getAddress1());
                            result.put("ADDRESS2", preferredAddress.getAddress2());
                            result.put("CITY_VILLAGE", preferredAddress.getCityVillage());
                            result.put("DISTRICT", preferredAddress.getCountyDistrict());
                            result.put("LANDMARK", preferredAddress.getAddress3());

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

                        if (result.get("ADDRESS1") == null || result.get("ADDRESS1").isEmpty()) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                            alertDialog.setMessage(getResources().getString(R.string.pmdt_user_address_not_assigned));
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

                            submitButton.setEnabled(false);

                            return;
                        } else
                            submitButton.setEnabled(true);

                        address1.getEditText().setText(result.get("ADDRESS1"));
                        address2.getEditText().setText(result.get("ADDRESS2"));
                        addressDistrict.getEditText().setText(result.get("DISTRICT"));
                        addressCityVillage.getEditText().setText(result.get("CITY_VILLAGE"));
                        addressLandmark.getEditText().setText(result.get("LANDMARK"));
                    }
                };
                autopopulateFormTask.execute("");
            }
        }
    };

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        return false;
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

//        observations.add(new String[]{"VISIT DATE", App.getSqlDate(secondDateCalendar)});

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

//        if (dotLastPrescribedDay.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"DOT AT LAST PRESCRIBED DAY", App.get(dotLastPrescribedDay).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
//
//        if (administerInjections.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"TREATMENT SUPPORTER ADMINISTER INJECTION", App.get(administerInjections).equals(getResources().getString(R.string.yes)) ? "YES" :
//                    (App.get(administerInjections).equals(getResources().getString(R.string.no)) ? "NO" : "NOT APPLICABLE")});
//
//        if (missedDose.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"PATIENT MISSED DOSE", App.get(missedDose).equals(getResources().getString(R.string.yes)) ? "YES" :
//                    (App.get(missedDose).equals(getResources().getString(R.string.no)) ? "NO" :
//                            (App.get(missedDose).equals(getResources().getString(R.string.pmdt_refused)) ? "REFUSED" : "UNKNOWN"))});
//
//        if (reasonMissedDose.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"REASON MISSED DOSE", App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_adverse_events)) ? "ADVERSE EVENTS" :
//                    (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_drug_not_available)) ? "DRUG NOT AVAILABLE" :
//                            (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_treatment_supporter_did_not_come)) ? "TREATMENT SUPPORTER DID NOT COME" :
//                                    (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_refused)) ? "REFUSED" :
//                                            (App.get(reasonMissedDose).equals(getResources().getString(R.string.pmdt_unknown)) ? "UNKNOWN" : "OTHER REASON MISSED DOSE"))))});
//
//        if (otherReasonMissedDose.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"OTHER REASON MISSED DOSE", App.get(otherReasonMissedDose)});
//
//        observations.add(new String[]{"TREATMENT SUPPORTER PRACTICE INFECTION CONTROL MEASURES", App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_always)) ? "ALWAYS" :
//                (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_sometimes)) ? "SOMETIMES" :
//                        (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_rarely)) ? "RARELY" :
//                                (App.get(practiceInfectionControl).equals(getResources().getString(R.string.pmdt_never)) ? "NEVER" : "UNKNOWN")))});
//
//
//        observations.add(new String[]{"FAMILY PRACTICE INFECTION CONTROL MEASURES", App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_always)) ? "ALWAYS" :
//                (App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_sometimes)) ? "SOMETIMES" :
//                        (App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_rarely)) ? "RARELY" :
//                                (App.get(familyPracticeInfectionControl).equals(getResources().getString(R.string.pmdt_never)) ? "NEVER" : "UNKNOWN")))});
//
//        observations.add(new String[]{"TREATMENT SUPPORTER WAIT AFTER ADMINISTERING DRUGS", App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_does_not_wait)) ? "DOES NOT WAIT" :
//                (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_less_than_ten_mins)) ? "LESS THAN 10 MINUTES" :
//                        (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_ten_to_twenty_mins)) ? "10 TO 20 MINUTES" :
//                                (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_twenty_to_thirty_mins)) ? "20 TO 30 MINUTES" :
//                                        (App.get(waitAfterAdministerDrug).equals(getResources().getString(R.string.pmdt_thirty_mins_an_hour)) ? "30 MINUTES TO ONE HOUR" : "MORE THAN AN HOUR"))))});
//
//        observations.add(new String[]{"TREATMENT SUPPORTER CONTACT SCREENING", App.get(registerHouseholdMembers).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
//
//        if (contactScreeningPerformedDate.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"LAST CONTACT SCREENING PERFORMED DATE", App.getSqlDate(thirdDateCalendar)});
//
//        observations.add(new String[]{"PATIENT SATISFIED WITH TREATMENT SUPPORTER", App.get(patientSatisfied).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
//
//        observations.add(new String[]{"PATIENT SUBMITTED SPUTUM LAST VISIT", App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.yes)) ? "YES" :
//                (App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.no)) ? "NO" : "NOT APPLICABLE")});
//
//        if (reasonNotSubmittedSample.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"REASON NOT SUBMITTED SPUTUM", App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.pmdt_could_not_produce_sputum)) ? "COULD NOT PRODUCE SAMPLE" :
//                    (App.get(sputumSubmittedLastVisit).equals(getResources().getString(R.string.pmdt_doctor_did_not_request)) ? "DOCTOR DID NOT REQUEST" : "OTHER REASON NOT SUBMITTED SPUTUM")});
//
//        if (otherReasonNotSubmittedSample.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"OTHER REASON NOT SUBMITTED SPUTUM", App.get(otherReasonNotSubmittedSample)});
//
//        observations.add(new String[]{"PATIENT FOOD BASKET INCENTIVE", App.get(foodBasketIncentiveLastMonth).equals(getResources().getString(R.string.yes)) ? "YES" :
//                (App.get(foodBasketIncentiveLastMonth).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});
//
//
//        observations.add(new String[]{"ADVERSE EVENT REPORTED", App.get(adverseEventLastVisit).equals(getResources().getString(R.string.yes)) ? "YES" :
//                (App.get(adverseEventLastVisit).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});
//
//        //actionAdverseEvent
//        if (actionAdverseEvent.getVisibility() == View.VISIBLE) {
//            String actionAdverseEventString = "";
//            for (CheckBox cb : actionAdverseEvent.getCheckedBoxes()) {
//                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_arranged_follow_up)))
//                    actionAdverseEventString = actionAdverseEventString + "ARRANGED FOLLOW UP NEXT DAY FOR CLINICAL REVIEW" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_consulted_and_advised)))
//                    actionAdverseEventString = actionAdverseEventString + "CONSULTED ON PHONE WITH TB DOCTOR AND ADVISED ANCILLARY MEDICATIONS" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_consulted_and_arranged_transfer)))
//                    actionAdverseEventString = actionAdverseEventString + "CONSULTED ON PHONE WITH TB DOCTOR AND ARRANGED TRANSFER TO REFERRAL HOSPITAL" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_no_action_required)))
//                    actionAdverseEventString = actionAdverseEventString + "NO ACTION WAS REQUIRED" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_other)))
//                    actionAdverseEventString = actionAdverseEventString + "OTHER ACTION ADVERSE EVENT" + " ; ";
//            }
//            observations.add(new String[]{"ACTION ADVERSE EVENT", actionAdverseEventString});
//        }
//
//        if (otherActionAdverseEvent.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"OTHER ACTION ADVERSE EVENT", App.get(otherActionAdverseEvent)});
//
//        observations.add(new String[]{"TREATMENT SUPPORTER ADDRESS SOCIAL PROBLEMS", App.get(addressSocialProblems).equals(getResources().getString(R.string.yes)) ? "YES" :
//                (App.get(addressSocialProblems).equals(getResources().getString(R.string.no)) ? "NO" : "REFUSED")});
//
//
//        observations.add(new String[]{"REFER FOR COUNSELING", App.get(referCounseling).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
//
//        if (counselingType.getVisibility() == View.VISIBLE) {
//            String conselingTypeString = "";
//            for (CheckBox cb : counselingType.getCheckedBoxes()) {
//                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adherence)))
//                    conselingTypeString = conselingTypeString + "ADHERENCE" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_infection_control)))
//                    conselingTypeString = conselingTypeString + "INFECTION CONTROL COUNSELLING" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_adverse_events)))
//                    conselingTypeString = conselingTypeString + "ADVERSE EVENTS" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_psychosocial_issues)))
//                    conselingTypeString = conselingTypeString + "PSYCHOSOCIAL COUNSELING" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_substance_abuse)))
//                    conselingTypeString = conselingTypeString + "SUBSTANCE ABUSE" + " ; ";
//                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pmdt_other)))
//                    conselingTypeString = conselingTypeString + "OTHER COUNSELING TYPE" + " ; ";
//            }
//            observations.add(new String[]{"COUNSELING TYPE", conselingTypeString});
//        }
//
//        if (otherCounselingType.getVisibility() == View.VISIBLE)
//            observations.add(new String[]{"OTHER COUNSELING TYPE", App.get(otherCounselingType)});
//
//        if (!App.get(treatmentCoordinatorNotes).isEmpty())
//            observations.add(new String[]{"TREATMENT COORDINATOR MONITORING NOTES", App.get(treatmentCoordinatorNotes)});

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

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

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
                String treatmentSupporterUsername = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER ID");

                if (treatmentSupporterUsername != null && !treatmentSupporterUsername.isEmpty())
                    result.put("TREATMENT SUPPORTER USERNAME", treatmentSupporterUsername);

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.get("TREATMENT SUPPORTER USERNAME") == null || result.get("TREATMENT SUPPORTER USERNAME").isEmpty()) {

                    if (autoCompleteUserAdapter == null) {
                        final AsyncTask<String, String, String[]> autopopulateFormTask = new AsyncTask<String, String, String[]>() {
                            @Override
                            protected String[] doInBackground(String... params) {
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

                                treatmentSupporters = serverService.getUsersByRole(getResources().getString(R.string.treatment_supporter_role));
                                userNames = new String[treatmentSupporters.length];

                                if (treatmentSupporters != null && treatmentSupporters.length > 0) {
                                    for (int i = 0; i < treatmentSupporters.length; i++) {
                                        String[] array = treatmentSupporters[i].getUsername().split("\\.");
                                        userNames[i] = array[0] + " " + array[1];
                                    }
                                }

                                return userNames;
                            }

                            @Override
                            protected void onProgressUpdate(String... values) {
                            }


                            @Override
                            protected void onPostExecute(String[] result) {
                                super.onPostExecute(result);
                                loading.dismiss();

                                if (result == null || result.length == 0) {

                                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                                    alertDialog.setMessage(getResources().getString(R.string.pmdt_treatment_supporters_not_found));
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

                                    submitButton.setEnabled(false);

                                    return;
                                } else
                                    submitButton.setEnabled(true);

                                autoCompleteUserAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, result);
                                treatmentSupporterList.setAdapter(autoCompleteUserAdapter);

                            }
                        };
                        autopopulateFormTask.execute("");
                    }

                } else {
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
                                        // TODO: handle exception
                                    }
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    submitButton.setEnabled(false);

                    return;
                }
            }
        };
        autopopulateFormTask.execute("");

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

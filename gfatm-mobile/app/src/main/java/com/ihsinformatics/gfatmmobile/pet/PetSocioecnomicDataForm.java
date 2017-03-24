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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetSocioecnomicDataForm extends AbstractFormActivity {

    Context context;
    TitledButton formDate;
    TitledSpinner ethinicity;
    TitledEditText otherEthinicity;
    TitledSpinner contactEducationLevel;
    TitledSpinner maritalStatus;
    TitledSpinner emloyementStatus;
    TitledSpinner occupation;
    TitledEditText contactIncome;
    TitledEditText contactIncomeGroup;
    TitledSpinner householdHead;
    TitledEditText otherHouseholdHead;
    TitledSpinner householdHeadEducationLevel;
    TitledSpinner motherTongue;
    TitledEditText otherMotherTongue;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.PET_SOCIO_ECNOMICS_DATA;
        FORM = Forms.pet_socioEcnomicData;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
        ethinicity = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_ethnicity), getResources().getStringArray(R.array.pet_ethnicities), "", App.VERTICAL, true);
        otherEthinicity = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        contactEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_contact_education_level), getResources().getStringArray(R.array.pet_contact_education_levels), getResources().getString(R.string.pet_intermediate), App.VERTICAL, true);
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_martial_status), getResources().getStringArray(R.array.pet_martial_statuses), getResources().getString(R.string.pet_single), App.VERTICAL, true);
        emloyementStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_employement_status), getResources().getStringArray(R.array.pet_employement_statuses), getResources().getString(R.string.pet_employed), App.VERTICAL, true);
        occupation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_occupation), getResources().getStringArray(R.array.pet_occupations), getString(R.string.pet_artist), App.VERTICAL, true);
        contactIncome = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income), "0", "", 10, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        contactIncomeGroup = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income_group), getResources().getString(R.string.pet_none), "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        householdHead = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_household_head), getResources().getStringArray(R.array.pet_household_heads), getString(R.string.pet_mother), App.VERTICAL, true);
        otherHouseholdHead = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        householdHeadEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_household_education), getResources().getStringArray(R.array.pet_contact_education_levels), getString(R.string.pet_intermediate), App.VERTICAL);
        motherTongue = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_mother_tongue), getResources().getStringArray(R.array.pet_mother_tongues), getString(R.string.urdu), App.VERTICAL);
        otherMotherTongue = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        views = new View[]{formDate.getButton(), ethinicity.getSpinner(), otherEthinicity.getEditText(), contactEducationLevel.getSpinner(), maritalStatus.getSpinner(), emloyementStatus.getSpinner(), occupation.getSpinner(), contactIncome.getEditText(), contactIncomeGroup.getEditText(),
                householdHead.getSpinner(), otherHouseholdHead.getEditText(), householdHeadEducationLevel.getSpinner(), motherTongue.getSpinner(), otherMotherTongue.getEditText()};

        viewGroups = new View[][]{{formDate, ethinicity, otherEthinicity, contactEducationLevel, maritalStatus, emloyementStatus, occupation, contactIncome, contactIncomeGroup, householdHead, otherHouseholdHead, householdHeadEducationLevel, motherTongue, otherMotherTongue}};

        contactIncome.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(contactIncome).equals(""))
                    contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                else {
                    Float income = Float.parseFloat(App.get(contactIncome));
                    if (income == 0)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                    else if (income >= 1 && income <= 7000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_class));
                    else if (income >= 7001 && income <= 35000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_middle_class));
                    else if (income >= 35001 && income <= 87000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_middle_class));
                    else if (income >= 87001 && income <= 173000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_middle_class));
                    else
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_class));
                }


            }
        });
        contactIncomeGroup.getEditText().setKeyListener(null);

        formDate.getButton().setOnClickListener(this);
        ethinicity.getSpinner().setOnItemSelectedListener(this);
        householdHead.getSpinner().setOnItemSelectedListener(this);
        motherTongue.getSpinner().setOnItemSelectedListener(this);

        resetViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        otherEthinicity.setVisibility(View.GONE);
        otherHouseholdHead.setVisibility(View.GONE);
        otherMotherTongue.setVisibility(View.GONE);

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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }


    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(otherMotherTongue).isEmpty() && otherMotherTongue.getVisibility() == View.VISIBLE) {
            otherMotherTongue.getEditText().setError(getString(R.string.empty_field));
            otherMotherTongue.getEditText().requestFocus();
            error = true;
        }

        if (App.get(otherHouseholdHead).isEmpty() && otherHouseholdHead.getVisibility() == View.VISIBLE) {
            otherHouseholdHead.getEditText().setError(getString(R.string.empty_field));
            otherHouseholdHead.getEditText().requestFocus();
            error = true;
        }

        if (App.get(otherEthinicity).isEmpty() && otherEthinicity.getVisibility() == View.VISIBLE) {
            otherEthinicity.getEditText().setError(getString(R.string.empty_field));
            otherEthinicity.getEditText().requestFocus();
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
        } else
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
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/

        final String ethnicityString = App.get(ethinicity).equals(getResources().getString(R.string.pet_urdu_speaking)) ? "URDU SPEAKING" :
                (App.get(ethinicity).equals(getResources().getString(R.string.pet_sindhi)) ? "SINDHI" :
                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_pakhtun)) ? "PASHTUN" :
                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_punjabi)) ? "PUNJABI" :
                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_balochi)) ? "BALOCHI" :
                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_bihari)) ? "BIHARI" :
                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_memon)) ? "MEMON" :
                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_gujrati)) ? "GUJRATI" :
                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_brohi)) ? "BROHI" :
                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_hindko)) ? "HINDKO" :
                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_gilgiti_kashmiri)) ? "GILGITI" :
                                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_siraiki)) ? "SARAIKI" :
                                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_benagli)) ? "BANGALI" :
                                                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_afghani)) ? "AFGHAN" :
                                                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.pet_hazara)) ? "HAZARA" :
                                                                                                                                (App.get(ethinicity).equals(getResources().getString(R.string.pet_other_ethnicity)) ? "OTHER ETHNICITY" :
                                                                                                                                        (App.get(ethinicity).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))))))));

        observations.add(new String[]{"ETHNICITY", ethnicityString});
        if (otherEthinicity.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER ETHNICITY", App.get(otherEthinicity)});

        final String contactEducationLevelString = App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_elementary)) ? "ELEMENTARY EDUCATION" :
                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_primary)) ? "PRIMARY EDUCATION" :
                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_secondary)) ? "SECONDARY EDUCATION" :
                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_intermediate)) ? "INTERMEDIATE EDUCATION" :
                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_undergraduate)) ? "UNDERGRADUATE EDUCATION" :
                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_graduate)) ? "GRADUATE EDUCATION" :
                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_doctorate)) ? "DOCTORATE EDUCATION" :
                                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_religious)) ? "RELIGIOUS EDUCATION" :
                                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_polytechnic)) ? "POLYTECHNIC EDUCATION" :
                                                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_special_education)) ? "SPECIAL EDUCATION RECEIVED" :
                                                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_no_formal_education)) ? "NO FORMAL EDUCATION" :
                                                                                                (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_other)) ? "OTHER" :
                                                                                                        (App.get(contactEducationLevel).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))));
        observations.add(new String[]{"HIGHEST EDUCATION LEVEL", contactEducationLevelString});

        final String maritalStatusString = App.get(maritalStatus).equals(getResources().getString(R.string.pet_single)) ? "SINGLE" :
                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_engaged)) ? "ENGAGED" :
                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_married)) ? "MARRIED" :
                                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_separated)) ? "SEPARATED" :
                                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_divorced)) ? "DIVORCED" :
                                                (App.get(maritalStatus).equals(getResources().getString(R.string.pet_widower)) ? "WIDOWED" :
                                                        (App.get(maritalStatus).equals(getResources().getString(R.string.pet_other)) ? "OTHER" :
                                                                (App.get(maritalStatus).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSE")))))));
        observations.add(new String[]{"MARITAL STATUS", maritalStatusString});

        final String employementStatusString = App.get(emloyementStatus).equals(getResources().getString(R.string.pet_employed)) ? "EMPLOYED" :
                (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_unable_to_work)) ? "UNABLE TO WORK" :
                        (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_student)) ? "STUDENT" :
                                (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_unemployed)) ? "UNEMPLOYED" :
                                        (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_housework)) ? "HOUSEWORK" :
                                                (App.get(emloyementStatus).equals(getResources().getString(R.string.pet_retired)) ? "RETIRED" :
                                                        (App.get(emloyementStatus).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSE"))))));
        observations.add(new String[]{"EMPLOYMENT STATUS", employementStatusString});

        final String occupationString = App.get(occupation).equals(getResources().getString(R.string.pet_artist)) ? "ARTIST" :
                (App.get(occupation).equals(getResources().getString(R.string.pet_beggar)) ? "BEGGAR" :
                                (App.get(occupation).equals(getResources().getString(R.string.pet_carpenter)) ? "CARPENTER" :
                                        (App.get(occupation).equals(getResources().getString(R.string.pet_casual_labor)) ? "CASUAL LABOR" :
                                                (App.get(occupation).equals(getResources().getString(R.string.pet_child_labor)) ? "CHILD LABOR" :
                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_clerk)) ? "CLERK" :
                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_doctor)) ? "MEDICAL OFFICER/DOCTOR" :
                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_driver)) ? "DRIVER" :
                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_engineer)) ? "ENGINEER" :
                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_farmer)) ? "FARMER" :
                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_fisherman)) ? "FISHERMAN" :
                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_food_vendor)) ? "FOOD VENDOR" :
                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_forestry_worker)) ? "FORESTRY WORKER" :
                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_housework)) ? "HOUSEWORK" :
                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_machine_operator)) ? "MACHINE OPERATOR" :
                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_miner)) ? "MINER" :
                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_pilot)) ? "PILOT" :
                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_plumber)) ? "PLUMBER" :
                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_professional)) ? "PROFESSIONAL" :
                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_reporter)) ? "REPORTER" :
                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_sales_representative)) ? "SALES REPRESENTATIVE" :
                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_security_officer)) ? "SECURITY OFFICER" :
                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_shepherd)) ? "SHEPHERD" :
                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_slum_worker)) ? "SLUM WORKER" :
                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_small_bussiness_owner)) ? "SMALL BUSINESS OWNER" :
                                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_tailor)) ? "TAILOR" :
                                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_trader)) ? "TRADER" :
                                                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_teacher)) ? "TEACHER" :
                                                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_vegetable_fruit_Seller)) ? "VEGETABLE SELLER" :
                                                                                                                                                                                                                                                        (App.get(occupation).equals(getResources().getString(R.string.pet_waiter)) ? "WAITER" :
                                                                                                                                                                                                                                                                (App.get(occupation).equals(getResources().getString(R.string.pet_other)) ? "OTHER" : "UNKNOWN"))))))))))))))))))))))))))))));
        observations.add(new String[]{"OCCUPATION", occupationString});
        observations.add(new String[]{"MONTHLY INCOME", App.get(contactIncome)});

        final String incomeClassString = App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_none)) ? "NONE" :
                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_lower_class)) ? "LOWER INCOME CLASS" :
                        (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_lower_middle_class)) ? "LOWER MIDDLE INCOME CLASS" :
                                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_middle_class)) ? "MIDDLE INCOME CLASS" :
                                        (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_upper_middle_class)) ? "UPPER MIDDLE INCOME CLASS" :
                                                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_upper_class)) ? "UPPER INCOME CLASS" : "UNKNOWN")))));
        observations.add(new String[]{"INCOME CLASS", incomeClassString});

        final String householdHeadtring = App.get(householdHead).equals(getResources().getString(R.string.pet_mother)) ? "MOTHER" :
                (App.get(householdHead).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                        (App.get(householdHead).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                (App.get(householdHead).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                        (App.get(householdHead).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                (App.get(householdHead).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                        (App.get(householdHead).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                (App.get(householdHead).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                        (App.get(householdHead).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                (App.get(householdHead).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                        (App.get(householdHead).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                (App.get(householdHead).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))));

        observations.add(new String[]{"RELATIONSHIP TO HEAD OF HOUSEHOLD", householdHeadtring});

        if (otherHouseholdHead.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FAMILY MEMBER", App.get(otherHouseholdHead)});

        final String houseHoldEducationLevelString = App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_elementary)) ? "ELEMENTARY EDUCATION" :
                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_primary)) ? "PRIMARY EDUCATION" :
                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_secondary)) ? "SECONDARY EDUCATION" :
                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_intermediate)) ? "INTERMEDIATE EDUCATION" :
                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_undergraduate)) ? "UNDERGRADUATE EDUCATION" :
                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_graduate)) ? "GRADUATE EDUCATION" :
                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_doctorate)) ? "DOCTORATE EDUCATION" :
                                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_religious)) ? "RELIGIOUS EDUCATION" :
                                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_polytechnic)) ? "POLYTECHNIC EDUCATION" :
                                                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_special_education)) ? "SPECIAL EDUCATION RECEIVED" :
                                                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_no_formal_education)) ? "NO FORMAL EDUCATION" :
                                                                                                (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_other)) ? "OTHER" :
                                                                                                        (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED"))))))))))));
        observations.add(new String[]{"CURRENT EDUCATION LEVEL OF HEAD OF HOUSEHOLD", houseHoldEducationLevelString});

        final String motherTongueString = App.get(motherTongue).equals(getResources().getString(R.string.pet_urdu)) ? "URDU LANGUAGE" :
                (App.get(motherTongue).equals(getResources().getString(R.string.pet_punjabi)) ? "PUNJABI LANGUAGE" :
                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_sindhi)) ? "SINDHI LANGUAGE" :
                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_pushto)) ? "PUSHTO LANGUAGE" :
                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_balochi)) ? "BALOCHI LANGUAGE" :
                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_siraiki)) ? "SIRAIKI LANGUAGE" :
                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_hazara)) ? "HAZARA LANGUAGE" :
                                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_english)) ? "ENGLISH LANGUAGE" :
                                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_gujrati)) ? "GUJRATI LANGUAGE" :
                                                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_memoni)) ? "MEMONI LANGUAGE" :
                                                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_brahui)) ? "BRAHUI LANGUAGE" :
                                                                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_chitrali)) ? "CHITRALI LANGUAGE" :
                                                                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_hindko)) ? "HINDKO LANGUAGE" :
                                                                                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_kalaasha)) ? "KALAASHA LANGUAGE" :
                                                                                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_kashmiri)) ? "KASHMIRI LANGUAGE" :
                                                                                                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_persian)) ? "PERSIAN LANGUAGE" :
                                                                                                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_balti)) ? "BALTI LANGUAGE" :
                                                                                                                                                (App.get(motherTongue).equals(getResources().getString(R.string.pet_makrani)) ? "MAKRANI LANGUAGE" :
                                                                                                                                                        (App.get(motherTongue).equals(getResources().getString(R.string.pet_other)) ? "OTHER" :
                                                                                                                                                                (App.get(motherTongue).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN")))))))))))))))))));
        observations.add(new String[]{"MOTHER TONGUE", motherTongueString});
        if (otherMotherTongue.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER LANGUAGE", App.get(otherMotherTongue)});


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
                else {

                    String encounterId = "";

                    if (result.contains("_")) {
                        String[] successArray = result.split("_");
                        encounterId = successArray[1];
                    }

                    String[][] concept = serverService.getConceptUuidAndDataType(ethnicityString);
                    result = serverService.savePersonAttributeType("Ethnicity", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(contactEducationLevelString);
                    result = serverService.savePersonAttributeType("Education Level", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(maritalStatusString);
                    result = serverService.savePersonAttributeType("Marital Status", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(employementStatusString);
                    result = serverService.savePersonAttributeType("Employment Status", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(occupationString);
                    result = serverService.savePersonAttributeType("Occupation", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(incomeClassString);
                    result = serverService.savePersonAttributeType("Income Class", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;

                    concept = serverService.getConceptUuidAndDataType(motherTongueString);
                    result = serverService.savePersonAttributeType("Mother Tongue", concept[0][0], encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

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
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == ethinicity.getSpinner()) {
            if (App.get(ethinicity).equals(getResources().getString(R.string.pet_other_ethnicity)))
                otherEthinicity.setVisibility(View.VISIBLE);
            else
                otherEthinicity.setVisibility(View.GONE);
        } else if (spinner == householdHead.getSpinner()) {
            if (App.get(householdHead).equals(getResources().getString(R.string.pet_other)))
                otherHouseholdHead.setVisibility(View.VISIBLE);
            else
                otherHouseholdHead.setVisibility(View.GONE);
        } else if (spinner == motherTongue.getSpinner()) {
            if (App.get(motherTongue).equals(getResources().getString(R.string.pet_other)))
                otherMotherTongue.setVisibility(View.VISIBLE);
            else
                otherMotherTongue.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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
            } else if (obs[0][0].equals("ETHNICITY")) {
                String value = obs[0][1].equals("URDU SPEAKING") ? getResources().getString(R.string.pet_urdu_speaking) :
                        (obs[0][1].equals("SINDHI") ? getResources().getString(R.string.pet_sindhi) :
                                (obs[0][1].equals("PASHTUN") ? getResources().getString(R.string.pet_pakhtun) :
                                        (obs[0][1].equals("PUNJABI") ? getResources().getString(R.string.pet_punjabi) :
                                                (obs[0][1].equals("BALOCHI") ? getResources().getString(R.string.pet_balochi) :
                                                        (obs[0][1].equals("BIHARI") ? getResources().getString(R.string.pet_bihari) :
                                                                (obs[0][1].equals("MEMON") ? getResources().getString(R.string.pet_memon) :
                                                                        (obs[0][1].equals("GUJRATI") ? getResources().getString(R.string.pet_gujrati) :
                                                                                (obs[0][1].equals("BROHI") ? getResources().getString(R.string.pet_brohi) :
                                                                                        (obs[0][1].equals("HINDKO") ? getResources().getString(R.string.pet_hindko) :
                                                                                                (obs[0][1].equals("GILGITI") ? getResources().getString(R.string.pet_gilgiti_kashmiri) :
                                                                                                        (obs[0][1].equals("SARAIKI") ? getResources().getString(R.string.pet_siraiki) :
                                                                                                                (obs[0][1].equals("BANGALI") ? getResources().getString(R.string.pet_benagli) :
                                                                                                                        (obs[0][1].equals("AFGHAN") ? getResources().getString(R.string.pet_afghani) :
                                                                                                                                (obs[0][1].equals("HAZARA") ? getResources().getString(R.string.pet_hazara) :
                                                                                                                                        (obs[0][1].equals("HAZARA") ? getResources().getString(R.string.pet_hazara) :
                                                                                                                                                (obs[0][1].equals("OTHER ETHNICITY") ? getResources().getString(R.string.pet_other_ethnicity) :
                                                                                                                                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.pet_unknown) : getResources().getString(R.string.refused))))))))))))))))));
                ethinicity.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER ETHNICITY")) {
                otherEthinicity.getEditText().setText(obs[0][1]);
                otherEthinicity.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("HIGHEST EDUCATION LEVEL")) {
                String value = obs[0][1].equals("ELEMENTARY EDUCATION") ? getResources().getString(R.string.pet_elementary) :
                        (obs[0][1].equals("PRIMARY EDUCATION") ? getResources().getString(R.string.pet_primary) :
                                (obs[0][1].equals("SECONDARY EDUCATION") ? getResources().getString(R.string.pet_secondary) :
                                        (obs[0][1].equals("INTERMEDIATE EDUCATION") ? getResources().getString(R.string.pet_intermediate) :
                                                (obs[0][1].equals("UNDERGRADUATE EDUCATION") ? getResources().getString(R.string.pet_undergraduate) :
                                                        (obs[0][1].equals("GRADUATE EDUCATION") ? getResources().getString(R.string.pet_graduate) :
                                                                (obs[0][1].equals("DOCTORATE EDUCATION") ? getResources().getString(R.string.pet_doctorate) :
                                                                        (obs[0][1].equals("RELIGIOUS EDUCATION") ? getResources().getString(R.string.pet_religious) :
                                                                                (obs[0][1].equals("POLYTECHNIC EDUCATION") ? getResources().getString(R.string.pet_polytechnic) :
                                                                                        (obs[0][1].equals("SPECIAL EDUCATION RECEIVED") ? getResources().getString(R.string.pet_special_education) :
                                                                                                (obs[0][1].equals("NO FORMAL EDUCATION") ? getResources().getString(R.string.pet_no_formal_education) :
                                                                                                        (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))))))))));
                contactEducationLevel.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("MARITAL STATUS")) {
                String value = obs[0][1].equals("SINGLE") ? getResources().getString(R.string.pet_single) :
                        (obs[0][1].equals("ENGAGED") ? getResources().getString(R.string.pet_engaged) :
                                (obs[0][1].equals("MARRIED") ? getResources().getString(R.string.pet_married) :
                                        (obs[0][1].equals("SEPARATED") ? getResources().getString(R.string.pet_separated) :
                                                (obs[0][1].equals("DIVORCED") ? getResources().getString(R.string.pet_divorced) :
                                                        (obs[0][1].equals("WIDOWED") ? getResources().getString(R.string.pet_widower) :
                                                                (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused))))))));
                maritalStatus.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("EMPLOYMENT STATUS")) {
                String value = obs[0][1].equals("EMPLOYED") ? getResources().getString(R.string.pet_employed) :
                        (obs[0][1].equals("UNABLE TO WORK") ? getResources().getString(R.string.pet_unable_to_work) :
                                (obs[0][1].equals("STUDENT") ? getResources().getString(R.string.pet_student) :
                                        (obs[0][1].equals("UNEMPLOYED") ? getResources().getString(R.string.pet_unemployed) :
                                                (obs[0][1].equals("HOUSEWORK") ? getResources().getString(R.string.pet_housework) :
                                                        (obs[0][1].equals("RETIRED") ? getResources().getString(R.string.pet_retired) :
                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))));
                emloyementStatus.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OCCUPATION")) {
                String value = obs[0][1].equals("ARTIST") ? getResources().getString(R.string.pet_artist) :
                        (obs[0][1].equals("BEGGAR") ? getResources().getString(R.string.pet_beggar) :
                                (obs[0][1].equals("CARPENTER") ? getResources().getString(R.string.pet_carpenter) :
                                        (obs[0][1].equals("CASUAL LABOR") ? getResources().getString(R.string.pet_casual_labor) :
                                                (obs[0][1].equals("CHILD LABOR") ? getResources().getString(R.string.pet_child_labor) :
                                                        (obs[0][1].equals("CLERK") ? getResources().getString(R.string.pet_clerk) :
                                                                (obs[0][1].equals("MEDICAL OFFICER/DOCTOR") ? getResources().getString(R.string.pet_doctor) :
                                                                        (obs[0][1].equals("DRIVER") ? getResources().getString(R.string.pet_driver) :
                                                                                (obs[0][1].equals("ENGINEER") ? getResources().getString(R.string.pet_engineer) :
                                                                                        (obs[0][1].equals("FARMER") ? getResources().getString(R.string.pet_farmer) :
                                                                                                (obs[0][1].equals("FISHERMAN") ? getResources().getString(R.string.pet_fisherman) :
                                                                                                        (obs[0][1].equals("FOOD VENDOR") ? getResources().getString(R.string.pet_food_vendor) :
                                                                                                                (obs[0][1].equals("FORESTRY WORKER") ? getResources().getString(R.string.pet_forestry_worker) :
                                                                                                                        (obs[0][1].equals("HOUSEWORK") ? getResources().getString(R.string.pet_housework) :
                                                                                                                                (obs[0][1].equals("MACHINE OPERATOR") ? getResources().getString(R.string.pet_machine_operator) :
                                                                                                                                        (obs[0][1].equals("MINER") ? getResources().getString(R.string.pet_miner) :
                                                                                                                                                (obs[0][1].equals("PILOT") ? getResources().getString(R.string.pet_pilot) :
                                                                                                                                                        (obs[0][1].equals("PLUMBER") ? getResources().getString(R.string.pet_plumber) :
                                                                                                                                                                (obs[0][1].equals("PROFESSIONAL") ? getResources().getString(R.string.pet_professional) :
                                                                                                                                                                        (obs[0][1].equals("REPORTER") ? getResources().getString(R.string.pet_reporter) :
                                                                                                                                                                                (obs[0][1].equals("SALES REPRESENTATIVE") ? getResources().getString(R.string.pet_sales_representative) :
                                                                                                                                                                                        (obs[0][1].equals("SECURITY OFFICER") ? getResources().getString(R.string.pet_security_officer) :
                                                                                                                                                                                                (obs[0][1].equals("SHEPHERD") ? getResources().getString(R.string.pet_shepherd) :
                                                                                                                                                                                                        (obs[0][1].equals("SLUM WORKER") ? getResources().getString(R.string.pet_slum_worker) :
                                                                                                                                                                                                                (obs[0][1].equals("SMALL BUSINESS OWNER") ? getResources().getString(R.string.pet_small_bussiness_owner) :
                                                                                                                                                                                                                        (obs[0][1].equals("TAILOR") ? getResources().getString(R.string.pet_tailor) :
                                                                                                                                                                                                                                (obs[0][1].equals("TRADER") ? getResources().getString(R.string.pet_trader) :
                                                                                                                                                                                                                                        (obs[0][1].equals("TEACHER") ? getResources().getString(R.string.pet_teacher) :
                                                                                                                                                                                                                                                (obs[0][1].equals("VEGETABLE SELLER") ? getResources().getString(R.string.pet_vegetable_fruit_Seller) :
                                                                                                                                                                                                                                                        (obs[0][1].equals("WAITER") ? getResources().getString(R.string.pet_waiter) :
                                                                                                                                                                                                                                                                (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) : getResources().getString(R.string.unknown)))))))))))))))))))))))))))))));
                occupation.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("MONTHLY INCOME")) {
                contactIncome.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("INCOME CLASS")) {
                String value = obs[0][1].equals("NONE") ? getResources().getString(R.string.pet_none) :
                        (obs[0][1].equals("LOWER INCOME CLASS") ? getResources().getString(R.string.pet_lower_class) :
                                (obs[0][1].equals("LOWER MIDDLE INCOME CLASS") ? getResources().getString(R.string.pet_lower_middle_class) :
                                        (obs[0][1].equals("MIDDLE INCOME CLASS") ? getResources().getString(R.string.pet_middle_class) :
                                                (obs[0][1].equals("UPPER MIDDLE INCOME CLASS") ? getResources().getString(R.string.pet_upper_middle_class) :
                                                        (obs[0][1].equals("UPPER INCOME CLASS") ? getResources().getString(R.string.pet_upper_class) : getResources().getString(R.string.unknown))))));
                contactIncomeGroup.getEditText().setText(value);
            } else if (obs[0][0].equals("RELATIONSHIP TO HEAD OF HOUSEHOLD")) {
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
                householdHead.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER FAMILY MEMBER")) {
                otherHouseholdHead.getEditText().setText(obs[0][1]);
                otherHouseholdHead.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CURRENT EDUCATION LEVEL OF HEAD OF HOUSEHOLD")) {
                String value = obs[0][1].equals("ELEMENTARY EDUCATION") ? getResources().getString(R.string.pet_elementary) :
                        (obs[0][1].equals("PRIMARY EDUCATION") ? getResources().getString(R.string.pet_primary) :
                                (obs[0][1].equals("SECONDARY EDUCATION") ? getResources().getString(R.string.pet_secondary) :
                                        (obs[0][1].equals("INTERMEDIATE EDUCATION") ? getResources().getString(R.string.pet_intermediate) :
                                                (obs[0][1].equals("UNDERGRADUATE EDUCATION") ? getResources().getString(R.string.pet_undergraduate) :
                                                        (obs[0][1].equals("GRADUATE EDUCATION") ? getResources().getString(R.string.pet_graduate) :
                                                                (obs[0][1].equals("DOCTORATE EDUCATION") ? getResources().getString(R.string.pet_doctorate) :
                                                                        (obs[0][1].equals("RELIGIOUS EDUCATION") ? getResources().getString(R.string.pet_religious) :
                                                                                (obs[0][1].equals("POLYTECHNIC EDUCATION") ? getResources().getString(R.string.pet_polytechnic) :
                                                                                        (obs[0][1].equals("SPECIAL EDUCATION RECEIVED") ? getResources().getString(R.string.pet_special_education) :
                                                                                                (obs[0][1].equals("NO FORMAL EDUCATION") ? getResources().getString(R.string.pet_no_formal_education) :
                                                                                                        (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                                                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) : getResources().getString(R.string.refused)))))))))))));
                householdHeadEducationLevel.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("MOTHER TONGUE")) {
                String value = obs[0][1].equals("URDU LANGUAGE") ? getResources().getString(R.string.pet_urdu) :
                        (obs[0][1].equals("PUNJABI LANGUAGE") ? getResources().getString(R.string.pet_punjabi) :
                                (obs[0][1].equals("SINDHI LANGUAGE") ? getResources().getString(R.string.pet_sindhi) :
                                        (obs[0][1].equals("PUSHTO LANGUAGE") ? getResources().getString(R.string.pet_pushto) :
                                                (obs[0][1].equals("BALOCHI LANGUAGE") ? getResources().getString(R.string.pet_balochi) :
                                                        (obs[0][1].equals("SIRAIKI LANGUAGE") ? getResources().getString(R.string.pet_siraiki) :
                                                                (obs[0][1].equals("HAZARA LANGUAGE") ? getResources().getString(R.string.pet_hazara) :
                                                                        (obs[0][1].equals("ENGLISH LANGUAGE") ? getResources().getString(R.string.pet_english) :
                                                                                (obs[0][1].equals("GUJRATI LANGUAGE") ? getResources().getString(R.string.pet_gujrati) :
                                                                                        (obs[0][1].equals("MEMONI LANGUAGE") ? getResources().getString(R.string.pet_memoni) :
                                                                                                (obs[0][1].equals("BRAHUI LANGUAGE") ? getResources().getString(R.string.pet_brahui) :
                                                                                                        (obs[0][1].equals("CHITRALI LANGUAGE") ? getResources().getString(R.string.pet_chitrali) :
                                                                                                                (obs[0][1].equals("HINDKO LANGUAGE") ? getResources().getString(R.string.pet_hindko) :
                                                                                                                        (obs[0][1].equals("KALAASHA LANGUAGE") ? getResources().getString(R.string.pet_kalaasha) :
                                                                                                                                (obs[0][1].equals("KASHMIRI LANGUAGE") ? getResources().getString(R.string.pet_kashmiri) :
                                                                                                                                        (obs[0][1].equals("PERSIAN LANGUAGE") ? getResources().getString(R.string.pet_persian) :
                                                                                                                                                (obs[0][1].equals("BALTI LANGUAGE") ? getResources().getString(R.string.pet_balti) :
                                                                                                                                                        (obs[0][1].equals("MAKRANI LANGUAGE") ? getResources().getString(R.string.pet_makrani) :
                                                                                                                                                                (obs[0][1].equals("OTHER") ? getResources().getString(R.string.pet_other) :
                                                                                                                                                                        (obs[0][1].equals("REFUSED") ? getResources().getString(R.string.refused) : getResources().getString(R.string.pet_unknown))))))))))))))))))));
                motherTongue.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("OTHER LANGUAGE")) {
                otherMotherTongue.getEditText().setText(obs[0][1]);
                otherMotherTongue.setVisibility(View.VISIBLE);
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

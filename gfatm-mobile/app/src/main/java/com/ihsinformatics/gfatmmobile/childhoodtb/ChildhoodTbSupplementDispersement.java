package com.ihsinformatics.gfatmmobile.childhoodtb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbSupplementDispersement extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    Snackbar snackbar;
    ScrollView scrollView;


    Boolean dateChoose = false;

    TitledRadioGroup currentPatientTreatment;
    TitledRadioGroup tbClassification;
    TitledCheckBoxes tbTreatmentAdditionalTreatment;
    TitledRadioGroup tbTreatmentPediasureDispersed;
    TitledRadioGroup tbTreatmentPediasureQuantity;
    TitledRadioGroup tbTreatmentVitaminBDispersed;
    TitledRadioGroup tbTreatmentVitaminBQuantity;
    TitledRadioGroup tbTreatmentIronDispersed;
    TitledRadioGroup tbTreatmentIronQuantity;
    TitledRadioGroup tbTreatmentAnthelminthicDispersed;
    TitledRadioGroup tbTreatmentAnthelminthicQuantity;
    TitledRadioGroup tbTreatmentCalpolDispersed;
    TitledRadioGroup tbTreatmentCalpolQuantity;


    TitledCheckBoxes nonTBAdditionalTreatment;
    TitledRadioGroup nonTBVitaminBDispersed;
    TitledRadioGroup nonTBVitaminBQuantity;
    TitledRadioGroup nonTBIronDispersed;
    TitledRadioGroup nonTBIronQuantity;
    TitledRadioGroup nonTBAnthelminthicDispersed;
    TitledRadioGroup nonTBAnthelminthicQuantity;
    TitledRadioGroup nonTBCalpolDispersed;
    TitledRadioGroup nonTBCalpolQuantity;

    TitledButton nextDateOfDrug;

    MyTextView staffInstruction;


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
        formName = Forms.CHILDHOODTB_SUPPLEMENT_DISBURSEMENT;
        form = Forms.childhoodTb_supplement_disbursement;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        currentPatientTreatment = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_treatment_currently_on), getResources().getStringArray(R.array.ctb_treatment_currently_on_list), null, App.VERTICAL, App.VERTICAL, true, "PATIENT CURRENT TREATMENT", new String[]{"ATT", "ANTIBIOTIC TRIAL", "IPT", "UNDER INVESTIGATION"});
        tbClassification = new TitledRadioGroup(context, getResources().getString(R.string.ctb_tb_patient_tb_treatment), getResources().getString(R.string.ctb_tb_classification), getResources().getStringArray(R.array.ctb_tb_classification_list), null, App.VERTICAL, App.VERTICAL, true, "TUBERCULOSIS INFECTION TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION", "DRUG-RESISTANT TB"});
        tbTreatmentAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_mo_initiate_additional_treatment), getResources().getStringArray(R.array.ctb_pediasure_vitamin_iron_anthelminthic_calpol), null, App.VERTICAL, App.VERTICAL, false, "ADDITIONAL TREATMENT TO TB PATIENT", new String[]{"IRON", "MULTIVITAMIN", "ANTHELMINTHIC", "PEDIASURE", "VITAMIN B COMPLEX", "CALPOL", "OTHER ADDITIONAL TREATMENT", "NONE"});
        tbTreatmentPediasureDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pediasure_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "PEDIASURE DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        tbTreatmentPediasureQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_pediasure_quantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF PEDIASURE DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        tbTreatmentVitaminBDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_vitamin_b_complex_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "VITAMIN B-COMPLEX DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        tbTreatmentVitaminBQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_vitamin_b_qunantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF VITAMIN B COMPLEX DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        tbTreatmentIronDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "IRON DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        tbTreatmentIronQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_qunantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF IRON DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        tbTreatmentAnthelminthicDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "ANTHELMINTIC DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        tbTreatmentAnthelminthicQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_quantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF ANTHELMINTHIC DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        tbTreatmentCalpolDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_calpol_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "CALPOL DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        tbTreatmentCalpolQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_calpol_quantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF CALPOL DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));

        nonTBAdditionalTreatment = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_mo_initiate_additional_treatment), getResources().getStringArray(R.array.ctb_vitamin_iron_anthelminthic_calpol), null, App.VERTICAL, App.VERTICAL, false, "ADDITIONAL TREATMENT TO NON TB PATIENT", new String[]{"VITAMIN B COMPLEX", "IRON", "ANTHELMINTHIC", "CALPOL"});
        nonTBVitaminBDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_vitamin_b_complex_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "VITAMIN B-COMPLEX DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        nonTBVitaminBQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_vitamin_b_qunantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF VITAMIN B COMPLEX DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        nonTBIronDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "IRON DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        nonTBIronQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_iron_qunantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF IRON DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        nonTBAnthelminthicDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "ANTHELMINTIC DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        nonTBAnthelminthicQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_anthelminthic_quantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF ANTHELMINTHIC DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));
        nonTBCalpolDispersed = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_calpol_dispersed), getResources().getStringArray(R.array.ctb_yes_no_incomplete), null, App.HORIZONTAL, App.VERTICAL, true, "CALPOL DISPERSED", new String[]{"YES", "NO", "INCOMPLETE"});
        nonTBCalpolQuantity = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_calpol_quantity), getResources().getStringArray(R.array.ctb_1_to_4_list), null, App.HORIZONTAL, App.VERTICAL, true, "QUANTITY OF CALPOL DISPERSED", getResources().getStringArray(R.array.ctb_1_to_4_list));


        nextDateOfDrug = new TitledButton(context, null, getResources().getString(R.string.ctb_next_date_drug_dispersal), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        staffInstruction = new MyTextView(context, getResources().getString(R.string.ctb_staff_instruction));

        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        views = new View[]{
                formDate.getButton(),
                currentPatientTreatment.getRadioGroup(),
                tbClassification.getRadioGroup(),
                tbTreatmentPediasureDispersed.getRadioGroup(),
                tbTreatmentPediasureQuantity.getRadioGroup(),
                tbTreatmentVitaminBDispersed.getRadioGroup(),
                tbTreatmentVitaminBQuantity.getRadioGroup(),
                tbTreatmentIronDispersed.getRadioGroup(),
                tbTreatmentIronQuantity.getRadioGroup(),
                tbTreatmentAnthelminthicDispersed.getRadioGroup(),
                tbTreatmentAnthelminthicQuantity.getRadioGroup(),
                tbTreatmentCalpolDispersed.getRadioGroup(),
                tbTreatmentCalpolQuantity.getRadioGroup(),
                nonTBVitaminBDispersed.getRadioGroup(),
                nonTBVitaminBQuantity.getRadioGroup(),
                nonTBIronDispersed.getRadioGroup(),
                nonTBIronQuantity.getRadioGroup(),
                nonTBAnthelminthicDispersed.getRadioGroup(),
                nonTBAnthelminthicQuantity.getRadioGroup(),
                nonTBCalpolDispersed.getRadioGroup(),
                nonTBCalpolQuantity.getRadioGroup(),
                nextDateOfDrug.getButton(),

                nextDateOfDrug};
        viewGroups = new View[][]
                {{
                        formDate,
                        currentPatientTreatment,
                        tbClassification,
                        tbTreatmentAdditionalTreatment,
                        tbTreatmentPediasureDispersed,
                        tbTreatmentPediasureQuantity,
                        tbTreatmentVitaminBDispersed,
                        tbTreatmentVitaminBQuantity,
                        tbTreatmentIronDispersed,
                        tbTreatmentIronQuantity,
                        tbTreatmentAnthelminthicDispersed,
                        tbTreatmentAnthelminthicQuantity,
                        tbTreatmentCalpolDispersed,
                        tbTreatmentCalpolQuantity,


                        nonTBAdditionalTreatment,
                        nonTBVitaminBDispersed,
                        nonTBVitaminBQuantity,
                        nonTBIronDispersed,
                        nonTBIronQuantity,
                        nonTBAnthelminthicDispersed,
                        nonTBAnthelminthicQuantity,
                        nonTBCalpolDispersed,
                        nonTBCalpolQuantity,
                        nextDateOfDrug,
                        staffInstruction
                }};
        formDate.getButton().setOnClickListener(this);

        currentPatientTreatment.getRadioGroup().setOnCheckedChangeListener(this);
        tbClassification.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentPediasureDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentPediasureQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentVitaminBDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentVitaminBQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentIronDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentIronQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentAnthelminthicDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentAnthelminthicQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentCalpolDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        tbTreatmentCalpolQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBVitaminBDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBVitaminBQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBIronDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBIronQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBAnthelminthicDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBAnthelminthicQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBCalpolDispersed.getRadioGroup().setOnCheckedChangeListener(this);
        nonTBCalpolQuantity.getRadioGroup().setOnCheckedChangeListener(this);
        nextDateOfDrug.getButton().setOnClickListener(this);

        for (CheckBox cb : tbTreatmentAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        for (CheckBox cb : nonTBAdditionalTreatment.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();

    }

    @Override
    public void updateDisplay() {

        String personDOB = App.getPatient().getPerson().getBirthdate();
        Calendar maxDateCalender = formDateCalendar.getInstance();
        maxDateCalender.setTime(formDateCalendar.getTime());
        maxDateCalender.add(Calendar.YEAR, 2);
        if (snackbar != null)
            snackbar.dismiss();
        Date date = new Date();
        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {
            String formDa = formDate.getButton().getText().toString();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.ctb_form_date_less_than_patient_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                secondDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, 1);
                secondDateCalendar.setTime(requiredDate.getTime());
            }

        }

        String nextAppointmentDateString = App.getSqlDate(secondDateCalendar);
        Date nextAppointmentDate = App.stringToDate(nextAppointmentDateString, "yyyy-MM-dd");

        String formDateString = App.getSqlDate(formDateCalendar);
        Date formStDate = App.stringToDate(formDateString, "yyyy-MM-dd");


        if (!dateChoose) {
            Calendar requiredDate = formDateCalendar.getInstance();
            requiredDate.setTime(formDateCalendar.getTime());
            requiredDate.add(Calendar.DATE, 30);

            if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                secondDateCalendar.setTime(requiredDate.getTime());
            } else {
                requiredDate.add(Calendar.DATE, -1);
                secondDateCalendar.setTime(requiredDate.getTime());
            }
        }


        if (!(nextDateOfDrug.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString()))) {
            Calendar dateToday = Calendar.getInstance();
            dateToday.add(Calendar.MONTH, 24);

            String formDa = nextDateOfDrug.getButton().getText().toString();

            if (secondDateCalendar.before(formDateCalendar)) {

                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_date_past), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextDateOfDrug.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

            } else if (secondDateCalendar.after(dateToday)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_cant_be_greater_than_24_months), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextDateOfDrug.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (secondDateCalendar.before(secondDateCalendar)) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_date_of_next_visit_cant_be_before_missed_visit_date), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextDateOfDrug.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else if (nextAppointmentDate.compareTo(formStDate) == 0) {
                secondDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_start_date_and_next_visit_date_cant_be_same), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                nextDateOfDrug.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
            } else
                nextDateOfDrug.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        }
        dateChoose = false;
        formDate.getButton().setEnabled(true);
        nextDateOfDrug.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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


        observations.add(new String[]{"NEXT DATE OF DRUG DISPERSAL", App.getSqlDateTime(secondDateCalendar)});


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
                    id = serverService.saveFormLocally("Childhood TB-Supplement Disbursement Form", form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting("Childhood TB-Supplement Disbursement Form", form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
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

        return true;
    }

    @Override
    public void refill(int formId) {
        super.refill(formId);

        OfflineForm fo = serverService.getSavedFormById(formId);
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("NEXT DATE OF DRUG DISPERSAL")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                nextDateOfDrug.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
                nextDateOfDrug.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        }
        if (view == nextDateOfDrug.getButton()) {
            nextDateOfDrug.getButton().setEnabled(false);
            showDateDialog(secondDateCalendar, true, false, true);
            /*Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");*/
            dateChoose = true;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_att))) {
            for (CheckBox cb : tbTreatmentAdditionalTreatment.getCheckedBoxes()) {
                if (App.get(cb).equals(getResources().getString(R.string.ctb_pediasure))) {
                    if (cb.isChecked()) {
                        tbTreatmentPediasureDispersed.setVisibility(View.VISIBLE);
                        if (App.get(tbTreatmentPediasureDispersed).equals(getResources().getString(R.string.yes))) {
                            tbTreatmentPediasureQuantity.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tbTreatmentPediasureDispersed.setVisibility(View.GONE);
                        tbTreatmentPediasureQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_vitamin_B_complex))) {
                    if (cb.isChecked()) {
                        tbTreatmentVitaminBDispersed.setVisibility(View.VISIBLE);
                        if (App.get(tbTreatmentVitaminBDispersed).equals(getResources().getString(R.string.yes))) {
                            tbTreatmentVitaminBQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tbTreatmentVitaminBDispersed.setVisibility(View.GONE);
                        tbTreatmentVitaminBQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_iron))) {
                    if (cb.isChecked()) {
                        tbTreatmentIronDispersed.setVisibility(View.VISIBLE);
                        if (App.get(tbTreatmentIronDispersed).equals(getResources().getString(R.string.yes))) {
                            tbTreatmentIronQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tbTreatmentIronDispersed.setVisibility(View.GONE);
                        tbTreatmentIronQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_anthelminthic))) {
                    if (cb.isChecked()) {
                        tbTreatmentAnthelminthicDispersed.setVisibility(View.VISIBLE);
                        if (App.get(tbTreatmentAnthelminthicDispersed).equals(getResources().getString(R.string.yes))) {
                            tbTreatmentAnthelminthicQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tbTreatmentAnthelminthicDispersed.setVisibility(View.GONE);
                        tbTreatmentAnthelminthicQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_calpol))) {
                    if (cb.isChecked()) {
                        tbTreatmentCalpolDispersed.setVisibility(View.VISIBLE);
                        if (App.get(tbTreatmentCalpolDispersed).equals(getResources().getString(R.string.yes))) {
                            tbTreatmentCalpolQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        tbTreatmentCalpolDispersed.setVisibility(View.GONE);
                        tbTreatmentCalpolQuantity.setVisibility(View.GONE);
                    }
                }

            }
        }

        if (currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_antibiotic_trial)) || currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_ipt)) || currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_under_investigation))) {
            for (CheckBox cb : nonTBAdditionalTreatment.getCheckedBoxes()) {
                if (App.get(cb).equals(getResources().getString(R.string.ctb_vitamin_B_complex))) {
                    if (cb.isChecked()) {
                        nonTBVitaminBDispersed.setVisibility(View.VISIBLE);
                        if (App.get(nonTBVitaminBDispersed).equals(getResources().getString(R.string.yes))) {
                            nonTBVitaminBQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        nonTBVitaminBDispersed.setVisibility(View.GONE);
                        nonTBVitaminBQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_iron))) {
                    if (cb.isChecked()) {
                        nonTBIronDispersed.setVisibility(View.VISIBLE);
                        if (App.get(nonTBIronDispersed).equals(getResources().getString(R.string.yes))) {
                            nonTBIronQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        nonTBIronDispersed.setVisibility(View.GONE);
                        nonTBIronQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_anthelminthic))) {
                    if (cb.isChecked()) {
                        nonTBAnthelminthicDispersed.setVisibility(View.VISIBLE);
                        if (App.get(nonTBIronDispersed).equals(getResources().getString(R.string.yes))) {
                            nonTBIronQuantity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        nonTBAnthelminthicDispersed.setVisibility(View.GONE);
                        nonTBAnthelminthicQuantity.setVisibility(View.GONE);
                    }
                } else if (App.get(cb).equals(getResources().getString(R.string.ctb_calpol))) {
                    if (cb.isChecked()) {
                        nonTBCalpolDispersed.setVisibility(View.VISIBLE);
                        if (App.get(nonTBCalpolDispersed).equals(getResources().getString(R.string.yes))) {
                            nonTBCalpolQuantity.setVisibility(View.VISIBLE);
                        }


                    } else {
                        nonTBCalpolDispersed.setVisibility(View.GONE);
                        nonTBCalpolQuantity.setVisibility(View.GONE);
                    }
                }

            }
        }


    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        tbClassification.setVisibility(View.GONE);
        tbTreatmentAdditionalTreatment.setVisibility(View.GONE);
        tbTreatmentPediasureDispersed.setVisibility(View.GONE);
        tbTreatmentPediasureQuantity.setVisibility(View.GONE);
        tbTreatmentVitaminBDispersed.setVisibility(View.GONE);
        tbTreatmentVitaminBQuantity.setVisibility(View.GONE);
        tbTreatmentIronDispersed.setVisibility(View.GONE);
        tbTreatmentIronQuantity.setVisibility(View.GONE);
        tbTreatmentAnthelminthicDispersed.setVisibility(View.GONE);
        tbTreatmentAnthelminthicQuantity.setVisibility(View.GONE);
        tbTreatmentCalpolDispersed.setVisibility(View.GONE);
        tbTreatmentCalpolQuantity.setVisibility(View.GONE);
        nonTBAdditionalTreatment.setVisibility(View.GONE);
        nonTBVitaminBDispersed.setVisibility(View.GONE);
        nonTBVitaminBQuantity.setVisibility(View.GONE);
        nonTBIronDispersed.setVisibility(View.GONE);
        nonTBIronQuantity.setVisibility(View.GONE);
        nonTBAnthelminthicDispersed.setVisibility(View.GONE);
        nonTBAnthelminthicQuantity.setVisibility(View.GONE);
        nonTBCalpolDispersed.setVisibility(View.GONE);
        nonTBCalpolQuantity.setVisibility(View.GONE);


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        secondDateCalendar = Calendar.getInstance();
        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 30);
        Calendar requiredDate = formDateCalendar.getInstance();
        requiredDate.setTime(formDateCalendar.getTime());
        requiredDate.add(Calendar.DATE, 30);

        if (requiredDate.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            secondDateCalendar.setTime(requiredDate.getTime());
        } else {
            requiredDate.add(Calendar.DATE, -1);
            secondDateCalendar.setTime(requiredDate.getTime());
        }

        nextDateOfDrug.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

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

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar;
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;

            else
                return null;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setTag(getArguments().getInt("type"));
            if (!getArguments().getBoolean("allowFutureDate", false))
                dialog.getDatePicker().setMaxDate(new Date().getTime());
            if (!getArguments().getBoolean("allowPastDate", false))
                dialog.getDatePicker().setMinDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            if (((int) view.getTag()) == DATE_DIALOG_ID)
                formDateCalendar.set(yy, mm, dd);
            else if (((int) view.getTag()) == SECOND_DATE_DIALOG_ID)
                secondDateCalendar.set(yy, mm, dd);
            updateDisplay();

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == currentPatientTreatment.getRadioGroup()) {
            currentPatientTreatment.getQuestionView().setError(null);
            if (currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_att))) {
                tbClassification.setVisibility(View.VISIBLE);
                tbTreatmentAdditionalTreatment.setVisibility(View.VISIBLE);

                nonTBAdditionalTreatment.setVisibility(View.GONE);
                nonTBVitaminBDispersed.setVisibility(View.GONE);
                nonTBVitaminBQuantity.setVisibility(View.GONE);
                nonTBIronDispersed.setVisibility(View.GONE);
                nonTBIronQuantity.setVisibility(View.GONE);
                nonTBAnthelminthicDispersed.setVisibility(View.GONE);
                nonTBAnthelminthicQuantity.setVisibility(View.GONE);
                nonTBCalpolDispersed.setVisibility(View.GONE);
                nonTBCalpolQuantity.setVisibility(View.GONE);


            } else if (currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_antibiotic_trial)) || currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_ipt)) || currentPatientTreatment.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_under_investigation))) {
                nonTBAdditionalTreatment.setVisibility(View.VISIBLE);
                for (CheckBox cb : nonTBAdditionalTreatment.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_vitamin_B_complex)) && cb.isChecked() == true) {
                        nonTBVitaminBDispersed.setVisibility(View.VISIBLE);
                    }
                    if (cb.getText().equals(getResources().getString(R.string.ctb_iron)) && cb.isChecked() == true) {
                        nonTBIronDispersed.setVisibility(View.VISIBLE);
                    }
                    if (cb.getText().equals(getResources().getString(R.string.ctb_anthelminthic)) && cb.isChecked() == true) {
                        nonTBAnthelminthicDispersed.setVisibility(View.VISIBLE);
                    }
                    if (cb.getText().equals(getResources().getString(R.string.ctb_calpol)) && cb.isChecked() == true) {
                        nonTBCalpolDispersed.setVisibility(View.VISIBLE);
                    }
                }

                tbClassification.setVisibility(View.GONE);
                tbTreatmentAdditionalTreatment.setVisibility(View.GONE);
                tbTreatmentPediasureDispersed.setVisibility(View.GONE);
                tbTreatmentPediasureQuantity.setVisibility(View.GONE);
                tbTreatmentVitaminBDispersed.setVisibility(View.GONE);
                tbTreatmentVitaminBQuantity.setVisibility(View.GONE);
                tbTreatmentIronDispersed.setVisibility(View.GONE);
                tbTreatmentIronQuantity.setVisibility(View.GONE);
                tbTreatmentAnthelminthicDispersed.setVisibility(View.GONE);
                tbTreatmentAnthelminthicQuantity.setVisibility(View.GONE);
                tbTreatmentCalpolDispersed.setVisibility(View.GONE);
                tbTreatmentCalpolQuantity.setVisibility(View.GONE);
            }
        } else if (group == tbTreatmentPediasureDispersed.getRadioGroup()) {
            tbTreatmentPediasureDispersed.getQuestionView().setError(null);
            if (tbTreatmentPediasureDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbTreatmentPediasureQuantity.setVisibility(View.VISIBLE);
            } else {
                tbTreatmentPediasureQuantity.setVisibility(View.GONE);
            }
        } else if (group == tbTreatmentPediasureQuantity.getRadioGroup()) {
            tbTreatmentPediasureQuantity.getQuestionView().setError(null);
        } else if (group == tbTreatmentVitaminBDispersed.getRadioGroup()) {
            tbTreatmentVitaminBDispersed.getQuestionView().setError(null);
            if (tbTreatmentVitaminBDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbTreatmentVitaminBQuantity.setVisibility(View.VISIBLE);
            } else {
                tbTreatmentVitaminBQuantity.setVisibility(View.GONE);
            }
        } else if (group == tbTreatmentVitaminBQuantity.getRadioGroup()) {
            tbTreatmentVitaminBQuantity.getQuestionView().setError(null);
        } else if (group == tbTreatmentIronDispersed.getRadioGroup()) {
            tbTreatmentIronDispersed.getQuestionView().setError(null);
            if (tbTreatmentIronDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbTreatmentIronQuantity.setVisibility(View.VISIBLE);
            } else {
                tbTreatmentIronQuantity.setVisibility(View.GONE);
            }
        } else if (group == tbTreatmentIronQuantity.getRadioGroup()) {
            tbTreatmentIronQuantity.getQuestionView().setError(null);
        } else if (group == tbTreatmentAnthelminthicDispersed.getRadioGroup()) {
            tbTreatmentAnthelminthicDispersed.getQuestionView().setError(null);
            if (tbTreatmentAnthelminthicDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbTreatmentAnthelminthicQuantity.setVisibility(View.VISIBLE);
            } else {
                tbTreatmentAnthelminthicQuantity.setVisibility(View.GONE);
            }
        } else if (group == tbTreatmentAnthelminthicQuantity.getRadioGroup()) {
            tbTreatmentAnthelminthicQuantity.getQuestionView().setError(null);
        } else if (group == tbTreatmentCalpolDispersed.getRadioGroup()) {
            tbTreatmentCalpolDispersed.getQuestionView().setError(null);
            if (tbTreatmentCalpolDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbTreatmentCalpolQuantity.setVisibility(View.VISIBLE);
            } else {
                tbTreatmentCalpolQuantity.setVisibility(View.GONE);
            }
        } else if (group == tbTreatmentCalpolQuantity.getRadioGroup()) {
            tbTreatmentCalpolQuantity.getQuestionView().setError(null);
        } else if (group == nonTBVitaminBDispersed.getRadioGroup()) {
            nonTBVitaminBDispersed.getQuestionView().setError(null);
            if (nonTBVitaminBDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                nonTBVitaminBQuantity.setVisibility(View.VISIBLE);
            } else {
                nonTBVitaminBQuantity.setVisibility(View.GONE);
            }
        } else if (group == nonTBVitaminBQuantity.getRadioGroup()) {
            nonTBVitaminBQuantity.getQuestionView().setError(null);
        } else if (group == nonTBIronDispersed.getRadioGroup()) {
            nonTBIronDispersed.getQuestionView().setError(null);
            if (nonTBIronDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                nonTBIronQuantity.setVisibility(View.VISIBLE);
            } else {
                nonTBIronQuantity.setVisibility(View.GONE);
            }
        } else if (group == nonTBIronQuantity.getRadioGroup()) {
            nonTBIronQuantity.getQuestionView().setError(null);
        } else if (group == nonTBAnthelminthicDispersed.getRadioGroup()) {
            nonTBAnthelminthicDispersed.getQuestionView().setError(null);
            if (nonTBAnthelminthicDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                nonTBAnthelminthicQuantity.setVisibility(View.VISIBLE);
            } else {
                nonTBAnthelminthicQuantity.setVisibility(View.GONE);
            }
        } else if (group == nonTBAnthelminthicQuantity.getRadioGroup()) {
            nonTBAnthelminthicQuantity.getQuestionView().setError(null);
        } else if (group == nonTBCalpolDispersed.getRadioGroup()) {
            nonTBCalpolDispersed.getQuestionView().setError(null);
            if (nonTBCalpolDispersed.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                nonTBCalpolQuantity.setVisibility(View.VISIBLE);
            } else {
                nonTBCalpolQuantity.setVisibility(View.GONE);
            }
        } else if (group == nonTBCalpolQuantity.getRadioGroup()) {
            nonTBCalpolQuantity.getQuestionView().setError(null);
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

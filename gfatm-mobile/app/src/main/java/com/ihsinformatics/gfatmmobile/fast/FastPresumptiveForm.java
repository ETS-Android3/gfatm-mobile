package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 1/5/2017.
 */

public class FastPresumptiveForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    Boolean emptyError = false;


    // Views...
    TitledEditText husbandName;
    TitledEditText fatherName;
    TitledRadioGroup patientAttendant;
    TitledSpinner patientConsultation;
    TitledEditText patientConsultationOther;
    MyTextView patientSymptomTitle;
    MyTextView patientDemographicsTitle;
    TitledRadioGroup typeOfScreen;
    TitledRadioGroup cough;
    TitledRadioGroup productiveCough;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledSpinner feverDuration;
    TitledRadioGroup nightSweats;
    TitledRadioGroup weightLoss;
    MyTextView patientTbHistoryTitle;
    TitledRadioGroup tbContact;
    TitledRadioGroup tbHistory;
    TitledRadioGroup presumptiveTb;
    TitledRadioGroup pregnancyHistory;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 3;
        formName = Forms.FAST_PRESUMPTIVE_FORM;
        form = Forms.fastPresumptiveForm;

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
            for (int i = 0; i < pageCount; i++) {
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
        if (App.getPatient().getPerson().getAge() < 15) {
            submitButton.setEnabled(false);
            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.fast_patient_age_less_than_15));
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
        }

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        patientDemographicsTitle = new MyTextView(context, getResources().getString(R.string.fast_demographics_title));
        patientDemographicsTitle.setTypeface(null, Typeface.BOLD);
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.fast_husband_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "PARTNER FULL NAME");
        fatherName = new TitledEditText(context, null, getResources().getString(R.string.fast_father_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "FATHER NAME");
        patientAttendant = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_this_patient_or_attendant), getResources().getStringArray(R.array.fast_patient_or_attendant_list), getResources().getString(R.string.fast_patient_title), App.VERTICAL, App.VERTICAL, true, "PERSON ATTENDING FACILITY", new String[]{"SELF", "ATTENDANT"});
        patientConsultation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_speciality_patient_consult), getResources().getStringArray(R.array.fast_patient_consultation_list), "", App.VERTICAL, false, "PATIENT CONSULTATION DEPARTMENT", getResources().getStringArray(R.array.fast_patient_consultation_list_obs));
        patientConsultationOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER FACILITY DEPARTMENT");
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_this_patient_pregnant), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "PREGNANCY STATUS",  getResources().getStringArray(R.array.fast_choice_list_concepts));


        patientSymptomTitle = new MyTextView(context, getResources().getString(R.string.fast_symptoms_title));
        patientSymptomTitle.setTypeface(null, Typeface.BOLD);
        typeOfScreen = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_type_of_screen), getResources().getStringArray(R.array.fast_type_of_screen_list), "", App.HORIZONTAL, App.VERTICAL, true,"TYPE OF SCREENING",new String[]{"CXR Screening","VERBAL SYMPTOM SCREENING"});
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_cough_period_title), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "COUGH LASTING MORE THAN 2 WEEKS",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        productiveCough = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_is_your_cough_productive), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true,"PRODUCTIVE COUGH",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_sputum_in_blood), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "HEMOPTYSIS",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_fever), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "FEVER",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        feverDuration = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_how_long_you_have_fever), getResources().getStringArray(R.array.fast_duration_list), "", App.VERTICAL, false,"FEVER DURATION",new String[]{"","FEVER LASTING LESS THAN TWO WEEKS" ,"FEVER LASTING FOR 2 TO 3 WEEKS" ,"FEVER LASTING MORE THAN THREE WEEKS" , "UNKNOWN","REFUSED"});
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_night_sweats), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "NIGHT SWEATS",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_do_you_have_unexplained_weight_loss), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "WEIGHT LOSS",  getResources().getStringArray(R.array.fast_choice_list_concepts));

        patientTbHistoryTitle = new MyTextView(context, getResources().getString(R.string.fast_tbhistory_title));
        patientTbHistoryTitle.setTypeface(null, Typeface.BOLD);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_tb_before), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "HISTORY OF TUBERCULOSIS",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        tbContact = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_close_with_someone_diagnosed), getResources().getStringArray(R.array.fast_choice_list), "", App.VERTICAL, App.VERTICAL, true, "TUBERCULOSIS CONTACT",  getResources().getStringArray(R.array.fast_choice_list_concepts));
        presumptiveTb = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_presumptive_tb), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true,"PRESUMPTIVE TUBERCULOSIS",getResources().getStringArray(R.array.yes_no_list_concept));


        // Used for reset fields...
        views = new View[]{formDate.getButton(), husbandName.getEditText(), fatherName.getEditText(), patientAttendant.getRadioGroup(), patientConsultation.getSpinner(),
                patientConsultationOther.getEditText(), cough.getRadioGroup(), typeOfScreen.getRadioGroup(),
                productiveCough.getRadioGroup(), haemoptysis.getRadioGroup(), fever.getRadioGroup(), feverDuration.getSpinner(),
                tbContact.getRadioGroup(), tbHistory.getRadioGroup(), nightSweats.getRadioGroup(), weightLoss.getRadioGroup(), presumptiveTb.getRadioGroup(), pregnancyHistory.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, patientDemographicsTitle, husbandName, fatherName, patientAttendant, patientConsultation, patientConsultationOther, pregnancyHistory},
                        {patientSymptomTitle, typeOfScreen, cough, productiveCough, haemoptysis, fever, feverDuration, nightSweats, weightLoss},
                        {patientTbHistoryTitle, tbHistory, tbContact, presumptiveTb}};

        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        patientConsultation.getSpinner().setOnItemSelectedListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        productiveCough.getRadioGroup().setOnCheckedChangeListener(this);
        typeOfScreen.getRadioGroup().setOnCheckedChangeListener(this);
        formDate.getButton().setOnClickListener(this);
        patientAttendant.getRadioGroup().setOnCheckedChangeListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        tbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        }

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {

        if (App.getPatient().getPerson().getAge() <= 14) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.fast_patient_age_less_than_15_cant_submit));
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
        } else {
            Boolean error = super.validate();
            emptyError = error;


            if (husbandName.getVisibility() == View.VISIBLE && App.get(husbandName).length() == 1) {
                if (App.isLanguageRTL())
                    gotoPage(2);
                else
                    gotoPage(0);
                husbandName.getEditText().setError(getString(R.string.fast_husband_name_cannot_be_less_than_2_characters));
                husbandName.getEditText().requestFocus();
                error = true;
            }

            if (fatherName.getVisibility() == View.VISIBLE && App.get(fatherName).length() == 1) {
                if (App.isLanguageRTL())
                    gotoPage(2);
                else
                    gotoPage(0);
                fatherName.getEditText().setError(getString(R.string.fast_father_name_cannot_be_less_than_2_characters));
                fatherName.getEditText().requestFocus();
                error = true;
            }


            if (error) {

                int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
                if (!emptyError)
                    alertDialog.setMessage(getString(R.string.form_error));
                else
                    alertDialog.setMessage(getString(R.string.fast_required_field_error));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //   DrawableCompat.setTint(clearIcon, color);
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


     /*   if (husbandName.getVisibility() == View.VISIBLE && !(App.get(husbandName).isEmpty()))
            observations.add(new String[]{"PARTNER FULL NAME", App.get(husbandName)});

        if (fatherName.getVisibility() == View.VISIBLE && !App.get(fatherName).isEmpty())
            observations.add(new String[]{"FATHER NAME", App.get(fatherName)});
*/
        // observations.add(new String[]{"PERSON ATTENDING FACILITY", App.get(patientAttendant).equals(getResources().getString(R.string.fast_patient_title)) ? "SELF" : "ATTENDANT"});

      /*  if (patientConsultation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT CONSULTATION DEPARTMENT", App.get(patientConsultation).equals(getResources().getString(R.string.fast_chesttbclinic_title)) ? "CHEST MEDICINE DEPARTMENT" :
                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_medicine_title)) ? "GENERAL MEDICINE DEPARTMENT" :
                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_ent_title)) ? "EAR, NOSE AND THROAT DEPARTMENT" :
                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_gynaeobstetrics_otherthen_title)) ? "OBSTETRICS AND GYNECOLOGY DEPARTMENT" :
                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_pregnancy_title)) ? "ANTENATAL DEPARTMENT" :
                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_surgery_title)) ? "GENERAL SURGERY DEPARTMENT" :
                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_orthopedics_title)) ? "ORTHOPEDIC DEPARTMENT" :
                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_emergency_title)) ? "EMERGENCY DEPARTMENT" :
                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_paediatrics_title)) ? "PAEDIATRICS DEPARTMENT" :
                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_dermatology)) ? "DERMATOLOGY DEPARTMENT" :
                                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_neurology_title)) ? "NEUROLOGY DEPARTMENT" :
                                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_cardiology_title)) ? "CARDIOLOGY DEPARTMENT" :
                                                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_urology_title)) ? "UROLOGY DEPARTMENT" :
                                                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_psychiatry_title)) ? "PSYCHIATRY DEPARTMENT" :
                                                                                                                            (App.get(patientConsultation).equals(getResources().getString(R.string.fast_opthamology_title)) ? "OPHTHALMOLOGY DEPARTMENT" :
                                                                                                                                    (App.get(patientConsultation).equals(getResources().getString(R.string.fast_endocrionology_title)) ? "ENDOCRINOLOGY DEPARTMENT" : "OTHER FACILITY DEPARTMENT")))))))))))))))});
*/
      /*if (patientConsultationOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER FACILITY DEPARTMENT", App.get(patientConsultationOther)});
*/
       /* if (pregnancyHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PREGNANCY STATUS", App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(pregnancyHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});
*/
   /*     if (cough.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH LASTING MORE THAN 2 WEEKS", App.get(cough).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(cough).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(cough).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});
*/
      /*  if (typeOfScreen.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TYPE OF SCREENING", App.get(typeOfScreen).equals(getResources().getString(R.string.fast_cxr)) ? "CXR Screening" :
                    "VERBAL SYMPTOM SCREENING"});*/

/*
        if (productiveCough.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PRODUCTIVE COUGH", App.get(productiveCough).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(productiveCough).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(productiveCough).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (haemoptysis.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HEMOPTYSIS", App.get(haemoptysis).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(haemoptysis).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(haemoptysis).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

*/

    /*    if (fever.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(fever).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(fever).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

*/
      /*  if (feverDuration.getVisibility() == View.VISIBLE && !feverDuration.getSpinner().getSelectedItem().equals(""))
            observations.add(new String[]{"FEVER DURATION", App.get(feverDuration).equals(getResources().getString(R.string.fast_less_than_2_weeks_title)) ? "FEVER LASTING LESS THAN TWO WEEKS" :
                    (App.get(feverDuration).equals(getResources().getString(R.string.fast_2to3_weeks)) ? "FEVER LASTING FOR 2 TO 3 WEEKS" :
                            (App.get(feverDuration).equals(getResources().getString(R.string.fast_morethan3weeks)) ? "FEVER LASTING MORE THAN THREE WEEKS" :
                                    (App.get(feverDuration).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN")))});

        if (nightSweats.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(nightSweats).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(nightSweats).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (weightLoss.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(weightLoss).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(weightLoss).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});
*/
      /*  if (tbHistory.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"HISTORY OF TUBERCULOSIS", App.get(tbHistory).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbHistory).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbHistory).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});

        if (tbContact.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS CONTACT", App.get(tbContact).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" :
                    (App.get(tbContact).equals(getResources().getString(R.string.fast_no_title)) ? "NO" :
                            (App.get(tbContact).equals(getResources().getString(R.string.fast_refused_title)) ? "REFUSED" : "UNKNOWN"))});


        if (presumptiveTb.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PRESUMPTIVE TUBERCULOSIS", App.get(presumptiveTb).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});
*/

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
                    id = serverService.saveFormLocallyTesting("FAST-Presumptive", form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveProgramEnrollement(App.getSqlDate(formDateCalendar), "FAST", id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting("FAST-Presumptive", form, formDateCalendar, observations.toArray(new String[][]{}), id);
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int formId) {
        super.refill(formId);
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == patientConsultation.getSpinner()) {
            patientConsultation.getQuestionView().setError(null);
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                patientConsultationOther.setVisibility(View.VISIBLE);
            } else {
                patientConsultationOther.setVisibility(View.GONE);
            }
        }
        if (spinner == feverDuration.getSpinner()) {
            feverDuration.getQuestionView().setError(null);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patientAttendant.getRadioGroup()) {
            if (patientAttendant.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_attendant_title))) {
                patientConsultation.setVisibility(View.GONE);
                patientConsultationOther.setVisibility(View.GONE);

            } else {
                patientConsultation.setVisibility(View.VISIBLE);
                if (patientConsultation.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    patientConsultationOther.setVisibility(View.VISIBLE);
                } else {
                    patientConsultationOther.setVisibility(View.GONE);
                }
            }
        } else if (radioGroup == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
                presumptiveTb.getRadioGroup().setEnabled(false);

                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    rb.setClickable(false);
                }

                productiveCough.setVisibility(View.VISIBLE);
                haemoptysis.setVisibility(View.VISIBLE);
            } else {
                productiveCough.setVisibility(View.GONE);
                haemoptysis.setVisibility(View.GONE);

                if (tbContact.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) ||
                        tbHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
                    presumptiveTb.getRadioGroup().setEnabled(false);

                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                } else {
                    presumptiveTb.getRadioGroup().getButtons().get(1).setChecked(true);
                    presumptiveTb.getRadioGroup().setEnabled(false);

                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                }

            }
        } else if (radioGroup == fever.getRadioGroup()) {
            if (fever.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                feverDuration.setVisibility(View.VISIBLE);
            } else {
                feverDuration.setVisibility(View.GONE);
            }
        } else if (radioGroup == typeOfScreen.getRadioGroup()) {
            typeOfScreen.getQuestionView().setError(null);
            if (typeOfScreen.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_verbal))) {
                fever.setVisibility(View.VISIBLE);
                if (App.get(fever).equals(getResources().getString(R.string.yes))) {
                    feverDuration.setVisibility(View.VISIBLE);
                }
                nightSweats.setVisibility(View.VISIBLE);
                weightLoss.setVisibility(View.VISIBLE);
                tbHistory.setVisibility(View.VISIBLE);
                tbContact.setVisibility(View.VISIBLE);
            } else {
                fever.setVisibility(View.GONE);
                feverDuration.setVisibility(View.GONE);
                nightSweats.setVisibility(View.GONE);
                weightLoss.setVisibility(View.GONE);
                tbHistory.setVisibility(View.GONE);
                tbContact.setVisibility(View.GONE);
            }
        } else if (radioGroup == tbHistory.getRadioGroup()) {
            if (tbHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
                presumptiveTb.getRadioGroup().setEnabled(false);

                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    rb.setClickable(false);
                }

            } else {

                if (tbContact.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) ||
                        cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
                    presumptiveTb.getRadioGroup().setEnabled(false);

                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                } else {
                    presumptiveTb.getRadioGroup().getButtons().get(1).setChecked(true);
                    presumptiveTb.getRadioGroup().setEnabled(false);

                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                }
            }
        } else if (radioGroup == tbContact.getRadioGroup()) {
            if (tbContact.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
                presumptiveTb.getRadioGroup().setEnabled(false);

                for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                    rb.setClickable(false);
                }

            } else {
                if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) ||
                        tbHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                    presumptiveTb.getRadioGroup().getButtons().get(0).setChecked(true);
                    presumptiveTb.getRadioGroup().setEnabled(false);

                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                } else {
                    presumptiveTb.getRadioGroup().getButtons().get(1).setChecked(true);
                    presumptiveTb.getRadioGroup().setEnabled(false);

                    for (RadioButton rb : presumptiveTb.getRadioGroup().getButtons()) {
                        rb.setClickable(false);
                    }
                }

            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        Log.d("abc", App.getPatient().getPerson().getGender());
        if (App.getPatient().getPerson().getGender().equals("male") || App.getPatient().getPerson().getGender().equals("M")) {
            husbandName.setVisibility(View.GONE);
            pregnancyHistory.setVisibility(View.GONE);
        }
        patientConsultationOther.setVisibility(View.GONE);
        feverDuration.setVisibility(View.GONE);
        productiveCough.setVisibility(View.GONE);
        haemoptysis.setVisibility(View.GONE);
        fever.setVisibility(View.GONE);
        nightSweats.setVisibility(View.GONE);
        weightLoss.setVisibility(View.GONE);
        tbHistory.setVisibility(View.GONE);
        tbContact.setVisibility(View.GONE);


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

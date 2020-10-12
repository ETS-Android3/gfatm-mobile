package com.ihsinformatics.gfatmmobile.common;

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
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class DeceasedPatientSummary extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...

    TitledEditText primaryDiagnosis;
    TitledRadioGroup patientSickAtDiagnosis;
    TitledEditText diagnosisDescription;
    TitledRadioGroup ptbPatient;
    TitledEditText initialCxrDescription;
    TitledRadioGroup pcmPatient;
    TitledRadioGroup tbContact;
    TitledRadioGroup tbInfectionType;
    TitledEditText treatmentDuration;
    TitledRadioGroup weekMonthDuration;
    TitledRadioGroup patientComplianceIssue;
    TitledEditText complianceComments;
    TitledRadioGroup adverseDrugEffect;
    TitledEditText drugSideEffectDetails;
    TitledRadioGroup reportComorbidity;
    TitledEditText comorbidConditionDetail;
    TitledEditText caseSummary;


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
        formName = Forms.DECEASED_PATIENT_SUMMARY;
        form = Forms.deceasedPatientSummary;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        primaryDiagnosis = new TitledEditText(context, null, getResources().getString(R.string.primary_diagnosis), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"PRIMARY DIAGNOSIS");
        patientSickAtDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.patient_sick), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"PATIENT SICK AT DIAGNOSIS",getResources().getStringArray(R.array.yes_no_list_concept));
        diagnosisDescription = new TitledEditText(context, null, getResources().getString(R.string.diangosis_description), "", "", 500, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"DIAGNOSIS DESCRIPTION");
        ptbPatient = new TitledRadioGroup(context, null, getResources().getString(R.string.patient_ptb), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"PULMONARY TUBERCULOSIS PATIENT",getResources().getStringArray(R.array.yes_no_list_concept));
        initialCxrDescription = new TitledEditText(context, null, getResources().getString(R.string.description_cxr), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"INITIAL CXR DESCRIPTION");
        pcmPatient = new TitledRadioGroup(context, null, getResources().getString(R.string.patient_pcm), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"PATIENT HAVE PCM",getResources().getStringArray(R.array.yes_no_list_concept));
        tbContact = new TitledRadioGroup(context, null, getResources().getString(R.string.patient_contact_tb), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"TUBERCULOSIS CONTACT",getResources().getStringArray(R.array.yes_no_list_concept));
        tbInfectionType =  new TitledRadioGroup(context, null, getResources().getString(R.string.dstb_or_drtb), getResources().getStringArray(R.array.dstb_drtb), null, App.HORIZONTAL, App.VERTICAL, true,"TUBERCULOSIS INFECTION TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION" , "DRUG-RESISTANT TB"});
        treatmentDuration = new TitledEditText(context, null, getResources().getString(R.string.duration_treatment), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true,"DURATION OF TREATMENT");
        weekMonthDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.week_month_patient_died), getResources().getStringArray(R.array.month_week_array_list), null, App.HORIZONTAL, App.VERTICAL, true,"DURATION IN WEEK OR MONTH", new String[]{"WEEK","MONTHS"});

        patientComplianceIssue = new TitledRadioGroup(context, null, getResources().getString(R.string.compliance_issue), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"PATIENT COMPLIANCE ISSUE",getResources().getStringArray(R.array.yes_no_list_concept));
        complianceComments = new TitledEditText(context, null, getResources().getString(R.string.compliance_detials), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"COMMENTS ON COMPLIANCE");
        adverseDrugEffect = new TitledRadioGroup(context, null, getResources().getString(R.string.drug_effect), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"ADVERSE DRUG EFFECT",getResources().getStringArray(R.array.yes_no_list_concept));
        drugSideEffectDetails = new TitledEditText(context, null, getResources().getString(R.string.drug_complications_detail), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"DRUG SIDE EFFECTS");
        reportComorbidity = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbid_condition), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true,"COMORBIDITIES REPORTED",getResources().getStringArray(R.array.yes_no_list_concept));
        comorbidConditionDetail = new TitledEditText(context, null, getResources().getString(R.string.what_where_comorbid_cond), "", "", 200, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"COMORBID CONDITION DETAIL");
        caseSummary = new TitledEditText(context, null, getResources().getString(R.string.case_summary), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"CASE SUMMARY");

        // Used for reset fields...
        views = new View[]{formDate.getButton(),
                primaryDiagnosis.getEditText(),
                patientSickAtDiagnosis.getRadioGroup(),
                diagnosisDescription.getEditText(),
                ptbPatient.getRadioGroup(),
                initialCxrDescription.getEditText(),
                pcmPatient.getRadioGroup(),
                tbContact.getRadioGroup(),
                tbInfectionType.getRadioGroup(),
                treatmentDuration.getEditText(),
                weekMonthDuration.getRadioGroup(),
                patientComplianceIssue.getRadioGroup(),
                complianceComments.getEditText(),
                adverseDrugEffect.getRadioGroup(),
                drugSideEffectDetails.getEditText(),
                reportComorbidity.getRadioGroup(),
                comorbidConditionDetail.getEditText(),
                caseSummary.getEditText()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, primaryDiagnosis,
                        patientSickAtDiagnosis,
                        diagnosisDescription,
                        ptbPatient,
                        initialCxrDescription,
                        pcmPatient,
                        tbContact,
                        tbInfectionType,
                        treatmentDuration,
                        weekMonthDuration,
                        patientComplianceIssue,
                        complianceComments,
                        adverseDrugEffect,
                        drugSideEffectDetails,
                        reportComorbidity,
                        comorbidConditionDetail,
                        caseSummary}};

        formDate.getButton().setOnClickListener(this);
        patientSickAtDiagnosis.getRadioGroup().setOnCheckedChangeListener(this);
        ptbPatient.getRadioGroup().setOnCheckedChangeListener(this);
        pcmPatient.getRadioGroup().setOnCheckedChangeListener(this);
        tbContact.getRadioGroup().setOnCheckedChangeListener(this);
        tbInfectionType.getRadioGroup().setOnCheckedChangeListener(this);
        patientComplianceIssue.getRadioGroup().setOnCheckedChangeListener(this);
        adverseDrugEffect.getRadioGroup().setOnCheckedChangeListener(this);
        reportComorbidity.getRadioGroup().setOnCheckedChangeListener(this);
        weekMonthDuration.getRadioGroup().setOnCheckedChangeListener(this);

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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
        Boolean error = super.validate();


        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
          //  DrawableCompat.setTint(clearIcon, color);
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
                    id = serverService.saveFormLocally(formName, form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";



                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
            showDateDialog(formDateCalendar,false,true, false);

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


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }



    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        diagnosisDescription.setVisibility(View.GONE);
        initialCxrDescription.setVisibility(View.GONE);
        tbInfectionType.setVisibility(View.GONE);
        complianceComments.setVisibility(View.GONE);
        drugSideEffectDetails.setVisibility(View.GONE);
        comorbidConditionDetail.setVisibility(View.GONE);


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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == patientSickAtDiagnosis.getRadioGroup()) {
            patientSickAtDiagnosis.getQuestionView().setError(null);
            if (App.get(patientSickAtDiagnosis).equals(getResources().getString(R.string.yes))) {
                diagnosisDescription.setVisibility(View.VISIBLE);
            } else {
                diagnosisDescription.setVisibility(View.GONE);
            }
        }else if (radioGroup == ptbPatient.getRadioGroup()) {
            ptbPatient.getQuestionView().setError(null);
            if (App.get(ptbPatient).equals(getResources().getString(R.string.yes))) {
                initialCxrDescription.setVisibility(View.VISIBLE);
            } else {
                initialCxrDescription.setVisibility(View.GONE);
            }
        }else if (radioGroup == tbContact.getRadioGroup()) {
            tbContact.getQuestionView().setError(null);
            if (App.get(tbContact).equals(getResources().getString(R.string.yes))) {
                tbInfectionType.setVisibility(View.VISIBLE);
            } else {
                tbInfectionType.setVisibility(View.GONE);
            }
        }else if (radioGroup == patientComplianceIssue.getRadioGroup()) {
            patientComplianceIssue.getQuestionView().setError(null);
            if (App.get(patientComplianceIssue).equals(getResources().getString(R.string.yes))) {
                complianceComments.setVisibility(View.VISIBLE);
            } else {
                complianceComments.setVisibility(View.GONE);
            }
        }else if (radioGroup == adverseDrugEffect.getRadioGroup()) {
            adverseDrugEffect.getQuestionView().setError(null);
            if (App.get(adverseDrugEffect).equals(getResources().getString(R.string.yes))) {
                drugSideEffectDetails.setVisibility(View.VISIBLE);
            } else {
                drugSideEffectDetails.setVisibility(View.GONE);
            }
        }else if (radioGroup == reportComorbidity.getRadioGroup()) {
            reportComorbidity.getQuestionView().setError(null);
            if (App.get(reportComorbidity).equals(getResources().getString(R.string.yes))) {
                comorbidConditionDetail.setVisibility(View.VISIBLE);
            } else {
                comorbidConditionDetail.setVisibility(View.GONE);
            }
        }else if (radioGroup == pcmPatient.getRadioGroup()) {
            pcmPatient.getQuestionView().setError(null);
        }
        else if (radioGroup == tbInfectionType.getRadioGroup()) {
            tbInfectionType.getQuestionView().setError(null);
        }else if (radioGroup == weekMonthDuration.getRadioGroup()) {
            weekMonthDuration.getQuestionView().setError(null);
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

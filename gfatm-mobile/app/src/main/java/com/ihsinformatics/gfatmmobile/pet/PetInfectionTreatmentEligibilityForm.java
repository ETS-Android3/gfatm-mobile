package com.ihsinformatics.gfatmmobile.pet;

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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetInfectionTreatmentEligibilityForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledRadioGroup pregnancyHistory;
    TitledRadioGroup pregnancyTestResult;
    TitledCheckBoxes evaluationType;
    TitledRadioGroup tbDiagnosis;
    TitledRadioGroup infectionType;
    TitledRadioGroup tbReferral;
    TitledSpinner referralSite;
    TitledEditText othersSite;
    TitledRadioGroup tbRuledOut;
    TitledEditText petEligiable;
    TitledRadioGroup treatmentInitiated;
    TitledEditText reasonTreatmentNotInitiated;
    TitledRadioGroup petConsent;
    TitledEditText clincianNote;

    ScrollView scrollView;

    Boolean refillFlag = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.PET_INFECTION_TREATMENT_ELIGIBILITY;
        form = Forms.pet_infectionTreatmenEligibility;

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
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView = new ScrollView(mainContent.getContext());
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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        String columnName = "pet_location";

        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }
        locationArray[locations.length] = "OTHER";

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_symptom_screen), App.VERTICAL);
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_pregnancy_history), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"PREGNANCY STATUS",new String[]{ "YES" , "NO"});
        pregnancyTestResult = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_pregnancy_test_result), getResources().getStringArray(R.array.pet_pregnancy_test_results), getResources().getString(R.string.pet_positive), App.VERTICAL, App.VERTICAL,true,"PREGNANCY TEST RESULT",new String[]{"POSITIVE" , "NEGATIVE" , "PREGNANCY TEST UNDETERMINENT"});
        evaluationType = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_evaluation_type), getResources().getStringArray(R.array.pet_evaluation_types), null, App.VERTICAL, App.VERTICAL, true,"TB EVALUATION TYPE",new String[]{ "EVIDENCE BASED EVALUATION" ,  "CLINICAL EVALUATION"});
        tbDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_diagnosed), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL,true,"TUBERCULOSIS DIAGNOSED",getResources().getStringArray(R.array.yes_no_list_concept));
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_type_of_tb), getResources().getStringArray(R.array.pet_infection_types), getResources().getString(R.string.pet_dstb), App.HORIZONTAL, App.VERTICAL,true,"TUBERCULOSIS INFECTION TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION", "DRUG-RESISTANT TB"});
        tbReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_patient_referred_fot_tb_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true,"PATIENT REFERRED",getResources().getStringArray(R.array.yes_no_list_concept));
        referralSite = new TitledSpinner(context, null, getResources().getString(R.string.pet_referral_site), locationArray, "", App.VERTICAL);
        othersSite = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        tbRuledOut = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_ruled_out), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true,"TB RULED OUT",getResources().getStringArray(R.array.yes_no_list_concept));
        petEligiable = new TitledEditText(context, null, getResources().getString(R.string.pet_eligible), getResources().getString(R.string.yes), "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        petEligiable.getEditText().setKeyListener(null);
        treatmentInitiated = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_initiating_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL,true,"TREATMENT INITIATED",getResources().getStringArray(R.array.yes_no_list_concept));
        reasonTreatmentNotInitiated = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_not_initiating_treatment), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"TREATMENT NOT INITIATED OTHER REASON");
        petConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_consent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, false,"PET CONSENT",getResources().getStringArray(R.array.yes_no_list_concept));
        clincianNote = new TitledEditText(context, null, getResources().getString(R.string.pet_doctor_notes), "", "", 250, RegexUtil.OTHER_WITH_NEWLINE_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "CLINICIAN NOTES (TEXT)");
        clincianNote.getEditText().setSingleLine(false);
        clincianNote.getEditText().setMinimumHeight(150);

        linearLayout1.addView(pregnancyHistory);
        linearLayout1.addView(pregnancyTestResult);
        linearLayout1.addView(evaluationType);
        linearLayout1.addView(tbDiagnosis);
        linearLayout1.addView(infectionType);
        linearLayout1.addView(tbReferral);
        linearLayout1.addView(referralSite);
        linearLayout1.addView(othersSite);
        linearLayout1.addView(tbRuledOut);
        linearLayout1.addView(petEligiable);
        linearLayout1.addView(treatmentInitiated);
        linearLayout1.addView(reasonTreatmentNotInitiated);
        linearLayout1.addView(petConsent);
        linearLayout1.addView(clincianNote);

        views = new View[]{formDate.getButton(), pregnancyHistory.getRadioGroup(), pregnancyTestResult.getRadioGroup(), evaluationType, tbDiagnosis.getRadioGroup(),
                infectionType.getRadioGroup(), tbReferral.getRadioGroup(), referralSite.getSpinner(), othersSite.getEditText(), tbRuledOut.getRadioGroup(),
                petEligiable.getEditText(), petConsent.getRadioGroup(), clincianNote.getEditText(), treatmentInitiated.getRadioGroup(), reasonTreatmentNotInitiated.getEditText()};

        viewGroups = new View[][]{{formDate, linearLayout1}};

        View listenerViewer[] = new View[]{formDate, pregnancyHistory, tbReferral, referralSite, tbRuledOut, tbDiagnosis, treatmentInitiated};
        for (View v : listenerViewer) {

            if (v instanceof TitledButton)
                ((TitledButton) v).getButton().setOnClickListener(this);
            else if (v instanceof TitledRadioGroup)
                ((TitledRadioGroup) v).getRadioGroup().setOnCheckedChangeListener(this);
            else if (v instanceof TitledSpinner)
                ((TitledSpinner) v).getSpinner().setOnItemSelectedListener(this);

        }

        for (CheckBox cb : evaluationType.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        resetViews();

    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);

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
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        pregnancyTestResult.setVisibility(View.GONE);
        othersSite.setVisibility(View.GONE);
        infectionType.setVisibility(View.GONE);
        tbReferral.setVisibility(View.GONE);
        referralSite.setVisibility(View.GONE);
        othersSite.setVisibility(View.GONE);
        tbRuledOut.setVisibility(View.VISIBLE);
        treatmentInitiated.setVisibility(View.VISIBLE);

        String s = App.getPatient().getPerson().getPersonAttribute("Marital Status");

        if (App.getPatient().getPerson().getAge() > 14 && !(s.equalsIgnoreCase("Single")))
            pregnancyHistory.setVisibility(View.VISIBLE);
        else
            pregnancyHistory.setVisibility(View.GONE);



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

    }

    @Override
    public boolean validate() {

        Boolean error = super.validate();
        View view = null;



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
                                        othersSite.getEditText().clearFocus();
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

        if (referralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referralSite).equals(getResources().getString(R.string.pet_other)) ? App.get(othersSite) : App.get(referralSite)});

        if (petEligiable.getVisibility() == View.VISIBLE)
            observations.add(new String[]{ "PET ELIGIBLE", App.get(petEligiable).toUpperCase()});



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

                if (App.get(petEligiable).equals(getResources().getString(R.string.yes))) {
                    result = serverService.saveProgramEnrollement(App.getSqlDate(formDateCalendar), "PET", id);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

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
            showDateDialog(formDateCalendar,false,true, false);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == referralSite.getSpinner()) {
            if (App.get(referralSite).equalsIgnoreCase(getResources().getString(R.string.pet_other)))
                othersSite.setVisibility(View.VISIBLE);
            else
                othersSite.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Boolean selected = false;
        for (CheckBox cb : evaluationType.getCheckedBoxes()) {
            if (cb.isChecked()) {
                selected = true;
                break;
            }
        }

        evaluationType.getQuestionView().setError(null);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == pregnancyHistory.getRadioGroup()) {
            if (App.get(pregnancyHistory).equals(getResources().getString(R.string.yes)))
                pregnancyTestResult.setVisibility(View.VISIBLE);
            else
                pregnancyTestResult.setVisibility(View.GONE);
        }
        if (group == tbRuledOut.getRadioGroup()) {
            if (App.get(tbRuledOut).equals(getResources().getString(R.string.yes))) {
                petEligiable.getEditText().setText(getResources().getString(R.string.yes));
                petConsent.setVisibility(View.VISIBLE);
                treatmentInitiated.setVisibility(View.VISIBLE);
                if(App.get(treatmentInitiated).equals(getString(R.string.yes)))
                    reasonTreatmentNotInitiated.setVisibility(View.GONE);
                else
                    reasonTreatmentNotInitiated.setVisibility(View.VISIBLE);
            }
            else {
                petEligiable.getEditText().setText(getResources().getString(R.string.no));
                treatmentInitiated.setVisibility(View.GONE);
                reasonTreatmentNotInitiated.setVisibility(View.GONE);
                petConsent.setVisibility(View.GONE);
            }
        }
        if (group == tbDiagnosis.getRadioGroup()) {
            if (App.get(tbDiagnosis).equals(getResources().getString(R.string.yes))) {
                infectionType.setVisibility(View.VISIBLE);
                tbReferral.setVisibility(View.VISIBLE);
                if (App.get(tbReferral).equals(getResources().getString(R.string.yes)))
                    referralSite.setVisibility(View.VISIBLE);
                else
                    referralSite.setVisibility(View.GONE);
                tbRuledOut.setVisibility(View.GONE);
                petEligiable.getEditText().setText(getResources().getString(R.string.no));
                treatmentInitiated.setVisibility(View.GONE);
                reasonTreatmentNotInitiated.setVisibility(View.GONE);
                if (App.get(referralSite).equals(getResources().getString(R.string.pet_other)))
                    othersSite.setVisibility(View.VISIBLE);
                else
                    othersSite.setVisibility(View.GONE);
            } else {
                infectionType.setVisibility(View.GONE);
                tbReferral.setVisibility(View.GONE);
                referralSite.setVisibility(View.GONE);
                othersSite.setVisibility(View.GONE);
                tbRuledOut.setVisibility(View.VISIBLE);

                if (App.get(tbRuledOut).equals(getResources().getString(R.string.yes))) {
                    petEligiable.getEditText().setText(getResources().getString(R.string.yes));
                    treatmentInitiated.setVisibility(View.VISIBLE);
                    if(App.get(treatmentInitiated).equals(getString(R.string.yes)))
                        reasonTreatmentNotInitiated.setVisibility(View.GONE);
                    else
                        reasonTreatmentNotInitiated.setVisibility(View.VISIBLE);
                }
                else {
                    petEligiable.getEditText().setText(getResources().getString(R.string.no));
                    treatmentInitiated.setVisibility(View.GONE);
                    reasonTreatmentNotInitiated.setVisibility(View.GONE);
                }
            }

            if (App.get(petEligiable).equals(getResources().getString(R.string.yes))) {
                petConsent.setVisibility(View.VISIBLE);
            }
            else {
                petConsent.setVisibility(View.GONE);
            }

        } else if (group == tbReferral.getRadioGroup()) {
            if (App.get(tbReferral).equals(getResources().getString(R.string.yes))) {
                referralSite.setVisibility(View.VISIBLE);
                if (App.get(referralSite).equalsIgnoreCase(getResources().getString(R.string.pet_other)))
                    othersSite.setVisibility(View.VISIBLE);
                else
                    othersSite.setVisibility(View.GONE);
            } else {
                referralSite.setVisibility(View.GONE);
                othersSite.setVisibility(View.GONE);
            }
        } else if (group == treatmentInitiated.getRadioGroup()) {
            if (App.get(treatmentInitiated).equals(getResources().getString(R.string.yes)))
                reasonTreatmentNotInitiated.setVisibility(View.GONE);
            else
                reasonTreatmentNotInitiated.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void refill(int encounterId) {
        super.refill(encounterId);
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

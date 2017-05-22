package com.ihsinformatics.gfatmmobile.pmdt;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tahira on 3/1/2017.
 */

public class PmdtPatientDissociationForm extends AbstractFormActivity {

    Context context;
    TitledButton formDate;
    TitledEditText treatmentSupporterId;
    TitledEditText treatmentSupporterFirstName;
    TitledEditText treatmentSupporterLastName;

    TitledSpinner reasonDissociation;
    TitledEditText otherReasonDissociation;

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
        FORM_NAME = Forms.PMDT_PATIENT_DISSOCIATION;
        FORM = Forms.pmdtPatientDissociation;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        treatmentSupporterId = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_id), "", "", 10, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_first_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterFirstName.setFocusableInTouchMode(true);
        treatmentSupporterLastName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_treatment_supporter_last_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        treatmentSupporterLastName.setFocusableInTouchMode(true);
        reasonDissociation = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_reason_dissociation), getResources().getStringArray(R.array.pmdt_reasons_dissociation), getResources().getString(R.string.pmdt_patient_not_satisfied), App.VERTICAL);
        otherReasonDissociation = new TitledEditText(context, null, getResources().getString(R.string.pmdt_reason_dissociation_other), "", "", 255, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        views = new View[]{formDate.getButton(), treatmentSupporterId.getEditText(), treatmentSupporterFirstName.getEditText(), treatmentSupporterLastName.getEditText(),
                reasonDissociation.getSpinner(), otherReasonDissociation.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentSupporterId, treatmentSupporterFirstName, treatmentSupporterLastName,
                        reasonDissociation, otherReasonDissociation}};

        formDate.getButton().setOnClickListener(this);
        reasonDissociation.getSpinner().setOnItemSelectedListener(this);
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

        if (otherReasonDissociation.getVisibility() == View.VISIBLE && App.get(otherReasonDissociation).isEmpty()) {
            otherReasonDissociation.getEditText().setError(getResources().getString(R.string.mandatory_field));
            otherReasonDissociation.getEditText().requestFocus();
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

            observations.add(new String[]{"DISSOCIATION REASON", App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_patient_not_satisfied)) ? "PATIENT NOT SATISFIED WITH TREATMENT SUPPORTER" :
                    (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_supervisor_not_satisfied)) ? "SUPERVISOR NOT SATISFIED WITH TREATMENT SUPPORTER" :
                            (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_treatment_outcome_declared)) ? "TREATMENT OUTCOME DECLARED" :
                                    (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_treatment_supporter_unwilling_patient)) ? "TREATMENT SUPPORTER NOT SUPPORTING PATIENT" :
                                            (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_treatment_supporter_relocated)) ? "RELOCATED TREATMENT SUPPORTER" :
                                                    (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_patient_relocated)) ? "RELOCATED PATIENT" :
                                                            (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_treatment_supporter_shifted)) ? "TREATMENT SUPPORTER PROGRAM LOCATION CHANGED" :
                                                                    (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_patient_transferred)) ? "PATIENT PROGRAM LOCATION CHANGED" :
                                                                            (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_treatment_supporter_unwilling_program)) ? "TREATMENT SUPPORTER NOT SUPPORTING PROGRAM" :
                                                                                    (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_treatment_supporter_leave_program)) ? "TREATMENT SUPPORTER LEFT PROGRAM" : "OTHER DISSOCIATION REASON")))))))))});


        if (otherReasonDissociation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER DISSOCIATION REASON", App.get(otherReasonDissociation)});


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

                    //setting person attribute "Treatment Supporter" to empty, indicating patient is no more assigned to any treatment supporter
                    //TODO: issue in offline mode, person attribute gets removed from the local db but not from the live server. on refilling form,
                    // it does not fill the form and pops up error, please fill patient assignment first
                    result = serverService.savePersonAttributeType("Treatment Supporter", "", encounterId);
                    if (!result.equals("SUCCESS"))
                        return result;
                }

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
    public void refill(int encounterId) {

        OfflineForm offlineForm = serverService.getOfflineFormById(encounterId);
        String date = offlineForm.getFormDate();
        ArrayList<String[][]> obsValue = offlineForm.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("TREATMENT SUPPORTER ID")) {
                treatmentSupporterId.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER FIRST NAME")) {
                treatmentSupporterFirstName.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TREATMENT SUPPORTER LAST NAME")) {
                treatmentSupporterFirstName.getEditText().setText(obs[0][1]);
            }  else if (obs[0][0].equals("DISSOCIATION REASON")) {
                String value = obs[0][1].equals("PATIENT NOT SATISFIED WITH TREATMENT SUPPORTER") ? getResources().getString(R.string.pmdt_patient_not_satisfied) :
                        (obs[0][1].equals("SUPERVISOR NOT SATISFIED WITH TREATMENT SUPPORTER") ? getResources().getString(R.string.pmdt_supervisor_not_satisfied) :
                                (obs[0][1].equals("TREATMENT OUTCOME DECLARED") ? getResources().getString(R.string.pmdt_treatment_outcome_declared) :
                                        (obs[0][1].equals("TREATMENT SUPPORTER NOT SUPPORTING PATIENT") ? getResources().getString(R.string.pmdt_treatment_supporter_unwilling_patient) :
                                                (obs[0][1].equals("RELOCATED TREATMENT SUPPORTER") ? getResources().getString(R.string.pmdt_treatment_supporter_relocated) :
                                                        (obs[0][1].equals("RELOCATED PATIENT") ? getResources().getString(R.string.pmdt_patient_relocated) :
                                                                (obs[0][1].equals("TREATMENT SUPPORTER PROGRAM LOCATION CHANGED") ? getResources().getString(R.string.pmdt_treatment_supporter_shifted) :
                                                                        (obs[0][1].equals("PATIENT PROGRAM LOCATION CHANGED") ? getResources().getString(R.string.pmdt_patient_transferred) :
                                                                                (obs[0][1].equals("TREATMENT SUPPORTER NOT SUPPORTING PROGRAM") ? getResources().getString(R.string.pmdt_treatment_supporter_unwilling_program) :
                                                                                        (obs[0][1].equals("TREATMENT SUPPORTER LEFT PROGRAM") ? getResources().getString(R.string.pmdt_treatment_supporter_leave_program) : getResources().getString(R.string.pmdt_other))))))))));

                reasonDissociation.getSpinner().selectValue(value);

            } else if (obs[0][0].equals("OTHER DISSOCIATION REASON")) {
                otherReasonDissociation.getEditText().setText(obs[0][1]);
                otherReasonDissociation.setVisibility(View.VISIBLE);
            }
        }
        submitButton.setEnabled(true);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonDissociation.getSpinner()) {
            if (App.get(reasonDissociation).equals(getResources().getString(R.string.pmdt_other)))
                otherReasonDissociation.setVisibility(View.VISIBLE);
            else
                otherReasonDissociation.setVisibility(View.GONE);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        submitButton.setEnabled(false);

        String assignedTreatmentSupporter = App.getPatient().getPerson().getTreatmentSupporter();

        if (assignedTreatmentSupporter.isEmpty()) {
            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.pmdt_treatment_supporter_not_assigned));
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

                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

            submitButton.setEnabled(false);
            return;

        } else {

            submitButton.setEnabled(true);

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
                    String treatmentSupporterId = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER ID");
                    String treatmentSupporterFirstName = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER FIRST NAME");
                    String treatmentSupporterLastName = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_PATIENT_ASSIGNMENT, "TREATMENT SUPPORTER LAST NAME");

                    if (treatmentSupporterId != null && !treatmentSupporterId.isEmpty())
                        result.put("TREATMENT SUPPORTER ID", treatmentSupporterId);
                    if (treatmentSupporterFirstName != null && !treatmentSupporterFirstName.isEmpty())
                        result.put("TREATMENT SUPPORTER FIRST NAME", treatmentSupporterFirstName);
                    if (treatmentSupporterLastName != null && !treatmentSupporterLastName.isEmpty())
                        result.put("TREATMENT SUPPORTER LAST NAME", treatmentSupporterLastName);

                    return result;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                @Override
                protected void onPostExecute(HashMap<String, String> result) {
                    super.onPostExecute(result);
                    loading.dismiss();

                    if (result.get("TREATMENT SUPPORTER ID") != null || !result.get("TREATMENT SUPPORTER ID").isEmpty())
                        treatmentSupporterId.getEditText().setText(result.get("TREATMENT SUPPORTER ID"));

                    if (result.get("TREATMENT SUPPORTER FIRST NAME") != null || !result.get("TREATMENT SUPPORTER FIRST NAME").isEmpty())
                        treatmentSupporterFirstName.getEditText().setText(result.get("TREATMENT SUPPORTER FIRST NAME"));

                    if (result.get("TREATMENT SUPPORTER LAST NAME") != null || !result.get("TREATMENT SUPPORTER LAST NAME").isEmpty())
                        treatmentSupporterLastName.getEditText().setText(result.get("TREATMENT SUPPORTER LAST NAME"));

                }
            };
            autopopulateFormTask.execute("");

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

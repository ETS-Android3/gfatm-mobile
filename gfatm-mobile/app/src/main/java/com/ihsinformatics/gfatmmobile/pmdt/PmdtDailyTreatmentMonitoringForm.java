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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tahira on 3/1/2017.
 */

public class PmdtDailyTreatmentMonitoringForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText treatmentDay;
    TitledRadioGroup treatmentDayType;
    TitledRadioGroup patientAdverseEvent;
    // Extra Views for date (to fetch treatment start date)...
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String treatmentStartDateString;

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
        FORM_NAME = Forms.PMDT_DAILY_TREATMENT_MONITORING;
        FORM = Forms.pmdtDailyTreatmentMonitoring;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_reporting_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        treatmentDay = new TitledEditText(context, null, getResources().getString(R.string.pmdt_day_of_treatment), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
//        treatmentDay.setFocusableInTouchMode(true);
        treatmentDayType = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_treatment_day_type), getResources().getStringArray(R.array.pmdt_types_treatment_day), getResources().getString(R.string.pmdt_fully_observed_day), App.VERTICAL, App.VERTICAL);
        patientAdverseEvent = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_patient_adverse_event), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);

        views = new View[]{formDate.getButton(), treatmentDay.getEditText(), treatmentDayType.getRadioGroup(),
                patientAdverseEvent.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, treatmentDay, treatmentDayType, patientAdverseEvent}};

        formDate.getButton().setOnClickListener(this);
        treatmentDayType.getRadioGroup().setOnCheckedChangeListener(this);
        patientAdverseEvent.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentDay.getEditText().setKeyListener(null);
        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

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

        View view = null;
        Boolean error = false;

        if (App.get(treatmentDay).isEmpty()) {
            treatmentDay.getEditText().setError(getResources().getString(R.string.mandatory_field));
            treatmentDay.getEditText().requestFocus();
            gotoLastPage();
            error = true;
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
        observations.add(new String[]{"DAY OF TREATMENT", App.get(treatmentDay)});
        observations.add(new String[]{"DAY OF TREATMENT ADHERENCE", App.get(treatmentDayType).equals(getResources().getString(R.string.pmdt_fully_observed_day)) ? "FULLY OBSERVED DAY" :
                (App.get(treatmentDayType).equals(getResources().getString(R.string.pmdt_missed_prescribed_day)) ? "MISSED PRESCRIBED DAY" : "INCOMPLETE PRESCRIBED DAY")});
        observations.add(new String[]{"ADVERSE EVENT TREATMENT DAY", App.get(patientAdverseEvent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

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

                String result = "";
                result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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

        OfflineForm offlineForm = serverService.getOfflineFormById(encounterId);
        String date = offlineForm.getFormDate();
        ArrayList<String[][]> obsValue = offlineForm.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("DAY OF TREATMENT")) {
                treatmentDay.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("DAY OF TREATMENT ADHERENCE")) {
                for (RadioButton rb : treatmentDayType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pmdt_fully_observed_day)) && obs[0][1].equals("FULLY OBSERVED DAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_missed_prescribed_day)) && obs[0][1].equals("MISSED PRESCRIBED DAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.pmdt_incomplete_prescribed_day)) && obs[0][1].equals("INCOMPLETE PRESCRIBED DAY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("ADVERSE EVENT TREATMENT DAY")) {
                for (RadioButton rb : patientAdverseEvent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
        }
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
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int encounterId = Integer.valueOf(id);

                refill(encounterId);

            } else bundle.putBoolean("save", false);
        }

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
                String treatmentInitiationDate = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PMDT_TREATMENT_INITIATION, "TREATMENT START DATE");

                if (treatmentInitiationDate != null && !treatmentInitiationDate.isEmpty())
                    result.put("TREATMENT START DATE", treatmentInitiationDate);

                return result;
            }

            @Override
            protected void onProgressUpdate(String... values) {
            }


            @Override
            protected void onPostExecute(HashMap<String, String> result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result.get("TREATMENT START DATE") == null || result.get("TREATMENT START DATE").isEmpty()) {

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.pmdt_treatment_initiation_missing));
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

                    return;
                }

                if (result.get("TREATMENT START DATE") != null && !result.get("TREATMENT START DATE").isEmpty()) {
                    String format = "";
                    treatmentStartDateString = result.get("TREATMENT START DATE");
                    if (treatmentStartDateString.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }
                    secondDateCalendar.setTime(App.stringToDate(treatmentStartDateString, format));

                    // Auto-calculate day of treatment - date difference between reporting date and treatment start date
                    int daysDifference = 0;

                    Date treatmentStartDate = App.stringToDate(treatmentStartDateString, format);
                    Date reportingDate = App.stringToDate(DateFormat.format("yyyy-MM-dd", formDateCalendar).toString(), format);
                    long diff = treatmentStartDate.getTime() - reportingDate.getTime();
                    daysDifference = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                    treatmentDay.getEditText().setText(String.valueOf(Math.abs(daysDifference)));

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

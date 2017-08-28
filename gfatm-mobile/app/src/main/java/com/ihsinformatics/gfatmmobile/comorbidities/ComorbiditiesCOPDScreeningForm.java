package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by New User on 28-Jul-17.
 */

public class ComorbiditiesCOPDScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    MyTextView copdScreening;
    TitledSpinner breathlessWeeks;
    TitledSpinner productiveCough;
    TitledSpinner lessActivity;
    TitledSpinner smokedCigarettes;
    TitledSpinner ageRange;
    TitledEditText copdTotalScore;
    TitledRadioGroup referredToPulmonologist;

    /**
     * CHANGE PAGE_COUNT and FORM_NAME Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.COMORBIDITIES_COPD_SCREENING;
        FORM = Forms.comorbidities_copdScreeningForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesCOPDScreeningForm.MyAdapter());
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
        formDate.setTag("formDate");
        copdScreening = new MyTextView(context, getResources().getString(R.string.comorbidities_copd_screening_header));
        copdScreening.setTypeface(null, Typeface.BOLD);
        breathlessWeeks= new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_copd_breathless_week), getResources().getStringArray(R.array.comorbidities_copd_breathless_week_options), "", App.VERTICAL, true);
        productiveCough= new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_copd_productive_cough), getResources().getStringArray(R.array.comorbidities_copd_productive_cough_options), "", App.VERTICAL, true);
        lessActivity= new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_copd_less_activity), getResources().getStringArray(R.array.comorbidities_copd_less_activity_options), "", App.VERTICAL, true);
        smokedCigarettes= new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_copd_smoked_cigarettes), getResources().getStringArray(R.array.comorbidities_copd_smoked_cigarettes_options), "", App.VERTICAL, true);
        ageRange = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_copd_age_range), getResources().getStringArray(R.array.comorbidities_copd_age_range_options), "", App.VERTICAL, true);
        copdTotalScore= new TitledEditText(context, null, getResources().getString(R.string.comorbidities_copd_score), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        copdTotalScore.getEditText().setText(String.valueOf(getTotalCOPDScore()));
        copdTotalScore.getEditText().setFocusable(false);
        if(getTotalCOPDScore() >= 5) {
            copdTotalScore.setVisibility(View.VISIBLE);
        }
        else {
            copdTotalScore.setVisibility(View.GONE);
        }
        referredToPulmonologist = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_copd_referred_to_pulmonologist), getResources().getStringArray(R.array.comorbidities_yes_no), "", App.VERTICAL, App.VERTICAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), breathlessWeeks.getSpinner(), productiveCough.getSpinner(), lessActivity.getSpinner(), smokedCigarettes.getSpinner(), ageRange.getSpinner(), copdTotalScore.getEditText(), referredToPulmonologist.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, copdScreening, breathlessWeeks, productiveCough, lessActivity, smokedCigarettes, ageRange, copdTotalScore, referredToPulmonologist}};

        formDate.getButton().setOnClickListener(this);
        referredToPulmonologist.getRadioGroup().setOnCheckedChangeListener(this);
        breathlessWeeks.getSpinner().setOnItemSelectedListener(this);
        productiveCough.getSpinner().setOnItemSelectedListener(this);
        lessActivity.getSpinner().setOnItemSelectedListener(this);
        smokedCigarettes.getSpinner().setOnItemSelectedListener(this);
        ageRange.getSpinner().setOnItemSelectedListener(this);

        /*reasonForDiscontinuation.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                showOtherPreferredLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });*/

        resetViews();
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
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

        Boolean error = false;

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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

        observations.add(new String[]{"SHORTNESS OF BREATH DURING PAST 4 WEEKS", App.get(breathlessWeeks).equals(getResources().getString(R.string.comorbidities_copd_breathless_week_options_none)) ? "NONE OF THE TIME" :
                (App.get(breathlessWeeks).equals(getResources().getString(R.string.comorbidities_copd_breathless_week_options_little)) ? "A LITTLE OF THE TIME" :
                        (App.get(breathlessWeeks).equals(getResources().getString(R.string.comorbidities_copd_breathless_week_options_some)) ? "SOME OF THE TIME" :
                                (App.get(breathlessWeeks).equals(getResources().getString(R.string.comorbidities_copd_breathless_week_options_most)) ? "MOSTLY" : "ALWAYS")))});

        observations.add(new String[]{"PRODUCTIVE COUGH", App.get(productiveCough).equals(getResources().getString(R.string.comorbidities_copd_productive_cough_options_never)) ? "NEVER" :
                (App.get(productiveCough).equals(getResources().getString(R.string.comorbidities_copd_productive_cough_options_occasional)) ? "ONLY WITH OCCASIONAL COLDS OR CHEST INFECTIONS" :
                        (App.get(productiveCough).equals(getResources().getString(R.string.comorbidities_copd_productive_cough_options_few_days)) ? "YES, A FEW DAYS A MONTH" :
                                (App.get(productiveCough).equals(getResources().getString(R.string.comorbidities_copd_productive_cough_options_most_days)) ? "YES, MOST DAYS A WEEK" : "YES, EVERY DAY")))});

        observations.add(new String[]{"LESS ACTIVITY DUE TO BREATHING PROBLEM", App.get(lessActivity).equals(getResources().getString(R.string.comorbidities_copd_less_activity_options_strongly_disagree)) ? "STRONGLY DISAGREE" :
                (App.get(lessActivity).equals(getResources().getString(R.string.comorbidities_copd_less_activity_options_disagree)) ? "DISAGREE" :
                        (App.get(lessActivity).equals(getResources().getString(R.string.comorbidities_copd_less_activity_options_unsure)) ? "UNKNOWN" :
                                (App.get(lessActivity).equals(getResources().getString(R.string.comorbidities_copd_less_activity_options_agree)) ? "AGREE" : "STRONGLY AGREE")))});

        observations.add(new String[]{"SMOKED AT LEAST 100 CIGARETTES (ENTIRE LIFE)", App.get(smokedCigarettes).equals(getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_no)) ? "NO" :
                                (App.get(smokedCigarettes).equals(getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_yes)) ? "YES" : "UNKNOWN")});

        observations.add(new String[]{"RESPONDENT AGE", App.get(ageRange).equals(getResources().getString(R.string.comorbidities_copd_age_range_options_35)) ? "35-49" :
                        (App.get(ageRange).equals(getResources().getString(R.string.comorbidities_copd_age_range_options_50)) ? "50-49" :
                                (App.get(ageRange).equals(getResources().getString(R.string.comorbidities_copd_age_range_options_60)) ? "60-69" : "70+"))});


        observations.add(new String[]{"COPD SCORE", App.get(copdTotalScore)});

        observations.add(new String[]{"REFERRED TO PULMONOLOGIST", App.get(referredToPulmonologist).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

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
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;
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

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        //formValues.put(firstName.getTag(), App.get(firstName));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }
            else if (obs[0][0].equals("SHORTNESS OF BREATH DURING PAST 4 WEEKS")) {
                String value = obs[0][1].equals("NONE OF THE TIME") ? getResources().getString(R.string.comorbidities_copd_breathless_week_options_none) :
                        (obs[0][1].equals("A LITTLE OF THE TIME") ? getResources().getString(R.string.comorbidities_copd_breathless_week_options_little) :
                                (obs[0][1].equals("SOME OF THE TIME") ? getResources().getString(R.string.comorbidities_copd_breathless_week_options_some) :
                                        (obs[0][1].equals("MOSTLY") ? getResources().getString(R.string.comorbidities_copd_breathless_week_options_most) : getResources().getString(R.string.comorbidities_copd_breathless_week_options_all))));
                breathlessWeeks.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("PRODUCTIVE COUGH")) {
                String value = obs[0][1].equals("NEVER") ? getResources().getString(R.string.comorbidities_copd_productive_cough_options_never) :
                        (obs[0][1].equals("ONLY WITH OCCASIONAL COLDS OR CHEST INFECTIONS") ? getResources().getString(R.string.comorbidities_copd_productive_cough_options_occasional) :
                                (obs[0][1].equals("YES, A FEW DAYS A MONTH") ? getResources().getString(R.string.comorbidities_copd_productive_cough_options_few_days) :
                                        (obs[0][1].equals("YES, MOST DAYS A WEEK") ? getResources().getString(R.string.comorbidities_copd_productive_cough_options_most_days) : getResources().getString(R.string.comorbidities_copd_productive_cough_options_everyday))));
                productiveCough.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("LESS ACTIVITY DUE TO BREATHING PROBLEM")) {
                String value = obs[0][1].equals("STRONGLY DISAGREE") ? getResources().getString(R.string.comorbidities_copd_less_activity_options_strongly_disagree) :
                        (obs[0][1].equals("DISAGREE") ? getResources().getString(R.string.comorbidities_copd_less_activity_options_disagree) :
                                (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.comorbidities_copd_less_activity_options_unsure) :
                                        (obs[0][1].equals("AGREE") ? getResources().getString(R.string.comorbidities_copd_less_activity_options_agree) : getResources().getString(R.string.comorbidities_copd_less_activity_options_strongly_agree))));
                lessActivity.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("SMOKED AT LEAST 100 CIGARETTES (ENTIRE LIFE)")) {
                String value = obs[0][1].equals("NO") ? getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_no) :
                        (obs[0][1].equals("YES") ? getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_yes) : getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_unknown));
                smokedCigarettes.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("RESPONDENT AGE")) {
                String value = obs[0][1].equals("35-49") ? getResources().getString(R.string.comorbidities_copd_age_range_options_35) :
                        (obs[0][1].equals("50-49") ? getResources().getString(R.string.comorbidities_copd_age_range_options_50) :
                                        (obs[0][1].equals("60-69") ? getResources().getString(R.string.comorbidities_copd_age_range_options_60) : getResources().getString(R.string.comorbidities_copd_age_range_options_70)));
                ageRange.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("COPD SCORE")) {
                copdTotalScore.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("REFERRED TO PULMONOLOGIST")) {
                for (RadioButton rb : referredToPulmonologist.getRadioGroup().getButtons()) {
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
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
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
        if (spinner == breathlessWeeks.getSpinner()) {
            copdTotalScore.getEditText().setText(String.valueOf(getTotalCOPDScore()));
            if(getTotalCOPDScore() >= 5) {
                copdTotalScore.setVisibility(View.VISIBLE);
            }
            else {
                copdTotalScore.setVisibility(View.GONE);
            }
        }
        else if(spinner == productiveCough.getSpinner()) {
            copdTotalScore.getEditText().setText(String.valueOf(getTotalCOPDScore()));
            if(getTotalCOPDScore() >= 5) {
                copdTotalScore.setVisibility(View.VISIBLE);
            }
            else {
                copdTotalScore.setVisibility(View.GONE);
            }
        }
        else if(spinner == lessActivity.getSpinner()) {
            copdTotalScore.getEditText().setText(String.valueOf(getTotalCOPDScore()));
            if(getTotalCOPDScore() >= 5) {
                copdTotalScore.setVisibility(View.VISIBLE);
            }
            else {
                copdTotalScore.setVisibility(View.GONE);
            }
        }
        else if(spinner == smokedCigarettes.getSpinner()) {
            copdTotalScore.getEditText().setText(String.valueOf(getTotalCOPDScore()));
            if(getTotalCOPDScore() >= 5) {
                copdTotalScore.setVisibility(View.VISIBLE);
            }
            else {
                copdTotalScore.setVisibility(View.GONE);
            }
        }
        else if(spinner == ageRange.getSpinner()) {
            copdTotalScore.getEditText().setText(String.valueOf(getTotalCOPDScore()));
            if(getTotalCOPDScore() >= 5) {
                copdTotalScore.setVisibility(View.VISIBLE);
            }
            else {
                copdTotalScore.setVisibility(View.GONE);
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
        // mentalHealthNextScheduledVisit.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        Boolean flag = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    int getTotalCOPDScore() {
        ArrayList<String> selectedOptions = new ArrayList<String>();
        selectedOptions.add(App.get(breathlessWeeks));
        selectedOptions.add(App.get(productiveCough));
        selectedOptions.add(App.get(lessActivity));
        selectedOptions.add(App.get(smokedCigarettes));
        selectedOptions.add(App.get(ageRange));

        int breathLessNoneFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_breathless_week_options_none));
        int breathLessLittleFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_breathless_week_options_little));
        int productiveCoughNoFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_productive_cough_options_never));
        int productiveCoughOccasionFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_productive_cough_options_occasional));
        int lessActivityStronglyDisagreeFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_less_activity_options_strongly_disagree));
        int lessActivityDisagreeFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_less_activity_options_disagree));
        int lessActivityUnsureFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_less_activity_options_unsure));
        int smokedNoFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_no));
        int smokedUnknownFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_unknown));
        int ageRange35Freq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_age_range_options_35));

        int breathLessSomeFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_breathless_week_options_some));
        int productiveCoughFewFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_productive_cough_options_few_days));
        int productiveCoughMostFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_productive_cough_options_most_days));
        int lessActivityAgreeFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_less_activity_options_agree));
        int ageRange50Freq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_age_range_options_50));

        int breathLessMostFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_breathless_week_options_most));
        int breathLessAllFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_breathless_week_options_all));
        int productiveCoughEveryFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_productive_cough_options_everyday));
        int lessActivityStronglyAgreeFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_less_activity_options_strongly_agree));
        int smokedYesFreq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_smoked_cigarettes_options_yes));
        int ageRange60Freq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_age_range_options_60));
        int ageRange70Freq = Collections.frequency(selectedOptions, getResources().getString(R.string.comorbidities_copd_age_range_options_70));


        int neverTotal = 0 * (breathLessNoneFreq + breathLessLittleFreq + productiveCoughNoFreq + productiveCoughOccasionFreq + lessActivityStronglyDisagreeFreq
                + lessActivityDisagreeFreq + lessActivityUnsureFreq + smokedNoFreq + smokedUnknownFreq + ageRange35Freq);
        int sometimesTotal = breathLessSomeFreq + productiveCoughFewFreq + productiveCoughMostFreq + lessActivityAgreeFreq + ageRange50Freq;
        int mostTotal = 2 * (breathLessMostFreq + breathLessAllFreq + breathLessAllFreq + productiveCoughEveryFreq + lessActivityStronglyAgreeFreq + smokedYesFreq +  ageRange60Freq + ageRange70Freq);

        return neverTotal + sometimesTotal + mostTotal;
    }










    /*void showOtherPreferredLocation() {
        String text = reasonForDiscontinuation.getSpinner().getSelectedItem().toString();

        if (text.equalsIgnoreCase(getResources().getString(R.string.comorbidities_location_other))) {
            if(!akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
                otherPreferredLocation.setVisibility(View.VISIBLE);
                numberOfSessionsConducted.setVisibility(View.GONE);
            }
        } else {
            if(!akuadsSeverity.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_MH_severity_level_normal))) {
                otherPreferredLocation.setVisibility(View.GONE);
                numberOfSessionsConducted.setVisibility(View.VISIBLE);
            }
        }
    }*/

    /**
     * Goto view at given location in the pager
     */
    @Override
    protected void gotoPage(int pageNo) {

        pager.setCurrentItem(pageNo);
        navigationSeekbar.setProgress(pageNo);

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

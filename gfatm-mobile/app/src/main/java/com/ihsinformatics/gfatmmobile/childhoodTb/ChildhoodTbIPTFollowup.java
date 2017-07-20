package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbIPTFollowup extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {


    Context context;
    TitledButton formDate;
    TitledEditText fathersName;
    TitledEditText weightAtBaseline;
    TitledButton iptStartDate;
    TitledEditText dose;
    TitledEditText weightVisit;
    TitledEditText complaints;
    TitledRadioGroup iptDose;
    TitledRadioGroup iptCompliance;
    TitledRadioGroup iptOutcome;

    Snackbar snackbar;
    ScrollView scrollView;

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
        FORM_NAME = Forms.CHILDHOODTB_IPT_FOLLOWUP;
        FORM = Forms.childhoodTb_isoniazid_preventive_therapy_followup;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
        if (App.getPatient().getPerson().getAge() > 15) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.ctb_age_greater_than_15));
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
        }

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        fathersName = new TitledEditText(context, null, getResources().getString(R.string.ctb_father_name), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        weightAtBaseline = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_at_baseline), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        iptStartDate = new TitledButton(context, null,  getResources().getString(R.string.ctb_ipt_start_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        iptStartDate.setTag("iptStartDate");
        dose = new TitledEditText(context, null, getResources().getString(R.string.ctb_dose_at_intiation_point), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        weightVisit = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_visit), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        complaints = new TitledEditText(context, null, getResources().getString(R.string.ctb_complaints), "", "", 500, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        iptDose = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_dose), getResources().getStringArray(R.array.ctb_dose_list),getResources().getString(R.string.ctb_quater_per_day), App.VERTICAL, App.VERTICAL, true);
        iptCompliance = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ipt_compliance), getResources().getStringArray(R.array.yes_no_options), null, App.VERTICAL, App.VERTICAL);
        iptOutcome = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_ipt_outcome), getResources().getStringArray(R.array.ctb_ipt_outcome_list),getResources().getString(R.string.ctb_complete), App.VERTICAL, App.VERTICAL, true);

        views = new View[]{formDate.getButton(), fathersName.getEditText(),weightAtBaseline.getEditText(),iptStartDate.getButton(), iptDose.getRadioGroup(),iptCompliance.getRadioGroup(),iptOutcome.getRadioGroup(),
                dose.getEditText(),weightVisit.getEditText(),complaints.getEditText(),
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, fathersName,weightAtBaseline,iptStartDate,dose,weightVisit,complaints,iptDose,iptCompliance,iptOutcome
        }};

        formDate.getButton().setOnClickListener(this);
        iptStartDate.getButton().setOnClickListener(this);
        iptDose.getRadioGroup().setOnCheckedChangeListener(this);
        iptCompliance.getRadioGroup().setOnCheckedChangeListener(this);
        iptOutcome.getRadioGroup().setOnCheckedChangeListener(this);
        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();

            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
        if (!iptStartDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }


      }

    @Override
    public boolean validate() {
        if (App.getPatient().getPerson().getAge() > 15) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
            alertDialog.setMessage(getString(R.string.ctb_age_greater_than_15));
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
        }else {
            boolean error = false;
            if(App.get(complaints).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                complaints.getEditText().setError(getString(R.string.empty_field));
                complaints.getEditText().requestFocus();
                error = true;
            }
            if(!App.get(complaints).isEmpty()){
                if (App.get(complaints).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    complaints.getEditText().setError(getString(R.string.ctb_spaces_only));
                    complaints.getEditText().requestFocus();
                    error = true;
                }
            }
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
            }else {
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
        observations.add(new String[]{"IPT START DATE", App.getSqlDateTime(secondDateCalendar)});

        observations.add(new String[]{"DOSE AT TREATMENT INITIATION POINT", App.get(dose)});
        observations.add(new String[]{"WEIGHT (KG)", App.get(weightVisit)});
        observations.add(new String[]{"COMPLAINTS", App.get(complaints)});

        observations.add(new String[]{"IPT DOSE", App.get(iptDose).equals(getResources().getString(R.string.ctb_quater_per_day)) ? "1/4 TAB ONCE ADAY" :
                (App.get(iptDose).equals(getResources().getString(R.string.ctb_half_per_day)) ? "1/2 TAB ONCE A DAY" :
                        "1 TAB ONCE A DAY")});

        if(!App.get(iptCompliance).isEmpty()) {
            observations.add(new String[]{"IPT COMPLIANCE", App.get(iptCompliance).toUpperCase()});
        }
        observations.add(new String[]{"IPT OUTCOME", App.get(iptOutcome).equals(getResources().getString(R.string.ctb_complete)) ? "COMPLETED" :
                (App.get(iptOutcome).equals(getResources().getString(R.string.ctb_continuing)) ? "CONTINUING" :
                        "LOST TO FOLLOW-UP")});

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

                String result = serverService.saveEncounterAndObservation("IPT Followup", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
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

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
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
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }else if (obs[0][0].equals("IPT START DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
            } else if (obs[0][0].equals("DOSE AT TREATMENT INITIATION POINT")) {
                dose.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weightVisit.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COMPLAINTS")) {
                complaints.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("IPT DOSE")) {
                for (RadioButton rb : iptDose.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_quater_per_day)) && obs[0][1].equals("1/4 TAB ONCE ADAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_half_per_day)) && obs[0][1].equals("1/2 TAB ONCE A DAY")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_per_day)) && obs[0][1].equals("1 TAB ONCE A DAY")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }else if (obs[0][0].equals("IPT COMPLIANCE")) {
                for (RadioButton rb : iptCompliance.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            }
            else if (obs[0][0].equals("IPT OUTCOME")) {
                for (RadioButton rb : iptOutcome.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_complete)) && obs[0][1].equals("LOST TO FOLLOW-UP")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_continuing)) && obs[0][1].equals("CONTINUING")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_lost_to_followup)) && obs[0][1].equals("LOST TO FOLLOW-UP")) {
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
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
        if (view == iptStartDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
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

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        iptStartDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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
        String husbandNameString = App.getPatient().getPerson().getGuardianName();
        if(husbandNameString!=null) {
            fathersName.getEditText().setText(husbandNameString);
        }
        fathersName.getEditText().setKeyListener(null);
        String referralTransferLocation = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Presumptive Case Confirmation", "WEIGHT (KG)");
        if(referralTransferLocation!=null){
            String weight = referralTransferLocation.split("\\.")[0];
            weightAtBaseline.getEditText().setText(weight);

        }
        weightAtBaseline.getEditText().setKeyListener(null);


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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

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
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetInfectionTreatmentEligibilityForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;

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
    TitledRadioGroup petConsent;

    ScrollView scrollView;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.PET_INFECTION_TREATMENT_ELIGIBILITY;
        FORM = Forms.pet_infectionTreatmenEligibility;

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
                scrollView = new ScrollView(mainContent.getContext());
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

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(columnName);
        String[] locationArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }
        locationArray[locations.length] = "OTHER";

        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_symptom_screen), App.VERTICAL);
        pregnancyHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_pregnancy_history), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        pregnancyTestResult = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_pregnancy_test_result), getResources().getStringArray(R.array.pet_pregnancy_test_results), getResources().getString(R.string.pet_positive), App.VERTICAL, App.VERTICAL, true);
        evaluationType = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_evaluation_type), getResources().getStringArray(R.array.pet_evaluation_types), null, App.VERTICAL, App.VERTICAL, true);
        tbDiagnosis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_diagnosed), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_type_of_tb), getResources().getStringArray(R.array.pet_infection_types), getResources().getString(R.string.pet_dstb), App.HORIZONTAL, App.VERTICAL, true);
        tbReferral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_patient_referred_fot_tb_treatment), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        referralSite = new TitledSpinner(context, null, getResources().getString(R.string.pet_referral_site), locationArray, "", App.VERTICAL, true);
        othersSite = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        tbRuledOut = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_ruled_out), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        petEligiable = new TitledEditText(context, null, getResources().getString(R.string.pet_eligible), getResources().getString(R.string.no), "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        petEligiable.getEditText().setKeyListener(null);
        petConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_consent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

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
        linearLayout1.addView(petConsent);

        views = new View[]{formDate.getButton(), pregnancyHistory.getRadioGroup(), pregnancyTestResult.getRadioGroup(), evaluationType, tbDiagnosis.getRadioGroup(),
                infectionType.getRadioGroup(), tbReferral.getRadioGroup(), referralSite.getSpinner(), othersSite.getEditText(), tbRuledOut.getRadioGroup(), petEligiable.getEditText(), petConsent.getRadioGroup()};

        viewGroups = new View[][]{{formDate, linearLayout1}};

        View listenerViewer[] = new View[]{formDate, pregnancyHistory, referralSite, tbRuledOut, tbDiagnosis};
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
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        pregnancyTestResult.setVisibility(View.GONE);
        othersSite.setVisibility(View.GONE);
        infectionType.setVisibility(View.GONE);
        tbReferral.setVisibility(View.GONE);
        referralSite.setVisibility(View.GONE);
        othersSite.setVisibility(View.GONE);
        tbRuledOut.setVisibility(View.VISIBLE);

        String s = App.getPatient().getPerson().getMaritalStatus();


        if (App.getPatient().getPerson().getAge() < 14 || App.getPatient().getPerson().getMaritalStatus().equalsIgnoreCase("Single"))
            pregnancyHistory.setVisibility(View.GONE);
        else
            pregnancyHistory.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;

        if (othersSite.getVisibility() == View.VISIBLE) {
            if (App.get(othersSite).isEmpty()) {
                othersSite.getEditText().setError(getString(R.string.empty_field));
                othersSite.getEditText().requestFocus();
                error = true;
            }
        } else
            othersSite.clearFocus();

        Boolean flag = false;
        for (CheckBox cb : evaluationType.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            evaluationType.getQuestionView().setError(getString(R.string.empty_field));
            evaluationType.getQuestionView().requestFocus();
            view = evaluationType;
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
                                        othersSite.clearFocus();
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
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"PREGNANCY STATUS", App.get(pregnancyHistory).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"PREGNANCY TEST RESULT", App.get(pregnancyTestResult).equals(getResources().getString(R.string.pet_positive)) ? "POSITIVE" :
                (App.get(pregnancyTestResult).equals(getResources().getString(R.string.pet_negative)) ? "NEGATIVE" : "PREGNANCY TEST UNDETERMINENT")});
        String evaluationTypeString = "";
        for (CheckBox cb : evaluationType.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_evidence_based_evaluation)))
                evaluationTypeString = evaluationTypeString + "EVIDENCE BASED EVALUATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_clinical_evaluation)))
                evaluationTypeString = evaluationTypeString + "CLINICAL EVALUATION" + " ; ";
        }
        observations.add(new String[]{"TB EVALUATION TYPE", evaluationTypeString});
        observations.add(new String[]{"TUBERCULOSIS DIAGNOSED", App.get(tbDiagnosis).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (infectionType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(infectionType).equals(getResources().getString(R.string.pet_dstb)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" : "DRUG-RESISTANT TUBERCULOSIS INFECTION"});
        if (tbReferral.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT REFERRED", App.get(tbReferral).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (tbReferral.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT REFERRED", App.get(tbReferral).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (referralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRING FACILITY NAME", App.get(referralSite).equals(getResources().getString(R.string.pet_other)) ? App.get(othersSite) : App.get(referralSite)});
        if (tbRuledOut.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TB RULED OUT", App.get(tbRuledOut).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (petEligiable.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PET ELIGIBLE", App.get(petEligiable).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (petConsent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PET CONSENT", App.get(petConsent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});

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
            if (App.get(tbRuledOut).equals(getResources().getString(R.string.yes)))
                petEligiable.getEditText().setText(getResources().getString(R.string.yes));
            else
                petEligiable.getEditText().setText(getResources().getString(R.string.no));
        }
        if (group == tbDiagnosis.getRadioGroup()) {
            if (App.get(tbDiagnosis).equals(getResources().getString(R.string.yes))) {
                infectionType.setVisibility(View.VISIBLE);
                tbReferral.setVisibility(View.VISIBLE);
                referralSite.setVisibility(View.VISIBLE);
                tbRuledOut.setVisibility(View.GONE);
                petEligiable.getEditText().setText(getResources().getString(R.string.no));
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

                if (App.get(tbRuledOut).equals(getResources().getString(R.string.yes)))
                    petEligiable.getEditText().setText(getResources().getString(R.string.yes));
                else
                    petEligiable.getEditText().setText(getResources().getString(R.string.no));
            }
        }
        if (group == tbRuledOut.getRadioGroup()) {
            if (App.get(tbRuledOut).equals(getResources().getString(R.string.yes)))
                petEligiable.getEditText().setText(getResources().getString(R.string.yes));
            else
                petEligiable.getEditText().setText(getResources().getString(R.string.no));
        }
    }

    @Override
    public void refill(int encounterId) {
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

package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
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

public class ChildhoodTbUltrasoundTest extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup formType;
    TitledEditText testId;
    TitledRadioGroup pointTestBeingDone;
    TitledSpinner monthTreatment;
    TitledRadioGroup ultrasoundSite;
    TitledEditText otherUltrasoundSite;
    TitledSpinner ultrasoundResult;
    TitledEditText otherUltrasoundResult;
    TitledRadioGroup ultrasoundInterpretation;

    ImageView testIdView;
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
        FORM_NAME = Forms.CHILDHOODTB_ULTRASOUND_TEST;
        FORM = Forms.childhoodTb_ultrasound_order_and_result;

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


        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        testId = new TitledEditText(context,null,getResources().getString(R.string.ctb_test_id),"","",20,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,true);
        formType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_type_of_form),getResources().getStringArray(R.array.ctb_type_of_form_list),null,App.HORIZONTAL,App.VERTICAL,true);
        pointTestBeingDone = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_point_test_being_done),getResources().getStringArray(R.array.ctb_ultrasound_test_point_list),getResources().getString(R.string.ctb_diagnostic),App.VERTICAL,App.VERTICAL,true);
        monthTreatment= new TitledSpinner(context,null,getResources().getString(R.string.ctb_month_treatment),getResources().getStringArray(R.array.ctb_0_to_24),null,App.HORIZONTAL);
        updateFollowUpMonth();
        ultrasoundSite = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_site_ultrasound),getResources().getStringArray(R.array.ctb_site_of_ultrasound_list),getResources().getString(R.string.ctb_diagnostic),App.VERTICAL,App.VERTICAL,true);
        otherUltrasoundSite = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        ultrasoundResult = new TitledSpinner(context,null,getResources().getString(R.string.ctb_ultrasound_result),getResources().getStringArray(R.array.ctb_ultrasound_result_list),null,App.VERTICAL);
        otherUltrasoundResult = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        ultrasoundInterpretation = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_ultrasound_interpretation),getResources().getStringArray(R.array.ctb_ultrasound_interpretation_list),getResources().getString(R.string.ctb_diagnostic),App.VERTICAL,App.VERTICAL,true);
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        testId.setLayoutParams(param);
        linearLayout.addView(testId);
        testIdView = new ImageView(context);
        testIdView.setImageResource(R.drawable.ic_checked);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        testIdView.setLayoutParams(param1);
        testIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        testIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(testIdView);


        views = new View[]{formDate.getButton(),formType.getRadioGroup(),pointTestBeingDone.getRadioGroup(),ultrasoundSite.getRadioGroup(),ultrasoundResult.getSpinner(),ultrasoundInterpretation.getRadioGroup(),
                testId.getEditText(),monthTreatment.getSpinner(),otherUltrasoundSite.getEditText(),otherUltrasoundResult
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType,linearLayout,formDate,pointTestBeingDone,monthTreatment,ultrasoundSite,otherUltrasoundSite,ultrasoundResult,otherUltrasoundResult,ultrasoundInterpretation}};

        formDate.getButton().setOnClickListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        pointTestBeingDone.getRadioGroup().setOnCheckedChangeListener(this);
        ultrasoundSite.getRadioGroup().setOnCheckedChangeListener(this);
        monthTreatment.getSpinner().setOnItemSelectedListener(this);
        ultrasoundResult.getSpinner().setOnItemSelectedListener(this);
        ultrasoundInterpretation.getRadioGroup().setOnCheckedChangeListener(this);
        testId.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                try {
                    if (testId.getEditText().getText().length() > 0) {
                        testIdView.setVisibility(View.VISIBLE);
                        testIdView.setImageResource(R.drawable.ic_checked);
                    } else {
                        testIdView.setVisibility(View.INVISIBLE);
                    }
                    goneVisibility();
                    submitButton.setEnabled(false);


                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });
        testIdView.setOnTouchListener(this);
        resetViews();

    }

    public void updateFollowUpMonth(){
        String treatmentDate = serverService.getObsValue(App.getPatientId(), App.getProgram() + "-" + "Treatment Initiation", "REGISTRATION DATE");
        String format = "";


        if (treatmentDate.contains("/")) {
            format = "dd/MM/yyyy";
        } else {
            format = "yyyy-MM-dd";
        }
        Date convertedDate = App.stringToDate(treatmentDate, format);
        Calendar treatmentDateCalender = App.getCalendar(convertedDate);
        int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);

        String [] monthArray = new String[diffMonth + 1];

        for(int i =0 ; i <= diffMonth ; i++){
            monthArray[i] = String.valueOf(i);
        }

        monthTreatment.getSpinner().setSpinnerData(monthArray);
    }



    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


            Date date = new Date();
        if(formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_result))){
            Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Childhood TB-Ultrasound Test Order");
            String format = "";
            String formDa = formDate.getButton().getText().toString();

            for(int i =0 ; i < testIds.length ; i++){
                if(testIds[i][0].equals(testId.getEditText().getText().toString())){
                    String orderdate = testIds[i][1].toString();
                    if (orderdate.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(orderdate, format);

                    if(formDateCalendar.before(App.getCalendar(orderDate))){
                        formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        break;
                    }
                    else {
                        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                    }
                }
            }
        }
         if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
             String personDOB = App.getPatient().getPerson().getBirthdate();

            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            }
            else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }

        updateFollowUpMonth();
        formDate.getButton().setEnabled(true);
       }

    @Override
    public boolean validate() {
        boolean error=false;
        if (App.get(testId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            testId.getEditText().setError(getString(R.string.empty_field));
            testId.getEditText().requestFocus();
            error = true;
        }
        else{
            testId.getEditText().setError(null);
        }
        if (App.get(formType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            formType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            formType.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            formType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (otherUltrasoundSite.getVisibility() == View.VISIBLE) {
            if(App.get(otherUltrasoundSite).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherUltrasoundSite.getEditText().setError(getString(R.string.empty_field));
                otherUltrasoundSite.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(otherUltrasoundSite).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherUltrasoundSite.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherUltrasoundSite.getEditText().requestFocus();
                error = true;
            }
        }
        if(otherUltrasoundResult.getVisibility()==View.VISIBLE){
            if(App.get(otherUltrasoundResult).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherUltrasoundResult.getEditText().setError(getString(R.string.empty_field));
                otherUltrasoundResult.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(otherUltrasoundResult).trim().length() <= 0){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherUltrasoundResult.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherUltrasoundResult.getEditText().requestFocus();
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
        if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
            observations.add(new String[]{"TEST ID", App.get(testId)});
            observations.add(new String[]{"TEST CONTEXT STATUS", App.get(pointTestBeingDone).equals(getResources().getString(R.string.ctb_diagnostic)) ? "DIAGNOSTIC TESTING" :
                            "REGULAR FOLLOW UP"});
            if(monthTreatment.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"FOLLOW-UP MONTH", App.get(monthTreatment)});
            }
            observations.add(new String[]{"ULTRASOUND SITE", App.get(ultrasoundSite).equals(getResources().getString(R.string.ctb_abdomen)) ? "ULTRASOUND, ABDOMEN" :
                    "ULTRASOUND SITE OTHER"});

            if(otherUltrasoundSite.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"ULTRASOUND SITE OTHER", App.get(otherUltrasoundSite)});
            }

        }else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
            observations.add(new String[]{"TEST ID", App.get(testId)});
            observations.add(new String[]{"ULTRASOUND RESULT", App.get(ultrasoundResult).equals(getResources().getString(R.string.ctb_abdomen_adenopathy)) ? "ABDOMEN ADENOPATHY" :
                    (App.get(ultrasoundResult).equals(getResources().getString(R.string.ctb_intestinal_wall_thickening)) ? "INTESTINAL WALL THICKENING" :
                            (App.get(ultrasoundResult).equals(getResources().getString(R.string.ctb_ascites)) ? "ASCITES" :
                                    (App.get(ultrasoundResult).equals(getResources().getString(R.string.ctb_pleural_effusion)) ? "PLEURAL EFFUSION" :
                                            (App.get(ultrasoundResult).equals(getResources().getString(R.string.ctb_matted_lymph_nodes)) ? "MATTED LYMPH NODES" :
                                                    (App.get(ultrasoundResult).equals(getResources().getString(R.string.ctb_normal)) ? "NORMAL" :
                                                            "OTHER ULTRASOUND RESULT")))))});

            if(otherUltrasoundResult.getVisibility()==View.VISIBLE){
                observations.add(new String[]{"OTHER ULTRASOUND RESULT", App.get(otherUltrasoundResult)});
            }

            observations.add(new String[]{"ULTRASOUND INTERPRETATION", App.get(ultrasoundInterpretation).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                    (App.get(ultrasoundInterpretation).equals(getResources().getString(R.string.ctb_normal)) ? "NORMAL" :
                            "OTHER ULTRASOUND RESULT")});
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

                String result = "";

                if (App.get(formType).equals(getResources().getString(R.string.ctb_order))){
                    result = serverService.saveEncounterAndObservation("Ultrasound Test Order", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = serverService.saveEncounterAndObservation("Ultrasound Test Result", FORM, formDateCalendar, observations.toArray(new String[][]{}),true);
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int encounterId) {
        OfflineForm fo = serverService.getOfflineFormById(encounterId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();

        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());


        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if(fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);
                testIdView.setImageResource(R.drawable.ic_checked_green);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    testId.getEditText().setText(obs[0][1]);
                }
                else if (obs[0][0].equals("TEST CONTEXT STATUS")) {
                    for (RadioButton rb : pointTestBeingDone.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_diagnostic)) && obs[0][1].equals("DIAGNOSTIC TESTING")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_followup)) && obs[0][1].equals("REGULAR FOLLOW UP")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                }
                else if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                    monthTreatment.getSpinner().selectValue(obs[0][1]);
                }else if (obs[0][0].equals("ULTRASOUND SITE")) {
                    for (RadioButton rb : ultrasoundSite.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_abdomen)) && obs[0][1].equals("ULTRASOUND, ABDOMEN")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("ULTRASOUND SITE OTHER")) {
                            rb.setChecked(true);
                            break;
                        }
                    }
                }
                else if (obs[0][0].equals("ULTRASOUND SITE OTHER")) {
                    otherUltrasoundSite.getEditText().setText(obs[0][1]);
                }
                submitButton.setEnabled(true);
            }else{
                formType.getRadioGroup().getButtons().get(1).setChecked(true);
                formType.getRadioGroup().getButtons().get(0).setEnabled(false);
                if (obs[0][0].equals("TEST ID")) {
                    testId.getEditText().setText(obs[0][1]);
                    testId.getEditText().setEnabled(false);
                    testIdView.setEnabled(false);
                    testIdView.setImageResource(R.drawable.ic_checked);
                    checkTestId();
                }
               else if (obs[0][0].equals("ULTRASOUND RESULT")) {
                    String value = obs[0][1].equals("ABDOMEN ADENOPATHY") ? getResources().getString(R.string.ctb_abdomen_adenopathy) :
                            (obs[0][1].equals("INTESTINAL WALL THICKENING") ? getResources().getString(R.string.ctb_intestinal_wall_thickening) :
                                    (obs[0][1].equals("ASCITES") ? getResources().getString(R.string.ctb_ascites) :
                                            (obs[0][1].equals("PLEURAL EFFUSION") ? getResources().getString(R.string.ctb_pleural_effusion) :
                                                    (obs[0][1].equals("MATTED LYMPH NODES") ? getResources().getString(R.string.ctb_matted_lymph_nodes) :
                                                            (obs[0][1].equals("NORMAL") ? getResources().getString(R.string.ctb_normal) :
                                                                    getResources().getString(R.string.ctb_other_title))))));
                    ultrasoundResult.getSpinner().selectValue(value);
                }else if (obs[0][0].equals("OTHER ULTRASOUND RESULT")) {
                    otherUltrasoundResult.getEditText().setText(obs[0][1]);
                }
                else if (obs[0][0].equals("ULTRASOUND INTERPRETATION")) {
                    for (RadioButton rb : ultrasoundInterpretation.getRadioGroup().getButtons()) {
                        if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                            rb.setChecked(true);
                            break;
                        } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal)) && obs[0][1].equals("NORMAL")) {
                            rb.setChecked(true);
                            break;
                        }else if (rb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER ULTRASOUND RESULT")) {
                            rb.setChecked(true);
                            break;
                        }
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
        if (spinner == ultrasoundResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherUltrasoundResult.setVisibility(View.VISIBLE);
            } else {
                otherUltrasoundResult.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formType.getRadioGroup().getButtons().get(0).setEnabled(true);
        formType.getRadioGroup().getButtons().get(1).setEnabled(true);
        testIdView.setEnabled(true);
        testIdView.setVisibility(View.GONE);
        testId.setVisibility(View.GONE);
        testIdView.setImageResource(R.drawable.ic_checked);

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
       goneVisibility();
        submitButton.setEnabled(false);
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

    void goneVisibility(){
        formDate.setVisibility(View.GONE);
        pointTestBeingDone.setVisibility(View.GONE);
        monthTreatment.setVisibility(View.GONE);
        ultrasoundSite.setVisibility(View.GONE);
        otherUltrasoundSite.setVisibility(View.GONE);
        ultrasoundResult.setVisibility(View.GONE);
        otherUltrasoundResult.setVisibility(View.GONE);
        ultrasoundInterpretation.setVisibility(View.GONE);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == formType.getRadioGroup()) {
            if (group == formType.getRadioGroup()) {
                formDate.setVisibility(View.VISIBLE);
                testId.setVisibility(View.VISIBLE);
                testId.getEditText().setText("");
                testId.getEditText().setError(null);
                goneVisibility();
                submitButton.setEnabled(false);
            }
        }
        if (group == pointTestBeingDone.getRadioGroup()) {
            if (pointTestBeingDone.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_followup))) {
                monthTreatment.setVisibility(View.VISIBLE);

            } else {
                monthTreatment.setVisibility(View.GONE);
            }
        }
        if (group == ultrasoundSite.getRadioGroup()) {
            if (ultrasoundSite.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_other_title))) {
                otherUltrasoundSite.setVisibility(View.VISIBLE);

            } else {
                otherUltrasoundSite.setVisibility(View.GONE);
            }
        }


    }

    private void checkTestId() {
        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.verifying_test_id));
                        loading.show();
                    }
                });

                String result = "";

                Object[][] testIds = serverService.getTestIdByPatientAndEncounterType(App.getPatientId(), "Childhood TB-Ultrasound Test Order");

                if (testIds == null || testIds.length < 1) {
                    if (App.get(formType).equals(getResources().getString(R.string.ctb_order)))
                        return "SUCCESS";
                    else
                        return "";
                }


                if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
                    result = "SUCCESS";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(testId))) {
                            return "";
                        }
                    }
                }

                if (App.get(formType).equals(getResources().getString(R.string.ctb_result))) {
                    result = "";
                    for (int i = 0; i < testIds.length; i++) {
                        if (String.valueOf(testIds[i][0]).equals(App.get(testId))) {
                            return "SUCCESS";
                        }
                    }
                }

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

                    testIdView.setImageResource(R.drawable.ic_checked_green);
                    showTestOrderOrTestResult();
                    submitButton.setEnabled(true);

                } else {

                    if (App.get(formType).equals(getResources().getString(R.string.ctb_order))) {
                        testId.getEditText().setError("Test Id already used.");
                    } else {
                        testId.getEditText().setError("No order form found for the test id for patient");
                    }

                }

                try {
                    InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        submissionFormTask.execute("");

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean error = false;

                checkTestId();


                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
    }
    void showTestOrderOrTestResult() {
        if (formType.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.ctb_order))) {
            formDate.setVisibility(View.VISIBLE);
            ultrasoundSite.setVisibility(View.VISIBLE);
            pointTestBeingDone.setVisibility(View.VISIBLE);

            ultrasoundResult.setVisibility(View.GONE);
            ultrasoundInterpretation.setVisibility(View.GONE);
            otherUltrasoundResult.setVisibility(View.GONE);


        } else {
            formDate.setVisibility(View.VISIBLE);
            ultrasoundResult.setVisibility(View.VISIBLE);
            ultrasoundInterpretation.setVisibility(View.VISIBLE);

            pointTestBeingDone.setVisibility(View.GONE);
            monthTreatment.setVisibility(View.GONE);
            ultrasoundSite.setVisibility(View.GONE);
            otherUltrasoundSite.setVisibility(View.GONE);
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

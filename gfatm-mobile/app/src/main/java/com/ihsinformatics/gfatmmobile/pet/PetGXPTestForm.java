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

public class PetGXPTestForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledSpinner orderIds;
    TitledEditText cartidgeID;
    TitledRadioGroup sampleAccepted;
    TitledSpinner whySampleRejected;
    TitledEditText otherReasonForRejection;

    TitledSpinner geneXpertMTBResult;
    TitledRadioGroup mtbBurden;
    TitledSpinner mtbRIFResult;
    TitledEditText errorCode;
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
        FORM_NAME = Forms.PET_GXP_TEST;
        FORM = Forms.pet_gxp_test;

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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        sampleAccepted = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_sample_accepted_lab_techician),getResources().getStringArray(R.array.ctb_accepted_by_techician_list),getResources().getString(R.string.ctb_accepted),App.HORIZONTAL,App.VERTICAL,true);
        whySampleRejected = new TitledSpinner(context,null,getResources().getString(R.string.ctb_why_sample_rejected),getResources().getStringArray(R.array.ctb_why_sample_rejected_list),getResources().getString(R.string.ctb_saliva),App.HORIZONTAL,true);
        otherReasonForRejection = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.pet_empty_array), "", App.HORIZONTAL);
        cartidgeID = new TitledEditText(context,null,getResources().getString(R.string.ctb_cartridge_id),"","",10,RegexUtil.OTHER_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,true);
        geneXpertMTBResult = new TitledSpinner(context,null,getResources().getString(R.string.ctb_mtb_result),getResources().getStringArray(R.array.ctb_mtb_result_list),getResources().getString(R.string.ctb_mtb_not_detected),App.HORIZONTAL,true);
        mtbBurden = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_mtb_burden),getResources().getStringArray(R.array.ctb_mtb_burden_list),getResources().getString(R.string.ctb_very_low),App.HORIZONTAL,App.VERTICAL,true);
        mtbRIFResult = new TitledSpinner(context,null,getResources().getString(R.string.ctb_mtb_rif_result),getResources().getStringArray(R.array.ctb_mtb_rif_result_list),getResources().getString(R.string.ctb_not_detected),App.HORIZONTAL,true);
        errorCode = new TitledEditText(context,null,getResources().getString(R.string.ctb_error_code),"","",4,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,true);


        views = new View[]{formDate.getButton(),sampleAccepted.getRadioGroup(),whySampleRejected.getSpinner(),otherReasonForRejection.getEditText(),geneXpertMTBResult.getSpinner(),mtbBurden.getRadioGroup(),mtbRIFResult.getSpinner(),
                errorCode.getEditText(),orderIds.getQuestionView(),cartidgeID.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,orderIds,sampleAccepted,whySampleRejected,otherReasonForRejection,cartidgeID
                        ,geneXpertMTBResult,mtbBurden,mtbRIFResult,errorCode}};

        formDate.getButton().setOnClickListener(this);
        sampleAccepted.getRadioGroup().setOnCheckedChangeListener(this);
        whySampleRejected.getSpinner().setOnItemSelectedListener(this);
        geneXpertMTBResult.getSpinner().setOnItemSelectedListener(this);
        mtbBurden.getRadioGroup().setOnCheckedChangeListener(this);
        mtbRIFResult.getSpinner().setOnItemSelectedListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);

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
            } else {
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "GXP Specimen Collection", "ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (formDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            formDateCalendar = Calendar.getInstance();
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDateCalendar = App.getCalendar(dDate);
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }

                }
            }

        } else{
            String formDa = formDate.getButton().getText().toString();

            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

             if (!App.get(orderIds).equals("")) {
                    String encounterDateTime = serverService.getEncounterDateTimeByObs(App.getPatientId(), App.getProgram() + "-" + "GXP Specimen Collection", "ORDER ID", App.get(orderIds));

                    String format = "";
                    if (encounterDateTime.contains("/")) {
                        format = "dd/MM/yyyy";
                    } else {
                        format = "yyyy-MM-dd";
                    }

                    Date orderDate = App.stringToDate(encounterDateTime, format);

                    if (formDateCalendar.before(App.getCalendar(orderDate))) {

                        Date dDate = App.stringToDate(formDa, "EEEE, MMM dd,yyyy");
                        if (dDate.before(orderDate)) {
                            formDateCalendar = Calendar.getInstance();
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        } else {
                            formDateCalendar = App.getCalendar(dDate);
                            formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
                        }

                        snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_result_date_cannot_be_before_order_date), Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();

                    }

                }

        }
        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        boolean error=false;
        if (errorCode.getVisibility() == View.VISIBLE && App.get(errorCode).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            errorCode.getEditText().setError(getString(R.string.empty_field));
            errorCode.getEditText().requestFocus();
            error = true;
        }
        else{
            errorCode.getEditText().setError(null);
        }

        if(cartidgeID.getVisibility()==View.VISIBLE){
            if(App.get(cartidgeID).isEmpty()){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cartidgeID.getEditText().setError(getString(R.string.empty_field));
                cartidgeID.getEditText().requestFocus();
                error = true;
            }
            else if(App.get(cartidgeID).length()<10){
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                cartidgeID.getEditText().setError(getString(R.string.ctb_cartridge_id_length));
                cartidgeID.getEditText().requestFocus();
                error = true;
            }
            else if (App.get(cartidgeID).trim().length() <= 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    cartidgeID.getEditText().setError(getString(R.string.ctb_spaces_only));
                    cartidgeID.getEditText().requestFocus();
                    error = true;
                }
        }


        Boolean flag = true;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            if (saveFlag) {
                flag = false;
            }else {
                flag = true;
            }
        }


        if(orderIds.getVisibility()==View.VISIBLE  && flag){
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "GXP Test", "ORDER ID");
            if(resultTestIds != null){
                for(String id : resultTestIds) {

                    if (id.equals(App.get(orderIds))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_gxp_result_exist_for_order_id) + App.get(orderIds));
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

                        return false;
                    }
                }
            }
        }

        if(cartidgeID.getVisibility() == View.VISIBLE  && flag){
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "GXP Test", "CARTRIDGE ID");
            if(resultTestIds != null) {
                for (String id : resultTestIds) {
                    if (id.equals(App.get(cartidgeID))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_test_result_found_error) + App.get(cartidgeID));
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

                        return false;
                    }

                }
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
        observations.add(new String[]{"ORDER ID", App.get(orderIds)});
        observations.add(new String[]{"CARTRIDGE ID", App.get(cartidgeID)});

        observations.add(new String[]{"SPECIMEN ACCEPTED", App.get(sampleAccepted).equals(getResources().getString(R.string.ctb_accepted)) ? "ACCEPTED" :
                "REJECTED"});

        if(whySampleRejected.getVisibility()==View.VISIBLE){
        }
        observations.add(new String[]{"SPECIMEN UNSATISFACTORY FOR DIAGNOSIS", App.get(whySampleRejected).equals(getResources().getString(R.string.ctb_saliva)) ? "SALIVA" :
                (App.get(whySampleRejected).equals(getResources().getString(R.string.ctb_blood)) ? "BLOOD IN SAMPLE" :
                        (App.get(whySampleRejected).equals(getResources().getString(R.string.ctb_food_particles)) ? "FOOD PARTICALS" :
                                (App.get(whySampleRejected).equals(getResources().getString(R.string.ctb_older_than_3_days)) ? "SAMPLE OLDER THAN 3 DAYS" :
                                        (App.get(whySampleRejected).equals(getResources().getString(R.string.ctb_insufficient_qunatity)) ? "INSUFFICIENT QUANTITY" : "OTHER REASON OF SAMPLE REJECTION"))))});

        if(otherReasonForRejection.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"OTHER REASON OF SAMPLE REJECTION", App.get(otherReasonForRejection)});
        }
        observations.add(new String[]{"DATE OF  TEST RESULT RECEIVED", App.getSqlDateTime(secondDateCalendar)});

        observations.add(new String[]{"GENEXPERT MTB/RIF RESULT", App.get(geneXpertMTBResult).equals(getResources().getString(R.string.ctb_mtb_detected)) ? "DETECTED" :
                (App.get(geneXpertMTBResult).equals(getResources().getString(R.string.ctb_mtb_not_detected)) ? "NOT DETECTED" :
                        (App.get(geneXpertMTBResult).equals(getResources().getString(R.string.ctb_error)) ? "ERROR" :
                                (App.get(geneXpertMTBResult).equals(getResources().getString(R.string.ctb_invalid)) ? "INVALID" : "NO RESULT")))});



        if (mtbBurden.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"MTB BURDEN", App.get(mtbBurden).toUpperCase()});
        }

        if (mtbRIFResult.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"RIF RESISTANCE RESULT", App.get(mtbRIFResult).toUpperCase()});
        }
        if (errorCode.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ERROR CODE", App.get(errorCode)});


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

                String result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+"GXP Test", FORM, formDateCalendar, observations.toArray(new String[][]{}),false);
                if (result.contains("SUCCESS"))
                    return "SUCCESS";

                return result;

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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {
        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if(obs[0][0].equals("TIME TAKEN TO FILL FORM")){
                timeTakeToFill = obs[0][1];
            }else if (obs[0][0].equals("CARTRIDGE ID")) {
                cartidgeID.getEditText().setText(obs[0][1]);
            }
            else if (obs[0][0].equals("ORDER ID")) {
                orderIds.getSpinner().selectValue(obs[0][1]);
            }

            else if (obs[0][0].equals("SPECIMEN ACCEPTED")) {
                for (RadioButton rb : sampleAccepted.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_accepted)) && obs[0][1].equals("ACCEPTED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_rejected)) && obs[0][1].equals("REJECTED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                sampleAccepted.setVisibility(View.VISIBLE);
            }
            else if (obs[0][0].equals("SPECIMEN UNSATISFACTORY FOR DIAGNOSIS")) {
                String value = obs[0][1].equals("SALIVA") ? getResources().getString(R.string.ctb_saliva) :
                        (obs[0][1].equals("BLOOD IN SAMPLE") ? getResources().getString(R.string.ctb_blood) :
                                (obs[0][1].equals("FOOD PARTICALS") ? getResources().getString(R.string.ctb_food_particles) :
                                        (obs[0][1].equals("SAMPLE OLDER THAN 3 DAYS") ? getResources().getString(R.string.ctb_older_than_3_days) :
                                                (obs[0][1].equals("INSUFFICIENT QUANTITY") ? getResources().getString(R.string.ctb_insufficient_qunatity) :
                                                    getResources().getString(R.string.ctb_other_title)))));
                whySampleRejected.getSpinner().selectValue(value);
            }
            else if (obs[0][0].equals("OTHER REASON OF SAMPLE REJECTION")) {
                otherReasonForRejection.getEditText().setText(obs[0][1]);
            }

            else if (obs[0][0].equals("DATE OF  TEST RESULT RECEIVED")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
            } else if (obs[0][0].equals("GENEXPERT MTB/RIF RESULT")) {
                String value = obs[0][1].equals("DETECTED") ? getResources().getString(R.string.ctb_mtb_detected) :
                        (obs[0][1].equals("NOT DETECTED") ? getResources().getString(R.string.ctb_mtb_not_detected) :
                                (obs[0][1].equals("ERROR") ? getResources().getString(R.string.ctb_error) :
                                        (obs[0][1].equals("INVALID") ? getResources().getString(R.string.ctb_invalid) :
                                                getResources().getString(R.string.ctb_no_result))));
                geneXpertMTBResult.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("MTB BURDEN")) {
                for (RadioButton rb : mtbBurden.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_very_low)) && obs[0][1].equals("VERY LOW")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_low)) && obs[0][1].equals("LOW")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_medium)) && obs[0][1].equals("MEDIUM")) {
                        rb.setChecked(true);
                        break;
                    }else if (rb.getText().equals(getResources().getString(R.string.ctb_high)) && obs[0][1].equals("HIGH")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("RIF RESISTANCE RESULT")) {
                String value = obs[0][1].equals("NOT DETECTED") ? getResources().getString(R.string.ctb_not_detected) :
                        (obs[0][1].equals("DETECTED") ? getResources().getString(R.string.ctb_detected) :
                                getResources().getString(R.string.ctb_indeterminate));
                mtbRIFResult.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("ERROR CODE")) {
                errorCode.getEditText().setText(obs[0][1]);
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
        if (spinner == geneXpertMTBResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_mtb_detected))) {
                mtbBurden.setVisibility(View.VISIBLE);
                mtbRIFResult.setVisibility(View.VISIBLE);
            } else {
                mtbBurden.setVisibility(View.GONE);
                mtbRIFResult.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_error))) {
                errorCode.setVisibility(View.VISIBLE);
            } else {
                errorCode.setVisibility(View.GONE);
            }
        }
        if (spinner == whySampleRejected.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherReasonForRejection.setVisibility(View.VISIBLE);
            } else {
                otherReasonForRejection.setVisibility(View.GONE);
            }
        }
        if (spinner == orderIds.getSpinner()) {
            updateDisplay();
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

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        whySampleRejected.setVisibility(View.GONE);
        otherReasonForRejection.setVisibility(View.GONE);
        mtbBurden.setVisibility(View.GONE);
        mtbRIFResult.setVisibility(View.GONE);
        errorCode.setVisibility(View.GONE);

        String[] testIds = serverService.getAllObsValues(App.getPatientId(), App.getProgram() + "-" + "GXP Specimen Collection", "ORDER ID");
        if(testIds == null || testIds.length == 0){
            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.ctb_no_gxp_speciman_error));
            submitButton.setEnabled(false);
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

        if(testIds != null) {
            orderIds.getSpinner().setSpinnerData(testIds);
        }

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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == sampleAccepted.getRadioGroup()) {
            if (sampleAccepted.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rejected))) {
                whySampleRejected.setVisibility(View.VISIBLE);
                if(App.get(whySampleRejected).equals(getResources().getString(R.string.ctb_other_title))){
                    otherReasonForRejection.setVisibility(View.VISIBLE);
                }
                geneXpertMTBResult.setVisibility(View.GONE);
                mtbBurden.setVisibility(View.GONE);
                mtbRIFResult.setVisibility(View.GONE);
                errorCode.setVisibility(View.GONE);

                cartidgeID.setVisibility(View.GONE);
            } else {
                geneXpertMTBResult.setVisibility(View.VISIBLE);
                if(App.get(geneXpertMTBResult).equals(getResources().getString(R.string.ctb_mtb_detected))){
                    mtbBurden.setVisibility(View.VISIBLE);
                    mtbRIFResult.setVisibility(View.VISIBLE);
                }
                else if (App.get(geneXpertMTBResult).equals(getResources().getString(R.string.ctb_error))){
                    errorCode.setVisibility(View.VISIBLE);
                }
                cartidgeID.setVisibility(View.VISIBLE);

                whySampleRejected.setVisibility(View.GONE);
                otherReasonForRejection.setVisibility(View.GONE);
            }
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

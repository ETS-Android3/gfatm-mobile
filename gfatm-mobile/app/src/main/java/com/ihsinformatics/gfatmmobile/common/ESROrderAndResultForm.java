package com.ihsinformatics.gfatmmobile.common;

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
 * Created by Haris on 2/21/2017.
 */

public class ESROrderAndResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledRadioGroup formType;


    //orderView
    TitledRadioGroup assessment_type;
    TitledEditText monthOfTreatment;
    TitledEditText orderId;
    TitledEditText doctor_notes;


    //resultView
    TitledSpinner orderIds;
    TitledEditText sampleId;
    TitledEditText esr_value;
    TitledSpinner esr_unit;
    TitledEditText esr_unit_other;


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
        formName = Forms.ESR_ORDER_AND_RESULT;
        form = Forms.esrOrderAndResult;

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
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.fast_order_and_result_list), "", App.HORIZONTAL, App.HORIZONTAL);
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }

        assessment_type = new TitledRadioGroup(context, null, getResources().getString(R.string.common_cbc_assessment_type), getResources().getStringArray(R.array.common_cbc_assessment_type_options), getString(R.string.common_cbc_assessment_type_baseline), App.VERTICAL, App.VERTICAL, true, "TYPE OF ASSESSMENT", new String[]{"BASELINE ASSESSMENT", "TREATMENT INITIATION", "FOLLOW UP", "END OF TREATMENT ASSESSMENT", "POST-TREATMENT ASSESSMENT"});
        monthOfTreatment = new TitledEditText(context, null, getResources().getString(R.string.fast_month_of_treatment), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "FOLLOW-UP MONTH");
        orderId = new TitledEditText(context, null, getResources().getString(R.string.order_id), "", "", 40, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "ORDER ID");
        doctor_notes = new TitledEditText(context, null, "Notes", "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "CLINICIAN NOTES (TEXT)");

        /////////////////////
        orderIds = new TitledSpinner(context, "", getResources().getString(R.string.order_id), getResources().getStringArray(R.array.ztts_empty_array), "", App.HORIZONTAL, true);
        sampleId = new TitledEditText(context, null, getResources().getString(R.string.common_cbc_sample_id), "", "", 50, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        esr_value = new TitledEditText(context, null, getResources().getString(R.string.common_esr_value), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true, "ESR Result Value");
        esr_unit = new TitledSpinner(context, "", getResources().getString(R.string.common_esr_unit), getResources().getStringArray(R.array.common_esr_unit_options), getResources().getString(R.string.common_esr_unit_1), App.HORIZONTAL, true, "ESR Result Unit", new String[]{"mm/hr", "OTHER ESR RESULT UNIT"});
        esr_unit_other = new TitledEditText(context, null, getString(R.string.common_esr_unit_2), "", "", 5, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true, "Other ESR Result Unit");

        // Used for reset fields...
        views = new View[]{formType.getRadioGroup(), formDate.getButton(), assessment_type.getRadioGroup(), monthOfTreatment, orderId, doctor_notes, orderIds.getSpinner(), sampleId.getEditText(), esr_value.getEditText(), esr_unit.getSpinner(), esr_unit_other.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, assessment_type, monthOfTreatment, orderId, doctor_notes, orderIds, sampleId, esr_value, esr_unit, esr_unit_other}};

        formDate.getButton().setOnClickListener(this);
        orderIds.getSpinner().setOnItemSelectedListener(this);
        formType.getRadioGroup().setOnCheckedChangeListener(this);
        assessment_type.getRadioGroup().setOnCheckedChangeListener(this);
        monthOfTreatment.getEditText().setEnabled(false);

        esr_value.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = esr_value.getEditText().getText().toString();
                if (text != null)
                    if (text.length() > 0) {
                        esr_unit.setVisibility(View.VISIBLE);
                        if (esr_unit.getSpinner().getSelectedItem().toString().equals(getString(R.string.common_esr_unit_2))) {
                            esr_unit_other.setVisibility(View.VISIBLE);
                        } else {
                            esr_unit_other.setVisibility(View.GONE);
                        }
                    } else {
                        esr_unit.setVisibility(View.GONE);
                        esr_unit_other.setVisibility(View.GONE);
                    }
            }
        });
        esr_unit.getSpinner().setOnItemSelectedListener(this);


        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


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
        Boolean error = super.validate();


        if (orderIds.getVisibility() == View.VISIBLE) {
            String[] resultTestIds = serverService.getAllObsValues(App.getPatientId(), "ESR Test Result", "ORDER ID");
            if (resultTestIds != null) {
                for (String id : resultTestIds) {

                    if (id.equals(App.get(orderIds))) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.ctb_order_result_found_error) + App.get(orderIds));
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

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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

                String result = "";


                if (App.get(formType).equals(getResources().getString(R.string.fast_order))) {
                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocallyTesting("ESR Test Order", form, formDateCalendar, observations.toArray(new String[][]{}));

                    result = serverService.saveEncounterAndObservationTesting("ESR Test Order", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;

                    String uuidEncounter = result.split("_")[1];

                    result = serverService.saveLabTestOrder(uuidEncounter, "refer_esr", App.get(orderId), formDateCalendar, id, "WHOLE BLOOD SAMPLE", "WHOLE BLOOD");
                    if (!result.contains("SUCCESS"))
                        return result;

                    /*String uuidLabOrder = result.split("_")[1];

                    final ArrayList<String[]> newObservations = new ArrayList<String[]>();
                    newObservations.add(new String[]{"LAB ORDER UUID",uuidLabOrder});
                    result = serverService.updateEncounterAndObservationTesting("ESR Test Order", uuidEncounter, newObservations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
                        return result;*/

                    return "SUCCESS";

                } else if (App.get(formType).equals(getResources().getString(R.string.fast_result))) {

                    String id = null;
                    if (App.getMode().equalsIgnoreCase("OFFLINE"))
                        id = serverService.saveFormLocallyTesting("ESR Test Result", form, formDateCalendar, observations.toArray(new String[][]{}));

                    //String orderUuid = serverService.getObsValueByObs(App.getPatientId(), "ESR Test Order", "ORDER ID", App.get(orderIds), "LAB ORDER UUID");

                    String orderUuid = serverService.getOrderUuidByLabTestId(App.getPatientId(), "ESR", App.get(orderIds));

                    result = serverService.saveLabTestResult("refer_esr", App.get(orderIds), orderUuid, observations.toArray(new String[][]{}), id);
                    if (!result.contains("SUCCESS"))
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
        OfflineForm fo = serverService.getSavedFormById(formId);

        ArrayList<String[][]> obsValue = fo.getObsValue();


        for (int i = 0; i < obsValue.size(); i++) {
            String[][] obs = obsValue.get(i);
            if (fo.getFormName().contains("Order")) {
                formType.getRadioGroup().getButtons().get(0).setChecked(true);
                formType.getRadioGroup().getButtons().get(1).setEnabled(false);

                submitButton.setEnabled(true);

            } else {
                formType.getRadioGroup().getButtons().get(0).setChecked(false);
                formType.getRadioGroup().getButtons().get(1).setEnabled(true);

            }

        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {

            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);

            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == esr_unit.getSpinner())
            if (spinner.getSelectedItem().equals(getString(R.string.common_esr_unit_2))) {
                esr_unit_other.setVisibility(View.VISIBLE);
            } else {
                esr_unit_other.setVisibility(View.GONE);
            }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    @Override
    public void resetViews() {
        super.resetViews();

        formDate.setVisibility(View.GONE);
        assessment_type.setVisibility(View.GONE);
        monthOfTreatment.setVisibility(View.GONE);
        orderId.setVisibility(View.GONE);
        doctor_notes.setVisibility(View.GONE);
        orderIds.setVisibility(View.GONE);
        sampleId.setVisibility(View.GONE);
        esr_value.setVisibility(View.GONE);
        esr_unit.setVisibility(View.GONE);
        esr_unit_other.setVisibility(View.GONE);

        submitButton.setEnabled(false);


        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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

    public void setOrderId() {
        Date nowDate = new Date();
        orderId.getEditText().setText("ESR - " + App.getSqlDateTime(nowDate));
        orderId.getEditText().setKeyListener(null);
        orderId.getEditText().setFocusable(false);
    }

    public void updateFollowUpMonth() {


        String treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "TREATMENT START DATE");
        if (treatmentDate == null) {
            treatmentDate = serverService.getLatestObsValue(App.getPatientId(), "PET-" + "Treatment Initiation", "TREATMENT START DATE");
        }

        String format = "";
        String[] monthArray;

        if (treatmentDate == null) {
            monthArray = new String[1];
            monthArray[0] = "0";
            monthOfTreatment.getEditText().setText("" + monthArray[0]);
        } else {
            if (treatmentDate.contains("/")) {
                format = "dd/MM/yyyy";
            } else {
                format = "yyyy-MM-dd";
            }
            Date convertedDate = App.stringToDate(treatmentDate, format);
            Calendar treatmentDateCalender = App.getCalendar(convertedDate);
            int diffYear = formDateCalendar.get(Calendar.YEAR) - treatmentDateCalender.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + formDateCalendar.get(Calendar.MONTH) - treatmentDateCalender.get(Calendar.MONTH);
            monthOfTreatment.getEditText().setText("" + diffMonth);


        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == assessment_type.getRadioGroup()) {
            monthOfTreatment.setVisibility(View.GONE);
            if (assessment_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_cbc_assessment_type_folowup))) {
                monthOfTreatment.setVisibility(View.VISIBLE);
                updateFollowUpMonth();
            }
        }
        if (radioGroup == formType.getRadioGroup()) {
            if (radioGroup == formType.getRadioGroup()) {
                formDate.setVisibility(View.GONE);
                assessment_type.setVisibility(View.GONE);
                monthOfTreatment.setVisibility(View.GONE);
                orderId.setVisibility(View.GONE);
                doctor_notes.setVisibility(View.GONE);
                orderIds.setVisibility(View.GONE);
                sampleId.setVisibility(View.GONE);
                esr_value.setVisibility(View.GONE);
                esr_unit.setVisibility(View.GONE);
                esr_unit_other.setVisibility(View.GONE);

                if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_order))) {
                    setOrderId();
                    submitButton.setEnabled(true);
                    formDate.setVisibility(View.VISIBLE);
                    assessment_type.setVisibility(View.VISIBLE);
                    if (assessment_type.getRadioGroup().getSelectedValue().equals(getString(R.string.common_cbc_assessment_type_folowup))) {
                        monthOfTreatment.setVisibility(View.VISIBLE);
                        updateFollowUpMonth();
                    }
                    orderId.setVisibility(View.VISIBLE);
                    doctor_notes.setVisibility(View.VISIBLE);
                } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_result))) {
                    submitButton.setEnabled(true);
                    formDate.setVisibility(View.VISIBLE);
                    orderIds.setVisibility(View.VISIBLE);
                    sampleId.setVisibility(View.VISIBLE);
                    esr_value.setVisibility(View.VISIBLE);

                    if (App.get(esr_value).length() > 0) {
                        esr_unit.setVisibility(View.VISIBLE);
                        if (esr_unit.getSpinner().getSelectedItem().toString().equals(getString(R.string.common_esr_unit_2))) {
                            esr_unit_other.setVisibility(View.VISIBLE);
                        } else {
                            esr_unit_other.setVisibility(View.GONE);
                        }
                    }

                    String[] testIds = serverService.getAllTestsIds(App.getPatientId(), "ESR");

                    if (testIds == null || testIds.length == 0) {
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.fast_no_order_found_for_the_patient));
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
                        submitButton.setEnabled(false);
                        return;
                    }

                    if (testIds != null) {
                        orderIds.getSpinner().setSpinnerData(testIds);
                    }
                }
            }
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

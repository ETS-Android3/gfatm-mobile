package com.ihsinformatics.gfatmmobile.pet;


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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MUHAMMAD WAQAS on 8/29/2017.
 */

public class PetRetrivelForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledRadioGroup intervention;
    TitledSpinner refusalFor;
    TitledEditText petDuration;
    TitledCheckBoxes counselingProvided;
    TitledEditText totalSession;
    LinearLayout datesLinearLayout;
    TitledCheckBoxes counselingProvidedTo;
    TitledCheckBoxes counselingProvidedBy;
    TitledCheckBoxes counselingRegarding;
    TitledCheckBoxes counselingTechnique;
    TitledEditText otherCounselingTechnique;
    TitledCheckBoxes reasonForAgreement;
    TitledEditText selfApproach;
    TitledEditText contactInvestigationOnly;
    TitledEditText informationFromExternalSource;
    TitledEditText incentivesContinuation;
    TitledEditText otherReasonForAgreement;
    TitledEditText psychologistNotes;

    ScrollView scrollView;

    Boolean refillFlag = false;

    String clickDate = "-1";

    public static final int THIRD_DIALOG_ID = 3;
    protected Calendar thirdDateCalender;
    protected DialogFragment thirdDateFragment;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 6;
        FORM_NAME = Forms.PET_RETRIVEL_FORM;
        FORM = Forms.pet_retrivel_form;

        thirdDateCalender = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
                scrollView.setScrollbarFadingEnabled(false);
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
        intervention = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_intervention), getResources().getStringArray(R.array.pet_interventions), "", App.HORIZONTAL, App.VERTICAL);
        refusalFor = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_refusal_for), getResources().getStringArray(R.array.pet_refusal_for_array), getResources().getString(R.string.pet_study_participation), App.VERTICAL, true);
        petDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        counselingProvided = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_provided), getResources().getStringArray(R.array.pet_counseling_provided_array), null, App.VERTICAL, App.VERTICAL, true);
        totalSession = new TitledEditText(context, null, getResources().getString(R.string.pet_number_of_counselinng_sessions), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true);
        datesLinearLayout = new LinearLayout(context);
        datesLinearLayout.setOrientation(LinearLayout.VERTICAL);
        counselingProvidedTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_provided_to), getResources().getStringArray(R.array.pet_counseling_provided_to_array), null, App.VERTICAL, App.VERTICAL, true);
        counselingProvidedBy = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_provided_by), getResources().getStringArray(R.array.pet_counseling_provided_by_array), null, App.VERTICAL, App.VERTICAL, true);
        counselingRegarding = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseled_regarding), getResources().getStringArray(R.array.pet_counseled_regarding_array), null, App.VERTICAL, App.VERTICAL, true);
        counselingTechnique = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_technique_used), getResources().getStringArray(R.array.pet_counseling_technique_used_array), null, App.VERTICAL, App.VERTICAL, true);
        otherCounselingTechnique = new TitledEditText(context, null, getResources().getString(R.string.pet_other_counseling_technique), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        otherCounselingTechnique.getEditText().setSingleLine(false);
        otherCounselingTechnique.getEditText().setMinimumHeight(150);
        reasonForAgreement = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_reason_for_aggrement), getResources().getStringArray(R.array.pet_reason_for_aggrement_array), null, App.VERTICAL, App.VERTICAL, true);
        selfApproach = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_self_approach), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        selfApproach.getEditText().setSingleLine(false);
        selfApproach.getEditText().setMinimumHeight(150);
        contactInvestigationOnly = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_contact_investigation_only), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        contactInvestigationOnly.getEditText().setSingleLine(false);
        contactInvestigationOnly.getEditText().setMinimumHeight(150);
        informationFromExternalSource = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_information_from_external_source), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        informationFromExternalSource.getEditText().setSingleLine(false);
        informationFromExternalSource.getEditText().setMinimumHeight(150);
        incentivesContinuation = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_incentives_continuation), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        incentivesContinuation.getEditText().setSingleLine(false);
        incentivesContinuation.getEditText().setMinimumHeight(150);
        otherReasonForAgreement = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_others), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        otherReasonForAgreement.getEditText().setSingleLine(false);
        otherReasonForAgreement.getEditText().setMinimumHeight(150);
        psychologistNotes = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_notes), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        psychologistNotes.getEditText().setSingleLine(false);
        psychologistNotes.getEditText().setMinimumHeight(150);

        views = new View[]{formDate.getButton(), refusalFor.getSpinner(), petDuration.getEditText(), counselingProvided, totalSession.getEditText(), counselingProvidedTo, counselingProvidedBy, counselingRegarding,
                counselingTechnique, otherCounselingTechnique.getEditText(), reasonForAgreement, selfApproach.getEditText(), informationFromExternalSource.getEditText(),
                contactInvestigationOnly.getEditText(), incentivesContinuation.getEditText(), otherReasonForAgreement.getEditText(), psychologistNotes.getEditText(), intervention};

        viewGroups = new View[][]{{formDate, intervention, refusalFor, petDuration, counselingProvided},
                {totalSession, datesLinearLayout},
                {counselingProvidedTo},
                {counselingProvidedBy, counselingRegarding, counselingTechnique, otherCounselingTechnique},
                {reasonForAgreement},
                {selfApproach, contactInvestigationOnly, informationFromExternalSource,
                        incentivesContinuation, otherReasonForAgreement, psychologistNotes}};

        formDate.getButton().setOnClickListener(this);
        refusalFor.getSpinner().setOnItemSelectedListener(this);

        for (CheckBox cb : counselingProvided.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counselingProvidedTo.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counselingProvidedBy.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counselingRegarding.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : counselingTechnique.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        for (CheckBox cb : reasonForAgreement.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        intervention.getRadioGroup().setOnCheckedChangeListener(this);


        resetViews();

        totalSession.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (App.get(totalSession).equals("") || App.get(totalSession).equals("0"))
                    datesLinearLayout.setVisibility(View.GONE);
                else {
                    datesLinearLayout.setVisibility(View.VISIBLE);
                    int no = Integer.valueOf(App.get(totalSession));
                    if (datesLinearLayout.getChildCount() != no) {

                        if (datesLinearLayout.getChildCount() > no) {
                            for (int i = datesLinearLayout.getChildCount(); i > no; i--) {
                                datesLinearLayout.removeViewAt(i - 1);
                            }
                        } else {
                            for (int i = datesLinearLayout.getChildCount(); i < no; i++) {
                                int j = i + 1;
                                thirdDateCalender = Calendar.getInstance();
                                TitledEditText dateSession = new TitledEditText(context, null, getResources().getString(R.string.pet_session_date) + " " + j, "", "", 50, null, -1, App.VERTICAL, true);
                                datesLinearLayout.addView(dateSession);
                                dateSession.getEditText().setTag(i);
                                dateSession.getEditText().setKeyListener(null);
                                dateSession.getEditText().setFocusable(false);
                                dateSession.getEditText().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        clickDate = v.getTag().toString();

                                        Bundle args = new Bundle();
                                        args.putInt("type", THIRD_DIALOG_ID);
                                        args.putBoolean("allowPastDate", true);
                                        args.putBoolean("allowFutureDate", false);
                                        thirdDateFragment.setArguments(args);
                                        thirdDateFragment.show(getFragmentManager(), "DatePicker");

                                    }
                                });
                            }
                        }

                    }

                }

            }
        });

    }

    @Override
    public void resetViews() {

        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        petDuration.setVisibility(View.GONE);
        otherCounselingTechnique.setVisibility(View.GONE);
        selfApproach.setVisibility(View.GONE);
        informationFromExternalSource.setVisibility(View.GONE);
        contactInvestigationOnly.setVisibility(View.GONE);
        incentivesContinuation.setVisibility(View.GONE);
        otherReasonForAgreement.setVisibility(View.GONE);
        datesLinearLayout.setVisibility(View.GONE);

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

        if(!autoFill) {
            String interventionString = serverService.getLatestObsValue(App.getPatientId(), App.getProgram() + "-" + Forms.PET_BASELINE_SCREENING, "INTERVENTION");
            if(interventionString != null){

                for (RadioButton rb : intervention.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet)) && interventionString.equals("PET")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.sci)) && interventionString.equals("SCI")) {
                        rb.setChecked(true);
                        break;
                    }
                }

            }
        }

    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = false;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);

        if (clickDate.equals("-1")) {

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
        } else {

            for (int i = 0; i < datesLinearLayout.getChildCount(); i++) {

                TitledEditText v = (TitledEditText) datesLinearLayout.getChildAt(i);
                String s = v.getEditText().toString();
                if (v.getEditText().getTag().toString().equals(clickDate)) {
                    v.getEditText().setText(DateFormat.format("EEEE, MMM dd,yyyy", thirdDateCalender).toString());
                    v.getEditText().setError(null);
                    break;
                }

            }

            clickDate = "-1";
        }
    }


    @Override
    public boolean validate() {

        Boolean error = false;
        View view = null;
        if (isTitledEditTextEmpty(psychologistNotes, 5))
            error = true;

        if (isTitledEditTextEmpty(selfApproach, 5))
            error = true;
        if (isTitledEditTextEmpty(informationFromExternalSource, 5))
            error = true;
        if (isTitledEditTextEmpty(incentivesContinuation, 5))
            error = true;
        if (isTitledEditTextEmpty(otherReasonForAgreement, 5))
            error = true;

        if (isCheckBoxesChecked(reasonForAgreement, 4))
            error = true;
        if (isCheckBoxesChecked(counselingProvidedBy, 3))
            error = true;
        if (isCheckBoxesChecked(counselingRegarding, 3))
            error = true;
        if (isCheckBoxesChecked(counselingTechnique, 3))
            error = true;
        if (isTitledEditTextEmpty(otherCounselingTechnique, 3))
            error = true;
        if (isCheckBoxesChecked(counselingProvidedTo, 2))
            error = true;
        if (isTitledEditTextEmpty(totalSession, 1))
            error = true;
        for (int i = 0; i < datesLinearLayout.getChildCount(); i++) {
            if (datesLinearLayout.getChildAt(i) instanceof TitledEditText)
                if (isTitledEditTextEmpty((TitledEditText) datesLinearLayout.getChildAt(i), 1))
                    error = true;
        }
        if (intervention.getVisibility() == View.VISIBLE && App.get(intervention).isEmpty()) {
            intervention.getQuestionView().setError(getString(R.string.empty_field));
            intervention.getQuestionView().requestFocus();
            gotoFirstPage();
            view = intervention;
            error = true;
        }else {
            intervention.getQuestionView().setError(null);
            intervention.getQuestionView().clearFocus();
        }

        if (isTitledEditTextEmpty(petDuration, 0))
            error = true;
        if (isCheckBoxesChecked(counselingProvided, 0))
            error = true;

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
        } else
            return true;

    }

    public boolean isCheckBoxesChecked(TitledCheckBoxes titledCheckBoxes, int pageNumber) {
        Boolean flagCheckBoxes = false;
        for (CheckBox cb : titledCheckBoxes.getCheckedBoxes()) {
            if (cb.isChecked()) {
                flagCheckBoxes = true;
                break;
            }
        }
        if (!flagCheckBoxes) {
            titledCheckBoxes.getQuestionView().setError(getResources().getString(R.string.mandatory_field));
            titledCheckBoxes.getQuestionView().requestFocus();
            gotoPage(pageNumber);
            return true;
        }else{
            titledCheckBoxes.getQuestionView().setError(null);
            titledCheckBoxes.getQuestionView().clearFocus();
        }
        return false;
    }

    public boolean isTitledEditTextEmpty(TitledEditText titledEditText, int pageNumber) {
        if (App.get(titledEditText).isEmpty() && titledEditText.getVisibility() == View.VISIBLE) {
            titledEditText.getEditText().setError(getResources().getString(R.string.mandatory_field));
            titledEditText.getEditText().requestFocus();
            gotoPage(pageNumber);
            return true;
        }else{
            titledEditText.getEditText().setError(null);
            titledEditText.getEditText().clearFocus();
        }
        return false;
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
        observations.add(new String[]{"INTERVENTION", App.get(intervention).equals(getResources().getString(R.string.pet)) ? "PET" : "SCI"});
        observations.add(new String[]{"REFUSAL FOR", App.get(refusalFor).equals(getResources().getString(R.string.pet_study_participation)) ? "REFUSED PARTICIPATION IN STUDY" :
                (App.get(refusalFor).equals(getResources().getString(R.string.pet_verbal_symptom_screening)) ? "VERBAL SYMPTOM SCREENING" :
                        (App.get(refusalFor).equals(getResources().getString(R.string.pet_investigations)) ? "INVESTIGATION" :
                                (App.get(refusalFor).equals(getResources().getString(R.string.pet_pet_initiation)) ? "PET INITIATION" : "PET CONTINUATION")))});
        if (petDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PET DURATION)", App.get(petDuration)});
        String counselingProvidedString = "";
        for (CheckBox cb : counselingProvided.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_face_to_face)))
                counselingProvidedString = counselingProvidedString + "FACE TO FACE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_on_phone)))
                counselingProvidedString = counselingProvidedString + "ON PHONE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_home_visit)))
                counselingProvidedString = counselingProvidedString + "HOME VISIT" + " ; ";
        }
        observations.add(new String[]{"COUNSELING MODE", counselingProvidedString});
        observations.add(new String[]{"TOTAL NUMBER OF SESSIONS", App.get(totalSession)});
        if(datesLinearLayout.getVisibility() == View.VISIBLE) {
            String sessionDateString = "";
            for (int i = 0; i < datesLinearLayout.getChildCount(); i++) {

                TitledEditText v = (TitledEditText) datesLinearLayout.getChildAt(i);
                String date = v.getEditText().getText().toString();
                if (!date.equals("")) {
                    Date dateFormat = App.stringToDate(date, "EEEE, MMM dd,yyyy");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateFormat);
                    String dateOnString = App.getSqlDate(calendar);
                    if(i == 0)
                        sessionDateString = dateOnString;
                    else
                        sessionDateString = sessionDateString + " ; " + dateOnString;
                }

            }
            observations.add(new String[]{"SESSION DATE", sessionDateString});
        }

        String counsellingProvidedToString = "";
        for (CheckBox cb : counselingProvidedTo.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_self)))
                counsellingProvidedToString = counsellingProvidedToString + "SELF" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_guardian)))
                counsellingProvidedToString = counsellingProvidedToString + "GUARDIAN" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_guardian)))
                counsellingProvidedToString = counsellingProvidedToString + "MOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_father)))
                counsellingProvidedToString = counsellingProvidedToString + "FATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_maternal_grandmother)))
                counsellingProvidedToString = counsellingProvidedToString + "MATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_maternal_grandfather)))
                counsellingProvidedToString = counsellingProvidedToString + "MATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_paternal_grandmother)))
                counsellingProvidedToString = counsellingProvidedToString + "PATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_paternal_grandfather)))
                counsellingProvidedToString = counsellingProvidedToString + "PATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_brother)))
                counsellingProvidedToString = counsellingProvidedToString + "BROTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_sister)))
                counsellingProvidedToString = counsellingProvidedToString + "SISTER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_son)))
                counsellingProvidedToString = counsellingProvidedToString + "SON" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_daughter)))
                counsellingProvidedToString = counsellingProvidedToString + "DAUGHTER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_spouse)))
                counsellingProvidedToString = counsellingProvidedToString + "SPOUSE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_aunt)))
                counsellingProvidedToString = counsellingProvidedToString + "AUNT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_uncle)))
                counsellingProvidedToString = counsellingProvidedToString + "UNCLE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_complete_family)))
                counsellingProvidedToString = counsellingProvidedToString + "COMPLETE FAMILY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                counsellingProvidedToString = counsellingProvidedToString + "OTHER" + " ; ";
        }
        observations.add(new String[]{"FAMILY MEMBERS COUNSELLED", counsellingProvidedToString});

        String counsellingProvidedByString = "";
        for (CheckBox cb : counselingProvidedBy.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_counselor)))
                counsellingProvidedByString = counsellingProvidedByString + "COUNSELOR" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_health_worker)))
                counsellingProvidedByString = counsellingProvidedByString + "HEALTH WORKER" + " ; ";
        }
        observations.add(new String[]{"COUNSELING BY", counsellingProvidedByString});

        String counselingRegardingString = "";
        for (CheckBox cb : counselingRegarding.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_tb_disease)))
                counselingRegardingString = counselingRegardingString + "TUBERCULOSIS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_contact_screening)))
                counselingRegardingString = counselingRegardingString + "CONTACT SCREENING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet)))
                counselingRegardingString = counselingRegardingString + "PET PROGRAM" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_adherence)))
                counselingRegardingString = counselingRegardingString + "ADHERENCE" + " ; ";
        }
        observations.add(new String[]{"COUNSELING TYPE", counselingRegardingString});

        String counselingTechniqueString = "";
        for (CheckBox cb : counselingTechnique.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_cost_analysis)))
                counselingTechniqueString = counselingTechniqueString + "COST BENEFIT ANALYSIS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_decatastrophization)))
                counselingTechniqueString = counselingTechniqueString + "DECATASTROPHIZING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_what_if_technique)))
                counselingTechniqueString = counselingTechniqueString + "WHAT IF TECHNIQUE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_worst_possible_outcome)))
                counselingTechniqueString = counselingTechniqueString + "WORST POSSIBLE OUTCOME" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_rogerian_principles)))
                counselingTechniqueString = counselingTechniqueString + "WORKING THROUGH ROGERIAN PRINCIPLES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_downward_arrow)))
                counselingTechniqueString = counselingTechniqueString + "DOWNWARD ARROW TECHNIQUE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_clarification)))
                counselingTechniqueString = counselingTechniqueString + "CLARIFICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_reflection)))
                counselingTechniqueString = counselingTechniqueString + "REFLECTION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_summarization)))
                counselingTechniqueString = counselingTechniqueString + "SUMMARIZATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_catharsis)))
                counselingTechniqueString = counselingTechniqueString + "CATHARSIS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                counselingTechniqueString = counselingTechniqueString + "OTHER" + " ; ";
        }
        observations.add(new String[]{"COUNSELING TECHNIQUE", counselingTechniqueString});

        if (otherCounselingTechnique.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COUNSELING TECHNIQUE", App.get(otherCounselingTechnique)});

        String reasonForAgreementString = "";
        for (CheckBox cb : reasonForAgreement.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_convinced_after_counseling)))
                reasonForAgreementString = reasonForAgreementString + "CONVINCED AFTER COUNSELING" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_self_approach_for_pet)))
                reasonForAgreementString = reasonForAgreementString + "SELF APPROACH FOR PET" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_for_screening_only)))
                reasonForAgreementString = reasonForAgreementString + "FOR SCREENING ONLY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_contact_investigation_only)))
                reasonForAgreementString = reasonForAgreementString + "CONTACT INVESTIGATION ONLY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_duration_reduced)))
                reasonForAgreementString = reasonForAgreementString + "DURATION OF PET REDUCED" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_management_of_adverse_effects_clinically)))
                reasonForAgreementString = reasonForAgreementString + "CLINICALLY MANAGEMENT OF ADVERSE EFFECTS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_management_of_adverse_effects_by_ancillary_drugs)))
                reasonForAgreementString = reasonForAgreementString + "MANAGEMENT OF ADVERSE EFFECTS BY ANCILLARY DRUGS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_information_of_pet_from_external_source)))
                reasonForAgreementString = reasonForAgreementString + "INFORMATION OF PET FROM EXTERNAL SOURCE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_family_diagnosed_other_than_index_case)))
                reasonForAgreementString = reasonForAgreementString + "OTHER THAN INDEX CASE, FAMILY MEMBER DIAGNOSED WITH TB" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_incentives_continuation)))
                reasonForAgreementString = reasonForAgreementString + "INCENTIVE CONTINUATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other_reason)))
                reasonForAgreementString = reasonForAgreementString + "OTHER" + " ; ";
        }
        observations.add(new String[]{"REASON FOR AGREEMENT", reasonForAgreementString});

        if (selfApproach.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SELF APPROACH FOR PET (TEXT)", App.get(selfApproach)});

        if (contactInvestigationOnly.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"CONTACT INVESTIGATION (TEXT)", App.get(contactInvestigationOnly)});

        if (informationFromExternalSource.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INFORMATION OF PET FROM EXTERNAL SOURCE (TEXT)", App.get(informationFromExternalSource)});

        if (incentivesContinuation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INCENTIVE CONTINUATION (TEXT)", App.get(incentivesContinuation)});

        if (otherReasonForAgreement.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON FOR AGREEMENT", App.get(otherReasonForAgreement)});

        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(psychologistNotes)});

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

                String result = serverService.saveEncounterAndObservation(App.getProgram()+"-"+FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
            formDate.getButton().setEnabled(false);

        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;
        if (spinner == refusalFor.getSpinner()) {

            if (App.get(refusalFor).equals(getResources().getString(R.string.pet_pet_continuation)))
                petDuration.setVisibility(View.VISIBLE);
            else
                petDuration.setVisibility(View.GONE);

        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        for (CheckBox cb : counselingProvided.getCheckedBoxes()) {
            if (cb.isChecked())
                counselingProvided.getQuestionView().setError(null);
        }
        for (CheckBox cb : counselingProvidedTo.getCheckedBoxes()) {
            if (cb.isChecked())
                counselingProvidedTo.getQuestionView().setError(null);
        }
        for (CheckBox cb : counselingProvidedBy.getCheckedBoxes()) {
            if (cb.isChecked())
                counselingProvidedBy.getQuestionView().setError(null);
        }
        for (CheckBox cb : counselingRegarding.getCheckedBoxes()) {
            if (cb.isChecked())
                counselingRegarding.getQuestionView().setError(null);
        }


        for (CheckBox cb : counselingTechnique.getCheckedBoxes()) {
            if (cb.isChecked()) {
                counselingTechnique.getQuestionView().setError(null);
            }
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                otherCounselingTechnique.setVisibility(View.VISIBLE);
            else
                otherCounselingTechnique.setVisibility(View.GONE);
        }

        for (CheckBox cb : reasonForAgreement.getCheckedBoxes()) {
            if (cb.isChecked()) {
                reasonForAgreement.getQuestionView().setError(null);
            }
            if (cb.getText().equals(getResources().getString(R.string.pet_self_approach_for_pet))) {
                if (cb.isChecked())
                    selfApproach.setVisibility(View.VISIBLE);
                else
                    selfApproach.setVisibility(View.GONE);
            }

//            if (cb.getText().equals(getResources().getString(R.string.pet_investigations))) {
//                if (cb.isChecked())
//                    contactInvestigationOnly.setVisibility(View.VISIBLE);
//                else
//                    contactInvestigationOnly.setVisibility(View.GONE);
//            }

            if (cb.getText().equals(getResources().getString(R.string.pet_information_of_pet_from_external_source))) {
                if (cb.isChecked())
                    informationFromExternalSource.setVisibility(View.VISIBLE);
                else
                    informationFromExternalSource.setVisibility(View.GONE);
            }

            if (cb.getText().equals(getResources().getString(R.string.pet_incentives_continuation))) {
                if (cb.isChecked())
                    incentivesContinuation.setVisibility(View.VISIBLE);
                else
                    incentivesContinuation.setVisibility(View.GONE);
            }

            if (cb.getText().equals(getResources().getString(R.string.pet_other_reason))) {
                if (cb.isChecked())
                    otherReasonForAgreement.setVisibility(View.VISIBLE);
                else
                    otherReasonForAgreement.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (RadioButton rb : intervention.getRadioGroup().getButtons()) {
            if (rb.isChecked()) {
                intervention.getQuestionView().setError(null);
            }
        }
    }

    @Override
    public void refill(int formId) {

        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("INTERVENTION")) {
                for (RadioButton rb : intervention.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.pet)) && obs[0][1].equals("PET")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.sci)) && obs[0][1].equals("SCI")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REFUSAL FOR")) {
                String value = obs[0][1].equals("REFUSED PARTICIPATION IN STUDY") ? getResources().getString(R.string.pet_study_participation) :
                        (obs[0][1].equals("VERBAL SYMPTOM SCREENING") ? getResources().getString(R.string.pet_verbal_symptom_screening) :
                                (obs[0][1].equals("INVESTIGATION") ? getResources().getString(R.string.pet_investigations) :
                                        (obs[0][1].equals("PET INITIATION") ? getResources().getString(R.string.pet_pet_initiation) : getResources().getString(R.string.pet_pet_continuation))));
                refusalFor.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("PET DURATION")) {
                petDuration.getEditText().setText(String.valueOf(obs[0][1]));
                petDuration.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("COUNSELING MODE")) {
                for (CheckBox cb : counselingProvided.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_face_to_face)) && obs[0][1].equals("FACE TO FACE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_on_phone)) && obs[0][1].equals("ON PHONE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_home_visit)) && obs[0][1].equals("HOME VISIT")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("TOTAL NUMBER OF SESSIONS")) {
                totalSession.getEditText().setText(String.valueOf(obs[0][1]));
            } else if (obs[0][0].equals("FAMILY MEMBERS COUNSELLED")) {
                for (CheckBox cb : counselingProvidedTo.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_self)) && obs[0][1].equals("SELF")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_guardian)) && obs[0][1].equals("GUARDIAN")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_mother)) && obs[0][1].equals("MOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_father)) && obs[0][1].equals("FATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_maternal_grandfather)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_maternal_grandmother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_paternal_grandfather)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_paternal_grandmother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_brother)) && obs[0][1].equals("BROTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_sister)) && obs[0][1].equals("SISTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_son)) && obs[0][1].equals("SON")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_daughter)) && obs[0][1].equals("DAUGHTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_spouse)) && obs[0][1].equals("SPOUSE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_uncle)) && obs[0][1].equals("UNCLE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_aunt)) && obs[0][1].equals("AUNT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_complete_family)) && obs[0][1].equals("COMPLETE FAMILY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }

                }
            } else if (obs[0][0].equals("COUNSELING BY")) {
                for (CheckBox cb : counselingProvidedBy.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_counselor)) && obs[0][1].equals("COUNSELOR")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_health_worker)) && obs[0][1].equals("HEALTH WORKER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING TYPE")) {
                for (CheckBox cb : counselingRegarding.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_tb_disease)) && obs[0][1].equals("TUBERCULOSIS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_contact_screening)) && obs[0][1].equals("CONTACT SCREENING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet)) && obs[0][1].equals("PET PROGRAM")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_adherence)) && obs[0][1].equals("ADHERENCE")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUNSELING TECHNIQUE")) {
                for (CheckBox cb : counselingTechnique.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_cost_analysis)) && obs[0][1].equals("COST BENEFIT ANALYSIS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_decatastrophization)) && obs[0][1].equals("DECATASTROPHIZING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_what_if_technique)) && obs[0][1].equals("WHAT IF TECHNIQUE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_worst_possible_outcome)) && obs[0][1].equals("WORST POSSIBLE OUTCOME")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_rogerian_principles)) && obs[0][1].equals("WORKING THROUGH ROGERIAN PRINCIPLES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_downward_arrow)) && obs[0][1].equals("DOWNWARD ARROW TECHNIQUE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_clarification)) && obs[0][1].equals("CLARIFICATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_reflection)) && obs[0][1].equals("REFLECTION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_summarization)) && obs[0][1].equals("SUMMARIZATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_catharsis)) && obs[0][1].equals("CATHARSIS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }

            } else if (obs[0][0].equals("OTHER COUNSELING TECHNIQUE")) {
                otherCounselingTechnique.getEditText().setText(String.valueOf(obs[0][1]));
                otherCounselingTechnique.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR AGREEMENT")) {
                for (CheckBox cb : reasonForAgreement.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_convinced_after_counseling)) && obs[0][1].equals("CONVINCED AFTER COUNSELING")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_self_approach_for_pet)) && obs[0][1].equals("SELF APPROACH FOR PET")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_for_screening_only)) && obs[0][1].equals("FOR SCREENING ONLY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_contact_investigation_only)) && obs[0][1].equals("CONTACT INVESTIGATION ONLY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_duration_reduced)) && obs[0][1].equals("DURATION OF PET REDUCED")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_management_of_adverse_effects_clinically)) && obs[0][1].equals("CLINICALLY MANAGEMENT OF ADVERSE EFFECTS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_management_of_adverse_effects_by_ancillary_drugs)) && obs[0][1].equals("MANAGEMENT OF ADVERSE EFFECTS BY ANCILLARY DRUGS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_information_of_pet_from_external_source)) && obs[0][1].equals("INFORMATION OF PET FROM EXTERNAL SOURCE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_family_diagnosed_other_than_index_case)) && obs[0][1].equals("OTHER THAN INDEX CASE, FAMILY MEMBER DIAGNOSED WITH TB")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_incentives_continuation)) && obs[0][1].equals("INCENTIVE CONTINUATION")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other_reason)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SELF APPROACH FOR PET (TEXT)")) {
                selfApproach.getEditText().setText(String.valueOf(obs[0][1]));
                selfApproach.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CONTACT INVESTIGATION (TEXT)")) {
                contactInvestigationOnly.getEditText().setText(String.valueOf(obs[0][1]));
                contactInvestigationOnly.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INFORMATION OF PET FROM EXTERNAL SOURCE (TEXT)")) {
                informationFromExternalSource.getEditText().setText(String.valueOf(obs[0][1]));
                informationFromExternalSource.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("INCENTIVE CONTINUATION (TEXT)")) {
                incentivesContinuation.getEditText().setText(String.valueOf(obs[0][1]));
                incentivesContinuation.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON FOR AGREEMENT")) {
                otherReasonForAgreement.getEditText().setText(String.valueOf(obs[0][1]));
                otherReasonForAgreement.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                psychologistNotes.getEditText().setText(String.valueOf(obs[0][1]));
            } else if (obs[0][0].equals("SESSION DATE")) {
                datesLinearLayout.setVisibility(View.VISIBLE);

                int j = datesLinearLayout.getChildCount() + 1;
                thirdDateCalender = Calendar.getInstance();
                TitledEditText dateSession = new TitledEditText(context, null, getResources().getString(R.string.pet_session_date) + " " + j, "", "", 50, null, -1, App.VERTICAL, true);
                datesLinearLayout.addView(dateSession);
                dateSession.getEditText().setTag(i);
                dateSession.getEditText().setKeyListener(null);
                dateSession.getEditText().setFocusable(false);
                dateSession.getEditText().setText(String.valueOf(obs[0][1]));
                dateSession.getEditText().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        clickDate = v.getTag().toString();

                        Bundle args = new Bundle();
                        args.putInt("type", THIRD_DIALOG_ID);
                        args.putBoolean("allowPastDate", true);
                        args.putBoolean("allowFutureDate", false);
                        thirdDateFragment.setArguments(args);
                        thirdDateFragment.show(getFragmentManager(), "DatePicker");

                    }
                });


            }

        }

    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            if (getArguments().getInt("type") == DATE_DIALOG_ID)
                calendar = formDateCalendar;
            else if (getArguments().getInt("type") == SECOND_DATE_DIALOG_ID)
                calendar = secondDateCalendar;
            else if (getArguments().getInt("type") == THIRD_DIALOG_ID) {

                for (int i = 0; i < datesLinearLayout.getChildCount(); i++) {

                    TitledEditText v = (TitledEditText) datesLinearLayout.getChildAt(i);
                    String s = v.getEditText().toString();
                    if (v.getEditText().getTag().toString().equals(clickDate)) {
                        String date = v.getEditText().getText().toString();
                        if (!date.equals("")) {
                            Date dateFormat = App.stringToDate(date, "EEEE, MMM dd,yyyy");
                            calendar = Calendar.getInstance();
                            calendar.setTime(dateFormat);
                        }

                        break;
                    }

                }

            } else
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
            else if (((int) view.getTag()) == THIRD_DIALOG_ID)
                thirdDateCalender.set(yy, mm, dd);

            updateDisplay();

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
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

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
 * Created by Rabbia on 11/24/2016.
 */

public class PetRefusalForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
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
    TitledCheckBoxes reasonForRefusal;
    TitledEditText misconception;
    TitledEditText discouragementFromExternalSource;
    TitledEditText discouragementFromInternalSource;
    TitledEditText delayInIncentives;
    TitledEditText otherReasonForRefusal;
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

        pageCount = 6;
        formName = Forms.PET_REFUSAL;
        form = Forms.pet_refusal;

        thirdDateCalender = Calendar.getInstance();
        thirdDateFragment = new SelectDateFragment();

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        otherCounselingTechnique = new TitledEditText(context, null, getResources().getString(R.string.pet_other_counseling_technique), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        otherCounselingTechnique.getEditText().setSingleLine(false);
        otherCounselingTechnique.getEditText().setMinimumHeight(150);
        reasonForRefusal = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_reasons_for_refusal), getResources().getStringArray(R.array.pet_reasons_for_refusal_array), null, App.VERTICAL, App.VERTICAL, true);
        misconception = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_misconception), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        misconception.getEditText().setSingleLine(false);
        misconception.getEditText().setMinimumHeight(150);
        discouragementFromExternalSource = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_discouragment_from_external_source), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        discouragementFromExternalSource.getEditText().setSingleLine(false);
        discouragementFromExternalSource.getEditText().setMinimumHeight(150);
        discouragementFromInternalSource = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_discouragment_from_internal_source), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        discouragementFromInternalSource.getEditText().setSingleLine(false);
        discouragementFromInternalSource.getEditText().setMinimumHeight(150);
        delayInIncentives = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_delay_in_incentives), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        delayInIncentives.getEditText().setSingleLine(false);
        delayInIncentives.getEditText().setMinimumHeight(150);
        otherReasonForRefusal = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_others), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        otherReasonForRefusal.getEditText().setSingleLine(false);
        otherReasonForRefusal.getEditText().setMinimumHeight(150);
        psychologistNotes = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_notes), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        psychologistNotes.getEditText().setSingleLine(false);
        psychologistNotes.getEditText().setMinimumHeight(150);

        views = new View[]{formDate.getButton(), refusalFor.getSpinner(), petDuration.getEditText(), counselingProvided, totalSession.getEditText(), counselingProvidedTo, counselingProvidedBy, counselingRegarding,
                                        counselingTechnique, otherCounselingTechnique.getEditText(), reasonForRefusal, misconception.getEditText(), discouragementFromInternalSource.getEditText(),
                                        discouragementFromExternalSource.getEditText(), delayInIncentives.getEditText(), otherReasonForRefusal.getEditText(), psychologistNotes.getEditText()};

        viewGroups = new View[][]{{formDate, refusalFor, petDuration,counselingProvided},
                                    {totalSession, datesLinearLayout},
                                    {counselingProvidedTo},
                                    {counselingProvidedBy, counselingRegarding, counselingTechnique, otherCounselingTechnique},
                                    {reasonForRefusal},
                                    {misconception, discouragementFromExternalSource, discouragementFromInternalSource,
                                            delayInIncentives, otherReasonForRefusal, psychologistNotes}};

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
        for (CheckBox cb : reasonForRefusal.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

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

                if(App.get(totalSession).equals("") || App.get(totalSession).equals("0"))
                    datesLinearLayout.setVisibility(View.GONE);
                else{
                    datesLinearLayout.setVisibility(View.VISIBLE);
                    int no = Integer.valueOf(App.get(totalSession));
                    if(datesLinearLayout.getChildCount() != no) {

                        if(datesLinearLayout.getChildCount() > no){
                            for (int i = datesLinearLayout.getChildCount(); i > no; i--) {
                                datesLinearLayout.removeViewAt(i - 1);
                            }
                        }
                        else {
                            for (int i = datesLinearLayout.getChildCount(); i < no; i++) {
                                int j = i+1;
                                thirdDateCalender = Calendar.getInstance();
                                TitledEditText dateSession = new TitledEditText(context, null, getResources().getString(R.string.pet_session_date) + " " + j , "", "", 50, null, -1, App.VERTICAL, true);
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
        misconception.setVisibility(View.GONE);
        discouragementFromInternalSource.setVisibility(View.GONE);
        discouragementFromExternalSource.setVisibility(View.GONE);
        delayInIncentives.setVisibility(View.GONE);
        otherReasonForRefusal.setVisibility(View.GONE);
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

    }

    @Override
    public void updateDisplay() {

        if(refillFlag){
            refillFlag = false;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);

        if(clickDate.equals("-1")) {

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
        else {

            for(int i = 0; i< datesLinearLayout.getChildCount(); i++){

                TitledEditText v = (TitledEditText) datesLinearLayout.getChildAt(i);
                String s = v.getEditText().toString();
                if(v.getEditText().getTag().toString().equals(clickDate)){
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

        if (isTitledEditTextEmpty(misconception, 5))
            error = true;
        if (isTitledEditTextEmpty(discouragementFromExternalSource, 5))
            error = true;
        if (isTitledEditTextEmpty(discouragementFromInternalSource, 5))
            error = true;
        if (isTitledEditTextEmpty(delayInIncentives, 5))
            error = true;
        if (isTitledEditTextEmpty(otherReasonForRefusal, 5))
            error = true;

        if (isCheckBoxesChecked(reasonForRefusal, 4))
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
        }
        else{
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
        } else{
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
        observations.add(new String[]{"REFUSAL FOR", App.get(refusalFor).equals(getResources().getString(R.string.pet_study_participation)) ? "REFUSED PARTICIPATION IN STUDY" :
                (App.get(refusalFor).equals(getResources().getString(R.string.pet_verbal_symptom_screening)) ? "VERBAL SYMPTOM SCREENING" :
                        (App.get(refusalFor).equals(getResources().getString(R.string.pet_investigations)) ? "INVESTIGATION" :
                                (App.get(refusalFor).equals(getResources().getString(R.string.pet_pet_initiation)) ? "PET INITIATION" : "PET CONTINUATION")))});
        if(petDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PET DURATION", App.get(petDuration)});
        String counselingProvidedString = "";
        for(CheckBox cb : counselingProvided.getCheckedBoxes()){
            if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_face_to_face)))
                counselingProvidedString = counselingProvidedString + "FACE TO FACE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_on_phone)))
                counselingProvidedString = counselingProvidedString + "ON PHONE" + " ; ";
            else if(cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_home_visit)))
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

        if(otherCounselingTechnique.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER COUNSELING TECHNIQUE", App.get(otherCounselingTechnique)});

        String reasonForRefusalString = "";
        for (CheckBox cb : reasonForRefusal.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_not_contagious)))
                reasonForRefusalString = reasonForRefusalString + "NOT CONTIGUOUS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_due_to_adverse_effects)))
                reasonForRefusalString = reasonForRefusalString + "ADVERSE EFFECT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_high_dose_of_medicines)))
                reasonForRefusalString = reasonForRefusalString + "HIGH DOSE OF MEDICINE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_due_to_misconception)))
                reasonForRefusalString = reasonForRefusalString + "MISCONCEPTIONS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_due_to_any_fear)))
                reasonForRefusalString = reasonForRefusalString + "ANY FEAR OR BELIEF" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_consider_pet_as_insignificant)))
                reasonForRefusalString = reasonForRefusalString + "CONSIDER PET INSIGNIFICANT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_consider_contact_healthy)))
                reasonForRefusalString = reasonForRefusalString + "CONSIDER CONTACT HEALTHY" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_long_duration_of_treatment)))
                reasonForRefusalString = reasonForRefusalString + "LONG DURATION OF TREATMENT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_due_to_schedule)))
                reasonForRefusalString = reasonForRefusalString + "CONTACT SCHEDULE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_due_to_labs)))
                reasonForRefusalString = reasonForRefusalString + "DUE TO LABS" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_can_not_treatment_responsibility)))
                reasonForRefusalString = reasonForRefusalString + "CANNOT TAKE RESPONSIBILITY FOR FAMILY TREATMENT" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_lack_of_tb_information)))
                reasonForRefusalString = reasonForRefusalString + "LACK OF INFORMATION REGARDING TB" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_long_queues_at_hospital)))
                reasonForRefusalString = reasonForRefusalString + "LONG QUEUE AT HOSPITAL" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_discouragement_from_external_source)))
                reasonForRefusalString = reasonForRefusalString + "DISCOURAGEMENT FROM EXTERNAL SOURCES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_discouragement_from_internal_source)))
                reasonForRefusalString = reasonForRefusalString + "DISCOURAGEMENT FROM INTERNAL SOURCES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_delay_in_incentives)))
                reasonForRefusalString = reasonForRefusalString + "DELAY IN INCENTIVES" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other_reason)))
                reasonForRefusalString = reasonForRefusalString + "OTHER" + " ; ";
        }
        observations.add(new String[]{"REFUSAL REASON", reasonForRefusalString});

        if(misconception.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFUSAL DUE TO MISCONCEPTION (TEXT)", App.get(misconception)});

        if(discouragementFromExternalSource.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DETAIL OF DISCOURAGEMENT FROM EXTERNAL SOURCES", App.get(discouragementFromExternalSource)});

        if(discouragementFromInternalSource.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DETAIL OF DISCOURAGEMENT FROM INTERNAL SOURCES", App.get(discouragementFromInternalSource)});

        if(delayInIncentives.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DETAIL OF DELAY IN INCENTIVES", App.get(delayInIncentives)});

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

                String result = serverService.saveEncounterAndObservation(formName, form, formDateCalendar, observations.toArray(new String[][]{}), false);
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

            if(App.get(refusalFor).equals(getResources().getString(R.string.pet_pet_continuation)))
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

        for (CheckBox cb : reasonForRefusal.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.pet_due_to_misconception))) {
                if (cb.isChecked())
                    misconception.setVisibility(View.VISIBLE);
                else
                    misconception.setVisibility(View.GONE);
            }

            if (cb.getText().equals(getResources().getString(R.string.pet_discouragement_from_external_source))) {
                if (cb.isChecked())
                    discouragementFromExternalSource.setVisibility(View.VISIBLE);
                else
                    discouragementFromExternalSource.setVisibility(View.GONE);
            }

            if (cb.getText().equals(getResources().getString(R.string.pet_discouragement_from_internal_source))) {
                if (cb.isChecked())
                    discouragementFromInternalSource.setVisibility(View.VISIBLE);
                else
                    discouragementFromInternalSource.setVisibility(View.GONE);
            }

            if (cb.getText().equals(getResources().getString(R.string.pet_delay_in_incentives))) {
                if (cb.isChecked())
                    delayInIncentives.setVisibility(View.VISIBLE);
                else
                    delayInIncentives.setVisibility(View.GONE);
            }

            if (cb.getText().equals(getResources().getString(R.string.pet_other_reason))){
                if (cb.isChecked())
                    otherReasonForRefusal.setVisibility(View.VISIBLE);
                else
                    otherReasonForRefusal.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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

            if(obs[0][0].equals("TIME TAKEN TO FILL form")){
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("REFUSAL FOR")) {
                String value = obs[0][1].equals("REFUSED PARTICIPATION IN STUDY") ? getResources().getString(R.string.pet_study_participation) :
                        (obs[0][1].equals("VERBAL SYMPTOM SCREENING") ? getResources().getString(R.string.pet_verbal_symptom_screening) :
                                (obs[0][1].equals("INVESTIGATION") ? getResources().getString(R.string.pet_investigations) :
                                        (obs[0][1].equals("PET INITIATION") ? getResources().getString(R.string.pet_pet_initiation) : getResources().getString(R.string.pet_pet_continuation) )));
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
            }  else if (obs[0][0].equals("TOTAL NUMBER OF SESSIONS")) {
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
            } else if (obs[0][0].equals("REFUSAL REASON")) {
                for (CheckBox cb : reasonForRefusal.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.pet_not_contagious)) && obs[0][1].equals("NOT CONTIGUOUS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_due_to_adverse_effects)) && obs[0][1].equals("ADVERSE EFFECT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_high_dose_of_medicines)) && obs[0][1].equals("HIGH DOSE OF MEDICINE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_due_to_misconception)) && obs[0][1].equals("MISCONCEPTIONS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_due_to_any_fear)) && obs[0][1].equals("ANY FEAR OR BELIEF")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_consider_pet_as_insignificant)) && obs[0][1].equals("CONSIDER PET INSIGNIFICANT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_consider_contact_healthy)) && obs[0][1].equals("CONSIDER CONTACT HEALTHY")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_long_duration_of_treatment)) && obs[0][1].equals("LONG DURATION OF TREATMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_due_to_schedule)) && obs[0][1].equals("CONTACT SCHEDULE")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_due_to_labs)) && obs[0][1].equals("DUE TO LABS")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_can_not_treatment_responsibility)) && obs[0][1].equals("CANNOT TAKE RESPONSIBILITY FOR FAMILY TREATMENT")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_lack_of_tb_information)) && obs[0][1].equals("LONG QUEUE AT HOSPITAL")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_discouragement_from_external_source)) && obs[0][1].equals("DISCOURAGEMENT FROM EXTERNAL SOURCES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_discouragement_from_internal_source)) && obs[0][1].equals("DISCOURAGEMENT FROM INTERNAL SOURCES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_delay_in_incentives)) && obs[0][1].equals("DELAY IN INCENTIVES")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.pet_other_reason)) && obs[0][1].equals("OTHER")) {
                        cb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REFUSAL DUE TO MISCONCEPTION (TEXT)")) {
                misconception.getEditText().setText(String.valueOf(obs[0][1]));
                misconception.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DETAIL OF DISCOURAGEMENT FROM EXTERNAL SOURCES")) {
                discouragementFromExternalSource.getEditText().setText(String.valueOf(obs[0][1]));
                discouragementFromExternalSource.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DETAIL OF DISCOURAGEMENT FROM INTERNAL SOURCES")) {
                discouragementFromInternalSource.getEditText().setText(String.valueOf(obs[0][1]));
                discouragementFromInternalSource.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("DETAIL OF DELAY IN INCENTIVES")) {
                delayInIncentives.getEditText().setText(String.valueOf(obs[0][1]));
                delayInIncentives.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REFUSAL REASON")) {
                otherReasonForRefusal.getEditText().setText(String.valueOf(obs[0][1]));
                otherReasonForRefusal.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                psychologistNotes.getEditText().setText(String.valueOf(obs[0][1]));
            } else if (obs[0][0].equals("SESSION DATE")) {
                datesLinearLayout.setVisibility(View.VISIBLE);

                int j = datesLinearLayout.getChildCount() + 1;
                thirdDateCalender = Calendar.getInstance();
                TitledEditText dateSession = new TitledEditText(context, null, getResources().getString(R.string.pet_session_date) + " " + j , "", "", 50, null, -1, App.VERTICAL, true);
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

                for(int i = 0; i< datesLinearLayout.getChildCount(); i++){

                    TitledEditText v = (TitledEditText) datesLinearLayout.getChildAt(i);
                    String s = v.getEditText().toString();
                    if(v.getEditText().getTag().toString().equals(clickDate)){
                        String date = v.getEditText().getText().toString();
                        if(!date.equals("")){
                            Date dateFormat = App.stringToDate(date,"EEEE, MMM dd,yyyy");
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

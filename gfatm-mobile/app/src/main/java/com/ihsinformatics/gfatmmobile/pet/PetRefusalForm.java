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
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
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
        refusalFor = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_refusal_for), getResources().getStringArray(R.array.pet_refusal_for_array), getResources().getString(R.string.pet_study_participation), App.VERTICAL, true,"REFUSAL FOR",new String[]{ "REFUSED PARTICIPATION IN STUDY" , "VERBAL SYMPTOM SCREENING" , "INVESTIGATION" , "PET INITIATION" , "PET CONTINUATION"});
        petDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true,"PET DURATION");
        counselingProvided = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_provided), getResources().getStringArray(R.array.pet_counseling_provided_array), null, App.VERTICAL, App.VERTICAL, true,"COUNSELING MODE",new String[]{ "FACE TO FACE" ,  "ON PHONE" ,  "HOME VISIT"});
        totalSession = new TitledEditText(context, null, getResources().getString(R.string.pet_number_of_counselinng_sessions), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_PHONE, App.VERTICAL, true,"TOTAL NUMBER OF SESSIONS");
        datesLinearLayout = new LinearLayout(context);
        datesLinearLayout.setOrientation(LinearLayout.VERTICAL);
        counselingProvidedTo = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_provided_to), getResources().getStringArray(R.array.pet_counseling_provided_to_array), null, App.VERTICAL, App.VERTICAL, true,"FAMILY MEMBERS COUNSELLED",new String[]{ "SELF" ,  "GUARDIAN" ,  "MOTHER" ,  "FATHER" ,  "MATERNAL GRANDFATHER",  "MATERNAL GRANDMOTHER"  ,  "PATERNAL GRANDFATHER",  "PATERNAL GRANDMOTHER"  ,  "BROTHER" ,  "SISTER" ,  "SON" ,  "DAUGHTER" ,  "SPOUSE" ,  "AUNT" ,  "UNCLE" ,  "COMPLETE FAMILY" ,  "OTHER"});
        counselingProvidedBy = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_provided_by), getResources().getStringArray(R.array.pet_counseling_provided_by_array), null, App.VERTICAL, App.VERTICAL, true,"COUNSELING BY",new String[]{ "COUNSELOR" ,  "HEALTH WORKER"});
        counselingRegarding = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseled_regarding), getResources().getStringArray(R.array.pet_counseled_regarding_array), null, App.VERTICAL, App.VERTICAL, true,"COUNSELING TYPE",new String[]{ "TUBERCULOSIS" ,  "CONTACT SCREENING" ,  "PET PROGRAM" ,  "ADHERENCE"});
        counselingTechnique = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_counseling_technique_used), getResources().getStringArray(R.array.pet_counseling_technique_used_array), null, App.VERTICAL, App.VERTICAL, true,"COUNSELING TECHNIQUE",new String[]{ "COST BENEFIT ANALYSIS" ,  "DECATASTROPHIZING" ,  "WHAT IF TECHNIQUE" ,  "WORST POSSIBLE OUTCOME" ,  "WORKING THROUGH ROGERIAN PRINCIPLES" ,  "DOWNWARD ARROW TECHNIQUE" ,  "CLARIFICATION" ,  "REFLECTION" ,  "SUMMARIZATION" ,  "CATHARSIS" ,  "OTHER"});
        otherCounselingTechnique = new TitledEditText(context, null, getResources().getString(R.string.pet_other_counseling_technique), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER COUNSELING TECHNIQUE");
        otherCounselingTechnique.getEditText().setSingleLine(false);
        otherCounselingTechnique.getEditText().setMinimumHeight(150);
        reasonForRefusal = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_reasons_for_refusal), getResources().getStringArray(R.array.pet_reasons_for_refusal_array), null, App.VERTICAL, App.VERTICAL, true,"REFUSAL REASON",new String[]{ "NOT CONTIGUOUS" ,  "ADVERSE EFFECT" ,  "HIGH DOSE OF MEDICINE" ,  "MISCONCEPTIONS" ,  "ANY FEAR OR BELIEF" ,  "CONSIDER PET INSIGNIFICANT" ,  "CONSIDER CONTACT HEALTHY" ,  "LONG DURATION OF TREATMENT" ,  "CONTACT SCHEDULE" ,  "DUE TO LABS" ,  "CANNOT TAKE RESPONSIBILITY FOR FAMILY TREATMENT" ,  "LACK OF INFORMATION REGARDING TB" ,  "LONG QUEUE AT HOSPITAL" ,  "DISCOURAGEMENT FROM EXTERNAL SOURCES" ,  "DISCOURAGEMENT FROM INTERNAL SOURCES" ,  "DELAY IN INCENTIVES" ,  "OTHER" });
        misconception = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_misconception), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"REFUSAL DUE TO MISCONCEPTION (TEXT)");
        misconception.getEditText().setSingleLine(false);
        misconception.getEditText().setMinimumHeight(150);
        discouragementFromExternalSource = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_discouragment_from_external_source), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"DETAIL OF DISCOURAGEMENT FROM EXTERNAL SOURCES");
        discouragementFromExternalSource.getEditText().setSingleLine(false);
        discouragementFromExternalSource.getEditText().setMinimumHeight(150);
        discouragementFromInternalSource = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_discouragment_from_internal_source), "", "", 100, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"DETAIL OF DISCOURAGEMENT FROM INTERNAL SOURCES");
        discouragementFromInternalSource.getEditText().setSingleLine(false);
        discouragementFromInternalSource.getEditText().setMinimumHeight(150);
        delayInIncentives = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_delay_in_incentives), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"DETAIL OF DELAY IN INCENTIVES");
        delayInIncentives.getEditText().setSingleLine(false);
        delayInIncentives.getEditText().setMinimumHeight(150);
        otherReasonForRefusal = new TitledEditText(context, null, getResources().getString(R.string.pet_explain_others), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        otherReasonForRefusal.getEditText().setSingleLine(false);
        otherReasonForRefusal.getEditText().setMinimumHeight(150);
        psychologistNotes = new TitledEditText(context, null, getResources().getString(R.string.pet_psychologist_notes), "", "", 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"CLINICIAN NOTES (TEXT)");
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
                    TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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

        Boolean error = super.validate();
        View view = null;

        for (int i = 0; i < datesLinearLayout.getChildCount(); i++) {
            if (datesLinearLayout.getChildAt(i) instanceof TitledEditText)
                if (isTitledEditTextEmpty((TitledEditText) datesLinearLayout.getChildAt(i), 1))
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

        final ArrayList<String[]> observations = getObservations();

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

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

                    /*Toast.makeText(context, getString(R.string.form_does_not_exist),
                            Toast.LENGTH_LONG).show();*/

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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally(formName + " Form", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting(formName + " Form", form, formDateCalendar, observations.toArray(new String[][]{}),id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";
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
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar,false,true, false);

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
        super.refill(formId);
        refillFlag = true;

        OfflineForm fo = serverService.getSavedFormById(formId);
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

           if (obs[0][0].equals("SESSION DATE")) {
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

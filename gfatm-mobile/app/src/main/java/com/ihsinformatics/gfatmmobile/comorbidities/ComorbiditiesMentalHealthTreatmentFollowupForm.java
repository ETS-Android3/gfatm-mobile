package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 08-Feb-17.
 */

public class ComorbiditiesMentalHealthTreatmentFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText gpClinicCode;
    TitledEditText treatmentFollowupMHSessionNumber;
    TitledRadioGroup treatmentFollowupMHConditionBeforeSession;
    TitledSpinner treatmentFollowupMHClientsComplaint;
    TitledRadioGroup treatmentFollowupMHCooperation;
    TitledRadioGroup treatmentFollowupMHDefensiveness;
    TitledRadioGroup treatmentFollowupMHDistress;
    TitledRadioGroup treatmentFollowupMHConditionAfterSession;
    TitledRadioGroup treatmentFollowupMHImprovedStatus;
    TitledEditText treatmentFollowupMHAdviceForClient;
    TitledRadioGroup treatmentFollowupMHContinuationStatus;
    TitledSpinner preferredTherapyLocationSpinner;
    TitledButton treatmentFollowupMHNextAppointmentDate;

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
        FORM_NAME = Forms.COMORBIDITIES_TREATMENT_FOLLOWUP_MENTAL_HEALTH_FORM;
        FORM = Forms.comorbidities_treatmentFollowupMHForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesMentalHealthTreatmentFollowupForm.MyAdapter());
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
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        gpClinicCode = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_gp_clinic_code), "", getResources().getString(R.string.comorbidities_preferredlocation_gpcliniccode_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        treatmentFollowupMHSessionNumber = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_session_number), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        treatmentFollowupMHConditionBeforeSession = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_condition_before_session_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_before_session_options_very_bad), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHClientsComplaint = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.comorbidities_treatment_followup_MH_client_complaint), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_client_complaint_options), "", App.HORIZONTAL);
        treatmentFollowupMHCooperation = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_cooperation), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_cooperation_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_cooperation_options_complaint), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHDefensiveness = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_defensiveness_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_defensiveness_options_reserved), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHDistress = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_distress), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_distress_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_distress_options_severly), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHConditionAfterSession = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_after_session), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_condition_after_session_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_condition_after_session_options_very_bad), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHImprovedStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_improved), getResources().getStringArray(R.array.comorbidities_yes_no), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHAdviceForClient = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_advice), "", "", 200, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        treatmentFollowupMHContinuationStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status), getResources().getStringArray(R.array.comorbidities_treatment_followup_MH_continuation_status_options), getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue), App.VERTICAL, App.VERTICAL);
        treatmentFollowupMHNextAppointmentDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_urinedr_nexttestdate), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);

        //For getting the comorbidities location
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
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        preferredTherapyLocationSpinner = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.comorbidities_treatment_followup_MH_preferred_location), locationArray, "", App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), gpClinicCode.getEditText(), treatmentFollowupMHSessionNumber.getEditText(), treatmentFollowupMHConditionBeforeSession.getRadioGroup(),
                treatmentFollowupMHClientsComplaint.getSpinner(), treatmentFollowupMHCooperation.getRadioGroup(), treatmentFollowupMHDefensiveness.getRadioGroup(),
                treatmentFollowupMHDistress.getRadioGroup(), treatmentFollowupMHConditionAfterSession.getRadioGroup(), treatmentFollowupMHImprovedStatus.getRadioGroup(),
                treatmentFollowupMHAdviceForClient.getEditText(), treatmentFollowupMHContinuationStatus.getRadioGroup(), preferredTherapyLocationSpinner.getSpinner(), treatmentFollowupMHNextAppointmentDate.getButton()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, gpClinicCode, treatmentFollowupMHSessionNumber, treatmentFollowupMHConditionBeforeSession,
                        treatmentFollowupMHClientsComplaint, treatmentFollowupMHCooperation, treatmentFollowupMHDefensiveness,
                        treatmentFollowupMHDistress, treatmentFollowupMHConditionAfterSession, treatmentFollowupMHImprovedStatus,
                        treatmentFollowupMHAdviceForClient, treatmentFollowupMHContinuationStatus, preferredTherapyLocationSpinner, treatmentFollowupMHNextAppointmentDate}};

        formDate.getButton().setOnClickListener(this);
        treatmentFollowupMHNextAppointmentDate.getButton().setOnClickListener(this);
        treatmentFollowupMHConditionBeforeSession.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHCooperation.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHDefensiveness.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHDistress.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHConditionAfterSession.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHImprovedStatus.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentFollowupMHContinuationStatus.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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

        if (validate()) {

            resetViews();
        }

        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
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
        } else if (view == treatmentFollowupMHNextAppointmentDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        treatmentFollowupMHNextAppointmentDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        displayPreferredLocationAndNextAppointmentDate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == treatmentFollowupMHContinuationStatus.getRadioGroup()) {
            displayPreferredLocationAndNextAppointmentDate();
        }
    }

    void displayPreferredLocationAndNextAppointmentDate() {

        if (treatmentFollowupMHContinuationStatus.getRadioGroup().getSelectedValue().equalsIgnoreCase(getResources().getString(R.string.comorbidities_treatment_followup_MH_continuation_status_options_continue))) {
            preferredTherapyLocationSpinner.setVisibility(View.VISIBLE);
            treatmentFollowupMHNextAppointmentDate.setVisibility(View.VISIBLE);
        } else {
            preferredTherapyLocationSpinner.setVisibility(View.GONE);
            treatmentFollowupMHNextAppointmentDate.setVisibility(View.GONE);
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






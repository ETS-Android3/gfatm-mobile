package com.ihsinformatics.gfatmmobile.pet;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
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
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetAdverseEventForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledEditText weight;


    TitledRadioGroup dizziness;
    TitledRadioGroup nausea;
    TitledRadioGroup abdominalPain;
    TitledRadioGroup lossOfAppetite;
    TitledRadioGroup jaundice;
    TitledRadioGroup rash;
    TitledRadioGroup tendonPain;
    TitledRadioGroup eyeProblem;
    TitledEditText otherSideEffects;
    TitledRadioGroup sideeffectsConsistent;

    TitledCheckBoxes actionPlan;
    TitledEditText medicationDiscontinueReason;
    TitledEditText medicationDiscontinueDuration;
    TitledEditText newMedication;
    TitledEditText newMedicationDuration;
    TitledRadioGroup petRegimen;
    TitledEditText isoniazidDose;
    TitledEditText rifapentineDose;
    TitledEditText levofloxacinDose;
    TitledEditText ethionamideDose;
    TitledCheckBoxes ancillaryDrugs;
    TitledEditText ancillaryDrugDuration;
    TitledEditText newInstruction;
    TitledButton returnVisitDate;

    Snackbar snackbar;
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 2;
        FORM_NAME = Forms.PET_ADVERSE_EVENTS;
        FORM = Forms.pet_adverseEvents;

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
        weight = new TitledEditText(context, null, getResources().getString(R.string.pet_weight), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_NUMBER_FLAG_DECIMAL, App.HORIZONTAL, true);


        MyLinearLayout linearLayout1 = new MyLinearLayout(context, getResources().getString(R.string.pet_adverse_events_followup_details), App.VERTICAL);
        dizziness = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_dizziness), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        nausea = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_nausea_vomiting), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        abdominalPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_abdominal_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        lossOfAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_loss_of_appetite), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        jaundice = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_jaundice), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        rash = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_rash), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        tendonPain = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tendon_pain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        eyeProblem = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_eye_problems), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        otherSideEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other_side_effects), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherSideEffects.getEditText().setSingleLine(false);
        otherSideEffects.getEditText().setMinimumHeight(150);
        sideeffectsConsistent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_side_effect_consistent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);

        linearLayout1.addView(dizziness);
        linearLayout1.addView(nausea);
        linearLayout1.addView(abdominalPain);
        linearLayout1.addView(lossOfAppetite);
        linearLayout1.addView(jaundice);
        linearLayout1.addView(rash);
        linearLayout1.addView(tendonPain);
        linearLayout1.addView(eyeProblem);
        linearLayout1.addView(otherSideEffects);
        linearLayout1.addView(sideeffectsConsistent);

        MyLinearLayout linearLayout2 = new MyLinearLayout(context, getResources().getString(R.string.pet_symptoms_require), App.VERTICAL);
        actionPlan = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_action_plan), getResources().getStringArray(R.array.pet_action_plan), null, App.VERTICAL, App.VERTICAL, true);
        medicationDiscontinueReason = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_reason), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        medicationDiscontinueReason.getEditText().setSingleLine(false);
        medicationDiscontinueReason.getEditText().setMinimumHeight(150);
        medicationDiscontinueDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_discontinue_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        newMedication = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication_reason), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newMedication.getEditText().setSingleLine(false);
        newMedication.getEditText().setMinimumHeight(150);
        newMedicationDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_new_medication_duration), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        petRegimen = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_regimen), getResources().getStringArray(R.array.pet_regimens), "", App.VERTICAL, App.VERTICAL);
        isoniazidDose = new TitledEditText(context, null, getResources().getString(R.string.pet_isoniazid_dose), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        rifapentineDose = new TitledEditText(context, null, getResources().getString(R.string.pet_rifapentine_dose), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        levofloxacinDose = new TitledEditText(context, null, getResources().getString(R.string.pet_levofloxacin_dose), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        ethionamideDose = new TitledEditText(context, null, getResources().getString(R.string.pet_ethionamide_dose), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        ancillaryDrugs = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_ancillary_drugs), getResources().getStringArray(R.array.pet_ancillary_drugs), null, App.VERTICAL, App.VERTICAL, true);
        ancillaryDrugDuration = new TitledEditText(context, null, getResources().getString(R.string.pet_ancillary_drug_duration_days), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        newInstruction = new TitledEditText(context, null, getResources().getString(R.string.pet_new_instructions), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        newInstruction.getEditText().setSingleLine(false);
        newInstruction.getEditText().setMinimumHeight(150);
        returnVisitDate = new TitledButton(context, null, getResources().getString(R.string.pet_return_visit_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);

        linearLayout2.addView(actionPlan);
        linearLayout2.addView(medicationDiscontinueReason);
        linearLayout2.addView(medicationDiscontinueDuration);
        linearLayout2.addView(newMedication);
        linearLayout2.addView(newMedicationDuration);
        linearLayout2.addView(petRegimen);
        linearLayout2.addView(isoniazidDose);
        linearLayout2.addView(rifapentineDose);
        linearLayout2.addView(levofloxacinDose);
        linearLayout2.addView(ethionamideDose);
        linearLayout2.addView(ancillaryDrugs);
        linearLayout2.addView(ancillaryDrugDuration);
        linearLayout2.addView(newInstruction);
        linearLayout2.addView(returnVisitDate);

        views = new View[]{formDate.getButton(), weight.getEditText(), dizziness.getRadioGroup(), nausea.getRadioGroup(), abdominalPain.getRadioGroup(), lossOfAppetite.getRadioGroup(), jaundice.getRadioGroup(), jaundice.getRadioGroup(), rash.getRadioGroup(),
                tendonPain.getRadioGroup(), eyeProblem.getRadioGroup(), otherSideEffects.getEditText(), sideeffectsConsistent.getRadioGroup(),
                actionPlan, medicationDiscontinueReason.getEditText(), medicationDiscontinueDuration.getEditText(), newMedication.getEditText(), newMedicationDuration.getEditText(),
                petRegimen.getRadioGroup(), isoniazidDose.getEditText(), rifapentineDose.getEditText(), levofloxacinDose.getEditText(), ethionamideDose.getEditText(), ancillaryDrugs, ancillaryDrugDuration,
                newInstruction.getEditText(), returnVisitDate.getButton()
        };

        viewGroups = new View[][]{{formDate, weight, linearLayout1},
                {linearLayout2}};

        formDate.getButton().setOnClickListener(this);
        returnVisitDate.getButton().setOnClickListener(this);
        petRegimen.getRadioGroup().setOnCheckedChangeListener(this);

        medicationDiscontinueReason.setVisibility(View.GONE);
        medicationDiscontinueDuration.setVisibility(View.GONE);
        newMedication.setVisibility(View.GONE);
        newMedicationDuration.setVisibility(View.GONE);
        petRegimen.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ancillaryDrugs.setVisibility(View.GONE);
        ancillaryDrugDuration.setVisibility(View.GONE);

        for (CheckBox cb : actionPlan.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            if (formDateCalendar.after(new Date())) {

                Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }

    }


    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        medicationDiscontinueReason.setVisibility(View.GONE);
        medicationDiscontinueDuration.setVisibility(View.GONE);
        newMedication.setVisibility(View.GONE);
        newMedicationDuration.setVisibility(View.GONE);
        petRegimen.setVisibility(View.GONE);
        isoniazidDose.setVisibility(View.GONE);
        rifapentineDose.setVisibility(View.GONE);
        levofloxacinDose.setVisibility(View.GONE);
        ethionamideDose.setVisibility(View.GONE);
        ancillaryDrugs.setVisibility(View.GONE);
        ancillaryDrugDuration.setVisibility(View.GONE);

    }


    @Override
    public boolean validate() {

        View view = null;
        Boolean error = false;

        if (App.get(newInstruction).isEmpty()) {
            newInstruction.getEditText().setError(getString(R.string.empty_field));
            newInstruction.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else
            newInstruction.clearFocus();

        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE && App.get(ancillaryDrugDuration).isEmpty()) {
            ancillaryDrugDuration.getEditText().setError(getString(R.string.empty_field));
            ancillaryDrugDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else
            ancillaryDrugDuration.clearFocus();

        Boolean flag = false;
        for (CheckBox cb : ancillaryDrugs.getCheckedBoxes()) {
            if (cb.isChecked())
                flag = true;
        }
        if (!flag) {
            ancillaryDrugs.getQuestionView().setError(getString(R.string.empty_field));
            ancillaryDrugs.getQuestionView().requestFocus();
            gotoLastPage();
            view = ancillaryDrugs;
            error = true;
        }

        if (petRegimen.getVisibility() == View.VISIBLE && App.get(petRegimen).isEmpty()) {
            petRegimen.getQuestionView().setError(getString(R.string.empty_field));
            petRegimen.getQuestionView().requestFocus();
            view = petRegimen;
            gotoLastPage();
            error = true;
        }

        if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)) && App.get(isoniazidDose).isEmpty()) {
            isoniazidDose.getEditText().setError(getString(R.string.empty_field));
            isoniazidDose.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

            if (App.get(isoniazidDose).isEmpty()) {
                isoniazidDose.getEditText().setError(getString(R.string.empty_field));
                isoniazidDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            }
            if (App.get(rifapentineDose).isEmpty()) {
                rifapentineDose.getEditText().setError(getString(R.string.empty_field));
                rifapentineDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            }
        } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {
            if (App.get(levofloxacinDose).isEmpty()) {
                levofloxacinDose.getEditText().setError(getString(R.string.empty_field));
                levofloxacinDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            }
            if (App.get(ethionamideDose).isEmpty()) {
                ethionamideDose.getEditText().setError(getString(R.string.empty_field));
                ethionamideDose.getEditText().requestFocus();
                gotoLastPage();
                view = null;
                error = true;
            }
        }

        if (newMedication.getVisibility() == View.VISIBLE && App.get(newMedication).isEmpty()) {
            newMedication.getEditText().setError(getString(R.string.empty_field));
            newMedication.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }

        if (newMedicationDuration.getVisibility() == View.VISIBLE && App.get(newMedicationDuration).isEmpty()) {
            newMedicationDuration.getEditText().setError(getString(R.string.empty_field));
            newMedicationDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }

        if (medicationDiscontinueReason.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueReason).isEmpty()) {
            medicationDiscontinueReason.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueReason.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }

        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE && App.get(medicationDiscontinueDuration).isEmpty()) {
            medicationDiscontinueDuration.getEditText().setError(getString(R.string.empty_field));
            medicationDiscontinueDuration.getEditText().requestFocus();
            gotoLastPage();
            view = null;
            error = true;
        }

        if (App.get(weight).isEmpty()) {
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            gotoLastPage();
            view = null;
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
                                        newInstruction.clearFocus();
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

        final ContentValues values = new ContentValues();
        values.put("formDate", App.getSqlDate(formDateCalendar));
        // start time...
        // end time...
        // gps coordinate...

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"DIZZINESS AND GIDDINESS", App.get(dizziness).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"NAUSEA AND VOMITING", App.get(nausea).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"ABDOMINAL PAIN", App.get(abdominalPain).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"LOSS OF APPETITE", App.get(lossOfAppetite).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"JAUNDICE", App.get(jaundice).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"RASH", App.get(rash).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"MUSCLE PAIN", App.get(tendonPain).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"VISION PROBLEM", App.get(eyeProblem).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"OTHER ADVERSE EVENT", App.get(otherSideEffects)});
        observations.add(new String[]{"CONSISTENT SIDE EFFECTS", App.get(sideeffectsConsistent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        String actionPlanString = "";
        for (CheckBox cb : actionPlan.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_continue_medication_greater_adherence)))
                actionPlanString = actionPlanString + "CONTINUE MEDICATION (HIGH ADHERENCE)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_continue_medication_less_adherence)))
                actionPlanString = actionPlanString + "CONTINUE MEDICATION (LOW ADHERENCE)" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_discontinue_medication)))
                actionPlanString = actionPlanString + "DISCONTINUE MEDICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_begin_cxr_protocol)))
                actionPlanString = actionPlanString + "BEGIN CLINICAL MONITORING PROTOCOL" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_new_medication)))
                actionPlanString = actionPlanString + "GIVE NEW MEDICATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_seek_expert_consultation)))
                actionPlanString = actionPlanString + "SEEK EXPERT CONSULTATION" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_change_drug_dosage)))
                actionPlanString = actionPlanString + "CHANGE DRUG DOSAGE" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_ancillary_drug)))
                actionPlanString = actionPlanString + "GIVE ANCILLARY DRUG" + " ; ";
        }
        observations.add(new String[]{"ACTION PLAN", actionPlanString});
        if (medicationDiscontinueReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON TO DISCONTINUE MEDICATION", App.get(medicationDiscontinueReason)});
        if (medicationDiscontinueDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DURATION OF DISCONTINUATION IN DAYS", App.get(medicationDiscontinueDuration)});
        if (newMedication.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"NEW MEDICATION", App.get(newMedication)});
        if (newMedicationDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DURATION OF NEW MEDICATION IN DAYS", App.get(newMedicationDuration)});

        // REGIMEN...

        if (isoniazidDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ISONIAZID DOSE", App.get(isoniazidDose)});
        if (rifapentineDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"RIFAPENTINE DOSE", App.get(rifapentineDose)});
        if (levofloxacinDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LEVOFLOXACIN DOSE", App.get(levofloxacinDose)});
        if (ethionamideDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ETHIONAMIDE DOSE", App.get(ethionamideDose)});
        if (isoniazidDose.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ISONIAZID DOSE", App.get(isoniazidDose)});
        if (ancillaryDrugs.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ANCILLARY DRUGS", App.get(ancillaryDrugs).equals(getResources().getString(R.string.pet_iron_deficiency_prtocol)) ? "IRON" :
                    (App.get(ancillaryDrugs).equals(getResources().getString(R.string.pet_vitamin_d_protocol)) ? "VITAMIN D" : "CHLORPHENIRAMINE / METHSCOPOLAMINE / PHENYLEPHRINE")});
        if (ancillaryDrugDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"MEDICATION DURATION", App.get(ancillaryDrugDuration)});
        observations.add(new String[]{"INSTRUCTIONS TO PATIENT AND/OR FAMILY", App.get(newInstruction)});
        observations.add(new String[]{"RETURN VISIT DATE", App.getSqlDate(secondDateCalendar)});

        AsyncTask<String, String, String> submissionFormTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.signing_in));
                        loading.show();
                    }
                });

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
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

                Toast toast = Toast.makeText(context, result, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();

            }
        };
        submissionFormTask.execute("");

        resetViews();
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
        } else if (view == returnVisitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", true);
            args.putBoolean("allowPastDate", false);
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

        for (CheckBox cb : actionPlan.getCheckedBoxes()) {

            if (App.get(cb).equals(getResources().getString(R.string.pet_discontinue_medication))) {
                if (cb.isChecked()) {
                    medicationDiscontinueReason.setVisibility(View.VISIBLE);
                    medicationDiscontinueDuration.setVisibility(View.VISIBLE);
                } else {
                    medicationDiscontinueReason.setVisibility(View.GONE);
                    medicationDiscontinueDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_new_medication))) {
                if (cb.isChecked()) {
                    newMedication.setVisibility(View.VISIBLE);
                    newMedicationDuration.setVisibility(View.VISIBLE);
                } else {
                    newMedication.setVisibility(View.GONE);
                    newMedicationDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_ancillary_drug))) {
                if (cb.isChecked()) {
                    ancillaryDrugs.setVisibility(View.VISIBLE);
                    ancillaryDrugDuration.setVisibility(View.VISIBLE);
                } else {
                    ancillaryDrugs.setVisibility(View.GONE);
                    ancillaryDrugDuration.setVisibility(View.GONE);
                }
            }

            if (App.get(cb).equals(getResources().getString(R.string.pet_change_drug_dosage))) {
                if (cb.isChecked()) {
                    petRegimen.setVisibility(View.VISIBLE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                    if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy)))
                        isoniazidDose.setVisibility(View.VISIBLE);
                    else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {
                        isoniazidDose.setVisibility(View.VISIBLE);
                        rifapentineDose.setVisibility(View.VISIBLE);
                    } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {
                        levofloxacinDose.setVisibility(View.VISIBLE);
                        ethionamideDose.setVisibility(View.VISIBLE);
                    }

                } else {
                    petRegimen.setVisibility(View.GONE);
                    isoniazidDose.setVisibility(View.GONE);
                    rifapentineDose.setVisibility(View.GONE);
                    levofloxacinDose.setVisibility(View.GONE);
                    ethionamideDose.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == petRegimen.getRadioGroup()) {

            int age = App.getPatient().getPerson().getAge();

            if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_prophylaxis_therapy))) {

                if (age < 12) isoniazidDose.getEditText().setText("15");
                else isoniazidDose.getEditText().setText("25");

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_isoniazid_rifapentine))) {

                if (age < 12) isoniazidDose.getEditText().setText("15");
                else isoniazidDose.getEditText().setText("25");

                if (age < 2) rifapentineDose.getEditText().setText("");
                else if (age >= 2 && age < 12) rifapentineDose.getEditText().setText("300");
                else rifapentineDose.getEditText().setText("450");

                isoniazidDose.setVisibility(View.VISIBLE);
                rifapentineDose.setVisibility(View.VISIBLE);
                levofloxacinDose.setVisibility(View.GONE);
                ethionamideDose.setVisibility(View.GONE);

            } else if (App.get(petRegimen).equals(getResources().getString(R.string.pet_levofloxacin_ethionamide))) {

                if (age < 2) levofloxacinDose.getEditText().setText("15");
                else if (age < 15) levofloxacinDose.getEditText().setText("7.5");
                else levofloxacinDose.getEditText().setText("750");

                if (age < 15) ethionamideDose.getEditText().setText("15");
                else ethionamideDose.getEditText().setText("500");

                isoniazidDose.setVisibility(View.GONE);
                rifapentineDose.setVisibility(View.GONE);
                levofloxacinDose.setVisibility(View.VISIBLE);
                ethionamideDose.setVisibility(View.VISIBLE);

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

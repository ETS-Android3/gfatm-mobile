package com.ihsinformatics.gfatmmobile.childhoodTb;

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
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
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
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbPresumptiveCaseConfirmation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledEditText weight;
    TitledEditText height;
    TitledSpinner weightPercentile;
    TitledRadioGroup cough;
    TitledSpinner coughDuration;
    TitledRadioGroup fever;
    TitledRadioGroup nightSweats;
    TitledRadioGroup weightLoss;
    TitledRadioGroup appetite;
    TitledRadioGroup generalAppearance;
    TitledEditText generalAppearanceExplanation;
    TitledRadioGroup headEyeEearNoseThroat;
    TitledEditText headEyeEearNoseThroatExplanation;
    TitledRadioGroup lymphNodeExamination;
    TitledEditText lymphNodeExplanation;
    TitledRadioGroup spineExamination;
    TitledEditText spineExplanation;
    TitledRadioGroup jointsExamination;
    TitledEditText jointsExplanation;
    TitledRadioGroup skinExamination;
    TitledEditText skinExplanation;
    TitledRadioGroup chestExamination;
    TitledEditText chestExplanation;
    TitledRadioGroup abdominalExamination;
    TitledEditText abdominalExplanation;
    TitledEditText othersExplanation;
    TitledRadioGroup tbExamOutcome;
    TitledRadioGroup bcgScar;
    TitledRadioGroup tbBefore;
    TitledRadioGroup tbMedication;
    TitledRadioGroup contactTbHistory;
    TitledCheckBoxes closeContactType;
    TitledEditText otherContactType;
    TitledEditText additionalCommentHistoryOfPatient;
    TitledRadioGroup tbInfectionForm;
    TitledRadioGroup tbType;
    TitledRadioGroup smearPositive;
    TitledRadioGroup childPrimaryCaregiver;
    TitledRadioGroup sameBedAsChild;
    TitledRadioGroup sameRoomRAsChild;
    TitledRadioGroup liveInSameHoushold;
    TitledRadioGroup seeChildEveryday;
    TitledRadioGroup contactCoughing;
    TitledRadioGroup oneCloseContactInHousehold;
    TitledRadioGroup conclusion;
    TitledEditText doctorNotes;

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
        FORM_NAME = Forms.CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION;
        FORM = Forms.childhoodTb_presumptive_case_confirmation;

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
        weight = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight), "", "", 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        height = new TitledEditText(context, null, getResources().getString(R.string.ctb_height), "", "", 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        weightPercentile = new TitledSpinner(context, null, getResources().getString(R.string.ctb_weight_percentile), getResources().getStringArray(R.array.ctb_weight_percentile_list), null, App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        coughDuration = new TitledSpinner(context, null, getResources().getString(R.string.ctb_cough_duration), getResources().getStringArray(R.array.ctb_cough_duration_list), null, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        appetite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        generalAppearance = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_general_appearance), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        generalAppearanceExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        headEyeEearNoseThroat = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_head_eye_ear_nose_throat), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        headEyeEearNoseThroatExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        lymphNodeExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_lymphnode), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        lymphNodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        spineExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_spine), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        jointsExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_joints), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        skinExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_skin), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        chestExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_chest), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        abdominalExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_abdominal), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), getResources().getString(R.string.ctb_unremarkable), App.HORIZONTAL, App.VERTICAL, true);
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        othersExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_title), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        bcgScar = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_bcg), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);
        tbExamOutcome = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_exam_outcome), getResources().getStringArray(R.array.ctb_tb_exam_outcome_list), getResources().getString(R.string.ctb_not_sugguestive_tb), App.VERTICAL, App.VERTICAL, true);
        tbBefore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_before), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        tbMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_medication), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);

        contactTbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL, true);
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL, App.VERTICAL, true);

        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_title), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        additionalCommentHistoryOfPatient = new TitledEditText(context, null, getResources().getString(R.string.ctb_additional_comments_contact_history), "", "", 500, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);

        tbInfectionForm = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_infection_form), getResources().getStringArray(R.array.ctb_tb_form_list), getResources().getString(R.string.unknown), App.HORIZONTAL, App.VERTICAL, true);

        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_type), getResources().getStringArray(R.array.ctb_tb_type_list), getResources().getString(R.string.unknown), App.VERTICAL, App.VERTICAL, true);

        smearPositive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_smear_positive), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        childPrimaryCaregiver = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_primary_caregiver), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        sameBedAsChild = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_sleep_same_bed), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        sameRoomRAsChild = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_sleep_same_room), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        liveInSameHoushold = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_live_in_same_household), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        seeChildEveryday = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_see_child_everyday), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        contactCoughing = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_contact_coughing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        oneCloseContactInHousehold = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_contact_in_child_household), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL, true);

        conclusion = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_conclusion), getResources().getStringArray(R.array.ctb_conclusion_list), getResources().getString(R.string.ctb_tb_presumptive_confirmed), App.HORIZONTAL, App.VERTICAL, true);
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);


        views = new View[]{formDate.getButton(), weightPercentile.getSpinner(), cough.getRadioGroup(), coughDuration.getSpinner(), fever.getRadioGroup(),
                nightSweats.getRadioGroup(), weightLoss.getRadioGroup(), appetite.getRadioGroup(), generalAppearance.getRadioGroup(),
                headEyeEearNoseThroat.getRadioGroup(), lymphNodeExamination.getRadioGroup(), spineExamination.getRadioGroup(),
                jointsExamination.getRadioGroup(), skinExamination.getRadioGroup(), chestExamination.getRadioGroup(), abdominalExamination.getRadioGroup(),
                tbExamOutcome.getRadioGroup(), bcgScar.getRadioGroup(), bcgScar.getRadioGroup(), tbBefore.getRadioGroup(), tbMedication.getRadioGroup(),
                contactTbHistory.getRadioGroup(), tbInfectionForm.getRadioGroup(), tbType.getRadioGroup(), smearPositive.getRadioGroup(),
                childPrimaryCaregiver.getRadioGroup(), sameBedAsChild.getRadioGroup(), sameRoomRAsChild.getRadioGroup(), liveInSameHoushold.getRadioGroup(),
                seeChildEveryday.getRadioGroup(), contactCoughing.getRadioGroup(), oneCloseContactInHousehold.getRadioGroup(), conclusion.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, weight, height, weightPercentile, cough, coughDuration, fever, nightSweats, weightLoss, appetite, generalAppearance, generalAppearanceExplanation, headEyeEearNoseThroat, headEyeEearNoseThroatExplanation, lymphNodeExamination, lymphNodeExplanation, spineExamination, spineExplanation,
                        jointsExamination, jointsExplanation, skinExamination, skinExplanation, chestExamination, chestExplanation, abdominalExamination, abdominalExplanation, othersExplanation, tbExamOutcome, bcgScar, tbBefore, tbMedication, contactTbHistory, closeContactType, otherContactType, additionalCommentHistoryOfPatient, tbInfectionForm, tbType, smearPositive, childPrimaryCaregiver
                        , sameBedAsChild, sameRoomRAsChild, liveInSameHoushold, seeChildEveryday, contactCoughing, oneCloseContactInHousehold, conclusion, doctorNotes
                }};

        formDate.getButton().setOnClickListener(this);
        weightPercentile.getSpinner().setOnItemSelectedListener(this);
        cough.getRadioGroup().setOnCheckedChangeListener(this);
        coughDuration.getSpinner().setOnItemSelectedListener(this);
        fever.getRadioGroup().setOnCheckedChangeListener(this);
        nightSweats.getRadioGroup().setOnCheckedChangeListener(this);
        weightLoss.getRadioGroup().setOnCheckedChangeListener(this);
        appetite.getRadioGroup().setOnCheckedChangeListener(this);
        generalAppearance.getRadioGroup().setOnCheckedChangeListener(this);
        headEyeEearNoseThroat.getRadioGroup().setOnCheckedChangeListener(this);
        lymphNodeExamination.getRadioGroup().setOnCheckedChangeListener(this);
        spineExamination.getRadioGroup().setOnCheckedChangeListener(this);
        jointsExamination.getRadioGroup().setOnCheckedChangeListener(this);
        skinExamination.getRadioGroup().setOnCheckedChangeListener(this);
        chestExamination.getRadioGroup().setOnCheckedChangeListener(this);
        abdominalExamination.getRadioGroup().setOnCheckedChangeListener(this);
        tbExamOutcome.getRadioGroup().setOnCheckedChangeListener(this);
        bcgScar.getRadioGroup().setOnCheckedChangeListener(this);
        tbBefore.getRadioGroup().setOnCheckedChangeListener(this);
        tbMedication.getRadioGroup().setOnCheckedChangeListener(this);
        contactTbHistory.getRadioGroup().setOnCheckedChangeListener(this);
        tbInfectionForm.getRadioGroup().setOnCheckedChangeListener(this);
        tbType.getRadioGroup().setOnCheckedChangeListener(this);
        smearPositive.getRadioGroup().setOnCheckedChangeListener(this);
        childPrimaryCaregiver.getRadioGroup().setOnCheckedChangeListener(this);
        sameBedAsChild.getRadioGroup().setOnCheckedChangeListener(this);
        sameRoomRAsChild.getRadioGroup().setOnCheckedChangeListener(this);
        liveInSameHoushold.getRadioGroup().setOnCheckedChangeListener(this);
        seeChildEveryday.getRadioGroup().setOnCheckedChangeListener(this);
        contactCoughing.getRadioGroup().setOnCheckedChangeListener(this);
        oneCloseContactInHousehold.getRadioGroup().setOnCheckedChangeListener(this);
        conclusion.getRadioGroup().setOnCheckedChangeListener(this);

        ArrayList<MyCheckBox> checkBoxList = closeContactType.getCheckedBoxes();
        for (CheckBox cb : closeContactType.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (formDateCalendar.after(date)) {

                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }


    }

    @Override
    public boolean validate() {
        boolean error = false;
        if (App.get(weight).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            weight.getEditText().setError(getString(R.string.empty_field));
            weight.getEditText().requestFocus();
            error = true;
        }
        if (App.get(height).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            height.getEditText().setError(getString(R.string.empty_field));
            height.getEditText().requestFocus();
            error = true;
        }
        if (generalAppearanceExplanation.getVisibility() == View.VISIBLE && App.get(generalAppearanceExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            generalAppearanceExplanation.getEditText().setError(getString(R.string.empty_field));
            generalAppearanceExplanation.getEditText().requestFocus();
            error = true;
        }
        if (headEyeEearNoseThroatExplanation.getVisibility() == View.VISIBLE && App.get(headEyeEearNoseThroatExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            headEyeEearNoseThroatExplanation.getEditText().setError(getString(R.string.empty_field));
            headEyeEearNoseThroatExplanation.getEditText().requestFocus();
            error = true;
        }
        if (lymphNodeExplanation.getVisibility() == View.VISIBLE && App.get(lymphNodeExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            lymphNodeExplanation.getEditText().setError(getString(R.string.empty_field));
            lymphNodeExplanation.getEditText().requestFocus();
            error = true;
        }
        if (spineExplanation.getVisibility() == View.VISIBLE && App.get(spineExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            spineExplanation.getEditText().setError(getString(R.string.empty_field));
            spineExplanation.getEditText().requestFocus();
            error = true;
        }
        if (jointsExplanation.getVisibility() == View.VISIBLE && App.get(jointsExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            jointsExplanation.getEditText().setError(getString(R.string.empty_field));
            jointsExplanation.getEditText().requestFocus();
            error = true;
        }
        if (skinExplanation.getVisibility() == View.VISIBLE && App.get(skinExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            skinExplanation.getEditText().setError(getString(R.string.empty_field));
            skinExplanation.getEditText().requestFocus();
            error = true;
        }
        if (chestExplanation.getVisibility() == View.VISIBLE && App.get(chestExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            chestExplanation.getEditText().setError(getString(R.string.empty_field));
            chestExplanation.getEditText().requestFocus();
            error = true;
        }
        if (abdominalExplanation.getVisibility() == View.VISIBLE && App.get(abdominalExplanation).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            abdominalExplanation.getEditText().setError(getString(R.string.empty_field));
            abdominalExplanation.getEditText().requestFocus();
            error = true;
        }

        if (otherContactType.getVisibility() == View.VISIBLE && App.get(otherContactType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherContactType.getEditText().setError(getString(R.string.empty_field));
            otherContactType.getEditText().requestFocus();
            error = true;
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
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"WEIGHT (KG)", App.get(weight)});
        observations.add(new String[]{"HEIGHT (CM)", App.get(height)});
        observations.add(new String[]{"WEIGHT PERCENTILE", App.get(weightPercentile)});
        observations.add(new String[]{"COUGH", App.get(cough).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(cough).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(cough).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        if (coughDuration.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"COUGH DURATION", App.get(coughDuration).equals(getResources().getString(R.string.ctb_less_than_2_weeks)) ? "COUGH LASTING LESS THAN 2 WEEKS (163739)" :
                    (App.get(coughDuration).equals(getResources().getString(R.string.ctb_2_to_3_weeks)) ? "COUGH LASTING MORE THAN 2 WEEKS" :
                            (App.get(coughDuration).equals(getResources().getString(R.string.ctb_more_than_3_weeks)) ? "COUGH LASTING MORE THAN 3 WEEKS" :
                                    (App.get(coughDuration).equals(getResources().getString(R.string.unknown)) ? "UNKNOWN" : "REFUSED")))});

        observations.add(new String[]{"FEVER", App.get(fever).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(fever).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(fever).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"NIGHT SWEATS", App.get(nightSweats).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(nightSweats).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(nightSweats).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"WEIGHT LOSS", App.get(weightLoss).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(weightLoss).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(weightLoss).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        observations.add(new String[]{"APPETITE", App.get(appetite).equals(getResources().getString(R.string.ctb_poor)) ? "POOR APPETITE" :
                (App.get(appetite).equals(getResources().getString(R.string.ctb_ok)) ? "OK" :
                        (App.get(appetite).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        if(generalAppearanceExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"GENERAL APPEARANCE EXPLANATION", App.get(generalAppearanceExplanation)});
        }
        if(headEyeEearNoseThroatExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"HEAD, EARS, EYES, NOSE AND THROAT DESCRIPTION", App.get(headEyeEearNoseThroatExplanation)});
        }
        if(lymphNodeExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN", App.get(lymphNodeExplanation)});
        }
        if(jointsExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"JOINTS PHYSICAL EXAMINATION (TEXT)", App.get(jointsExplanation)});
        }
        if(skinExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"SKIN EXAMINATION (TEXT)", App.get(skinExplanation)});
        }
        if(chestExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"CHEST EXAMINATION (TEXT)", App.get(chestExplanation)});
        }
        if(abdominalExplanation.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ABDOMINAL EXAMINATION (TEXT)", App.get(abdominalExplanation)});
        }
        if(!App.get(othersExplanation).isEmpty()){
            observations.add(new String[]{"FREE TEXT COMMENT", App.get(othersExplanation)});
        }

        observations.add(new String[]{"TUBERCULOSIS PHYSICAL EXAM OUTCOME", App.get(tbExamOutcome).equals(getResources().getString(R.string.ctb_suggestive_tb)) ? "SUGGESTIVE OF TB" :
                (App.get(tbExamOutcome).equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) ? "STRONGLY SUGGESTIVE OF TB" :
                         "NO TB INDICATION")});

        observations.add(new String[]{"BACILLUS CALMETTE–GUÉRIN VACCINE", App.get(bcgScar).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(bcgScar).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(bcgScar).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});


        observations.add(new String[]{"HISTORY OF TUBERCULOSIS", App.get(tbBefore).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(tbBefore).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(tbBefore).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        if(tbMedication.getVisibility()==View.VISIBLE) {
            observations.add(new String[]{"PATIENT TAKEN TB MEDICATION BEFORE", App.get(tbMedication).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(tbMedication).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(tbMedication).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        }
        observations.add(new String[]{"PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS", App.get(contactTbHistory).equals(getResources().getString(R.string.yes)) ? "YES" :
                (App.get(contactTbHistory).equals(getResources().getString(R.string.no)) ? "NO" :
                        (App.get(contactTbHistory).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});

        String closeContactString = "";
        for (CheckBox cb : closeContactType.getCheckedBoxes()) {
            if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_mother)))
                closeContactString = closeContactString + "MOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_father)))
                closeContactString = closeContactString + "FATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_brother)))
                closeContactString = closeContactString + "BROTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_sister)))
                closeContactString = closeContactString + "SISTER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandfather)))
                closeContactString = closeContactString + "PATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandmother)))
                closeContactString = closeContactString + "PATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandfather)))
                closeContactString = closeContactString + "MATERNAL GRANDFATHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandmother)))
                closeContactString = closeContactString + "MATERNAL GRANDMOTHER" + " ; ";
            else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.ctb_other_title)))
                closeContactString = closeContactString + "OTHER CONTACT TYPE" + " ; ";
        }
        observations.add(new String[]{"CLOSE CONTACT WITH PATIENT", closeContactString});

        if (otherContactType.getVisibility() == View.VISIBLE){
            observations.add(new String[]{"OTHER CONTACT TYPE", App.get(otherContactType)});
        }

        if(additionalCommentHistoryOfPatient.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"ADDITIONAL COMMENTS REGARDING CONTACT HISTORY OF THE PATIENT", App.get(additionalCommentHistoryOfPatient)});
        }
        if(tbInfectionForm.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"TUBERCULOSIS INFECTION TYPE", App.get(tbInfectionForm).equals(getResources().getString(R.string.ctb_susceptible_tb)) ? "DRUG-SENSITIVE TUBERCULOSIS INFECTION" :
                    (App.get(tbInfectionForm).equals(getResources().getString(R.string.ctb_dr_tb)) ? "DRUG-RESISTANT TB" :
                            (App.get(tbInfectionForm).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }
        if(tbType.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"SITE OF TUBERCULOSIS DISEASE", App.get(tbType).equals(getResources().getString(R.string.ctb_pulmonary)) ? "PULMONARY TUBERCULOSIS" :
                    (App.get(tbType).equals(getResources().getString(R.string.ctb_extra_pulmonary)) ? "EXTRA-PULMONARY TUBERCULOSIS" :
                            (App.get(tbType).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(smearPositive.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"SMEAR POSITIVE TUBERCULOSIS INFECTION", App.get(smearPositive).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(smearPositive).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(smearPositive).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(childPrimaryCaregiver.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"INDEX CASE PRIMARY CARETAKER OF CONTACT", App.get(childPrimaryCaregiver).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(childPrimaryCaregiver).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(childPrimaryCaregiver).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(sameBedAsChild.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"INDEX CASE SHARES BED WITH CONTACT", App.get(sameBedAsChild).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(sameBedAsChild).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(sameBedAsChild).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(sameRoomRAsChild.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"INDEX CASE SHARES BEDROOM WITH CONTACT", App.get(sameRoomRAsChild).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(sameRoomRAsChild).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(sameRoomRAsChild).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(liveInSameHoushold.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"INDEX CASE LIVES WITH CONTACT", App.get(liveInSameHoushold).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(liveInSameHoushold).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(liveInSameHoushold).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(seeChildEveryday.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"INDEX CASE MEETS CONTACT DAILY", App.get(seeChildEveryday).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(seeChildEveryday).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(seeChildEveryday).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(contactCoughing.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"INDEX CASE COUGHING", App.get(contactCoughing).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(contactCoughing).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(contactCoughing).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        if(oneCloseContactInHousehold.getVisibility()==View.VISIBLE){
            observations.add(new String[]{"MULTIPLE INDEX CASES IN HOUSEHOLD", App.get(oneCloseContactInHousehold).equals(getResources().getString(R.string.yes)) ? "YES" :
                    (App.get(oneCloseContactInHousehold).equals(getResources().getString(R.string.no)) ? "NO" :
                            (App.get(oneCloseContactInHousehold).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "UNKNOWN"))});
        }

        observations.add(new String[]{"CONCLUSION", App.get(conclusion).equals(getResources().getString(R.string.ctb_tb_presumptive_confirmed)) ? "TB PRESUMPTIVE CONFIRMED" : "NOT A TB PRESUMPTIVE" });

        if(!App.get(doctorNotes).isEmpty()){
            observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(doctorNotes)});
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

                String result = serverService.saveEncounterAndObservation("Presumptive Case Confirmation", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
    public void refill(int encounterId) {

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


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (CheckBox cb : closeContactType.getCheckedBoxes()) {
            if (App.get(cb).equals(getResources().getString(R.string.ctb_other_title))) {
                if (cb.isChecked()) {
                    otherContactType.setVisibility(View.VISIBLE);
                } else {
                    otherContactType.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        coughDuration.setVisibility(View.GONE);
        generalAppearanceExplanation.setVisibility(View.GONE);
        headEyeEearNoseThroatExplanation.setVisibility(View.GONE);
        lymphNodeExplanation.setVisibility(View.GONE);
        spineExplanation.setVisibility(View.GONE);
        jointsExplanation.setVisibility(View.GONE);
        skinExplanation.setVisibility(View.GONE);
        chestExplanation.setVisibility(View.GONE);
        abdominalExplanation.setVisibility(View.GONE);
        tbMedication.setVisibility(View.GONE);
        closeContactType.setVisibility(View.GONE);
        otherContactType.setVisibility(View.GONE);
        additionalCommentHistoryOfPatient.setVisibility(View.GONE);
        tbInfectionForm.setVisibility(View.GONE);
        tbType.setVisibility(View.GONE);
        smearPositive.setVisibility(View.GONE);
        childPrimaryCaregiver.setVisibility(View.GONE);
        sameBedAsChild.setVisibility(View.GONE);
        sameRoomRAsChild.setVisibility(View.GONE);
        liveInSameHoushold.setVisibility(View.GONE);
        seeChildEveryday.setVisibility(View.GONE);
        contactCoughing.setVisibility(View.GONE);
        oneCloseContactInHousehold.setVisibility(View.GONE);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == cough.getRadioGroup()) {
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
            }
        } else if (group == generalAppearance.getRadioGroup()) {
            if (generalAppearance.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                generalAppearanceExplanation.setVisibility(View.VISIBLE);
            } else {
                generalAppearanceExplanation.setVisibility(View.GONE);
            }
        } else if (group == headEyeEearNoseThroat.getRadioGroup()) {
            if (headEyeEearNoseThroat.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                headEyeEearNoseThroatExplanation.setVisibility(View.VISIBLE);
            } else {
                headEyeEearNoseThroatExplanation.setVisibility(View.GONE);
            }
        } else if (group == lymphNodeExamination.getRadioGroup()) {
            if (lymphNodeExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                lymphNodeExplanation.setVisibility(View.VISIBLE);
            } else {
                lymphNodeExplanation.setVisibility(View.GONE);
            }
        } else if (group == spineExamination.getRadioGroup()) {
            if (spineExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                spineExplanation.setVisibility(View.VISIBLE);
            } else {
                spineExplanation.setVisibility(View.GONE);
            }
        } else if (group == jointsExamination.getRadioGroup()) {
            if (jointsExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                jointsExplanation.setVisibility(View.VISIBLE);
            } else {
                jointsExplanation.setVisibility(View.GONE);
            }
        } else if (group == skinExamination.getRadioGroup()) {
            if (skinExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                skinExplanation.setVisibility(View.VISIBLE);
            } else {
                skinExplanation.setVisibility(View.GONE);
            }
        } else if (group == chestExamination.getRadioGroup()) {
            if (chestExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                chestExplanation.setVisibility(View.VISIBLE);
            } else {
                chestExplanation.setVisibility(View.GONE);
            }
        } else if (group == abdominalExamination.getRadioGroup()) {
            if (abdominalExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                abdominalExplanation.setVisibility(View.VISIBLE);
            } else {
                abdominalExplanation.setVisibility(View.GONE);
            }
        } else if (group == tbBefore.getRadioGroup()) {
            if (tbBefore.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbMedication.setVisibility(View.VISIBLE);

            } else {
                tbMedication.setVisibility(View.GONE);

            }
        } else if (group == contactTbHistory.getRadioGroup()) {
            if (contactTbHistory.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                closeContactType.setVisibility(View.VISIBLE);
                additionalCommentHistoryOfPatient.setVisibility(View.VISIBLE);
                tbInfectionForm.setVisibility(View.VISIBLE);
                tbType.setVisibility(View.VISIBLE);
                smearPositive.setVisibility(View.VISIBLE);
                childPrimaryCaregiver.setVisibility(View.VISIBLE);
                sameBedAsChild.setVisibility(View.VISIBLE);
                sameRoomRAsChild.setVisibility(View.VISIBLE);
                liveInSameHoushold.setVisibility(View.VISIBLE);
                seeChildEveryday.setVisibility(View.VISIBLE);
                contactCoughing.setVisibility(View.VISIBLE);
                oneCloseContactInHousehold.setVisibility(View.VISIBLE);
            } else {
                closeContactType.setVisibility(View.GONE);
                otherContactType.setVisibility(View.GONE);
                additionalCommentHistoryOfPatient.setVisibility(View.GONE);
                tbInfectionForm.setVisibility(View.GONE);
                tbType.setVisibility(View.GONE);
                smearPositive.setVisibility(View.GONE);
                childPrimaryCaregiver.setVisibility(View.GONE);
                sameBedAsChild.setVisibility(View.GONE);
                sameRoomRAsChild.setVisibility(View.GONE);
                liveInSameHoushold.setVisibility(View.GONE);
                seeChildEveryday.setVisibility(View.GONE);
                contactCoughing.setVisibility(View.GONE);
                oneCloseContactInHousehold.setVisibility(View.GONE);
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

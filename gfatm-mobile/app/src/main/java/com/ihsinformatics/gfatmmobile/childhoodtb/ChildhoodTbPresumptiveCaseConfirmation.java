package com.ihsinformatics.gfatmmobile.childhoodtb;

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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyCheckBox;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbPresumptiveCaseConfirmation extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledRadioGroup childPresumptive;
    TitledEditText weight;
    TitledEditText height;
    TitledRadioGroup muac;
    TitledEditText weightPercentileEditText;
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
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.CHILDHOODTB_PRESUMPTIVE_CASE_CONFIRMATION;
        form = Forms.childhoodTb_presumptive_case_confirmation;

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
            for (int i = 0; i < pageCount; i++) {
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
        childPresumptive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_mo_think_child_presumptive), getResources().getStringArray(R.array.yes_no_options), null, App.HORIZONTAL, App.VERTICAL, true, "CHILD DIAGNOSED PRESUMPTIVE BY MO", getResources().getStringArray(R.array.yes_no_list_concept));
        weight = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "WEIGHT (KG)");
        height = new TitledEditText(context, null, getResources().getString(R.string.ctb_height), "", "", 4, RegexUtil.FLOAT_FILTER, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true, "HEIGHT (CM)");
        muac = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_muac), getResources().getStringArray(R.array.ctb_muac_list), null, App.VERTICAL, App.VERTICAL, true, "MUAC (CM)", new String[]{"SEVERE ACUTE MALNUTRITION LESS THAN OR EQUAL TO 11.5", "MODERATE ACUTE MALNUTRITION LESS THAN OR EQUAL TO 12.5", "NORMAL MUAC GREATER THAN EQUAL TO 12.5"});
        weightPercentileEditText = new TitledEditText(context, null, getResources().getString(R.string.ctb_weight_percentile), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "WEIGHT PERCENTILE GROUP");
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "COUGH", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        coughDuration = new TitledSpinner(context, null, getResources().getString(R.string.ctb_cough_duration), getResources().getStringArray(R.array.ctb_cough_duration_list), null, App.VERTICAL, false, "COUGH DURATION", new String[]{"", "COUGH LASTING LESS THAN 2 WEEKS", "COUGH LASTING MORE THAN 2 WEEKS", "COUGH LASTING MORE THAN 3 WEEKS", "UNKNOWN", "REFUSED"});
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "FEVER", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "NIGHT SWEATS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "WEIGHT LOSS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        appetite = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_child_appetite), getResources().getStringArray(R.array.ctb_appetite_list), null, App.HORIZONTAL, App.VERTICAL, true, "APPETITE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        generalAppearance = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_general_appearance), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        generalAppearanceExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "GENERAL APPEARANCE EXPLANATION");
        headEyeEearNoseThroat = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_head_eye_ear_nose_throat), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        headEyeEearNoseThroatExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "HEAD, EARS, EYES, NOSE AND THROAT DESCRIPTION");
        lymphNodeExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_lymphnode), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        lymphNodeExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN");
        spineExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_spine), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        spineExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "SPINAL PHYSICAL EXAMINATION (TEXT)");
        jointsExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_joints), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        jointsExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "JOINTS PHYSICAL EXAMINATION (TEXT)");
        skinExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_skin), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        skinExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "SKIN EXAMINATION (TEXT)");
        chestExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_chest), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        chestExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "CHEST EXAMINATION (TEXT)");
        abdominalExamination = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_abdominal), getResources().getStringArray(R.array.ctb_remarkable_unremarkable), null, App.HORIZONTAL, App.VERTICAL, true);
        abdominalExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_explanation), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "ABDOMINAL EXAMINATION (TEXT)");
        othersExplanation = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_title), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "FREE TEXT COMMENT");
        bcgScar = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_bcg), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "BACILLUS CALMETTE–GUÉRIN VACCINE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        tbExamOutcome = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_exam_outcome), getResources().getStringArray(R.array.ctb_tb_exam_outcome_list), null, App.VERTICAL, App.VERTICAL, true, "TUBERCULOSIS PHYSICAL EXAM OUTCOME", new String[]{"SUGGESTIVE OF TB", "STRONGLY SUGGESTIVE OF TB", "NO TB INDICATION"});
        tbBefore = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_before), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "HISTORY OF TUBERCULOSIS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        tbMedication = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_medication), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "PATIENT TAKEN TB MEDICATION BEFORE", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        contactTbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_history_2years), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});
        closeContactType = new TitledCheckBoxes(context, null, getResources().getString(R.string.ctb_close_contact_type), getResources().getStringArray(R.array.ctb_close_contact_type_list), null, App.VERTICAL, App.VERTICAL, true, "CLOSE CONTACT WITH PATIENT", new String[]{"SELF", "MOTHER", "FATHER", "BROTHER", "SISTER", "PATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "MATERNAL GRANDMOTHER", "UNCLE", "AUNT", "OTHER CONTACT TYPE"});

        otherContactType = new TitledEditText(context, null, getResources().getString(R.string.ctb_other_title), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "OTHER CONTACT TYPE");

        additionalCommentHistoryOfPatient = new TitledEditText(context, null, getResources().getString(R.string.ctb_additional_comments_contact_history), "", "", 500, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false, "ADDITIONAL COMMENTS REGARDING CONTACT HISTORY OF THE PATIENT");

        tbInfectionForm = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_infection_form), getResources().getStringArray(R.array.ctb_tb_form_list), null, App.HORIZONTAL, App.VERTICAL, true, "TUBERCULOSIS INFECTION TYPE", new String[]{"DRUG-SENSITIVE TUBERCULOSIS INFECTION", "DRUG-RESISTANT TB", "REFUSED", "UNKNOWN"});

        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_tb_type), getResources().getStringArray(R.array.ctb_tb_type_list), null, App.VERTICAL, App.VERTICAL, true, "SITE OF TUBERCULOSIS DISEASE", new String[]{"PULMONARY TUBERCULOSIS", "EXTRA-PULMONARY TUBERCULOSIS", "REFUSED", "UNKNOWN"});

        smearPositive = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_smear_positive), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "SMEAR POSITIVE TUBERCULOSIS INFECTION", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        childPrimaryCaregiver = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_primary_caregiver), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE PRIMARY CARETAKER OF CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        sameBedAsChild = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_sleep_same_bed), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE SHARES BED WITH CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        sameRoomRAsChild = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_sleep_same_room), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE SHARES BEDROOM WITH CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        liveInSameHoushold = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_live_in_same_household), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE LIVES WITH CONTACT", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        seeChildEveryday = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_see_child_everyday), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE MEETS CONTACT DAILY", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        contactCoughing = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_contact_coughing), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "INDEX CASE COUGHING", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        oneCloseContactInHousehold = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_contact_in_child_household), getResources().getStringArray(R.array.yes_no_unknown_refused_options), null, App.HORIZONTAL, App.VERTICAL, true, "MULTIPLE INDEX CASES IN HOUSEHOLD", new String[]{"YES", "NO", "REFUSED", "UNKNOWN"});

        conclusion = new TitledRadioGroup(context, null, getResources().getString(R.string.ctb_conclusion), getResources().getStringArray(R.array.ctb_conclusion_list), getResources().getString(R.string.ctb_tb_presumptive_confirmed), App.HORIZONTAL, App.VERTICAL, true, "CONCLUSION", new String[]{"TB PRESUMPTIVE CONFIRMED", "NOT A TB PRESUMPTIVE"});
        doctorNotes = new TitledEditText(context, null, getResources().getString(R.string.ctb_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false, "CLINICIAN NOTES (TEXT)");


        views = new View[]{formDate.getButton(), muac.getRadioGroup(), childPresumptive.getRadioGroup(), cough.getRadioGroup(), coughDuration.getSpinner(), fever.getRadioGroup(),
                nightSweats.getRadioGroup(), weightLoss.getRadioGroup(), appetite.getRadioGroup(), generalAppearance.getRadioGroup(),
                headEyeEearNoseThroat.getRadioGroup(), lymphNodeExamination.getRadioGroup(), spineExamination.getRadioGroup(),
                jointsExamination.getRadioGroup(), skinExamination.getRadioGroup(), chestExamination.getRadioGroup(), abdominalExamination.getRadioGroup(),
                tbExamOutcome.getRadioGroup(), bcgScar.getRadioGroup(), bcgScar.getRadioGroup(), tbBefore.getRadioGroup(), tbMedication.getRadioGroup(),
                contactTbHistory.getRadioGroup(), tbInfectionForm.getRadioGroup(), tbType.getRadioGroup(), smearPositive.getRadioGroup(),
                childPrimaryCaregiver.getRadioGroup(), sameBedAsChild.getRadioGroup(), sameRoomRAsChild.getRadioGroup(), liveInSameHoushold.getRadioGroup(),
                seeChildEveryday.getRadioGroup(), contactCoughing.getRadioGroup(), oneCloseContactInHousehold.getRadioGroup(), conclusion.getRadioGroup(),
                weight.getEditText(), height.getEditText(), generalAppearanceExplanation.getEditText(), headEyeEearNoseThroatExplanation.getEditText(),
                lymphNodeExplanation.getEditText(), spineExplanation.getEditText(), jointsExplanation.getEditText(),
                skinExplanation.getEditText(), chestExplanation.getEditText(), abdominalExplanation.getEditText(), othersExplanation.getEditText(),
                otherContactType.getEditText(), additionalCommentHistoryOfPatient.getEditText(), doctorNotes.getEditText(), weightPercentileEditText.getEditText()
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, childPresumptive, weight, height, muac, weightPercentileEditText, cough, coughDuration, fever, nightSweats, weightLoss, appetite, generalAppearance, generalAppearanceExplanation, headEyeEearNoseThroat, headEyeEearNoseThroatExplanation, lymphNodeExamination, lymphNodeExplanation, spineExamination, spineExplanation,
                        jointsExamination, jointsExplanation, skinExamination, skinExplanation, chestExamination, chestExplanation, abdominalExamination, abdominalExplanation, othersExplanation, tbExamOutcome, bcgScar, tbBefore, tbMedication, contactTbHistory, closeContactType, otherContactType, additionalCommentHistoryOfPatient, tbInfectionForm, tbType, smearPositive, childPrimaryCaregiver
                        , sameBedAsChild, sameRoomRAsChild, liveInSameHoushold, seeChildEveryday, contactCoughing, oneCloseContactInHousehold, conclusion, doctorNotes
                }};

        formDate.getButton().setOnClickListener(this);
        childPresumptive.getRadioGroup().setOnCheckedChangeListener(this);
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
        muac.getRadioGroup().setOnCheckedChangeListener(this);
        conclusion.getRadioGroup().setOnCheckedChangeListener(this);


        ArrayList<MyCheckBox> checkBoxList = closeContactType.getCheckedBoxes();
        for (CheckBox cb : closeContactType.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        height.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (!App.get(height).equals("")) {
                    int h = Integer.parseInt(App.get(height));
                    if (h < 10.0 || h > 272.0)
                        height.getEditText().setError(getString(R.string.pet_invalid_height_range));
                    else
                        height.getEditText().setError(null);
                }
            }
        });

        weight.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (!App.get(weight).equals("")) {

                    Double w = Double.parseDouble(App.get(weight));
                    if (w < 0.5 || w > 700.0)
                        weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                    else
                        weight.getEditText().setError(null);

                    String percentile = serverService.getPercentile(App.get(weight));
                    weightPercentileEditText.getEditText().setText(percentile);

                } else {
                    weightPercentileEditText.getEditText().setText("");
                }

            }
        });
        weightPercentileEditText.getEditText().setKeyListener(null);

        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();


            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
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
        boolean error = super.validate();

        if (weight.getVisibility() == View.VISIBLE) {

            if (!App.get(weight).isEmpty()) {
                {
                    if (App.get(weight).length() == 4 && StringUtils.countMatches(App.get(weight), ".") == 0) {
                        if (App.isLanguageRTL())
                            gotoPage(0);
                        else
                            gotoPage(0);
                        weight.getEditText().setError(getString(R.string.ctb_invalid_value_weight));
                        weight.getEditText().requestFocus();
                        error = true;
                    } else if (StringUtils.countMatches(App.get(weight), ".") > 1) {
                        if (App.isLanguageRTL())
                            gotoPage(0);
                        else
                            gotoPage(0);
                        weight.getEditText().setError(getString(R.string.ctb_invalid_value_weight));
                        weight.getEditText().requestFocus();
                        error = true;
                    } else {
                        Double w = Double.parseDouble(App.get(weight));
                        if (w < 0.5 || w > 700.0) {
                            weight.getEditText().setError(getString(R.string.pet_invalid_weight_range));
                            gotoFirstPage();
                            error = true;
                            weight.getQuestionView().requestFocus();
                        } else {
                            weight.getEditText().setError(null);
                            weight.getQuestionView().clearFocus();
                        }
                    }
                }

            }
        }

        if (height.getVisibility() == View.VISIBLE) {
            if (!App.get(height).isEmpty()) {
                if (App.get(height).length() == 4 && StringUtils.countMatches(App.get(height), ".") == 0) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    height.getEditText().setError(getString(R.string.ctb_invalid_value_weight));
                    height.getEditText().requestFocus();
                    error = true;
                } else if (StringUtils.countMatches(App.get(height), ".") > 1) {
                    if (App.isLanguageRTL())
                        gotoPage(0);
                    else
                        gotoPage(0);
                    height.getEditText().setError(getString(R.string.ctb_invalid_value_weight));
                    height.getEditText().requestFocus();
                    error = true;
                } else {

                    int h = Integer.parseInt(App.get(height));
                    if (h < 10.0 || h > 272.0) {
                        height.getEditText().setError(getString(R.string.pet_invalid_height_range));
                        gotoFirstPage();
                        error = true;
                        height.getQuestionView().requestFocus();
                    } else {
                        height.getEditText().setError(null);
                        height.getQuestionView().clearFocus();
                    }

                }
            }
        }

        if (generalAppearanceExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(generalAppearanceExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                generalAppearanceExplanation.getEditText().setError(getString(R.string.empty_field));
                generalAppearanceExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(generalAppearanceExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                generalAppearanceExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                generalAppearanceExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (headEyeEearNoseThroatExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(headEyeEearNoseThroatExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                headEyeEearNoseThroatExplanation.getEditText().setError(getString(R.string.empty_field));
                headEyeEearNoseThroatExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(headEyeEearNoseThroatExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                headEyeEearNoseThroatExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                headEyeEearNoseThroatExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (lymphNodeExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(lymphNodeExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                lymphNodeExplanation.getEditText().setError(getString(R.string.empty_field));
                lymphNodeExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(lymphNodeExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                lymphNodeExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                lymphNodeExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (spineExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(spineExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                spineExplanation.getEditText().setError(getString(R.string.empty_field));
                spineExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(spineExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                spineExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                spineExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (jointsExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(jointsExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                jointsExplanation.getEditText().setError(getString(R.string.empty_field));
                jointsExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(jointsExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                jointsExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                jointsExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (skinExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(skinExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                skinExplanation.getEditText().setError(getString(R.string.empty_field));
                skinExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(skinExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                skinExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                skinExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (chestExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(chestExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                chestExplanation.getEditText().setError(getString(R.string.empty_field));
                chestExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(chestExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                chestExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                chestExplanation.getEditText().requestFocus();
                error = true;
            }
        }
        if (abdominalExplanation.getVisibility() == View.VISIBLE) {
            if (App.get(abdominalExplanation).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                abdominalExplanation.getEditText().setError(getString(R.string.empty_field));
                abdominalExplanation.getEditText().requestFocus();
                error = true;
            } else if (App.get(abdominalExplanation).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                abdominalExplanation.getEditText().setError(getString(R.string.ctb_spaces_only));
                abdominalExplanation.getEditText().requestFocus();
                error = true;
            }
        }

        if (otherContactType.getVisibility() == View.VISIBLE) {
            if (App.get(otherContactType).isEmpty()) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherContactType.getEditText().setError(getString(R.string.empty_field));
                otherContactType.getEditText().requestFocus();
                error = true;
            } else if (App.get(otherContactType).trim().length() <= 0) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherContactType.getEditText().setError(getString(R.string.ctb_spaces_only));
                otherContactType.getEditText().requestFocus();
                error = true;
            }
        }
        if (!App.get(doctorNotes).isEmpty() && App.get(doctorNotes).trim().length() <= 0) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            doctorNotes.getEditText().setError(getString(R.string.ctb_spaces_only));
            doctorNotes.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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
                if (App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally("Childhood TB-Presumptive Case Confirmation", form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveProgramEnrollement(App.getSqlDate(formDateCalendar), "Childhood TB", id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting("Childhood TB-Presumptive Case Confirmation", form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

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

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

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
            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            } else if (obs[0][0].equals("CHILD DIAGNOSED PRESUMPTIVE BY MO")) {
                for (RadioButton rb : childPresumptive.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WEIGHT (KG)")) {
                weight.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("HEIGHT (CM)")) {
                height.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("MUAC (CM)")) {
                for (RadioButton rb : muac.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_sam)) && obs[0][1].equals("SEVERE ACUTE MALNUTRITION LESS THAN OR EQUAL TO 11.5")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_mam)) && obs[0][1].equals("MODERATE ACUTE MALNUTRITION LESS THAN OR EQUAL TO 12.5")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_normal_12_5)) && obs[0][1].equals("NORMAL MUAC GREATER THAN EQUAL TO 12.5")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WEIGHT PERCENTILE GROUP")) {
                weightPercentileEditText.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("COUGH")) {
                for (RadioButton rb : cough.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        coughDuration.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("COUGH DURATION")) {
                String value = obs[0][1].equals("COUGH LASTING LESS THAN 2 WEEKS") ? getResources().getString(R.string.ctb_less_than_2_weeks) :
                        (obs[0][1].equals("COUGH LASTING MORE THAN 2 WEEKS") ? getResources().getString(R.string.ctb_2_to_3_weeks) :
                                (obs[0][1].equals("COUGH LASTING MORE THAN 3 WEEKS") ? getResources().getString(R.string.ctb_more_than_3_weeks) :
                                        (obs[0][1].equals("UNKNOWN") ? getResources().getString(R.string.unknown) :
                                                getResources().getString(R.string.ctb_refused))));
                coughDuration.getSpinner().selectValue(value);
            } else if (obs[0][0].equals("FEVER")) {
                for (RadioButton rb : fever.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("NIGHT SWEATS")) {
                for (RadioButton rb : nightSweats.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("WEIGHT LOSS")) {
                for (RadioButton rb : weightLoss.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("APPETITE")) {
                for (RadioButton rb : appetite.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_poor)) && obs[0][1].equals("POOR APPETITE")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_ok)) && obs[0][1].equals("OK")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("GENERAL APPEARANCE EXPLANATION")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    generalAppearance.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    generalAppearance.getRadioGroup().getButtons().get(0).setChecked(true);
                    generalAppearanceExplanation.setVisibility(View.VISIBLE);
                    generalAppearanceExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("HEAD, EARS, EYES, NOSE AND THROAT DESCRIPTION")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    headEyeEearNoseThroat.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    headEyeEearNoseThroat.getRadioGroup().getButtons().get(0).setChecked(true);
                    headEyeEearNoseThroatExplanation.setVisibility(View.VISIBLE);
                    headEyeEearNoseThroatExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("LYMPH NODE EXAMIMATION OF NECK, AXILLA AND GORIN")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    lymphNodeExamination.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    lymphNodeExamination.getRadioGroup().getButtons().get(0).setChecked(true);
                    lymphNodeExplanation.setVisibility(View.VISIBLE);
                    lymphNodeExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("JOINTS PHYSICAL EXAMINATION (TEXT)")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    jointsExamination.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    jointsExamination.getRadioGroup().getButtons().get(0).setChecked(true);
                    jointsExplanation.setVisibility(View.VISIBLE);
                    jointsExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("SKIN EXAMINATION (TEXT)")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    skinExamination.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    skinExamination.getRadioGroup().getButtons().get(0).setChecked(true);
                    skinExplanation.setVisibility(View.VISIBLE);
                    skinExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("CHEST EXAMINATION (TEXT)")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    chestExamination.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    chestExamination.getRadioGroup().getButtons().get(0).setChecked(true);
                    chestExplanation.setVisibility(View.VISIBLE);
                    chestExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("ABDOMINAL EXAMINATION (TEXT)")) {
                if (obs[0][1].equals(getResources().getString(R.string.ctb_did_not_examine))) {
                    abdominalExamination.getRadioGroup().getButtons().get(2).setChecked(true);
                } else {
                    abdominalExamination.getRadioGroup().getButtons().get(0).setChecked(true);
                    abdominalExplanation.setVisibility(View.VISIBLE);
                    abdominalExplanation.getEditText().setText(obs[0][1]);
                }
            } else if (obs[0][0].equals("FREE TEXT COMMENT")) {
                othersExplanation.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TUBERCULOSIS PHYSICAL EXAM OUTCOME")) {
                for (RadioButton rb : tbExamOutcome.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_suggestive_tb)) && obs[0][1].equals("SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) && obs[0][1].equals("STRONGLY SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_strongly_suggestive_tb)) && obs[0][1].equals("STRONGLY SUGGESTIVE OF TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_sugguestive_tb)) && obs[0][1].equals("NO TB INDICATION")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("BACILLUS CALMETTE–GUÉRIN VACCINE")) {
                for (RadioButton rb : bcgScar.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("HISTORY OF TUBERCULOSIS")) {
                for (RadioButton rb : tbBefore.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        tbMedication.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT TAKEN TB MEDICATION BEFORE")) {
                for (RadioButton rb : tbMedication.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("PATIENT IS CONTACT OF KNOWN OR SUSPECTED SUSPICIOUS CASE IN PAST 2 YEARS")) {
                for (RadioButton rb : contactTbHistory.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        closeContactType.setVisibility(View.VISIBLE);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CLOSE CONTACT WITH PATIENT")) {
                for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                    if (cb.getText().equals(getResources().getString(R.string.ctb_mother)) && obs[0][1].equals("MOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_father)) && obs[0][1].equals("FATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_brother)) && obs[0][1].equals("BROTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_sister)) && obs[0][1].equals("SISTER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandfather)) && obs[0][1].equals("PATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_paternal_grandmother)) && obs[0][1].equals("PATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandfather)) && obs[0][1].equals("MATERNAL GRANDFATHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_maternal_grandmother)) && obs[0][1].equals("MATERNAL GRANDMOTHER")) {
                        cb.setChecked(true);
                        break;
                    } else if (cb.getText().equals(getResources().getString(R.string.ctb_other_title)) && obs[0][1].equals("OTHER CONTACT TYPE")) {
                        cb.setChecked(true);
                        otherContactType.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            } else if (obs[0][0].equals("OTHER CONTACT TYPE")) {
                otherContactType.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("ADDITIONAL COMMENTS REGARDING CONTACT HISTORY OF THE PATIENT")) {
                additionalCommentHistoryOfPatient.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("TUBERCULOSIS INFECTION TYPE")) {
                for (RadioButton rb : tbInfectionForm.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_susceptible_tb)) && obs[0][1].equals("DRUG-SENSITIVE TUBERCULOSIS INFECTION")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_dr_tb)) && obs[0][1].equals("DRUG-RESISTANT TB")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SITE OF TUBERCULOSIS DISEASE")) {
                for (RadioButton rb : tbType.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_pulmonary)) && obs[0][1].equals("PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_extra_pulmonary)) && obs[0][1].equals("EXTRA-PULMONARY TUBERCULOSIS")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("SMEAR POSITIVE TUBERCULOSIS INFECTION")) {
                for (RadioButton rb : smearPositive.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE PRIMARY CARETAKER OF CONTACT")) {
                for (RadioButton rb : childPrimaryCaregiver.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE SHARES BED WITH CONTACT")) {
                for (RadioButton rb : sameBedAsChild.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE SHARES BEDROOM WITH CONTACT")) {
                for (RadioButton rb : sameRoomRAsChild.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE LIVES WITH CONTACT")) {
                for (RadioButton rb : liveInSameHoushold.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE MEETS CONTACT DAILY")) {
                for (RadioButton rb : seeChildEveryday.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX CASE COUGHING")) {
                for (RadioButton rb : contactCoughing.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("MULTIPLE INDEX CASES IN HOUSEHOLD")) {
                for (RadioButton rb : oneCloseContactInHousehold.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_unknown)) && obs[0][1].equals("UNKNOWN")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_refused)) && obs[0][1].equals("REFUSED")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CONCLUSION")) {
                for (RadioButton rb : conclusion.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.ctb_tb_presumptive_confirmed)) && obs[0][1].equals("TB PRESUMPTIVE CONFIRMED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.ctb_not_tb_presumptive)) && obs[0][1].equals("NOT A TB PRESUMPTIVE")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("CLINICIAN NOTES (TEXT)")) {
                doctorNotes.getEditText().setText(obs[0][1]);
            }

            if (generalAppearanceExplanation.getVisibility() == View.GONE && !App.get(generalAppearanceExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                generalAppearance.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (headEyeEearNoseThroatExplanation.getVisibility() == View.GONE && !App.get(headEyeEearNoseThroatExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                headEyeEearNoseThroat.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (lymphNodeExplanation.getVisibility() == View.GONE && !App.get(lymphNodeExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                lymphNodeExamination.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (spineExplanation.getVisibility() == View.GONE && !App.get(spineExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                spineExamination.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (jointsExplanation.getVisibility() == View.GONE && !App.get(jointsExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                jointsExamination.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (skinExplanation.getVisibility() == View.GONE && !App.get(skinExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                skinExamination.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (chestExplanation.getVisibility() == View.GONE && !App.get(chestExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                chestExamination.getRadioGroup().getButtons().get(1).setChecked(true);
            }

            if (abdominalExplanation.getVisibility() == View.GONE && !App.get(abdominalExplanation).equals(getResources().getString(R.string.ctb_did_not_examine))) {
                abdominalExamination.getRadioGroup().getButtons().get(1).setChecked(true);
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
        if (spinner == coughDuration.getSpinner()) {
            coughDuration.getQuestionView().setError(null);
        }
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

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        weight.setVisibility(View.GONE);
        weightPercentileEditText.setVisibility(View.GONE);
        height.setVisibility(View.GONE);
        muac.setVisibility(View.GONE);
        weightPercentileEditText.setVisibility(View.GONE);
        cough.setVisibility(View.GONE);
        fever.setVisibility(View.GONE);
        nightSweats.setVisibility(View.GONE);
        weightLoss.setVisibility(View.GONE);
        appetite.setVisibility(View.GONE);
        generalAppearance.setVisibility(View.GONE);
        headEyeEearNoseThroat.setVisibility(View.GONE);
        lymphNodeExamination.setVisibility(View.GONE);
        spineExamination.setVisibility(View.GONE);
        jointsExamination.setVisibility(View.GONE);
        skinExamination.setVisibility(View.GONE);
        chestExamination.setVisibility(View.GONE);
        abdominalExamination.setVisibility(View.GONE);
        othersExplanation.setVisibility(View.GONE);
        tbExamOutcome.setVisibility(View.GONE);
        bcgScar.setVisibility(View.GONE);
        tbBefore.setVisibility(View.GONE);
        contactTbHistory.setVisibility(View.GONE);
        conclusion.setVisibility(View.GONE);
        doctorNotes.setVisibility(View.GONE);

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
        if (group == cough.getRadioGroup()) {
            cough.getQuestionView().setError(null);
            if (cough.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                coughDuration.setVisibility(View.VISIBLE);
            } else {
                coughDuration.setVisibility(View.GONE);
            }
        } else if (group == fever.getRadioGroup()) {
            fever.getQuestionView().setError(null);
        } else if (group == nightSweats.getRadioGroup()) {
            nightSweats.getQuestionView().setError(null);
        } else if (group == weightLoss.getRadioGroup()) {
            weightLoss.getQuestionView().setError(null);
        } else if (group == appetite.getRadioGroup()) {
            appetite.getQuestionView().setError(null);
        } else if (group == tbExamOutcome.getRadioGroup()) {
            tbExamOutcome.getQuestionView().setError(null);
        } else if (group == bcgScar.getRadioGroup()) {
            bcgScar.getQuestionView().setError(null);
        } else if (group == tbMedication.getRadioGroup()) {
            tbMedication.getQuestionView().setError(null);
        } else if (group == tbInfectionForm.getRadioGroup()) {
            tbInfectionForm.getQuestionView().setError(null);
        } else if (group == tbType.getRadioGroup()) {
            tbType.getQuestionView().setError(null);
        } else if (group == smearPositive.getRadioGroup()) {
            smearPositive.getQuestionView().setError(null);
        } else if (group == childPrimaryCaregiver.getRadioGroup()) {
            childPrimaryCaregiver.getQuestionView().setError(null);
        } else if (group == sameBedAsChild.getRadioGroup()) {
            sameBedAsChild.getQuestionView().setError(null);
        } else if (group == sameRoomRAsChild.getRadioGroup()) {
            sameRoomRAsChild.getQuestionView().setError(null);
        } else if (group == liveInSameHoushold.getRadioGroup()) {
            liveInSameHoushold.getQuestionView().setError(null);
        } else if (group == seeChildEveryday.getRadioGroup()) {
            seeChildEveryday.getQuestionView().setError(null);
        } else if (group == contactCoughing.getRadioGroup()) {
            contactCoughing.getQuestionView().setError(null);
        } else if (group == oneCloseContactInHousehold.getRadioGroup()) {
            oneCloseContactInHousehold.getQuestionView().setError(null);
        } else if (group == generalAppearance.getRadioGroup()) {
            generalAppearance.getQuestionView().setError(null);
            if (generalAppearance.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                generalAppearanceExplanation.getEditText().setText("");

                generalAppearanceExplanation.setVisibility(View.VISIBLE);
            } else if (generalAppearance.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                generalAppearanceExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                generalAppearanceExplanation.setVisibility(View.GONE);
            } else {
                generalAppearanceExplanation.getEditText().setText("");
                generalAppearanceExplanation.setVisibility(View.GONE);
            }
        } else if (group == headEyeEearNoseThroat.getRadioGroup()) {
            headEyeEearNoseThroat.getQuestionView().setError(null);
            if (headEyeEearNoseThroat.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                headEyeEearNoseThroatExplanation.setVisibility(View.VISIBLE);
                headEyeEearNoseThroatExplanation.getEditText().setText("");
            } else if (headEyeEearNoseThroat.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                headEyeEearNoseThroatExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                headEyeEearNoseThroatExplanation.setVisibility(View.GONE);
            } else {
                headEyeEearNoseThroatExplanation.getEditText().setText("");
                headEyeEearNoseThroatExplanation.setVisibility(View.GONE);
            }
        } else if (group == lymphNodeExamination.getRadioGroup()) {
            lymphNodeExamination.getQuestionView().setError(null);
            if (lymphNodeExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                lymphNodeExplanation.setVisibility(View.VISIBLE);
                lymphNodeExplanation.getEditText().setText("");
            } else if (lymphNodeExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                lymphNodeExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                lymphNodeExplanation.setVisibility(View.GONE);
            } else {
                lymphNodeExplanation.getEditText().setText("");
                lymphNodeExplanation.setVisibility(View.GONE);
            }
        } else if (group == spineExamination.getRadioGroup()) {
            spineExamination.getQuestionView().setError(null);
            if (spineExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                spineExplanation.setVisibility(View.VISIBLE);
                spineExplanation.getEditText().setText("");
            } else if (spineExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                spineExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                spineExplanation.setVisibility(View.GONE);
            } else {
                spineExplanation.getEditText().setText("");
                spineExplanation.setVisibility(View.GONE);
            }
        } else if (group == jointsExamination.getRadioGroup()) {
            jointsExamination.getQuestionView().setError(null);
            if (jointsExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                jointsExplanation.getEditText().setText("");
                jointsExplanation.setVisibility(View.VISIBLE);
            } else if (jointsExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                jointsExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                jointsExplanation.setVisibility(View.GONE);
            } else {
                jointsExplanation.getEditText().setText("");
                jointsExplanation.setVisibility(View.GONE);
            }
        } else if (group == skinExamination.getRadioGroup()) {
            skinExamination.getQuestionView().setError(null);
            if (skinExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                skinExplanation.getEditText().setText("");
                skinExplanation.setVisibility(View.VISIBLE);
            } else if (skinExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                skinExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                skinExplanation.setVisibility(View.GONE);
            } else {
                skinExplanation.getEditText().setText("");
                skinExplanation.setVisibility(View.GONE);
            }
        } else if (group == chestExamination.getRadioGroup()) {
            chestExamination.getQuestionView().setError(null);
            if (chestExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                chestExplanation.getEditText().setText("");
                chestExplanation.setVisibility(View.VISIBLE);
            } else if (chestExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                chestExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                chestExplanation.setVisibility(View.GONE);
            } else {
                chestExplanation.getEditText().setText("");
                chestExplanation.setVisibility(View.GONE);
            }
        } else if (group == abdominalExamination.getRadioGroup()) {
            abdominalExamination.getQuestionView().setError(null);
            if (abdominalExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_remarkable))) {
                abdominalExplanation.getEditText().setText("");
                abdominalExplanation.setVisibility(View.VISIBLE);
            } else if (abdominalExamination.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_did_not_examine))) {
                abdominalExplanation.getEditText().setText(getResources().getString(R.string.ctb_did_not_examine));
                abdominalExplanation.setVisibility(View.GONE);
            } else {
                abdominalExplanation.getEditText().setText("");
                abdominalExplanation.setVisibility(View.GONE);
            }
        } else if (group == tbBefore.getRadioGroup()) {
            tbBefore.getQuestionView().setError(null);
            if (tbBefore.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                tbMedication.setVisibility(View.VISIBLE);

            } else {
                tbMedication.setVisibility(View.GONE);

            }
        } else if (group == contactTbHistory.getRadioGroup()) {
            contactTbHistory.getQuestionView().setError(null);
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
        } else if (group == childPresumptive.getRadioGroup()) {
            childPresumptive.getQuestionView().setError(null);
            if (childPresumptive.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.yes))) {
                weight.setVisibility(View.VISIBLE);
                height.setVisibility(View.VISIBLE);
                if (App.getPatient().getPerson().getAge() >= 0 && App.getPatient().getPerson().getAge() <= 5) {
                    muac.setVisibility(View.VISIBLE);
                }
                weightPercentileEditText.setVisibility(View.VISIBLE);
                cough.setVisibility(View.VISIBLE);
                if (App.get(cough).equals(getResources().getString(R.string.yes))) {
                    coughDuration.setVisibility(View.VISIBLE);
                }
                fever.setVisibility(View.VISIBLE);
                nightSweats.setVisibility(View.VISIBLE);
                weightLoss.setVisibility(View.VISIBLE);
                appetite.setVisibility(View.VISIBLE);
                generalAppearance.setVisibility(View.VISIBLE);
                if (App.get(generalAppearance).equals(getResources().getString(R.string.ctb_remarkable))) {
                    generalAppearanceExplanation.setVisibility(View.VISIBLE);
                }
                headEyeEearNoseThroat.setVisibility(View.VISIBLE);
                if (App.get(headEyeEearNoseThroat).equals(getResources().getString(R.string.ctb_remarkable))) {
                    headEyeEearNoseThroatExplanation.setVisibility(View.VISIBLE);
                }
                lymphNodeExamination.setVisibility(View.VISIBLE);
                if (App.get(lymphNodeExamination).equals(getResources().getString(R.string.ctb_remarkable))) {
                    lymphNodeExplanation.setVisibility(View.VISIBLE);
                }
                spineExamination.setVisibility(View.VISIBLE);
                if (App.get(spineExamination).equals(getResources().getString(R.string.ctb_remarkable))) {
                    spineExplanation.setVisibility(View.VISIBLE);
                }
                jointsExamination.setVisibility(View.VISIBLE);
                if (App.get(jointsExamination).equals(getResources().getString(R.string.ctb_remarkable))) {
                    jointsExplanation.setVisibility(View.VISIBLE);
                }
                skinExamination.setVisibility(View.VISIBLE);
                if (App.get(skinExamination).equals(getResources().getString(R.string.ctb_remarkable))) {
                    skinExplanation.setVisibility(View.VISIBLE);
                }
                chestExamination.setVisibility(View.VISIBLE);
                if (App.get(chestExamination).equals(getResources().getString(R.string.ctb_remarkable))) {
                    chestExplanation.setVisibility(View.VISIBLE);
                }
                abdominalExamination.setVisibility(View.VISIBLE);
                if (App.get(abdominalExamination).equals(getResources().getString(R.string.ctb_remarkable))) {
                    abdominalExplanation.setVisibility(View.VISIBLE);
                }
                othersExplanation.setVisibility(View.VISIBLE);
                tbExamOutcome.setVisibility(View.VISIBLE);
                bcgScar.setVisibility(View.VISIBLE);
                tbBefore.setVisibility(View.VISIBLE);
                contactTbHistory.setVisibility(View.VISIBLE);
                if (App.get(contactTbHistory).equals(getResources().getString(R.string.yes))) {
                    closeContactType.setVisibility(View.VISIBLE);
                    for (CheckBox cb : closeContactType.getCheckedBoxes()) {
                        if (cb.getText().equals(getResources().getString(R.string.ctb_other_title))) {
                            otherContactType.setVisibility(View.VISIBLE);
                        }
                    }
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

                }

                conclusion.setVisibility(View.VISIBLE);
                doctorNotes.setVisibility(View.VISIBLE);
            } else {
                weight.setVisibility(View.GONE);
                height.setVisibility(View.GONE);
                muac.setVisibility(View.GONE);
                weightPercentileEditText.setVisibility(View.GONE);
                cough.setVisibility(View.GONE);
                coughDuration.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
                nightSweats.setVisibility(View.GONE);
                weightLoss.setVisibility(View.GONE);
                appetite.setVisibility(View.GONE);
                generalAppearance.setVisibility(View.GONE);
                generalAppearanceExplanation.setVisibility(View.GONE);
                headEyeEearNoseThroat.setVisibility(View.GONE);
                headEyeEearNoseThroatExplanation.setVisibility(View.GONE);
                lymphNodeExamination.setVisibility(View.GONE);
                lymphNodeExplanation.setVisibility(View.GONE);
                spineExamination.setVisibility(View.GONE);
                spineExplanation.setVisibility(View.GONE);
                jointsExamination.setVisibility(View.GONE);
                jointsExplanation.setVisibility(View.GONE);
                skinExamination.setVisibility(View.GONE);
                skinExplanation.setVisibility(View.GONE);
                chestExamination.setVisibility(View.GONE);
                chestExplanation.setVisibility(View.GONE);
                abdominalExamination.setVisibility(View.GONE);
                abdominalExplanation.setVisibility(View.GONE);
                othersExplanation.setVisibility(View.GONE);
                tbExamOutcome.setVisibility(View.GONE);
                bcgScar.setVisibility(View.GONE);
                tbBefore.setVisibility(View.GONE);
                contactTbHistory.setVisibility(View.GONE);
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

                conclusion.setVisibility(View.GONE);
                doctorNotes.setVisibility(View.GONE);
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

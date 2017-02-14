package com.ihsinformatics.gfatmmobile.pet;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * Created by Rabbia on 11/24/2016.
 */

public class CounsellingFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText followupMonth;
    TitledEditText referralComplain;
    TitledRadioGroup missedDosed;
    TitledRadioGroup adherentToPet;
    TitledEditText reasonForNonAdherent;

    TitledRadioGroup adverseEventReport;
    LinearLayout adverseEffectsLayout;
    TitledCheckBoxes adverseEffects1;
    TitledCheckBoxes adverseEffects2;
    TitledEditText otherEffects;

    TitledSpinner treatmentSuppoterRelation;
    TitledRadioGroup behavioralComplaint;
    TitledSpinner behaviouralComplaintType;
    TitledEditText other;

    TitledRadioGroup treatmentSupportNegligence;
    TitledEditText treatmentSupportNegligenceReason;
    TitledRadioGroup misconceptionInPet;
    TitledEditText misconception;
    TitledRadioGroup infectionControllFollowing;
    TitledRadioGroup infectionControlCounselling;
    TitledCheckBoxes patientFacingProblem;
    TitledEditText otherProblem;

    TitledEditText contactComments;
    TitledEditText psychologistComments;

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
        FORM_NAME = Forms.PET_COUNSELLING_FOLLOWUP;
        FORM = Forms.pet_counsellingFollowup;

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

    /**
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews() {

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        followupMonth = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_month), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        referralComplain = new TitledEditText(context, null, getResources().getString(R.string.pet_referral_complain), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        referralComplain.getEditText().setSingleLine(false);
        referralComplain.getEditText().setMinimumHeight(150);
        missedDosed = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_missed_dosed), getResources().getStringArray(R.array.pet_missed_dosed_options), getResources().getString(R.string.pet_no_missed_dosed), App.VERTICAL, App.VERTICAL);
        adherentToPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherent), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.HORIZONTAL);
        reasonForNonAdherent = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_for_non_adherent), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        adverseEventReport = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_report), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        adverseEffectsLayout = new LinearLayout(context);
        adverseEffectsLayout.setOrientation(LinearLayout.HORIZONTAL);
        adverseEffects1 = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_adverse_effects), getResources().getStringArray(R.array.pet_adverse_effects_1), null, App.VERTICAL, App.VERTICAL, true);
        adverseEffectsLayout.addView(adverseEffects1);
        adverseEffects2 = new TitledCheckBoxes(context, null, "", getResources().getStringArray(R.array.pet_adverse_effects_2), null, App.VERTICAL, App.VERTICAL);
        adverseEffectsLayout.addView(adverseEffects2);
        otherEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        treatmentSuppoterRelation = new TitledSpinner(context, "", getResources().getString(R.string.pet_treatment_support), getResources().getStringArray(R.array.pet_household_heads), "", App.VERTICAL);
        behavioralComplaint = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        behaviouralComplaintType = new TitledSpinner(context, "", getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        treatmentSupportNegligence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_caretaker_negligence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        treatmentSupportNegligenceReason = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_caretaker_negligence), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        misconceptionInPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconcepions_after_pet_initiation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        misconception = new TitledEditText(context, null, getResources().getString(R.string.pet_misconception), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        infectionControllFollowing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_following), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        infectionControlCounselling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_counselling), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        patientFacingProblem = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_patient_problem), getResources().getStringArray(R.array.pet_patient_problem), null, App.VERTICAL, App.VERTICAL);
        otherProblem = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        contactComments = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_comments), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactComments.getEditText().setSingleLine(false);
        contactComments.getEditText().setMinimumHeight(150);
        psychologistComments = new TitledEditText(context, null, getResources().getString(R.string.pet_comment_psychologist), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        psychologistComments.getEditText().setSingleLine(false);
        psychologistComments.getEditText().setMinimumHeight(150);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), followupMonth.getEditText(), missedDosed.getRadioGroup(), adherentToPet.getRadioGroup(), reasonForNonAdherent.getEditText(),
                adverseEventReport.getRadioGroup(), adverseEffects1, adverseEffects2, otherEffects.getEditText(), treatmentSuppoterRelation.getSpinner(),
                treatmentSuppoterRelation.getSpinner(), behaviouralComplaintType.getSpinner(), other.getEditText(), treatmentSupportNegligence.getRadioGroup(),
                treatmentSupportNegligenceReason.getEditText(), misconceptionInPet.getRadioGroup(), misconception.getEditText(),
                infectionControllFollowing.getRadioGroup(), infectionControlCounselling.getRadioGroup(), patientFacingProblem, otherProblem.getEditText(),
                contactComments.getEditText(), psychologistComments.getEditText(), behavioralComplaint.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]{{formDate, followupMonth, referralComplain, missedDosed, adherentToPet, reasonForNonAdherent, adverseEventReport, adverseEffectsLayout, otherEffects,
                treatmentSuppoterRelation, behavioralComplaint, behaviouralComplaintType, other, treatmentSupportNegligence, treatmentSupportNegligenceReason, misconceptionInPet, misconception,
                infectionControllFollowing, infectionControlCounselling, patientFacingProblem, otherProblem, contactComments, psychologistComments}};

        formDate.getButton().setOnClickListener(this);
        adherentToPet.getRadioGroup().setOnCheckedChangeListener(this);
        treatmentSupportNegligence.getRadioGroup().setOnCheckedChangeListener(this);
        infectionControllFollowing.getRadioGroup().setOnCheckedChangeListener(this);
        misconceptionInPet.getRadioGroup().setOnCheckedChangeListener(this);
        adverseEventReport.getRadioGroup().setOnCheckedChangeListener(this);
        behavioralComplaint.getRadioGroup().setOnCheckedChangeListener(this);
        behaviouralComplaintType.getSpinner().setOnItemSelectedListener(this);
        for (CheckBox cb : adverseEffects2.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);

        reasonForNonAdherent.setVisibility(View.GONE);
        adverseEffectsLayout.setVisibility(View.GONE);
        otherEffects.setVisibility(View.GONE);
        behaviouralComplaintType.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        treatmentSupportNegligenceReason.setVisibility(View.GONE);
        misconception.setVisibility(View.GONE);
        infectionControlCounselling.setVisibility(View.VISIBLE);
        otherProblem.setVisibility(View.GONE);
        for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
            cb.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        reasonForNonAdherent.setVisibility(View.GONE);
        adverseEffectsLayout.setVisibility(View.GONE);
        otherEffects.setVisibility(View.GONE);
        behaviouralComplaintType.setVisibility(View.GONE);
        other.setVisibility(View.GONE);
        treatmentSupportNegligenceReason.setVisibility(View.GONE);
        misconception.setVisibility(View.GONE);
        infectionControlCounselling.setVisibility(View.VISIBLE);
        misconception.setVisibility(View.GONE);
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        View view = null;
        if (App.get(otherProblem).isEmpty() && otherProblem.getVisibility() == View.VISIBLE) {
            otherProblem.getEditText().setError(getString(R.string.empty_field));
            otherProblem.getEditText().requestFocus();
            error = true;
        } else
            otherProblem.clearFocus();

        if (App.get(misconception).isEmpty() && misconception.getVisibility() == View.VISIBLE) {
            misconception.getEditText().setError(getString(R.string.empty_field));
            misconception.getEditText().requestFocus();
            error = true;
        } else
            misconception.clearFocus();

        if (App.get(treatmentSupportNegligenceReason).isEmpty() && treatmentSupportNegligenceReason.getVisibility() == View.VISIBLE) {
            treatmentSupportNegligenceReason.getEditText().setError(getString(R.string.empty_field));
            treatmentSupportNegligenceReason.getEditText().requestFocus();
            error = true;
        } else
            treatmentSupportNegligenceReason.clearFocus();

        if (App.get(other).isEmpty() && other.getVisibility() == View.VISIBLE) {
            other.getEditText().setError(getString(R.string.empty_field));
            other.getEditText().requestFocus();
            error = true;
        } else
            other.clearFocus();

        if (App.get(otherEffects).isEmpty() && otherEffects.getVisibility() == View.VISIBLE) {
            otherEffects.getEditText().setError(getString(R.string.empty_field));
            otherEffects.getEditText().requestFocus();
            error = true;
        } else
            otherEffects.clearFocus();

        if (adverseEffectsLayout.getVisibility() == View.VISIBLE) {
            Boolean flag = false;
            for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                if (cb.isChecked()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag) {
                adverseEffects1.getQuestionView().setError(getString(R.string.empty_field));
                adverseEffects1.getQuestionView().requestFocus();
                view = adverseEffects1;
                gotoLastPage();
                error = true;
            }
        }

        if (App.get(reasonForNonAdherent).isEmpty() && reasonForNonAdherent.getVisibility() == View.VISIBLE) {
            reasonForNonAdherent.getEditText().setError(getString(R.string.empty_field));
            reasonForNonAdherent.getEditText().requestFocus();
            error = true;
            view = null;
        } else
            reasonForNonAdherent.clearFocus();

        if (App.get(followupMonth).isEmpty() && followupMonth.getVisibility() == View.VISIBLE) {
            followupMonth.getEditText().setError(getString(R.string.empty_field));
            followupMonth.getEditText().requestFocus();
            error = true;
            view = null;
        } else
            followupMonth.clearFocus();

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
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

        final ContentValues values = new ContentValues();
        values.put("formDate", App.getSqlDate(formDateCalendar));
        // start time...
        // end time...
        // gps coordinate...

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(followupMonth)});
        observations.add(new String[]{"REFERRAL COMPLAINT", App.get(referralComplain)});
        //Missed Dose
        observations.add(new String[]{"PATIENT ADHERENT TO TRETMENT", App.get(adherentToPet).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (reasonForNonAdherent.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LOW ADHERENCE REASON", App.get(reasonForNonAdherent)});
        observations.add(new String[]{"ADVERSE EVENTS REPORTED", App.get(adverseEventReport).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (adverseEffectsLayout.getVisibility() == View.VISIBLE) {
            String adverseEffects = "";
            for (CheckBox cb : adverseEffects1.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_joint_pain)))
                    adverseEffects = adverseEffects + "JOINT PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_headache)))
                    adverseEffects = adverseEffects + "HEADACHE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_skin_rash)))
                    adverseEffects = adverseEffects + "RASH" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_nausea)))
                    adverseEffects = adverseEffects + "NAUSEA" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_dizziness)))
                    adverseEffects = adverseEffects + "DIZZINESS AND GIDDINESS" + " ; ";
            }
            for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_vomiting)))
                    adverseEffects = adverseEffects + "VOMITING" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_abdominal_pain)))
                    adverseEffects = adverseEffects + "ABDOMINAL PAIN" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_loss_of_appetite)))
                    adverseEffects = adverseEffects + "LOSS OF APPETITE" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_visual_impairment)))
                    adverseEffects = adverseEffects + "VISUAL IMPAIRMENT" + " ; ";
                else if (cb.isChecked() && cb.getText().equals(getResources().getString(R.string.pet_other)))
                    adverseEffects = adverseEffects + "OTHER ADVERSE EVENT" + " ; ";
            }
            observations.add(new String[]{"ADVERSE EVENTS", adverseEffects});
        }
        if (otherEffects.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"ADVERSE EVENTS", App.get(otherEffects)});
        observations.add(new String[]{"TREATMENT SUPPORTER RELATIONSHIP TO PATIENT", (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_mother))) ? "MOTHER" :
                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_father)) ? "FATHER" :
                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_maternal_grandmother)) ? "MATERNAL GRANDMOTHER" :
                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_maternal_grandfather)) ? "MATERNAL GRANDFATHER" :
                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_paternal_grandmother)) ? "PATERNAL GRANDMOTHER" :
                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_paternal_grandfather)) ? "PATERNAL GRANDFATHER" :
                                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_brother)) ? "BROTHER" :
                                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_sister)) ? "SISTER" :
                                                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_son)) ? "SON" :
                                                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_daughter)) ? "SPOUSE" :
                                                                                        (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_aunt)) ? "AUNT" :
                                                                                                (App.get(treatmentSuppoterRelation).equals(getResources().getString(R.string.pet_uncle)) ? "UNCLE" : "OTHER FAMILY MEMBER")))))))))))});
        observations.add(new String[]{"BEHAVIORAL COMPLAINS AFTER TREATMENT", App.get(behavioralComplaint).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (behaviouralComplaintType.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"BEHAVIOUR", App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_irritable)) ? "IRRITABILITY" :
                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_stubborn)) ? "STUBBORN BEHAVIOUR" :
                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_shy)) ? "INTROVERTED PERSONALITY" :
                                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_aggressive)) ? "AGGRESSIVE BEHAVIOUR" :
                                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_argumentative)) ? "ARGUMENTATIVE BEHAVIOUR" :
                                                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_non_complaint)) ? "NON COMPLAINT BEHAVIOUR" :
                                                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_responsible)) ? "RESPONSIBLE PERSONALITY" :
                                                                    (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_cooperative)) ? "COOPERATIVE BEHAVIOUR" :
                                                                            (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_noncooperative)) ? "NON-COOPERATIVE BEHAVIOUR" : "OTHER PERSONALITY TYPE"))))))))});
        if (other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER PERSONALITY TYPE", App.get(other)});
        observations.add(new String[]{"TREATMENT SUPPORTER NEGLIGENCE", App.get(treatmentSupportNegligence).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (treatmentSupportNegligenceReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT SUPPORTER NEGLIGENCE REASON", App.get(treatmentSupportNegligenceReason)});
        observations.add(new String[]{"TREATMENT MISCONCEPTIONS", App.get(misconception).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (misconceptionInPet.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"TREATMENT MISCONCEPTION (TEXT)", App.get(misconceptionInPet)});
        observations.add(new String[]{"INFECTION CONTROL MEASURES FOLLOWED", App.get(infectionControllFollowing).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (infectionControlCounselling.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"INFECTION CONTROL COUNSELLING", App.get(infectionControlCounselling).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"SOCIAL PROBLEM", App.get(patientFacingProblem).equals(getResources().getString(R.string.pet_academic_problem)) ? "ACADEMIC PROBLEM" :
                (App.get(patientFacingProblem).equals(getResources().getString(R.string.pet_economic_problem)) ? "ECONOMIC PROBLEM" :
                        (App.get(patientFacingProblem).equals(getResources().getString(R.string.pet_health_problem)) ? "PERSONAL BARRIER TO HEALTH CARE" :
                                (App.get(patientFacingProblem).equals(getResources().getString(R.string.pet_unknown)) ? "UNKNOWN" :
                                        (App.get(patientFacingProblem).equals(getResources().getString(R.string.refused)) ? "REFUSED" : "OTHER NON-CODED"))))});
        observations.add(new String[]{"CARETAKER COMMENTS", App.get(contactComments)});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(psychologistComments)});

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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, App.getSqlDate(formDateCalendar), observations.toArray(new String[][]{}));
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

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

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
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == behaviouralComplaintType.getSpinner()) {
            if (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_other)))
                other.setVisibility(View.VISIBLE);
            else
                other.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        boolean flag = false;
        for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
            if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherEffects.setVisibility(View.VISIBLE);
        else
            otherEffects.setVisibility(View.GONE);

        flag = false;
        for (CheckBox cb : patientFacingProblem.getCheckedBoxes()) {
            if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            otherProblem.setVisibility(View.VISIBLE);
        else
            otherProblem.setVisibility(View.GONE);

    }

    @Override
    public void onPageSelected(int pageNo) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (group == adherentToPet.getRadioGroup()) {
            if (App.get(adherentToPet).equals(getResources().getString(R.string.no)))
                reasonForNonAdherent.setVisibility(View.VISIBLE);
            else
                reasonForNonAdherent.setVisibility(View.GONE);
        } else if (group == adverseEventReport.getRadioGroup()) {
            if (App.get(adverseEventReport).equals(getResources().getString(R.string.no))) {
                adverseEffectsLayout.setVisibility(View.GONE);
                otherEffects.setVisibility(View.GONE);
            } else {
                adverseEffectsLayout.setVisibility(View.VISIBLE);
                boolean flag = false;
                for (CheckBox cb : adverseEffects2.getCheckedBoxes()) {
                    if (cb.getText().equals(getString(R.string.pet_other)) && cb.isChecked()) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    otherEffects.setVisibility(View.VISIBLE);
                else
                    otherEffects.setVisibility(View.GONE);
            }
        } else if (group == behavioralComplaint.getRadioGroup()) {
            if (App.get(behavioralComplaint).equals(getResources().getString(R.string.no))) {
                behaviouralComplaintType.setVisibility(View.GONE);
                other.setVisibility(View.GONE);
            } else {
                behaviouralComplaintType.setVisibility(View.VISIBLE);
                if (App.get(behaviouralComplaintType).equals(getResources().getString(R.string.pet_other)))
                    other.setVisibility(View.VISIBLE);
                else
                    other.setVisibility(View.GONE);
            }
        } else if (group == treatmentSupportNegligence.getRadioGroup()) {
            if (App.get(treatmentSupportNegligence).equals(getResources().getString(R.string.yes)))
                treatmentSupportNegligenceReason.setVisibility(View.VISIBLE);
            else
                treatmentSupportNegligenceReason.setVisibility(View.GONE);
        } else if (group == misconceptionInPet.getRadioGroup()) {
            if (App.get(misconceptionInPet).equals(getResources().getString(R.string.yes)))
                misconception.setVisibility(View.VISIBLE);
            else
                misconception.setVisibility(View.GONE);
        } else if (group == infectionControlCounselling.getRadioGroup()) {
            if (App.get(infectionControlCounselling).equals(getResources().getString(R.string.yes)))
                infectionControllFollowing.setVisibility(View.GONE);
            else
                infectionControllFollowing.setVisibility(View.VISIBLE);
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
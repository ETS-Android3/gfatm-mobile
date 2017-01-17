package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class CounsellingFollowupForm extends AbstractFormActivity {

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
    TitledSpinner behaviouralComplaints;
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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
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
                ScrollView scrollView = new ScrollView(mainContent.getContext());
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
        followupMonth = new TitledEditText(context, null, getResources().getString(R.string.pet_followup_month), "", "", 2, RegexUtil.numericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        referralComplain = new TitledEditText(context, null, getResources().getString(R.string.pet_referral_complain), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        referralComplain.getEditText().setSingleLine(false);
        referralComplain.getEditText().setMinimumHeight(150);
        missedDosed = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_missed_dosed), getResources().getStringArray(R.array.pet_missed_dosed_options), getResources().getString(R.string.no), App.VERTICAL, App.VERTICAL);
        adherentToPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adherent), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.HORIZONTAL);
        reasonForNonAdherent = new TitledEditText(context, null, getResources().getString(R.string.pet_reason_for_non_adherent), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        adverseEventReport = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_adverse_event_report), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        adverseEffectsLayout = new LinearLayout(context);
        adverseEffectsLayout.setOrientation(LinearLayout.HORIZONTAL);
        adverseEffects1 = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_adverse_effects), getResources().getStringArray(R.array.pet_adverse_effects_1), null, App.VERTICAL, App.VERTICAL);
        adverseEffectsLayout.addView(adverseEffects1);
        adverseEffects2 = new TitledCheckBoxes(context, null, "", getResources().getStringArray(R.array.pet_adverse_effects_2), null, App.VERTICAL, App.VERTICAL);
        adverseEffectsLayout.addView(adverseEffects2);
        otherEffects = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        treatmentSuppoterRelation = new TitledSpinner(context, "", getResources().getString(R.string.pet_treatment_support), getResources().getStringArray(R.array.pet_household_heads), "", App.VERTICAL);
        behaviouralComplaints = new TitledSpinner(context, "", getResources().getString(R.string.pet_behavioural_complain), getResources().getStringArray(R.array.pet_contact_behaviours), "", App.VERTICAL);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        treatmentSupportNegligence = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_caretaker_negligence), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        treatmentSupportNegligenceReason = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        misconceptionInPet = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_misconcepions_after_pet_initiation), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        misconception = new TitledEditText(context, null, getResources().getString(R.string.pet_misconception), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        infectionControllFollowing = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_following), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        infectionControlCounselling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_control_counselling), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        patientFacingProblem = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_patient_problem), getResources().getStringArray(R.array.pet_patient_problem), null, App.VERTICAL, App.VERTICAL);
        otherProblem = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        contactComments = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_comments), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        contactComments.getEditText().setSingleLine(false);
        contactComments.getEditText().setMinimumHeight(150);
        psychologistComments = new TitledEditText(context, null, getResources().getString(R.string.pet_comment_psychologist), "", "", 1000, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        psychologistComments.getEditText().setSingleLine(false);
        psychologistComments.getEditText().setMinimumHeight(150);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), followupMonth.getEditText(), missedDosed.getRadioGroup(), adherentToPet.getRadioGroup(), reasonForNonAdherent.getEditText(),
                adverseEventReport.getRadioGroup(), adverseEffects1, adverseEffects2, otherEffects.getEditText(), treatmentSuppoterRelation.getSpinner(),
                treatmentSuppoterRelation.getSpinner(), behaviouralComplaints.getSpinner(), other.getEditText(), treatmentSupportNegligence.getRadioGroup(),
                treatmentSupportNegligenceReason.getEditText(), misconceptionInPet.getRadioGroup(), misconception.getEditText(),
                infectionControllFollowing.getRadioGroup(), infectionControlCounselling.getRadioGroup(), patientFacingProblem, otherProblem.getEditText(),
                contactComments.getEditText(), psychologistComments.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]{{formDate, followupMonth, referralComplain, missedDosed, adherentToPet, reasonForNonAdherent, adverseEventReport, adverseEffectsLayout, otherEffects,
                treatmentSuppoterRelation, behaviouralComplaints, other, treatmentSupportNegligence, treatmentSupportNegligenceReason, misconceptionInPet, misconception,
                infectionControllFollowing, infectionControlCounselling, patientFacingProblem, otherProblem, contactComments, psychologistComments}};

        formDate.getButton().setOnClickListener(this);

    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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

        //resetViews();
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
    }

    @Override
    public void onPageSelected(int pageNo) {

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

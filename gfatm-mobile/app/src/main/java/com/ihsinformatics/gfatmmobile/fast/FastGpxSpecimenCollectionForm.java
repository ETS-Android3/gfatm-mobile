package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
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
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Haris on 1/19/2017.
 */

public class FastGpxSpecimenCollectionForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledButton sampleSubmissionDate;
    TitledRadioGroup testContextStatus;
    TitledRadioGroup tbCategory;
    TitledRadioGroup baselineRepeatReason;
    TitledRadioGroup sampleType;
    TitledSpinner specimenSource;
    TitledEditText specimenSourceOther;
    TitledRadioGroup sampleAccepted;
    TitledSpinner reasonRejected;
    TitledEditText otherReasonRejected;
    TitledEditText cartridgeId;

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
        FORM_NAME = Forms.FAST_GXP_SPECIMEN_COLLECTION_FORM;
        FORM = Forms.fastGpxSpecimenCollectionForm;

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
        sampleSubmissionDate = new TitledButton(context, null, getResources().getString(R.string.fast_day_was_the_sample_submitted), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        testContextStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_point_test_being_done), getResources().getStringArray(R.array.fast_test_being_done_list), getResources().getString(R.string.fast_baseline_new), App.VERTICAL, App.VERTICAL);
        tbCategory = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_baseline_test_specify_patient_category), getResources().getStringArray(R.array.fast_patient_category_list), getResources().getString(R.string.fast_category_1), App.VERTICAL, App.VERTICAL);
        baselineRepeatReason = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_repeating_test_if_baseline_repeat), getResources().getStringArray(R.array.fast_repeating_test_if_baseline_repeat_list), getResources().getString(R.string.fast_rif_resistant), App.VERTICAL, App.VERTICAL);
        baselineRepeatReason.setVisibility(View.GONE);
        sampleType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_specimen_type), getResources().getStringArray(R.array.fast_specimen_type_list), getResources().getString(R.string.fast_sputum), App.VERTICAL, App.VERTICAL);
        specimenSource = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_specimen_come_from), getResources().getStringArray(R.array.fast_specimen_come_from_list), getResources().getString(R.string.fast_lymph), App.VERTICAL);
        specimenSource.setVisibility(View.GONE);
        specimenSourceOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        specimenSourceOther.setVisibility(View.GONE);
        sampleAccepted = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_accepted_lab_technician), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL);
        reasonRejected = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_why_was_the_sample_rejected), getResources().getStringArray(R.array.fast_sample_rejected_list), getResources().getString(R.string.fast_saliva), App.VERTICAL);
        reasonRejected.setVisibility(View.GONE);
        otherReasonRejected = new TitledEditText(context, null, getResources().getString(R.string.fast_other_reason_for_rejection), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        otherReasonRejected.setVisibility(View.GONE);
        cartridgeId = new TitledEditText(context, null, getResources().getString(R.string.fast_cartridge_id), "", "", 9, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), sampleSubmissionDate.getButton(), testContextStatus.getRadioGroup(), tbCategory.getRadioGroup(),
                baselineRepeatReason.getRadioGroup(), sampleType.getRadioGroup(), specimenSource.getSpinner(), specimenSourceOther.getEditText(), sampleAccepted.getRadioGroup(), reasonRejected.getSpinner(), otherReasonRejected.getEditText(), cartridgeId.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, sampleSubmissionDate, testContextStatus, tbCategory, baselineRepeatReason, sampleType, specimenSource, specimenSourceOther, sampleAccepted, reasonRejected, otherReasonRejected, cartridgeId}};

        formDate.getButton().setOnClickListener(this);
        sampleSubmissionDate.getButton().setOnClickListener(this);
        testContextStatus.getRadioGroup().setOnCheckedChangeListener(this);
        tbCategory.getRadioGroup().setOnCheckedChangeListener(this);
        baselineRepeatReason.getRadioGroup().setOnCheckedChangeListener(this);
        sampleType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenSource.getSpinner().setOnItemSelectedListener(this);
        sampleAccepted.getRadioGroup().setOnCheckedChangeListener(this);
        reasonRejected.getSpinner().setOnItemSelectedListener(this);

    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        sampleSubmissionDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (specimenSourceOther.getVisibility() == View.VISIBLE && App.get(specimenSourceOther).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenSourceOther.getEditText().setError(getString(R.string.empty_field));
            specimenSourceOther.getEditText().requestFocus();
            error = true;
        }

        if (otherReasonRejected.getVisibility() == View.VISIBLE && App.get(otherReasonRejected).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherReasonRejected.getEditText().setError(getString(R.string.empty_field));
            otherReasonRejected.getEditText().requestFocus();
            error = true;
        }

        if (cartridgeId.getVisibility() == View.VISIBLE && App.get(cartridgeId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cartridgeId.getEditText().setError(getString(R.string.empty_field));
            cartridgeId.getEditText().requestFocus();
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

        if (validate()) {
            resetViews();
        }

        //resetViews();
        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

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

        if (view == sampleSubmissionDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == specimenSource.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                specimenSourceOther.setVisibility(View.VISIBLE);
            } else {
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (spinner == reasonRejected.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                otherReasonRejected.setVisibility(View.VISIBLE);
            } else {
                otherReasonRejected.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == testContextStatus.getRadioGroup()) {
            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_new))) {
                tbCategory.setVisibility(View.VISIBLE);
            } else {
                tbCategory.setVisibility(View.GONE);
            }

            if (testContextStatus.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_baseline_repeat))) {
                baselineRepeatReason.setVisibility(View.VISIBLE);
            } else {
                baselineRepeatReason.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleType.getRadioGroup()) {
            if (sampleType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_extra_pulmonary))) {
                specimenSource.setVisibility(View.VISIBLE);
                if (specimenSource.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    specimenSourceOther.setVisibility(View.VISIBLE);
                }
            } else {
                specimenSource.setVisibility(View.GONE);
                specimenSourceOther.setVisibility(View.GONE);
            }
        } else if (radioGroup == sampleAccepted.getRadioGroup()) {
            if (sampleAccepted.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                reasonRejected.setVisibility(View.VISIBLE);
                if (reasonRejected.getSpinner().getSelectedItem().equals(getResources().getString(R.string.fast_other_title))) {
                    otherReasonRejected.setVisibility(View.VISIBLE);
                }
                cartridgeId.setVisibility(View.GONE);
            } else {
                reasonRejected.setVisibility(View.GONE);
                cartridgeId.setVisibility(View.VISIBLE);
                otherReasonRejected.setVisibility(View.GONE);
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

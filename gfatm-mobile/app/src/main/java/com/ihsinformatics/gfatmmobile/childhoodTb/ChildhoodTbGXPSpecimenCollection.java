package com.ihsinformatics.gfatmmobile.childhoodTb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbGXPSpecimenCollection extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledButton sampleSubmitDate;
    TitledSpinner baselineRepeatFollowup;
    TitledRadioGroup patientCategory;
    TitledSpinner reasonBaselineRepeat;
    TitledRadioGroup specimenType;
    TitledSpinner specimenComeFrom;
    TitledEditText otherSpecimentComeFrom;
    TitledRadioGroup sampleAcceptedByTechnician;
    TitledSpinner whySampleRejcted;
    TitledEditText reasonForRejection;
    TitledEditText cartridgeId;

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
        FORM_NAME = Forms.CHILDHOODTB_GXP_SPECIMEN_COLLECTION_FORM;
        FORM = Forms.childhoodTb_gxp_specimen_form;

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
        sampleSubmitDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        sampleSubmitDate.setTag("sampleSubmitDate");
        baselineRepeatFollowup = new TitledSpinner(context,null,getResources().getString(R.string.ctb_baseline_repeat_followup),getResources().getStringArray(R.array.ctb_ctb_baseline_repeat_followup_list),null,App.VERTICAL);
        patientCategory = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_patient_category),getResources().getStringArray(R.array.ctb_patient_category_list),null,App.HORIZONTAL,App.VERTICAL);
        reasonBaselineRepeat = new TitledSpinner(context,null,getResources().getString(R.string.ctb_reason_for_repeating),getResources().getStringArray(R.array.ctb_reason_for_repeating_list),null,App.VERTICAL);
        specimenType = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_specimen_type),getResources().getStringArray(R.array.ctb_specimen_type_list),null,App.HORIZONTAL,App.VERTICAL);
        specimenComeFrom = new TitledSpinner(context,null,getResources().getString(R.string.ctb_speciment_route),getResources().getStringArray(R.array.ctb_speciment_route_list),null,App.VERTICAL);
        otherSpecimentComeFrom = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_specify),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        sampleAcceptedByTechnician = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_accepted_by_technician),getResources().getStringArray(R.array.ctb_accepted_by_techician_list),null,App.HORIZONTAL,App.VERTICAL);
        whySampleRejcted = new TitledSpinner(context,null,getResources().getString(R.string.ctb_why_sample_rejected),getResources().getStringArray(R.array.ctb_why_sample_rejected_list),null,App.VERTICAL);
        reasonForRejection = new TitledEditText(context,null,getResources().getString(R.string.ctb_other_reason_rejection),"","",50,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.HORIZONTAL,false);
        cartridgeId = new TitledEditText(context,null,getResources().getString(R.string.ctb_cartridge_id),"","",4,RegexUtil.NUMERIC_FILTER,InputType.TYPE_CLASS_NUMBER,App.HORIZONTAL,false);


        views = new View[]{formDate.getButton(),sampleSubmitDate.getButton(),baselineRepeatFollowup.getSpinner(),patientCategory.getRadioGroup(),reasonBaselineRepeat.getSpinner(),
                specimenType.getRadioGroup(),specimenComeFrom.getSpinner(),sampleAcceptedByTechnician.getRadioGroup(),whySampleRejcted.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate,sampleSubmitDate,baselineRepeatFollowup,patientCategory,reasonBaselineRepeat,specimenType,specimenComeFrom,otherSpecimentComeFrom,sampleAcceptedByTechnician
                ,whySampleRejcted,reasonForRejection,cartridgeId}};

        formDate.getButton().setOnClickListener(this);
        sampleSubmitDate.getButton().setOnClickListener(this);
        baselineRepeatFollowup.getSpinner().setOnItemSelectedListener(this);
        patientCategory.getRadioGroup().setOnCheckedChangeListener(this);
        reasonBaselineRepeat.getSpinner().setOnItemSelectedListener(this);
        specimenType.getRadioGroup().setOnCheckedChangeListener(this);
        specimenComeFrom.getSpinner().setOnItemSelectedListener(this);
        sampleAcceptedByTechnician.getRadioGroup().setOnCheckedChangeListener(this);
        whySampleRejcted.getSpinner().setOnItemSelectedListener(this);


        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();


            Date date = new Date();

            if (formDateCalendar.after(date)) {

                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        if (!sampleSubmitDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString())) {

            //
            // +Date date = App.stringToDate(sampleSubmitDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (secondDateCalendar.after(date)) {

                secondDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                sampleSubmitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        }
    }

    @Override
    public boolean validate() {
        boolean error=false;
        if (patientCategory.getVisibility() == View.VISIBLE && App.get(patientCategory).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            patientCategory.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            patientCategory.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            patientCategory.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (specimenType.getVisibility() == View.VISIBLE && App.get(specimenType).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            specimenType.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
            specimenType.getRadioGroup().requestFocus();
            error = true;
        }
        else{
            specimenType.getRadioGroup().getButtons().get(1).setError(null);
        }
        if (otherSpecimentComeFrom.getVisibility() == View.VISIBLE && App.get(otherSpecimentComeFrom).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            otherSpecimentComeFrom.getEditText().setError(getString(R.string.empty_field));
            otherSpecimentComeFrom.getEditText().requestFocus();
            error = true;
        }
        if (reasonForRejection.getVisibility() == View.VISIBLE && App.get(reasonForRejection).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            reasonForRejection.getEditText().setError(getString(R.string.empty_field));
            reasonForRejection.getEditText().requestFocus();
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

        return true;
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
        if (view == sampleSubmitDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == baselineRepeatFollowup.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_baseline))) {
                patientCategory.setVisibility(View.VISIBLE);
            } else {
                patientCategory.setVisibility(View.GONE);
            }
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_baseline_repeat))) {
                reasonBaselineRepeat.setVisibility(View.VISIBLE);
            } else {
                reasonBaselineRepeat.setVisibility(View.GONE);
            }
        }
        if (spinner == specimenComeFrom.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                otherSpecimentComeFrom.setVisibility(View.VISIBLE);
            } else {
                otherSpecimentComeFrom.setVisibility(View.GONE);
            }
        }
        if (spinner == whySampleRejcted.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ctb_other_title))) {
                reasonForRejection.setVisibility(View.VISIBLE);
            } else {
                reasonForRejection.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        sampleSubmitDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
        reasonBaselineRepeat.setVisibility(View.GONE);
        specimenComeFrom.setVisibility(View.GONE);
        otherSpecimentComeFrom.setVisibility(View.GONE);
        whySampleRejcted.setVisibility(View.GONE);
        reasonForRejection.setVisibility(View.GONE);
        cartridgeId.setVisibility(View.GONE);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == specimenType.getRadioGroup()) {
            if (specimenType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_extra_pulmonary))) {
                specimenComeFrom.setVisibility(View.VISIBLE);

            } else {
                specimenComeFrom.setVisibility(View.GONE);
            }
        }
        if (group == sampleAcceptedByTechnician.getRadioGroup()) {
            if (sampleAcceptedByTechnician.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_rejected))) {
                whySampleRejcted.setVisibility(View.VISIBLE);

            } else {
                whySampleRejcted.setVisibility(View.GONE);
            }
            if (sampleAcceptedByTechnician.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ctb_accepted))) {
                cartridgeId.setVisibility(View.VISIBLE);
                reasonForRejection.setVisibility(View.GONE);


            } else {
                cartridgeId.setVisibility(View.GONE);
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

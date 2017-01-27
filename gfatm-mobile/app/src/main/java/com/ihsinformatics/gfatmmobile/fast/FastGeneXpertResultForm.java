package com.ihsinformatics.gfatmmobile.fast;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 1/24/2017.
 */

public class FastGeneXpertResultForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText cartridgeId;
    TitledButton dateTestResult;
    TitledSpinner gxpResult;
    TitledSpinner mtbBurden;
    TitledSpinner rifResult;
    TitledEditText errorCode;

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
        FORM_NAME = Forms.FAST_GENEXPERT_RESULT_FORM;

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
        cartridgeId = new TitledEditText(context, null, getResources().getString(R.string.fast_cartridge_id), "", "", 50, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        dateTestResult = new TitledButton(context, null, getResources().getString(R.string.fast_date_of_result_recieved), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        gxpResult = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_genexpert_mtb_result), getResources().getStringArray(R.array.fast_genexpert_mtb_result_list), getResources().getString(R.string.fast_mtb_not_detected), App.VERTICAL);
        mtbBurden = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_mtb_burden), getResources().getStringArray(R.array.fast_mtb_burden_list), getResources().getString(R.string.fast_very_low), App.VERTICAL);
        mtbBurden.setVisibility(View.GONE);
        rifResult = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_if_mtb_then_rif_result), getResources().getStringArray(R.array.fast_if_mtb_then_rif_list), getResources().getString(R.string.fast_not_detected), App.VERTICAL);
        rifResult.setVisibility(View.GONE);
        errorCode = new TitledEditText(context, null, getResources().getString(R.string.fast_error_code), "", "", 15, RegexUtil.NumericFilter, InputType.TYPE_CLASS_PHONE, App.VERTICAL, false);
        errorCode.setVisibility(View.GONE);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), cartridgeId.getEditText(), dateTestResult.getButton(), gxpResult.getSpinner(),
                mtbBurden.getSpinner(), rifResult.getSpinner(), errorCode.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, cartridgeId, dateTestResult, gxpResult, mtbBurden, rifResult, errorCode}};

        formDate.getButton().setOnClickListener(this);
        dateTestResult.getButton().setOnClickListener(this);
        gxpResult.getSpinner().setOnItemSelectedListener(this);
        mtbBurden.getSpinner().setOnItemSelectedListener(this);
        rifResult.getSpinner().setOnItemSelectedListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dateTestResult.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());

    }

    @Override
    public boolean validate() {
        Boolean error = false;

        if (cartridgeId.getVisibility() == View.VISIBLE && App.get(cartridgeId).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            cartridgeId.getEditText().setError(getString(R.string.empty_field));
            cartridgeId.getEditText().requestFocus();
            error = true;
        }

        if (errorCode.getVisibility() == View.VISIBLE && App.get(errorCode).isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            errorCode.getEditText().setError(getString(R.string.empty_field));
            errorCode.getEditText().requestFocus();
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

        if (view == dateTestResult.getButton()) {
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
        if (spinner == gxpResult.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_mtb_detected))) {
                mtbBurden.setVisibility(View.VISIBLE);
                rifResult.setVisibility(View.VISIBLE);
            } else {
                mtbBurden.setVisibility(View.GONE);
                rifResult.setVisibility(View.GONE);
            }

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_error))) {
                errorCode.setVisibility(View.VISIBLE);
            } else {
                errorCode.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
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

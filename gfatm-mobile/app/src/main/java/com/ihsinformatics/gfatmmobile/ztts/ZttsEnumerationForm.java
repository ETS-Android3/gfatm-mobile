package com.ihsinformatics.gfatmmobile.ztts;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.shared.RequestType;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by New User on 29-Dec-16.
 */

public class ZttsEnumerationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledRadioGroup formType;

    TitledButton formDate;
    TitledEditText blockCode;
    MyTextView blockBuildingLevel;
    TitledEditText total_build;
    TitledEditText building_na;
    TitledEditText empty_plot_na;
    TitledEditText school_na;
    TitledEditText commercial_na;
    TitledEditText other_na;
    TitledEditText build_refused;
    TitledEditText build_accessed;

    //house hold level
    TitledEditText block_code;
    TitledEditText building_code;
    TitledEditText total_dwellings;
    TitledEditText total_households;
    MyTextView houseHoldLevel;
    TitledButton addDwelling;
    LinearLayout dynamicViewsLayout;
    static int countDwellings = 0;
    static int TotalHouseholds = 0;
    String stringcountbuildings = "";
    String stringcountDwellings = "";
    String stringcountHouseHoldes = "";
    static int count_building_na = 0;
    ArrayList<LinearLayout> houseHoldList = new ArrayList<>();


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
        FORM_NAME = Forms.ZTTS_ENUMERATION;
        FORM = Forms.ztts_enumerationForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ZttsEnumerationForm.MyAdapter());
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
        countDwellings = 0;
        TotalHouseholds = 0;
        formType = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_select_form_type), getResources().getStringArray(R.array.ztts_form_type), "", App.HORIZONTAL, App.VERTICAL);
        formType.getQuestionView().setPadding(0, 0, 0, 10);
        formType.getRadioGroup().setPadding(0, 0, 0, 20);
        blockCode = new TitledEditText(context, null, getResources().getString(R.string.ztts_block_code), "", getResources().getString(R.string.ztts_block_code_hint), 5, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        blockBuildingLevel = new MyTextView(context, getResources().getString(R.string.ztts_block_code_building_level));
        blockBuildingLevel.setTypeface(null, Typeface.BOLD);
        total_build = new TitledEditText(context, null, getResources().getString(R.string.ztts_total_build), "", getResources().getString(R.string.ztts_total_build), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        building_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_na), "", getResources().getString(R.string.ztts_build_na), 6, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        empty_plot_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_empty_plot_na), "", getResources().getString(R.string.ztts_empty_plot_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        school_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_school_na), "", getResources().getString(R.string.ztts_school_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        commercial_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_commercial_na), "", getResources().getString(R.string.ztts_commercial_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        other_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_other_na), "", getResources().getString(R.string.ztts_other_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        build_refused = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_refused), "", getResources().getString(R.string.ztts_build_refused), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        build_accessed = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_accessed), "", getResources().getString(R.string.ztts_build_accessed), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        block_code = new TitledEditText(context, null, getResources().getString(R.string.ztts_block_code), "", getResources().getString(R.string.ztts_block_code_hint), 5, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        building_code = new TitledEditText(context, null, getResources().getString(R.string.ztts_building_code), "", getResources().getString(R.string.ztts_building_code), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        total_dwellings = new TitledEditText(context, null, getResources().getString(R.string.ztts_total_dwellings), "", getResources().getString(R.string.ztts_total_dwellings), 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        total_households = new TitledEditText(context, null, getResources().getString(R.string.ztts_total_households), "", getResources().getString(R.string.ztts_total_households), 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        houseHoldLevel = new MyTextView(context, getResources().getString(R.string.ztts_house_hold_level));
        houseHoldLevel.setTypeface(null, Typeface.BOLD);
        addDwelling = new TitledButton(context, null, null, "Add Dwelling", App.HORIZONTAL);
        dynamicViewsLayout = new LinearLayout(context);
        dynamicViewsLayout.setOrientation(LinearLayout.VERTICAL);


        // Used for reset fields...
        views = new View[]{formType.getRadioGroup(), formDate.getButton(), blockCode.getEditText(), block_code.getEditText(), total_build.getEditText(),
                empty_plot_na.getEditText(), school_na.getEditText(), commercial_na.getEditText(), other_na.getEditText(), building_na.getEditText(), build_refused.getEditText(),
                build_accessed.getEditText(), block_code.getEditText(), building_code.getEditText(), total_dwellings.getEditText(), total_households.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, blockCode, blockBuildingLevel, total_build, empty_plot_na, school_na,
                        commercial_na, other_na, building_na, build_refused, build_accessed, block_code, building_code, total_dwellings, total_households, houseHoldLevel, dynamicViewsLayout, addDwelling}};

        formType.getRadioGroup().setOnCheckedChangeListener(this);
        formDate.getButton().setOnClickListener(this);

        blockCode.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String blockCodeString = App.get(blockCode);
                if(!blockCodeString.startsWith(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)))){
                    blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)) + blockCodeString);
                }

            }
        });

        block_code.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String blockCodeString = App.get(blockCode);
                if(!blockCodeString.startsWith(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)))){
                    blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)) + blockCodeString);
                }

            }
        });

        empty_plot_na.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (empty_plot_na.getEditText().getText().length() >= 1) {
                    try {

                        if (Integer.parseInt(empty_plot_na.getEditText().getText().toString()) <= Integer.parseInt(total_build.getEditText().getText().toString())) {
                            empty_plot_na.getEditText().setError(null);
                        } else {
                            empty_plot_na.getEditText().setError("value should be <= " + total_build.getEditText().getText().toString());
                        }
                    } catch (Exception e) {

                    }
                }
                int num_empty_plot_na;
                int num_school_na;
                int num_commercial_na;
                int num_other_na;

                if (empty_plot_na.getEditText().getText().length() == 0) {
                    num_empty_plot_na = 0;
                } else {
                    num_empty_plot_na = Integer.parseInt(empty_plot_na.getEditText().getText().toString());
                }

                if (school_na.getEditText().getText().length() == 0) {
                    num_school_na = 0;
                } else {
                    num_school_na = Integer.parseInt(school_na.getEditText().getText().toString());
                }
                if (commercial_na.getEditText().getText().length() == 0) {
                    num_commercial_na = 0;
                } else {
                    num_commercial_na = Integer.parseInt(commercial_na.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_other_na;
                building_na.getEditText().setText(String.valueOf(count_building_na));

            }
        });
        school_na.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (school_na.getEditText().getText().length() >= 1) {
                    try {
                        if (Integer.parseInt(school_na.getEditText().getText().toString()) <= (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()))) {
                            school_na.getEditText().setError(null);
                        } else {
                            school_na.getEditText().setError("value should be <= " + (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString())));
                        }
                    } catch (Exception e) {

                    }
                }

                int num_empty_plot_na;
                int num_school_na;
                int num_commercial_na;
                int num_other_na;

                if (empty_plot_na.getEditText().getText().length() == 0) {
                    num_empty_plot_na = 0;
                } else {
                    num_empty_plot_na = Integer.parseInt(empty_plot_na.getEditText().getText().toString());
                }

                if (school_na.getEditText().getText().length() == 0) {
                    num_school_na = 0;
                } else {
                    num_school_na = Integer.parseInt(school_na.getEditText().getText().toString());
                }
                if (commercial_na.getEditText().getText().length() == 0) {
                    num_commercial_na = 0;
                } else {
                    num_commercial_na = Integer.parseInt(commercial_na.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_other_na;
                building_na.getEditText().setText(String.valueOf(count_building_na));

            }
        });
        commercial_na.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (commercial_na.getEditText().getText().length() >= 1) {
                    try {
                        if (Integer.parseInt(commercial_na.getEditText().getText().toString()) <= (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()))) {
                            commercial_na.getEditText().setError(null);
                        } else {
                            commercial_na.getEditText().setError("value should be <= " + (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString())));
                        }
                    } catch (Exception e) {

                    }
                }
                int num_empty_plot_na;
                int num_school_na;
                int num_commercial_na;
                int num_other_na;

                if (empty_plot_na.getEditText().getText().length() == 0) {
                    num_empty_plot_na = 0;
                } else {
                    num_empty_plot_na = Integer.parseInt(empty_plot_na.getEditText().getText().toString());
                }

                if (school_na.getEditText().getText().length() == 0) {
                    num_school_na = 0;
                } else {
                    num_school_na = Integer.parseInt(school_na.getEditText().getText().toString());
                }
                if (commercial_na.getEditText().getText().length() == 0) {
                    num_commercial_na = 0;
                } else {
                    num_commercial_na = Integer.parseInt(commercial_na.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_other_na;
                building_na.getEditText().setText(String.valueOf(count_building_na));

            }
        });
        other_na.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (other_na.getEditText().getText().length() >= 1) {
                    try {
                        if (Integer.parseInt(other_na.getEditText().getText().toString()) <= (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()) - Integer.parseInt(commercial_na.getEditText().getText().toString()))) {
                            other_na.getEditText().setError(null);
                        } else {
                            other_na.getEditText().setError("value should be <= " + (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()) - Integer.parseInt(commercial_na.getEditText().getText().toString())));
                        }
                    } catch (Exception e) {

                    }
                }
                int num_empty_plot_na;
                int num_school_na;
                int num_commercial_na;
                int num_other_na;

                if (empty_plot_na.getEditText().getText().length() == 0) {
                    num_empty_plot_na = 0;
                } else {
                    num_empty_plot_na = Integer.parseInt(empty_plot_na.getEditText().getText().toString());
                }

                if (school_na.getEditText().getText().length() == 0) {
                    num_school_na = 0;
                } else {
                    num_school_na = Integer.parseInt(school_na.getEditText().getText().toString());
                }
                if (commercial_na.getEditText().getText().length() == 0) {
                    num_commercial_na = 0;
                } else {
                    num_commercial_na = Integer.parseInt(commercial_na.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_other_na;
                building_na.getEditText().setText(String.valueOf(count_building_na));

            }
        });
        build_refused.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (build_refused.getEditText().getText().length() >= 1) {
                    try {

                        if (Integer.parseInt(build_refused.getEditText().getText().toString()) <= (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(building_na.getEditText().getText().toString()))) {
                            build_refused.getEditText().setError(null);

                        } else {
                            build_refused.getEditText().setError("value should be <= " + (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(building_na.getEditText().getText().toString())));
                        }
                    } catch (Exception e) {

                    }
                }
                try {
                    build_accessed.getEditText().setText(String.valueOf((Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(building_na.getEditText().getText().toString()) - Integer.parseInt(build_refused.getEditText().getText().toString()))));

                } catch (Exception e) {
                }


            }
        });

        addDwelling.getButton().setOnClickListener(this);

        resetViews();

    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
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
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
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
        Boolean error = false;
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ztts_block_code_building_level))) {

            if (App.get(blockCode).isEmpty() && blockCode.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                blockCode.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(total_build).isEmpty() && total_build.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                total_build.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(building_na).isEmpty() && building_na.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
//                building_na.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(empty_plot_na).isEmpty() && empty_plot_na.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                empty_plot_na.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(school_na).isEmpty() && school_na.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                school_na.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(commercial_na).isEmpty() && commercial_na.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                commercial_na.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(other_na).isEmpty() && other_na.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                other_na.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(build_accessed).isEmpty() && build_accessed.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                build_accessed.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(build_refused).isEmpty() && build_refused.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                build_refused.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (!error) {
                int num_total_build = 0;
                int num_total_na = 0;
                int num_build_refused = 0;
                int num_build_accessed = 0;
                int sum = 0;

                num_total_build = Integer.parseInt(App.get(total_build));
                num_total_na = Integer.parseInt(App.get(building_na));
                num_build_refused = Integer.parseInt(App.get(build_refused));
                num_build_accessed = Integer.parseInt(App.get(build_accessed));

                sum = num_build_accessed + num_build_refused + num_total_na;
                if (sum == num_total_build) {
                    return true;
                } else if (sum > num_total_build) {
                    showAlert("sum of all buildings is greater than total buildings");
                    return false;
                } else if (sum < num_total_build) {
                    showAlert("sum of all buildings is less than total buildings");
                    return false;
                }
            }


        } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ztts_house_hold_level))) {

            if (App.get(block_code).isEmpty() && block_code.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                block_code.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(building_code).isEmpty() && building_code.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                building_code.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(total_dwellings).isEmpty() && total_dwellings.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                total_dwellings.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(total_households).isEmpty() && total_households.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                total_households.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }

            for (LinearLayout ll : houseHoldList) {
                if (((LinearLayout) ll.getParent()).getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < ll.getChildCount(); i++) {
                        if (ll.getChildAt(i) instanceof TitledEditText) {
                            if (((TitledEditText) ll.getChildAt(i)).getEditText().getText().toString().trim().length() <= 0 && ((TitledEditText) ll.getChildAt(i)).getVisibility() == View.VISIBLE) {
                                ((TitledEditText) ll.getChildAt(i)).getEditText().setError(getString(R.string.empty_field));
                                ((TitledEditText) ll.getChildAt(i)).getEditText().requestFocus();
                                error = true;

                            }
                        }
                    }
                }
            }

            try {
                if (countDwellings != Integer.parseInt(App.get(total_dwellings))) {
                    showAlert(getString(R.string.form_error) + "\n Add remaining dwelling(s) which is equals to " + (Integer.parseInt(App.get(total_dwellings)) - countDwellings));
                    return false;
                }
                if (TotalHouseholds != Integer.parseInt(App.get(total_households))) {
                    showAlert(getString(R.string.form_error) + "\n Add remaining houshold(s) which is equals to " + (Integer.parseInt(App.get(total_households)) - TotalHouseholds));
                    return false;

                }
            } catch (Exception e) {

            }


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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            } else {
                endTime = new Date();
            }
            bundle.putBoolean("save", false);
        } else {
            endTime = new Date();
        }


        final ContentValues values = new ContentValues();

        values.put("location", App.getLocation());
        values.put("entereddate", App.getSqlDate(formDateCalendar));

        /*observations.add(new String[]{"LONGITUDE", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE", String.valueOf(App.getLatitude())});*/

        if (App.get(formType).equals(getResources().getString(R.string.ztts_block_code_building_level))) {

            observations.add(new String[]{"BLOCK_CODE", App.get(blockCode)});
            observations.add(new String[]{"Total Number of Building Block", App.get(total_build)});
            observations.add(new String[]{"Number of empty plots", App.get(empty_plot_na)});
            observations.add(new String[]{"Number of School", App.get(school_na)});
            observations.add(new String[]{"Number of Commercial", App.get(commercial_na)});
            observations.add(new String[]{"Number of Other Not Applicable Buildings", App.get(other_na)});
            observations.add(new String[]{"Total Number of Building Not Applicable", App.get(building_na)});
            observations.add(new String[]{"Number of Building refused access", App.get(build_refused)});
            observations.add(new String[]{"Total Number of Building accessed", App.get(build_accessed)});

        } else if (App.get(formType).equals(getResources().getString(R.string.ztts_house_hold_level))) {

            observations.add(new String[]{"BLOCK_CODE", App.get(block_code)});
            observations.add(new String[]{"BUILDING_CODE", App.get(building_code)});
            observations.add(new String[]{"Total Number of Dwellings", App.get(total_dwellings)});
            observations.add(new String[]{"Total Number of Households", App.get(total_households)});

            JSONArray dwellingArray = new JSONArray();

            for (int i = 0; i < dynamicViewsLayout.getChildCount(); i++) {
                String dwellingCode = ((MyTextView) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(0)).getText().toString();
                String dwellingRefused = ((TitledSpinner) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(1)).getSpinnerValue();

                JSONObject dwellingObj = new JSONObject();
                try {
                    dwellingObj.put("dwell_code",dwellingCode);
                    dwellingObj.put("refused", dwellingRefused);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray householdArray=new JSONArray();

                for (int j = 0; j < ((LinearLayout) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(2)).getChildCount(); j++) {
                    String householdCode;
                    String males;
                    String male_greater_15;
                    String male_2_4;
                    String females;
                    String female_greater_15;
                    String female_2_4;

                    JSONObject houseHoldObj = new JSONObject();

                    LinearLayout householdes = ((LinearLayout) ((LinearLayout) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(2)).getChildAt(j));
                    if (householdes.getChildAt(0).getVisibility() == View.VISIBLE) {

                        householdCode = ((MyTextView) householdes.getChildAt(0)).getText().toString();
                        try {
                            houseHoldObj.put("household_code",householdCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(1).getVisibility() == View.VISIBLE) {

                        males = ((TitledEditText) householdes.getChildAt(1)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Total Number of Males",males);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(2).getVisibility() == View.VISIBLE) {

                        male_greater_15 = ((TitledEditText) householdes.getChildAt(2)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Males greater than 15",male_greater_15);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(3).getVisibility() == View.VISIBLE) {

                        male_2_4 = ((TitledEditText) householdes.getChildAt(3)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Males between 2 and 4",male_2_4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(4).getVisibility() == View.VISIBLE) {

                        females = ((TitledEditText) householdes.getChildAt(4)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Total Number of Females",females);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(5).getVisibility() == View.VISIBLE) {

                        female_greater_15 = ((TitledEditText) householdes.getChildAt(5)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Females greater than 15",female_greater_15);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(6).getVisibility() == View.VISIBLE) {

                        female_2_4 = ((TitledEditText) householdes.getChildAt(6)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Females between 2 and 4",female_2_4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    householdArray.put(houseHoldObj);
                    try {
                        dwellingObj.put("householdes",householdArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dwellingArray.put(dwellingObj);
            }

            observations.add(new String[]{"DWELLINGS", dwellingArray.toString()});

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

                String result = "";

                if (App.get(formType).equals(getResources().getString(R.string.ztts_block_code_building_level))) {
                    result = serverService.submitToGwtApp(RequestType.ZTTS_ENUMERATION_BLOCK, FORM, values, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ztts_house_hold_level))) {
                    result = serverService.submitToGwtApp(RequestType.ZTTS_ENUMERATION_HOUSEHOLD, FORM, values, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                }

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

        //serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getSavedFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("TIME TAKEN TO FILL FORM")) {
                timeTakeToFill = obs[0][1];
            }

            if (obs[0][0].equals("FOLLOW-UP MONTH")) {
                blockCode.getEditText().setText(obs[0][1]);
            }
        }
    }


    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            formDate.getButton().setEnabled(false);
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == addDwelling.getButton()) {
            int num_total_dwellings = 0;
            if (total_dwellings.getEditText().getText().length() == 0) {
                num_total_dwellings = 0;
            } else {
                num_total_dwellings = Integer.parseInt(total_dwellings.getEditText().getText().toString());
            }
            if (countDwellings < num_total_dwellings) {
                block_code.getEditText().setEnabled(false);
                building_code.getEditText().setEnabled(false);
                total_dwellings.getEditText().setEnabled(false);
                total_households.getEditText().setEnabled(false);
                countDwellings++;
                final LinearLayout dwellingLayout = new LinearLayout(context);
                dwellingLayout.setOrientation(LinearLayout.VERTICAL);
                dynamicViewsLayout.addView(dwellingLayout);

                if (String.valueOf(building_code.getEditText().getText().toString()).length() <= 1) {
                    stringcountbuildings = "0" + building_code.getEditText().getText().toString().trim();
                } else {
                    stringcountbuildings = "" + building_code.getEditText().getText().toString().trim();

                }

                if (String.valueOf(countDwellings).length() <= 1) {
                    stringcountDwellings = "0" + countDwellings;
                } else {
                    stringcountDwellings = "" + countDwellings;
                }
                final MyTextView dwellingCode = new MyTextView(context, "Dwelling Code : " + (stringcountbuildings + "-" + stringcountDwellings));
                dwellingCode.setPadding(0, 100, 0, 20);
                dwellingCode.setTypeface(null, Typeface.BOLD);
                dwellingCode.setTextColor(Color.parseColor("#CF0000"));
                dwellingLayout.addView(dwellingCode);

                final TitledSpinner dwelling_refused = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_house_hold_refused), getResources().getStringArray(R.array.ztts_yes_no), "", App.HORIZONTAL);
                dwelling_refused.getSpinner().setSelection(1);
                dwelling_refused.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            TotalHouseholds -= ((LinearLayout) dwellingLayout.getChildAt(2)).getChildCount();
                            for (int j = 0; j < ((LinearLayout) dwellingLayout.getChildAt(2)).getChildCount(); j++) {
                                houseHoldList.remove((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(j));

                            }
                            ((LinearLayout) dwellingLayout.getChildAt(2)).removeAllViews();
                            dwellingLayout.getChildAt(3).setVisibility(View.GONE);
                        } else if (i == 1) {
                            dwellingLayout.getChildAt(3).setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                dwellingLayout.addView(dwelling_refused);
                final LinearLayout household_nested = new LinearLayout(context);
                household_nested.setOrientation(LinearLayout.VERTICAL);
                dwellingLayout.addView(household_nested);


                final TitledButton addHouseHold = new TitledButton(context, null, null, "Add Household", App.HORIZONTAL);
                addHouseHold.getButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int num_total_households = 0;
                        if (total_households.getEditText().getText().length() == 0) {
                            num_total_households = 0;
                        } else {
                            num_total_households = Integer.parseInt(total_households.getEditText().getText().toString());
                        }
                        if (TotalHouseholds < num_total_households) {
                            TotalHouseholds++;
                            LinearLayout houseHoldLayout = new LinearLayout(context);
                            houseHoldLayout.setOrientation(LinearLayout.VERTICAL);
                            household_nested.addView(houseHoldLayout);

                            houseHoldList.add(houseHoldLayout);

                            final int countHouseHoldes = ((LinearLayout) dwellingLayout.getChildAt(2)).getChildCount();
                            if (String.valueOf(countHouseHoldes).length() <= 1) {
                                stringcountHouseHoldes = "0" + countHouseHoldes;
                            } else {
                                stringcountHouseHoldes = "" + countHouseHoldes;
                            }
                            final MyTextView householdCode = new MyTextView(context, "Household Code : " + dwellingCode.getText().toString().split(":")[1].trim() + "-" + stringcountHouseHoldes);
                            householdCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
                            DrawableCompat.setTint(householdCode.getCompoundDrawables()[2], Color.RED);
                            householdCode.setTypeface(null, Typeface.BOLD);
                            householdCode.setTextColor(Color.parseColor("#00CF00"));
                            if (countHouseHoldes == 1) {
                                householdCode.setPadding(0, 20, 0, 20);
                            } else {
                                householdCode.setPadding(0, 50, 0, 20);
                            }

                            houseHoldLayout.addView(householdCode);


                            int totalHouseholdsInHouseholdLayout = ((LinearLayout) dwellingLayout.getChildAt(2)).getChildCount();


                            for (int w = 0; w < totalHouseholdsInHouseholdLayout; w++) {
                                ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(w)).getChildAt(0).setClickable(false);
                                ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(w)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            }
                            ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayout - 1)).getChildAt(0).setClickable(true);
                            ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayout - 1)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
                            DrawableCompat.setTint((((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayout - 1)).getChildAt(0))).getCompoundDrawables()[2], Color.RED);


                            householdCode.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                                    alertDialog.setMessage("Are you sure you want to delet ?");
                                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_warning);
                                    DrawableCompat.setTint(clearIcon, color);
                                    alertDialog.setIcon(clearIcon);
                                    alertDialog.setTitle(getResources().getString(R.string.title_alert));
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    int totalHouseholdsInHouseholdLayout = ((LinearLayout) dwellingLayout.getChildAt(2)).getChildCount();

                                                    LinearLayout layoutToDelete = ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayout - 1));
                                                    ((LinearLayout) dwellingLayout.getChildAt(2)).removeView(layoutToDelete);
                                                    houseHoldList.remove(layoutToDelete);
                                                    TotalHouseholds--;

                                                    int totalHouseholdsInHouseholdLayoutAfterDelet = ((LinearLayout) dwellingLayout.getChildAt(2)).getChildCount();


                                                    for (int w = 0; w < totalHouseholdsInHouseholdLayoutAfterDelet; w++) {
                                                        ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(w)).getChildAt(0).setClickable(false);
                                                        ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(w)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                                    }
                                                    try {
                                                        ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayoutAfterDelet - 1)).getChildAt(0).setClickable(true);
                                                        ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayoutAfterDelet - 1)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
                                                        DrawableCompat.setTint((((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(totalHouseholdsInHouseholdLayoutAfterDelet - 1)).getChildAt(0))).getCompoundDrawables()[2], Color.RED);


                                                    } catch (Exception e) {

                                                    }


                                                    try {
                                                        InputMethodManager imm = (InputMethodManager) mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
                                                    } catch (Exception e) {
                                                        // TODO: handle exception
                                                    }
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
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
                                }
                            });

                            final TitledEditText males = new TitledEditText(context, null, "Number of Males" + " ", "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            males.setTag("household_" + countDwellings + "_" + countHouseHoldes);
                            males.getEditText().addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    int num;
                                    if (males.getEditText().getText().length() == 0) {
                                        num = 0;
                                    } else {
                                        num = Integer.parseInt(males.getEditText().getText().toString());
                                    }
                                    if (num > 0) {
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(2).setVisibility(View.VISIBLE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(3).setVisibility(View.VISIBLE);

                                    } else {
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(2).setVisibility(View.GONE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(3).setVisibility(View.GONE);
                                    }

                                }
                            });
                            houseHoldLayout.addView(males);

                            TitledEditText males_greater_15 = new TitledEditText(context, null, "Male greater than 15" + " ", "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            houseHoldLayout.addView(males_greater_15);
                            males_greater_15.setVisibility(View.GONE);


                            TitledEditText males_2_to_4 = new TitledEditText(context, null, "Male 2-4" + " ", "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            houseHoldLayout.addView(males_2_to_4);
                            males_2_to_4.setVisibility(View.GONE);


                            final TitledEditText females = new TitledEditText(context, null, "Number of Females " + " ", "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            females.setTag("household_" + countDwellings + "_" + countHouseHoldes);
                            females.getEditText().addTextChangedListener(new TextWatcher() {

                                @Override
                                public void afterTextChanged(Editable s) {
                                }

                                @Override
                                public void beforeTextChanged(CharSequence s, int start,
                                                              int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start,
                                                          int before, int count) {
                                    int num;
                                    if (females.getEditText().getText().length() == 0) {
                                        num = 0;
                                    } else {
                                        num = Integer.parseInt(females.getEditText().getText().toString());
                                    }
                                    if (num > 0) {
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(5).setVisibility(View.VISIBLE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(6).setVisibility(View.VISIBLE);

                                    } else {
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(5).setVisibility(View.GONE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(2)).getChildAt(countHouseHoldes - 1))).getChildAt(6).setVisibility(View.GONE);
                                    }

                                }
                            });
                            houseHoldLayout.addView(females);

                            TitledEditText females_greater_15 = new TitledEditText(context, null, "Female greater than 15" + " ", "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            houseHoldLayout.addView(females_greater_15);
                            females_greater_15.setVisibility(View.GONE);

                            TitledEditText females_2_to_4 = new TitledEditText(context, null, "Female 2-4" + " ", "", "", 50, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            houseHoldLayout.addView(females_2_to_4);
                            females_2_to_4.setVisibility(View.GONE);
                        } else {
                            showAlert("You can't add more household(s)");
                        }
                    }
                });
                dwellingLayout.addView(addHouseHold);
            } else {
                int totaldwellings = 0;
                try {
                    totaldwellings = Integer.parseInt(App.get(total_dwellings));
                } catch (Exception e) {

                }
                if (totaldwellings <= 0) {
                    validate();
                } else {
                    showAlert("You can't add more dwelling(s)");
                }
            }

        }
    }


    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /*MySpinner spinner = (MySpinner) parent;
        if (spinner == household_refused.getSpinner()) {
            if ((household_refused.getSpinner().getSelectedItemPosition() == 0)) {

            } else {

            }
        }*/
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

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
        building_na.getEditText().setFocusable(false);
        build_accessed.getEditText().setFocusable(false);


        blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)));
        block_code.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)));
        submitButton.setEnabled(false);
        for (View v : viewGroups[0]) {
            v.setVisibility(View.GONE);
        }
        viewGroups[0][0].setVisibility(View.VISIBLE);
        /////////////////////////////////////////////////////////////
        dynamicViewsLayout.removeAllViews();
        houseHoldList.removeAll(houseHoldList);
        houseHoldList.clear();

        block_code.getEditText().setEnabled(true);
        building_code.getEditText().setEnabled(true);
        total_dwellings.getEditText().setEnabled(true);
        total_households.getEditText().setEnabled(true);

        countDwellings = 0;
        TotalHouseholds = 0;
        stringcountbuildings = "";
        stringcountDwellings = "";
        stringcountHouseHoldes = "";
        count_building_na = 0;

        if (App.getMode().equalsIgnoreCase("OFFLINE"))
            submitButton.setEnabled(false);
        else
            submitButton.setEnabled(true);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == formType.getRadioGroup()) {

            showTestOrderOrTestResult();
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

    void showTestOrderOrTestResult() {
        //   formDate.setVisibility(View.VISIBLE);
        if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ztts_block_code_building_level))) {
            submitButton.setEnabled(true);

            blockCode.setVisibility(View.VISIBLE);
            formDate.setVisibility(View.VISIBLE);
            blockBuildingLevel.setVisibility(View.VISIBLE);
            total_build.setVisibility(View.VISIBLE);
            building_na.setVisibility(View.VISIBLE);
            empty_plot_na.setVisibility(View.VISIBLE);
            school_na.setVisibility(View.VISIBLE);
            commercial_na.setVisibility(View.VISIBLE);
            other_na.setVisibility(View.VISIBLE);
            build_refused.setVisibility(View.VISIBLE);
            build_accessed.setVisibility(View.VISIBLE);

            block_code.setVisibility(View.GONE);
            building_code.setVisibility(View.GONE);
            total_dwellings.setVisibility(View.GONE);
            total_households.setVisibility(View.GONE);
            houseHoldLevel.setVisibility(View.GONE);
            addDwelling.setVisibility(View.GONE);
            dynamicViewsLayout.setVisibility(View.GONE);

        } else if (formType.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.ztts_house_hold_level))) {
            submitButton.setEnabled(true);

            blockCode.setVisibility(View.GONE);
            formDate.setVisibility(View.GONE);
            blockBuildingLevel.setVisibility(View.GONE);
            total_build.setVisibility(View.GONE);
            building_na.setVisibility(View.GONE);
            empty_plot_na.setVisibility(View.GONE);
            school_na.setVisibility(View.GONE);
            commercial_na.setVisibility(View.GONE);
            other_na.setVisibility(View.GONE);
            build_refused.setVisibility(View.GONE);
            build_accessed.setVisibility(View.GONE);

            block_code.setVisibility(View.VISIBLE);
            building_code.setVisibility(View.VISIBLE);
            total_dwellings.setVisibility(View.VISIBLE);
            total_households.setVisibility(View.VISIBLE);
            houseHoldLevel.setVisibility(View.VISIBLE);
            addDwelling.setVisibility(View.VISIBLE);
            dynamicViewsLayout.setVisibility(View.VISIBLE);

        }
    }

    void showAlert(String message) {
        int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

        final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
        alertDialog.setMessage(message);
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
    }

}



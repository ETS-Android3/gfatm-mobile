package com.ihsinformatics.gfatmmobile.ztts;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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

import de.greenrobot.event.EventBus;

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
    TitledEditText door_locked;
    TitledEditText other_na;
    TitledEditText build_refused;
    TitledEditText build_accessed;

    //house hold level
    TitledEditText block_code;
    TitledEditText building_code;
    TitledRadioGroup is_building_accessed;
    TitledSpinner reason_building_not_accessed;
    TitledEditText if_other;
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
     * CHANGE pageCount and formName Variable only...
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.ZTTS_ENUMERATION;
        form = Forms.ztts_enumerationForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ZttsEnumerationForm.MyAdapter());
        pager.setOnPageChangeListener(this);
        navigationSeekbar.setMax(pageCount - 1);
        formNameView.setText(formName);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if (App.isLanguageRTL()) {
            for (int i = pageCount - 1; i >= 0; i--) {
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
            for (int i = 0; i < pageCount; i++) {
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
        EventBus.getDefault().register(this);
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
        door_locked = new TitledEditText(context, null, getResources().getString(R.string.ztts_door_locked), "", getResources().getString(R.string.ztts_door_locked), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        other_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_other_na), "", getResources().getString(R.string.ztts_other_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        build_refused = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_refused), "", getResources().getString(R.string.ztts_build_refused), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        build_accessed = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_accessed), "", getResources().getString(R.string.ztts_build_accessed), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        block_code = new TitledEditText(context, null, getResources().getString(R.string.ztts_block_code), "", getResources().getString(R.string.ztts_block_code_hint), 5, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        building_code = new TitledEditText(context, null, getResources().getString(R.string.ztts_building_code), "", getResources().getString(R.string.ztts_building_code), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        is_building_accessed = new TitledRadioGroup(context, null, getResources().getString(R.string.ztts_building_accessed), getResources().getStringArray(R.array.ztts_yes_no), "", App.HORIZONTAL, App.HORIZONTAL, true);
        reason_building_not_accessed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_reason_building_not_accessed), getResources().getStringArray(R.array.ztts_reason_building_not_accessed_options), "", App.HORIZONTAL);
        if_other = new TitledEditText(context, null, getResources().getString(R.string.ztts_reason_building_not_accessed_if_other), "", getResources().getString(R.string.ztts_reason_building_not_accessed_if_other), 250, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        total_dwellings = new TitledEditText(context, null, getResources().getString(R.string.ztts_total_dwellings), "", getResources().getString(R.string.ztts_total_dwellings), 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        total_households = new TitledEditText(context, null, getResources().getString(R.string.ztts_total_households), "", getResources().getString(R.string.ztts_total_households), 5, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        houseHoldLevel = new MyTextView(context, getResources().getString(R.string.ztts_house_hold_level));
        houseHoldLevel.setTypeface(null, Typeface.BOLD);
        addDwelling = new TitledButton(context, null, null, "Add Dwelling", App.HORIZONTAL);
        dynamicViewsLayout = new LinearLayout(context);
        dynamicViewsLayout.setOrientation(LinearLayout.VERTICAL);


        // Used for reset fields...
        views = new View[]{formType.getRadioGroup(), formDate.getButton(), blockCode.getEditText(), block_code.getEditText(), total_build.getEditText(),
                empty_plot_na.getEditText(), school_na.getEditText(), commercial_na.getEditText(), door_locked.getEditText(), other_na.getEditText(), building_na.getEditText(), build_refused.getEditText(),
                build_accessed.getEditText(), block_code.getEditText(), building_code.getEditText(), is_building_accessed.getRadioGroup(), reason_building_not_accessed.getSpinner(), if_other.getEditText(), total_dwellings.getEditText(), total_households.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formType, formDate, blockCode, blockBuildingLevel, total_build, empty_plot_na, school_na,
                        commercial_na, door_locked, other_na, building_na, build_refused, build_accessed, block_code, building_code, is_building_accessed, reason_building_not_accessed, if_other, total_dwellings, total_households, houseHoldLevel, dynamicViewsLayout, addDwelling}};

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
                if (!String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)).equals(blockCode.toString().toUpperCase().charAt(1)) && blockCodeString.length() >= 2) {
                    StringBuilder sb = new StringBuilder(blockCodeString);
                    blockCodeString = sb.deleteCharAt(1).toString();
                }
                if (!blockCodeString.startsWith(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)))) {
                    blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)) + blockCodeString);
                }
                if (blockCode.getEditText().getSelectionStart() == 0)
                    blockCode.getEditText().setSelection(1);

                if (blockCode.getEditText().getText().toString().trim().length() < 4) {
                    blockCode.getEditText().setError("Length shouldn't be < 4");
                } else if (blockCode.getEditText().getText().toString().trim().length() > 5) {
                    blockCode.getEditText().setError("Length shouldn't be > 5");
                } else {
                    blockCode.getEditText().setError(null);
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

                String blockCodeString = App.get(block_code);
                if (!String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)).equals(block_code.toString().toUpperCase().charAt(1)) && blockCodeString.length() >= 2) {
                    StringBuilder sb = new StringBuilder(blockCodeString);
                    blockCodeString = sb.deleteCharAt(1).toString();
                }
                if (!blockCodeString.startsWith(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)))) {
                    block_code.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)) + blockCodeString);
                }
                if (block_code.getEditText().getSelectionStart() == 0)
                    block_code.getEditText().setSelection(1);

                if (block_code.getEditText().getText().toString().trim().length() < 4) {
                    block_code.getEditText().setError("Length shouldn't be < 4");
                } else if (block_code.getEditText().getText().toString().trim().length() > 5) {
                    block_code.getEditText().setError("Length shouldn't be > 5");
                } else {
                    block_code.getEditText().setError(null);
                }

            }
        });
        total_build.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int num_total_build;
                int num_build_na;
                int num_build_refused;

                if (total_build.getEditText().getText().length() == 0) {
                    num_total_build = 0;
                } else {
                    num_total_build = Integer.parseInt(total_build.getEditText().getText().toString());
                }
                if (building_na.getEditText().getText().length() == 0) {
                    num_build_na = 0;
                } else {
                    num_build_na = Integer.parseInt(building_na.getEditText().getText().toString());
                }
                if (build_refused.getEditText().getText().length() == 0) {
                    num_build_refused = 0;
                } else {
                    num_build_refused = Integer.parseInt(build_refused.getEditText().getText().toString());
                }
                int num_build_accessed = num_total_build - num_build_na - num_build_refused;
                build_accessed.getEditText().setText("" + num_build_accessed);


            }
        });
        building_na.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int num_total_build;
                int num_build_na;
                int num_build_refused;

                if (total_build.getEditText().getText().length() == 0) {
                    num_total_build = 0;
                } else {
                    num_total_build = Integer.parseInt(total_build.getEditText().getText().toString());
                }
                if (building_na.getEditText().getText().length() == 0) {
                    num_build_na = 0;
                } else {
                    num_build_na = Integer.parseInt(building_na.getEditText().getText().toString());
                }
                if (build_refused.getEditText().getText().length() == 0) {
                    num_build_refused = 0;
                } else {
                    num_build_refused = Integer.parseInt(build_refused.getEditText().getText().toString());
                }
                int num_build_accessed = num_total_build - num_build_na - num_build_refused;
                build_accessed.getEditText().setText("" + num_build_accessed);


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
                int num_door_locked;
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
                if (door_locked.getEditText().getText().length() == 0) {
                    num_door_locked = 0;
                } else {
                    num_door_locked = Integer.parseInt(door_locked.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_door_locked + num_other_na;
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
                int num_door_locked;
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
                if (door_locked.getEditText().getText().length() == 0) {
                    num_door_locked = 0;
                } else {
                    num_door_locked = Integer.parseInt(door_locked.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_door_locked + num_other_na;
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
                int num_door_locked;
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
                if (door_locked.getEditText().getText().length() == 0) {
                    num_door_locked = 0;
                } else {
                    num_door_locked = Integer.parseInt(door_locked.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_door_locked + num_other_na;
                building_na.getEditText().setText(String.valueOf(count_building_na));

            }
        });
        door_locked.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (door_locked.getEditText().getText().length() >= 1) {
                    try {
                        if (Integer.parseInt(door_locked.getEditText().getText().toString()) <= (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()) - Integer.parseInt(commercial_na.getEditText().getText().toString()))) {
                            door_locked.getEditText().setError(null);
                        } else {
                            door_locked.getEditText().setError("value should be <= " + (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()) - Integer.parseInt(commercial_na.getEditText().getText().toString())));
                        }
                    } catch (Exception e) {

                    }
                }
                int num_empty_plot_na;
                int num_school_na;
                int num_commercial_na;
                int num_door_locked;
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
                if (door_locked.getEditText().getText().length() == 0) {
                    num_door_locked = 0;
                } else {
                    num_door_locked = Integer.parseInt(door_locked.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_door_locked + num_other_na;
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
                        if (Integer.parseInt(other_na.getEditText().getText().toString()) <= (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()) - Integer.parseInt(commercial_na.getEditText().getText().toString()) - Integer.parseInt(door_locked.getEditText().getText().toString()))) {
                            other_na.getEditText().setError(null);
                        } else {
                            other_na.getEditText().setError("value should be <= " + (Integer.parseInt(total_build.getEditText().getText().toString()) - Integer.parseInt(empty_plot_na.getEditText().getText().toString()) - Integer.parseInt(school_na.getEditText().getText().toString()) - Integer.parseInt(commercial_na.getEditText().getText().toString()) - Integer.parseInt(door_locked.getEditText().getText().toString())));
                        }
                    } catch (Exception e) {

                    }
                }
                int num_empty_plot_na;
                int num_school_na;
                int num_commercial_na;
                int num_door_locked;
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
                if (door_locked.getEditText().getText().length() == 0) {
                    num_door_locked = 0;
                } else {
                    num_door_locked = Integer.parseInt(door_locked.getEditText().getText().toString());
                }
                if (other_na.getEditText().getText().length() == 0) {
                    num_other_na = 0;
                } else {
                    num_other_na = Integer.parseInt(other_na.getEditText().getText().toString());
                }
                count_building_na = num_empty_plot_na + num_school_na + num_commercial_na + num_door_locked + num_other_na;
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
        is_building_accessed.getRadioGroup().setOnCheckedChangeListener(this);
        reason_building_not_accessed.getSpinner().setOnItemSelectedListener(this);

        resetViews();

    }

    public void onEvent(String event) {
        // your implementation
        blockCode.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)));
        block_code.getEditText().setText(String.valueOf(App.getLocation().toString().toUpperCase().charAt(0)));
    }

    @Override
    public void updateDisplay() {

        //formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            //String personDOB = App.getPatient().getPerson().getBirthdate();

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            }/* else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            }*/ else
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
            } else {
                if (blockCode.getEditText().getText().toString().trim().length() < 4) {
                    blockCode.getEditText().setError("Length shouldn't be < 4");
                    blockCode.getEditText().requestFocus();
                    error = true;
                }
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
            if (App.get(door_locked).isEmpty() && door_locked.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                door_locked.getEditText().setError(getString(R.string.empty_field));
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
            } else {
                if (block_code.getEditText().getText().toString().trim().length() < 4) {
                    block_code.getEditText().setError("Length shouldn't be < 4");
                    block_code.getEditText().requestFocus();
                    error = true;
                }
            }
            if (App.get(building_code).isEmpty() && building_code.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                building_code.getEditText().setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(is_building_accessed).isEmpty() && is_building_accessed.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
//                is_building_accessed.getQuestionView().setError(getString(R.string.empty_field));
                is_building_accessed.getRadioGroup().getButtons().get(1).setError(getString(R.string.empty_field));
                error = true;
            }
            if (App.get(if_other).isEmpty() && if_other.getVisibility() == View.VISIBLE) {
                gotoFirstPage();
                if_other.getEditText().setError(getString(R.string.empty_field));
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
                if (Integer.parseInt(App.get(total_households)) == 0) {

                } else {
                    if (TotalHouseholds != Integer.parseInt(App.get(total_households))) {
                        showAlert(getString(R.string.form_error) + "\n Add remaining houshold(s) which is equals to " + (Integer.parseInt(App.get(total_households)) - TotalHouseholds));
                        return false;

                    }
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

        final Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                Boolean flag = serverService.deleteOfflineForms(encounterId);
                if(!flag){

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

        observations.add(new String[]{"COUNTRY", App.getCountry()});
        observations.add(new String[]{"PROVINCE", App.getProvince()});
        observations.add(new String[]{"CITY", "KARACHI"});

        if (App.get(formType).equals(getResources().getString(R.string.ztts_block_code_building_level))) {

            observations.add(new String[]{"BLOCK_CODE", App.get(blockCode)});
            observations.add(new String[]{"Total Number of Building Block", App.get(total_build)});
            observations.add(new String[]{"Number of empty plots", App.get(empty_plot_na)});
            observations.add(new String[]{"Number of School", App.get(school_na)});
            observations.add(new String[]{"Number of Commercial", App.get(commercial_na)});
            observations.add(new String[]{"Door Locked", App.get(door_locked)});
            observations.add(new String[]{"Number of Other Not Applicable Buildings", App.get(other_na)});
            observations.add(new String[]{"Total Number of Building Not Applicable", App.get(building_na)});
            observations.add(new String[]{"Number of Building refused access", App.get(build_refused)});
            observations.add(new String[]{"Total Number of Building accessed", App.get(build_accessed)});

        } else if (App.get(formType).equals(getResources().getString(R.string.ztts_house_hold_level))) {
            if (block_code.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"BLOCK_CODE", App.get(block_code)});
            if (building_code.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"BUILDING_CODE", App.get(building_code)});
            if (is_building_accessed.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"Building Accessed", App.get(is_building_accessed)});
            if (reason_building_not_accessed.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"Reason Building not Accessed", App.get(reason_building_not_accessed)});
            if (if_other.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"Other Reason Building not Accessed", App.get(if_other)});
            if (total_dwellings.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"Total Number of Dwellings", App.get(total_dwellings)});
            if (total_households.getVisibility() == View.VISIBLE)
                observations.add(new String[]{"Total Number of Households", App.get(total_households)});

            JSONArray dwellingArray = new JSONArray();

            for (int i = 0; i < dynamicViewsLayout.getChildCount(); i++) {
                String dwellingCode = ((MyTextView) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(0)).getText().toString();
                String dwellingAccessed = ((TitledSpinner) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(1)).getSpinnerValue();
                String reasonDwellingNotAccess = ((TitledSpinner) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(2)).getSpinnerValue();

                JSONObject dwellingObj = new JSONObject();
                try {
                    dwellingObj.put("dwell_code", dwellingCode);
                    dwellingObj.put("refused", dwellingAccessed.equalsIgnoreCase("Yes") ? "No" : "Yes");
                    if (((TitledSpinner) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(2)).getVisibility() == View.VISIBLE)
                        dwellingObj.put("Reason Dwelling not Accessed", reasonDwellingNotAccess);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray householdArray = new JSONArray();

                for (int j = 0; j < ((LinearLayout) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(3)).getChildCount(); j++) {
                    String householdCode;
                    String householdAccessed;
                    String reasonHouseholdNotAccessed;
                    String males;
                    String male_greater_15;
                    String male_2_4;
                    String females;
                    String female_greater_15;
                    String female_2_4;

                    JSONObject houseHoldObj = new JSONObject();

                    LinearLayout householdes = ((LinearLayout) ((LinearLayout) ((LinearLayout) dynamicViewsLayout.getChildAt(i)).getChildAt(3)).getChildAt(j));
                    if (householdes.getChildAt(0).getVisibility() == View.VISIBLE) {

                        householdCode = ((MyTextView) householdes.getChildAt(0)).getText().toString();
                        try {
                            houseHoldObj.put("household_code", householdCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (householdes.getChildAt(1).getVisibility() == View.VISIBLE) {
                        householdAccessed = ((TitledSpinner) householdes.getChildAt(1)).getSpinnerValue();
                        try {
                            houseHoldObj.put("Household Accessed", householdAccessed);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(2).getVisibility() == View.VISIBLE) {
                        reasonHouseholdNotAccessed = ((TitledSpinner) householdes.getChildAt(2)).getSpinnerValue();
                        try {
                            houseHoldObj.put("Reason Household not Accessed", reasonHouseholdNotAccessed);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if (householdes.getChildAt(3).getVisibility() == View.VISIBLE) {

                        males = ((TitledEditText) householdes.getChildAt(3)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Total Number of Males", males);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(4).getVisibility() == View.VISIBLE) {

                        male_greater_15 = ((TitledEditText) householdes.getChildAt(4)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Males greater than 15", male_greater_15);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(5).getVisibility() == View.VISIBLE) {

                        male_2_4 = ((TitledEditText) householdes.getChildAt(5)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Males between 2 and 4", male_2_4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(6).getVisibility() == View.VISIBLE) {

                        females = ((TitledEditText) householdes.getChildAt(6)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Total Number of Females", females);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(7).getVisibility() == View.VISIBLE) {

                        female_greater_15 = ((TitledEditText) householdes.getChildAt(7)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Females greater than 15", female_greater_15);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (householdes.getChildAt(8).getVisibility() == View.VISIBLE) {

                        female_2_4 = ((TitledEditText) householdes.getChildAt(8)).getEditText().getText().toString();
                        try {
                            houseHoldObj.put("Number of Females between 2 and 4", female_2_4);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    householdArray.put(houseHoldObj);
                    try {
                        dwellingObj.put("householdes", householdArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dwellingArray.put(dwellingObj);
            }
            //if (dynamicViewsLayout.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"DWELLINGS", dwellingArray.toString()});

        }
        Log.d("", observations.toArray(new String[][]{}) + "");

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
                    result = serverService.submitToGwtApp(RequestType.ZTTS_ENUMERATION_BLOCK, form, values, observations.toArray(new String[][]{}));
                    if (result.contains("SUCCESS"))
                        return "SUCCESS";
                } else if (App.get(formType).equals(getResources().getString(R.string.ztts_house_hold_level))) {
                    result = serverService.submitToGwtApp(RequestType.ZTTS_ENUMERATION_HOUSEHOLD, form, values, observations.toArray(new String[][]{}));
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

        //serverService.saveFormLocally(formName, "12345-5", formValues);

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
            showDateDialog(formDateCalendar,false,true, false);
            /*Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");*/
        } else if (view == addDwelling.getButton()) {
            int num_total_dwellings = 0;
            int _totalhouseholds = 0;
            int _blockCodeLength = 0;
            int _buildingCodeLength = 0;
            _blockCodeLength = block_code.getEditText().getText().toString().trim().length();
            _buildingCodeLength = building_code.getEditText().getText().toString().trim().length();

            if (total_dwellings.getEditText().getText().length() == 0) {
                num_total_dwellings = 0;
            } else {
                num_total_dwellings = Integer.parseInt(total_dwellings.getEditText().getText().toString());
            }

            if (total_households.getEditText().getText().length() == 0) {
                _totalhouseholds = 0;
            } else {
                _totalhouseholds = Integer.parseInt(total_households.getEditText().getText().toString());
            }
            if (countDwellings < num_total_dwellings && _totalhouseholds >= 0 && _blockCodeLength >= 4 && _buildingCodeLength > 0) {
                block_code.getEditText().setEnabled(false);
                building_code.getEditText().setEnabled(false);
                total_dwellings.getEditText().setEnabled(false);
                total_households.getEditText().setEnabled(false);
                is_building_accessed.getRadioGroup().getButtons().get(0).setEnabled(false);
                is_building_accessed.getRadioGroup().getButtons().get(1).setEnabled(false);
                reason_building_not_accessed.getSpinner().setEnabled(false);
                if_other.getEditText().setEnabled(false);
                countDwellings++;
                final LinearLayout dwellingLayout = new LinearLayout(context);
                dwellingLayout.setOrientation(LinearLayout.VERTICAL);
                dynamicViewsLayout.addView(dwellingLayout);

                if (String.valueOf(building_code.getEditText().getText().toString()).length() <= 1) {
                    stringcountbuildings = "00" + building_code.getEditText().getText().toString().trim();
                } else if (String.valueOf(building_code.getEditText().getText().toString()).length() <= 2) {
                    stringcountbuildings = "0" + building_code.getEditText().getText().toString().trim();
                } else {
                    stringcountbuildings = "" + building_code.getEditText().getText().toString().trim();
                }

                if (String.valueOf(countDwellings).length() <= 1) {
                    stringcountDwellings = "00" + countDwellings;
                } else if (String.valueOf(countDwellings).length() <= 2) {
                    stringcountDwellings = "0" + countDwellings;
                } else {
                    stringcountDwellings = "" + countDwellings;
                }
                final MyTextView dwellingCode = new MyTextView(context, "Dwelling Code : " + (stringcountbuildings + "-" + stringcountDwellings));
                dwellingCode.setPadding(0, 100, 0, 20);
                dwellingCode.setTypeface(null, Typeface.BOLD);
                dwellingCode.setTextColor(Color.parseColor("#CF0000"));
                dwellingLayout.addView(dwellingCode);

                final TitledSpinner reason_dwelling_not_accessed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_reason_dwelling_not_aacessed), getResources().getStringArray(R.array.ztts_refused_missed), "", App.HORIZONTAL);

                final TitledSpinner dwelling_accessed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_dwelling_accessed), getResources().getStringArray(R.array.ztts_yes_no), "", App.HORIZONTAL);
                dwelling_accessed.getQuestionView().setTextColor(Color.parseColor("#CF0000"));
                dwelling_accessed.getSpinner().setSelection(0);
                dwelling_accessed.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 1) {
                            TotalHouseholds -= ((LinearLayout) dwellingLayout.getChildAt(3)).getChildCount();
                            for (int j = 0; j < ((LinearLayout) dwellingLayout.getChildAt(3)).getChildCount(); j++) {
                                houseHoldList.remove((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(j));

                            }
                            ((LinearLayout) dwellingLayout.getChildAt(3)).removeAllViews();
                            dwellingLayout.getChildAt(4).setVisibility(View.GONE);
                            reason_dwelling_not_accessed.setVisibility(View.VISIBLE);
                        } else if (i == 0) {
                            dwellingLayout.getChildAt(4).setVisibility(View.VISIBLE);
                            reason_dwelling_not_accessed.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                dwellingLayout.addView(dwelling_accessed);
                dwellingLayout.addView(reason_dwelling_not_accessed);

                final LinearLayout household_nested = new LinearLayout(context);
                household_nested.setOrientation(LinearLayout.VERTICAL);
                dwellingLayout.addView(household_nested);


                final TitledButton addHouseHold = new TitledButton(


                        context, null, null, "Add Household", App.HORIZONTAL);

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

                            final int countHouseHoldes = ((LinearLayout) dwellingLayout.getChildAt(3)).getChildCount();
                            if (String.valueOf(countHouseHoldes).length() <= 1) {
                                stringcountHouseHoldes = "00" + countHouseHoldes;
                            } else if (String.valueOf(countHouseHoldes).length() <= 2) {
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

                            final TitledSpinner reason_household_not_accessed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_reason_household_not_aacessed), getResources().getStringArray(R.array.ztts_refused_missed), "", App.HORIZONTAL);

                            final TitledSpinner household_accessed = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_house_hold_accessed), getResources().getStringArray(R.array.ztts_yes_no), "", App.HORIZONTAL);
                            household_accessed.getQuestionView().setTextColor(Color.parseColor("#00CF00"));
                            household_accessed.getSpinner().setSelection(0);
                            household_accessed.getSpinner().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    TitledEditText no_of_males = (TitledEditText) ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(3);
                                    TitledEditText male_greater_15 = (TitledEditText) ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(4);
                                    TitledEditText male_2_4 = (TitledEditText) ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(5);
                                    TitledEditText no_of_females = (TitledEditText) ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(6);
                                    TitledEditText female_greater_15 = (TitledEditText) ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(7);
                                    TitledEditText female_2_4 = (TitledEditText) ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(8);
                                    if (i == 0) {
                                        reason_household_not_accessed.setVisibility(View.GONE);
                                        no_of_males.setVisibility(View.VISIBLE);
                                        if (no_of_males.getEditText().getText().toString().trim().length() > 0) {
                                            male_greater_15.setVisibility(View.VISIBLE);
                                            male_2_4.setVisibility(View.VISIBLE);
                                        }

                                        no_of_females.setVisibility(View.VISIBLE);
                                        if (no_of_females.getEditText().getText().toString().trim().length() > 0) {
                                            female_greater_15.setVisibility(View.VISIBLE);
                                            female_2_4.setVisibility(View.VISIBLE);
                                        }


                                    } else if (i == 1) {
                                        reason_household_not_accessed.setVisibility(View.VISIBLE);

                                        no_of_males.setVisibility(View.GONE);
                                        male_greater_15.setVisibility(View.GONE);
                                        male_2_4.setVisibility(View.GONE);
                                        no_of_females.setVisibility(View.GONE);
                                        female_greater_15.setVisibility(View.GONE);
                                        female_2_4.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            houseHoldLayout.addView(household_accessed);
                            houseHoldLayout.addView(reason_household_not_accessed);


                            int totalHouseholdsInHouseholdLayout = ((LinearLayout) dwellingLayout.getChildAt(3)).getChildCount();


                            for (int w = 0; w < totalHouseholdsInHouseholdLayout; w++) {
                                ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(w)).getChildAt(0).setClickable(false);
                                ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(w)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            }
                            ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayout - 1)).getChildAt(0).setClickable(true);
                            ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayout - 1)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
                            DrawableCompat.setTint((((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayout - 1)).getChildAt(0))).getCompoundDrawables()[2], Color.RED);


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
                                                    int totalHouseholdsInHouseholdLayout = ((LinearLayout) dwellingLayout.getChildAt(3)).getChildCount();

                                                    LinearLayout layoutToDelete = ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayout - 1));
                                                    ((LinearLayout) dwellingLayout.getChildAt(3)).removeView(layoutToDelete);
                                                    houseHoldList.remove(layoutToDelete);
                                                    TotalHouseholds--;

                                                    int totalHouseholdsInHouseholdLayoutAfterDelet = ((LinearLayout) dwellingLayout.getChildAt(3)).getChildCount();


                                                    for (int w = 0; w < totalHouseholdsInHouseholdLayoutAfterDelet; w++) {
                                                        ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(w)).getChildAt(0).setClickable(false);
                                                        ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(w)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                                                    }
                                                    try {
                                                        ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayoutAfterDelet - 1)).getChildAt(0).setClickable(true);
                                                        ((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayoutAfterDelet - 1)).getChildAt(0)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
                                                        DrawableCompat.setTint((((MyTextView) ((LinearLayout) ((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(totalHouseholdsInHouseholdLayoutAfterDelet - 1)).getChildAt(0))).getCompoundDrawables()[2], Color.RED);


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

                            final TitledEditText males = new TitledEditText(context, null, "Number of Males" + " ", "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
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
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(4).setVisibility(View.VISIBLE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(5).setVisibility(View.VISIBLE);

                                    } else {
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(4).setVisibility(View.GONE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(5).setVisibility(View.GONE);
                                    }

                                }
                            });
                            houseHoldLayout.addView(males);

                            final TitledEditText males_greater_15 = new TitledEditText(context, null, "Male greater than 15" + " ", "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            males_greater_15.getEditText().addTextChangedListener(new TextWatcher() {

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
                                    if (males_greater_15.getEditText().getText().length() >= 1) {
                                        try {
                                            if (Integer.parseInt(males_greater_15.getEditText().getText().toString()) <= (Integer.parseInt(males.getEditText().getText().toString()))) {
                                                males_greater_15.getEditText().setError(null);
                                            } else {
                                                males_greater_15.getEditText().setError("value should be <= " + (Integer.parseInt(males.getEditText().getText().toString())));
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                            houseHoldLayout.addView(males_greater_15);
                            males_greater_15.setVisibility(View.GONE);


                            final TitledEditText males_2_to_4 = new TitledEditText(context, null, "Male 2-4" + " ", "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            males_2_to_4.getEditText().addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if (males_2_to_4.getEditText().getText().length() >= 1) {
                                        try {
                                            if (Integer.parseInt(males_2_to_4.getEditText().getText().toString()) <= (Integer.parseInt(males.getEditText().getText().toString()) - Integer.parseInt(males_greater_15.getEditText().getText().toString()))) {
                                                males_2_to_4.getEditText().setError(null);
                                            } else {
                                                males_2_to_4.getEditText().setError("value should be <= " + (Integer.parseInt(males.getEditText().getText().toString()) - Integer.parseInt(males_greater_15.getEditText().getText().toString())));
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                            houseHoldLayout.addView(males_2_to_4);
                            males_2_to_4.setVisibility(View.GONE);


                            final TitledEditText females = new TitledEditText(context, null, "Number of Females " + " ", "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
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
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(7).setVisibility(View.VISIBLE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(8).setVisibility(View.VISIBLE);

                                    } else {
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(7).setVisibility(View.GONE);
                                        ((LinearLayout) (((LinearLayout) dwellingLayout.getChildAt(3)).getChildAt(countHouseHoldes - 1))).getChildAt(8).setVisibility(View.GONE);
                                    }

                                }
                            });
                            houseHoldLayout.addView(females);

                            final TitledEditText females_greater_15 = new TitledEditText(context, null, "Female greater than 15" + " ", "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            females_greater_15.getEditText().addTextChangedListener(new TextWatcher() {

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
                                    if (females_greater_15.getEditText().getText().length() >= 1) {
                                        try {
                                            if (Integer.parseInt(females_greater_15.getEditText().getText().toString()) <= (Integer.parseInt(females.getEditText().getText().toString()))) {
                                                females_greater_15.getEditText().setError(null);
                                            } else {
                                                females_greater_15.getEditText().setError("value should be <= " + (Integer.parseInt(females.getEditText().getText().toString())));
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                            houseHoldLayout.addView(females_greater_15);
                            females_greater_15.setVisibility(View.GONE);

                            final TitledEditText females_2_to_4 = new TitledEditText(context, null, "Female 2-4" + " ", "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
                            females_2_to_4.getEditText().addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if (females_2_to_4.getEditText().getText().length() >= 1) {
                                        try {
                                            if (Integer.parseInt(females_2_to_4.getEditText().getText().toString()) <= (Integer.parseInt(females.getEditText().getText().toString()) - Integer.parseInt(females_greater_15.getEditText().getText().toString()))) {
                                                females_2_to_4.getEditText().setError(null);
                                            } else {
                                                females_2_to_4.getEditText().setError("value should be <= " + (Integer.parseInt(females.getEditText().getText().toString()) - Integer.parseInt(females_greater_15.getEditText().getText().toString())));
                                            }
                                        } catch (Exception e) {

                                        }
                                    }
                                }
                            });
                            houseHoldLayout.addView(females_2_to_4);
                            females_2_to_4.setVisibility(View.GONE);
                        } else {
                            showAlert("You can't add more household(s)");
                        }
                    }
                });
                dwellingLayout.addView(addHouseHold);
                if (Integer.parseInt(total_households.getEditText().getText().toString()) == 0) {
                    addHouseHold.getButton().setVisibility(View.GONE);
                } else {
                    addHouseHold.getButton().setVisibility(View.VISIBLE);
                }
            } else {
                int totaldwellings = 0;
                int totalhouseholds = 0;
                int blockCodeLength = 0;
                int buildingCodeLength = 0;
                blockCodeLength = block_code.getEditText().getText().toString().trim().length();
                buildingCodeLength = building_code.getEditText().getText().toString().trim().length();
                try {
                    totaldwellings = Integer.parseInt(App.get(total_dwellings));
                    totalhouseholds = Integer.parseInt(App.get(total_households));
                } catch (Exception e) {

                }
                if (totaldwellings <= 0 || totalhouseholds < 0 || blockCodeLength < 4 || buildingCodeLength <= 0) {
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
        MySpinner spinner = (MySpinner) parent;
        if (spinner == reason_building_not_accessed.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.ztts_reason_building_not_accessed_other))) {
                if_other.setVisibility(View.VISIBLE);
            } else {
                if_other.setVisibility(View.GONE);
            }
        }
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
        is_building_accessed.getRadioGroup().getButtons().get(0).setEnabled(true);
        is_building_accessed.getRadioGroup().getButtons().get(1).setEnabled(true);
        reason_building_not_accessed.getSpinner().setEnabled(true);
        if_other.getEditText().setEnabled(true);

        countDwellings = 0;
        TotalHouseholds = 0;
        stringcountbuildings = "";
        stringcountDwellings = "";
        stringcountHouseHoldes = "";
        count_building_na = 0;


        submitButton.setEnabled(false);


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

        if (radioGroup == is_building_accessed.getRadioGroup()) {
            is_building_accessed.getRadioGroup().getButtons().get(1).setError(null);
            if (App.get(is_building_accessed).equals(getString(R.string.yes))) {
                reason_building_not_accessed.setVisibility(View.GONE);
                if_other.setVisibility(View.GONE);

                total_households.setVisibility(View.VISIBLE);
                total_dwellings.setVisibility(View.VISIBLE);
                dynamicViewsLayout.setVisibility(View.VISIBLE);
                addDwelling.setVisibility(View.VISIBLE);
                houseHoldLevel.setVisibility(View.VISIBLE);
            } else {
                reason_building_not_accessed.setVisibility(View.VISIBLE);
                if (App.get(reason_building_not_accessed).equals(getString(R.string.ztts_reason_building_not_accessed_other))) {
                    if_other.setVisibility(View.VISIBLE);
                } else {
                    if_other.setVisibility(View.GONE);
                }


                total_households.setVisibility(View.GONE);
                total_dwellings.setVisibility(View.GONE);
                dynamicViewsLayout.setVisibility(View.GONE);
                addDwelling.setVisibility(View.GONE);
                houseHoldLevel.setVisibility(View.GONE);

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
            door_locked.setVisibility(View.VISIBLE);
            other_na.setVisibility(View.VISIBLE);
            build_refused.setVisibility(View.VISIBLE);
            build_accessed.setVisibility(View.VISIBLE);

            block_code.setVisibility(View.GONE);
            building_code.setVisibility(View.GONE);
            is_building_accessed.setVisibility(View.GONE);
            reason_building_not_accessed.setVisibility(View.GONE);
            if_other.setVisibility(View.GONE);
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
            door_locked.setVisibility(View.GONE);
            other_na.setVisibility(View.GONE);
            build_refused.setVisibility(View.GONE);
            build_accessed.setVisibility(View.GONE);

            block_code.setVisibility(View.VISIBLE);
            building_code.setVisibility(View.VISIBLE);
            is_building_accessed.setVisibility(View.VISIBLE);

            /*reason_building_not_accessed.setVisibility(View.VISIBLE);
            if_other.setVisibility(View.VISIBLE);
            total_dwellings.setVisibility(View.VISIBLE);
            total_households.setVisibility(View.VISIBLE);
            houseHoldLevel.setVisibility(View.VISIBLE);
            addDwelling.setVisibility(View.VISIBLE);
            dynamicViewsLayout.setVisibility(View.VISIBLE);*/

            if (App.get(is_building_accessed).equals(getString(R.string.yes))) {
                reason_building_not_accessed.setVisibility(View.GONE);
                if_other.setVisibility(View.GONE);

                total_households.setVisibility(View.VISIBLE);
                total_dwellings.setVisibility(View.VISIBLE);
                dynamicViewsLayout.setVisibility(View.VISIBLE);
                addDwelling.setVisibility(View.VISIBLE);
                houseHoldLevel.setVisibility(View.VISIBLE);
            } else if (App.get(is_building_accessed).equals(getString(R.string.no))) {
                reason_building_not_accessed.setVisibility(View.VISIBLE);
                if (App.get(reason_building_not_accessed).equals(getString(R.string.ztts_reason_building_not_accessed_other))) {
                    if_other.setVisibility(View.VISIBLE);
                } else {
                    if_other.setVisibility(View.GONE);
                }


                total_households.setVisibility(View.GONE);
                total_dwellings.setVisibility(View.GONE);
                dynamicViewsLayout.setVisibility(View.GONE);
                addDwelling.setVisibility(View.GONE);
                houseHoldLevel.setVisibility(View.GONE);

            }

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



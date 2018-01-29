package com.ihsinformatics.gfatmmobile.ztts;

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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by New User on 29-Dec-16.
 */

public class ZttsEnumerationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledSpinner locations;
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
    MyTextView houseHoldLevel;
    TitledButton addDwelling;
    TitledButton addHouseHold;
    LinearLayout dynamicViewsLayout;


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
        blockCode = new TitledEditText(context, null, getResources().getString(R.string.ztts_block_code), "", getResources().getString(R.string.ztts_block_code_hint), 5, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        locations = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.ztts_location), getResources().getStringArray(R.array.ztts_locations_array), getResources().getString(R.string.none), App.VERTICAL, true);
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        blockBuildingLevel = new MyTextView(context, getResources().getString(R.string.ztts_block_code_building_level));
        blockBuildingLevel.setTypeface(null, Typeface.BOLD);
        total_build = new TitledEditText(context, null, getResources().getString(R.string.ztts_total_build), "", getResources().getString(R.string.ztts_total_build), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        building_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_na), "", getResources().getString(R.string.ztts_build_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        empty_plot_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_empty_plot_na), "", getResources().getString(R.string.ztts_empty_plot_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        school_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_school_na), "", getResources().getString(R.string.ztts_school_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        commercial_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_commercial_na), "", getResources().getString(R.string.ztts_commercial_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        other_na = new TitledEditText(context, null, getResources().getString(R.string.ztts_other_na), "", getResources().getString(R.string.ztts_other_na), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        build_refused = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_refused), "", getResources().getString(R.string.ztts_build_refused), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        build_accessed = new TitledEditText(context, null, getResources().getString(R.string.ztts_build_accessed), "", getResources().getString(R.string.ztts_build_accessed), 3, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        houseHoldLevel = new MyTextView(context, getResources().getString(R.string.ztts_house_hold_level));
        houseHoldLevel.setTypeface(null, Typeface.BOLD);
        addDwelling = new TitledButton(context, null, null, "Add Dwelling", App.HORIZONTAL);
        addHouseHold = new TitledButton(context, null, null, "Add Household", App.HORIZONTAL);
        dynamicViewsLayout = new LinearLayout(context);
        dynamicViewsLayout.setOrientation(LinearLayout.VERTICAL);


        // Used for reset fields...
        views = new View[]{locations.getSpinner(), formDate.getButton(), blockCode.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{locations, formDate, blockCode, blockBuildingLevel, total_build, building_na, empty_plot_na, school_na,
                        commercial_na, other_na, build_refused, build_accessed, houseHoldLevel, dynamicViewsLayout, addHouseHold, addDwelling}};

        formDate.getButton().setOnClickListener(this);
        locations.getSpinner().setOnItemSelectedListener(this);
        total_build.getEditText().addTextChangedListener(new TextWatcher() {

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
                if (total_build.getEditText().getText().length() == 0) {
                    num = 0;
                } else {
                    num = Integer.parseInt(total_build.getEditText().getText().toString());
                }

                if (num > 0) {
                    building_na.setVisibility(View.VISIBLE);
                    if (building_na.getEditText().getText().length() > 0) {
                        int value = Integer.parseInt(building_na.getEditText().getText().toString());
                        if (value > 0) {
                            empty_plot_na.setVisibility(View.VISIBLE);
                            school_na.setVisibility(View.VISIBLE);
                            commercial_na.setVisibility(View.VISIBLE);
                            other_na.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    building_na.setVisibility(View.GONE);
                    empty_plot_na.setVisibility(View.GONE);
                    school_na.setVisibility(View.GONE);
                    commercial_na.setVisibility(View.GONE);
                    other_na.setVisibility(View.GONE);

                }

            }
        });
        building_na.getEditText().addTextChangedListener(new TextWatcher() {

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
                if (building_na.getEditText().getText().length() == 0) {
                    num = 0;
                } else {
                    num = Integer.parseInt(building_na.getEditText().getText().toString());
                }

                if (num > 0) {
                    empty_plot_na.setVisibility(View.VISIBLE);
                    school_na.setVisibility(View.VISIBLE);
                    commercial_na.setVisibility(View.VISIBLE);
                    other_na.setVisibility(View.VISIBLE);
                } else {
                    empty_plot_na.setVisibility(View.GONE);
                    school_na.setVisibility(View.GONE);
                    commercial_na.setVisibility(View.GONE);
                    other_na.setVisibility(View.GONE);
                }
            }
        });
        addDwelling.getButton().setOnClickListener(this);
        addHouseHold.getButton().setOnClickListener(this);


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

        if (App.get(blockCode).isEmpty()) {
            gotoFirstPage();
            blockCode.getEditText().setError(getString(R.string.empty_field));
            blockCode.getEditText().requestFocus();
            error = true;
        } else if (!App.get(blockCode).isEmpty() && Integer.parseInt(App.get(blockCode)) > 24) {
            gotoFirstPage();
            blockCode.getEditText().setError(getString(R.string.comorbidities_vitals_month_of_visit_limit));
            blockCode.getEditText().requestFocus();
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
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
        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"FOLLOW-UP MONTH", App.get(blockCode)});


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
                result = serverService.saveEncounterAndObservation(App.getProgram() + "-" + FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}), false);
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
        // submissionFormTask.execute("");

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

    static int countDwellings = 0;
    static int countHouseHoldes = 0;

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
            addDwelling.getButton().setEnabled(false);
            countHouseHoldes=0;
            countDwellings++;
            addHouseHold.setVisibility(View.VISIBLE);
            MyTextView dwellingCode = new MyTextView(context, "Dwelling Code : 00" + (countDwellings));
            dwellingCode.setTypeface(null, Typeface.BOLD);
            dwellingCode.setTextColor(Color.BLACK);
            dynamicViewsLayout.addView(dwellingCode);

        }else if (view == addHouseHold.getButton()) {
            addDwelling.getButton().setEnabled(true);
            countHouseHoldes++;
            MyTextView householdCode = new MyTextView(context, "Household Code : 00" + (countHouseHoldes));
            householdCode.setTypeface(null, Typeface.BOLD);
            householdCode.setTextColor(Color.BLACK);
            dynamicViewsLayout.addView(householdCode);

            TitledEditText males = new TitledEditText(context, null, "Number of Males" + " " , "", "", 50, null, -1, App.VERTICAL, true);
            males.getEditText().setTag((countHouseHoldes));
            males.getEditText().setFocusable(false);
            dynamicViewsLayout.addView(males);

            TitledEditText males_greater_15 = new TitledEditText(context, null, "Male greater than 15" + " "  , "", "", 50, null, -1, App.VERTICAL, true);
            males_greater_15.getEditText().setTag((countHouseHoldes));
            males_greater_15.getEditText().setFocusable(false);
            dynamicViewsLayout.addView(males_greater_15);


            TitledEditText males_2_to_4 = new TitledEditText(context, null, "Male 2-4" + " "  , "", "", 50, null, -1, App.VERTICAL, true);
            males_2_to_4.getEditText().setTag((countHouseHoldes));
            males_2_to_4.getEditText().setFocusable(false);
            dynamicViewsLayout.addView(males_2_to_4);



            TitledEditText females = new TitledEditText(context, null, "Number of Females " + " "  , "", "", 50, null, -1, App.VERTICAL, true);
            females.getEditText().setTag((countHouseHoldes));
            females.getEditText().setFocusable(false);
            dynamicViewsLayout.addView(females);

            TitledEditText females_greater_15 = new TitledEditText(context, null, "Female greater than 15" + " "  , "", "", 50, null, -1, App.VERTICAL, true);
            females_greater_15.getEditText().setTag((countHouseHoldes));
            females_greater_15.getEditText().setFocusable(false);
            dynamicViewsLayout.addView(females_greater_15);

            TitledEditText females_2_to_4 = new TitledEditText(context, null, "Female 2-4" + " "  , "", "", 50, null, -1, App.VERTICAL, true);
            females_2_to_4.getEditText().setTag((countHouseHoldes));
            females_2_to_4.getEditText().setFocusable(false);
            dynamicViewsLayout.addView(females_2_to_4);


        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == locations.getSpinner()) {
            if (!(locations.getSpinner().getSelectedItemPosition() == 0)) {
                blockCode.getEditText().setText(String.valueOf(spinner.getSelectedItem().toString().toUpperCase().charAt(0)));
            } else {
                ((TextView) locations.getSpinner().getSelectedView()).setTextColor(Color.GRAY);
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


        building_na.setVisibility(View.GONE);
        empty_plot_na.setVisibility(View.GONE);
        school_na.setVisibility(View.GONE);
        commercial_na.setVisibility(View.GONE);
        other_na.setVisibility(View.GONE);
        addHouseHold.setVisibility(View.GONE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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



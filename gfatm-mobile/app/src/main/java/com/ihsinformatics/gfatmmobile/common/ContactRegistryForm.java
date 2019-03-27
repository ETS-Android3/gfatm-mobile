package com.ihsinformatics.gfatmmobile.common;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/9/2017.
 */

public class ContactRegistryForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText contacts;
    TitledEditText adultContacts;
    TitledEditText childhoodContacts;
    TitledRadioGroup familyConsent;
    TitledRadioGroup indexElgibilityForContactScreening;
    TitledSpinner reasonNotEligibile;
    TitledEditText otherReason;

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
        formName = Forms.CONTACT_REGISTRY;
        form = Forms.contactRegistryForm;

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

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        contacts = new TitledEditText(context, null, getResources().getString(R.string.fast_how_many_people_sleep_in_your_home), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        contacts.getEditText().setKeyListener(null);
        contacts.getEditText().setFocusable(false);
        adultContacts = new TitledEditText(context, null, getResources().getString(R.string.fast_total_number_of_adult_contacts), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        childhoodContacts = new TitledEditText(context, null, getResources().getString(R.string.fast_total_number_of_childhood_contacts), "", "", 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        familyConsent = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_family_consent), getResources().getStringArray(R.array.yes_no_options), getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        indexElgibilityForContactScreening = new TitledRadioGroup(context, null, getResources().getString(R.string.index_patient_eligible_for_contact_screening), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL, true);
        reasonNotEligibile = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.reason), getResources().getStringArray(R.array.reason_index_patient_eligible_for_contact_screening_list), "", App.HORIZONTAL, true);
        otherReason = new TitledEditText(context, null, getResources().getString(R.string.specify_other), "", "", 255, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), contacts.getEditText(), adultContacts.getEditText(),
                childhoodContacts.getEditText(), familyConsent.getRadioGroup(), indexElgibilityForContactScreening.getRadioGroup(),
                reasonNotEligibile.getSpinner(), otherReason.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, contacts, adultContacts, childhoodContacts, familyConsent, indexElgibilityForContactScreening, reasonNotEligibile, otherReason}};

        formDate.getButton().setOnClickListener(this);
        indexElgibilityForContactScreening.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNotEligibile.getSpinner().setOnItemSelectedListener(this);

        contacts.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 50) {
                        contacts.getEditText().setError(getResources().getString(R.string.fast_enter_value_between_0_50));
                    } else {
                        contacts.getEditText().setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        adultContacts.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 25) {
                        adultContacts.getEditText().setError(getResources().getString(R.string.fast_enter_value_between_0_25));
                    } else {
                        if (!App.get(childhoodContacts).isEmpty()) {
                            int sum = Integer.parseInt(App.get(adultContacts)) + Integer.parseInt(App.get(childhoodContacts));
                            contacts.getEditText().setText(String.valueOf(sum));
                        } else {
                            int sum = Integer.parseInt(App.get(adultContacts));
                            contacts.getEditText().setText(String.valueOf(sum));
                        }
                        adultContacts.getEditText().setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (App.get(adultContacts).isEmpty() && App.get(childhoodContacts).isEmpty()) {
                    contacts.getEditText().setText(null);
                } else if (App.get(adultContacts).isEmpty()) {
                    int sum = Integer.parseInt(App.get(childhoodContacts));
                    contacts.getEditText().setText(String.valueOf(sum));
                } else if (App.get(childhoodContacts).isEmpty()) {
                    int sum = Integer.parseInt(App.get(adultContacts));
                    contacts.getEditText().setText(String.valueOf(sum));
                }
            }
        });

        childhoodContacts.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 25) {
                        childhoodContacts.getEditText().setError(getResources().getString(R.string.fast_enter_value_between_0_25));
                    } else {
                        if (!App.get(adultContacts).isEmpty()) {
                            int sum = Integer.parseInt(App.get(adultContacts)) + Integer.parseInt(App.get(childhoodContacts));
                            contacts.getEditText().setText(String.valueOf(sum));
                        } else {
                            int sum = Integer.parseInt(App.get(childhoodContacts));
                            contacts.getEditText().setText(String.valueOf(sum));
                        }
                        childhoodContacts.getEditText().setError(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (App.get(adultContacts).isEmpty() && App.get(childhoodContacts).isEmpty()) {
                    contacts.getEditText().setText(null);
                } else if (App.get(adultContacts).isEmpty()) {
                    int sum = Integer.parseInt(App.get(childhoodContacts));
                    contacts.getEditText().setText(String.valueOf(sum));
                } else if (App.get(childhoodContacts).isEmpty()) {
                    int sum = Integer.parseInt(App.get(adultContacts));
                    contacts.getEditText().setText(String.valueOf(sum));
                }
            }
        });


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

        contacts.getEditText().setError(null);
        adultContacts.getEditText().setError(null);
        childhoodContacts.getEditText().setError(null);
        otherReason.getEditText().setError(null);
        indexElgibilityForContactScreening.getQuestionView().setError(null);
        reasonNotEligibile.getQuestionView().setError(null);

        if (contacts.getVisibility() == View.VISIBLE && contacts.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contacts.getEditText().setError(getString(R.string.empty_field));
            contacts.getEditText().requestFocus();
            error = true;
        } else if (contacts.getVisibility() == View.VISIBLE && Integer.parseInt(contacts.getEditText().getText().toString()) > 50) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            contacts.getEditText().setError(getString(R.string.fast_enter_value_between_0_50));
            contacts.getEditText().requestFocus();
            error = true;
        }

        if (adultContacts.getVisibility() == View.VISIBLE && adultContacts.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            adultContacts.getEditText().setError(getString(R.string.empty_field));
            adultContacts.getEditText().requestFocus();
            error = true;
        } else if (adultContacts.getVisibility() == View.VISIBLE && Integer.parseInt(adultContacts.getEditText().getText().toString()) > 25) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            adultContacts.getEditText().setError(getString(R.string.fast_enter_value_between_0_25));
            adultContacts.getEditText().requestFocus();
            error = true;
        }


        if (childhoodContacts.getVisibility() == View.VISIBLE && childhoodContacts.getEditText().getText().toString().trim().isEmpty()) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            childhoodContacts.getEditText().setError(getString(R.string.empty_field));
            childhoodContacts.getEditText().requestFocus();
            error = true;
        } else if (childhoodContacts.getVisibility() == View.VISIBLE && Integer.parseInt(childhoodContacts.getEditText().getText().toString()) > 25) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            childhoodContacts.getEditText().setError(getString(R.string.fast_enter_value_between_0_25));
            childhoodContacts.getEditText().requestFocus();
            error = true;
        }

        if (App.get(indexElgibilityForContactScreening).equals("")) {
            if (App.isLanguageRTL())
                gotoPage(0);
            else
                gotoPage(0);
            indexElgibilityForContactScreening.getQuestionView().setError("");
            indexElgibilityForContactScreening.getQuestionView().requestFocus();
            error = true;
        } else if (App.get(indexElgibilityForContactScreening).equals(getString(R.string.no))) {

            if (App.get(reasonNotEligibile).equals("")) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                reasonNotEligibile.getQuestionView().setError("");
                reasonNotEligibile.getQuestionView().requestFocus();
            } else if (App.get(reasonNotEligibile).equals(getString(R.string.other)) && App.get(otherReason).equals("")) {
                if (App.isLanguageRTL())
                    gotoPage(0);
                else
                    gotoPage(0);
                otherReason.getEditText().setError(getString(R.string.empty_field));
                otherReason.getEditText().requestFocus();
                error = true;
            }
        }

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
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

        final ArrayList<String[]> observations = new ArrayList<String[]>();

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

        observations.add(new String[]{"LONGITUDE (DEGREES)", String.valueOf(App.getLongitude())});
        observations.add(new String[]{"LATITUDE (DEGREES)", String.valueOf(App.getLatitude())});
        observations.add(new String[]{"NUMBER OF CONTACTS", App.get(contacts)});
        observations.add(new String[]{"NUMBER OF ADULT CONTACTS", App.get(adultContacts)});
        observations.add(new String[]{"NUMBER OF CHILDHOOD CONTACTS", App.get(childhoodContacts)});
        observations.add(new String[]{"FAMILY CONSENT FOR CONTACT INVESTIGATION", App.get(familyConsent).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        observations.add(new String[]{"INDEX PATIENT ELIGIBLE FOR CONTACT SCREENING", App.get(indexElgibilityForContactScreening).equals(getResources().getString(R.string.yes)) ? "YES" : "NO"});
        if (reasonNotEligibile.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON INDEX PATIENT NOT ELIGIBLE FOR CONTACT SCREENING", App.get(reasonNotEligibile).equals(getResources().getString(R.string.index_patient_transferred_out)) ? "TRANSFERRED OUT" :
                    (App.get(reasonNotEligibile).equals(getResources().getString(R.string.index_patient_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                            (App.get(reasonNotEligibile).equals(getResources().getString(R.string.index_patient_loss_to_followup)) ? "INDEX PATIENT LOST TO FOLLOW UP" :
                                    (App.get(reasonNotEligibile).equals(getResources().getString(R.string.index_patient_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                            (App.get(reasonNotEligibile).equals(getResources().getString(R.string.out_of_city)) ? "PATIENT OUT OF CITY" :
                                                    (App.get(reasonNotEligibile).equals(getResources().getString(R.string.index_without_contacts)) ? "INDEX WITHOUT CONTACTS" : "OTHER REASON INDEX PATIENT NOT ELIGIBLE FOR CONTACT SCREENING")))))});

        if (otherReason.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER REASON INDEX PATIENT NOT ELIGIBLE FOR CONTACT SCREENING", App.get(otherReason)});

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

                String id = null;
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocallyTesting(Forms.CONTACT_REGISTRY, form, formDateCalendar,observations.toArray(new String[][]{}));

                result = serverService.saveEncounterAndObservationTesting(Forms.CONTACT_REGISTRY, form, formDateCalendar, observations.toArray(new String[][]{}),id);
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(formName, "12345-5", formValues);*/

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
            } else if (obs[0][0].equals("NUMBER OF CONTACTS")) {
                contacts.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NUMBER OF ADULT CONTACTS")) {
                adultContacts.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("NUMBER OF CHILDHOOD CONTACTS")) {
                childhoodContacts.getEditText().setText(obs[0][1]);
            } else if (obs[0][0].equals("FAMILY CONSENT FOR CONTACT INVESTIGATION")) {
                for (RadioButton rb : familyConsent.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("INDEX PATIENT ELIGIBLE FOR CONTACT SCREENING")) {
                for (RadioButton rb : indexElgibilityForContactScreening.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.no)) && obs[0][1].equals("NO")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.yes)) && obs[0][1].equals("YES")) {
                        rb.setChecked(true);
                        break;
                    }
                }
            } else if (obs[0][0].equals("REASON INDEX PATIENT NOT ELIGIBLE FOR CONTACT SCREENING")) {
                String value = obs[0][1].equals("TRANSFERRED OUT") ? getResources().getString(R.string.index_patient_transferred_out) :
                        (obs[0][1].equals("REFUSAL OF TREATMENT BY PATIENT") ? getResources().getString(R.string.index_patient_refused_treatment) :
                                (obs[0][1].equals("INDEX PATIENT LOST TO FOLLOW UP") ? getResources().getString(R.string.index_patient_loss_to_followup) :
                                        (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.index_patient_treatment_failure) :
                                                (obs[0][1].equals("PATIENT OUT OF CITY") ? getResources().getString(R.string.out_of_city) :
                                                        (obs[0][1].equals("INDEX WITHOUT CONTACTS") ? getResources().getString(R.string.index_without_contacts) : getResources().getString(R.string.other))))));

                reasonNotEligibile.getSpinner().selectValue(value);
                reasonNotEligibile.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER REASON INDEX PATIENT NOT ELIGIBLE FOR CONTACT SCREENING")) {
                otherReason.getEditText().setText(obs[0][1]);
                otherReason.setVisibility(View.VISIBLE);
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
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);*/
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonNotEligibile.getSpinner()) {

            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.other)))
                otherReason.setVisibility(View.VISIBLE);
            else
                otherReason.setVisibility(View.GONE);

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        reasonNotEligibile.setVisibility(View.GONE);
        otherReason.setVisibility(View.GONE);

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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        if (radioGroup == indexElgibilityForContactScreening.getRadioGroup()) {
            if (App.get(indexElgibilityForContactScreening).equals(getResources().getString(R.string.yes))) {
                reasonNotEligibile.setVisibility(View.GONE);
                otherReason.setVisibility(View.GONE);
            } else {
                reasonNotEligibile.setVisibility(View.VISIBLE);
                if (App.get(reasonNotEligibile).equals(getResources().getString(R.string.other)))
                    otherReason.setVisibility(View.VISIBLE);
                else
                    otherReason.setVisibility(View.GONE);
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

package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MySpinner;
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
 * Created by Rabbia on 11/24/2016.
 */

public class PetSocioecnomicDataForm extends AbstractFormActivity {

    Context context;

    TitledSpinner ethinicity;
    TitledEditText otherEthinicity;
    TitledSpinner contactEducationLevel;
    TitledEditText otherContactEducationLevel;
    TitledSpinner maritalStatus;
    TitledSpinner emloyementStatus;
    TitledSpinner occupation;
    TitledEditText otherOccupation;
    TitledEditText contactIncome;
    TitledEditText contactIncomeGroup;
    TitledSpinner householdHead;
    TitledEditText otherHouseholdHead;
    TitledSpinner householdHeadEducationLevel;
    TitledEditText otherHouseholdHeadEducationLevel;
    TitledSpinner motherTongue;
    TitledEditText otherMotherTongue;

    Boolean refillFlag = false;
    ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        pageCount = 1;
        formName = Forms.PET_SOCIO_ECNOMICS_DATA;
        form = Forms.pet_socioEcnomicData;

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
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
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
                scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }


    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        ethinicity = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_ethnicity), getResources().getStringArray(R.array.pet_ethnicities), "", App.VERTICAL, true, "ETHNICITY", new String[]{"URDU SPEAKING", "SINDHI", "PASHTUN", "PUNJABI", "BALOCHI", "BIHARI", "MEMON", "GUJRATI", "BROHI", "HINDKO", "GILGITI", "SARAIKI", "BANGALI", "AFGHAN", "HAZARA", "OTHER ETHNICITY", "UNKNOWN", "REFUSED"});
        otherEthinicity = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER ETHNICITY");
        contactEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_contact_education_level), getResources().getStringArray(R.array.pet_contact_education_levels), getResources().getString(R.string.pet_intermediate), App.VERTICAL, true, "HIGHEST EDUCATION LEVEL", new String[]{"ELEMENTARY EDUCATION", "PRIMARY EDUCATION", "SECONDARY EDUCATION", "INTERMEDIATE EDUCATION", "UNDERGRADUATE EDUCATION", "GRADUATE EDUCATION", "DOCTORATE EDUCATION", "RELIGIOUS EDUCATION", "POLYTECHNIC EDUCATION", "SPECIAL EDUCATION RECEIVED", "NO FORMAL EDUCATION", "OTHER", "UNKNOWN", "REFUSED"});
        otherContactEducationLevel = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER EDUCATION LEVEL");
        maritalStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_martial_status), getResources().getStringArray(R.array.pet_martial_statuses), getResources().getString(R.string.pet_single), App.VERTICAL, true, "MARITAL STATUS", new String[]{"SINGLE", "ENGAGED", "MARRIED", "SEPARATED", "DIVORCED", "WIDOWED", "OTHER", "REFUSE", "UNKNOWN"});
        emloyementStatus = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_employement_status), getResources().getStringArray(R.array.pet_employement_statuses), getResources().getString(R.string.pet_employed), App.VERTICAL, true, "EMPLOYMENT STATUS", new String[]{"EMPLOYED", "UNABLE TO WORK", "STUDENT", "UNEMPLOYED", "HOUSEWORK", "RETIRED", "REFUSE", "UNKNOWN"});
        occupation = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_occupation), getResources().getStringArray(R.array.pet_occupations), getString(R.string.pet_artist), App.VERTICAL, true, "OCCUPATION", new String[]{"ARTIST", "BEGGAR", "CARPENTER", "CASUAL LABOR", "CHILD LABOR", "CLERK", "MEDICAL OFFICER/DOCTOR", "DRIVER", "ENGINEER", "FARMER", "FISHERMAN", "FOOD VENDOR", "FORESTRY WORKER", "HOUSEWORK", "MACHINE OPERATOR", "MINER", "PILOT", "PLUMBER", "PROFESSIONAL", "REPORTER", "SALES REPRESENTATIVE", "SECURITY OFFICER", "SHEPHERD", "SLUM WORKER", "SMALL BUSINESS OWNER", "TAILOR", "TRADER", "TEACHER", "VEGETABLE SELLER", "WAITER", "OTHER", "UNKNOWN"});
        otherOccupation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER OCCUPATION");
        contactIncome = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income), "0", "", 10, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true, "MONTHLY INCOME");
        contactIncomeGroup = new TitledEditText(context, null, getResources().getString(R.string.pet_contact_income_group), getResources().getString(R.string.pet_none), "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        householdHead = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_household_head), getResources().getStringArray(R.array.pet_cnic_owners), getString(R.string.pet_self), App.VERTICAL, true, "RELATIONSHIP TO HEAD OF HOUSEHOLD", new String[]{"SELF", "MOTHER", "FATHER", "MATERNAL GRANDMOTHER", "MATERNAL GRANDFATHER", "PATERNAL GRANDMOTHER", "PATERNAL GRANDFATHER", "BROTHER", "SISTER", "SON", "DAUGHTER", "SPOUSE", "AUNT", "UNCLE", "OTHER"});
        otherHouseholdHead = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER FAMILY MEMBER");
        householdHeadEducationLevel = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_household_education), getResources().getStringArray(R.array.pet_contact_education_levels), getString(R.string.pet_intermediate), App.VERTICAL, true, "HIGHEST EDUCATION LEVEL", new String[]{"ELEMENTARY EDUCATION", "PRIMARY EDUCATION", "SECONDARY EDUCATION", "INTERMEDIATE EDUCATION", "UNDERGRADUATE EDUCATION", "GRADUATE EDUCATION", "DOCTORATE EDUCATION", "RELIGIOUS EDUCATION", "POLYTECHNIC EDUCATION", "SPECIAL EDUCATION RECEIVED", "NO FORMAL EDUCATION", "OTHER", "UNKNOWN", "REFUSED"});
        otherHouseholdHeadEducationLevel = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER HIGHEST EDUCATION LEVEL OF HEAD OF HOUSEHOLD");
        motherTongue = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_mother_tongue), getResources().getStringArray(R.array.pet_mother_tongues), getString(R.string.urdu), App.VERTICAL, true, "MOTHER TONGUE", new String[]{"URDU LANGUAGE", "PUNJABI LANGUAGE", "SINDHI LANGUAGE", "PUSHTO LANGUAGE", "BALOCHI LANGUAGE", "SIRAIKI LANGUAGE", "HAZARA LANGUAGE", "ENGLISH LANGUAGE", "GUJRATI LANGUAGE", "MEMONI LANGUAGE", "BRAHUI LANGUAGE", "CHITRALI LANGUAGE", "HINDKO LANGUAGE", "KALAASHA LANGUAGE", "KASHMIRI LANGUAGE", "PERSIAN LANGUAGE", "BALTI LANGUAGE", "MAKRANI LANGUAGE", "OTHER", "REFUSED", "UNKNOWN"});
        otherMotherTongue = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true, "OTHER LANGUAGE");

        views = new View[]{formDate.getButton(), ethinicity.getSpinner(), otherEthinicity.getEditText(), contactEducationLevel.getSpinner(), maritalStatus.getSpinner(), emloyementStatus.getSpinner(), occupation.getSpinner(), contactIncome.getEditText(), contactIncomeGroup.getEditText(),
                householdHead.getSpinner(), otherHouseholdHead.getEditText(), householdHeadEducationLevel.getSpinner(), motherTongue.getSpinner(), otherMotherTongue.getEditText(), otherOccupation.getEditText(), otherContactEducationLevel.getEditText(), otherHouseholdHeadEducationLevel.getEditText()};

        viewGroups = new View[][]{{formDate, ethinicity, otherEthinicity, contactEducationLevel, otherContactEducationLevel, maritalStatus, emloyementStatus, occupation, otherOccupation, contactIncome, contactIncomeGroup, householdHead, otherHouseholdHead, householdHeadEducationLevel, otherHouseholdHeadEducationLevel, motherTongue, otherMotherTongue}};

        contactIncome.getEditText().addTextChangedListener(new TextWatcher() {
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

                if (App.get(contactIncome).equals(""))
                    contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                else {
                    Float income = Float.parseFloat(App.get(contactIncome));
                    if (income == 0)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_none));
                    else if (income >= 1 && income <= 7000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_class));
                    else if (income >= 7001 && income <= 35000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_lower_middle_class));
                    else if (income >= 35001 && income <= 87000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_middle_class));
                    else if (income >= 87001 && income <= 173000)
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_middle_class));
                    else
                        contactIncomeGroup.getEditText().setText(getResources().getString(R.string.pet_upper_class));
                }


            }
        });
        contactIncomeGroup.getEditText().setKeyListener(null);

        formDate.getButton().setOnClickListener(this);
        ethinicity.getSpinner().setOnItemSelectedListener(this);
        householdHead.getSpinner().setOnItemSelectedListener(this);
        motherTongue.getSpinner().setOnItemSelectedListener(this);
        occupation.getSpinner().setOnItemSelectedListener(this);
        contactEducationLevel.getSpinner().setOnItemSelectedListener(this);
        householdHeadEducationLevel.getSpinner().setOnItemSelectedListener(this);

        resetViews();

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        otherEthinicity.setVisibility(View.GONE);
        otherHouseholdHead.setVisibility(View.GONE);
        otherMotherTongue.setVisibility(View.GONE);

        Bundle bundle = this.getArguments();
        Boolean autoFill = false;

        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);
                autoFill = true;

                refill(formId);

            } else bundle.putBoolean("save", false);
        }

    }

    @Override
    public void updateDisplay() {

        if (refillFlag) {
            refillFlag = true;
            return;
        }

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setEnabled(true);

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0, 10);

            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

            } else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "EEEE, MMM dd,yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
            } else
                formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        }
    }


    @Override
    public boolean validate() {

        Boolean error = super.validate();
        View view = null;

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
                                        otherMotherTongue.getEditText().clearFocus();
                                        otherHouseholdHead.getEditText().clearFocus();
                                        otherEthinicity.getEditText().clearFocus();
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
        } else
            return true;
    }

    @Override
    public boolean submit() {
        final HashMap<String, String> personAttribute = new HashMap<String, String>();
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


        String[][] concept = serverService.getConceptUuidAndDataType(ethinicity.getConceptAnswers()[ethinicity.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Ethnicity", concept[0][0]);

        concept = serverService.getConceptUuidAndDataType(contactEducationLevel.getConceptAnswers()[contactEducationLevel.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Education Level", concept[0][0]);


        concept = serverService.getConceptUuidAndDataType(maritalStatus.getConceptAnswers()[maritalStatus.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Marital Status", concept[0][0]);


        concept = serverService.getConceptUuidAndDataType(emloyementStatus.getConceptAnswers()[emloyementStatus.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Employment Status", concept[0][0]);


        concept = serverService.getConceptUuidAndDataType(occupation.getConceptAnswers()[occupation.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Occupation", concept[0][0]);


        final String incomeClassString = App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_none)) ? "NONE" :
                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_lower_class)) ? "LOWER INCOME CLASS" :
                        (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_lower_middle_class)) ? "LOWER MIDDLE INCOME CLASS" :
                                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_middle_class)) ? "MIDDLE INCOME CLASS" :
                                        (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_upper_middle_class)) ? "UPPER MIDDLE INCOME CLASS" :
                                                (App.get(contactIncomeGroup).equals(getResources().getString(R.string.pet_upper_class)) ? "UPPER INCOME CLASS" : "UNKNOWN")))));
        observations.add(new String[]{"INCOME CLASS", incomeClassString});
        concept = serverService.getConceptUuidAndDataType(incomeClassString);
        personAttribute.put("Income Class", concept[0][0]);


        concept = serverService.getConceptUuidAndDataType(motherTongue.getConceptAnswers()[motherTongue.getSpinner().getSelectedItemPosition()]);
        personAttribute.put("Mother Tongue", concept[0][0]);


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
                    id = serverService.saveFormLocally(formName, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveMultiplePersonAttribute(personAttribute, id);
                if (!result.equals("SUCCESS"))
                    return result;

                result = serverService.saveEncounterAndObservationTesting(formName, form, formDateCalendar, observations.toArray(new String[][]{}), id);
                if (!result.contains("SUCCESS"))
                    return result;

                return "SUCCESS";

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

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
        return false;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
           /* Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            formDate.getButton().setEnabled(false);*/
            formDate.getButton().setEnabled(false);
            showDateDialog(formDateCalendar, false, true, false);
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == ethinicity.getSpinner()) {
            if (App.get(ethinicity).equals(getResources().getString(R.string.pet_other_ethnicity)))
                otherEthinicity.setVisibility(View.VISIBLE);
            else
                otherEthinicity.setVisibility(View.GONE);
        } else if (spinner == householdHead.getSpinner()) {
            if (App.get(householdHead).equals(getResources().getString(R.string.pet_other)))
                otherHouseholdHead.setVisibility(View.VISIBLE);
            else
                otherHouseholdHead.setVisibility(View.GONE);
        } else if (spinner == motherTongue.getSpinner()) {
            if (App.get(motherTongue).equals(getResources().getString(R.string.pet_other)))
                otherMotherTongue.setVisibility(View.VISIBLE);
            else
                otherMotherTongue.setVisibility(View.GONE);
        } else if (spinner == occupation.getSpinner()) {
            if (App.get(occupation).equals(getResources().getString(R.string.pet_other)))
                otherOccupation.setVisibility(View.VISIBLE);
            else
                otherOccupation.setVisibility(View.GONE);
        } else if (spinner == contactEducationLevel.getSpinner()) {
            if (App.get(contactEducationLevel).equals(getResources().getString(R.string.pet_other)))
                otherContactEducationLevel.setVisibility(View.VISIBLE);
            else
                otherContactEducationLevel.setVisibility(View.GONE);
        } else if (spinner == householdHeadEducationLevel.getSpinner()) {
            if (App.get(householdHeadEducationLevel).equals(getResources().getString(R.string.pet_other)))
                otherHouseholdHeadEducationLevel.setVisibility(View.VISIBLE);
            else
                otherHouseholdHeadEducationLevel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void refill(int formId) {

        super.refill(formId);


        OfflineForm fo = serverService.getSavedFormById(formId);
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);

            if (obs[0][0].equals("INCOME CLASS")) {
                String value = obs[0][1].equals("NONE") ? getResources().getString(R.string.pet_none) :
                        (obs[0][1].equals("LOWER INCOME CLASS") ? getResources().getString(R.string.pet_lower_class) :
                                (obs[0][1].equals("LOWER MIDDLE INCOME CLASS") ? getResources().getString(R.string.pet_lower_middle_class) :
                                        (obs[0][1].equals("MIDDLE INCOME CLASS") ? getResources().getString(R.string.pet_middle_class) :
                                                (obs[0][1].equals("UPPER MIDDLE INCOME CLASS") ? getResources().getString(R.string.pet_upper_middle_class) :
                                                        (obs[0][1].equals("UPPER INCOME CLASS") ? getResources().getString(R.string.pet_upper_class) : getResources().getString(R.string.unknown))))));
                contactIncomeGroup.getEditText().setText(value);
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

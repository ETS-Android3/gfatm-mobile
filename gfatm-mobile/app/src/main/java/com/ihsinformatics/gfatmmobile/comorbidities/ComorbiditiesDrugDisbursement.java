package com.ihsinformatics.gfatmmobile.comorbidities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Fawad Jawaid on 16-Feb-17.
 */

public class ComorbiditiesDrugDisbursement extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    MyTextView aherenceTextView;
    TitledRadioGroup aherence;
    MyTextView drugDistributionRecord;
    TitledEditText drugDistributionNumber;
    TitledRadioGroup drugsPickedUp;
    MyTextView drugDistributionDetail;
    TitledButton drugDistributionDate;
    TitledEditText specifyOther;
    TitledEditText drugsDispersedDays;
    TitledEditText metformin;
    TitledEditText insulinN;
    TitledEditText insulinR;

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
        FORM_NAME = Forms.COMORBIDITIES_DRUG_DISBURSEMENT;
        FORM = Forms.comorbidities_DrugDisbursementForm;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new ComorbiditiesDrugDisbursement.MyAdapter());
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
        formDate.setTag("formDate");
        aherenceTextView = new MyTextView(context, getResources().getString(R.string.comorbidities_drug_disbursement_adherence_text));
        aherenceTextView.setTypeface(null, Typeface.BOLD);
        aherence = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_adherence), getResources().getStringArray(R.array.comorbidities_drug_disbursement_adherence_options), "", App.VERTICAL, App.VERTICAL);
        drugDistributionRecord = new MyTextView(context, getResources().getString(R.string.comorbidities_drug_disbursement_drug_distribution));
        drugDistributionRecord.setTypeface(null, Typeface.BOLD);
        drugDistributionNumber = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_drug_distribution_number), "", getResources().getString(R.string.comorbidities_drug_disbursement_drug_distribution_number_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        drugsPickedUp = new TitledRadioGroup(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_drugs_picked_up), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.VERTICAL, App.VERTICAL);
        drugDistributionDetail = new MyTextView(context, getResources().getString(R.string.comorbidities_drug_disbursement_drug_dist_detail_text));
        drugDistributionDetail.setTypeface(null, Typeface.BOLD);
        drugDistributionDate = new TitledButton(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_drug_dist_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.HORIZONTAL);
        specifyOther = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_specify_other), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        drugsDispersedDays = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_days_worth), "", getResources().getString(R.string.comorbidities_drug_disbursement_days_worth_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        metformin = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_number_of_metformin), "", getResources().getString(R.string.comorbidities_drug_disbursement_number_of_metformin_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.VERTICAL, true);
        insulinN = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_number_of_insulinN), "", getResources().getString(R.string.comorbidities_drug_disbursement_number_of_insulin_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        insulinR = new TitledEditText(context, null, getResources().getString(R.string.comorbidities_drug_disbursement_number_of_insulinR), "", getResources().getString(R.string.comorbidities_drug_disbursement_number_of_insulin_range), 2, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), aherence.getRadioGroup(), drugDistributionNumber.getEditText(), drugsPickedUp.getRadioGroup(), drugDistributionDate.getButton(), specifyOther.getEditText(),
                drugsDispersedDays.getEditText(), metformin.getEditText(), insulinN.getEditText(), insulinR.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, aherenceTextView, aherence, drugDistributionRecord, drugDistributionNumber, drugsPickedUp, drugDistributionDetail, drugDistributionDate, specifyOther, drugsDispersedDays, metformin, insulinN, insulinR}};

        formDate.getButton().setOnClickListener(this);
        drugDistributionDate.getButton().setOnClickListener(this);

        drugDistributionNumber.getEditText().addTextChangedListener(new TextWatcher() {

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
                try {
                    if (drugDistributionNumber.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(drugDistributionNumber.getEditText().getText().toString());
                        if (num < 0) {
                            drugDistributionNumber.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_drug_distribution_number_limit));
                        } else if (drugDistributionNumber.getEditText().getText().length() > 1 && num > 24) {
                            drugDistributionNumber.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_drug_distribution_number_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        drugsDispersedDays.getEditText().addTextChangedListener(new TextWatcher() {

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
                try {
                    if (drugsDispersedDays.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(drugsDispersedDays.getEditText().getText().toString());
                        if (num < 1 || num > 10) {
                            drugsDispersedDays.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_days_worth_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        insulinN.getEditText().addTextChangedListener(new TextWatcher() {

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
                try {
                    if (insulinN.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(insulinN.getEditText().getText().toString());
                        if (num < 0 || num > 3) {
                            insulinN.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_number_of_insulin_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        insulinR.getEditText().addTextChangedListener(new TextWatcher() {

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
                try {
                    if (insulinR.getEditText().getText().length() > 0) {
                        double num = Double.parseDouble(insulinR.getEditText().getText().toString());
                        if (num < 0 || num > 3) {
                            insulinR.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_number_of_insulin_limit));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });

        metformin.getEditText().addTextChangedListener(new TextWatcher() {

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
                try {
                    if (metformin.getEditText().getText().length() > 0) {
                        int num = Integer.parseInt(metformin.getEditText().getText().toString());
                        if (num < 0 || num > 15) {
                            metformin.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_number_of_metformin));
                        }
                    }
                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }
            }
        });


    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        drugDistributionDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(insulinR).isEmpty()) {
            gotoFirstPage();
            insulinR.getEditText().setError(getString(R.string.empty_field));
            insulinR.getEditText().requestFocus();
            error = true;
        } else if (!App.get(insulinR).isEmpty() && Integer.parseInt(App.get(insulinR)) > 3) {
            gotoFirstPage();
            insulinR.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_number_of_insulin_limit));
            insulinR.getEditText().requestFocus();
            error = true;
        }

        if (App.get(insulinN).isEmpty()) {
            gotoFirstPage();
            insulinN.getEditText().setError(getString(R.string.empty_field));
            insulinN.getEditText().requestFocus();
            error = true;
        } else if (!App.get(insulinN).isEmpty() && Integer.parseInt(App.get(insulinN)) > 3) {
            gotoFirstPage();
            insulinN.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_number_of_insulin_limit));
            insulinN.getEditText().requestFocus();
            error = true;
        }

        if (App.get(metformin).isEmpty()) {
            gotoFirstPage();
            metformin.getEditText().setError(getString(R.string.empty_field));
            metformin.getEditText().requestFocus();
            error = true;
        } else if (!App.get(metformin).isEmpty() && Integer.parseInt(App.get(metformin)) > 15) {
            gotoFirstPage();
            metformin.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_number_of_metformin_limit));
            metformin.getEditText().requestFocus();
            error = true;
        }

        if (App.get(drugsDispersedDays).isEmpty()) {
            gotoFirstPage();
            drugsDispersedDays.getEditText().setError(getString(R.string.empty_field));
            drugsDispersedDays.getEditText().requestFocus();
            error = true;
        } else if (!App.get(drugsDispersedDays).isEmpty() && Integer.parseInt(App.get(drugsDispersedDays)) > 10) {
            gotoFirstPage();
            drugsDispersedDays.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_days_worth_limit));
            drugsDispersedDays.getEditText().requestFocus();
            error = true;
        }

        if (App.get(specifyOther).isEmpty()) {
            gotoFirstPage();
            specifyOther.getEditText().setError(getString(R.string.empty_field));
            specifyOther.getEditText().requestFocus();
            error = true;
        }

        if (App.get(drugDistributionNumber).isEmpty()) {
            gotoFirstPage();
            drugDistributionNumber.getEditText().setError(getString(R.string.empty_field));
            drugDistributionNumber.getEditText().requestFocus();
            error = true;
        } else if (!App.get(drugDistributionNumber).isEmpty() && Integer.parseInt(App.get(drugDistributionNumber)) > 24) {
            gotoFirstPage();
            drugDistributionNumber.getEditText().setError(getString(R.string.comorbidities_drug_disbursement_drug_distribution_number_limit));
            drugDistributionNumber.getEditText().requestFocus();
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
    public void refill(int encounterId) {

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
        } else if (view == drugDistributionDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", false);
            args.putBoolean("allowFutureDate", true);
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        drugDistributionDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
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




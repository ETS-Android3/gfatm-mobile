package com.ihsinformatics.gfatmmobile.fast;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.InputType;
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
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MainActivity;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 1/12/2017.
 */

public class FastPromptForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;
    Boolean emptyError = false;


    // Views...

    MyTextView testingOfPresumptivePatientsTitle;
    MyTextView promptInstruction1;
    TitledEditText dueDateSample;
    TitledRadioGroup sputumContainerGiven;
    TitledRadioGroup sputum_sample;
    TitledRadioGroup reasonNoSputumSample;
    TitledRadioGroup freeXrayVoucher;
    TitledRadioGroup noXrayVoucher;
    MyTextView promptInstruction2;

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
        formName = Forms.FAST_PROMPT_FORM;
        form = Forms.fastPromptForm;

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
        testingOfPresumptivePatientsTitle = new MyTextView(context, getResources().getString(R.string.fast_testing_of_presumptive_patients));
        testingOfPresumptivePatientsTitle.setTypeface(null, Typeface.BOLD);
        promptInstruction1 = new MyTextView(context, getResources().getString(R.string.fast_prompt_instruction_1));
        promptInstruction1.setTextColor(Color.BLACK);
        promptInstruction1.setTypeface(null, Typeface.NORMAL);



        sputumContainerGiven = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_a_sputum_container), getResources().getStringArray(R.array.fast_yes_no_list), getResources().getString(R.string.fast_yes_title), App.VERTICAL, App.VERTICAL, true,"SPUTUM CONTAINER",getResources().getStringArray(R.array.yes_no_list_concept));
        sputum_sample = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_produced_a_sputum_sample), getResources().getStringArray(R.array.fast_yes_no_list), "", App.VERTICAL, App.VERTICAL, true,"SPUTUM PRODUCED",getResources().getStringArray(R.array.yes_no_list_concept));
        reasonNoSputumSample = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_if_no_why_not), getResources().getStringArray(R.array.fast_if_no_why_not_list), getResources().getString(R.string.fast_patient_unable_to_expectorate), App.VERTICAL, App.VERTICAL, true,"PATIENT DO NOT PRODUCE SPUTUM SAMPLE",new String[]{"UNABLE TO EXPECTORATE" , "REFUSED"});
        secondDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        secondDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        secondDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        dueDateSample = new TitledEditText(context, null, getResources().getString(R.string.fast_date_sputum_sample), DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString(), "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        dueDateSample.getEditText().setKeyListener(null);
        dueDateSample.getEditText().setFocusable(false);
        freeXrayVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_given_free_chest_xray), getResources().getStringArray(R.array.fast_yes_no_list),"", App.VERTICAL, App.VERTICAL, true,"VOUCHER GIVEN FOR TEST",getResources().getStringArray(R.array.yes_no_list_concept));
        noXrayVoucher = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_not_given_free_chest_xray), getResources().getStringArray(R.array.fast_not_given_free_list), getResources().getString(R.string.fast_presumptive_refused), App.VERTICAL, App.VERTICAL, true,"REASON VOUCHER NOT GIVEN",new String[]{"REFUSED" , "OTHER"});

        promptInstruction2 = new MyTextView(context, getResources().getString(R.string.fast_prompt_instruction_2));
        promptInstruction2.setTextColor(Color.BLACK);
        promptInstruction2.setTypeface(null, Typeface.NORMAL);


        // Used for reset fields...
        views = new View[]{formDate.getButton(), sputumContainerGiven.getRadioGroup(), sputum_sample.getRadioGroup(),
                reasonNoSputumSample.getRadioGroup(), freeXrayVoucher.getRadioGroup(), noXrayVoucher.getRadioGroup()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, testingOfPresumptivePatientsTitle,promptInstruction1, sputumContainerGiven, sputum_sample, reasonNoSputumSample, dueDateSample, freeXrayVoucher, noXrayVoucher,promptInstruction2}};

        formDate.getButton().setOnClickListener(this);
        sputumContainerGiven.getRadioGroup().setOnCheckedChangeListener(this);
        sputum_sample.getRadioGroup().setOnCheckedChangeListener(this);
        reasonNoSputumSample.getRadioGroup().setOnCheckedChangeListener(this);
        freeXrayVoucher.getRadioGroup().setOnCheckedChangeListener(this);
        noXrayVoucher.getRadioGroup().setOnCheckedChangeListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();
            personDOB = personDOB.substring(0,10);

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

        secondDateCalendar.set(Calendar.YEAR, formDateCalendar.get(Calendar.YEAR));
        secondDateCalendar.set(Calendar.DAY_OF_MONTH, formDateCalendar.get(Calendar.DAY_OF_MONTH));
        secondDateCalendar.set(Calendar.MONTH, formDateCalendar.get(Calendar.MONTH));
        secondDateCalendar.add(Calendar.DAY_OF_MONTH, 1);
        dueDateSample.getEditText().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());

        Log.d("formdate", DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        Log.d("formdate", formDate.getButton().getText().toString());

        formDate.getButton().setEnabled(true);
    }

    @Override
    public boolean validate() {
        Boolean error = super.validate();
        emptyError = error;

        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            if(!emptyError)
                alertDialog.setMessage(getString(R.string.form_error));
            else
                alertDialog.setMessage(getString(R.string.fast_required_field_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
          //  DrawableCompat.setTint(clearIcon, color);
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
        final ArrayList<String[]> observations = getObservations();

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

        observations.add(new String[]{"SPUTUM SAMPLE DUE DATE", App.getSqlDate(secondDateCalendar)});

      //  observations.add(new String[]{"SPUTUM CONTAINER", App.get(sputumContainerGiven).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});
       /* if (sputum_sample.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"SPUTUM PRODUCED", App.get(sputum_sample).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});
        if (reasonNoSputumSample.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT DO NOT PRODUCE SPUTUM SAMPLE", App.get(reasonNoSputumSample).equals(getResources().getString(R.string.fast_patient_unable_to_expectorate)) ? "UNABLE TO EXPECTORATE" : "REFUSED"});

        if (freeXrayVoucher.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"VOUCHER GIVEN FOR TEST", App.get(freeXrayVoucher).equals(getResources().getString(R.string.fast_yes_title)) ? "YES" : "NO"});
        if (noXrayVoucher.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON VOUCHER NOT GIVEN", App.get(noXrayVoucher).equals(getResources().getString(R.string.fast_presumptive_refused)) ? "REFUSED" : "OTHER"});
*/

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
                if(App.getMode().equalsIgnoreCase("OFFLINE"))
                    id = serverService.saveFormLocally("FAST-Prompt", form, formDateCalendar,observations.toArray(new String[][]{}));

                String result = "";

                result = serverService.saveEncounterAndObservationTesting("FAST-Prompt", form, formDateCalendar, observations.toArray(new String[][]{}),id);
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
        super.refill(formId);
        OfflineForm fo = serverService.getSavedFormById(formId);
        ArrayList<String[][]> obsValue = fo.getObsValue();

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("SPUTUM SAMPLE DUE DATE")) {
                String secondDate = obs[0][1];
                secondDateCalendar.setTime(App.stringToDate(secondDate, "yyyy-MM-dd"));
                dueDateSample.getEditText().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
                dueDateSample.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());
        reasonNoSputumSample.setVisibility(View.GONE);
        dueDateSample.setVisibility(View.GONE);
        noXrayVoucher.setVisibility(View.GONE);
        noXrayVoucher.setVisibility(View.GONE);
        updateDisplay();

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
        if (radioGroup == sputumContainerGiven.getRadioGroup()) {
            if (sputumContainerGiven.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                sputum_sample.setVisibility(View.VISIBLE);
                if (sputum_sample.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_no_title))) {
                    reasonNoSputumSample.setVisibility(View.VISIBLE);
                }
            } else {
                sputum_sample.setVisibility(View.GONE);
                reasonNoSputumSample.setVisibility(View.GONE);
                dueDateSample.setVisibility(View.GONE);
            }
        } else if (radioGroup == sputum_sample.getRadioGroup()) {
            if (sputum_sample.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title)) && sputumContainerGiven.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                reasonNoSputumSample.setVisibility(View.GONE);
                dueDateSample.setVisibility(View.VISIBLE);
            } else {
                reasonNoSputumSample.setVisibility(View.VISIBLE);
                dueDateSample.setVisibility(View.GONE);
            }
        } else if (radioGroup == freeXrayVoucher.getRadioGroup()) {
            if (freeXrayVoucher.getRadioGroup().getSelectedValue().equals(getResources().getString(R.string.fast_yes_title))) {
                noXrayVoucher.setVisibility(View.GONE);
            } else {
                noXrayVoucher.setVisibility(View.VISIBLE);
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

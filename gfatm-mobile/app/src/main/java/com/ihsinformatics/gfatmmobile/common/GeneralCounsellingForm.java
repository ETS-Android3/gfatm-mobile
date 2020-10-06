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
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.model.OfflineForm;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Haris on 2/21/2017.
 */

public class GeneralCounsellingForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...

    TitledEditText health_centre;
    TitledRadioGroup condition_before_session;
    TitledCheckBoxes chief_complaint;
    TitledEditText chief_complaint_other;
    TitledRadioGroup cooperation;
    TitledRadioGroup defensive;
    TitledRadioGroup mental_distress;
    TitledRadioGroup condition_after_session;
    TitledRadioGroup improvement_after_session;
    TitledEditText patient_comments;
    TitledEditText doctor_notes;


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
        formName = Forms.GENERAL_COUNSELLING;
        form = Forms.generalCounselling;

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
        String columnName = "";
        final Object[][] locations = serverService.getAllLocationsFromLocalDB(columnName);
        String[] locationArray = new String[locations.length + 2];
        locationArray[0] = "";
        int j = 1;
        for (int i = 0; i < locations.length; i++) {
            locationArray[j] = String.valueOf(locations[i][16]);
            j++;
        }
        health_centre = new TitledEditText(context, null, getResources().getString(R.string.common_health_centre), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        condition_before_session = new TitledRadioGroup(context, null, getResources().getString(R.string.common_condition_before_session), getResources().getStringArray(R.array.common_condition_before_session_options), null, App.VERTICAL, App.VERTICAL, true,"CONDITION BEFORE SESSION",new String[]{ "VERY BAD" , "POOR" , "NEUTRAL" , "GOOD" , "VERY GOOD"});
        chief_complaint = new TitledCheckBoxes(context, null, getResources().getString(R.string.common_chief_complaint), getResources().getStringArray(R.array.common_chief_complaint_options), null, App.VERTICAL, App.VERTICAL, true,"CHIEF COMPLAINT",new String[]{ "SUICIDAL IDEATION" ,  "HOMICIDAL IDEATION" ,  "MANIC STATE" ,  "ANXIETY ATTACK" ,  "MEDICAL ADHERENCE" ,  "DEPRESSION" ,  "VERBAL ABUSE" ,  "SEXUAL ABUSE" ,  "RELATIONSHIP PROBLEMS" ,  "PHYSICAL ILLNESS" ,  "DAILY LIFE STRUGGLE" ,  "SUBSTANCE ABUSE" ,  "ECONOMIC PROBLEM" ,  "PHYSICAL ABUSE" ,  "SEXUAL PROBLEM" ,  "OTHER COMPLAINT"});
        chief_complaint_other = new TitledEditText(context, null, getResources().getString(R.string.common_chief_complaint_other_specify), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"OTHER COMPLAINT");
        cooperation = new TitledRadioGroup(context, null, getResources().getString(R.string.common_cooperation), getResources().getStringArray(R.array.common_cooperation_options), null, App.VERTICAL, App.VERTICAL, true,"CO-OPERATION",new String[]{"COOPERATIVE BEHAVIOUR" , "NON-COOPERATIVE BEHAVIOUR" , "UNCOMFORTABLE" , "COMPLAINTS"});
        defensive = new TitledRadioGroup(context, null, getResources().getString(R.string.common_defensive), getResources().getStringArray(R.array.common_defensive_options), null, App.VERTICAL, App.VERTICAL, true,"DEFENSIVENESS",new String[]{ "RESERVED BEHAVIOUR" , "AGGRESSIVE BEHAVIOUR" , "NORMAL" , "OPEN BEHAVIOUR"});
        mental_distress = new TitledRadioGroup(context, null, getResources().getString(R.string.common_mental_distress), getResources().getStringArray(R.array.common_mental_distress_options), null, App.VERTICAL, App.VERTICAL, true,"MENTAL DISTRESS",new String[]{ "SEVERE" , "MODERATE" , "MILD" , "RARELY" , "NONE"});
        condition_after_session = new TitledRadioGroup(context, null, getResources().getString(R.string.common_condition_after_session), getResources().getStringArray(R.array.common_condition_after_session_options), null, App.VERTICAL, App.VERTICAL, true,"CONDITION AFTER SESSION",new String[]{ "VERY BAD" , "POOR" , "NEUTRAL" , "GOOD" , "VERY GOOD"});
        improvement_after_session = new TitledRadioGroup(context, null, getResources().getString(R.string.common_improvement_after_session), getResources().getStringArray(R.array.common_improvement_after_session_options), null, App.VERTICAL, App.VERTICAL, true,"IMPROVEMENT AFTER MENTAL HEALTH SESSION",getResources().getStringArray(R.array.yes_no_list_concept));
        patient_comments = new TitledEditText(context, null, getResources().getString(R.string.common_patient_comments), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"CARETAKER COMMENTS");
        doctor_notes = new TitledEditText(context, null, getResources().getString(R.string.common_doctor_notes), "", "", 1000, RegexUtil.OTHER_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true,"CLINICIAN NOTES (TEXT)");


        // Used for reset fields...
        views = new View[]{formDate.getButton(), health_centre.getEditText(), condition_before_session.getRadioGroup(),
                chief_complaint, chief_complaint_other.getEditText(), cooperation.getRadioGroup(), defensive.getRadioGroup(), mental_distress.getRadioGroup(),
                condition_after_session.getRadioGroup(), improvement_after_session.getRadioGroup(), patient_comments.getEditText(), doctor_notes.getEditText(),
        };

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, health_centre, condition_before_session, chief_complaint, chief_complaint_other, cooperation, defensive, mental_distress,
                        condition_after_session, improvement_after_session, patient_comments, doctor_notes,
                }};

        formDate.getButton().setOnClickListener(this);
        for (CheckBox cb : chief_complaint.getCheckedBoxes())
            cb.setOnCheckedChangeListener(this);
        condition_before_session.getRadioGroup().setOnCheckedChangeListener(this);


        resetViews();
    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        String formDa = formDate.getButton().getText().toString();
        String personDOB = App.getPatient().getPerson().getBirthdate();


        Date date = new Date();

        if (!(formDate.getButton().getText().equals(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString()))) {


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
        Boolean error = super.validate();
        if (error) {

            int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(),R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.form_error));
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

        final ArrayList<String[]> observations =getObservations();

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
                    id = serverService.saveFormLocallyTesting(Forms.GENERAL_COUNSELLING, form, formDateCalendar, observations.toArray(new String[][]{}));

                String result = "";


                result = serverService.saveEncounterAndObservationTesting(Forms.GENERAL_COUNSELLING, form, formDateCalendar, observations.toArray(new String[][]{}), id);
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
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;


    }

    @Override
    public void onCheckedChanged(CompoundButton group, boolean isChecked) {
        Boolean flag = false;
        for (CheckBox cb : chief_complaint.getCheckedBoxes()) {
            if (cb.getText().equals(getResources().getString(R.string.common_chief_complaint_other)) && cb.isChecked()) {
                flag = true;
                break;
            }
        }
        if (flag)
            chief_complaint_other.setVisibility(View.VISIBLE);
        else
            chief_complaint_other.setVisibility(View.GONE);
    }


    @Override
    public void resetViews() {
        super.resetViews();
        chief_complaint_other.setVisibility(View.GONE);
        String val = App.getPatient().getPerson().getPersonAttribute("Health Center");
        if(val.equals("")){
            val = App.getPatient().getIdentifierlocation();
            val = serverService.getLocationDescriptionFromName(val);
        }else {
            try {
                int id = Integer.parseInt(val);
                Object[] locs = serverService.getLocationNameThroughLocationId(val);
                if (locs == null) val = "";
                else val = String.valueOf(locs[1]);
            }catch (Exception e){

            }

        }

        health_centre.getEditText().setText(val);
        health_centre.getEditText().setEnabled(false);
        formDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", formDateCalendar).toString());

        Boolean flag = true;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean openFlag = bundle.getBoolean("open");
            if (openFlag) {

                bundle.putBoolean("open", false);
                bundle.putBoolean("save", true);

                String id = bundle.getString("formId");
                int formId = Integer.valueOf(id);

                refill(formId);
                flag = false;

            } else bundle.putBoolean("save", false);

        }
        if (flag) {
            //HERE FOR AUTOPOPULATING OBS
//            final AsyncTask<String, String, String> autopopulateFormTaskDates = new AsyncTask<String, String, String>() {
//                @Override
//                protected String doInBackground(String... params) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            loading.setInverseBackgroundForced(true);
//                            loading.setIndeterminate(true);
//                            loading.setCancelable(false);
//                            loading.setMessage(getResources().getString(R.string.fetching_data));
//                            loading.show();
//                        }
//                    });
//
//                    latestmisseddate = serverService.getLatestObsValue(App.getPatientId(), "RETURN VISIT DATE");
//                    refrelobs = serverService.getLatestObsValue(App.getPatientId(), "Referral and Transfer", "REFERRING FACILITY NAME");
//                    return latestmisseddate;
//                }
//
//                @Override
//                protected void onProgressUpdate(String... values) {
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    super.onPostExecute(result);
//                    loading.dismiss();
//                    if (result != null) {
//                        secondDateCalendar.setTime(App.stringToDate(result, "yyyy-MM-dd"));
//                        missedVisitDate.getButton().setText(DateFormat.format("EEEE, MMM dd,yyyy", secondDateCalendar).toString());
//                    }
//
//                    if (refrelobs != null) {
//                        health_centre.getEditText().setText(refrelobs);
//                    }
//
//                }
//            };
//            autopopulateFormTaskDates.execute("");
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup == condition_before_session.getRadioGroup()) {

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

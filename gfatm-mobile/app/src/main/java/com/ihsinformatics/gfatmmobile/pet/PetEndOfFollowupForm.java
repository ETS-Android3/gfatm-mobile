package com.ihsinformatics.gfatmmobile.pet;

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

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class PetEndOfFollowupForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    TitledButton formDate;
    TitledSpinner reasonForFollowupEnd;
    TitledEditText explanation;
    TitledRadioGroup reasonForExclusion;
    TitledEditText other;
    Snackbar snackbar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        PAGE_COUNT = 1;
        FORM_NAME = Forms.PET_END_FOLLOWOUP;
        FORM = Forms.pet_endOfFollowup;

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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }


    @Override
    public void initViews() {

        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        reasonForFollowupEnd = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_reason_for_end_of_followup), getResources().getStringArray(R.array.pet_reasons_for_end_of_followup), "", App.VERTICAL, true);
        explanation = new TitledEditText(context, null, getResources().getString(R.string.pet_explanation), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        explanation.getEditText().setSingleLine(false);
        explanation.getEditText().setMinimumHeight(150);
        reasonForExclusion = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reason_for_exclusion), getResources().getStringArray(R.array.pet_reason_for_exclusions), "", App.VERTICAL, App.VERTICAL);
        other = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 250, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        other.getEditText().setSingleLine(false);
        other.getEditText().setMinimumHeight(150);

        views = new View[]{formDate.getButton(), reasonForFollowupEnd.getSpinner(), explanation.getEditText(), reasonForExclusion.getRadioGroup(), other.getEditText()};

        viewGroups = new View[][]{{formDate, reasonForFollowupEnd, explanation, reasonForExclusion, other}};

        formDate.getButton().setOnClickListener(this);
        reasonForFollowupEnd.getSpinner().setOnItemSelectedListener(this);
        reasonForExclusion.getRadioGroup().setOnCheckedChangeListener(this);

        explanation.setVisibility(View.GONE);
        other.setVisibility(View.GONE);

    }

    @Override
    public void resetViews() {

        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        explanation.setVisibility(View.GONE);
        other.setVisibility(View.GONE);

    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            if (formDateCalendar.after(new Date())) {

                Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");
                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }
    }


    @Override
    public boolean validate() {

        Boolean error = false;

        if (App.get(other).isEmpty() && other.getVisibility() == View.VISIBLE) {
            other.getQuestionView().setError(getResources().getString(R.string.mandatory_field));
            other.getEditText().requestFocus();
            error = true;
        }

        if (App.get(explanation).isEmpty() && explanation.getVisibility() == View.VISIBLE) {
            explanation.getQuestionView().setError(getResources().getString(R.string.mandatory_field));
            explanation.getEditText().requestFocus();
            error = true;
        }

        if (error) {

            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
            alertDialog.setMessage(getResources().getString(R.string.form_error));
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
            // DrawableCompat.setTint(clearIcon, color);
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
            return false;
        }

        return true;
    }

    @Override
    public boolean submit() {

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        observations.add(new String[]{"REASON TO END FOLLOW-UP", App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_treatment_completed)) ? "TREATMENT COMPLETE" :
                (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_loss_of_followup)) ? "LOST TO FOLLOW-UP" :
                        (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_enrolled_in_tb)) ? "ENROLLED IN TB PROGRAM" :
                                (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_transferred_out)) ? "TRANSFERRED OUT" :
                                        (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_not_eligible_for_study)) ? "NOT ELIGIBLE FOR PROGRAM" :
                                                (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" :
                                                        (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_died)) ? "DECEASED" : "OTHER"))))))});
        if (explanation.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON TO END FOLLOW-UP (TEXT)", App.get(explanation)});
        if (!App.get(reasonForExclusion).equals(""))
            observations.add(new String[]{"REASON FOR EXCLUSION FROM PET PROGRAM", App.get(reasonForExclusion).equals(getResources().getString(R.string.pet_xdr)) ? "EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION" :
                    (App.get(reasonForExclusion).equals(getResources().getString(R.string.pet_pre_xdr)) ? "PRE-EXTREMELY DRUG-RESISTANT TUBERCULOSIS INFECTION" :
                            (App.get(reasonForExclusion).equals(getResources().getString(R.string.pet_transfered_out_to_other_site)) ? "TRANSFERRED OUT" :
                                    (App.get(reasonForExclusion).equals(getResources().getString(R.string.pet_index_refused_treatment)) ? "REFUSAL OF TREATMENT BY PATIENT" : "OTHER NON-CODED")))});
        if (other.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER NON-CODED", App.get(other)});


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

                String result = serverService.saveEncounterAndObservation(FORM_NAME, FORM, formDateCalendar, observations.toArray(new String[][]{}));
                return result;

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
                    resetViews();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.form_submitted));
                    Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                    alertDialog.setIcon(submitIcon);
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
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.insert_error));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    int color = App.getColor(context, R.attr.colorAccent);
                    DrawableCompat.setTint(clearIcon, color);
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
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonForFollowupEnd.getSpinner()) {
            if (App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_transferred_out)) ||
                    App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_refused_treatment)) ||
                    App.get(reasonForFollowupEnd).equals(getResources().getString(R.string.pet_other)))
                explanation.setVisibility(View.VISIBLE);
            else
                explanation.setVisibility(View.GONE);
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group == reasonForExclusion.getRadioGroup()) {
            if (App.get(reasonForExclusion).equals(getResources().getString(R.string.pet_other)))
                other.setVisibility(View.VISIBLE);
            else
                other.setVisibility(View.GONE);
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

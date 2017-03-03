package com.ihsinformatics.gfatmmobile.childhoodTb;

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
import android.widget.CheckBox;
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
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Babar on 31/1/2017.
 */

public class ChildhoodTbTestIndicationForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;
    TitledButton formDate;
    TitledRadioGroup chestXray;
    TitledRadioGroup ultraSound;
    TitledRadioGroup ctScan;
    TitledRadioGroup geneXpert;
    TitledRadioGroup mantouxTest;
    TitledRadioGroup smearMicroscopy;
    TitledRadioGroup histopathology;
    TitledRadioGroup cbc;
    TitledRadioGroup esr;
    TitledRadioGroup drugSensitivityTest;
    TitledEditText doctorNotes;

    Snackbar snackbar;
    ScrollView scrollView;

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
        FORM_NAME = Forms.CHILDHOODTB_TEST_INDICATION_FORM;
        FORM = Forms.childhoodTb_test_indication_form;

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
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        } else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                scrollView = new ScrollView(context);
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
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
        chestXray = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_chest_xray),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        ultraSound = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_ultrasound),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        ctScan = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_ct_scan),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        geneXpert = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_gene_xpert),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        mantouxTest = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_mantoux),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        smearMicroscopy = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_smear_microscopy),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        histopathology = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_histopathology),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        cbc = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_cbc),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        esr = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_esr),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        drugSensitivityTest = new TitledRadioGroup(context,null,getResources().getString(R.string.ctb_drug_sensitivity),getResources().getStringArray(R.array.yes_no_options),getResources().getString(R.string.no),App.HORIZONTAL,App.VERTICAL,true);
        doctorNotes = new TitledEditText(context,null,getResources().getString(R.string.ctb_doctor_notes),"","",1000,RegexUtil.ALPHA_FILTER,InputType.TYPE_CLASS_TEXT,App.VERTICAL,false);
        views = new View[]{formDate.getButton(),chestXray.getRadioGroup(),ultraSound.getRadioGroup(),ctScan.getRadioGroup(),geneXpert.getRadioGroup(),mantouxTest.getRadioGroup(),smearMicroscopy.getRadioGroup(),
                histopathology.getRadioGroup(),cbc.getRadioGroup(),esr.getRadioGroup(),drugSensitivityTest.getRadioGroup()};
        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, chestXray,ultraSound,ctScan,geneXpert,mantouxTest,smearMicroscopy,
                histopathology,cbc,esr,drugSensitivityTest,doctorNotes}};

        formDate.getButton().setOnClickListener(this);
        chestXray.getRadioGroup().setOnCheckedChangeListener(this);
        ultraSound.getRadioGroup().setOnCheckedChangeListener(this);
        ctScan.getRadioGroup().setOnCheckedChangeListener(this);
        geneXpert.getRadioGroup().setOnCheckedChangeListener(this);
        mantouxTest.getRadioGroup().setOnCheckedChangeListener(this);
        smearMicroscopy.getRadioGroup().setOnCheckedChangeListener(this);
        histopathology.getRadioGroup().setOnCheckedChangeListener(this);
        cbc.getRadioGroup().setOnCheckedChangeListener(this);
        esr.getRadioGroup().setOnCheckedChangeListener(this);
        drugSensitivityTest.getRadioGroup().setOnCheckedChangeListener(this);


        resetViews();

    }

    @Override
    public void updateDisplay() {

        if (snackbar != null)
            snackbar.dismiss();

        if (!formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString())) {

            Date date = App.stringToDate(formDate.getButton().getText().toString(), "dd-MMM-yyyy");

            if (formDateCalendar.after(date)) {

                formDateCalendar = App.getCalendar(date);

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

            } else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        }



    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public boolean submit() {
        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        observations.add(new String[]{"REFERRED CHEST X RAY", App.get(chestXray).toUpperCase()});
        observations.add(new String[]{"REFERRED ULTRASOUND", App.get(ultraSound).toUpperCase()});
        observations.add(new String[]{"REFERRED CT SCAN", App.get(ctScan).toUpperCase()});
        observations.add(new String[]{"REFERRED GENEXPERT", App.get(geneXpert).toUpperCase()});
        observations.add(new String[]{"REFERRED MANTOUX TEST", App.get(mantouxTest).toUpperCase()});
        observations.add(new String[]{"REFERRED SMEAR MICROSCOPY", App.get(smearMicroscopy).toUpperCase()});
        observations.add(new String[]{"REFERRED HISTOPATHOLOGY OR FNAC", App.get(histopathology).toUpperCase()});
        observations.add(new String[]{"REFERRED CBC", App.get(cbc).toUpperCase()});
        observations.add(new String[]{"REFERRED ESR TEST", App.get(esr).toUpperCase()});
        observations.add(new String[]{"REFERRED DRUG SENSITIVITY TEST", App.get(esr).toUpperCase()});
        observations.add(new String[]{"CLINICIAN NOTES (TEXT)", App.get(doctorNotes)});



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

                String result = serverService.saveEncounterAndObservation("Test Indication", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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
                    resetViews();

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

        serverService.saveFormLocally(FORM_NAME, FORM, "12345-5", formValues);

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

        if (snackbar != null)
            snackbar.dismiss();

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

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

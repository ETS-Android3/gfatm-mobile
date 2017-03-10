package com.ihsinformatics.gfatmmobile.fast;

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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
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
 * Created by Haris on 2/10/2017.
 */

public class FastReferralAndTransferForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {
    Context context;

    // Views...
    TitledButton formDate;
    TitledRadioGroup referralTransfer;
    TitledSpinner reasonReferralTransfer;
    TitledEditText reasonReferralTransferOther;
    TitledSpinner referralSite;
    TitledEditText referralSiteOther;


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
        FORM_NAME = Forms.FAST_REFERRAL_AND_TRANSFER_FORM;
        FORM = Forms.fastReferralTransferForm;

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
        referralTransfer = new TitledRadioGroup(context, null, getResources().getString(R.string.fast_patient_being_referred_out_or_transferred_out), getResources().getStringArray(R.array.fast_referral_transfer_list), getResources().getString(R.string.fast_referral), App.VERTICAL, App.VERTICAL);
        reasonReferralTransfer = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_reason_for_referral_transfer), getResources().getStringArray(R.array.fast_reason_referral_transfer_list), getResources().getString(R.string.fast_patient_choose_another_facility), App.VERTICAL);
        reasonReferralTransferOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(columnName);
        String[] locationArray = new String[locations.length + 1];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }
        locationArray[locationArray.length - 1] = getResources().getString(R.string.fast_other_title);

        referralSite = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.fast_location_for_referral_transfer), locationArray, "", App.VERTICAL);
        referralSiteOther = new TitledEditText(context, null, getResources().getString(R.string.fast_if_other_specify), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), referralTransfer.getRadioGroup(), reasonReferralTransfer.getSpinner(),
                referralSite.getSpinner(), reasonReferralTransferOther.getEditText(), referralSiteOther.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, referralTransfer, reasonReferralTransfer, reasonReferralTransferOther, referralSite, referralSiteOther}};

        formDate.getButton().setOnClickListener(this);
        reasonReferralTransfer.getSpinner().setOnItemSelectedListener(this);
        referralSite.getSpinner().setOnItemSelectedListener(this);

        resetViews();
    }

    @Override
    public void updateDisplay() {
        if (snackbar != null)
            snackbar.dismiss();

        if (!(formDate.getButton().getText().equals(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString()))) {

            String formDa = formDate.getButton().getText().toString();
            String personDOB = App.getPatient().getPerson().getBirthdate();


            Date date = new Date();
            if (formDateCalendar.after(App.getCalendar(date))) {

                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));

                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.form_date_future), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();

                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

            }else if (formDateCalendar.before(App.getCalendar(App.stringToDate(personDOB, "yyyy-MM-dd'T'HH:mm:ss")))) {
                formDateCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                snackbar = Snackbar.make(mainContent, getResources().getString(R.string.fast_form_cannot_be_before_person_dob), Snackbar.LENGTH_INDEFINITE);
                TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                tv.setMaxLines(2);
                snackbar.show();
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
            }
            else
                formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        }
    }

    @Override
    public boolean validate() {
        Boolean error = false;

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
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Boolean saveFlag = bundle.getBoolean("save", false);
            String encounterId = bundle.getString("formId");
            if (saveFlag) {
                serverService.deleteOfflineForms(encounterId);
            }
            bundle.putBoolean("save", false);
        }

        endTime = new Date();

        final ArrayList<String[]> observations = new ArrayList<String[]>();
        observations.add(new String[]{"FORM START TIME", App.getSqlDateTime(startTime)});
        observations.add(new String[]{"FORM END TIME", App.getSqlDateTime(endTime)});
        /*observations.add (new String[] {"LONGITUDE (DEGREES)", String.valueOf(longitude)});
        observations.add (new String[] {"LATITUDE (DEGREES)", String.valueOf(latitude)});*/
        if (referralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"PATIENT BEING REFEREED OUT OR TRANSFERRED OUT", App.get(referralTransfer).equals(getResources().getString(R.string.fast_referral)) ? "PATIENT REFERRED" : "PATIENT TRANSFERRED OUT"});


        if (reasonReferralTransfer.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REASON FOR REFERRAL OR TRANSFER", App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_patient_choose_another_facility)) ? "PATIENT CHOOSE ANOTHER FACILITY" :
                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_drtb_suspect)) ? "MULTI-DRUG RESISTANT TUBERCULOSIS SUSPECTED" :
                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_drtb)) ? "DRUG RESISTANT TUBERCULOSIS" :
                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_treatment_failure)) ? "TUBERCULOSIS TREATMENT FAILURE" :
                                            (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_complicated_tb)) ? "COMPLICATED TUBERCULOSIS" :
                                                    (App.get(reasonReferralTransfer).equals(getResources().getString(R.string.fast_mycobacterium_other_than_tb)) ? "MYCOBACTERIUM TUBERCULOSIS" : "OTHER TRANSFER OR REFERRAL REASON")))))});

        if (reasonReferralTransferOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"OTHER TRANSFER OR REFERRAL REASON", App.get(reasonReferralTransferOther)});

        if (referralSite.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"REFERRING FACILITY NAME", referralSite.getSpinner().getSelectedItem().toString()});

        if (referralSiteOther.getVisibility() == View.VISIBLE)
            observations.add(new String[]{"LOCATION OF REFERRAL OR TRANSFER OTHER", App.get(referralSiteOther)});

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

                String result = serverService.saveEncounterAndObservation("Referral Form", FORM, formDateCalendar, observations.toArray(new String[][]{}));
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

      /*  formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);*/

        return true;
    }

    @Override
    public void refill(int formId) {

        OfflineForm fo = serverService.getOfflineFormById(formId);
        String date = fo.getFormDate();
        ArrayList<String[][]> obsValue = fo.getObsValue();
        formDateCalendar.setTime(App.stringToDate(date, "yyyy-MM-dd"));
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

        for (int i = 0; i < obsValue.size(); i++) {

            String[][] obs = obsValue.get(i);
            if (obs[0][0].equals("FORM START TIME")) {
                startTime = App.stringToDate(obs[0][1], "yyyy-MM-dd hh:mm:ss");
            } else if (obs[0][0].equals("PATIENT BEING REFEREED OUT OR TRANSFERRED OUT")) {

                for (RadioButton rb : referralTransfer.getRadioGroup().getButtons()) {
                    if (rb.getText().equals(getResources().getString(R.string.fast_referral)) && obs[0][1].equals("PATIENT REFERRED")) {
                        rb.setChecked(true);
                        break;
                    } else if (rb.getText().equals(getResources().getString(R.string.fast_transfer)) && obs[0][1].equals("PATIENT TRANSFERRED OUT")) {
                        rb.setChecked(true);
                        break;
                    }
                }
                referralTransfer.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REASON FOR REFERRAL OR TRANSFER")) {
                String value = obs[0][1].equals("PATIENT CHOOSE ANOTHER FACILITY") ? getResources().getString(R.string.fast_patient_choose_another_facility) :
                        (obs[0][1].equals("MULTI-DRUG RESISTANT TUBERCULOSIS SUSPECTED") ? getResources().getString(R.string.fast_drtb_suspect) :
                                (obs[0][1].equals("DRUG RESISTANT TUBERCULOSIS") ? getResources().getString(R.string.fast_drtb) :
                                        (obs[0][1].equals("TUBERCULOSIS TREATMENT FAILURE") ? getResources().getString(R.string.fast_treatment_failure) :
                                                (obs[0][1].equals("COMPLICATED TUBERCULOSIS") ? getResources().getString(R.string.fast_complicated_tb) :
                                                        (obs[0][1].equals("MYCOBACTERIUM TUBERCULOSIS") ? getResources().getString(R.string.fast_mycobacterium_other_than_tb) :
                                                                getResources().getString(R.string.fast_other_title))))));

                reasonReferralTransfer.getSpinner().selectValue(value);
                reasonReferralTransfer.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("OTHER TRANSFER OR REFERRAL REASON")) {
                reasonReferralTransferOther.getEditText().setText(obs[0][1]);
                reasonReferralTransferOther.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("REFERRING FACILITY NAME")) {
                referralSite.getSpinner().selectValue(obs[0][1]);
                referralSite.setVisibility(View.VISIBLE);
            } else if (obs[0][0].equals("LOCATION OF REFERRAL OR TRANSFER OTHER")) {
                referralSiteOther.getEditText().setText(obs[0][1]);
                referralSiteOther.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
            args.putBoolean("allowPastDate", true);
            args.putBoolean("allowFutureDate", false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MySpinner spinner = (MySpinner) parent;
        if (spinner == reasonReferralTransfer.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                reasonReferralTransferOther.setVisibility(View.VISIBLE);
            } else {
                reasonReferralTransferOther.setVisibility(View.GONE);
            }
        } else if (spinner == referralSite.getSpinner()) {
            if (parent.getItemAtPosition(position).toString().equals(getResources().getString(R.string.fast_other_title))) {
                referralSiteOther.setVisibility(View.VISIBLE);
            } else {
                referralSiteOther.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        reasonReferralTransferOther.setVisibility(View.GONE);
        referralSiteOther.setVisibility(View.GONE);

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

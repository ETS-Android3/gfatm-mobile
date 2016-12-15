package com.ihsinformatics.gfatmmobile.pet;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyTextView;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledCheckBoxes;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class IndexPatientRegistrationForm extends AbstractFormActivity {

    Context         context;

    // Views...
    TitledButton formDate;
    TitledEditText firstName;
    TitledEditText lastName;
    TitledEditText husbandName;
    TitledRadioGroup gender;

    TitledRadioGroup ageModifiers;
    TitledEditText age;
    TitledEditText indexPatientId;
    Button scanQRCode;
    TitledEditText indexExternalPatientId;
    TitledEditText ernsNumber;

    TitledRadioGroup tbType;
    TitledRadioGroup infectionType;
    TitledRadioGroup dstAvailable;
    TitledRadioGroup resistanceType;

    TitledRadioGroup patientType;

    TitledCheckBoxes dstPattern;

    TitledCheckBoxes treatmentRegimen;

    MyTextView enrollmentDateTextView;
    Calendar enrollmentDateCalender;
    DatePicker treatmentEnrollmentDate;

    /**
     *
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

        PAGE_COUNT = 7;
        FORM_NAME = Forms.INDEX_PATIENT_REGISTRATION;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager =  (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter());
        pager.setOnPageChangeListener (this);
        navigationSeekbar.setMax (PAGE_COUNT - 1);
        formName.setText(FORM_NAME);

        initViews();

        groups = new ArrayList<ViewGroup>();

        if(App.isLanguageRTL()){
            for (int i = PAGE_COUNT-1; i >= 0; i--) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        else {
            for (int i = 0; i < PAGE_COUNT; i++) {
                LinearLayout layout = new LinearLayout(mainContent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                for (int j = 0; j < viewGroups[i].length; j++) {

                    View v = viewGroups[i][j];
                    layout.addView(v);
                }
                ScrollView scrollView = new ScrollView(mainContent.getContext());
                scrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }

        gotoFirstPage();

        return mainContent;
    }

    /**
     *
     * Initializes all views and ArrayList and Views Array
     */
    public void initViews(){

        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format ("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        firstName = new TitledEditText(context, null, getResources().getString(R.string.pet_first_name), "", "", 20, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL,true);
        firstName.setTag("firstName");
        lastName = new TitledEditText(context, null, getResources().getString(R.string.pet_last_name), "", "", 20, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        lastName.setTag("lastName");
        husbandName = new TitledEditText(context, null, getResources().getString(R.string.pet_father_husband_name), "", "", 20, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        husbandName.setTag("husbandName");
        gender = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_gender), getResources().getStringArray(R.array.pet_genders), "Male", App.HORIZONTAL, App.HORIZONTAL);
        gender.setTag("gender");

        // second page views...
        ageModifiers = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_age_modifier), getResources().getStringArray(R.array.pet_age_modifiers), "Years", App.HORIZONTAL, App.HORIZONTAL);
        age = new TitledEditText(context, null, getResources().getString(R.string.pet_age), "", "", 3, RegexUtil.NumericFilter,  InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.IdFilter, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        indexExternalPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_external_id), "", "", 20, RegexUtil.AlphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        ernsNumber = new TitledEditText(context, null, getResources().getString(R.string.pet_erns_number), "", "", RegexUtil.idLength, RegexUtil.ernsFilter, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);

        // third page views...
        tbType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_type), getResources().getStringArray(R.array.pet_tb_types), "PTB", App.HORIZONTAL, App.VERTICAL);
        infectionType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_infection_type), getResources().getStringArray(R.array.pet_infection_types), "DSTB", App.HORIZONTAL, App.VERTICAL);
        dstAvailable = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_dst_available), getResources().getStringArray(R.array.yes_no_options), "Yes", App.HORIZONTAL, App.VERTICAL);
        resistanceType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_resistance_Type), getResources().getStringArray(R.array.pet_resistance_types), "RR-TB", App.VERTICAL, App.VERTICAL);

        // fourth page views...
        patientType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_patient_Type), getResources().getStringArray(R.array.pet_patient_types), "New", App.VERTICAL, App.VERTICAL);

        // fifth page viws...
        dstPattern = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_dst_pattern), getResources().getStringArray(R.array.pet_dst_patterns), null, App.VERTICAL, App.VERTICAL);

        // sixth page viws...
        treatmentRegimen = new TitledCheckBoxes(context, null, getResources().getString(R.string.pet_treatment_regimen), getResources().getStringArray(R.array.pet_treatment_regimens), null, App.VERTICAL, App.VERTICAL);

        //seventh page views...
        enrollmentDateTextView = new MyTextView(context, getResources().getString(R.string.pet_treatment_enrollement));
        treatmentEnrollmentDate = new DatePicker(context);
        MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
        enrollmentDateCalender = Calendar.getInstance();
        treatmentEnrollmentDate.init(enrollmentDateCalender.get(Calendar.YEAR), enrollmentDateCalender.get(Calendar.MONTH), enrollmentDateCalender.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
        treatmentEnrollmentDate.setMaxDate(new Date().getTime());

        // Used for reset fields...
        views = new View[]{formDate.getButton(), firstName.getEditText(), lastName.getEditText(), husbandName.getEditText(), gender.getRadioGroup(),
                                ageModifiers.getRadioGroup(), age.getEditText(), indexPatientId.getEditText(), indexExternalPatientId.getEditText(), ernsNumber.getEditText(),
                                tbType.getRadioGroup(), infectionType.getRadioGroup(), dstAvailable.getRadioGroup(), resistanceType.getRadioGroup(),
                                patientType.getRadioGroup(), dstPattern, treatmentRegimen};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                        {{formDate, firstName, lastName, husbandName, gender},
                        {ageModifiers, age, indexPatientId, scanQRCode, indexExternalPatientId, ernsNumber},
                        {tbType, infectionType, dstAvailable, resistanceType},
                        {patientType},
                        {dstPattern},
                        {treatmentRegimen},
                        {enrollmentDateTextView, treatmentEnrollmentDate}};

        formDate.getButton().setOnClickListener(this);
        scanQRCode.setOnClickListener(this);
    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format ("dd-MMM-yyyy", formDateCalendar).toString());
        enrollmentDateTextView.setError(null);
        if(!DateFormat.format ("dd-MMM-yyyy", formDateCalendar).toString().equals(DateFormat.format ("dd-MMM-yyyy", enrollmentDateCalender).toString())) {
            if (enrollmentDateCalender.after(formDateCalendar)) {
                enrollmentDateTextView.setError(getResources().getString(R.string.enrollment_date_error));
                enrollmentDateTextView.requestFocus();
            }
        }

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        enrollmentDateTextView.setError(null);
        if(!DateFormat.format ("dd-MMM-yyyy", formDateCalendar).toString().equals(DateFormat.format ("dd-MMM-yyyy", enrollmentDateCalender).toString())) {
            if (enrollmentDateCalender.after(formDateCalendar)) {
                gotoLastPage();
                enrollmentDateTextView.setError(getResources().getString(R.string.enrollment_date_error));
                enrollmentDateTextView.requestFocus();
                error = true;
            }
        }

        if(App.get(ernsNumber).isEmpty()){
            if(App.isLanguageRTL())
                gotoPage(5);
            else
                gotoPage(1);
            ernsNumber.getEditText().setError(getString(R.string.empty_field));
            ernsNumber.getEditText().requestFocus();
            error = true;
        }
        if(App.get(indexPatientId).isEmpty()){
            if(App.isLanguageRTL())
                gotoPage(5);
            else
                gotoPage(1);
            indexPatientId.getEditText().setError(getString(R.string.empty_field));
            indexPatientId.getEditText().requestFocus();
            error = true;
        }
        if(App.get(age).isEmpty()){
            if(App.isLanguageRTL())
                gotoPage(5);
            else
                gotoPage(1);
            age.getEditText().setError(getString(R.string.empty_field));
            age.getEditText().requestFocus();
            error = true;
        }

        if(App.get(husbandName).isEmpty()){
            gotoFirstPage();
            husbandName.getEditText().setError(getString(R.string.empty_field));
            husbandName.getEditText().requestFocus();
            error = true;
        }
        if(App.get(lastName).isEmpty()){
            gotoFirstPage();
            lastName.getEditText().setError(getString(R.string.empty_field));
            lastName.getEditText().requestFocus();
            error = true;
        }
        if(App.get(firstName).isEmpty()){
            gotoFirstPage();
           firstName.getEditText().setError(getString(R.string.empty_field));
           firstName.getEditText().requestFocus();
           error = true;
        }

        if(error) {

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
                                InputMethodManager imm = (InputMethodManager)mainContent.getContext().getSystemService(mainContent.getContext().INPUT_METHOD_SERVICE);
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

        if(validate()){

            resetViews();
        }

        //resetViews();
        return false;
    }

    @Override
    public boolean save() {

        HashMap<String, String> formValues = new HashMap<String, String>();

        formValues.put(formDate.getTag(), App.getSqlDate(formDateCalendar));
        formValues.put(firstName.getTag(), App.get(firstName));
        formValues.put(lastName.getTag(), App.get(lastName));
        formValues.put(husbandName.getTag(), App.get(husbandName));
        formValues.put(gender.getTag(), App.get(gender));

        serverService.saveFormLocally(FORM_NAME, "12345-5", formValues);

        return true;
    }

    @Override
    public void onClick(View view) {

        super.onClick(view);

        if(view == formDate.getButton()){
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
            formDateFragment.setArguments(args);
            formDateFragment.show(getFragmentManager(), "DatePicker");
        }
        else if (view == scanQRCode){
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if(App.isCallable(mainContent.getContext(),intent)){
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                }
                else{
                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_error));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            } catch (ActivityNotFoundException e) {
                int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_error));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
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
    public void resetViews(){
        super.resetViews();

        formDate.getButton().setText(DateFormat.format ("dd-MMM-yyyy", formDateCalendar).toString());
        enrollmentDateCalender = Calendar.getInstance();
        treatmentEnrollmentDate.updateDate(enrollmentDateCalender.get(Calendar.YEAR), enrollmentDateCalender.get(Calendar.MONTH), enrollmentDateCalender.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
				if (RegexUtil.isValidId(str) && !RegexUtil.isNumeric(str, false)) {
                    indexPatientId.getEditText().setText(str);
				}
                else {

                    int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                    final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                    alertDialog.setMessage(getString(R.string.warning_before_clear));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                    DrawableCompat.setTint(clearIcon, color);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_clear));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

				}
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext()).create();
                alertDialog.setMessage(getString(R.string.warning_before_clear));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_clear);
                DrawableCompat.setTint(clearIcon, color);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_clear));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            mainContent.getContext().getResources().updateConfiguration(config,
                    null);
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

    public class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            enrollmentDateCalender.set(year,month,day);

            enrollmentDateTextView.setError(null);
            if(!DateFormat.format ("dd-MMM-yyyy", formDateCalendar).toString().equals(DateFormat.format ("dd-MMM-yyyy", enrollmentDateCalender).toString())) {
                if (enrollmentDateCalender.after(formDateCalendar)) {
                    enrollmentDateTextView.setError(getResources().getString(R.string.enrollment_date_error));
                    enrollmentDateTextView.requestFocus();
                }
            }

        }

    }

}

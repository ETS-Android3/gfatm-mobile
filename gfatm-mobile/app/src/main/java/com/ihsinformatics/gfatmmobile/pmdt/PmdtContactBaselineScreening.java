package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.Barcode;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Tahira on 3/28/2017.
 */

public class PmdtContactBaselineScreening extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener {

    Context context;
    TitledButton formDate;
    TitledButton baselineContactScreeningDate;
    TitledEditText indexPatientId;
    ImageView validateIndexPatientIdView;
    Button scanQRCode;
    TitledSpinner relationWithIndex;
    TitledEditText otherRelationWithIndex;
    TitledRadioGroup contactCough;              //title : Contact Verbal Symptom Screening
    TitledSpinner contactCoughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup contactFever;
    TitledRadioGroup contactWeightLoss;
    TitledRadioGroup contactReducedAppetite;
    TitledRadioGroup contactReducedActivity;
    TitledRadioGroup contactNightSweats;
    TitledRadioGroup contactGlandularSwelling;
    TitledRadioGroup contactReferred;
    LinearLayout facilityLinearLayout;
    TextView referredFacilityText;
    AutoCompleteTextView referredFacilityAutoCompleteList;

    ScrollView scrollView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 2;
        FORM_NAME = Forms.PMDT_CONTACT_BASELINE_SCREENING;
        FORM = Forms.pmdtContactBaselineScreening;

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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
                scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                scrollView.setScrollbarFadingEnabled(false);
                scrollView.addView(layout);
                groups.add(scrollView);
            }
        }
        return mainContent;
    }

    @Override
    public void initViews() {
        formDate = new TitledButton(context, null, getResources().getString(R.string.form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        baselineContactScreeningDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_baseline_screening_date), DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString(), App.VERTICAL);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);

        LinearLayout linearLayout = new LinearLayout(context);
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pmdt_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.ID_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.9f
        );
        indexPatientId.setLayoutParams(param);
        linearLayout.addView(indexPatientId);
        validateIndexPatientIdView = new ImageView(context);
        validateIndexPatientIdView.setImageResource(R.drawable.ic_checked);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.1f
        );
        validateIndexPatientIdView.setLayoutParams(param1);
        validateIndexPatientIdView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        validateIndexPatientIdView.setPadding(0, 5, 0, 0);

        linearLayout.addView(validateIndexPatientIdView);

        relationWithIndex = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_relation_with_index_patient), getResources().getStringArray(R.array.pmdt_contact_relation_with_index), getResources().getString(R.string.pmdt_other), App.VERTICAL, true);
        otherRelationWithIndex = new TitledEditText(context, null, getResources().getString(R.string.pmdt_relation_with_index_patient_other), "", "", 15, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        contactCough = new TitledRadioGroup(context, getResources().getString(R.string.pmdt_title_contact_verbal_symptom_screening), getResources().getString(R.string.pmdt_contact_cough), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactCoughDuration = new TitledSpinner(context, null, getResources().getString(R.string.pmdt_contact_cough_duration), getResources().getStringArray(R.array.pmdt_cough_durations), getResources().getString(R.string.pmdt_less_than_two_weeks), App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_haemoptysis), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactFever = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_fever), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactWeightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_weight_loss), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactReducedAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_reduced_appetite), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactReducedActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_reduced_activity), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactNightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_night_sweats), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactGlandularSwelling = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_glandular_swelling), getResources().getStringArray(R.array.pmdt_yes_no_refused_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);
        contactReferred = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_contact_referred), getResources().getStringArray(R.array.pmdt_yes_no_unknown), getResources().getString(R.string.yes), App.HORIZONTAL, App.VERTICAL);

        // Fetching PMDT Locations
        String program = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            program = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            program = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            program = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            program = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            program = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(program);
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        referredFacilityText = new TextView(context);
        referredFacilityText.setText(getResources().getString(R.string.pmdt_contact_referred_facility));
        referredFacilityAutoCompleteList = new AutoCompleteTextView(context);
        final ArrayAdapter<String> autoCompleteFacilityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, locationArray);
        referredFacilityAutoCompleteList.setAdapter(autoCompleteFacilityAdapter);
        referredFacilityAutoCompleteList.setHint("Enter facility");
        facilityLinearLayout = new LinearLayout(context);
        facilityLinearLayout.setOrientation(LinearLayout.VERTICAL);
        facilityLinearLayout.addView(referredFacilityText);
        facilityLinearLayout.addView(referredFacilityAutoCompleteList);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), baselineContactScreeningDate.getButton(), indexPatientId.getEditText(), relationWithIndex.getSpinner(), otherRelationWithIndex.getEditText(), contactCough.getRadioGroup(), contactCoughDuration.getSpinner(),
                haemoptysis.getRadioGroup(), contactFever.getRadioGroup(), contactWeightLoss.getRadioGroup(), contactReducedAppetite.getRadioGroup(), contactReducedActivity.getRadioGroup(), contactNightSweats.getRadioGroup(),
                contactGlandularSwelling.getRadioGroup(), contactReferred.getRadioGroup(), referredFacilityAutoCompleteList};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, baselineContactScreeningDate, linearLayout, scanQRCode, relationWithIndex, otherRelationWithIndex, contactCough, contactCoughDuration, haemoptysis, contactFever},
                        {contactWeightLoss, contactReducedAppetite, contactReducedActivity, contactNightSweats, contactGlandularSwelling, contactReferred, facilityLinearLayout}};

        formDate.getButton().setOnClickListener(this);
        baselineContactScreeningDate.getButton().setOnClickListener(this);
        scanQRCode.setOnClickListener(this);
        relationWithIndex.getSpinner().setOnItemSelectedListener(this);
        contactCough.getRadioGroup().setOnCheckedChangeListener(this);
        contactCoughDuration.getSpinner().setOnItemSelectedListener(this);
        haemoptysis.getRadioGroup().setOnCheckedChangeListener(this);
        contactFever.getRadioGroup().setOnCheckedChangeListener(this);
        contactWeightLoss.getRadioGroup().setOnCheckedChangeListener(this);
        contactReducedAppetite.getRadioGroup().setOnCheckedChangeListener(this);
        contactReducedActivity.getRadioGroup().setOnCheckedChangeListener(this);
        contactNightSweats.getRadioGroup().setOnCheckedChangeListener(this);
        contactGlandularSwelling.getRadioGroup().setOnCheckedChangeListener(this);
        contactReferred.getRadioGroup().setOnCheckedChangeListener(this);

        indexPatientId.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (indexPatientId.getEditText().getText().length() > 0) {
                        if (indexPatientId.getEditText().getText().length() < RegexUtil.idLength) {
                            indexPatientId.getEditText().setError("id incorrect"); //change error text and properly validate patient ID
                            validateIndexPatientIdView.setVisibility(View.INVISIBLE);
                        } else {
                            validateIndexPatientIdView.setVisibility(View.VISIBLE);
                            validateIndexPatientIdView.setImageResource(R.drawable.ic_checked);
                        }
                    } else {
                        validateIndexPatientIdView.setVisibility(View.INVISIBLE);
                    }
//                    goneVisibility();
                    submitButton.setEnabled(false);


                } catch (NumberFormatException nfe) {
                    //Exception: User might be entering " " (empty) value
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        validateIndexPatientIdView.setOnTouchListener(this);


    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        baselineContactScreeningDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public boolean submit() {
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
        } else if (view == baselineContactScreeningDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", SECOND_DATE_DIALOG_ID);
            args.putBoolean("allowFutureDate", false);
            args.putBoolean("allowPastDate", true);
            secondDateFragment.setArguments(args);
            secondDateFragment.show(getFragmentManager(), "DatePicker");
        } else if (view == scanQRCode) {
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(context, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
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
                //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.barcode_scanner_missing));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                //DrawableCompat.setTint(clearIcon, color);
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
    public void refill(int encounterId) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void resetViews() {
        super.resetViews();
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        baselineContactScreeningDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", secondDateCalendar).toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    indexPatientId.getEditText().setText(str);
                    indexPatientId.getEditText().requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    indexPatientId.getEditText().setText("");
                    indexPatientId.getEditText().requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.invalid_scanned_id));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                    //DrawableCompat.setTint(clearIcon, color);
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
            } else if (resultCode == RESULT_CANCELED) {

                int color = App.getColor(context, R.attr.colorAccent);

                indexPatientId.getEditText().setText("");
                indexPatientId.getEditText().requestFocus();

                /*final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
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
                alertDialog.show();*/

            }
            // Set the locale again, since the Barcode app restores system's
            // locale because of orientation
            Locale.setDefault(App.getCurrentLocale());
            Configuration config = new Configuration();
            config.locale = App.getCurrentLocale();
            context.getResources().updateConfiguration(config, null);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                ImageView view = (ImageView) v;
                //overlay is black with transparency of 0x77 (119)
                view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                Boolean error = false;

//                validate patient ID and on success -> validateIndexPatientIdView.setImageResource(R.drawable.ic_checked_green);


                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                ImageView view = (ImageView) v;
                //clear the overlay
                view.getDrawable().clearColorFilter();
                view.invalidate();
                break;
            }
        }
        return true;
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

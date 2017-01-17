package com.ihsinformatics.gfatmmobile.pet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.MyLinearLayout;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rabbia on 11/24/2016.
 */

public class BaselineScreeningForm extends AbstractFormActivity implements RadioGroup.OnCheckedChangeListener {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText indexPatientId;
    Button scanQRCode;
    TitledRadioGroup treatmentStatus;
    TitledRadioGroup contactRegistered;
    TitledRadioGroup tbHistory;
    TitledSpinner relationship;
    TitledEditText otherRelation;
    TitledEditText cnic;
    TitledSpinner cnicOwner;
    TitledEditText otherCnicOwner;
    TitledEditText phone1;
    TitledEditText phone2;
    TitledEditText address1;
    TitledEditText address2;
    TitledEditText town;
    TitledEditText city;
    TitledRadioGroup addressType;
    TitledEditText landmark;
    TitledRadioGroup entryLocation;

    TitledRadioGroup cough;
    TitledRadioGroup coughDuration;
    TitledRadioGroup haemoptysis;
    TitledRadioGroup fever;
    TitledRadioGroup weightLoss;
    TitledRadioGroup reduceAppetite;
    TitledRadioGroup reduceActivity;
    TitledRadioGroup nightSweats;
    TitledRadioGroup swelling;
    TitledRadioGroup referral;
    TitledRadioGroup facilityType;
    TitledSpinner referredFacility;

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

        PAGE_COUNT = 2;
        FORM_NAME = Forms.PET_BASELINE_SCREENING;
        FORM = Forms.pet_baselineScreening;

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
        indexPatientId = new TitledEditText(context, null, getResources().getString(R.string.pet_index_patient_id), "", "", RegexUtil.idLength, RegexUtil.idFilter, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        scanQRCode = new Button(context);
        scanQRCode.setText("Scan QR Code");
        treatmentStatus = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_treatment_status), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL);
        contactRegistered = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_contact_registered), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL);
        tbHistory = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_tb_history), getResources().getStringArray(R.array.yes_no_options), "", App.HORIZONTAL, App.VERTICAL);
        relationship = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_relationship), getResources().getStringArray(R.array.pet_cnic_owners), "", App.VERTICAL);
        otherRelation = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 15, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        cnic = new TitledEditText(context, null, getResources().getString(R.string.pet_cnic), "", "", 13, RegexUtil.numericFilter, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        cnicOwner = new TitledSpinner(mainContent.getContext(), "", getResources().getString(R.string.pet_cnic_owner), getResources().getStringArray(R.array.pet_cnic_owners), "", App.VERTICAL);
        otherCnicOwner = new TitledEditText(context, null, getResources().getString(R.string.pet_other), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        phone1 = new TitledEditText(context, null, getResources().getString(R.string.pet_phone_1), "", "", RegexUtil.mobileNumberLength, RegexUtil.numericFilter, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        phone2 = new TitledEditText(context, null, getResources().getString(R.string.pet_phone_2), "", "", RegexUtil.mobileNumberLength, RegexUtil.numericFilter, InputType.TYPE_CLASS_PHONE, App.HORIZONTAL, true);
        address1 = new TitledEditText(context, null, getResources().getString(R.string.pet_address_1), "", "", 10, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        address2 = new TitledEditText(context, null, getResources().getString(R.string.pet_address_2), "", "", 50, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        town = new TitledEditText(context, null, getResources().getString(R.string.pet_town), "", "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        city = new TitledEditText(context, null, getResources().getString(R.string.pet_city), App.getCity(), "", 20, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        addressType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_address_type), getResources().getStringArray(R.array.pet_address_types), getResources().getString(R.string.pet_permanent), App.HORIZONTAL, App.VERTICAL);
        landmark = new TitledEditText(context, null, getResources().getString(R.string.pet_landmark), "", "", 50, RegexUtil.alphaFilter, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        entryLocation = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_event_location), getResources().getStringArray(R.array.pet_locations_of_entry), getResources().getString(R.string.pet_contact_home), App.HORIZONTAL, App.VERTICAL);

        // second page views...
        MyLinearLayout linearLayout = new MyLinearLayout(context, getResources().getString(R.string.pet_contact_verbal_screening), App.VERTICAL);
        cough = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_has_cough), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        coughDuration = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_cough_duration), getResources().getStringArray(R.array.pet_cough_durations), "", App.VERTICAL, App.VERTICAL);
        haemoptysis = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_haemoptysis), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        fever = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_fever), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        weightLoss = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_weight_loss), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        reduceAppetite = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reduced_appetite), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        reduceActivity = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_reduced_activity), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        nightSweats = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_night_sweats), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        swelling = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_swelling), getResources().getStringArray(R.array.yes_no_unknown_refused_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        referral = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_referral), getResources().getStringArray(R.array.yes_no_unknown_options), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        facilityType = new TitledRadioGroup(context, null, getResources().getString(R.string.pet_facility_type), getResources().getStringArray(R.array.pet_facility_types), getResources().getString(R.string.no), App.HORIZONTAL, App.VERTICAL);
        referredFacility = new TitledSpinner(mainContent.getContext(), null, getResources().getString(R.string.pet_facility_referred), getResources().getStringArray(R.array.programs), "", App.VERTICAL);
        linearLayout.addView(cough);
        linearLayout.addView(coughDuration);
        linearLayout.addView(haemoptysis);
        linearLayout.addView(fever);
        linearLayout.addView(weightLoss);
        linearLayout.addView(reduceAppetite);
        linearLayout.addView(reduceActivity);
        linearLayout.addView(nightSweats);
        linearLayout.addView(swelling);
        linearLayout.addView(referral);
        linearLayout.addView(facilityType);
        linearLayout.addView(referredFacility);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), indexPatientId.getEditText(), treatmentStatus.getRadioGroup(), contactRegistered.getRadioGroup(), tbHistory.getRadioGroup(), relationship.getSpinner(), otherRelation.getEditText(),
                cnic.getEditText(), cnicOwner.getSpinner(), otherCnicOwner.getEditText(), phone1.getEditText(), phone2.getEditText(), address1.getEditText(), address2.getEditText(), town.getEditText(), city.getEditText(),
                addressType.getRadioGroup(), landmark.getEditText(), entryLocation.getRadioGroup(),
                cough.getRadioGroup(), coughDuration.getRadioGroup(), haemoptysis.getRadioGroup(), fever.getRadioGroup(), weightLoss.getRadioGroup(), reduceAppetite.getRadioGroup(), reduceActivity.getRadioGroup(),
                nightSweats.getRadioGroup(), nightSweats.getRadioGroup(), swelling.getRadioGroup(), referral.getRadioGroup(), facilityType.getRadioGroup(), referredFacility.getSpinner()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, indexPatientId, scanQRCode, treatmentStatus, contactRegistered, tbHistory, relationship, otherRelation,
                        cnic, cnicOwner, otherCnicOwner, phone1, phone2, address1, address2, town, city, addressType, landmark, entryLocation},
                        {linearLayout}};

        formDate.getButton().setOnClickListener(this);

    }

    @Override
    public void updateDisplay() {

        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

    }

    @Override
    public boolean validate() {

        Boolean error = false;

        if (error) {

            // int color = App.getColor(mainContent.getContext(), R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(mainContent.getContext(), R.style.dialog).create();
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

        if (validate()) {

            resetViews();
        }

        //resetViews();
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
    public void onClick(View view) {

        super.onClick(view);

        if (view == formDate.getButton()) {
            Bundle args = new Bundle();
            args.putInt("type", DATE_DIALOG_ID);
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

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void resetViews() {
        super.resetViews();

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

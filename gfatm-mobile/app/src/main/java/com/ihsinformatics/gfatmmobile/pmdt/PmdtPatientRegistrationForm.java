package com.ihsinformatics.gfatmmobile.pmdt;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ihsinformatics.gfatmmobile.AbstractFormActivity;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.custom.TitledButton;
import com.ihsinformatics.gfatmmobile.custom.TitledEditText;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by Tahira on 1/5/2017.
 */

public class PmdtPatientRegistrationForm extends AbstractFormActivity {

    Context context;

    // Views...
    TitledButton formDate;
    TitledEditText cnic;

    TitledEditText externalPatientId;
    TitledEditText endTbEmrId;

    TitledEditText currentAddress1;
    TitledEditText currentAddress2;
    TitledSpinner currentAddressTown;
    TitledSpinner currentAddressCity;
    TitledEditText currentAddressLandmark;

    TitledEditText permanentAddress1;
    TitledEditText permanentAddress2;
    TitledSpinner permanentAddressTown;
    TitledSpinner permanentAddressCity;
    TitledEditText permanentAddressLandmark;

    TitledEditText primaryPhone;
    TitledEditText alternatePhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        pageCount = 4;
        formName = Forms.PMDT_PATIENT_REGISTRAITON;

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

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_form_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        cnic = new TitledEditText(context, null, getResources().getString(R.string.pmdt_cnic), "", getResources().getString(R.string.pmdt_cnic_hint), 15, RegexUtil.NIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        externalPatientId = new TitledEditText(context, "", getResources().getString(R.string.pmdt_external_id), "", getResources().getString(R.string.pmdt_external_patient_id_hint), 11, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        endTbEmrId = new TitledEditText(context, "", getResources().getString(R.string.pmdt_endtb_emr_id), "", getResources().getString(R.string.pmdt_endtb_emr_id_hint), 13, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        // second page views...
        currentAddress1 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_1), "", getResources().getString(R.string.pmdt_current_address_1_hint), 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        currentAddress2 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_2), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        currentAddressTown = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_current_address_town), getResources().getStringArray(R.array.pmdt_towns), getResources().getString(R.string.pmdt_gulshan_e_iqbal), App.HORIZONTAL);
        currentAddressCity = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_current_address_city), getResources().getStringArray(R.array.pmdt_cities), getResources().getString(R.string.pmdt_karachi), App.HORIZONTAL);
        currentAddressLandmark = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        // third page views...
        permanentAddress1 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_permanent_address_1), "", getResources().getString(R.string.pmdt_permanent_address_1_hint), 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        permanentAddress2 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_permanent_address_2), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, false);
        permanentAddressTown = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_permanent_address_town), getResources().getStringArray(R.array.pmdt_towns), "Gulshan-e-Iqbal", App.HORIZONTAL);
        permanentAddressCity = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_permanent_address_city), getResources().getStringArray(R.array.pmdt_cities), "Karachi", App.HORIZONTAL);
        permanentAddressLandmark = new TitledEditText(context, "", getResources().getString(R.string.pmdt_permanent_address_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        // fourth (or last) page views...
        primaryPhone = new TitledEditText(context, "", getResources().getString(R.string.pmdt_primary_phone), "", getResources().getString(R.string.pmdt_primary_phone_hint), 12, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        alternatePhone = new TitledEditText(context, "", getResources().getString(R.string.pmdt_alternate_phone), "", "", 12, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        // if Primary Phone owner is not defined, collect as 'self'
        // if Alternate Phone owner is not defined, collect as 'self'

        // Used for reset fields...
        views = new View[]{formDate.getButton(), cnic.getEditText(), externalPatientId.getEditText(), endTbEmrId.getEditText(), currentAddress1.getEditText(),
                currentAddress2.getEditText(), currentAddressTown.getSpinner(), currentAddressCity.getSpinner(), currentAddressLandmark.getEditText(),
                permanentAddress1.getEditText(), permanentAddress2.getEditText(), permanentAddressTown.getSpinner(), permanentAddressCity.getSpinner(), permanentAddressLandmark.getEditText(),
                primaryPhone.getEditText(), alternatePhone.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, cnic, externalPatientId, endTbEmrId},
                        {currentAddress1, currentAddress2, currentAddressTown, currentAddressCity, currentAddressLandmark},
                        {permanentAddress1, permanentAddress2, permanentAddressTown, permanentAddressCity, permanentAddressLandmark},
                        {primaryPhone, alternatePhone}};

        formDate.getButton().setOnClickListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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

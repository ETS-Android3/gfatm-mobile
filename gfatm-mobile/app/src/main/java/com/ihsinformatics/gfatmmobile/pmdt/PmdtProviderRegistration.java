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
import com.ihsinformatics.gfatmmobile.custom.TitledRadioGroup;
import com.ihsinformatics.gfatmmobile.custom.TitledSpinner;
import com.ihsinformatics.gfatmmobile.shared.Forms;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.ArrayList;

/**
 * Created by Tahira on 1/18/2017.
 */

public class PmdtProviderRegistration extends AbstractFormActivity {

    Context context;

    // Views...
    TitledButton formDate;

    TitledSpinner personRole;
    TitledSpinner registeringFacility;

    TitledEditText firstName;
    TitledEditText lastName;
    TitledEditText cnic;
    TitledRadioGroup gender;
    TitledButton dobDate;
    TitledEditText age;         //not editable - autopopulate

    TitledEditText primaryContact;
    TitledEditText alternateContact;

    TitledSpinner personPastHealthWorker;
    TitledSpinner personHealthWorker;

    TitledSpinner occupation;
    TitledEditText otherOccupation;

    TitledEditText address1;
    TitledEditText address2;
    TitledSpinner addressCityDistrict;  // List - To be decided by Program Team later
    TitledSpinner addressTownTaluka;       // Towns - To be decided by Program Team later
    TitledEditText addressLandmark;

    TitledSpinner administerInjections;
    TitledSpinner treatmentSupporterTraining;
    TitledSpinner trainingFacility;   // list of facilities/locations to be fetched from local database
    TitledEditText trainer;
    TitledButton trainingDate;

    TitledSpinner languages;
    TitledEditText otherLanguage;

    TitledEditText id;
    TitledEditText password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PAGE_COUNT = 4;
        FORM_NAME = Forms.PMDT_PROVIDER_REGISTRAITON;

        mainContent = super.onCreateView(inflater, container, savedInstanceState);
        context = mainContent.getContext();
        pager = (ViewPager) mainContent.findViewById(R.id.pager);
        pager.setAdapter(new PmdtProviderRegistration.MyAdapter());
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

    @Override
    public void initViews() {
        // first page views...
        formDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        formDate.setTag("formDate");
        personRole = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_person_role), getResources().getStringArray(R.array.pmdt_roles), getResources().getString(R.string.pmdt_treatment_coordinator), App.HORIZONTAL);
        registeringFacility = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_facility_registered), getResources().getStringArray(R.array.pmdt_roles), getResources().getString(R.string.pmdt_treatment_coordinator), App.HORIZONTAL);
        firstName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_first_name), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        lastName = new TitledEditText(context, "", getResources().getString(R.string.pmdt_last_name), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        cnic = new TitledEditText(context, null, getResources().getString(R.string.pmdt_cnic), "", getResources().getString(R.string.pmdt_cnic_hint), 15, RegexUtil.NIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        gender = new TitledRadioGroup(context, null, getResources().getString(R.string.pmdt_gender), getResources().getStringArray(R.array.pmdt_gender), "Male", App.HORIZONTAL, App.HORIZONTAL);
        dobDate = new TitledButton(context, null, getResources().getString(R.string.pmdt_dob), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);
        dobDate.setTag("dobDate");
        age = new TitledEditText(context, "", getResources().getString(R.string.pmdt_age), "", "", 2, null, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        primaryContact = new TitledEditText(context, "", getResources().getString(R.string.pmdt_primary_phone), "", getResources().getString(R.string.pmdt_primary_phone_hint), 12, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, true);
        alternateContact = new TitledEditText(context, "", getResources().getString(R.string.pmdt_alternate_phone), "", "", 12, RegexUtil.NUMERIC_FILTER, InputType.TYPE_CLASS_NUMBER, App.HORIZONTAL, false);
        // if Primary Phone owner is not defined, collect as 'self'
        // if Alternate Phone owner is not defined, collect as 'self'

        personPastHealthWorker = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_person_past), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL);
        personHealthWorker = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_person_healthworker), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL);
        occupation = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_occupation), getResources().getStringArray(R.array.occupation_list), getResources().getString(R.string.pmdt_artist), App.HORIZONTAL);
        otherOccupation = new TitledEditText(context, "", getResources().getString(R.string.pmdt_other_occupation), "", "", 100, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        address1 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_1), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        address2 = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_2), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.VERTICAL, true);
        addressCityDistrict = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_current_address_city), getResources().getStringArray(R.array.pmdt_cities), getResources().getString(R.string.pmdt_karachi), App.HORIZONTAL);
        addressLandmark = new TitledEditText(context, "", getResources().getString(R.string.pmdt_current_address_landmark), "", "", 50, null, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        administerInjections = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_administer_injections), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL);
        treatmentSupporterTraining = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_treatment_supporter_training), getResources().getStringArray(R.array.yes_no_options), getResources().getString(R.string.yes), App.HORIZONTAL);
        trainingFacility = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_training_facility), getResources().getStringArray(R.array.fast_locations), getResources().getString(R.string.pmdt_karachi), App.HORIZONTAL);
        trainer = new TitledEditText(context, "", getResources().getString(R.string.pmdt_trainer), "", "", 50, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);
        trainingDate = new TitledButton(context, null, getResources().getString(R.string.pet_date), DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString(), App.HORIZONTAL);

        languages = new TitledSpinner(context, "", getResources().getString(R.string.pmdt_mother_tongue), getResources().getStringArray(R.array.pmdt_mother_tongues), "", App.HORIZONTAL);
        otherLanguage = new TitledEditText(context, "", getResources().getString(R.string.pmdt_mother_tongue_other), "", "", 25, RegexUtil.ALPHA_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, false);

        id = new TitledEditText(context, "", getResources().getString(R.string.pmdt_id), "", "Enter id/username", 25, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_CLASS_TEXT, App.HORIZONTAL, true);
        password = new TitledEditText(context, "", getResources().getString(R.string.pmdt_password), "", "Enter password", 15, RegexUtil.ALPHANUMERIC_FILTER, InputType.TYPE_NUMBER_VARIATION_PASSWORD, App.HORIZONTAL, true);

        // Used for reset fields...
        views = new View[]{formDate.getButton(), personRole.getSpinner(), registeringFacility.getSpinner(), firstName.getEditText(), lastName.getEditText(),
                cnic.getEditText(), dobDate.getButton(), age.getEditText(), primaryContact.getEditText(), alternateContact.getEditText(), personPastHealthWorker.getSpinner(),
                personHealthWorker.getSpinner(), occupation.getSpinner(), otherOccupation.getEditText(), address1.getEditText(), address2.getEditText(),
                addressCityDistrict.getSpinner(), addressTownTaluka.getSpinner(), addressLandmark.getEditText(), administerInjections.getSpinner(), trainingFacility.getSpinner(),
                trainer.getEditText(), trainingDate.getButton(), languages.getSpinner(), otherLanguage.getEditText(), id.getEditText(), password.getEditText()};

        // Array used to display views accordingly...
        viewGroups = new View[][]
                {{formDate, personRole, registeringFacility, firstName, lastName, cnic, gender, dobDate, age},
                        {primaryContact, alternateContact, personPastHealthWorker, personHealthWorker, occupation, otherOccupation},
                        {address1, address2, addressCityDistrict, addressTownTaluka, addressLandmark},
                        {administerInjections, treatmentSupporterTraining, trainingFacility, trainer, trainingDate},
                        {languages, otherLanguage, id, password}};

        formDate.getButton().setOnClickListener(this);
        dobDate.getButton().setOnClickListener(this);
        trainingDate.getButton().setOnClickListener(this);
    }

    @Override
    public void updateDisplay() {
        formDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        dobDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
        trainingDate.getButton().setText(DateFormat.format("dd-MMM-yyyy", formDateCalendar).toString());
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
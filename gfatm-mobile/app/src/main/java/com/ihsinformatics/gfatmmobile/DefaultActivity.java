package com.ihsinformatics.gfatmmobile;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.RegexUtil;

public class DefaultActivity extends AbstractSettingActivity {

    EditText supportContact;
    EditText supportEmail;
    EditText city;
    Spinner country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        int color = App.getColor(this, R.attr.colorAccent);

        LinearLayout supportContactLayout = new LinearLayout(this);
        supportContactLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        supportContactLayout.setOrientation(LinearLayout.VERTICAL);
        supportContactLayout.setPadding(0,40,0,40);

        TextView supportContactTextView = new TextView(this);
        supportContactTextView.setText(getString(R.string.support_contact));
        supportContactTextView.setTextColor(color);
        supportContactLayout.addView(supportContactTextView);
        supportContact = new EditText(this);
        supportContact.setInputType (InputType.TYPE_CLASS_PHONE);
        supportContact.setMaxEms(RegexUtil.mobileNumberLength);
        supportContact.setFilters (new InputFilter[] {new InputFilter.LengthFilter (RegexUtil.mobileNumberLength)});
        supportContact.setSingleLine(true);
        supportContact.setText(App.getSupportContact());
        supportContact.setGravity(Gravity.LEFT);
        supportContactLayout.addView(supportContact);

        layout.addView(supportContactLayout);

        LinearLayout supportEmailLayout = new LinearLayout(this);
        supportEmailLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        supportEmailLayout.setOrientation(LinearLayout.VERTICAL);
        supportEmailLayout.setPadding(0,40,0,40);

        TextView supportEmailTextView = new TextView(this);
        supportEmailTextView.setText(getString(R.string.support_email));
        supportEmailTextView.setTextColor(color);
        supportEmailLayout.addView(supportEmailTextView);
        supportEmail = new EditText(this);
        supportEmail.setMaxEms(RegexUtil.defaultEditTextLength);
        supportEmail.setFilters (new InputFilter[] {new InputFilter.LengthFilter (RegexUtil.defaultEditTextLength)});
        supportEmail.setSingleLine(true);
        supportEmail.setText(App.getSupportEmail());
        supportEmailLayout.addView(supportEmail);

        layout.addView(supportEmailLayout);

        LinearLayout cityLayout = new LinearLayout(this);
        cityLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        cityLayout.setOrientation(LinearLayout.VERTICAL);
        cityLayout.setPadding(0,40,0,40);

        TextView cityTextView = new TextView(this);
        cityTextView.setText(getString(R.string.city));
        cityTextView.setTextColor(color);
        cityLayout.addView(cityTextView);
        city = new EditText(this);
        city.setMaxEms(RegexUtil.defaultEditTextLength);
        city.setFilters (new InputFilter[] {new InputFilter.LengthFilter (RegexUtil.defaultEditTextLength)});
        city.setSingleLine(true);
        city.setText(App.getCity());
        cityLayout.addView(city);

        layout.addView(cityLayout);

        LinearLayout countryLayout = new LinearLayout(this);
        countryLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        countryLayout.setOrientation(LinearLayout.VERTICAL);
        countryLayout.setPadding(0,40,0,40);

        TextView countryTextView = new TextView(this);
        countryTextView.setText(getString(R.string.country));
        countryTextView.setTextColor(color);
        countryLayout.addView(countryTextView);
        country = new Spinner(this);
        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        country.setAdapter(spinnerArrayAdapter);
        country.setSelection(App.getIndex(country, App.getCountry()));

        countryLayout.addView(country);

        layout.addView(countryLayout);

        resetButton.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {


        if(v == resetButton){

            supportContact.setError(null);
            supportEmail.setError(null);
            city.setError(null);

            supportContact.setText(getString(R.string.support_contact_default));
            supportEmail.setText(getString(R.string.support_email_default));
            city.setText(getString(R.string.city_default));
            country.setSelection(App.getIndex(country, getString(R.string.country_default)));

        }

        else if(v == okButton){

            Boolean cancel = validateFields();
            if(!cancel) {

                App.setSupportContact(App.get(supportContact));
                App.setSupportEmail(App.get(supportEmail));
                App.setCity(App.get(city));
                App.setCountry(App.get(country));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences (DefaultActivity.this);
                SharedPreferences.Editor editor = preferences.edit ();
                editor.putString (Preferences.SUPPORT_CONTACT, App.getSupportContact());
                editor.putString (Preferences.SUPPORT_EMAIL, App.getSupportEmail());
                editor.putString (Preferences.CITY, App.getCity());
                editor.putString (Preferences.COUNTRY, App.getCountry());
                editor.apply ();

                onBackPressed();
            }

        }

    }

    public Boolean validateFields(){

        supportContact.setError(null);
        supportEmail.setError(null);
        city.setError(null);

        Boolean cancel = false;

        if(App.get(city).isEmpty()) {
            city.setError(getString(R.string.empty_field));
            city.requestFocus();
            cancel = true;
        }
        else{

            if(!RegexUtil.isWord(App.get(city))) {
                city.setError(getString(R.string.invalid_value));
                city.requestFocus();
                cancel = true;
            }
        }

        if(App.get(supportEmail).isEmpty()) {
            supportEmail.setError(getString(R.string.empty_field));
            supportEmail.requestFocus();
            cancel = true;
        }
        else{

            if(!RegexUtil.isEmailAddress(App.get(supportEmail))) {
                supportEmail.setError(getString(R.string.invalid_value));
                supportEmail.requestFocus();
                cancel = true;
            }
        }

        if(App.get(supportContact).isEmpty()) {
            supportContact.setError(getString(R.string.empty_field));
            supportContact.requestFocus();
            cancel = true;
        }
        else{

            if(!RegexUtil.isContactNumber(App.get(supportContact))) {
                supportContact.setError(getString(R.string.invalid_value));
                supportContact.requestFocus();
                cancel = true;
            }
        }

        return cancel;
    }


}

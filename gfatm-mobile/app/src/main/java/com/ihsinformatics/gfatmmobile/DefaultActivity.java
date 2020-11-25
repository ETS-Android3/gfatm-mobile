package com.ihsinformatics.gfatmmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

public class DefaultActivity extends AbstractSettingActivity implements AdapterView.OnItemSelectedListener {

    EditText supportContact;
    EditText supportEmail;
    Spinner country;
    Spinner province;
    Spinner district;
    Spinner city;
    boolean flag = true;
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        serverService = new ServerService(getApplicationContext());

        int color = App.getColor(this, R.attr.colorAccent);

        LinearLayout supportContactLayout = new LinearLayout(this);
        supportContactLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        supportContactLayout.setOrientation(LinearLayout.VERTICAL);
        supportContactLayout.setPadding(0, 10, 0, 10);

        TextView supportContactTextView = new TextView(this);
        supportContactTextView.setText(getString(R.string.support_contact));
        supportContactTextView.setTextColor(color);
        supportContactLayout.addView(supportContactTextView);
        supportContact = new EditText(this);
        supportContact.setInputType(InputType.TYPE_CLASS_PHONE);
        supportContact.setMaxEms(RegexUtil.mobileNumberLength);
        supportContact.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        supportContact.setSingleLine(true);
        supportContact.setText(App.getSupportContact());
        supportContact.setGravity(Gravity.START);
        supportContactLayout.addView(supportContact);

        layout.addView(supportContactLayout);

        LinearLayout supportEmailLayout = new LinearLayout(this);
        supportEmailLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        supportEmailLayout.setOrientation(LinearLayout.VERTICAL);
        supportEmailLayout.setPadding(0, 10, 0, 10);

        TextView supportEmailTextView = new TextView(this);
        supportEmailTextView.setText(getString(R.string.support_email));
        supportEmailTextView.setTextColor(color);
        supportEmailLayout.addView(supportEmailTextView);
        supportEmail = new EditText(this);
        supportEmail.setMaxEms(RegexUtil.defaultEditTextLength);
        supportEmail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(RegexUtil.defaultEditTextLength)});
        supportEmail.setSingleLine(true);
        supportEmail.setText(App.getSupportEmail());
        supportEmailLayout.addView(supportEmail);

        layout.addView(supportEmailLayout);

        LinearLayout countryLayout = new LinearLayout(this);
        countryLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        countryLayout.setOrientation(LinearLayout.VERTICAL);
        countryLayout.setPadding(0, 10, 0, 10);

        TextView countryTextView = new TextView(this);
        countryTextView.setText(getString(R.string.country));
        countryTextView.setTextColor(color);
        countryLayout.addView(countryTextView);
        country = new Spinner(this);
        String[] countries = serverService.getCountryList();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries);
        country.setAdapter(spinnerArrayAdapter);
        country.setSelection(App.getIndex(country, App.getCountry()));

        countryLayout.addView(country);

        layout.addView(countryLayout);

        LinearLayout provinceLayout = new LinearLayout(this);
        provinceLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        provinceLayout.setOrientation(LinearLayout.VERTICAL);
        provinceLayout.setPadding(0, 10, 0, 10);

        TextView provinceTextView = new TextView(this);
        provinceTextView.setText(getString(R.string.province));
        provinceTextView.setTextColor(color);
        provinceLayout.addView(provinceTextView);
        province = new Spinner(this);
        String[] provinces = serverService.getProvinceList(App.getCountry());
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        province.setAdapter(spinnerArrayAdapter1);
        province.setSelection(App.getIndex(province, App.getProvince()));

        provinceLayout.addView(province);

        province.setOnItemSelectedListener(this);

        layout.addView(provinceLayout);

// DISTRICT
        LinearLayout districtLayout = new LinearLayout(this);
        districtLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        districtLayout.setOrientation(LinearLayout.VERTICAL);
        districtLayout.setPadding(0, 10, 0, 10);

        TextView districtTextView = new TextView(this);
        districtTextView.setText(getString(R.string.district));
        districtTextView.setTextColor(color);
        districtLayout.addView(districtTextView);
        district = new Spinner(this);
        String[] districts = serverService.getDistrictList(province.getSelectedItem().toString());
        ArrayAdapter<String> districtArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        district.setAdapter(districtArrayAdapter1);
        district.setSelection(App.getIndex(district, App.getDistrict()));

        districtLayout.addView(district);

        district.setOnItemSelectedListener(this);

        layout.addView(districtLayout);

        // CITY
        LinearLayout cityLayout = new LinearLayout(this);
        cityLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        cityLayout.setOrientation(LinearLayout.VERTICAL);
        cityLayout.setPadding(0, 10, 0, 10);

        TextView cityTextView = new TextView(this);
        cityTextView.setText(getString(R.string.city));
        cityTextView.setTextColor(color);
        cityLayout.addView(cityTextView);
        city = new Spinner(this);
        String[] cities = serverService.getCityList(district.getSelectedItem().toString());
        ArrayAdapter<String> cityArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        city.setAdapter(cityArrayAdapter1);
        city.setSelection(App.getIndex(city, App.getCity()));

        cityLayout.addView(city);

        district.setOnItemSelectedListener(this);

        layout.addView(cityLayout);


        resetButton.setVisibility(View.VISIBLE);

    }

    public void setProvinceSpinner(String country) {

        String[] provinces = serverService.getProvinceList(country);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        province.setAdapter(spinnerArrayAdapter1);

        if (flag) {
            province.setSelection(App.getIndex(province, App.getProvince()));
            flag = false;
        }

    }

    public void setDistrictSpinner(String province) {

        String[] districts = serverService.getDistrictList(province);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        district.setAdapter(spinnerArrayAdapter1);

        if (flag) {
            district.setSelection(App.getIndex(district, App.getDistrict()));
            flag = false;
        }
    }

    public void setCitySpinner(String district) {

        String[] cities = serverService.getCityList(district);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        city.setAdapter(spinnerArrayAdapter1);
        city.setSelection(App.getIndex(city, App.getCity()));

        if (flag) {
            city.setSelection(App.getIndex(city, App.getCity()));
            flag = false;
        }

    }

    @Override
    public void onClick(View v) {


        if (v == resetButton) {

            supportContact.setError(null);
            supportEmail.setError(null);

            supportContact.setText(getString(R.string.support_contact_default));
            supportEmail.setText(getString(R.string.support_email_default));
            flag = true;
            country.setSelection(App.getIndex(country, getString(R.string.country_default)));
            province.setSelection(App.getIndex(province, getString(R.string.province_default)));

        } else if (v == okButton) {

            Boolean cancel = validateFields();
            if (!cancel) {

                okButton.setClickable(false);
                Boolean flag = false;
                if (!App.getProvince().equals(App.get(province)))
                    flag = true;

                App.setSupportContact(App.get(supportContact));
                App.setSupportEmail(App.get(supportEmail));
                App.setCountry(App.get(country));
                App.setProvince(App.get(province));
                App.setCity(App.get(city));
                App.setDistrict(App.get(district));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DefaultActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.SUPPORT_CONTACT, App.getSupportContact());
                editor.putString(Preferences.SUPPORT_EMAIL, App.getSupportEmail());
                editor.putString(Preferences.COUNTRY, App.getCountry());
                editor.putString(Preferences.PROVINCE, App.getProvince());
                editor.putString(Preferences.DISTRICT, App.getDistrict());
                editor.putString(Preferences.CITY, App.getCity());
                if (flag) {

                    editor.putString(Preferences.LOCATION, "");
                    App.setLocation("");

                }
                editor.apply();

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // in case keyboard is not in display
                }

                onBackPressed();

                if (flag) {
                    Intent languageActivityIntent = new Intent(this, LocationSelectionDialog.class);
                    startActivity(languageActivityIntent);
                }
            }

        }

    }

    public Boolean validateFields() {

        supportContact.setError(null);
        supportEmail.setError(null);

        Boolean cancel = false;

        if (App.get(supportEmail).isEmpty()) {
            supportEmail.setError(getString(R.string.empty_field));
            supportEmail.requestFocus();
            cancel = true;
        } else {

            if (!RegexUtil.isEmailAddress(App.get(supportEmail))) {
                supportEmail.setError(getString(R.string.invalid_value));
                supportEmail.requestFocus();
                cancel = true;
            }
        }

        if (App.get(supportContact).isEmpty()) {
            supportContact.setError(getString(R.string.empty_field));
            supportContact.requestFocus();
            cancel = true;
        }

        if (province.getSelectedItem() == null || province.getSelectedItem().equals("")) {
            final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog).create();
            String message = getResources().getString(R.string.no_province);
            alertDialog.setMessage(message);
            Drawable clearIcon = ContextCompat.getDrawable(this, R.drawable.ic_submit);
            alertDialog.setIcon(clearIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_error));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            cancel = true;
        }

        return cancel;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;

        if (spinner == country) {
            String value = (String) spinner.getItemAtPosition(position);
            setProvinceSpinner(value);
        } else if (spinner == district) {
            String value = (String) spinner.getItemAtPosition(position);
            setCitySpinner(value);
        } else if (spinner == province) {
            String value = (String) spinner.getItemAtPosition(position);
            setDistrictSpinner(value);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

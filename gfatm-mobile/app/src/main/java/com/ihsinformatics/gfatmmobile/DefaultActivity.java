package com.ihsinformatics.gfatmmobile;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
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

import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

public class DefaultActivity extends AbstractSettingActivity implements AdapterView.OnItemSelectedListener {

    EditText supportContact;
    EditText supportEmail;
    Spinner country;
    Spinner province;
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
        supportContact.setGravity(Gravity.LEFT);
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
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countries);
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
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        province.setAdapter(spinnerArrayAdapter1);
        province.setSelection(App.getIndex(province, App.getProvince()));

        provinceLayout.addView(province);

        country.setOnItemSelectedListener(this);

        layout.addView(provinceLayout);

        resetButton.setVisibility(View.VISIBLE);

    }

    public void setProvinceSpinner(String country) {

        String[] provinces = serverService.getProvinceList(country);
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        province.setAdapter(spinnerArrayAdapter1);

        if (flag) {
            province.setSelection(App.getIndex(province, App.getProvince()));
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

                App.setSupportContact(App.get(supportContact));
                App.setSupportEmail(App.get(supportEmail));
                App.setCountry(App.get(country));
                App.setProvince(App.get(province));

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DefaultActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.SUPPORT_CONTACT, App.getSupportContact());
                editor.putString(Preferences.SUPPORT_EMAIL, App.getSupportEmail());
                editor.putString(Preferences.COUNTRY, App.getCountry());
                editor.putString(Preferences.PROVINCE, App.getProvince());
                editor.apply();

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                onBackPressed();
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
            Drawable clearIcon = getResources().getDrawable(R.drawable.error);
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
        String value = (String) spinner.getItemAtPosition(position);
        setProvinceSpinner(value);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

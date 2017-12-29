package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.model.SearchPatient;
import com.ihsinformatics.gfatmmobile.shared.RequestType;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rabbia on 11/10/2016.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Context context;

    ProgressDialog loading;

    ServerService serverService;

    LinearLayout searchFormLayout;
    LinearLayout searchResultLayout;
    LinearLayout resultLayout;
    Button searchPatientButton;
    Button searchPatientAgainButton;

    RadioGroup gender;
    Spinner ageRange;


    CheckBox nameCheckBox;
    EditText name;

    CheckBox mobileNumberCheckBox;
    EditText mobileNumber1;
    EditText mobileNumber2;

    CheckBox patientIdCheckBox;
    EditText patientId;
    Button scanBarcode;

    CheckBox healthCenterCheckBox;
    Spinner healthCenter;

    CheckBox motherNameCheckBox;
    EditText motherName;

    CheckBox cnicCheckBox;
    EditText cnic1;
    EditText cnic2;
    EditText cnic3;

    CheckBox guardianCheckBox;
    EditText guardianName;

    CheckBox programCheckBox;
    Spinner program;

    InputFilter[] alphaFilter = new InputFilter[1];
    InputFilter[] numericFilter = new InputFilter[1];
    InputFilter[] idFilter = new InputFilter[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_patient);

        context = this;

        serverService = new ServerService(getApplicationContext());
        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alphaFilter[0] = RegexUtil.ALPHA_FILTER;
        numericFilter[0] = RegexUtil.NUMERIC_FILTER;
        idFilter[0] = RegexUtil.ID_FILTER;

        searchPatientButton = (Button) findViewById(R.id.searchPatient);
        searchPatientAgainButton = (Button) findViewById(R.id.searchAgainPatient);
        searchFormLayout = (LinearLayout) findViewById(R.id.searchForm);
        searchResultLayout = (LinearLayout) findViewById(R.id.searchResult);
        resultLayout = (LinearLayout) findViewById(R.id.resultList);

        ageRange = (Spinner) findViewById(R.id.ageRange);
        gender = (RadioGroup) findViewById(R.id.gender);

        List<String> spinnerList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.age_ranges)));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        ageRange.setAdapter(spinnerArrayAdapter);

        nameCheckBox = (CheckBox) findViewById(R.id.nameCheckBox);
        name = (EditText) findViewById(R.id.name);
        name.setFilters(alphaFilter);

        mobileNumberCheckBox = (CheckBox) findViewById(R.id.mobileNumberCheckBox);
        mobileNumber1 = (EditText) findViewById(R.id.mobileNumber1);
        mobileNumber2 = (EditText) findViewById(R.id.mobileNumber2);
//        mobileNumber1.setFilters(numericFilter);

        patientIdCheckBox = (CheckBox) findViewById(R.id.patientIdCheckBox);
        patientId = (EditText) findViewById(R.id.patientId);
        patientId.setFilters(idFilter);
        scanBarcode = (Button) findViewById(R.id.scan_barcode);

        healthCenterCheckBox = (CheckBox) findViewById(R.id.healthCenterCheckBox);
        healthCenter = (Spinner) findViewById(R.id.healthCenter);

        final Object[][] locations = serverService.getAllLocationsFromLocalDB();
        String[] locationArray = new String[locations.length];
        for (int i = 0; i < locations.length; i++) {
            locationArray[i] = String.valueOf(locations[i][1]);
        }

        List<String> locationList = new ArrayList<String>(Arrays.asList(locationArray));
        ArrayAdapter<String> locationArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, locationList);
        healthCenter.setAdapter(locationArrayAdapter);
        healthCenter.setEnabled(false);

        motherNameCheckBox = (CheckBox) findViewById(R.id.motherNameCheckBox);
        motherName = (EditText) findViewById(R.id.motherName);
        motherName.setFilters(alphaFilter);

        cnicCheckBox = (CheckBox) findViewById(R.id.cnicCheckBox);
        cnic1 = (EditText) findViewById(R.id.cnic1);
        cnic1.setFilters(numericFilter);
        cnic2 = (EditText) findViewById(R.id.cnic2);
        cnic2.setFilters(numericFilter);
        cnic3 = (EditText) findViewById(R.id.cnic3);
        cnic3.setFilters(numericFilter);

        guardianCheckBox = (CheckBox) findViewById(R.id.guardianNameCheckBox);
        guardianName = (EditText) findViewById(R.id.guardianName);
        guardianName.setFilters(alphaFilter);

        programCheckBox = (CheckBox) findViewById(R.id.programCheckBox);
        program = (Spinner) findViewById(R.id.program);

        String[] programs = getResources().getStringArray(R.array.programs);
        List<String> programList = new ArrayList<String>(Arrays.asList(programs));
        ArrayAdapter<String> programArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, programList);
        program.setAdapter(programArrayAdapter);
        program.setEnabled(false);

        searchPatientButton.setOnClickListener(this);
        searchPatientAgainButton.setOnClickListener(this);
        scanBarcode.setOnClickListener(this);

        nameCheckBox.setOnCheckedChangeListener(this);
        mobileNumberCheckBox.setOnCheckedChangeListener(this);
        patientIdCheckBox.setOnCheckedChangeListener(this);
        healthCenterCheckBox.setOnCheckedChangeListener(this);
        motherNameCheckBox.setOnCheckedChangeListener(this);
        guardianCheckBox.setOnCheckedChangeListener(this);
        cnicCheckBox.setOnCheckedChangeListener(this);
        programCheckBox.setOnCheckedChangeListener(this);

        cnic1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (cnic1.getText().toString().length() == 5) {
                    cnic2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        cnic2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (cnic2.getText().toString().length() == 7) {
                    cnic3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        mobileNumber1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (mobileNumber1.getText().toString().length() == 4) {
                    mobileNumber2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

    }

    @Override
    public void onClick(View v) {

        if (v == searchPatientButton) {

            if (nameCheckBox.isChecked() || mobileNumberCheckBox.isChecked() || patientIdCheckBox.isChecked() ||
                    healthCenterCheckBox.isChecked() || cnicCheckBox.isChecked() || motherNameCheckBox.isChecked()
                    || guardianCheckBox.isChecked() || programCheckBox.isChecked()) {

                if (validate()) {

                    searchFormLayout.setVisibility(View.GONE);
                    searchResultLayout.setVisibility(View.VISIBLE);
                    search();
                }

            } else {

                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.provide_search_criteria));
                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                // DrawableCompat.setTint(clearIcon, color);
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

        } else if (v == searchPatientAgainButton) {
            searchFormLayout.setVisibility(View.VISIBLE);
            searchResultLayout.setVisibility(View.GONE);
        } else if (v == scanBarcode) {
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(context, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    int color = App.getColor(context, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
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
                int color = App.getColor(context, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    patientId.setText(str);
                } else {

                    int color = App.getColor(context, R.attr.colorAccent);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
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

                int color = App.getColor(context, R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
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
            context.getResources().updateConfiguration(config, null);
        }
    }

    public boolean validate() {

        Boolean flag = true;

        if (motherNameCheckBox.isChecked()) {
            if (App.get(motherName).isEmpty()) {
                motherName.setError(getString(R.string.empty_field));
                motherName.requestFocus();
                flag = false;
            } else if (App.get(motherName).length() <= 3) {
                motherName.setError(getString(R.string.invalid_value));
                motherName.requestFocus();
                flag = false;
            }
        }
        if (patientIdCheckBox.isChecked()) {
            if (App.get(patientId).isEmpty()) {
                patientId.setError(getString(R.string.empty_field));
                patientId.requestFocus();
                flag = false;
            } else if (!RegexUtil.isValidId(App.get(patientId))) {
                patientId.setError(getString(R.string.invalid_id));
                patientId.requestFocus();
                flag = false;
            }
        }
        if (mobileNumberCheckBox.isChecked()) {
            if (App.get(mobileNumber1).isEmpty()) {
                mobileNumber1.setError(getString(R.string.empty_field));
                mobileNumber1.requestFocus();
                flag = false;
            } else if (App.get(mobileNumber1).length() != 4) {
                mobileNumber1.setError(getString(R.string.invalid_value));
                mobileNumber1.requestFocus();
                flag = false;
            }

            if (App.get(mobileNumber2).isEmpty()) {
                mobileNumber2.setError(getString(R.string.empty_field));
                mobileNumber2.requestFocus();
                flag = false;
            } else if (App.get(mobileNumber2).length() != 7) {
                mobileNumber2.setError(getString(R.string.invalid_value));
                mobileNumber2.requestFocus();
                flag = false;
            }
        }
        if (nameCheckBox.isChecked()) {
            if (App.get(name).isEmpty()) {
                name.setError(getString(R.string.empty_field));
                name.requestFocus();
                flag = false;
            } else if (App.get(name).length() <= 3) {
                name.setError(getString(R.string.invalid_value));
                name.requestFocus();
                flag = false;
            }
        }
        if (cnicCheckBox.isChecked()) {
            if (App.get(cnic1).isEmpty()) {
                cnic1.setError(getString(R.string.empty_field));
                cnic1.requestFocus();
                flag = false;
            } else if (App.get(cnic1).length() != 5) {
                cnic1.setError(getString(R.string.invalid_value));
                cnic1.requestFocus();
                flag = false;
            }

            if (App.get(cnic2).isEmpty()) {
                cnic2.setError(getString(R.string.empty_field));
                cnic2.requestFocus();
                flag = false;
            } else if (App.get(cnic2).length() != 7) {
                cnic2.setError(getString(R.string.invalid_value));
                cnic2.requestFocus();
                flag = false;
            }

            if (App.get(cnic3).isEmpty()) {
                cnic3.setError(getString(R.string.empty_field));
                cnic3.requestFocus();
                flag = false;
            }

            if (guardianCheckBox.isChecked()) {
                if (App.get(guardianName).isEmpty()) {
                    guardianName.setError(getString(R.string.empty_field));
                    guardianName.requestFocus();
                    flag = false;
                } else if (App.get(guardianName).length() <= 3) {
                    guardianName.setError(getString(R.string.invalid_value));
                    guardianName.requestFocus();
                    flag = false;
                }
            }
        }

        return flag;

    }

    public void search() {

        final ArrayList<String[]> parameters = new ArrayList<String[]>();
        final ContentValues values = new ContentValues();

        int genderId = gender.getCheckedRadioButtonId();
        RadioButton genderSelectedButton = (RadioButton) findViewById(genderId);

        String gender = genderSelectedButton.getText().toString().equals("Male") ? "M" : "F";
        values.put("age_range", App.get(ageRange));
        values.put("gender", gender);

        if (nameCheckBox.isChecked()) {
            parameters.add(new String[]{"PERSON_NAME", App.get(name)});
        }
        if (mobileNumberCheckBox.isChecked()) {
            String mobileNumber = App.get(mobileNumber1) + "-" + App.get(mobileNumber2);
            parameters.add(new String[]{"CONTACT_NUMBER", mobileNumber});
        }
        if (patientIdCheckBox.isChecked()) {
            parameters.add(new String[]{"PATIENT_IDENTIFIER", App.get(patientId)});
        }
        if (healthCenterCheckBox.isChecked()) {
            parameters.add(new String[]{"HEALTH_CENTRE", App.get(healthCenter)});
        }
        if (motherNameCheckBox.isChecked()) {
            parameters.add(new String[]{"MOTHER_NAME", App.get(motherName)});
        }
        if (cnicCheckBox.isChecked()) {
            String cnicNumber = App.get(cnic1) + "-" + App.get(cnic2) + "-" + App.get(cnic3);
            parameters.add(new String[]{"CNIC", cnicNumber});
        }
        if (guardianCheckBox.isChecked()) {
            parameters.add(new String[]{"GUARDIAN_NAME", App.get(guardianName)});
        }
        if (programCheckBox.isChecked()) {
            parameters.add(new String[]{"PROGRAM", App.get(program).equals(getResources().getString(R.string.childhood_tb)) ? getResources().getString(R.string.childhood_tb_db) : App.get(program)});
        }

        AsyncTask<String, String, JSONObject> submissionFormTask = new AsyncTask<String, String, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.searching));
                        loading.show();
                    }
                });

                JSONObject result = serverService.searchPatients(RequestType.GFATM_SEARCH, values, parameters.toArray(new String[][]{}));
//                if (result.has("response"))
//                    return "SUCCESS";

                return result;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            @Override
            protected void onPostExecute(JSONObject result) {
                super.onPostExecute(result);
                loading.dismiss();

                try {
                    if (result != null) {
                        if (result.has("response")) {
                            if (result.get("response").equals("SUCCESS")) {
                                try {
                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }


                                ArrayList<SearchPatient> searchPatients = new ArrayList<>();

                                JSONArray personArray = result.getJSONArray("personArray");
                                for (int i = 0; i < personArray.length(); i++) {

                                    JSONObject person = personArray.getJSONObject(i);
                                    SearchPatient searchPatient = SearchPatient.parseJSONObject(person);
                                    searchPatients.add(searchPatient);
                                }

                                fillList(searchPatients);

//                            final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
//                            alertDialog.setMessage(getResources().getString(R.string.form_submitted));
//                            Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
//                            alertDialog.setIcon(submitIcon);
//                            int color = App.getColor(context, R.attr.colorAccent);
//                            DrawableCompat.setTint(submitIcon, color);
//                            alertDialog.setTitle(getResources().getString(R.string.title_completed));
//                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            try {
//                                                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
//                                                imm.hideSoftInputFromWindow(mainContent.getWindowToken(), 0);
//                                            } catch (Exception e) {
//                                                // TODO: handle exception
//                                            }
//                                            dialog.dismiss();
//                                        }
//                                    });
//                            alertDialog.show();
                            } else if (result.get("response").equals("ERROR")) {
                                resultLayout.removeAllViews();
                                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                                alertDialog.setMessage(getResources().getString(R.string.search_error) + "\n\n (" + result.get("details") + ")");
                                Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                                alertDialog.setIcon(clearIcon);
                                alertDialog.setTitle(getResources().getString(R.string.title_error));
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                                                } catch (Exception e) {
                                                    // TODO: handle exception
                                                }
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        }
                    } else {
                        resultLayout.removeAllViews();
                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getResources().getString(R.string.data_connection_error));
                        Drawable clearIcon = getResources().getDrawable(R.drawable.error);
                        alertDialog.setIcon(clearIcon);
                        alertDialog.setTitle(getResources().getString(R.string.title_error));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
                                        } catch (Exception e) {
                                            // TODO: handle exception
                                        }
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        submissionFormTask.execute("");


    }


    public void fillList(ArrayList<SearchPatient> patients) {
        resultLayout.removeAllViews();

        for (int j = 0; j < patients.size(); j++) {
            SearchPatient patient = patients.get(j);

            LinearLayout verticalLayout = new LinearLayout(this);
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            verticalLayout.setPadding(10, 20, 10, 20);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            linearLayout.setLayoutParams(params);
            linearLayout.setLayoutParams(params);
//            linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//            linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

            final LinearLayout moreLayout = new LinearLayout(this);
            moreLayout.setOrientation(LinearLayout.VERTICAL);

            final int color = App.getColor(this, R.attr.colorPrimaryDark);
            final int color1 = App.getColor(this, R.attr.colorAccent);


//            selection.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    for (RadioButton rb : radioButtons) {
//
//                        if (rb != v) {
//                            rb.setChecked(false);
//                        }
//                    }
//
//                    App.setLocation(v.getTag().toString());
//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LocationSetupActivity.this);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString(Preferences.LOCATION, App.getLocation());
//                    editor.apply();
//                }
//
//            });

            ImageView selectImage = new ImageView(this);
            selectImage.setImageResource(R.drawable.ic_checked);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(40, 40);
            selectImage.setLayoutParams(layoutParams);
            linearLayout.addView(selectImage);

            TextView tv = new TextView(this);
            tv.setText(patient.getFullName() + " ");
            tv.setTextSize(getResources().getDimension(R.dimen.medium));
            tv.setTextColor(color);
            tv.setPadding(10, 0, 0, 0);
            linearLayout.addView(tv);

            final TextView text = new TextView(this);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            text.setLayoutParams(params1);
            text.setText("(" + patient.getIdentifier() + ")");
            text.setTextSize(getResources().getDimension(R.dimen.small));
            text.setTag(patient.getIdentifier());
            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
            text.setPadding(10, 0, 0, 0);
            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
            linearLayout.addView(text);

            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    copyPatientId(text.getTag().toString());
                    Toast.makeText(context, getResources().getString(R.string.pid_copied), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            text.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    copyPatientId(text.getTag().toString());
                    Toast.makeText(context, getResources().getString(R.string.pid_copied), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


            verticalLayout.addView(linearLayout);
            moreLayout.setPadding(50, 0, 0, 0);
            moreLayout.setVisibility(View.GONE);


            if (!(patient.getGender() == null || patient.getGender().equals(""))) {

                LinearLayout ll2 = new LinearLayout(this);
                ll2.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv2 = new TextView(this);
                tv2.setText(getResources().getString(R.string.pmdt_gender) + " ");
                tv2.setTextSize(getResources().getDimension(R.dimen.small));
                tv2.setTextColor(color1);
                ll2.addView(tv2);

                TextView tv3 = new TextView(this);
                tv3.setText(patient.getGender().equals("M") ? "Male" : "Female");
                tv3.setTextSize(getResources().getDimension(R.dimen.small));
                ll2.addView(tv3);

                moreLayout.addView(ll2);
                moreLayout.setVisibility(View.VISIBLE);

            }

            if (!(patient.getDob() == null || patient.getDob().equals(""))) {

                LinearLayout ll3 = new LinearLayout(this);
                ll3.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv4 = new TextView(this);
                tv4.setText(getResources().getString(R.string.pmdt_dob) + " ");
                tv4.setTextSize(getResources().getDimension(R.dimen.small));
                tv4.setTextColor(color1);
                ll3.addView(tv4);

                TextView tv5 = new TextView(this);
                Date date = App.stringToDate(patient.getDob(), "yyyy-MM-dd");
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                tv5.setText(df.format(date));
                tv5.setTextSize(getResources().getDimension(R.dimen.small));
                ll3.addView(tv5);

                moreLayout.addView(ll3);
                moreLayout.setVisibility(View.VISIBLE);

            }

            if (!serverService.isPatientAvailableLocally(text.getTag().toString()))
                selectImage.setImageResource(R.drawable.ic_download);


            selectImage.setTag(text.getTag());
            selectImage.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (serverService.isPatientAvailableLocally(text.getTag().toString())) {
                        getPatient(text.getTag().toString());
                    }
                    else{
                        int color = App.getColor(context, R.attr.colorAccent);

                        final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                        alertDialog.setMessage(getString(R.string.warning_before_get_Patient));
                        Drawable clearIcon = getResources().getDrawable(R.drawable.ic_download);
                        DrawableCompat.setTint(clearIcon, color);
                        alertDialog.setIcon(clearIcon);
                        alertDialog.setTitle(getResources().getString(R.string.title_select_patient));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getPatient(text.getTag().toString());
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                    }
                    return false;

                }
            });

            if (!(patient.getAge() == null || patient.getAge().equals(""))) {
                LinearLayout ll4 = new LinearLayout(this);
                ll4.setOrientation(LinearLayout.HORIZONTAL);

                TextView tv6 = new TextView(this);
                tv6.setText(getResources().getString(R.string.age) + " ");
                tv6.setTextSize(getResources().getDimension(R.dimen.small));
                tv6.setTextColor(color1);
                ll4.addView(tv6);

                String age = patient.getAge();
                if(age.equals("0")) {
                    Date birthDate = App.stringToDate(patient.getDob(), "yyyy-MM-dd");
                    int ageInMonth = App.getDiffMonths(birthDate, new Date());
                    if (ageInMonth == 0) {
                        long ageInLong = App.getDiffDays(birthDate, new Date());
                        age = ageInLong + " days";
                    } else
                        age = ageInMonth + " months";
                }
                else age = age + " years";

                TextView tv7 = new TextView(this);
                tv7.setText(age);
                tv7.setTextSize(getResources().getDimension(R.dimen.small));
                ll4.addView(tv7);

                moreLayout.addView(ll4);
                moreLayout.setVisibility(View.VISIBLE);
            }

            verticalLayout.addView(moreLayout);

            if (moreLayout.getVisibility() == View.VISIBLE) {
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                        } else {
                            moreLayout.setVisibility(View.VISIBLE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                        }
                    }
                });

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (moreLayout.getVisibility() == View.VISIBLE) {
                            moreLayout.setVisibility(View.GONE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                        } else {
                            moreLayout.setVisibility(View.VISIBLE);
                            DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                            text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                        }
                    }
                });

            } else {
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
            }

            moreLayout.setVisibility(View.GONE);
            resultLayout.addView(verticalLayout);

        }

    }

    private void copyPatientId(String pid){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(pid);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",pid);
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == nameCheckBox) {
            name.setEnabled(isChecked);

        } else if (buttonView == mobileNumberCheckBox) {
            mobileNumber1.setEnabled(isChecked);
            mobileNumber2.setEnabled(isChecked);

        } else if (buttonView == patientIdCheckBox) {
            patientId.setEnabled(isChecked);
            scanBarcode.setEnabled(isChecked);

        } else if (buttonView == healthCenterCheckBox) {
            healthCenter.setEnabled(isChecked);

        } else if (buttonView == motherNameCheckBox) {
            motherName.setEnabled(isChecked);

        } else if (buttonView == cnicCheckBox) {
            cnic1.setEnabled(isChecked);
            cnic2.setEnabled(isChecked);
            cnic3.setEnabled(isChecked);

        } else if (buttonView == guardianCheckBox) {
            guardianName.setEnabled(isChecked);

        } else if (buttonView == programCheckBox ) {
            program.setEnabled(isChecked);

        }
    }


    public void getPatient(final String pid) {

        // Authenticate from server
        AsyncTask<String, String, String> getPatientTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.finding_patient));
                        loading.show();
                    }
                });

                String result = serverService.getPatient(pid, true);
                return result;

            }

            @Override
            protected void onProgressUpdate(String... values) {
            }

            ;

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();

                if (result == null || result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.data_connection_error) );
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
                } else if (result.equals("PATIENT_NOT_FOUND")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.patient_not_found));
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
                } else if (result.equals("OFFLINE_PATIENT")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.offline_patient));
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
                } else if (result.equals("FAIL")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.patient_get_error));
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
                } else {

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Preferences.PATIENT_ID, App.getPatientId());
                    editor.apply();

                    Intent intent = new Intent();
                    intent.putExtra("key", "SELECT");
                    setResult(RESULT_OK, intent);
                    finish();

                }

            }
        };
        getPatientTask.execute("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
package com.ihsinformatics.gfatmmobile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.model.Address;
import com.ihsinformatics.gfatmmobile.model.User;
import com.ihsinformatics.gfatmmobile.util.RegexUtil;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectPatientActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {

    protected static ProgressDialog loading;

    Calendar dateOfBirthCalendar;
    DialogFragment dobFragment;

    TextView cancelButton;
    TextView createPatient;
    TextView createButton;

    LinearLayout selectLayout;
    LinearLayout createLayout;

    EditText selectPatientId;
    Button selectPatientScanButton;
    Button searchPatient;

    EditText firstName;
    EditText lastName;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    EditText dob;
    EditText age;
    Spinner ageModifier;
    EditText createPatientId;
    Button createPatientScanButton;
    EditText externalId;
    ImageView selectButton;
    Toast toast;
    boolean flag = false;
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_patient);
        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);

        serverService = new ServerService(getApplicationContext());

        cancelButton = (TextView) findViewById(R.id.cancelButton);
        createButton = (TextView) findViewById(R.id.createPatientButton);
        createPatient = (TextView) findViewById(R.id.createPatient);
        selectLayout = (LinearLayout) findViewById(R.id.selectLayout);
        createLayout = (LinearLayout) findViewById(R.id.createLayout);

        selectPatientId = (EditText) findViewById(R.id.selectPatientId);
        selectPatientScanButton = (Button) findViewById(R.id.selectBarcodeScan);
        searchPatient = (Button) findViewById(R.id.searchPatientButton);

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        gender = (RadioGroup) findViewById(R.id.gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        createPatientId = (EditText) findViewById(R.id.createPatientId);
        createPatientScanButton = (Button) findViewById(R.id.createBarcodeScan);
        externalId = (EditText) findViewById(R.id.externalId);
        dob = (EditText) findViewById(R.id.dob);
        ageModifier = (Spinner) findViewById(R.id.age_modifier);
        dob.setKeyListener(null);

        dateOfBirthCalendar = Calendar.getInstance();
        dobFragment = new SelectDateFragment();

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    dobFragment.show(getFragmentManager(), "DatePicker");
                }
            }
        });

        age = (EditText) findViewById(R.id.age);

        age.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                dob.setError(null);
                if(s.length() != 0){

                    if (!flag) {
                        Calendar cal = Calendar.getInstance();

                        if(ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.years)))
                            cal.add(Calendar.YEAR, -Integer.parseInt(s.toString()));
                        else if(ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.months)))
                            cal.add(Calendar.MONTH, -Integer.parseInt(s.toString()));
                        else if(ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.days)))
                            cal.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(s.toString()));

                        dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                        dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());
                    }

                    if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.years)) && Integer.parseInt(age.getText().toString()) >= 120) {
                        age.setError(getResources().getString(R.string.age_invalid_year));
                        age.requestFocus();
                    }else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.months)) && Integer.parseInt(age.getText().toString()) >= 180) {
                        age.setError(getResources().getString(R.string.age_invalid_month));
                        age.requestFocus();
                    }else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.days)) && Integer.parseInt(age.getText().toString()) >= 5400) {
                        age.setError(getResources().getString(R.string.age_invalid_day));
                        age.requestFocus();
                    }
                    else
                        age.setError(null);

                }
                else{
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, 0);

                    dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dob.setText("");
                    dob.setError(null);
                }
            }
        });

        selectPatientId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(start == 5 && s.length()==5){
                    int i = selectPatientId.getSelectionStart();
                    if (i == 5){
                        selectPatientId.setText(selectPatientId.getText().toString().substring(0,4));
                       selectPatientId.setSelection(4);
                   }
                }
                else if(s.length()==5 && !s.toString().contains("-")){
                    selectPatientId.setText(s + "-");
                    selectPatientId.setSelection(6);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createPatientId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(start == 5 && s.length()==5){
                    int i = createPatientId.getSelectionStart();
                    if (i == 5){
                        createPatientId.setText(createPatientId.getText().toString().substring(0,4));
                        createPatientId.setSelection(4);
                    }
                }
                else if(s.length()==5 && !s.toString().contains("-")){
                    createPatientId.setText(s + "-");
                    createPatientId.setSelection(6);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectButton = (ImageView) findViewById(R.id.select);
        /*int color = App.getColor(this, R.attr.colorAccent);
        DrawableCompat.setTint(selectButton.getDrawable(), color);*/
        selectButton.setOnTouchListener(this);

        dob.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        createPatient.setOnClickListener(this);
        createButton.setOnClickListener(this);
        createPatientScanButton.setOnClickListener(this);
        selectPatientScanButton.setOnClickListener(this);
        searchPatient.setOnClickListener(this);
        ageModifier.setOnItemSelectedListener(this);


        this.setFinishOnTouchOutside(false);

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

                /*String id = App.get(selectPatientId).toString().toUpperCase();
                selectPatientId.setText(id);*/

                if (App.get(selectPatientId).isEmpty()) {
                    selectPatientId.setError(getString(R.string.empty_field));
                    selectPatientId.requestFocus();
                    error = true;
                } else if (!RegexUtil.isValidId(App.get(selectPatientId))) {
                    selectPatientId.setError(getString(R.string.invalid_id));
                    selectPatientId.requestFocus();
                    error = true;
                }

                if (!error) getPatient();

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

    public void getPatient() {

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

                String result = serverService.getPatient(App.get(selectPatientId), true, false);
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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

                    hideKeyboard();

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
    public void onClick(View v) {

        if (v == cancelButton) {

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            super.onBackPressed();

        } else if (v == searchPatient) {

            hideKeyboard();

            Intent intent = new Intent();
            intent.putExtra("key", "SEARCH");
            setResult(RESULT_OK, intent);
            finish();


        } else if (v == createPatient) {
            selectLayout.setVisibility(View.GONE);
            createLayout.setVisibility(View.VISIBLE);
            createButton.setVisibility(View.VISIBLE);

            setTitle(getResources().getString(R.string.title_create_new_patient));
        } else if (v == createButton) {
            if(createPatientValidate()){

                final String id = App.get(createPatientId);
                final String fname = App.get(firstName);
                final String lname = App.get(lastName);
                final String g = (male.isChecked()) ? "M" : "F";
                final String eId = App.get(externalId);

                AsyncTask<String, String, String> createPatientTask = new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setInverseBackgroundForced(true);
                                loading.setIndeterminate(true);
                                loading.setCancelable(false);
                                loading.setMessage(getResources().getString(R.string.creating_patient));
                                loading.show();
                            }
                        });

                        ContentValues values = new ContentValues();
                        values.put("patientId", id);
                        values.put("firstName", fname);
                        values.put("lastName", lname);
                        values.put("gender", g);
                        values.put("dob", App.getSqlDate(dateOfBirthCalendar));
                        values.put("externalId", eId);

                        String result = serverService.createPatient(values);

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

                        if (result.equals("CONNECTION_ERROR")) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                            alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
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
                            final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                            alertDialog.setMessage(getResources().getString(R.string.data_connection_error) + "\n\n (" + result + ")");
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
                        } else if (result.equals("DUPLICATE")) {
                            createPatientId.setError(getResources().getString(R.string.duplicate_patient_id));
                        } else if (result.equals("SUCCESS")) {

                            hideKeyboard();

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Preferences.PATIENT_ID, App.getPatientId());
                            editor.apply();

                            Intent intent = new Intent();
                            intent.putExtra("key", "CREATE");
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
                            alertDialog.setMessage(getResources().getString(R.string.patient_creation_error) + "\n\n (" + result + ")");
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
                        }



                    }
                };
                createPatientTask.execute("");

            }
        }
        else if ( v == dob){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            dobFragment.show(getFragmentManager(), "DatePicker");
        }
        else if ( v == createPatientScanButton){
            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(SelectPatientActivity.this, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
                final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
        else if( v == selectPatientScanButton){

            try {
                Intent intent = new Intent(Barcode.BARCODE_INTENT);
                if (App.isCallable(SelectPatientActivity.this, intent)) {
                    intent.putExtra(Barcode.SCAN_MODE, Barcode.QR_MODE);
                    startActivityForResult(intent, Barcode.BARCODE_RESULT_2);
                } else {
                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
                final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Retrieve barcode scan results
        if (requestCode == Barcode.BARCODE_RESULT) {
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    createPatientId.setText(str);
                    createPatientId.requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    createPatientId.setText("");
                    createPatientId.requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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

                int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                createPatientId.setText("");
                createPatientId.requestFocus();

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
            SelectPatientActivity.this.getResources().updateConfiguration(config,null);
        }
        else if(requestCode == Barcode.BARCODE_RESULT_2){
            if (resultCode == RESULT_OK) {
                String str = data.getStringExtra(Barcode.SCAN_RESULT);
                // Check for valid Id
                if (RegexUtil.isValidId(str)) {
                    selectPatientId.setText(str);
                    selectPatientId.requestFocus();
                } else {

                    //int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    selectPatientId.setText("");
                    selectPatientId.requestFocus();

                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this, R.style.dialog).create();
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

                int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                selectPatientId.setText("");
                selectPatientId.requestFocus();

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
            SelectPatientActivity.this.getResources().updateConfiguration(config,null);
        }
    }

    @Override
    public void onBackPressed() {

        if (createLayout.getVisibility() == View.VISIBLE) {

            selectLayout.setVisibility(View.VISIBLE);
            createLayout.setVisibility(View.GONE);
            createButton.setVisibility(View.GONE);

            firstName.setText("");
            lastName.setText("");
            male.setChecked(true);
            dob.setText("");
            age.setText("");
            createPatientId.setText("");
            externalId.setText("");
            ageModifier.setSelection(0);

            setTitle(getResources().getString(R.string.title_select_patient));

        } else
            super.onBackPressed();
    }

    public boolean createPatientValidate(){
        boolean error = false;

        if (App.get(createPatientId).isEmpty()) {
            createPatientId.setError(getString(R.string.empty_field));
            createPatientId.requestFocus();
            error = true;
        }else if(!RegexUtil.isValidId(App.get(createPatientId))){
            createPatientId.setError(getString(R.string.invalid_id));
            createPatientId.requestFocus();
            error = true;
        }
        if (App.get(dob).isEmpty()) {
            dob.setError(getString(R.string.empty_field));
            error = true;
        } else {
            if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.years)) && Integer.parseInt(age.getText().toString()) >= 120) {
                age.setError(getResources().getString(R.string.age_invalid_year));
                age.requestFocus();
                error = true;
            }else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.months)) && Integer.parseInt(age.getText().toString()) >= 180) {
                age.setError(getResources().getString(R.string.age_invalid_month));
                age.requestFocus();
                error = true;
            }else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.days)) && Integer.parseInt(age.getText().toString()) >= 5400) {
                age.setError(getResources().getString(R.string.age_invalid_day));
                age.requestFocus();
                error = true;
            }
            else
                age.setError(null);
        }
        if (App.get(lastName).isEmpty()) {
            lastName.setError(getString(R.string.empty_field));
            lastName.requestFocus();
            error = true;
        }
        if (App.get(firstName).isEmpty()) {
            firstName.setError(getString(R.string.empty_field));
            firstName.requestFocus();
            error = true;
        }

        if (error)
            return false;

        return true;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String s = age.getText().toString();
        if(!s.equals("")){

            if(!s.equals("0")) {

                Calendar cal = Calendar.getInstance();

                if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.years)))
                    cal.add(Calendar.YEAR, -Integer.parseInt(s.toString()));
                else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.months)))
                    cal.add(Calendar.MONTH, -Integer.parseInt(s.toString()));
                else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.days)))
                    cal.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(s.toString()));

                dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());
            } else{

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 0);
                cal.add(Calendar.MONTH, 0);
                cal.add(Calendar.DAY_OF_MONTH, 0);

                dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dob.setText("");
                dob.setError(null);
            }

            if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.years)) && Integer.parseInt(age.getText().toString()) >= 120) {
                age.setError(getResources().getString(R.string.age_invalid_year));
                age.requestFocus();
            }else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.months)) && Integer.parseInt(age.getText().toString()) >= 180) {
                age.setError(getResources().getString(R.string.age_invalid_month));
                age.requestFocus();
            }else if (ageModifier.getSelectedItem().toString().equals(getResources().getString(R.string.days)) && Integer.parseInt(age.getText().toString()) >= 30) {
                age.setError(getResources().getString(R.string.age_invalid_day));
                age.requestFocus();
            }
            else
                age.setError(null);

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int yy = dateOfBirthCalendar.get(Calendar.YEAR);
            int mm = dateOfBirthCalendar.get(Calendar.MONTH);
            int dd = dateOfBirthCalendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            String formDa = dob.getText().toString();

            dateOfBirthCalendar.set(yy, mm, dd);
            dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());

            Date date = new Date();

            ageModifier.setSelection(0);

            if (dateOfBirthCalendar.after(App.getCalendar(date))) {

                if (!formDa.equals("")) {
                    dateOfBirthCalendar = App.getCalendar(App.stringToDate(formDa, "dd-MMM-yyyy"));
                    dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());
                }

                flag = true;
                if (!formDa.equals(""))
                    age.setText(String.valueOf(App.getDiffYears(dateOfBirthCalendar.getTime(), new Date())));
                else
                    age.setText("");
                flag = false;

            } else {
                dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());
                flag = true;
                age.setText(String.valueOf(App.getDiffYears(dateOfBirthCalendar.getTime(), new Date())));
                flag = false;
            }

        }
    }

}

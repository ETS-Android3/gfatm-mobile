package com.ihsinformatics.gfatmmobile;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.RegexUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SelectPatientActivity extends AppCompatActivity implements View.OnClickListener {

    Calendar dateOfBirthCalendar;
    DialogFragment dobFragment;

    TextView cancelButton;
    TextView createPatient;
    TextView createButton;

    LinearLayout selectLayout;
    LinearLayout createLayout;

    EditText firstName;
    EditText lastName;
    RadioGroup gender;
    EditText dob;
    EditText age;
    EditText createPatientId;
    Button createPatientScanButton;
    EditText externalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_patient);

        cancelButton = (TextView) findViewById(R.id.cancelButton);
        createButton = (TextView) findViewById(R.id.createPatientButton);
        createPatient = (TextView) findViewById(R.id.createPatient);
        selectLayout = (LinearLayout) findViewById(R.id.selectLayout);
        createLayout = (LinearLayout) findViewById(R.id.createLayout);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        gender = (RadioGroup) findViewById(R.id.gender);
        createPatientId = (EditText) findViewById(R.id.createPatientId);
        createPatientScanButton = (Button) findViewById(R.id.createBarcodeScan);
        externalId = (EditText) findViewById(R.id.externalId);
        dob = (EditText) findViewById(R.id.dob);
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
                if(s.length() != 0){
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, -Integer.parseInt(s.toString()));

                    dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());

                }
                else{
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.YEAR, 0);

                    dateOfBirthCalendar.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dob.setText("");
                }
            }
        });

        dob.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        createPatient.setOnClickListener(this);
        createButton.setOnClickListener(this);
        createPatientScanButton.setOnClickListener(this);
    }

    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Calendar calendar = dateOfBirthCalendar;

            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            dateOfBirthCalendar.set(yy, mm, dd);
            dob.setText(DateFormat.format("dd-MMM-yyyy", dateOfBirthCalendar).toString());
            age.setText(String.valueOf(App.getDiffYears(dateOfBirthCalendar.getTime(), new Date())));

        }
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

        } else if (v == createPatient) {
            selectLayout.setVisibility(View.GONE);
            createLayout.setVisibility(View.VISIBLE);
            createButton.setVisibility(View.VISIBLE);

            setTitle(getResources().getString(R.string.title_create_new_patient));
        } else if (v == createButton) {
            /*selectLayout.setVisibility(View.VISIBLE);
            createLayout.setVisibility(View.GONE);
            createButton.setVisibility(View.GONE);

            setTitle(getResources().getString(R.string.title_select_patient));*/
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
                    int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
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
                int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);
                final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
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
                    createPatientId.setText(str);
                    createPatientId.requestFocus();
                } else {

                    int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                    final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
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

                int color = App.getColor(SelectPatientActivity.this, R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(SelectPatientActivity.this).create();
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
            SelectPatientActivity.this.getResources().updateConfiguration(config,null);
        }
    }


    @Override
    public void onBackPressed() {

        if (createLayout.getVisibility() == View.VISIBLE) {
            selectLayout.setVisibility(View.VISIBLE);
            createLayout.setVisibility(View.GONE);
            createButton.setVisibility(View.GONE);

            setTitle(getResources().getString(R.string.title_select_patient));
        } else
            super.onBackPressed();
    }

}

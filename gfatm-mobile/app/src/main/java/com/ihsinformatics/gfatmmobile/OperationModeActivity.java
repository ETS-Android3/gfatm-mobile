package com.ihsinformatics.gfatmmobile;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OperationModeActivity extends AbstractSettingActivity implements RadioGroup.OnCheckedChangeListener {

    LinearLayout userNameLayout;
    LinearLayout passwordLayout;
    String usernameTemp = "";
    String passwordTemp = "";
    EditText userNameEditText;
    EditText passwordEditText;
    protected static ProgressDialog loading;
    private ServerService serverService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        serverService = new ServerService(getApplicationContext());
        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        resetButton.setVisibility(View.VISIBLE);
        resetButton.setText(R.string.cancel);


        String[] modes = getResources().getStringArray(R.array.operation_modes);

        for (String mo : modes) {

            RadioButton rb = new RadioButton(this);
            rb.setText(mo);
            rb.setPadding(0, 40, 0, 40);
            rb.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            if (App.getMode().equals(mo))
                rb.setChecked(true);

            if (App.isLanguageRTL())
                rb.setGravity(Gravity.RIGHT);

        }

        userNameLayout = new LinearLayout(this);
        userNameLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        userNameLayout.setOrientation(LinearLayout.VERTICAL);
        userNameLayout.setPadding(0, 10, 0, 10);

        TextView userNameTextView = new TextView(this);
        userNameTextView.setText(getString(R.string.username));
        //userNameTextView.setTextColor(color);
        userNameLayout.addView(userNameTextView);
        userNameEditText = new EditText(this);
        userNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        userNameEditText.setMaxEms(35);
        userNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});
        userNameEditText.setSingleLine(true);
        userNameEditText.setText(App.getUsername());
        userNameEditText.setGravity(Gravity.LEFT);
        userNameEditText.setKeyListener(null);
        userNameEditText.setFocusable(false);
        userNameLayout.addView(userNameEditText);

        layout.addView(userNameLayout);

        passwordLayout = new LinearLayout(this);
        passwordLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        AppBarLayout.LayoutParams.MATCH_PARENT,
                        AppBarLayout.LayoutParams.WRAP_CONTENT));
        passwordLayout.setOrientation(LinearLayout.VERTICAL);
        passwordLayout.setPadding(0, 10, 0, 10);

        TextView passwordTextView = new TextView(this);
        passwordTextView.setText(getString(R.string.password));
        //userNameTextView.setTextColor(color);
        passwordLayout.addView(passwordTextView);
        passwordEditText = new EditText(this);
        passwordEditText.setMaxEms(35);
        passwordEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});
        passwordEditText.setSingleLine(true);
        passwordEditText.setGravity(Gravity.LEFT);
        passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        passwordLayout.addView(passwordEditText);

        layout.addView(passwordLayout);
        userNameLayout.setVisibility(View.GONE);
        passwordLayout.setVisibility(View.GONE);



        // App.g


      /*  if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                ((RadioButton) radioGroup.getChildAt(i)).setEnabled(false);
            }
        }*/



        RadioButton selectedButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (selectedButton.getText().toString().equals("Online") && App.getMode().equalsIgnoreCase("Offline")){
            userNameLayout.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.VISIBLE);
         }

        /* if(App.getMode().equalsIgnoreCase("Online")){
             radioGroup.getChildAt(0).setEnabled(false);
             radioGroup.check(radioGroup.getChildAt(1).getId());
         }*/

         radioGroup.setOnCheckedChangeListener(this);
         resetButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == okButton) {

            RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

            if (rb.getText().toString().equals("Online") && App.getMode().equalsIgnoreCase("Offline")) {
                if(!validatePassword()){
                    passwordEditText.setError(getResources().getString(R.string.empty_field));
                    passwordEditText.requestFocus();
                }
                else {
                    App.setMode("Online");
                    AsyncTask<String, String, String> submissionTask = new AsyncTask<String, String, String>() {
                        @Override
                        protected String doInBackground(String... params) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                            /*loading.setInverseBackgroundForced(true);*/
                                    loading.setIndeterminate(true);
                                    loading.setCancelable(false);
                                    loading.setMessage(getResources().getString(R.string.signing_in));
                                    loading.show();
                                }
                            });

                            usernameTemp = App.getUsername();
                            passwordTemp = App.getPassword();

                            App.setUsername(App.get(userNameEditText));
                            App.setPassword(App.get(passwordEditText));

                            App.setPatient(null);
                            App.setPatientId(null);

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OperationModeActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Preferences.PATIENT_ID, "");
                            editor.apply();

                            String result = serverService.getUser();
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
                            if (result.equals("SUCCESS")) {

                                // Save username and password in preferences
                                App.setUsername(App.get(userNameEditText));
                                App.setPassword(App.get(passwordEditText));
                                App.setAutoLogin("Enabled");
                                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                                App.setLastLogin(date);
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OperationModeActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(Preferences.USERNAME, App.getUsername());
                                editor.putString(Preferences.PASSWORD, App.getPassword());
                                editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                                editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                                editor.putString(Preferences.USER_FULLNAME, App.getUserFullName());
                                editor.putString(Preferences.ROLES, App.getRoles());
                                editor.putString(Preferences.PROVIDER_UUID, App.getProviderUUid());
                                editor.putString(Preferences.MODE, App.getMode());
                                editor.apply();

                                View view = getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                }

                                onBackPressed();

                            } else if (result.equals("AUTHENTICATION_ERROR")) {
                                App.setMode("Offline");
                                App.setUsername(usernameTemp);
                                App.setPassword(passwordTemp);
                                passwordEditText.setText("");
                                passwordEditText.setError(getResources().getString(R.string.authentication_error));
                                passwordEditText.requestFocus();
                            } else if (result.equals("CONNECTION_ERROR")) {
                                App.setMode("Offline");
                                App.setUsername(usernameTemp);
                                App.setPassword(passwordTemp);
                                passwordEditText.setText("");
                                passwordEditText.setError(getResources().getString(R.string.data_connection_error));
                                passwordEditText.requestFocus();
                            } else if (result.equals("PROVIDER_NOT_FOUND")) {
                                App.setMode("Offline");
                                App.setUsername(usernameTemp);
                                App.setPassword(passwordTemp);
                                passwordEditText.setText("");
                                passwordEditText.setError(getResources().getString(R.string.provider_not_found));
                                passwordEditText.requestFocus();
                            } else if (result.equals("USER_NOT_FOUND")) {
                                App.setMode("Offline");
                                App.setUsername(usernameTemp);
                                App.setPassword(passwordTemp);
                                passwordEditText.setText("");
                                passwordEditText.setError(getResources().getString(R.string.user_not_found));
                                passwordEditText.requestFocus();
                            } else if (result.equals("VERSION_MISMATCH")) {
                                App.setMode("Offline");
                                App.setUsername(usernameTemp);
                                App.setPassword(passwordTemp);
                                passwordEditText.setText("");
                                passwordEditText.setError(getResources().getString(R.string.version_mismatch));
                                passwordEditText.requestFocus();
                            }
                        }
                    };
                    submissionTask.execute("");
                }
            }
            else {

                int count = serverService.getPendingOfflineSavedFormsCount(App.getUsername());

                if(count >= App.OFFLINE_FORM_CAP){
                    final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.dialog).create();
                    String statement = getResources().getString(R.string.offline_forms_limit_error);
                    statement = statement.replace("#no#",String.valueOf(count));
                    alertDialog.setMessage(statement);
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_warning);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_alert));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            });
                    alertDialog.show();
                }
                else {
                    App.setMode(rb.getText().toString());
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OperationModeActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Preferences.MODE, App.getMode());
                    editor.apply();
                    onBackPressed();
                }
            }
        }
        if (v == resetButton) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            onBackPressed();
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton selectedButton = (RadioButton) findViewById(group.getCheckedRadioButtonId());
        if (selectedButton.getText().toString().equals("Online") && App.getMode().equalsIgnoreCase("Offline")){
            passwordEditText.setText("");
            userNameLayout.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.VISIBLE);
        }
        else{
            userNameLayout.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.GONE);
        }

    }

    public boolean validatePassword(){
        if(passwordEditText.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}

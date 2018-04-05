package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    protected static ProgressDialog loading;
    EditText username;
    EditText password;
    Button loginButton;
    CheckBox offlineCheckBox;
    String usernameTemp = "";
    String passwordTemp = "";
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        serverService = new ServerService(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        offlineCheckBox = (CheckBox) findViewById(R.id.offlineCheckBox);

        loginButton.setOnClickListener(this);

        String titleText = getResources().getString(R.string.app_name) + " - " + App.getVersion();
        getSupportActionBar().setTitle(titleText);

        username.setText(App.getUsername());

        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
    }


    public boolean validate() {

        Boolean cancel = false;

        password.setError(null);
        username.setError(null);

        if (TextUtils.isEmpty(App.get(password))) {
            password.setError(getResources().getString(R.string.empty_field));
            password.requestFocus();
            cancel = true;
        }

        if (TextUtils.isEmpty(App.get(username))) {
            username.setError(getResources().getString(R.string.empty_field));
            username.requestFocus();
            cancel = true;
        }

        return cancel;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        if (!validate()) {

            if (offlineCheckBox.isChecked())
                App.setMode(getResources().getString(R.string.offline));
            else
                App.setMode(getResources().getString(R.string.online));

            // Authenticate from server
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

                    App.setUsername(App.get(username));
                    App.setPassword(App.get(password));

                    App.setPatient(null);
                    App.setPatientId(null);

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
                        App.setUsername(App.get(username));
                        App.setPassword(App.get(password));
                        App.setLocation("");
                        Date time = Calendar.getInstance().getTime();
                        App.setLastActivity(time);

                        App.setAutoLogin("Enabled");
                        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                        App.setLastLogin(date);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.USERNAME, App.getUsername());
                        editor.putString(Preferences.PASSWORD, App.getPassword());
                        editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                        editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                        editor.putString(Preferences.USER_FULLNAME, App.getUserFullName());
                        editor.putString(Preferences.ROLES, App.getRoles());
                        editor.putString(Preferences.PROVIDER_UUID, App.getProviderUUid());
                        editor.putString(Preferences.PATIENT_ID, "");
                        editor.putString(Preferences.MODE, App.getMode());
                        String timeString = App.getSqlDateTime(time);
                        editor.putString(Preferences.LAST_ACTIVITY, timeString);
                        editor.putString(Preferences.LOCATION, App.getLocation());

                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (result.equals("AUTHENTICATION_ERROR")) {
                        App.setUsername(usernameTemp);
                        App.setPassword(passwordTemp);
                        password.setText("");
                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.authentication_error), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (result.equals("CONNECTION_ERROR")) {
                        App.setUsername(usernameTemp);
                        App.setPassword(passwordTemp);
                        password.setText("");
                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.data_connection_error), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (result.equals("PROVIDER_NOT_FOUND")) {
                        App.setUsername(usernameTemp);
                        App.setPassword(passwordTemp);
                        password.setText("");
                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.provider_not_found), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (result.equals("USER_NOT_FOUND")) {
                        App.setUsername(usernameTemp);
                        App.setPassword(passwordTemp);
                        password.setText("");
                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.user_not_found), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (result.equals("VERSION_MISMATCH")){
                        App.setUsername(usernameTemp);
                        App.setPassword(passwordTemp);
                        password.setText("");
                        Toast toast = Toast.makeText(LoginActivity.this, getResources().getString(R.string.version_mismatch), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                }
            };
            submissionTask.execute("");
        }

    }

    @Override
    public void onClick(View v) {

        if (v == loginButton) {

            if(offlineCheckBox.isChecked()){

                int count = serverService.getPendingOfflineSavedFormsCount(App.get(username));

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
                                }
                            });
                    alertDialog.show();
                    return;
                }

            }

            attemptLogin();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.popup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_server) {

            Intent serverActivityIntent = new Intent(this, ServerActivity.class);
            startActivity(serverActivityIntent);

            return true;
        } else if (id == R.id.menu_defaults) {

            Intent defaultActivityIntent = new Intent(this, DefaultActivity.class);
            startActivity(defaultActivityIntent);

            return true;
        } else if (id == R.id.menu_language) {

            Intent languageActivityIntent = new Intent(this, LanguageActivity.class);
            startActivity(languageActivityIntent);

            return true;
        } else if (id == R.id.menu_theme) {

            Intent themeActivityIntent = new Intent(this, ThemeActivity.class);
            startActivity(themeActivityIntent);

            return true;
        } else if (id == R.id.nav_ssl_encryption) {

            Intent sslActivityIntent = new Intent(this, SSLEncryptionActivity.class);
            startActivity(sslActivityIntent);

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String lang = preferences.getString(Preferences.LANGUAGE, "");

        if (!App.getLanguage().equals(lang)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preferences.LANGUAGE, App.getLanguage());
            editor.apply();

            restartActivity();
        }

        serverService = new ServerService(getApplicationContext());

    }

    /**
     * Restarts the current activity...
     */
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


}



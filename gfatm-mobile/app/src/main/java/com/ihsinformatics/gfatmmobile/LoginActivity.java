package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.SimpleDateFormat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        loading = new ProgressDialog(this);
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

            // Authenticate from server
            AsyncTask<String, String, Boolean> authenticationTask = new AsyncTask<String, String, Boolean>() {
                @Override
                protected Boolean doInBackground(String... params) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setInverseBackgroundForced(true);
                            loading.setIndeterminate(true);
                            loading.setCancelable(false);
                            loading.setMessage(getResources().getString(R.string.signing_in));
                            loading.show();
                        }
                    });

                   /* if ((App.isOfflineMode ()) || (App.get (username).equalsIgnoreCase (defaultUser) && App.get (password).equals (defaultPassword)))
                        return true;*/

                   /* boolean exists = serverService.checkOrGetCurrentUser ();
                    return exists;*/

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return true;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                }

                ;

                @Override
                protected void onPostExecute(Boolean result) {
                    super.onPostExecute(result);
                    loading.dismiss();
                    if (result) {
                        //serverService.setCurrentUser (App.get (username));

                        // Save username and password in preferences
                        App.setUsername(App.get(username));
                        App.setPassword(App.get(password));
                        App.setAutoLogin("Enabled");
                        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                        App.setLastLogin(date);
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.USERNAME, App.getUsername());
                        editor.putString(Preferences.PASSWORD, App.getPassword());
                        editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                        editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        /*App.setUsername ("");
                        App.setPassword ("");
                        Toast toast = Toast.makeText (LoginActivity.this, getResources ().getString (R.string.authentication_error), App.getDelay ());
                        toast.setGravity (Gravity.CENTER, 0, 0);
                        toast.show ();*/
                    }
                }
            };
            authenticationTask.execute("");


        }

    }

    @Override
    public void onClick(View v) {

        if (v == loginButton) {
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



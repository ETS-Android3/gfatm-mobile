package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.shared.FormsObject;
import com.ihsinformatics.gfatmmobile.util.LocationService;
import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    private static final int SELECT_PATIENT_ACTIVITY = 0;
    private static final int SAVED_FORM_ACTIVITY = 1;
    protected static ProgressDialog loading;
    Context context = this;
    LinearLayout buttonLayout;
    LinearLayout programLayout;
    LinearLayout headerLayout;
    Button formButton;
    Button reportButton;
    Button searchButton;
    RadioGroup radioGroup;
    FormFragment fragmentForm = new FormFragment();
    ReportFragment fragmentReport = new ReportFragment();
    SearchFragment fragmentSearch = new SearchFragment();
    ImageView change;
    ImageView update;

    TextView patientName;
    TextView patientDob;
    TextView patientId;
    TextView id;

    FragmentManager fm = getFragmentManager();
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

        loading = new ProgressDialog(this);
        serverService = new ServerService(getApplicationContext());

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragmentForm, "FORM");
        fragmentTransaction.add(R.id.fragment_place, fragmentReport, "REPORT");
        fragmentTransaction.add(R.id.fragment_place, fragmentSearch, "SEARCH");

        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(fragmentSearch);

        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.menuUsername);
        nav_user.setText(App.getUserFullName());
        TextView nav_userRole = (TextView) hView.findViewById(R.id.menuUserRoles);
        nav_userRole.setText(App.getRoles());

        change = (ImageView) findViewById(R.id.change);
        int color = App.getColor(this, R.attr.colorBackground);
        DrawableCompat.setTint(change.getDrawable(), color);
        change.setOnTouchListener(this);

        update = (ImageView) findViewById(R.id.update);
        DrawableCompat.setTint(update.getDrawable(), color);
        update.setOnTouchListener(this);

        String title = toolbar.getTitle() + " (" + App.getVersion() + ")";
        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!title.contains(" ----- Offline Mode"))
                title = title + " ----- Offline Mode";
            update.setVisibility(View.GONE);
        } else {
            if (!title.contains(" ----- Offline Mode"))
                title.replace(" ----- Offline Mode", "");

            if (App.getPatient() == null)
                update.setVisibility(View.GONE);
            else
                update.setVisibility(View.VISIBLE);

        }
        getSupportActionBar().setTitle(title);
        String subtitle = getResources().getString(R.string.program) + " " + App.getProgram() + "  |  " + "Location:" + " " + App.getLocation();
        getSupportActionBar().setSubtitle(subtitle);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        programLayout = (LinearLayout) findViewById(R.id.programLayout);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                App.setProgram(rb.getText().toString());

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.PROGRAM, App.getProgram());
                editor.apply();

                buttonLayout.setVisibility(View.VISIBLE);
                programLayout.setVisibility(View.GONE);
                headerLayout.setVisibility(View.VISIBLE);

                String subtitle = getResources().getString(R.string.program) + " " + App.getProgram();
                getSupportActionBar().setSubtitle(subtitle);
                fragmentForm.fillMainContent();
                fragmentReport.fillReportFragment();
                showFormFragment();

                if (App.getLocation().equals(""))
                    openLocationSelectionDialog();

            }
        });

        headerLayout = (LinearLayout) findViewById(R.id.header);
        formButton = (Button) findViewById(R.id.formButton);
        reportButton = (Button) findViewById(R.id.reportButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        patientName = (TextView) findViewById(R.id.patientName);
        patientDob = (TextView) findViewById(R.id.patientDob);
        patientId = (TextView) findViewById(R.id.patientId);
        id = (TextView) findViewById(R.id.id);

        if (App.getPatient() != null) {
            String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
            String lname = App.getPatient().getPerson().getFamilyName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getFamilyName().substring(1);

            patientName.setText(fname + " " + lname);
            String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
            if (!dob.equals("")) {
                Date date = App.stringToDate(dob, "yyyy-MM-dd");
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
            } else patientDob.setText(dob);
            if (!App.getPatient().getPatientId().equals(""))
                id.setVisibility(View.VISIBLE);
            patientId.setText(App.getPatient().getPatientId());
        }

        if (!App.getProgram().equals("")) {
            showFormFragment();

            if (App.getLocation().equals(""))
                openLocationSelectionDialog();
        }
        else
            showProgramSelection();

        //serverService.submitOfflineForms();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (!getSupportActionBar().getSubtitle().toString().contains(App.getProgram())) {
            String subtitle = getResources().getString(R.string.program) + " " + App.getProgram() + "  |  " + getResources().getString(R.string.location) + " " + App.getLocation();
            getSupportActionBar().setSubtitle(subtitle);

            fragmentForm.fillMainContent();
            fragmentReport.fillReportFragment();
            showFormFragment();
        }

        if (!getSupportActionBar().getSubtitle().toString().contains(App.getLocation())) {
            String subtitle = getResources().getString(R.string.program) + " " + App.getProgram() + "  |  " + getResources().getString(R.string.location) + " " + App.getLocation();
            getSupportActionBar().setSubtitle(subtitle);
        }

        String title = getSupportActionBar().getTitle().toString();
        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            if (!title.contains(" ----- Offline Mode"))
                title = title + " ----- Offline Mode";
            update.setVisibility(View.GONE);
        } else {
            if (!title.contains(" ----- Offline Mode"))
                title.replace(" ----- Offline Mode", "");
            if (App.getPatient() == null)
                update.setVisibility(View.GONE);
            else
                update.setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setTitle(title);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = preferences.getString(Preferences.LANGUAGE, "");
        if (!App.getLanguage().equals(lang)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preferences.LANGUAGE, App.getLanguage());
            editor.apply();
            restartActivity();
        }

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment form = fm.findFragmentByTag("FORM");
        if (form != null && form.isVisible() && fragmentForm.isFormVisible()) {

            int color = App.getColor(MainActivity.this, R.attr.colorAccent);

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.warning_before_close_form));
            Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
            backIcon.setAutoMirrored(true);
            DrawableCompat.setTint(backIcon, color);
            alertDialog.setIcon(backIcon);
            alertDialog.setTitle(getResources().getString(R.string.back_to_form_menu));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            fragmentForm.setMainContentVisible(true);
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


            return;
        }

        int color = App.getColor(MainActivity.this, R.attr.colorAccent);

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.setMessage(getString(R.string.warning_before_close));
        Drawable backIcon = getResources().getDrawable(R.drawable.ic_back);
        backIcon.setAutoMirrored(true);
        DrawableCompat.setTint(backIcon, color);
        alertDialog.setIcon(backIcon);
        alertDialog.setTitle(getResources().getString(R.string.title_close));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        close();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));

    }

    public void close() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.offline_forms) {
            Intent savedFormActivityIntent = new Intent(this, OfflineFormActivity.class);
            startActivityForResult(savedFormActivityIntent, SAVED_FORM_ACTIVITY);
        } else if (id == R.id.update_database) {
            Intent fetchMetadataActivityIntent = new Intent(this, UpdateDatabaseActivity.class);
            startActivity(fetchMetadataActivityIntent);
        } else if (id == R.id.location_setup) {
            Intent locationSetupActivityIntent = new Intent(this, LocationSetupActivity.class);
            startActivity(locationSetupActivityIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_program) {

            Intent programActivityIntent = new Intent(this, ProgramActivity.class);
            startActivity(programActivityIntent);

        } else if (id == R.id.nav_theme) {

            Intent themeActivityIntent = new Intent(this, ThemeActivity.class);
            startActivity(themeActivityIntent);

        } else if (id == R.id.nav_language) {

            Intent languageActivityIntent = new Intent(this, LanguageActivity.class);
            startActivity(languageActivityIntent);

        } else if (id == R.id.nav_operation_mode) {

            Intent operationModeActivityIntent = new Intent(this, OperationModeActivity.class);
            startActivity(operationModeActivityIntent);

        } else if (id == R.id.nav_defaults) {

            Intent defaultActivityIntent = new Intent(this, DefaultActivity.class);
            startActivity(defaultActivityIntent);

        } else if (id == R.id.nav_server) {

            Intent serverActivityIntent = new Intent(this, ServerActivity.class);
            startActivity(serverActivityIntent);

        } else if (id == R.id.nav_ssl_encryption) {

            Intent sslActivityIntent = new Intent(this, SSLEncryptionActivity.class);
            startActivity(sslActivityIntent);

        } else if (id == R.id.nav_logout) {

            int color = App.getColor(MainActivity.this, R.attr.colorAccent);

            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
            alertDialog.setMessage(getString(R.string.warning_before_logout));
            Drawable logoutIcon = getResources().getDrawable(R.drawable.ic_logout);
            DrawableCompat.setTint(logoutIcon, color);
            alertDialog.setIcon(logoutIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_logout));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            App.setAutoLogin("Disabled");
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Preferences.LAST_LOGIN, App.getLastLogin());
                            editor.putString(Preferences.AUTO_LOGIN, App.getAutoLogin());
                            editor.apply();
                            startLoginIntent();

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void selectFrag(View view) {

        if (view == formButton)
            showFormFragment();
        else if (view == reportButton)
            showReportFragment();
        else if (view == searchButton)
            showSearchFragment();

    }

    private void showFormFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(color);
        formButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], color);

        reportButton.setTextColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        searchButton.setTextColor(getResources().getColor(R.color.dark_grey));
        searchButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        //FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.show(fragmentForm);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(fragmentSearch);
        fragmentTransaction.commit();
    }

    private void showReportFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(getResources().getColor(R.color.dark_grey));
        formButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        reportButton.setTextColor(color);
        reportButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], color);

        searchButton.setTextColor(getResources().getColor(R.color.dark_grey));
        searchButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        //FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.show(fragmentReport);
        fragmentTransaction.hide(fragmentSearch);
        fragmentTransaction.commit();
    }

    private void showSearchFragment() {

        int color = App.getColor(this, R.attr.colorPrimaryDark);

        formButton.setTextColor(getResources().getColor(R.color.dark_grey));
        formButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(formButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        reportButton.setTextColor(getResources().getColor(R.color.dark_grey));
        reportButton.setBackgroundResource(R.drawable.border_button);
        DrawableCompat.setTint(reportButton.getCompoundDrawables()[0], getResources().getColor(R.color.dark_grey));

        searchButton.setTextColor(color);
        searchButton.setBackgroundResource(R.drawable.selected_border_button);
        DrawableCompat.setTint(searchButton.getCompoundDrawables()[0], color);

        //FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.show(fragmentSearch);
        fragmentTransaction.commit();
    }

    private void showProgramSelection() {

        programLayout.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.GONE);
        headerLayout.setVisibility(View.GONE);

    }

    /**
     * Restarts the current activity...
     */
    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void startLoginIntent() {
        Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(loginActivityIntent);
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                /*Intent gpsCoordinate = new Intent(this, LocationService.class);
                startActivity(gpsCoordinate);*/

                ImageView view = (ImageView) v;
                view.getDrawable().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
                view.invalidate();

                if (view == change) {

                    Intent selectPatientActivityIntent = new Intent(this, SelectPatientActivity.class);
                    startActivityForResult(selectPatientActivityIntent, SELECT_PATIENT_ACTIVITY);

                    break;
                } else if (view == update) {

                    updatePatientDetails();

                    break;
                }
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

    private void updatePatientDetails() {
        AsyncTask<String, String, String> updateTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.updating_patient));
                        loading.show();
                    }
                });

                String result = serverService.updatePatientDetails(App.getPatient().getPatientId());
                return result;
                //return "SUCCESS";
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
                    //resetViews();

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(getResources().getString(R.string.patient_updated));
                    Drawable submitIcon = getResources().getDrawable(R.drawable.ic_submit);
                    alertDialog.setIcon(submitIcon);
                    int color = App.getColor(context, R.attr.colorAccent);
                    DrawableCompat.setTint(submitIcon, color);
                    alertDialog.setTitle(getResources().getString(R.string.title_completed));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    fragmentReport.fillReportFragment();
                } else if (result.equals("CONNECTION_ERROR")) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
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
                } else {
                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    String message = getResources().getString(R.string.insert_error) + "\n\n (" + result + ")";
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
                }


            }
        };
        updateTask.execute("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SELECT PATIENT with an OK result
        if (requestCode == SELECT_PATIENT_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra("key");
                if (returnString != null && returnString.equals("SEARCH")) {
                    showSearchFragment();
                } else if (returnString != null && returnString.equals("SELECT")) {

                    if (App.getPatient() != null) {
                        String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                        String lname = App.getPatient().getPerson().getFamilyName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getFamilyName().substring(1);

                        patientName.setText(fname + " " + lname);
                        String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                        if (!dob.equals("")) {
                            Date date = App.stringToDate(dob, "yyyy-MM-dd");
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                            patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                        } else patientDob.setText(dob);
                        if (!App.getPatient().getPatientId().equals(""))
                            id.setVisibility(View.VISIBLE);
                        patientId.setText(App.getPatient().getPatientId());

                        fragmentReport.fillReportFragment();
                        fragmentForm.fillMainContent();

                    }
                } else if (returnString != null && returnString.equals("CREATE")) {

                    if (App.getPatient() != null) {
                        String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                        String lname = App.getPatient().getPerson().getFamilyName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getFamilyName().substring(1);

                        patientName.setText(fname + " " + lname);
                        String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                        if (!dob.equals("")) {
                            Date date = App.stringToDate(dob, "yyyy-MM-dd");
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                            patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                        } else patientDob.setText(dob);
                        if (!App.getPatient().getPatientId().equals(""))
                            id.setVisibility(View.VISIBLE);
                        patientId.setText(App.getPatient().getPatientId());

                        Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.patient_created_successfully), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        fragmentReport.fillReportFragment();
                        fragmentForm.fillMainContent();
                    }
                }


            }
        } else if (requestCode == SAVED_FORM_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                String returnString = data.getStringExtra("form_id");

                Object[][] form = serverService.getSavedForms(Integer.parseInt(returnString));

                String toastMessage = "";

                if (!App.getPatientId().equals(String.valueOf(form[0][3]))) {
                    App.setPatientId(String.valueOf(form[0][3]));
                    App.setPatient(serverService.getPatientBySystemIdFromLocalDB(App.getPatientId()));

                    toastMessage = getResources().getString(R.string.selected_patient_changed) + " " + App.getPatient().getPerson().getGivenName() + " " + App.getPatient().getPerson().getFamilyName() + " (" + App.getPatient().getPatientId() + ")";

                }
                if (!App.getLocation().equals(String.valueOf(form[0][7]))) {
                    App.setLocation(String.valueOf(form[0][7]));

                    if (!toastMessage.equals(""))
                        toastMessage = toastMessage + "\n";

                    toastMessage = toastMessage + getResources().getString(R.string.selected_location_changed) + " " + App.getLocation();

                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.PATIENT_ID, App.getPatientId());
                editor.putString(Preferences.LOCATION, App.getLocation());
                editor.apply();

                String subtitle = getResources().getString(R.string.program) + " " + App.getProgram() + "  |  " + getResources().getString(R.string.location) + " " + App.getLocation();
                getSupportActionBar().setSubtitle(subtitle);

                String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                String lname = App.getPatient().getPerson().getFamilyName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getFamilyName().substring(1);

                patientName.setText(fname + " " + lname);
                String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                if (!dob.equals("")) {
                    Date date = App.stringToDate(dob, "yyyy-MM-dd");
                    DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                    patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                } else patientDob.setText(dob);
                if (!App.getPatient().getPatientId().equals(""))
                    id.setVisibility(View.VISIBLE);
                patientId.setText(App.getPatient().getPatientId());

                fragmentReport.fillReportFragment();

                Boolean openFlag = data.getBooleanExtra("open", false);
                if (returnString != null) {
                    showFormFragment();
                    byte[] bytes = data.getByteArrayExtra("form_object");

                    ByteArrayInputStream bais = null;
                    ObjectInputStream ins = null;
                    try {

                        bais = new ByteArrayInputStream(bytes);
                        ins = new ObjectInputStream(bais);
                        FormsObject f = (FormsObject) ins.readObject();

                        fragmentForm.openForm(f, returnString, openFlag);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                if (!toastMessage.equals(""))
                    Toast.makeText(getApplicationContext(), toastMessage,
                            Toast.LENGTH_LONG).show();

            }
        }
    }

    public void openLocationSelectionDialog() {
        Intent languageActivityIntent = new Intent(this, LocationSelectionDialog.class);
        startActivity(languageActivityIntent);
    }

}

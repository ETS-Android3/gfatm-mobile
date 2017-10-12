package com.ihsinformatics.gfatmmobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.ihsinformatics.gfatmmobile.util.OfflineFormSyncService;
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
    public static LinearLayout headerLayout;
    public static  Button formButton;
    Button reportButton;
    Button searchButton;
    RadioGroup radioGroup;
    public static FormFragment fragmentForm = new FormFragment();
    public static ReportFragment fragmentReport = new ReportFragment();
    public static SummaryFragment fragmentSearch = new SummaryFragment();
    ImageView change;
    public static ImageView update;
    public static ImageView edit;

    public static TextView patientName;
    public static TextView patientDob;
    public static TextView patientId;
    public static TextView id;

    static boolean active = false;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if(message.equals("completed")) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.ic_checked);
                mBuilder.setContentTitle("Aao TB Mitao - Forms Upload completed");
                mBuilder.setContentText("Offline form upload completed successfully.");
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);

                Intent notificationIntent = new Intent();
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("buzz", "buzz");
                notificationIntent.putExtras(bundle);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());
            } else if (message.equals("completed_with_error")){

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                mBuilder.setSmallIcon(R.drawable.error);
                mBuilder.setContentTitle("Aao TB Mitao - Forms Upload completed");
                mBuilder.setContentText("Offline form upload with some error. Go to offline forms to see pending forms.");
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);

                Intent notificationIntent = new Intent(context, OfflineFormActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putString("buzz", "buzz");
                notificationIntent.putExtras(bundle);
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);
                mBuilder.setDefaults(Notification.DEFAULT_ALL);

                // Add as notification
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(0, mBuilder.build());

            }
        }
    };

//    TextView nav_default;

    public static ActionBar actionBar;

    FragmentManager fm = getFragmentManager();
    private ServerService serverService;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(active){
            return;
        }

        loading = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
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
        actionBar = getSupportActionBar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        TextView nav_appName = (TextView) hView.findViewById(R.id.appName);
        nav_appName.setText(getResources().getString(R.string.app_name) + " (" + App.getVersion() + ")");
        //nav_default = (TextView) hView.findViewById(R.id.selectedDefault);
        //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
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

        edit = (ImageView) findViewById(R.id.edit);
        DrawableCompat.setTint(edit.getDrawable(), color);
        edit.setOnTouchListener(this);
        //edit.setVisibility(View.GONE);

        getSupportActionBar().setTitle(Html.fromHtml("<small>" + App.getProgram() + "  |  " + App.getLocation() + "</small>"));
        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            getSupportActionBar().setSubtitle("Offline Mode");
            update.setVisibility(View.GONE);
        } else {
            getSupportActionBar().setSubtitle(null);

            if (App.getPatient() == null) {
                update.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
            else {
                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }

        }

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

                getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());
                //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
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
            String lname = App.getPatient().getPerson().getFamilyName();
            if(!lname.equals(""))
                lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

            patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
            String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
            if (!dob.equals("")) {
                Date date = App.stringToDate(dob, "yyyy-MM-dd");
                DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                if(App.getPatient().getPerson().getAge() == 0){
                    Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                    int age = App.getDiffMonths(birthDate, new Date());
                    if(age == 0 ){
                        long ageInLong = App.getDiffDays(birthDate, new Date());
                        patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                    }
                    else patientDob.setText(age + " months (" + df.format(date) + ")");
                }
                else patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
            } else patientDob.setText(dob);
            if (!App.getPatient().getPatientId().equals(""))
                id.setVisibility(View.VISIBLE);
            patientId.setText(App.getPatient().getPatientId());
            update.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        }

        if (!App.getProgram().equals("")) {
            showFormFragment();

            if (App.getLocation().equals(""))
                openLocationSelectionDialog();
        }
        else
            showProgramSelection();

        /*if(App.getMode().equalsIgnoreCase("ONLINE")) {
            int count = serverService.getPendingSavedFormsCount(App.getUsername(), App.getProgram());
            if (count > 0) {

                final int color1 = App.getColor(this, R.attr.colorAccent);

                final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                alertDialog.setMessage(count + " " + getString(R.string.offline_form_alert));
                Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                DrawableCompat.setTint(clearIcon, color1);
                alertDialog.setIcon(clearIcon);
                alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                        new IntentFilter("background-offline-sync"));
                                startSync();
                                dialog.dismiss();
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
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        String d = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String v = App.getLastLogin();
        if (!(App.getAutoLogin().equals("Enabled") && App.getLastLogin().equals(d))) {
            Intent intent = new Intent(context, LoginActivity.class);
            ServerService service = new ServerService(context);
            service.resetScreeningCounts();
            startActivity(intent);
            finish();
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }
        else {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showLocationAlert();
            } else {

                if (!LocationService.isInstanceCreated()) {
                    Intent intent = new Intent(MainActivity.this, LocationService.class);
                    startService(intent);
                }
            }
        }


        if (!getSupportActionBar().getTitle().toString().contains(App.getProgram())) {
            //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
            getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());
            fragmentForm.fillMainContent();
            fragmentReport.fillReportFragment();
            fragmentSearch.updateSummaryFragment();
            showFormFragment();
        }

        if (!getSupportActionBar().getTitle().toString().contains(App.getLocation())) {
            //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
            getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());
        }

        if (App.getMode().equalsIgnoreCase("OFFLINE")) {
            getSupportActionBar().setSubtitle("Offline Mode");
            update.setVisibility(View.GONE);
        } else {

            Boolean flag = getSupportActionBar().getSubtitle() == null ? false : true;

            getSupportActionBar().setSubtitle(null);
            if (App.getPatient() == null) {
                update.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                patientName.setText("");
                patientDob.setText("");
                patientId.setText("");
                id.setText("");

                getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());
                fragmentForm.fillMainContent();
                fragmentReport.fillReportFragment();
                fragmentSearch.updateSummaryFragment();
                showFormFragment();

            }
            else {


                String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                String lname = App.getPatient().getPerson().getFamilyName();
                if(!lname.equals(""))
                    lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                if(!App.get(patientName).equals(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")")) {

                    patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                    String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                    if (!dob.equals("")) {
                        Date date = App.stringToDate(dob, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                        if (App.getPatient().getPerson().getAge() == 0) {
                            Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                            int age = App.getDiffMonths(birthDate, new Date());
                            if (age == 0) {
                                long ageInLong = App.getDiffDays(birthDate, new Date());
                                patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                            } else patientDob.setText(age + " months (" + df.format(date) + ")");
                        } else
                            patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                    } else patientDob.setText(dob);
                    if (!App.getPatient().getPatientId().equals(""))
                        id.setVisibility(View.VISIBLE);
                    patientId.setText(App.getPatient().getPatientId());

                    getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());
                    fragmentForm.fillMainContent();
                    fragmentReport.fillReportFragment();
                    fragmentSearch.updateSummaryFragment();
                    showFormFragment();

                }

                update.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }


            /*if(flag) {
                int count = serverService.getPendingSavedFormsCount(App.getUsername(), App.getProgram());
                if (count > 0) {

                    final int color1 = App.getColor(this, R.attr.colorAccent);

                    final AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.dialog).create();
                    alertDialog.setMessage(count + " " + getString(R.string.offline_form_alert));
                    Drawable clearIcon = getResources().getDrawable(R.drawable.ic_submit);
                    DrawableCompat.setTint(clearIcon, color1);
                    alertDialog.setIcon(clearIcon);
                    alertDialog.setTitle(getResources().getString(R.string.title_offline_form_found));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                                            new IntentFilter("background-offline-sync"));
                                    startSync();
                                    dialog.dismiss();
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
            }*/

        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = preferences.getString(Preferences.LANGUAGE, "");
        if (!App.getLanguage().equals(lang)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preferences.LANGUAGE, App.getLanguage());
            editor.apply();
            restartActivity();
        }

    }

    public static void backToMainMenu(){
        fragmentForm.setMainContentVisible(true);
        headerLayout.setVisibility(View.VISIBLE);
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
                            headerLayout.setVisibility(View.VISIBLE);
                            getSupportActionBar().show();
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

        form = fm.findFragmentByTag("SEARCH");
        if (form != null && form.isVisible() && fragmentSearch.isViewVisible()) {
            fragmentSearch.setMainContentVisible(true);
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
        } else if (id == R.id.feedback) {
            Intent feedbackActivityIntent = new Intent(this, FeedbackActivity.class);
            startActivity(feedbackActivityIntent);
        } else if (id == R.id.location_setup) {
            Intent locationSetupActivityIntent = new Intent(this, LocationSetupActivity.class);
            startActivity(locationSetupActivityIntent);
        } else if (id == R.id.search) {
            Intent selectPatientActivityIntent = new Intent(this, SearchActivity.class);
            startActivityForResult(selectPatientActivityIntent, SELECT_PATIENT_ACTIVITY);
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
                } else if (view == edit) {

                    Intent selectPatientActivityIntent = new Intent(this, EditPatientActivity.class);
                    startActivityForResult(selectPatientActivityIntent, SELECT_PATIENT_ACTIVITY);

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

                String result = serverService.updatePatientDetails(App.getPatient().getPatientId(), true);
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

                    String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                    String lname = App.getPatient().getPerson().getFamilyName();
                    if(!lname.equals(""))
                        lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                    patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                    String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                    if (!dob.equals("")) {
                        Date date = App.stringToDate(dob, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                        if(App.getPatient().getPerson().getAge() == 0){
                            Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                            int age = App.getDiffMonths(birthDate, new Date());
                            if(age == 0 ){
                                long ageInLong = App.getDiffDays(birthDate, new Date());
                                patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                            }
                            else patientDob.setText(age + " months (" + df.format(date) + ")");
                        }
                        else patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                    } else patientDob.setText(dob);
                    if (!App.getPatient().getPatientId().equals(""))
                        id.setVisibility(View.VISIBLE);
                    patientId.setText(App.getPatient().getPatientId());

                    fragmentReport.fillReportFragment();
                    fragmentForm.fillMainContent();
                    fragmentSearch.updateSummaryFragment();

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
                        String lname = App.getPatient().getPerson().getFamilyName();
                        if(!lname.equals(""))
                            lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                        patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                        String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                        if (!dob.equals("")) {
                            Date date = App.stringToDate(dob, "yyyy-MM-dd");
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                            if(App.getPatient().getPerson().getAge() == 0){
                                Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                                int age = App.getDiffMonths(birthDate, new Date());
                                if(age == 0 ){
                                    long ageInLong = App.getDiffDays(birthDate, new Date());
                                    patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                                }
                                else patientDob.setText(age + " months (" + df.format(date) + ")");
                            }
                            else patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                        } else patientDob.setText(dob);
                        if (!App.getPatient().getPatientId().equals(""))
                            id.setVisibility(View.VISIBLE);
                        patientId.setText(App.getPatient().getPatientId());

                        fragmentReport.fillReportFragment();
                        fragmentForm.fillMainContent();
                        fragmentSearch.updateSummaryFragment();

                    }
                } else if (returnString != null && returnString.equals("CREATE")) {

                    if (App.getPatient() != null) {
                        String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                        String lname = App.getPatient().getPerson().getFamilyName();
                        if(!lname.equals(""))
                            lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                        patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                        String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                        if (!dob.equals("")) {
                            Date date = App.stringToDate(dob, "yyyy-MM-dd");
                            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                            if(App.getPatient().getPerson().getAge() == 0){
                                Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                                int age = App.getDiffMonths(birthDate, new Date());
                                if(age == 0 ){
                                    long ageInLong = App.getDiffDays(birthDate, new Date());
                                    patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                                }
                                else patientDob.setText(age + " months (" + df.format(date) + ")");
                            }
                            else patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                        } else patientDob.setText(dob);
                        if (!App.getPatient().getPatientId().equals(""))
                            id.setVisibility(View.VISIBLE);
                        patientId.setText(App.getPatient().getPatientId());

                        /*Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.patient_created_successfully), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();*/

                        fragmentReport.fillReportFragment();
                        fragmentForm.fillMainContent();
                        fragmentSearch.updateSummaryFragment();
                    }
                }


            }
        } else if (requestCode == SAVED_FORM_ACTIVITY) {
            if (resultCode == RESULT_OK) {

                String returnString = data.getStringExtra("form_id");

                Object[][] form = serverService.getSavedForms(Integer.parseInt(returnString));

                String toastMessage = "";

                String pid = String.valueOf(form[0][3]);
                if(!(pid == null || pid.equals("null"))) {

                    if (App.getPatientId() == null || !App.getPatientId().equals(String.valueOf(form[0][3]))) {

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
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.PATIENT_ID, App.getPatientId());
                editor.putString(Preferences.LOCATION, App.getLocation());
                editor.apply();

                //nav_default.setText(getResources().getString(R.string.program) + App.getProgram() + "  |  " + getResources().getString(R.string.location) + App.getLocation());
                getSupportActionBar().setTitle(App.getProgram() + "  |  " + App.getLocation());

                if(!(pid == null || pid.equals("null"))) {
                String fname = App.getPatient().getPerson().getGivenName().substring(0, 1).toUpperCase() + App.getPatient().getPerson().getGivenName().substring(1);
                    String lname = App.getPatient().getPerson().getFamilyName();
                    if(!lname.equals(""))
                        lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                    patientName.setText(fname + " " + lname + " (" + App.getPatient().getPerson().getGender() + ")");
                    String dob = App.getPatient().getPerson().getBirthdate().substring(0, 10);
                    if (!dob.equals("")) {
                        Date date = App.stringToDate(dob, "yyyy-MM-dd");
                        DateFormat df = new SimpleDateFormat("MMM dd, yyyy");
                        if(App.getPatient().getPerson().getAge() == 0){
                            Date birthDate = App.stringToDate(App.getPatient().getPerson().getBirthdate(), "yyyy-MM-dd");
                            int age = App.getDiffMonths(birthDate, new Date());
                            if(age == 0 ){
                                long ageInLong = App.getDiffDays(birthDate, new Date());
                                patientDob.setText(ageInLong + " days (" + df.format(date) + ")");
                            }
                            else patientDob.setText(age + " months (" + df.format(date) + ")");
                        }
                        else patientDob.setText(App.getPatient().getPerson().getAge() + " years (" + df.format(date) + ")");
                    } else patientDob.setText(dob);
                    if (!App.getPatient().getPatientId().equals(""))
                        id.setVisibility(View.VISIBLE);
                    patientId.setText(App.getPatient().getPatientId());

                    headerLayout.setVisibility(View.VISIBLE);
                }
                else
                    headerLayout.setVisibility(View.GONE);

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

    public void showLocationAlert() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.setMessage(getResources().getString(R.string.gps_not_enabled));
        Drawable clearIcon = getResources().getDrawable(R.drawable.ic_gps);
        int color = App.getColor(context, R.attr.colorAccent);
        DrawableCompat.setTint(clearIcon, color);
        alertDialog.setIcon(clearIcon);
        alertDialog.setTitle(getResources().getString(R.string.gps_settings));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startSync(){
        startService(new Intent(this, OfflineFormSyncService.class));
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

}

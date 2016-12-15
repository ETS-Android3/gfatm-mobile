package com.ihsinformatics.gfatmmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context = this;

    LinearLayout buttonLayout;
    LinearLayout programLayout;

    Button formButton;
    Button reportButton;
    Button searchButton;

    RadioGroup radioGroup;

    FormFragment fragmentForm = new FormFragment();
    ReportFragment fragmentReport = new ReportFragment();
    BlankFragment blankFragment = new BlankFragment();

    FragmentManager fm = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragment_place, fragmentForm, "FORM");
        fragmentTransaction.add(R.id.fragment_place, fragmentReport, "REPORT");
        fragmentTransaction.add(R.id.fragment_place, blankFragment, "SEARCH");

        fragmentTransaction.hide(fragmentForm);
        fragmentTransaction.hide(fragmentReport);
        fragmentTransaction.hide(blankFragment);

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
        nav_user.setText(App.getUsername());

        String title = toolbar.getTitle() + " (" + App.getVersion() + ")";
        getSupportActionBar().setTitle(title);
        String subtitle = getResources().getString(R.string.program) + " " + App.getProgram();
        getSupportActionBar().setSubtitle(subtitle);

        buttonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
        programLayout = (LinearLayout) findViewById(R.id.programLayout);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                App.setProgram(rb.getText().toString());

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Preferences.PROGRAM, App.getProgram());
                editor.apply();

                buttonLayout.setVisibility(View.VISIBLE);
                programLayout.setVisibility(View.GONE);

                String subtitle = getResources().getString(R.string.program) + " " + App.getProgram();
                getSupportActionBar().setSubtitle(subtitle);
                fragmentForm.fillMainContent();
                showFormFragment();

            }
        });

        formButton = (Button) findViewById(R.id.formButton);
        reportButton = (Button) findViewById(R.id.reportButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        if (!App.getProgram().equals(""))
            showFormFragment();
        else
            showProgramSelection();

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        if (!getSupportActionBar().getSubtitle().toString().contains(App.getProgram())) {
            String subtitle = getResources().getString(R.string.program) + " " + App.getProgram();
            getSupportActionBar().setSubtitle(subtitle);

            fragmentForm.fillMainContent();
            showFormFragment();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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

            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

        if (id == R.id.saved_forms) {
            Intent savedFormIntent = new Intent(this, SavedFormActivity.class);
            startActivity(savedFormIntent);
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

            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage(getString(R.string.warning_before_logout));
            Drawable logoutIcon = getResources().getDrawable(R.drawable.ic_logout);
            DrawableCompat.setTint(logoutIcon, color);
            alertDialog.setIcon(logoutIcon);
            alertDialog.setTitle(getResources().getString(R.string.title_logout));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            App.setAutoLogin("Disabled");
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
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
        fragmentTransaction.hide(blankFragment);
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
        fragmentTransaction.hide(blankFragment);
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
        fragmentTransaction.show(blankFragment);
        fragmentTransaction.commit();
    }

    private void showProgramSelection() {

        programLayout.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.GONE);

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

}

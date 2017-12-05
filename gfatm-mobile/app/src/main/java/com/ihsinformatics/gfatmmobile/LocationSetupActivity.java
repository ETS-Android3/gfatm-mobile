package com.ihsinformatics.gfatmmobile;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A login screen that offers login via email/password.
 */
public class LocationSetupActivity extends AppCompatActivity implements View.OnTouchListener {

    protected static ProgressDialog loading;
    protected TextView programName;
    protected LinearLayout contentLinearLayout;
    protected TextView syncText;
    protected TextView lastUpdate;
    protected TextView lastUpdateTextView;
    ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
    private ServerService serverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        loading = new ProgressDialog(LocationSetupActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        serverService = new ServerService(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_setup);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        programName = (TextView) findViewById(R.id.program);
        contentLinearLayout = (LinearLayout) findViewById(R.id.content);
        syncText = (TextView) findViewById(R.id.sync);
        lastUpdate = (TextView) findViewById(R.id.location_last_update);
        lastUpdateTextView = (TextView) findViewById(R.id.last_update);

        syncText.setOnTouchListener(this);

        fillList();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    public void fillList() {

        contentLinearLayout.removeAllViews();

        String lu = App.getLocationLastUpdate();
        lastUpdate.setText(lu);

        if (lastUpdate.getText().equals("")) {
            lastUpdateTextView.setVisibility(View.GONE);
            lastUpdate.setVisibility(View.GONE);
        } else {
            lastUpdateTextView.setVisibility(View.VISIBLE);
            lastUpdate.setVisibility(View.VISIBLE);
        }

        ServerService serverService = new ServerService(getApplicationContext());
        String columnName = "";
        /*if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";*/

        final Object[][] locations = serverService.getAllLocations(columnName);

        programName.setText(App.getProgram());

        if (locations == null || locations.length == 0) {
            final TextView text = new TextView(this);
            text.setText(getResources().getString(R.string.no_location));
            text.setTextSize(getResources().getDimension(R.dimen.small));
            contentLinearLayout.addView(text);
        } else {

            final AutoCompleteTextView locationAutocomplete = new AutoCompleteTextView(getApplicationContext());
            locationAutocomplete.setHint(getResources().getString(R.string.search_location));
            locationAutocomplete.setHintTextColor(getResources().getColor(R.color.light_grey));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 20, 0, 20);
            locationAutocomplete.setLayoutParams(layoutParams);

            locationAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String selection = (String) parent.getItemAtPosition(position);
                    App.setLocation(selection);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LocationSetupActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Preferences.LOCATION, App.getLocation());
                    editor.apply();

                    for (RadioButton rb : radioButtons) {

                        if (rb.getTag().toString().equalsIgnoreCase(selection))
                            rb.setChecked(true);
                        else
                            rb.setChecked(false);

                    }


                }
            });

            contentLinearLayout.addView(locationAutocomplete);

            String[] countries = new String[locations.length];

            for (int i = 0; i < locations.length; i++) {

                LinearLayout verticalLayout = new LinearLayout(getApplicationContext());
                verticalLayout.setOrientation(LinearLayout.VERTICAL);
                verticalLayout.setPadding(10, 20, 10, 20);

                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));

                final LinearLayout moreLayout = new LinearLayout(getApplicationContext());
                moreLayout.setOrientation(LinearLayout.VERTICAL);

                final int color = App.getColor(this, R.attr.colorPrimaryDark);
                final int color1 = App.getColor(this, R.attr.colorAccent);

                RadioButton selection = new RadioButton(this);
                linearLayout.addView(selection);
                selection.setTag(String.valueOf(locations[i][1]));
                radioButtons.add(selection);

                countries[i] = String.valueOf(locations[i][1]);

                if (locations[i][1].equals(App.getLocation()))
                    selection.setChecked(true);

                selection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for (RadioButton rb : radioButtons) {

                            if (rb != v) {
                                rb.setChecked(false);
                            }
                        }

                        App.setLocation(v.getTag().toString());
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LocationSetupActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.LOCATION, App.getLocation());
                        editor.apply();
                    }

                });

                final TextView text = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                if(String.valueOf(locations[i][16]).equals("") || String.valueOf(locations[i][16]).equals("null"))
                    text.setText(String.valueOf(locations[i][1]));
                else
                    text.setText(String.valueOf(locations[i][16]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                text.setPadding(10, 0, 0, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                moreLayout.setVisibility(View.GONE);

                if (!(locations[i][1] == null || locations[i][1].equals("") || locations[i][1].equals("null"))) {

                    LinearLayout ll5 = new LinearLayout(this);
                    ll5.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv8 = new TextView(this);
                    tv8.setText(getResources().getString(R.string.name) + " ");
                    tv8.setTextSize(getResources().getDimension(R.dimen.small));
                    tv8.setTextColor(color1);
                    ll5.addView(tv8);

                    TextView tv9 = new TextView(this);
                    tv9.setText(String.valueOf(locations[i][1]));
                    tv9.setTextSize(getResources().getDimension(R.dimen.small));
                    ll5.addView(tv9);

                    moreLayout.addView(ll5);

                    moreLayout.setVisibility(View.VISIBLE);

                }

                if (!(locations[i][10] == null || locations[i][10].equals("") || locations[i][10].equals("null"))) {
                    LinearLayout ll1 = new LinearLayout(this);
                    ll1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv = new TextView(this);
                    tv.setText(getResources().getString(R.string.primary_contact) + " ");
                    tv.setTextSize(getResources().getDimension(R.dimen.small));
                    tv.setTextColor(color1);
                    ll1.addView(tv);

                    TextView tv1 = new TextView(this);
                    tv1.setText(String.valueOf(locations[i][10]));
                    tv1.setTextSize(getResources().getDimension(R.dimen.small));
                    ll1.addView(tv1);

                    moreLayout.addView(ll1);
                    moreLayout.setVisibility(View.VISIBLE);

                }

                if (!(locations[i][11] == null || locations[i][11].equals("") || locations[i][11].equals("null"))) {

                    LinearLayout ll2 = new LinearLayout(this);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv2 = new TextView(this);
                    tv2.setText(getResources().getString(R.string.address_1) + " ");
                    tv2.setTextSize(getResources().getDimension(R.dimen.small));
                    tv2.setTextColor(color1);
                    ll2.addView(tv2);

                    TextView tv3 = new TextView(this);
                    tv3.setText(String.valueOf(locations[i][11]));
                    tv3.setTextSize(getResources().getDimension(R.dimen.small));
                    ll2.addView(tv3);

                    moreLayout.addView(ll2);
                    moreLayout.setVisibility(View.VISIBLE);

                }

                if (!(locations[i][12] == null || locations[i][12].equals("") || locations[i][12].equals("null"))) {

                    LinearLayout ll3 = new LinearLayout(this);
                    ll3.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv4 = new TextView(this);
                    tv4.setText(getResources().getString(R.string.address_2) + " ");
                    tv4.setTextSize(getResources().getDimension(R.dimen.small));
                    tv4.setTextColor(color1);
                    ll3.addView(tv4);

                    TextView tv5 = new TextView(this);
                    tv5.setText(String.valueOf(locations[i][12]));
                    tv5.setTextSize(getResources().getDimension(R.dimen.small));
                    ll3.addView(tv5);

                    moreLayout.addView(ll3);
                    moreLayout.setVisibility(View.VISIBLE);

                }

                if (!(locations[i][13] == null || locations[i][13].equals("") || locations[i][13].equals("null"))) {
                    LinearLayout ll4 = new LinearLayout(this);
                    ll4.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv6 = new TextView(this);
                    tv6.setText(getResources().getString(R.string.city_village) + " ");
                    tv6.setTextSize(getResources().getDimension(R.dimen.small));
                    tv6.setTextColor(color1);
                    ll4.addView(tv6);

                    TextView tv7 = new TextView(this);
                    tv7.setText(String.valueOf(locations[i][13]));
                    tv7.setTextSize(getResources().getDimension(R.dimen.small));
                    ll4.addView(tv7);

                    moreLayout.addView(ll4);
                    moreLayout.setVisibility(View.VISIBLE);
                }

                if (!(locations[i][14] == null || locations[i][14].equals("") || locations[i][14].equals("null"))) {
                    LinearLayout ll4 = new LinearLayout(this);
                    ll4.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv6 = new TextView(this);
                    tv6.setText(getResources().getString(R.string.province) + ": ");
                    tv6.setTextSize(getResources().getDimension(R.dimen.small));
                    tv6.setTextColor(color1);
                    ll4.addView(tv6);

                    TextView tv7 = new TextView(this);
                    tv7.setText(String.valueOf(locations[i][14]));
                    tv7.setTextSize(getResources().getDimension(R.dimen.small));
                    ll4.addView(tv7);

                    moreLayout.addView(ll4);
                    moreLayout.setVisibility(View.VISIBLE);
                }

                if (!(locations[i][15] == null || locations[i][15].equals("") || locations[i][15].equals("null"))) {
                    LinearLayout ll4 = new LinearLayout(this);
                    ll4.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv6 = new TextView(this);
                    tv6.setText(getResources().getString(R.string.district) + " ");
                    tv6.setTextSize(getResources().getDimension(R.dimen.small));
                    tv6.setTextColor(color1);
                    ll4.addView(tv6);

                    TextView tv7 = new TextView(this);
                    tv7.setText(String.valueOf(locations[i][15]));
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
                } else {
                    text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_less, 0);
                    DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                }

                moreLayout.setVisibility(View.GONE);
                contentLinearLayout.addView(verticalLayout);

            }

            final int color = App.getColor(this, R.attr.colorPrimaryDark);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countries);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            locationAutocomplete.setAdapter(spinnerArrayAdapter);
            locationAutocomplete.setTextColor(color);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                TextView view = (TextView) v;
                view.setTextColor(getResources().getColor(R.color.dark_grey));
                view.invalidate();

                syncLocation();

                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                TextView view = (TextView) v;
                //clear the overlay
                int color1 = App.getColor(this, R.attr.colorAccent);
                view.setTextColor(color1);
                view.invalidate();
                break;
            }
        }
        return true;
    }


    public void syncLocation() {
        AsyncTask<String, String, String> syncTask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setInverseBackgroundForced(true);
                        loading.setIndeterminate(true);
                        loading.setCancelable(false);
                        loading.setMessage(getResources().getString(R.string.fetching_locations));
                        loading.show();
                    }
                });

                String result = serverService.getLocations();
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

                    Calendar calendar = Calendar.getInstance();
                    App.setLocationLastUpdate(DateFormat.format("dd-MMM-yyyy HH:mm:ss", calendar).toString());
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Preferences.LOCATION_LAST_UPDATE, App.getLocationLastUpdate());
                    editor.apply();

                    fillList();
                }
            }
        };
        syncTask.execute("");
    }

    @Override
    public void onBackPressed() {

        Boolean flag = false;

        try {
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(programName.getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        for (RadioButton rb : radioButtons) {
            if (rb.isChecked()) {
                flag = true;
                super.onBackPressed();
            }
        }

    }
}

package com.ihsinformatics.gfatmmobile;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.util.ServerService;

import java.util.ArrayList;

public class LocationSelectionDialog extends AbstractSettingActivity {

    EditText supportContact;
    ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final int color = App.getColor(this, R.attr.colorPrimaryDark);
        final int color1 = App.getColor(this, R.attr.colorAccent);

        layout.removeAllViews();

        TextView programText = new TextView(this);
        programText.setText(App.getProgram());
        programText.setTextColor(color);
        programText.setTextSize(getResources().getDimension(R.dimen.medium));
        layout.addView(programText);

        ServerService serverService = new ServerService(getApplicationContext());
        String columnName = "";
        if (App.getProgram().equals(getResources().getString(R.string.pet)))
            columnName = "pet_location";
        else if (App.getProgram().equals(getResources().getString(R.string.fast)))
            columnName = "fast_location";
        else if (App.getProgram().equals(getResources().getString(R.string.comorbidities)))
            columnName = "comorbidities_location";
        else if (App.getProgram().equals(getResources().getString(R.string.pmdt)))
            columnName = "pmdt_location";
        else if (App.getProgram().equals(getResources().getString(R.string.childhood_tb)))
            columnName = "childhood_tb_location";

        final Object[][] locations = serverService.getAllLocations(columnName);

        if (locations == null || locations.length == 0) {
            final TextView text = new TextView(this);
            text.setText(getResources().getString(R.string.no_location));
            text.setTextSize(getResources().getDimension(R.dimen.small));
            layout.addView(text);
        } else {

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

                RadioButton selection = new RadioButton(this);
                linearLayout.addView(selection);
                selection.setTag(String.valueOf(locations[i][1]));
                radioButtons.add(selection);

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
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LocationSelectionDialog.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Preferences.LOCATION, App.getLocation());
                        editor.apply();

                        onBackPressed();
                    }

                });

                final TextView text = new TextView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                text.setLayoutParams(params);
                text.setText(String.valueOf(locations[i][1]));
                text.setTextSize(getResources().getDimension(R.dimen.small));
                text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_more, 0);
                text.setPadding(10, 0, 0, 0);
                DrawableCompat.setTint(text.getCompoundDrawables()[2], color);
                linearLayout.addView(text);

                verticalLayout.addView(linearLayout);
                moreLayout.setVisibility(View.GONE);

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

                    LinearLayout ll5 = new LinearLayout(this);
                    ll5.setOrientation(LinearLayout.HORIZONTAL);

                    TextView tv8 = new TextView(this);
                    tv8.setText(getResources().getString(R.string.description) + " ");
                    tv8.setTextSize(getResources().getDimension(R.dimen.small));
                    tv8.setTextColor(color1);
                    ll5.addView(tv8);

                    TextView tv9 = new TextView(this);
                    tv9.setText(String.valueOf(locations[i][14]));
                    tv9.setTextSize(getResources().getDimension(R.dimen.small));
                    ll5.addView(tv9);

                    moreLayout.addView(ll5);

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
                layout.addView(verticalLayout);

            }
        }

    }

    @Override
    public void onBackPressed() {

        Boolean flag = false;

        for (RadioButton rb : radioButtons) {
            if (rb.isChecked()) {
                flag = true;
                super.onBackPressed();
            }
        }

        if (!flag) {
            Toast toast = Toast.makeText(LocationSelectionDialog.this, getResources().getString(R.string.no_location_select), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

}

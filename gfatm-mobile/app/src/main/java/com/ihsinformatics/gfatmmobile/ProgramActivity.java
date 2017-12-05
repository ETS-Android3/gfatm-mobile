package com.ihsinformatics.gfatmmobile;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.ihsinformatics.gfatmmobile.util.ServerService;

public class ProgramActivity extends AbstractSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String[] programs = getResources().getStringArray(R.array.programs);

        ServerService serverService = new ServerService(this);
        String[] locatinPrograms = serverService.getLocationsProgamByName(App.getLocation());

        for (String pro : programs) {

            if (!locatinPrograms[0].equals("Y") && pro.equals(getResources().getString(R.string.fast)))
                continue;
            if (!locatinPrograms[1].equals("Y") && pro.equals(getResources().getString(R.string.pet)))
                continue;
            if (!locatinPrograms[2].equals("Y") && pro.equals(getResources().getString(R.string.childhood_tb)))
                continue;
            if (!locatinPrograms[3].equals("Y") && pro.equals(getResources().getString(R.string.comorbidities)))
                continue;

            RadioButton rb = new RadioButton(this);
            rb.setText(pro);
            rb.setPadding(0, 40, 0, 40);
            rb.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            if (App.getProgram().equals(pro))
                rb.setChecked(true);

            if (App.isLanguageRTL())
                rb.setGravity(Gravity.RIGHT);

        }

    }


    @Override
    public void onClick(View v) {

        if (v == okButton) {

            if (radioGroup.getCheckedRadioButtonId() == -1) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProgramActivity.this, R.style.dialog).create();
                alertDialog.setMessage(getString(R.string.no_program_selected));
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            } else {
                final RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

                if (!rb.getText().equals(App.getProgram())) {

                    AlertDialog alertDialog = new AlertDialog.Builder(ProgramActivity.this, R.style.dialog).create();
                    alertDialog.setMessage(getString(R.string.warning_before_program_change));

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    App.setProgram(rb.getText().toString());

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProgramActivity.this);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(Preferences.PROGRAM, App.getProgram());
                                    editor.apply();

                                    onBackPressed();
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


                } else
                    onBackPressed();

            }
        }

    }

    public void openLocationSelectionDialog() {
        Intent languageActivityIntent = new Intent(this, LocationSelectionDialog.class);
        startActivity(languageActivityIntent);
    }

}

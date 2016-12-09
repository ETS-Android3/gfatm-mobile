package com.ihsinformatics.gfatmmobile;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class LanguageActivity extends AbstractSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String[] languages = getResources().getStringArray(R.array.languages);

        for(String lang : languages){

            RadioButton rb = new RadioButton(this);
            rb.setText(lang);
            rb.setPadding(0,40,0,40);
            rb.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            if(App.getLanguage().equals(lang))
                rb.setChecked(true);

            if(App.isLanguageRTL())
                rb.setGravity(Gravity.RIGHT);

        }

    }

    @Override
    public void onClick(View v) {

        if(v == okButton){

            final RadioButton rb = (RadioButton) findViewById( radioGroup.getCheckedRadioButtonId());

            if(!rb.getText().equals(App.getLanguage())){

                AlertDialog alertDialog = new AlertDialog.Builder(LanguageActivity.this).create();
                alertDialog.setMessage(getString(R.string.warning_before_language_change));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                App.setLanguage(rb.getText().toString());

                                Locale locale = new Locale(App.getLanguage().substring (0, 2).toLowerCase());
                                Locale.setDefault (locale);
                                Configuration config = new Configuration();
                                config.setLocale(locale);
                                getResources().updateConfiguration (config, null);
                                App.setCurrentLocale (locale);

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


            }
            else
                onBackPressed();

        }

    }


}

package com.ihsinformatics.gfatmmobile;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ThemeActivity extends AbstractSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String[] themes = getResources().getStringArray(R.array.themes);

        for(String the : themes){

            RadioButton rb = new RadioButton(this);
            rb.setText(the);
            rb.setPadding(0,40,0,40);
            rb.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            if(App.getTheme().equals(the))
                rb.setChecked(true);

            if(App.isLanguageRTL())
                rb.setGravity(Gravity.RIGHT);

        }

    }

    @Override
    public void onClick(View v) {

        if(v == okButton){

            RadioButton rb = (RadioButton) findViewById( radioGroup.getCheckedRadioButtonId());
            App.setTheme(rb.getText().toString());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences (ThemeActivity.this);
            SharedPreferences.Editor editor = preferences.edit ();
            editor.putString (Preferences.THEME, App.getTheme ());
            editor.apply ();

            onBackPressed();
        }

    }


}

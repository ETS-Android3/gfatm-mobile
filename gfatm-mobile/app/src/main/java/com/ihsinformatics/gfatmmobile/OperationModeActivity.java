package com.ihsinformatics.gfatmmobile;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OperationModeActivity extends AbstractSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String[] modes = getResources().getStringArray(R.array.operation_modes);

        for (String mo : modes) {

            RadioButton rb = new RadioButton(this);
            rb.setText(mo);
            rb.setPadding(0, 40, 0, 40);
            rb.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            if (App.getMode().equals(mo))
                rb.setChecked(true);

            if (App.isLanguageRTL())
                rb.setGravity(Gravity.RIGHT);

        }

    }

    @Override
    public void onClick(View v) {

        if (v == okButton) {

            RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            App.setMode(rb.getText().toString());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(OperationModeActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preferences.MODE, App.getMode());
            editor.apply();

            onBackPressed();
        }

    }


}

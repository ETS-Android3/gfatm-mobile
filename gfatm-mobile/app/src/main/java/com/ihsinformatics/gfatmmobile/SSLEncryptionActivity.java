package com.ihsinformatics.gfatmmobile;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

public class SSLEncryptionActivity extends AbstractSettingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String[] ssl = getResources().getStringArray(R.array.ssl_encryption);

        for (String s : ssl) {

            RadioButton rb = new RadioButton(this);
            rb.setText(s);
            rb.setPadding(0, 40, 0, 40);
            rb.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            radioGroup.addView(rb);

            if (App.getSsl().equals(s))
                rb.setChecked(true);

            if (App.isLanguageRTL())
                rb.setGravity(Gravity.RIGHT);

        }

    }

    @Override
    public void onClick(View v) {

        if (v == okButton) {

            RadioButton rb = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            App.setSsl(rb.getText().toString());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SSLEncryptionActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(Preferences.SSL_ENCRYPTION, App.getSsl());
            editor.apply();

            onBackPressed();
        }

    }


}

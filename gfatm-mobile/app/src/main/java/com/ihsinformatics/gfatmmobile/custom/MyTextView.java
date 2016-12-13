package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

/**
 * Created by Rabbia on 11/25/2016.
 */

public class MyTextView extends TextView {

    String defaultValue;

    public MyTextView(Context context, String defaultValue) {
        super(context);
        this.defaultValue = defaultValue;
        selectDefaultValue();

        if(App.isLanguageRTL())
            setGravity(Gravity.RIGHT);
        else
            setGravity(Gravity.LEFT);

        setTextSize(getResources().getDimension(R.dimen.small));
    }

    public void selectDefaultValue(){

        if(defaultValue == null || defaultValue.equals(""))
            setText("");
        else
            setText(defaultValue);

    }
}

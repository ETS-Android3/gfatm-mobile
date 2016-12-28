package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.drm.DrmStore;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.ihsinformatics.gfatmmobile.App;

/**
 * Created by Rabbia on 11/25/2016.
 */

public class MyEditText extends EditText {

    String defaultValue;

    public MyEditText(Context context, String defaultValue, int length, InputFilter filter, int inputType) {
        super(context);
        this.defaultValue = defaultValue;

        setDefaultValue();

        InputFilter[] filters = new InputFilter[2];
        filters[0] = new InputFilter.LengthFilter(length);
        filters[1] = filter;
        setFilters(filters);

        setInputType(inputType);

        setSingleLine(true);

        if (App.isLanguageRTL())
            setGravity(Gravity.RIGHT);
        else
            setGravity(Gravity.LEFT);
    }

    public void setDefaultValue() {

        if (defaultValue == null || defaultValue.equals(""))
            setText("");
        else
            setText(defaultValue);

    }

    public String getValue() {
        return getValue().toString();
    }
}

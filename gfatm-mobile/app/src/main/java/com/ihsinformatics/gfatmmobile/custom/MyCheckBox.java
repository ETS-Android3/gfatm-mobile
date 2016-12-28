package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.widget.CheckBox;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class MyCheckBox extends CheckBox {

    Boolean defaultValue = false;

    public MyCheckBox(Context context, String text, Boolean defaultValue) {
        super(context);

        if (defaultValue != null)
            this.defaultValue = defaultValue;

        setText(text);

        setDefaultValue();

    }

    public void setDefaultValue() {
        setChecked(defaultValue);
    }

    public boolean getValue() {
        if (isChecked())
            return true;
        else
            return false;
    }


}

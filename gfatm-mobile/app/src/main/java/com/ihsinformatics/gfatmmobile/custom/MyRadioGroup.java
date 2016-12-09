package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/25/2016.
 */

public class MyRadioGroup extends RadioGroup implements android.widget.CompoundButton.OnCheckedChangeListener{

    private ArrayList<RadioButton>	buttons = new ArrayList<RadioButton>();
    private String defaultValue;

    public MyRadioGroup(Context context, String[] options, String defaultValue, int radioButtonsLayout) {
        super(context);
        this.defaultValue = defaultValue;

        for(String opt : options){

            RadioButton rb = new RadioButton(context);
            rb.setText(opt);
            buttons.add(rb);
            rb.setPadding(0,0,20,0);
            addView(rb);

        }

        if(radioButtonsLayout == App.HORIZONTAL)
            setOrientation (LinearLayout.HORIZONTAL);
        else
            setOrientation (LinearLayout.VERTICAL);

        selectDefaultValue();

    }

    @Override
    public void onCheckedChanged (CompoundButton button, boolean state)
    {
        if (state)
        {
            for (RadioButton r : buttons)
            {
                if (r != button)
                    r.setChecked (false);
            }
        }
    }

    public void selectDefaultValue(){
        for(RadioButton rb: buttons){
            String str = rb.getText().toString();
            if(str.equals(defaultValue))
                rb.setChecked(true);
        }
    }


    public String getValue(){
        String value = "";
        for(RadioButton rb: buttons){
            if(rb.isChecked()){
                value = rb.getTag().toString();
                break;
            }
        }
        return value;
    }


}

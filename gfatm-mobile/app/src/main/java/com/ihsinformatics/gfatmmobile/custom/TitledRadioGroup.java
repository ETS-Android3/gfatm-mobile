package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ihsinformatics.gfatmmobile.App;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledRadioGroup extends LinearLayout {

    MyTextView questionView;
    MyRadioGroup radioGroup;


    public TitledRadioGroup(Context context, String title, String ques, String[] options, String defaultValue, int radioButtonsLayout, int layoutOrientation) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);

        questionView = new MyTextView(context,ques);
        linearLayout.addView(questionView);

        radioGroup = new MyRadioGroup(context, options, defaultValue, radioButtonsLayout);
        linearLayout.addView(radioGroup);

        if(layoutOrientation == App.HORIZONTAL){
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20,0,0,0);
            radioGroup.setLayoutParams(layoutParams1);
        }

        addView(linearLayout);
    }

    public MyTextView getQuestionView(){
        return questionView;
    }

    public MyRadioGroup getRadioGroup(){
        return radioGroup;
    }

    public String getRadioGroupValue(){
        return radioGroup.getValue();
    }
}

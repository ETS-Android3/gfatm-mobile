package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.text.InputFilter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledEditText extends LinearLayout {

    MyTextView questionView;
    MyEditText editText;
    Boolean mandatory;


    public TitledEditText(Context context, String title, String ques, String defaultValue, String hint, int length, InputFilter filter, int inputType, int layoutOrientation, Boolean mandatory) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.mandatory = mandatory;

        if(mandatory){
            int color = App.getColor(context, R.attr.colorAccent);
            TextView mandatorySign = new TextView(context);
            mandatorySign.setText("*");
            mandatorySign.setTextColor(color);
            linearLayout.addView(mandatorySign);
        }

        questionView = new MyTextView(context,ques);
        linearLayout.addView(questionView);

        editText = new MyEditText(context, defaultValue, length, filter, inputType);
        linearLayout.addView(editText);

        if(layoutOrientation == App.HORIZONTAL){
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20,0,0,0);
            editText.setLayoutParams(layoutParams1);
        }

        if(!(hint == null || hint.equals("")))
            editText.setHint(hint);

        addView(linearLayout);
    }

    public MyTextView getQuestionView(){
        return questionView;
    }

    public MyEditText getEditText(){
        return editText;
    }

    public Boolean getMandatory(){ return mandatory; }
}

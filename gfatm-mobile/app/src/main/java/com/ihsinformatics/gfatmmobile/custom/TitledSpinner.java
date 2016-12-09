package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ihsinformatics.gfatmmobile.App;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledSpinner extends LinearLayout {

    MyTextView questionView;
    MySpinner spinner;


    public TitledSpinner(Context context, String title, String ques, String[] options, String defaultValue, int layoutOrientation) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);

        questionView = new MyTextView(context,ques);
        linearLayout.addView(questionView);

        spinner = new MySpinner(context, options, defaultValue);
        linearLayout.addView(spinner);

        addView(linearLayout);
    }

    public MyTextView getQuestionView(){
        return questionView;
    }

    public MySpinner getSpinner(){
        return spinner;
    }

    public String getSpinnerValue(){
        return spinner.getValue();
    }
}

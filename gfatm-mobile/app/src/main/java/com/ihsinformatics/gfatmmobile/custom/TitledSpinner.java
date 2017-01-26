package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledSpinner extends LinearLayout {

    MyTextView questionView;
    MySpinner spinner;
    Boolean mandatory = false;


    public TitledSpinner(Context context, String title, String ques, String[] options, String defaultValue, int layoutOrientation) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);

        questionView = new MyTextView(context, ques);
        linearLayout.addView(questionView);

        spinner = new MySpinner(context, options, defaultValue);
        linearLayout.addView(spinner);

        addView(linearLayout);
    }

    public TitledSpinner(Context context, String title, String ques, String[] options, String defaultValue, int layoutOrientation, boolean mandatory) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.mandatory = mandatory;

        LinearLayout hLayout = new LinearLayout(context);
        hLayout.setOrientation(HORIZONTAL);

        if (mandatory) {
            int color = App.getColor(context, R.attr.colorAccent);
            TextView mandatorySign = new TextView(context);
            mandatorySign.setText("*");
            mandatorySign.setTextColor(color);
            hLayout.addView(mandatorySign);
        }

        questionView = new MyTextView(context, ques);
        hLayout.addView(questionView);
        linearLayout.addView(hLayout);

        spinner = new MySpinner(context, options, defaultValue);
        linearLayout.addView(spinner);

        addView(linearLayout);
    }

    public MyTextView getQuestionView() {
        return questionView;
    }

    public MySpinner getSpinner() {
        return spinner;
    }

    public String getSpinnerValue() {
        return spinner.getValue();
    }

    public Boolean getMandatory() {
        return mandatory;
    }
}

package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledRadioGroup extends LinearLayout {

    MyTextView questionView;
    MyRadioGroup radioGroup;
    Boolean mandatory = false;
    String tag = "";

    public TitledRadioGroup(Context context, String title, String ques, String[] options, String defaultValue, int radioButtonsLayout, int layoutOrientation) {
        super(context);
        if(!App.isTabletDevice(context)){
            layoutOrientation = App.VERTICAL;
            radioButtonsLayout = App.VERTICAL;
        }
        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);

        questionView = new MyTextView(context, ques);
        linearLayout.addView(questionView);

        radioGroup = new MyRadioGroup(context, options, defaultValue, radioButtonsLayout);
        linearLayout.addView(radioGroup);

        if (layoutOrientation == App.HORIZONTAL) {
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20, 0, 0, 0);
            radioGroup.setLayoutParams(layoutParams1);
        }

        addView(linearLayout);
    }

    public TitledRadioGroup(Context context, String title, String ques, String[] options, String defaultValue, int radioButtonsLayout, int layoutOrientation, boolean mandatory) {
        super(context);
        if(!App.isTabletDevice(context)){
            layoutOrientation = App.VERTICAL;
            radioButtonsLayout = App.VERTICAL;
        }
        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.mandatory = mandatory;

        LinearLayout hLayout = new LinearLayout(context);
        hLayout.setOrientation(HORIZONTAL);

        questionView = new MyTextView(context, ques);
        hLayout.addView(questionView);

        if (mandatory) {
            TextView mandatorySign = new TextView(context);
            mandatorySign.setText(" *");
            mandatorySign.setTextColor(Color.parseColor("#ff0000"));
            hLayout.addView(mandatorySign);
        }

        linearLayout.addView(hLayout);

        radioGroup = new MyRadioGroup(context, options, defaultValue, radioButtonsLayout);
        linearLayout.addView(radioGroup);

        if (layoutOrientation == App.HORIZONTAL) {
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20, 0, 0, 0);
            radioGroup.setLayoutParams(layoutParams1);
        }

        addView(linearLayout);
    }

    public void setRadioGroupEnabled(Boolean flag) {

        for (RadioButton rg : radioGroup.getButtons()) {
            rg.setClickable(flag);
        }

    }

    public MyTextView getQuestionView() {
        return questionView;
    }

    public MyRadioGroup getRadioGroup() {
        return radioGroup;
    }

    public String getRadioGroupSelectedValue() {
        return radioGroup.getSelectedValue();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getMandatory() {
        return mandatory;
    }
}

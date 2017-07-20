package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledCheckBoxes extends LinearLayout {

    MyTextView questionView;
    ArrayList<MyCheckBox> checkedBoxes = new ArrayList<MyCheckBox>();
    Boolean[] defaultValues;
    Boolean mandatory = false;

    public TitledCheckBoxes(Context context, String title, String ques, String[] options, Boolean[] defaultValues, int radioButtonsLayout, int layoutOrientation) {
        super(context);
        if(!App.isTabletDevice(context)){
            layoutOrientation = App.VERTICAL;
            radioButtonsLayout = App.VERTICAL;
        }
        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.defaultValues = defaultValues;

        questionView = new MyTextView(context, ques);
        linearLayout.addView(questionView);

        LinearLayout ll = new LinearLayout(context);
        if (radioButtonsLayout == App.HORIZONTAL)
            ll.setOrientation(LinearLayout.HORIZONTAL);
        else
            ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < options.length; i++) {

            Boolean checked = false;
            if (defaultValues != null && defaultValues.length > i)
                checked = defaultValues[i];

            MyCheckBox checkBox = new MyCheckBox(context, options[i], checked);
            ll.addView(checkBox);
            checkedBoxes.add(checkBox);
        }

        linearLayout.addView(ll);
        addView(linearLayout);
    }

    public TitledCheckBoxes(Context context, String title, String ques, String[] options, Boolean[] defaultValues, int radioButtonsLayout, int layoutOrientation, Boolean mandatory) {
        super(context);
        if(!App.isTabletDevice(context)){
            layoutOrientation = App.VERTICAL;
            radioButtonsLayout = App.VERTICAL;
        }
        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.defaultValues = defaultValues;
        this.mandatory = mandatory;

        LinearLayout hLayout = new LinearLayout(context);
        hLayout.setOrientation(HORIZONTAL);

        if (mandatory) {
            TextView mandatorySign = new TextView(context);
            mandatorySign.setText(" *");
            mandatorySign.setTextColor(Color.parseColor("#ff0000"));
            hLayout.addView(mandatorySign);
        }

        questionView = new MyTextView(context, ques);
        hLayout.addView(questionView);

        linearLayout.addView(hLayout);

        LinearLayout ll = new LinearLayout(context);
        if (radioButtonsLayout == App.HORIZONTAL)
            ll.setOrientation(LinearLayout.HORIZONTAL);
        else
            ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < options.length; i++) {

            Boolean checked = false;
            if (defaultValues != null && defaultValues.length > i)
                checked = defaultValues[i];

            MyCheckBox checkBox = new MyCheckBox(context, options[i], checked);
            ll.addView(checkBox);
            checkedBoxes.add(checkBox);
        }

        linearLayout.addView(ll);
        addView(linearLayout);
    }

    public void setCheckedBoxesEnabled(Boolean flag) {

        for (MyCheckBox cb : checkedBoxes) {
            cb.setClickable(flag);
        }

    }

    public MyTextView getQuestionView() {
        return questionView;
    }

    public void selectDefaultValue() {
        if (defaultValues != null) {
            for (int i = 0; i < defaultValues.length; i++) {

                Boolean flag = defaultValues[i];
                if (checkedBoxes.get(i) != null)
                    checkedBoxes.get(i).setChecked(flag);

            }

            if (defaultValues.length != checkedBoxes.size()) {
                for (int i = defaultValues.length; i < checkedBoxes.size(); i++) {
                    if (checkedBoxes.get(i) != null)
                        checkedBoxes.get(i).setChecked(false);
                }
            }

        } else {
            for (int i = 0; i < checkedBoxes.size(); i++) {

                checkedBoxes.get(i).setChecked(false);

            }
        }
    }

    public MyCheckBox getCheckBox(int index) {

        if (index <= checkedBoxes.size())
            return checkedBoxes.get(index);
        else
            return null;

    }

    public MyCheckBox getCheckBox(String text) {

        for (MyCheckBox cb : checkedBoxes) {
            if (cb.getText().equals(text))
                return cb;
        }
        return null;
    }

    public ArrayList<MyCheckBox> getCheckedBoxes() {
        return checkedBoxes;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

}

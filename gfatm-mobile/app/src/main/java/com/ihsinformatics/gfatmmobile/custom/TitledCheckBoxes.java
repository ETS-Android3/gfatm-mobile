package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ihsinformatics.gfatmmobile.App;

import java.util.ArrayList;

/**
 * Created by Rabbia on 11/30/2016.
 */

public class TitledCheckBoxes extends LinearLayout {

    MyTextView questionView;
    ArrayList<MyCheckBox> checkedBoxes = new ArrayList<MyCheckBox>();
    Boolean[] defaultValues;


    public TitledCheckBoxes(Context context, String title, String ques, String[] options, Boolean[] defaultValues, int radioButtonsLayout, int layoutOrientation) {
        super(context);

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);
        this.defaultValues = defaultValues;

        questionView = new MyTextView(context,ques);
        linearLayout.addView(questionView);

        LinearLayout ll = new LinearLayout(context);
        if(radioButtonsLayout == App.HORIZONTAL)
            ll.setOrientation (LinearLayout.HORIZONTAL);
        else
            ll.setOrientation (LinearLayout.VERTICAL);

        for(int i = 0; i<options.length; i++){

            Boolean checked = false;
            if(defaultValues != null && defaultValues.length > i)
                checked = defaultValues[i];

            MyCheckBox checkBox = new MyCheckBox(context, options[i], checked);
            ll.addView(checkBox);
            checkedBoxes.add(checkBox);
        }

        linearLayout.addView(ll);
        addView(linearLayout);
    }

    public MyTextView getQuestionView(){
        return questionView;
    }

    public void selectDefaultValue(){
        if(defaultValues != null) {
            for (int i = 0; i < defaultValues.length; i++) {

                Boolean flag = defaultValues[i];
                if(checkedBoxes.get(i) != null)
                    checkedBoxes.get(i).setChecked(flag);

            }

            if(defaultValues.length != checkedBoxes.size()){
                for(int i = defaultValues.length; i<checkedBoxes.size(); i++){
                    if(checkedBoxes.get(i) != null)
                        checkedBoxes.get(i).setChecked(false);
                }
            }

        }
        else{
            for (int i = 0; i < checkedBoxes.size(); i++) {

                checkedBoxes.get(i).setChecked(false);

            }
        }
    }

    public MyCheckBox getCheckBox(int index){

        if(index <= checkedBoxes.size())
            return checkedBoxes.get(index);
        else
            return null;

    }

    public MyCheckBox getCheckBox(String text){

        for(MyCheckBox cb : checkedBoxes){
            if(cb.getText().equals(text))
                return cb;
        }
        return null;
    }

    public ArrayList<MyCheckBox> getCheckedBoxes(){
        return checkedBoxes;
    }

}

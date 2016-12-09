package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ihsinformatics.gfatmmobile.App;

/**
 * Created by Rabbia on 12/1/2016.
 */

public class TitledButton extends LinearLayout {

    MyTextView questionView;
    Button button;
    String defaultValue;

    public TitledButton(Context context, String title, String ques, String defaultValue, int layoutOrientation) {
        super(context);
        this.defaultValue = defaultValue;

        MyLinearLayout linearLayout = new MyLinearLayout(context, title, layoutOrientation);

        questionView = new MyTextView(context,ques);
        linearLayout.addView(questionView);

        button = new Button(context);
        setDefaultValue();

        if(layoutOrientation == App.HORIZONTAL){
            LayoutParams layoutParams1 = new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(20,0,0,0);
            button.setLayoutParams(layoutParams1);
        }

        linearLayout.addView(button);

        addView(linearLayout);
    }


    public MyTextView getQuestionView(){
        return questionView;
    }

    public Button getButton(){
        return button;
    }

    public String getButtonText(){
        return button.getText().toString();
    }

    public void setDefaultValue(){button.setText(defaultValue);}
}

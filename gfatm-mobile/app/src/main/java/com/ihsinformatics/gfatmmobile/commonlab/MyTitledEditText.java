package com.ihsinformatics.gfatmmobile.commonlab;

import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class MyTitledEditText extends LinearLayout {

    TextView tvTitle;
    private EditText etValue;

    public MyTitledEditText(Context context, String title, boolean mandatory, String dataType){
        super(context);
        View mainContent = inflate(getContext(), R.layout.lab_layout_edit_text, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title + "<font color=red>" + (mandatory ? "    *" : "") + "</font>"));
        etValue = mainContent.findViewById(R.id.editText);
        if(dataType.contains("FreeTextDatatype")){
            etValue.setInputType(InputType.TYPE_CLASS_TEXT);
        } else if(dataType.contains("LongFreeTextDatatype")){
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if(dataType.contains("FloatDatatype")){
            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if(dataType.contains("DateDatatype")){
            etValue.setInputType(InputType.TYPE_CLASS_DATETIME);
        }
    }

    public String getText() {
        return etValue.getText().toString();
    }

    public void showError(String s) {
        etValue.setError(s);
    }
}

package com.ihsinformatics.gfatmmobile.commonlab;

import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class MyTitledRadioGroup extends LinearLayout {

    TextView tvTitle;
    private RadioGroup rgValue;
    View mainContent;
    private TextView tvError;

    public MyTitledRadioGroup(Context context, String title, boolean mandatory, String dataType){
        super(context);
        mainContent = inflate(getContext(), R.layout.lab_layout_radio_group, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title + "<font color=red>" + (mandatory ? "    *" : "") + "</font>"));
        rgValue = mainContent.findViewById(R.id.rgAnswer);
        tvError = mainContent.findViewById(R.id.tvError);
    }

    public String getText() {
        RadioButton checked = mainContent.findViewById(rgValue.getCheckedRadioButtonId());
        if(checked == null) return "";
        return checked.getText().toString();

    }

    public void showError(String s) {
        tvError.setVisibility(View.VISIBLE);
        tvError.setError(s);
    }
}

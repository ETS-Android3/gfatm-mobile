package com.ihsinformatics.gfatmmobile.commonlab;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class MyTitledEditText extends LinearLayout {

    TextView tvTitle;

    public MyTitledEditText(Context context, String title, boolean mandatory){
        super(context);
        View mainContent = inflate(getContext(), R.layout.lab_layout_edit_text, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title + "<font color=red>" + (mandatory ? "    *" : "") + "</font>"));
    }
}

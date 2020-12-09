package com.ihsinformatics.gfatmmobile.medication;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class TitledRadioGroup extends LinearLayout {

    TextView tvTitle;

    public TitledRadioGroup(Context context, String title) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.layout_drugs_from, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
    }

}

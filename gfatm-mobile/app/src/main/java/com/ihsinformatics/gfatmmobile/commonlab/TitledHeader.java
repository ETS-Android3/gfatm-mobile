package com.ihsinformatics.gfatmmobile.commonlab;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class TitledHeader extends LinearLayout {

    TextView tvTitle;

    public TitledHeader(Context context, String title, String titleRight) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.lab_layout_header, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        if(titleRight!=null){
            TextView tvTitleRight = mainContent.findViewById(R.id.tvTitleRight);
            tvTitleRight.setVisibility(View.VISIBLE);
            tvTitleRight.setText(titleRight);
        }
    }
}

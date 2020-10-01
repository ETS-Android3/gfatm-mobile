package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class TitledHeader extends LinearLayout {

    TextView tvTitle;

    public TitledHeader(Context context, String title) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.layout_header, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
    }
}

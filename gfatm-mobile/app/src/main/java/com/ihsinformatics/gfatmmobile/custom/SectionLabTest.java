package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

public class SectionLabTest extends LinearLayout {

    LinearLayout llHeader;
    LinearLayout llSectionContent;
    TextView tvSectionName;
    ImageView ivIcon;

    public SectionLabTest(Context context, String title, String[][] tests) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.layout_section_lab_test, this);
        llHeader = mainContent.findViewById(R.id.llHeader);
        llSectionContent = mainContent.findViewById(R.id.llSectionContent);
        tvSectionName = mainContent.findViewById(R.id.tvSectionName);

        tvSectionName.setText(title);
        ivIcon = mainContent.findViewById(R.id.ivIcon);

        llHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llSectionContent.getVisibility() == View.VISIBLE) {
                    llSectionContent.setVisibility(View.GONE);
                    ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_plus));
                } else {
                    llSectionContent.setVisibility(View.VISIBLE);
                    ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_minus));
                }
            }
        });

        String[][] data = {{"", "", ""}, {"", "", ""}, {"", "", ""}};
        for (int i = 0; i < tests.length; i++) {
            TestSelection testSelection = new TestSelection(getContext(), data);
            llSectionContent.addView(testSelection);
        }

    }
}

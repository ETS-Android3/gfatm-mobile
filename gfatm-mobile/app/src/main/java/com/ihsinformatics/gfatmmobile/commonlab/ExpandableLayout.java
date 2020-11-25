package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestType;

import java.util.ArrayList;
import java.util.List;

public class ExpandableLayout extends LinearLayout implements MyLabInterface {

    LinearLayout layoutHeader;
    LinearLayout layoutContent;
    TextView tvTitle;
    ImageView ivIcon;
    Fragment parentFragment;
    View view;
    MyLabInterface myLabInterface;

    public void init() {
        view = inflate(getContext(), R.layout.lab_layout_expandable, this);
        layoutHeader = view.findViewById(R.id.layoutHeader);
        layoutContent = view.findViewById(R.id.layoutContent);
        tvTitle = view.findViewById(R.id.tvTitle);
        ivIcon = view.findViewById(R.id.ivIcon);
        layoutHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutContent.getVisibility() == View.VISIBLE) {
                    layoutContent.setVisibility(View.GONE);
                    ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.lab_ic_plus));
                } else {
                    layoutContent.setVisibility(View.VISIBLE);
                    ivIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_minus));
                }
            }
        });
    }

    private List<SelectableTestRow>  selectableTestRows;
    public ExpandableLayout(Context context, String title, String[][] tests, List<TestType> testTypes, Fragment parentFragment) {
        super(context);
        init();
        this.parentFragment = parentFragment;
        selectableTestRows = new ArrayList<>();
        tvTitle.setText(title);

        int i=0;
        for (String[] test : tests) {
            SelectableTestRow selectableTestRow = new SelectableTestRow(getContext(), test, testTypes.get(i), this);
            selectableTestRows.add(selectableTestRow);
            layoutContent.addView(selectableTestRow);

            i++;
        }
    }

    public List<SelectableTestRow> getSelectedSearchableTestRows() {
        List<SelectableTestRow> toReturn = new ArrayList<>();
        if(selectableTestRows!=null) {
            for(SelectableTestRow row: selectableTestRows) {
                if(row.isChecked()) {
                    toReturn.add(row);
                }
            }
        }

        return toReturn;
    }

    public ExpandableLayout(Context context, String title, String[][] data) {
        super(context);
        init();

        tvTitle.setText(title);

        LinearLayout.LayoutParams layoutParams;
        LinearLayout layout;

        for (String[] datum : data) {
            layout = new LinearLayout(context);
            layout.setOrientation(App.HORIZONTAL);
            layout.setWeightSum(3.0f);

            layoutParams = new LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, 1.0f);
            layoutParams.setMargins(40, 30, 40, 30);

            TextView textView = new TextView(getContext());
            textView.setText(datum[0]);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setLayoutParams(layoutParams);
            layout.addView(textView);

            layoutParams = new LayoutParams(
                    0, LayoutParams.WRAP_CONTENT, 2.0f);
            layoutParams.setMargins(40, 30, 40, 30);

            TextView textView2 = new TextView(getContext());
            textView2.setText(datum[1]);
            textView2.setLayoutParams(layoutParams);
            layout.addView(textView2);

            layoutContent.addView(layout);
        }
    }

    @Override
    public void onAddResultButtonClick(int position, boolean isCompleted) {
    }

    @Override
    public void onCancelButtonClick() {
    }

    @Override
    public String getEncounterName() {
        myLabInterface = (MyLabInterface) parentFragment;
        return myLabInterface.getEncounterName();
    }
}
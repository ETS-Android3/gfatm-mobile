package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

/**
 * Created by Rabbia on 11/25/2016.
 */

public class MyLinearLayout extends LinearLayout {

    LinearLayout childLayout;
    TextView titleTextView;

    public MyLinearLayout(Context context, String title, int layout) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 20);
        setLayoutParams(layoutParams);

        setFocusable(true);
        setClickable(true);

        titleTextView = new TextView(context);
        int color = App.getColor(context, R.attr.colorPrimaryDark);
        titleTextView.setTextColor(color);
        super.addView(titleTextView);
        titleTextView.setTypeface(null, Typeface.BOLD_ITALIC);

        LayoutParams layoutParams1 = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(20, 0, 0, 0);
        titleTextView.setLayoutParams(layoutParams1);

        if (title == null || title.equals(""))
            titleTextView.setVisibility(GONE);
        else
            titleTextView.setText(title);

        childLayout = new LinearLayout(context);
        childLayout.setPadding(5, 5, 5, 10);
        if (layout == App.HORIZONTAL)
            childLayout.setOrientation(LinearLayout.HORIZONTAL);
        else
            childLayout.setOrientation(LinearLayout.VERTICAL);

        super.addView(childLayout);

    }

    @Override
    public void addView(View child) {
        childLayout.addView(child);
    }

    public void setTitle(String title) {
        if (title == null || title.equals("")) {
            titleTextView.setVisibility(GONE);
            titleTextView.setText("");
        } else {
            titleTextView.setVisibility(VISIBLE);
            titleTextView.setText(title);
        }
    }
}

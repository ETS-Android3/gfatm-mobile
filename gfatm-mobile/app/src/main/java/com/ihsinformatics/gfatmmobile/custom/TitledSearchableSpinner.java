package com.ihsinformatics.gfatmmobile.custom;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitledSearchableSpinner extends LinearLayout {

    TextView tvTitle;
    SearchableSpinner searchableSpinner;
    ArrayAdapter<String> spinnerArrayAdapter;

    public TitledSearchableSpinner(Context context, String title, String[] options, String defaultValue, boolean mandatory) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.layout_searchable_spinner, this);

        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title + "<font color=red>" + (mandatory ? "    *" : "") + "</font>"));

        searchableSpinner = mainContent.findViewById(R.id.searchableSpinner);
        List<String> spinnerList = new ArrayList<String>(Arrays.asList(options));
        spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        searchableSpinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}

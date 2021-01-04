package com.ihsinformatics.gfatmmobile.commonlab;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyTitledSearchableSpinner extends LinearLayout {

    private TextView tvTitle;
    private SearchableSpinner searchableSpinner;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private String[] optionsValues;
    private String title;
    public MyTitledSearchableSpinner(Context context, String title, String[] options, String defaultValue, boolean mandatory) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.lab_layout_searchable_spinner, this);

        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title + "<font color=red>" + (mandatory ? "    *" : "") + "</font>"));
        this.title = title;
        searchableSpinner = mainContent.findViewById(R.id.searchableSpinner);
        List<String> spinnerList = new ArrayList<String>(Arrays.asList(options));
        spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        searchableSpinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public MyTitledSearchableSpinner(Context context, String title, String[] options, String[] optionsValues, String defaultValue, boolean mandatory) {
        this(context, title, options, defaultValue, mandatory);

        this.optionsValues = optionsValues;
    }

    public String getSpinnerSelectedItemValue() {
        if(optionsValues!=null) {
            return optionsValues[searchableSpinner.getSelectedItemPosition()];
        } else {
            return searchableSpinner.getSelectedItem().toString();
        }
    }

    public String getSpinnerSelectedItemName() {
        return searchableSpinner.getSelectedItem().toString();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        searchableSpinner.setEnabled(enabled);
    }
}

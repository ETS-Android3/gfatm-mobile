package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;

import java.util.Calendar;

public class MyTitledDatePicker extends LinearLayout {

    private TextView tvTitle;
    private EditText etValue;
    private View mainContent;
    private DatePickerDialog datePickerDialog;
    private Context context;

    public MyTitledDatePicker(Context context, String title, boolean mandatory, String dataType) {
        super(context);
        this.context = context;
        mainContent = inflate(getContext(), R.layout.lab_layout_date_picker, this);
        tvTitle = mainContent.findViewById(R.id.tvTitle);
        tvTitle.setText(Html.fromHtml(title + "<font color=red>" + (mandatory ? "    *" : "") + "</font>"));
        etValue = mainContent.findViewById(R.id.date);

        etValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MyTitledDatePicker.this.context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                etValue.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public String getText() {
        return etValue.getText().toString();
    }

    public void showError(String s) {
        etValue.setError(s);
    }
}

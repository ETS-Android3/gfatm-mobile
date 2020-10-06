package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;

public class SelectableTestRow extends LinearLayout {

    Button btnInstructions;
    CheckBox cbTest;
    TextView tvTestName;
    TextView tvTestDate;

    MyLabInterface myLabInterface;

    public SelectableTestRow(final Context context, final String[] test, final ViewGroup viewGroup) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.lab_layout_selectable_test_row, this);
        cbTest = mainContent.findViewById(R.id.cbTest);
        tvTestName = mainContent.findViewById(R.id.tvTestName);
        tvTestDate = mainContent.findViewById(R.id.tvTestDate);

        tvTestName.setText(test[0]);
        tvTestDate.setText(test[1]);

        btnInstructions = mainContent.findViewById(R.id.btnInstructions);

        btnInstructions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                myLabInterface = (MyLabInterface) viewGroup;

                String[][] data = {{"Encounter Name", myLabInterface.getEncounterName()},
                        {"Encounter Date", "12-Jan-2020"}, {"Test Name", test[0]},
                        {"Lab Reference", "2020-01-09 10:33:35:261"}};

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                View dialogView = inflate(getContext(), R.layout.lab_dialog_titled, null);

                final Button btnClose = dialogView.findViewById(R.id.btnClose);
                Button btnSave = dialogView.findViewById(R.id.btnSave);

                ((TextView) dialogView.findViewById(R.id.tvTitle)).setText(context.getString(R.string.add_instructions));

                LinearLayout linearLayout = dialogView.findViewById(R.id.mainContent);
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
                    textView.setLayoutParams(layoutParams);
                    layout.addView(textView);

                    layoutParams = new LayoutParams(
                            0, LayoutParams.WRAP_CONTENT, 2.0f);
                    layoutParams.setMargins(40, 30, 40, 30);

                    TextView textView2 = new TextView(getContext());
                    textView2.setText(datum[1]);
                    textView2.setLayoutParams(layoutParams);
                    layout.addView(textView2);

                    linearLayout.addView(layout);
                }

                layout = new LinearLayout(context);
                layout.setOrientation(App.HORIZONTAL);
                layout.setWeightSum(3.0f);

                layoutParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                layoutParams.setMargins(40, 30, 40, 30);

                TextView textView = new TextView(getContext());
                textView.setText(context.getString(R.string.instructions));
                textView.setLayoutParams(layoutParams);
                layout.addView(textView);

                layoutParams = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2.0f);
                layoutParams.setMargins(40, 30, 40, 30);


                EditText editText = new EditText(getContext());
                editText.setBackground(getResources().getDrawable(R.drawable.lab_background_box));
                editText.setPadding(20, 20, 20, 20);
                editText.setLines(4);
                editText.setGravity(Gravity.TOP | Gravity.START);
                editText.setLayoutParams(layoutParams);
                layout.addView(editText);

                linearLayout.addView(layout);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                btnClose.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnSave.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}

package com.ihsinformatics.gfatmmobile.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;

public class TestSelection extends LinearLayout {

    Button btnInstructions;

    public TestSelection(final Context context, final String[][] data) {
        super(context);
        View mainContent = inflate(getContext(), R.layout.layout_test_selection, this);

        btnInstructions = mainContent.findViewById(R.id.btnInstructions);
        btnInstructions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                View dialogView = inflate(getContext(), R.layout.dialog_add_instructions, null);

                LinearLayout linearLayout = dialogView.findViewById(R.id.mainContent);

                for (int i = 0; i < data.length; i++) {

                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(App.HORIZONTAL);
                    layout.setWeightSum(2.0f);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

                    layoutParams.setMargins(50, 20, 50, 20);


                    TextView textView = new TextView(getContext());
                    textView.setText("Encounter Name");
                    textView.setLayoutParams(layoutParams);
                    layout.addView(textView);


                    TextView textView2 = new TextView(getContext());
                    textView2.setText("PMDT Treatment Support");
                    textView2.setLayoutParams(layoutParams);
                    layout.addView(textView2);

                    linearLayout.addView(layout);

                }


                dialogBuilder.setView(dialogView);
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
    }

}

package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;

import java.util.List;

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.ViewHolder> {

    private List<String> data;
    private final LayoutInflater mInflater;
    private final boolean isCompleted;
    Context context;

    MyLabInterface myLabInterface;

    LabTestAdapter(Context context, /*List<String> data,*/ String testType, Fragment fragment) {
        this.mInflater = LayoutInflater.from(context);
        //this.data = data;
        isCompleted = testType.equals(context.getString(R.string.complete));
        myLabInterface = (MyLabInterface) fragment;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lab_test_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!isCompleted)
            holder.ibCheck.setVisibility(View.GONE);

        holder.btnAddResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLabInterface.onAddResultButtonClick();
            }
        });

        holder.btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummaryDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        //return data.size();
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton ibCheck;
        Button btnAddResult;
        Button btnSummary;

        ViewHolder(View itemView) {
            super(itemView);
            ibCheck = itemView.findViewById(R.id.ibCheck);
            btnAddResult = itemView.findViewById(R.id.btnAddResult);
            btnSummary = itemView.findViewById(R.id.btnSummary);
        }
    }

    private void showSummaryDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = mInflater.inflate(R.layout.lab_dialog_titled, null);

        dialogView.findViewById(R.id.layoutButtons).setVisibility(View.GONE);
        ((TextView) dialogView.findViewById(R.id.tvTitle)).setText(context.getString(R.string.summary));

        LinearLayout mainContent = dialogView.findViewById(R.id.mainContent);
        String[][] tests = {{"ABC", "XYZ"},
                {"ABC", "XYZ"},
                {"ABC", "XYZ"}};

        ExpandableLayout[] expandableLayouts = {new ExpandableLayout(context, "Test Order Detail", tests),
                new ExpandableLayout(context, "Test Samples Detail", tests),
                new ExpandableLayout(context, "Test Result Detail", tests)};

        for (ExpandableLayout expandableLayout : expandableLayouts)
            mainContent.addView(expandableLayout);

        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final ImageButton ibClose = dialogView.findViewById(R.id.ibClose);
        ibClose.setVisibility(View.VISIBLE);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}
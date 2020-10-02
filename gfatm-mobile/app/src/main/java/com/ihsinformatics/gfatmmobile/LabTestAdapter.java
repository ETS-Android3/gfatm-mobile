package com.ihsinformatics.gfatmmobile;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.ViewHolder> {

    private List<String> data;
    private final LayoutInflater mInflater;
    private final boolean isCompleted;
    Context context;

    public interface MyButtonInterface{
        void onAddResultClick();
    }

    MyButtonInterface myButtonInterface;

    LabTestAdapter(Context context, /*List<String> data,*/ String testType, Fragment fragment) {
        this.mInflater = LayoutInflater.from(context);
        //this.data = data;
        isCompleted = testType.equals("COMPLETE");
        myButtonInterface = (MyButtonInterface) fragment;
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
                myButtonInterface.onAddResultClick();
            }
        });

        holder.btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View dialogView = mInflater.inflate(R.layout.dialog_add_instructions, null);

                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        //return data.size();
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
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
}
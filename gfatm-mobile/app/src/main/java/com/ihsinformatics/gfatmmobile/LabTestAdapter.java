package com.ihsinformatics.gfatmmobile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

public class LabTestAdapter extends RecyclerView.Adapter<LabTestAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private final boolean isCompleted;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    LabTestAdapter(Context context, /*List<String> data,*/ String testType) {
        this.mInflater = LayoutInflater.from(context);
        //this.mData = data;
        isCompleted = testType.equals("COMPLETE");
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.lab_test_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!isCompleted)
            holder.ibCheck.setVisibility(View.GONE);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        //return mData.size();
        return 5;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageButton ibCheck;

        ViewHolder(View itemView) {
            super(itemView);
            ibCheck = itemView.findViewById(R.id.ibCheck);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

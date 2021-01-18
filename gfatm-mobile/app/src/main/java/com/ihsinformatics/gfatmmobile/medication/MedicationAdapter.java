package com.ihsinformatics.gfatmmobile.medication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrder;

import java.util.ArrayList;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private final boolean isCompleted;
    private ArrayList<DrugOrderEntity> drugsOrders;
    private DrugRenewListener drugRenewListener;

    MedicationAdapter(Context context, String medicationType, ArrayList<DrugOrderEntity> drugOrderEntities, DrugRenewListener drugRenewListener) {

        this.mInflater = LayoutInflater.from(context);
        isCompleted = medicationType.equals("Complete");
        this.drugRenewListener = drugRenewListener;
        this.context = context;
        this.drugsOrders = drugOrderEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.medication_item, parent, false);
        return new MedicationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!isCompleted) {
            holder.btnStopDose.setVisibility(View.VISIBLE);
            holder.btnRenewDose.setText("Revise");
        } else {
            holder.btnStopDose.setVisibility(View.GONE);
            holder.btnRenewDose.setText("Renew");
        }
        final DrugOrderEntity order = drugsOrders.get(position);
        MedicationDrug drug = DataAccess.getInstance().getDrugByUUID(drugsOrders.get(position).getDrugUUID());
        Object[][] encounter = DataAccess.getInstance().getEncountersByUUID(context, drugsOrders.get(position).getEncounterUUID());
        if(encounter.length > 0) {
            holder.tvEncounterDate.setText("Date: "+encounter[0][3].toString());
            holder.tvEncounterName.setText("Encounter: "+(String) encounter[0][0]);
        }
        holder.tvDrugName.setText(drug.getName());
        holder.tvDrugID.setText("OrderID: "+ drugsOrders.get(position).getOrderNumber());

        holder.btnRenewDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCompleted) {
                    drugRenewListener.onRenew(order);
                } else {
                    drugRenewListener.onRevise(order);
                }
            }
        });

        holder.btnStopDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DrugStopDialog.class);
                intent.putExtra("order", order);
                DrugStopDialog.drugRenewListener = drugRenewListener;
                context.startActivity(intent);
            }
        });

        holder.ibViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), DrugOrderDetails.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugsOrders.size();
    }

    public void updateData(ArrayList<DrugOrderEntity> currentDrugOrderEntities) {
        this.drugsOrders = currentDrugOrderEntities;
        notifyDataSetChanged();
    }

    public void setRenewListener(DrugRenewListener renewListener) {
        this.drugRenewListener = renewListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageButton ibViewDetails;
        private Button btnStopDose;
        private Button btnRenewDose;
        private ImageButton ibCheck;
        private TextView tvDrugName;
        private TextView tvDrugID;
        private TextView tvEncounterName;
        private TextView tvEncounterDate;

        ViewHolder(View itemView) {
            super(itemView);
            ibViewDetails = itemView.findViewById(R.id.ibViewDetails);
            btnStopDose = itemView.findViewById(R.id.btnStopDose);
            btnRenewDose = itemView.findViewById(R.id.btnRenewDose);
            tvDrugName = itemView.findViewById(R.id.tvDrugName);
            tvDrugID = itemView.findViewById(R.id.tvDrugID);
            tvEncounterName = itemView.findViewById(R.id.tvEncounterName);

            tvEncounterDate = itemView.findViewById(R.id.tvEncounterDate);
            ibCheck = itemView.findViewById(R.id.ibCheck);
        }
    }
}

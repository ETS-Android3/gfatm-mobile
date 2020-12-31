package com.ihsinformatics.gfatmmobile.medication;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private final boolean isCompleted;
    private List<String> drugs;
    private List<String> drugsIds;
    MedicationAdapter(Context context, String medicationType) {
        this.mInflater = LayoutInflater.from(context);
        isCompleted = medicationType.equals("Complete");
        this.context = context;
        drugs = new ArrayList<>();
        drugs.add("AMIKACIN");
        drugs.add("BEDAQUILINE");
        drugs.add("CYCLOSERINE");
        drugs.add("ETHAMBUTOL");
        drugs.add("ETHIONAMIDE");
        drugs.add("ISONIAZID");
        drugs.add("LINEZOLID");
        drugs.add("KANAMYCIN");
        drugs.add("RIFABUTIN");
        drugs.add("ZYLORIC");

        drugsIds = new ArrayList<>();
        drugsIds.add("423");
        drugsIds.add("234");
        drugsIds.add("654");
        drugsIds.add("256");
        drugsIds.add("260");
        drugsIds.add("266");
        drugsIds.add("432");
        drugsIds.add("433");
        drugsIds.add("444");
        drugsIds.add("460");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.medication_item, parent, false);
        return new MedicationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (!isCompleted)
            holder.ibCheck.setVisibility(View.GONE);

        holder.tvDrugName.setText(drugs.get(position));
        holder.tvDrugID.setText("OrderID: "+ drugsIds.get(position));
        holder.btnRenewDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Renew Dose", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnStopDose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Stop Dose", Toast.LENGTH_SHORT).show();
            }
        });

        holder.ibViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "View Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drugs.size();
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
            tvEncounterName.setVisibility(View.INVISIBLE);
            tvEncounterDate = itemView.findViewById(R.id.tvEncounterDate);
            ibCheck = itemView.findViewById(R.id.ibCheck);
        }
    }
}

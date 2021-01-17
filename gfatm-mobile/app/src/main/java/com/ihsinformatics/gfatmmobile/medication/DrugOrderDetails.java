package com.ihsinformatics.gfatmmobile.medication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDuration;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute;

public class DrugOrderDetails extends AppCompatActivity {

    private DrugOrderEntity order;
    private TextView tvName;
    private TextView tvFrequency;
    private TextView tvDose;
    private TextView tvDuration;
    private TextView tvRoute;
    private TextView tvInstructions;
    private TextView tvStartDate;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_activity_drug_order_details);
        order = (DrugOrderEntity) getIntent().getSerializableExtra("order");
        if(order == null) {
            finish();
        }

        tvName = findViewById(R.id.tvDrugName);
        tvFrequency=  findViewById(R.id.tvFrequency);
        tvDose = findViewById(R.id.tvDose);
        tvDuration = findViewById(R.id.tvDuration);
        tvRoute = findViewById(R.id.tvRoute);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvStartDate = findViewById(R.id.tvStartDate);

        MedicationDrug drug = DataAccess.getInstance().getDrugByUUID(order.getDrugUUID());
        MedicationFrequency frequency = DataAccess.getInstance().getFrequencyByUUID(order.getFrequencyUUID());
        MedicationRoute route = DataAccess.getInstance().getRouteByUUID(order.getRouteUUID());
        MedicationDuration duration = DataAccess.getInstance().getDurationByUUID(order.getDurationUnitsUUID());
        MedicationDoseUnit doseUnit = DataAccess.getInstance().getDoseUnitByUUID(order.getDoseUnitsUUID());

        tvName.setText(drug==null?"n/a":drug.getName());
        tvFrequency.setText(frequency==null?"n/a":frequency.getDisplay());
        tvDose.setText(order.getDose()+" "+(doseUnit==null?"n/a":doseUnit.getDisplay()));
        tvDuration.setText(order.getDuration()+" "+(duration==null?"n/a":duration.getDisplay()));
        tvRoute.setText(route==null?"n/a":route.getDisplay());
        tvInstructions.setText(order.getInstructions());
        tvStartDate.setText(order.getDateActivated());


    }
}
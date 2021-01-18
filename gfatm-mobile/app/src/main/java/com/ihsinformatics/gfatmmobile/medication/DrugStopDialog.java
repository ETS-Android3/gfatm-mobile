package com.ihsinformatics.gfatmmobile.medication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationOrderReason;
import com.ihsinformatics.gfatmmobile.medication.utils.MedicationUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DrugStopDialog extends AppCompatActivity implements View.OnClickListener {

    private EditText etDate;
    private Spinner spReason;
    private Button btnSave;
    private List<MedicationOrderReason> reasons;
    private List<String> reasonStrings;
    private List<String> reasonUUIDs;
    private DrugOrderEntity order;
    private Calendar calendar = Calendar.getInstance();
    private String selectedUUID;
    public static DrugRenewListener drugRenewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_activity_drug_stop_dialog);

        order = (DrugOrderEntity) getIntent().getSerializableExtra("order");

        if(order == null) {
            Toast.makeText(this, "Please select a drug", Toast.LENGTH_LONG).show();
        }

        btnSave = findViewById(R.id.btnSubmit);
        btnSave.setOnClickListener(this);

        reasons = DataAccess.getInstance().getAllOrderReasons();
        reasonStrings = new ArrayList<>();
        reasonUUIDs = new ArrayList<>();
        reasonStrings.add("--Select Reason--");
        reasonUUIDs.add(null);
        for(MedicationOrderReason r: reasons) {
            reasonStrings.add(r.getDisplay());
            reasonUUIDs.add(r.getUuid());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reasonStrings);
        spReason = findViewById(R.id.spReason);
        spReason.setAdapter(adapter);

        etDate = findViewById(R.id.etStopDate);
        etDate.setText(DateFormat.format("dd-MM-yyyy", new Date()).toString());
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrugStopDialog.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yy, int mm, int dd) {
                                calendar.set(yy, mm, dd);
                                etDate.setText(DateFormat.format("dd-MM-yyyy", calendar).toString());
                            }

                        }, year, month, dayOfMonth);

                Calendar today = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(today.getTime().getTime());
                datePickerDialog.getDatePicker().setMinDate(today.getTime().getTime());

                datePickerDialog.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        selectedUUID = reasonUUIDs.get(spReason.getSelectedItemPosition());
        if(selectedUUID==null) {
            Toast.makeText(DrugStopDialog.this, "Please select a reason", Toast.LENGTH_LONG).show();
            return;
        }
        save();
    }

    private void save() {
        List<DrugOrderEntity> drugOrders = new ArrayList<>();
        order.setDateStopped(DateFormat.format("yyy-MM-dd", calendar).toString());
        order.setOrderReasonUUID(selectedUUID);
        order.setAction("DISCONTINUE");
        order.setToUpload(true);
        order.setUploadReason("STOP");
        DataAccess.getInstance().updateDrugOrder(order);
        drugOrders.add(order);
        if(App.getMode().equalsIgnoreCase("online")) {
            new MedicationUtils(new MedicationUtils.OnDownloadCompleteListener() {
                @Override
                public void onCompleted() {
                    drugRenewListener.onStop(order);
                    Toast.makeText(DrugStopDialog.this, "Saved", Toast.LENGTH_LONG).show();
                    finish();
                }
            }).upload(drugOrders);
        } else {
            drugRenewListener.onStop(order);
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
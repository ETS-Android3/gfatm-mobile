package com.ihsinformatics.gfatmmobile.medication;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledEditText;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;

import java.util.ArrayList;
import java.util.Calendar;

@Deprecated
public class AddMedicineFragment extends Fragment {

    private LinearLayout mainLayout;
    private View[] views;
    private Button btnCancel;
    private Button btnSubmit;
    private TitledHeader headerDrugSelection;
    private MyTitledSearchableSpinner encounter;
    private MyTitledSearchableSpinner drugSet;
    private MyTitledSearchableSpinner drugs;
    private TitledHeader headerDrugPrescription;
    private View getDrugsFrom;
    private View dose;
    private MyTitledSearchableSpinner frequency;
    private MyTitledSearchableSpinner route;
    private MyTitledEditText dosingInstructions;
    private MyTitledEditText orderReason;
    private View startDate;
    private View duration;
    private MyMedicationInterface myMedicationInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myMedicationInterface = (MyMedicationInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.medicine_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        btnCancel = mainContent.findViewById(R.id.btnCancel);
        btnSubmit = mainContent.findViewById(R.id.btnSubmit);
        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<String> drugss = new ArrayList<String>();
        drugss.add("AMIKACIN");
        drugss.add("BEDAQUILINE");
        drugss.add("CYCLOSERINE");
        drugss.add("ETHAMBUTOL");
        drugss.add("ETHIONAMIDE");
        drugss.add("ISONIAZID");
        drugss.add("LINEZOLID");
        drugss.add("KANAMYCIN");
        drugss.add("RIFABUTIN");
        drugss.add("ZYLORIC");
        headerDrugSelection = new TitledHeader(getActivity(), "Drug Selection", "Add Medicine");
        getDrugsFrom = getActivity().getLayoutInflater().inflate(R.layout.layout_drugs_from, null);
        encounter = new MyTitledSearchableSpinner(getActivity(), "Encounter", getResources().getStringArray(R.array.dummy_items), null, false);
        drugSet = new MyTitledSearchableSpinner(getActivity(), "Drug Set", getResources().getStringArray(R.array.dummy_items), null, false);
        drugs = new MyTitledSearchableSpinner(getActivity(), "Drugs", drugss.toArray(new String[drugss.size()]), null, true);
        headerDrugPrescription = new TitledHeader(getActivity(), "Drug Prescription", null);
        dose = getActivity().getLayoutInflater().inflate(R.layout.layout_dose, null);
        frequency = new MyTitledSearchableSpinner(getActivity(), "Frequency", getResources().getStringArray(R.array.dummy_items), null, true);
        route = new MyTitledSearchableSpinner(getActivity(), "Route", getResources().getStringArray(R.array.dummy_items), null, true);
        dosingInstructions = new MyTitledEditText(getActivity(), "Dosing Instructions", false, null);
        startDate = getActivity().getLayoutInflater().inflate(R.layout.layout_start_date, null);
        duration = getActivity().getLayoutInflater().inflate(R.layout.layout_duration, null);
        orderReason = new MyTitledEditText(getActivity(), "Order Reason", false, null);

        views = new View[]{headerDrugSelection, getDrugsFrom, encounter, drugSet, drugs, headerDrugPrescription, dose, frequency, route, dosingInstructions, startDate, duration, orderReason};
        for (View v : views)
            mainLayout.addView(v);

        setListeners();

    }

    public void setListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMedicationInterface.onCancelButtonClick();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT).show();
            }
        });

        startDate.findViewById(R.id.editText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yy, int mm, int dd) {
                                calendar.set(yy, mm, dd);
                                ((EditText) startDate.findViewById(R.id.editText)).setText(DateFormat.format("MMMM dd,yyyy", calendar).toString());
                            }

                        }, year, month, dayOfMonth);

                Calendar today = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(today.getTime().getTime());

                datePickerDialog.show();
            }
        });
    }
}
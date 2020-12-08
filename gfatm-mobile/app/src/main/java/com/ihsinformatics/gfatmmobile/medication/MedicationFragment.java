package com.ihsinformatics.gfatmmobile.medication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.LabTestAdapter;

public class MedicationFragment extends Fragment implements View.OnClickListener{

    private View view;
    MedicationAdapter adapter;
    RecyclerView rvMedications;
    TextView tvNumberOfMedication;

    @Override
    public void onClick(View view) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.medication_fragment, container, false);

        tvNumberOfMedication = view.findViewById(R.id.tvNumberOfMedication);
        rvMedications = view.findViewById(R.id.rvMedications);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMedications.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MedicationAdapter(getActivity());
        rvMedications.setAdapter(adapter);

        tvNumberOfMedication.setText(String.valueOf(adapter.getItemCount()) + " medications listed");

    }
}

package com.ihsinformatics.gfatmmobile.medication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;

import java.util.List;

public class MedicationListFragment extends Fragment implements MyMedicationInterface{

    RecyclerView rvMedications;
    MedicationAdapter adapter;
    String medicationType;

    MyMedicationInterface myMedicationInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myMedicationInterface = (MyMedicationInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.medications_list_fragment, container, false);
        medicationType = getArguments().getString("Medication type");
        rvMedications = mainContent.findViewById(R.id.rvMedications);
        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMedications.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MedicationAdapter(getActivity(), medicationType);
        rvMedications.setAdapter(adapter);
    }

    @Override
    public void onCancelButtonClick() {

    }
}

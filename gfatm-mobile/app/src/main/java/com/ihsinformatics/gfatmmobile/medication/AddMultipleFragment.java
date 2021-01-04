package com.ihsinformatics.gfatmmobile.medication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Encounter;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;

import java.util.ArrayList;
import java.util.List;

public class AddMultipleFragment extends Fragment implements DrugAdapter.MyDrugInterface, RadioGroup.OnCheckedChangeListener {

    private LinearLayout mainLayout;
    private View[] views;
    private Button btnCancel;
    private Button btnSubmit;
    private TitledHeader headerDrugSelection;
    private MyTitledSearchableSpinner encounter;
    private MyTitledSearchableSpinner drugSet;
    private MyTitledSearchableSpinner drugsList;
    private View getDrugsFrom;
    private View addDrug;
    private TitledHeader headerDrugList;
    private List<DrugModel> drugsModelsList = new ArrayList<DrugModel>();
    private RecyclerView rvDrugs;
    private DrugAdapter adapter;
    private MyMedicationInterface myMedicationInterface;
    private RadioGroup rgDrugsFrom;
    DataAccess dataAccess;
    ArrayList<String> drugs = new ArrayList<String>();
    ArrayList<String> drugsUUIDs = new ArrayList<String>();
    private ArrayList<String> encounters = new ArrayList<String>();
    private ArrayList<String> encounterUUIDs = new ArrayList<String>();

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

        dataAccess = DataAccess.getInstance();

        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        populateAllDrugs();
        downloadEncounters();

        headerDrugSelection = new TitledHeader(getActivity(), "Drug Selection", "Add Multiple");
        getDrugsFrom = getActivity().getLayoutInflater().inflate(R.layout.layout_drugs_from, null);
        rgDrugsFrom = getDrugsFrom.findViewById(R.id.rgDrugsFrom);
        rgDrugsFrom.setOnCheckedChangeListener(this);
        encounter = new MyTitledSearchableSpinner(getActivity(), "Encounter", encounters.toArray(new String[encounters.size()]), encounterUUIDs.toArray(new String[encounterUUIDs.size()]), null, false);

        drugSet = new MyTitledSearchableSpinner(getActivity(), "Drug Set", getResources().getStringArray(R.array.dummy_items), null, false);
        drugSet.setEnabled(false);

        drugsList = new MyTitledSearchableSpinner(getActivity(), "Drugs", drugs.toArray(new String[drugs.size()]), drugsUUIDs.toArray(new String[drugsUUIDs.size()]), null, true);

        addDrug = getActivity().getLayoutInflater().inflate(R.layout.layout_button_add_drug, null);
        headerDrugList = new TitledHeader(getActivity(), "Drugs List", null);
        rvDrugs = new RecyclerView(getActivity());

        views = new View[]{headerDrugSelection, getDrugsFrom, encounter, drugSet, drugsList, addDrug, headerDrugList, rvDrugs};
        for (View v : views)
            mainLayout.addView(v);

        setAdapter();
        setListeners();
        updateDrugList();
    }

    void setAdapter() {
        rvDrugs.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DrugAdapter(getActivity(), drugsModelsList);
        adapter.onAttachToParentFragment(AddMultipleFragment.this);
        rvDrugs.setAdapter(adapter);
    }

    void updateDrugList() {
        headerDrugList.setVisibility(drugsModelsList.size() > 0 ? View.VISIBLE : View.GONE);
        rvDrugs.setVisibility(drugsModelsList.size() > 0 ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), "Submit "+drugsModelsList.size(), Toast.LENGTH_SHORT).show();
                myMedicationInterface.onSaveButtonClick();
            }
        });

        addDrug.findViewById(R.id.btnAddDrug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrugModel newDrugModel = new DrugModel();
                newDrugModel.setName(drugsList.getSpinnerSelectedItemName());
                newDrugModel.setDrugUUID(drugsList.getSpinnerSelectedItemValue());

                if(!drugsModelsList.contains(newDrugModel)) {
                    drugsModelsList.add(newDrugModel);
                    Toast.makeText(getActivity(), drugsList.getSpinnerSelectedItemName() + " added.", Toast.LENGTH_SHORT).show();
                    updateDrugList();
                } else {
                    Toast.makeText(getActivity(), drugsList.getSpinnerSelectedItemName() + " is ALREADY added.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void updateDrugsList() {
        updateDrugList();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rbChecked = getDrugsFrom.findViewById(checkedId);
        if(rbChecked.getText().toString().equalsIgnoreCase("drugs")) {
            drugSet.setEnabled(false);
            populateAllDrugs();
        } else {
            drugSet.setEnabled(true);
        }
    }

    private void downloadEncounters() {
        ArrayList<Encounter> encounterList = new ArrayList<>();
        if (App.getPatient() == null) return;
        Object[][] encounter = DataAccess.getInstance().getEncountersByPatient(getActivity(), App.getPatientId());
        System.out.println(encounter);
        for(int i=0; i<encounter.length; i++) {
            Encounter e = new Encounter();
            e.setDisplay(encounter[i][0].toString()+" "+encounter[i][5].toString());
            e.setUuid(encounter[i][6].toString());
            encounterList.add(e);
        }

        for(Encounter e: encounterList) {
            encounters.add(e.getDisplay());
            encounterUUIDs.add(e.getUuid());
        }
    }

    private void populateAllDrugs() {
        List<MedicationDrug> drugEntities = dataAccess.getAllDrugs();
        for(MedicationDrug d: drugEntities) {
            drugs.add(d.getName());
            drugsUUIDs.add(d.getUuid());
        }
    }
}
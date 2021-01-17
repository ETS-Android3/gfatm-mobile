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
import com.ihsinformatics.gfatmmobile.commonlab.network.CustomCallback;
import com.ihsinformatics.gfatmmobile.commonlab.network.CustomCallbackHelper;
import com.ihsinformatics.gfatmmobile.commonlab.network.RetrofitClientFactory;
import com.ihsinformatics.gfatmmobile.commonlab.network.Utils;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Encounter;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.DrugOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDoseUnit;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDrug;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationDuration;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationFrequency;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.MedicationRoute;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrder;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrderPostModel;
import com.ihsinformatics.gfatmmobile.medication.utils.MedicationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private DataAccess dataAccess;
    private ArrayList<String> drugs = new ArrayList<String>();
    private ArrayList<String> drugsUUIDs = new ArrayList<String>();
    private ArrayList<String> encounters = new ArrayList<String>();
    private ArrayList<String> encounterUUIDs = new ArrayList<String>();
    private Boolean isRenew;
    private DrugOrderEntity order;


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
        if(getArguments()!=null) {
            isRenew = getArguments().getBoolean("type");
            order = getArguments().getSerializable("order") != null ? (DrugOrderEntity) getArguments().getSerializable("order") : null;
        }
        headerDrugSelection = new TitledHeader(getActivity(), "Drug Selection", "Add Multiple");
        getDrugsFrom = getActivity().getLayoutInflater().inflate(R.layout.layout_drugs_from, null);
        getDrugsFrom.setVisibility(View.GONE);
        rgDrugsFrom = getDrugsFrom.findViewById(R.id.rgDrugsFrom);
        rgDrugsFrom.setOnCheckedChangeListener(this);
        encounter = new MyTitledSearchableSpinner(getActivity(), "Encounter", encounters.toArray(new String[encounters.size()]), encounterUUIDs.toArray(new String[encounterUUIDs.size()]), null, false);

        drugSet = new MyTitledSearchableSpinner(getActivity(), "Drug Set", getResources().getStringArray(R.array.dummy_items), null, false);
        drugSet.setEnabled(false);
        drugSet.setVisibility(View.GONE);

        drugsList = new MyTitledSearchableSpinner(getActivity(), "Drugs", drugs.toArray(new String[drugs.size()]), drugsUUIDs.toArray(new String[drugsUUIDs.size()]), null, true);
        if(order != null) {
            drugsList.setEnabled(false);
            MedicationDrug drugOrdered = DataAccess.getInstance().getDrugByUUID(order.getDrugUUID());
            drugsList.setSelection(drugOrdered.getName());
        }
        addDrug = getActivity().getLayoutInflater().inflate(R.layout.layout_button_add_drug, null);
        headerDrugList = new TitledHeader(getActivity(), "Drugs List", null);
        rvDrugs = new RecyclerView(getActivity());

        views = new View[]{headerDrugSelection, getDrugsFrom, encounter, drugSet, drugsList, addDrug, headerDrugList, rvDrugs};
        for (View v : views)
            mainLayout.addView(v);

        setAdapter();
        setListeners();
        updateDrugList();
        if(order != null) {
            if(!isRenew) {
                encounter.setEnabled(false);
            }
            drugsList.setEnabled(false);
            MedicationDrug drugOrdered = DataAccess.getInstance().getDrugByUUID(order.getDrugUUID());
            drugsList.setSelection(drugOrdered.getName());
            addDrugToList();
        }
    }

    void setAdapter() {
        DrugModel m = null;
        if(order!=null) {
            m = new DrugModel();
            m.copyEntity(order);
        }
        rvDrugs.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DrugAdapter(getActivity(), drugsModelsList, m);
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
                List<DrugOrderEntity> drugOrders = new ArrayList<>();
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                for(DrugModel d: drugsModelsList) {
                    DrugOrderEntity e = new DrugOrderEntity();
                    e.setToUpload(true);
                    e.setDateActivated(d.getStartDate());
                    e.setPatientUUID(App.getPatient().getUuid());
                    e.setInstructions(d.getInstructions());
                    e.setDuration(Integer.valueOf(d.getDurationAmount()));
                    e.setDose(Double.valueOf(d.getDoseAmount()));
                    e.setEncounterUUID(encounter.getSpinnerSelectedItemValue());
                    e.setOrdererUUID(App.getProviderUUid());
                    e.setCareSettingUUID("6f0c9a92-6f24-11e3-af88-005056821db0");
                    e.setQuantity(0d);

                    if(isRenew == null) {
                        e.setAction("NEW");
                        e.setUploadReason("NEW");
                    }
                    else {
                        e.setPreviousOrderUUID(order.getUuid());
                        e.setAction("REVISE");
                        e.setUploadReason("REVISE");
                    }

                    MedicationDrug drug = DataAccess.getInstance().getDrugByName(d.getName());
                    MedicationFrequency frequency = DataAccess.getInstance().getFrequencyByName(d.getFrequency());
                    MedicationRoute route = DataAccess.getInstance().getRouteByName(d.getRoute());
                    MedicationDuration duration = DataAccess.getInstance().getDurationByName(d.getDurationUnit());
                    MedicationDoseUnit doseUnit = DataAccess.getInstance().getDoseUnitByName(d.getDoseUnit());

                    e.setDrugUUID(drug.getUuid());
                    e.setConceptUUID(drug.getConceptUUID());
                    e.setRouteUUID(route.getUuid());
                    e.setFrequencyUUID(frequency.getUuid());
                    e.setDurationUnitsUUID(duration.getUuid());
                    e.setDoseUnitsUUID(doseUnit.getUuid());
                    e.setQuantityUnitsUUID(doseUnit.getUuid());

                    drugOrders.add(e);

                }

                // In Case of revise, old order should be stopped automatically -- for local db only
                if(isRenew!=null && !isRenew) {
                    order.setDateStopped(new SimpleDateFormat("yyy-MM-dd").format(new Date()));
                    DataAccess.getInstance().updateDrugOrder(order);
                }

                DataAccess.getInstance().insertAllDrugOrders(drugOrders);
                if(App.getMode().equalsIgnoreCase("online")) {
                    new MedicationUtils(new MedicationUtils.OnDownloadCompleteListener() {
                        @Override
                        public void onCompleted() {
                            myMedicationInterface.onSaveButtonClick();
                        }
                    }).upload(drugOrders);
                } else {
                    myMedicationInterface.onSaveButtonClick();
                }
            }
        });

        addDrug.findViewById(R.id.btnAddDrug).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDrugToList();
            }
        });
    }

    private void addDrugToList() {
        DrugModel newDrugModel = new DrugModel();
        newDrugModel.setName(drugsList.getSpinnerSelectedItemName());
        newDrugModel.setDrugUUID(drugsList.getSpinnerSelectedItemValue());
        // check if already same active drug in the database for this patient exists
        boolean activePresent = false;
        List<DrugOrderEntity> existingOrders = DataAccess.getInstance().getDrugOrdersByDrugUUID(newDrugModel.getDrugUUID(), App.getPatient().getUuid());
        if(isRenew==null) {
            for (DrugOrderEntity e : existingOrders) {
                if (MedicationUtils.isCurrentActive(e)) {
                    activePresent = true;
                    continue;
                }
            }
        }
        if(!drugsModelsList.contains(newDrugModel) && !activePresent) {
            drugsModelsList.add(newDrugModel);
            Toast.makeText(getActivity(), drugsList.getSpinnerSelectedItemName() + " added.", Toast.LENGTH_SHORT).show();
            updateDrugList();
        } else {
            Toast.makeText(getActivity(), drugsList.getSpinnerSelectedItemName() + " is ALREADY added.", Toast.LENGTH_SHORT).show();
        }
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
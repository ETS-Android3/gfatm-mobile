package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.ihsinformatics.gfatmmobile.App;
import com.ihsinformatics.gfatmmobile.MyLabInterface;
import com.ihsinformatics.gfatmmobile.R;
import com.ihsinformatics.gfatmmobile.commonlab.ExpandableLayout;
import com.ihsinformatics.gfatmmobile.commonlab.TitledHeader;
import com.ihsinformatics.gfatmmobile.commonlab.MyTitledSearchableSpinner;
import com.ihsinformatics.gfatmmobile.commonlab.network.CommonLabAPIClient;
import com.ihsinformatics.gfatmmobile.commonlab.network.HttpCodes;
import com.ihsinformatics.gfatmmobile.commonlab.network.RetrofitClientFactory;
import com.ihsinformatics.gfatmmobile.commonlab.network.Utils;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Encounter;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.EncountersResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestType;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestTypesResponse;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.DataAccess;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestOrderEntity;
import com.ihsinformatics.gfatmmobile.commonlab.persistance.entities.TestTypeEntity;
import com.ihsinformatics.gfatmmobile.shared.Metadata;
import com.ihsinformatics.gfatmmobile.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTestFragment extends Fragment implements MyLabInterface {

    LinearLayout mainLayout;
    View[] views;
    MyTitledSearchableSpinner encounterSpinner;
    TitledHeader header;
    Button btnCancel;
    Button btnSubmit;
    private List<TestTypeEntity> testTypes;
    HashMap<String, List<TestTypeEntity>> testTypesMap;
    private List<Encounter> encounterList;

    private List<ExpandableLayout> testTypeLayouts;
    private HashMap<String, Encounter> encounterHashMap;

    MyLabInterface myLabInterface;

    public void onAttachToParentFragment(Fragment fragment) {
        myLabInterface = (MyLabInterface) fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mainContent = inflater.inflate(R.layout.lab_add_test_fragment, container, false);
        mainLayout = mainContent.findViewById(R.id.mainLayout);
        btnCancel = mainContent.findViewById(R.id.btnCancel);
        btnSubmit = mainContent.findViewById(R.id.btnSubmit);

        return mainContent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testTypes = new ArrayList<>();
        testTypesMap = new HashMap<>();
        downloadEncounters();

    }

    private void downloadEncounters() {

        {
            encounterList = new ArrayList<>();
            if (App.getPatient() == null) return;
            Object[][] encounter = DataAccess.getInstance().getEncountersByPatient(getActivity(), App.getPatientId());
            System.out.println(encounter);
            for(int i=0; i<encounter.length; i++) {
                Encounter e = new Encounter();
                e.setDisplay(encounter[i][0].toString()+" "+encounter[i][5].toString());
                e.setUuid(encounter[i][6].toString());
                encounterList.add(e);
            }

            afterEncounterSDownloaded();
        }


    }

    private void afterEncounterSDownloaded() {
        downloadTestTypes();
    }

    private void downloadTestTypes() {
        testTypes = DataAccess.getInstance().getAllTestTypes();
        afterTestTypesDownloaded();
    }

    private void afterTestTypesDownloaded() {

        for(TestTypeEntity testType: testTypes) {
            String groupName = testType.getTestGroup();
            if(testTypesMap.containsKey(groupName)) {
                testTypesMap.get(groupName).add(testType);
            } else {
                List<TestTypeEntity> list = new ArrayList();
                list.add(testType);
                testTypesMap.put(groupName, list);
            }
        }
        proceed();
    }

    private void proceed() {
        setListeners();
        List<String> encountersDisplays = new ArrayList<>();
        encounterHashMap = new HashMap<>();
        testTypeLayouts = new ArrayList<>();
        for(Encounter e: encounterList) {
            encountersDisplays.add(e.getDisplay());
            encounterHashMap.put(e.getDisplay(), e);
        }
        header = new TitledHeader(getActivity(), getString(R.string.add_test), null);
        encounterSpinner = new MyTitledSearchableSpinner(getActivity(), getString(R.string.encounter), encountersDisplays.toArray(new String[0]), null, true);

        views = new View[testTypesMap.size()+2];
        views[0] = header;
        views[1] = encounterSpinner;

        int i = 2;
        Iterator it = testTypesMap.keySet().iterator();
        while (it.hasNext()) {

            String key = (String) it.next();
            List<TestTypeEntity> testTypeList = testTypesMap.get(key);
            String[][] tests = new String[testTypeList.size()][2];
            int j=0;
            for(TestTypeEntity t: testTypeList) {
                tests[j][0] = t.getName();
                tests[j][1] = (new Date().getTime()+j)+"";
                j++;
            }
            ExpandableLayout l = new ExpandableLayout(getActivity(), key, tests, testTypeList, (Fragment) this);
            views[i] = l;
            testTypeLayouts.add(l);
            i++;
        }

        /*bacteriologySection = new ExpandableLayout(getActivity(), "BACTERIOLOGY", tests, (Fragment) this);
        biochemistrySection = new ExpandableLayout(getActivity(), "BIOCHEMISTRY", tests, (Fragment) this);
        cardiologySection = new ExpandableLayout(getActivity(), "CARDIOLOGY", tests, (Fragment) this);
        hematologySection = new ExpandableLayout(getActivity(), "HEMATOLOGY", tests, (Fragment) this);*/

        // views = new View[]{header, encounterSpinner, bacteriologySection, biochemistrySection, cardiologySection, hematologySection};
        for (View v : views)
            mainLayout.addView(v);
    }

    public void setListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLabInterface.onCancelButtonClick();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encounterUUID = encounterHashMap.get(encounterSpinner.getSpinnerSelectedItem()).getUuid();
                List<SelectableTestRow> checkedTests = new ArrayList<>();
                for(ExpandableLayout l: testTypeLayouts) {
                    checkedTests.addAll(l.getSelectedSearchableTestRows());
                }
                List<TestOrderEntity> testOrderEntities = new ArrayList<>();
                for(SelectableTestRow row: checkedTests) {
                    TestOrderEntity entity = new TestOrderEntity();
                    entity.setLabReferenceNumber(row.getReferenceNumber());
                    entity.setLabTestType(row.testType);
                    entity.setCaresettingUUID("6f0c9a92-6f24-11e3-af88-005056821db0");
                    entity.setEncounterUUID(encounterUUID);
                    entity.setOrdererUUID(App.getProviderUUid());
                    entity.setPatientUUID(App.getPatient().getUuid());
                    testOrderEntities.add(entity);
                    /*JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("labReferenceNumber", row.getReferenceNumber());*/
                }

                DataAccess.getInstance().insertAllOrders(testOrderEntities);
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                myLabInterface.onResultSaved();
                /*
                {
                  "labReferenceNumber": "AFB Smear test - 2020-09-14 10:23:14",
                  "labTestType": "d3854cbc-f668-47c7-8054-6a6e33f4caaa",
                  "order": {
                    "action": "NEW",
                    "patient": "6bd343b7-5ad0-4b62-905d-56033320c547",
                    "concept": "5497AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                    "encounter": "9a953d18-6837-4e26-9236-959d3fd0d4b4",
                    "careSetting": "6f0c9a92-6f24-11e3-af88-005056821db0",
                    "type": "testorder",
                    "orderer": "449390a0-d338-4ff2-9b3f-61dc1617c2fe" // provider for tahira.niazi
                  }
                }
                * */

            }
        });
    }

    public void uploadData(List<SelectableTestRow> testRows) {

    }

    @Override
    public void onAddResultButtonClick(int position, boolean isCompleted) {
    }

    @Override
    public void onCancelButtonClick() {
    }

    @Override
    public String getEncounterName() {
        return encounterSpinner.getSpinnerSelectedItem();
    }

    @Override
    public void onResultSaved() {

    }
}

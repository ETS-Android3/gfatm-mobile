package com.ihsinformatics.gfatmmobile.commonlab;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
    private List<TestType> testTypes;
    HashMap<String, List<TestType>> testTypesMap;
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
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<EncountersResponse> call = apiClient.fetchAllEncountersByPatient(App.getPatient().getUuid(), Utils.getBasicAuth());
        call.enqueue(new Callback<EncountersResponse>() {
            @Override
            public void onResponse(Call<EncountersResponse> call, Response<EncountersResponse> response) {
                if(response.code() == HttpCodes.OK) {
                    EncountersResponse encountersResponse = response.body();
                    encounterList = encountersResponse.getResults();
                    afterEncounterSDownloaded();
                }
            }

            @Override
            public void onFailure(Call<EncountersResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void afterEncounterSDownloaded() {
        downloadTestTypes();
    }

    private void downloadTestTypes() {
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<TestTypesResponse> call = apiClient.fetchAllTestTypes("full", Utils.getBasicAuth());
        call.enqueue(new Callback<TestTypesResponse>() {
            @Override
            public void onResponse(Call<TestTypesResponse> call, Response<TestTypesResponse> response) {
                if(response.code() == HttpCodes.OK) {
                    TestTypesResponse testTypesResponse = response.body();
                    testTypes = testTypesResponse.getResults();
                    afterTestTypesDownloaded();
                }
            }

            @Override
            public void onFailure(Call<TestTypesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void afterTestTypesDownloaded() {

        for(TestType testType: testTypes) {
            String groupName = testType.getTestGroup();
            if(testTypesMap.containsKey(groupName)) {
                testTypesMap.get(groupName).add(testType);
            } else {
                List<TestType> list = new ArrayList();
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
            List<TestType> testTypeList = testTypesMap.get(key);
            String[][] tests = new String[testTypeList.size()][2];
            int j=0;
            for(TestType t: testTypeList) {
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
                List<SelectableTestRow> checkedTests = new ArrayList<>();
                for(ExpandableLayout l: testTypeLayouts) {
                    checkedTests.addAll(l.getSelectedSearchableTestRows());
                }
                Toast.makeText(getActivity(), "Submit", Toast.LENGTH_SHORT).show();
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
}

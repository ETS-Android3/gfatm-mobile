package com.ihsinformatics.gfatmmobile.commonlab;

import com.ihsinformatics.gfatmmobile.commonlab.network.CommonLabAPIClient;
import com.ihsinformatics.gfatmmobile.commonlab.network.HttpCodes;
import com.ihsinformatics.gfatmmobile.commonlab.network.RetrofitClientFactory;
import com.ihsinformatics.gfatmmobile.commonlab.network.Utils;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestTypesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetadataDownloadHelper {

    public static void downloadTestTypes() {
        CommonLabAPIClient apiClient = RetrofitClientFactory.createCommonLabApiClient();

        Call<TestTypesResponse> call = apiClient.fetchAllTestTypes("full", Utils.getBasicAuth());
        call.enqueue(new Callback<TestTypesResponse>() {
            @Override
            public void onResponse(Call<TestTypesResponse> call, Response<TestTypesResponse> response) {
                if(response.code() == HttpCodes.OK) {
                    TestTypesResponse testTypesResponse = response.body();

                }
            }

            @Override
            public void onFailure(Call<TestTypesResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}

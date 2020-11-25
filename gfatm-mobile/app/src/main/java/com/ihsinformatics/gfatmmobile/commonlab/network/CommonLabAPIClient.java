package com.ihsinformatics.gfatmmobile.commonlab.network;

import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.EncountersResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrdersResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestTypesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommonLabAPIClient {

    @GET("commonlab/labtestorder")
    Call<TestOrdersResponse> fetchAllTestOrders(@Query("patient") String patientUUID, @Header("Authorization") String auth);

    @GET("commonlab/labtestorder/{uuid}")
    Call<TestOrder> fetchTestOrderByUUID(@Path("uuid") String uuid, @Header("Authorization") String auth);

    @GET("commonlab/labtesttype")
    Call<TestTypesResponse> fetchAllTestTypes(@Query("v") String representation, @Header("Authorization") String auth);
///commonlab/labtestattributetype?testTypeUuid=d3854cbc-f668-47c7-8054-6a6e33f4caaa&v=full
    @GET("encounter")
    Call<EncountersResponse> fetchAllEncountersByPatient(@Query("patient") String patientUUID, @Header("Authorization") String auth);
}

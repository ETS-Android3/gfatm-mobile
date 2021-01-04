package com.ihsinformatics.gfatmmobile.commonlab.network;

import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.AttributeType;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Concept;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.Encounter;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.EncountersResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.OpenMRSResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrder;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestOrdersResponse;
import com.ihsinformatics.gfatmmobile.commonlab.network.gsonmodels.TestTypesResponse;
import com.ihsinformatics.gfatmmobile.medication.gson_pojos.DrugOrder;

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
    Call<TestOrdersResponse> fetchAllTestOrders(@Query("v") String representation, @Query("patient") String patientUUID, @Header("Authorization") String auth);

    @GET("commonlab/labtestorder/{uuid}")
    Call<TestOrder> fetchTestOrderByUUID(@Path("uuid") String uuid, @Header("Authorization") String auth);

    @GET("commonlab/labtesttype")
    Call<TestTypesResponse> fetchAllTestTypes(@Query("v") String representation, @Header("Authorization") String auth);

    @GET("commonlab/labtestattributetype")
    Call<OpenMRSResponse<AttributeType>> fetchAttributeTypes(@Query("v") String representation, @Query("testTypeUuid") String testTypeUuid, @Header("Authorization") String auth);

    @GET("encounter")
    Call<OpenMRSResponse<Encounter>> fetchAllEncountersByPatient(@Query("patient") String patientUUID, @Header("Authorization") String auth);

    @GET("concept/{uuid}")
    Call<Concept> fetchConcept(@Path("uuid") String uuid, @Header("Authorization") String auth);

    @GET("order?patient={uuid}&t=drugorder&v=custom:(uuid,orderNumber,action,patient:(uuid,display),careSetting:(uuid,display),previousOrder,dateActivated,dateStopped,autoExpireDate,encounter:(uuid,display),orderer:(uuid,display),orderReason:(uuid,display),orderReasonNonCoded,instructions,drug:(uuid,display),dose,doseUnits:(uuid,display),frequency:(uuid,display),quantity,quantityUnits:(uuid,display),duration,durationUnits:(uuid,display),route:(uuid,display))")
    Call<OpenMRSResponse<DrugOrder>> fetchDrugOrdersByPatientUUID(@Path("uuid") String uuid, @Header("Authorization") String auth);
}

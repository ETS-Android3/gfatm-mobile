package com.ihsinformatics.gfatmmobile.commonlab.network;

import retrofit2.Call;
import retrofit2.Response;

public interface CustomCallback<T> {
    public void onResponse(Call<T> call, Response<T> response, long requestID);
    public void onFailure(Call<T> call, Throwable t, long requestID);
}

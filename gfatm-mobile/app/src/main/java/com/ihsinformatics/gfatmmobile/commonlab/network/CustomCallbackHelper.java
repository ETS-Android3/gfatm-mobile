package com.ihsinformatics.gfatmmobile.commonlab.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomCallbackHelper<T> implements Callback<T> {

    private long requestId;
    private CustomCallback<T> callback;

    public  CustomCallbackHelper(CustomCallback<T> callback, long requestId) {
        this.callback = callback;
        this.requestId = requestId;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        callback.onResponse(call, response, requestId);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        callback.onFailure(call, t, requestId);
    }



}

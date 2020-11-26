package com.ihsinformatics.gfatmmobile.commonlab.network;

import com.ihsinformatics.gfatmmobile.App;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientFactory {

    private static Retrofit retrofit;


    private static synchronized Retrofit getRetrofitInstance() {
        String http;
        if (App.getSsl().equalsIgnoreCase("Enabled"))
            http = "https://";
        else
            http = "http://";
        String url = http + App.getIp()+":"+ App.getPort() + "/openmrs/ws/rest/v1/";
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static CommonLabAPIClient createCommonLabApiClient() {
        return getRetrofitInstance().create(CommonLabAPIClient.class);
    }
}

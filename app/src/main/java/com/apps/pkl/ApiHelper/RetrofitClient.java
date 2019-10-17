package com.apps.pkl.ApiHelper;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitAdd = null;
    private static Retrofit retrofitImg = null;

    public static Retrofit getClient(String baseUrl){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getClientAdd(String baseUrlAdd){
        HttpLoggingInterceptor interceptorAdd = new HttpLoggingInterceptor();
        interceptorAdd.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient clientAdd = new OkHttpClient.Builder().addInterceptor(interceptorAdd).build();

        if (retrofitAdd == null){
            retrofitAdd = new Retrofit.Builder()
                    .baseUrl(baseUrlAdd)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientAdd)
                    .build();
        }
        return retrofitAdd;
    }

}

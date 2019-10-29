package com.apps.pkl.ApiHelper;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static Retrofit retrofitAdd = null;
    private static Retrofit retrofitgetPrimer = null;
    private static Retrofit retrofitgetCek = null;

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
    public static Retrofit getClientPrimer(String baseUrlPrimer){
        HttpLoggingInterceptor interceptorPrimer = new HttpLoggingInterceptor();
        interceptorPrimer.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient clientprimer = new OkHttpClient.Builder().addInterceptor(interceptorPrimer).build();

        if (retrofitgetPrimer == null){
            retrofitgetPrimer = new Retrofit.Builder()
                    .baseUrl(baseUrlPrimer)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientprimer)
                    .build();
        }
        return retrofitgetPrimer;
    }
    public static Retrofit getClientCek(String baseUrlCek){
        HttpLoggingInterceptor interceptorCek = new HttpLoggingInterceptor();
        interceptorCek.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient clientCek = new OkHttpClient.Builder().addInterceptor(interceptorCek).build();
        if (retrofitgetCek == null){
            retrofitgetCek = new Retrofit.Builder()
                    .baseUrl(baseUrlCek)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientCek)
                    .build();
        }
        return retrofitgetCek;
    }

}

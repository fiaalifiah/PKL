package com.apps.pkl.ApiHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BaseApiService {
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("username") String username,
                                           @Field("password") String password);

    @FormUrlEncoded
    @POST("addtag.php")
    Call<ResponseBody> addTag(@Field("lat") String lat,
                          @Field("lng") String lng,
                          @Field("kabel") String kabel,
                          @Field("core") String core,
                          @Field("des") String des);
}

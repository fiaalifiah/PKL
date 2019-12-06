package com.apps.pkl.ApiHelper;

import okhttp3.MultipartBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BaseApiAdd {
    @FormUrlEncoded
    @POST("addtag.php")
    Call<ResponseBody> addTag(@Field("lat") Double lat,
                              @Field("lng") Double lng,
                              @Field("kabel") String kabel,
                              @Field("core") String core,
                              @Field("des") String des,
                              @Field("telkomA") Double telkoma,
                              @Field("telkomB") Double telkomb);

}

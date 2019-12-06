package com.apps.pkl.ApiHelper;

public class UtilsApi {
    public static final String BASE_URL_API = "https://telkom-pkl.000webhostapp.com/api/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
    public static BaseApiAdd getAPIAdd(){
        return RetrofitClient.getClientAdd(BASE_URL_API).create(BaseApiAdd.class);
    }
    public static BaseApiService getAPIPrimer(){
        return RetrofitClient.getClientPrimer(BASE_URL_API).create(BaseApiService.class);
    }
    public static BaseApiService getAPICek(){
        return RetrofitClient.getClientCek(BASE_URL_API).create(BaseApiService.class);
    }
    public static BaseApiService getAPITerkini(){
        return RetrofitClient.getClientTerkini(BASE_URL_API).create(BaseApiService.class);
    }
}

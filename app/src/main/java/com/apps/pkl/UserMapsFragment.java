package com.apps.pkl;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.apps.pkl.ApiHelper.BaseApiService;
import com.apps.pkl.ApiHelper.UtilsApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    FragmentManager FM;
    FragmentTransaction FT;
//    public static final String URL = "http://telkom-pkl.000webhostapp.com/api/";
//    private List<Result> results = new ArrayList<>();
    BaseApiService mApi,mApiCek;
    String title;
    LatLng center, latLng;
    ViewStub stub;
    FrameLayout ll;


    public UserMapsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mapsuser, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_homeuser);
        mapFragment.getMapAsync(this);
        mApi=UtilsApi.getAPIPrimer();
        mApiCek = UtilsApi.getAPICek();

        stub = (ViewStub) rootView.findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.viewflipper);
        stub.inflate();

        ll = rootView.findViewById(R.id.fram);

        ViewFlipper simpleViewFlipper = (ViewFlipper) rootView.findViewById(R.id.simpleViewFlipper);
        simpleViewFlipper.setFlipInterval(5000);
        simpleViewFlipper.setAutoStart(true);
        simpleViewFlipper.startFlipping();
        loadData();
        cekCacti();


    return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //icon
//        BitmapDrawable bitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_rto);
//        Bitmap bt=bitmap.getBitmap();
//        Bitmap icon = Bitmap.createScaledBitmap(bt, 50, 50, false);
//
//        LatLng rto = new LatLng(-7.801482, 110.3676471);
//        mMap.addMarker(new MarkerOptions().position(rto).title("Lokasi gangguan").icon(BitmapDescriptorFactory.fromBitmap(icon)));
        LatLng defaultlat = new LatLng(-7.801482, 110.3676471);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultlat,9.9f));
    }
    public void loadData(){
        mApi.view().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_tower);
                    Bitmap b=bitmapdraw.getBitmap();
                    Bitmap tower = Bitmap.createScaledBitmap(b, 50, 50, false);
                    try {
                        JSONObject jObj = new JSONObject(response.body().string());
                        String getObject = jObj.getString("result");
                        JSONArray jsonArray = new JSONArray(getObject);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            title = jsonObject.getString("Nama");
                            latLng = new LatLng(Double.parseDouble(jsonObject.getString("Lat")), Double.parseDouble(jsonObject.getString("Lng")));
                            mMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromBitmap(tower)));
                        }
                        Toast.makeText(getContext(), "Loaded", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void cekCacti(){
        mApi.cek().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jObj = new JSONObject(response.body().string());
                        if (jObj.getString("rto").equals("1")){
                            ll.removeAllViews();
                            ll.addView(LayoutInflater.from(getContext()).inflate(R.layout.respond_rto, ll, false));
                        }
                        if (jObj.getString("rto").equals("0")){
                            looping(1000);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(),"error :"+t,Toast.LENGTH_LONG);
            }
        });
    }
    public void looping(int miliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                cekCacti();
            }
        };
        handler.postDelayed(runnable,miliseconds);
    }
}

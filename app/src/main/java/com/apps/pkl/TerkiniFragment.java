package com.apps.pkl;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apps.pkl.ApiHelper.BaseApiService;
import com.apps.pkl.ApiHelper.UtilsApi;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TerkiniFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    BaseApiService mApi;


    public TerkiniFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_terkini, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mApi= UtilsApi.getAPITerkini();


    return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        int height = 50;
        int width = 50;
        //tower
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_tower);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap tower = Bitmap.createScaledBitmap(b, width, height, false);
        //icon
        BitmapDrawable bitmap=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_rto);
        Bitmap bt=bitmap.getBitmap();
        Bitmap icon = Bitmap.createScaledBitmap(bt, width, height, false);

        LatLng lokasi1 = new LatLng(-7.813989, 110.360533);
        mMap.addMarker(new MarkerOptions().position(lokasi1).title("Telkom Pugeran").icon(BitmapDescriptorFactory.fromBitmap(tower)));

        LatLng rto = new LatLng(-7.801482, 110.3676471);
        mMap.addMarker(new MarkerOptions().position(rto).title("Lokasi gangguan").icon(BitmapDescriptorFactory.fromBitmap(icon)));

        LatLng lokasi2 = new LatLng(-7.7867067,110.3725283);
        mMap.addMarker(new MarkerOptions().position(lokasi2).title("Telkom Central").icon(BitmapDescriptorFactory.fromBitmap(tower)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(lokasi1);
        builder.include(lokasi2);
        builder.include(rto);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,20));
    }
    public void getTerkini(){
        mApi.getTerkini().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jObj = new JSONObject(response.body().string());
                        String getObject = jObj.getString("result");


                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

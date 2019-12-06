package com.apps.pkl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.pkl.Notif.CHANNEL_1_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    FragmentManager FM;
    FragmentTransaction FT;

    BaseApiService mApi,mApiCek;
    String title, nama1, nama2;
    LatLng  latLng;
    ViewStub stub;
    RelativeLayout ll;
    Button btnRespond, btnUkur;
    private NotificationManagerCompat notificationManager;
    Bitmap tower, towerred, towergreen;

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

        notificationManager = NotificationManagerCompat.from(getContext());

        stub = (ViewStub) rootView.findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.viewflipper);
        stub.inflate();

        BitmapDrawable bitmapd=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_tower_red);
        Bitmap b1=bitmapd.getBitmap();
        towerred = Bitmap.createScaledBitmap(b1, 50, 50, false);
        BitmapDrawable bitmapdr=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_tower_hijau);
        Bitmap b2=bitmapdr.getBitmap();
        towergreen = Bitmap.createScaledBitmap(b2, 50, 50, false);

        ll = rootView.findViewById(R.id.fram);
        ViewFlipper simpleViewFlipper = (ViewFlipper) rootView.findViewById(R.id.simpleViewFlipper);
        simpleViewFlipper.setFlipInterval(5000);
        simpleViewFlipper.setAutoStart(true);
        simpleViewFlipper.startFlipping();
        loadData();


        return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng defaultlat = new LatLng(-7.801482, 110.3676471);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultlat,9.9f));
    }
    public void sendOnChannel1() {
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), UserActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("ALARM")
                .setContentText(nama1+" - "+nama2)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setContentIntent(contentIntent)
                .build();
        notificationManager.notify(1, notification);
    }
    public void loadData(){
        mApi.view().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_tower);
                    Bitmap b=bitmapdraw.getBitmap();
                    tower = Bitmap.createScaledBitmap(b, 50, 50, false);
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
                        cekCacti();
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
                        int total;
                        JSONObject jObj = new JSONObject(response.body().string());
                        if (jObj.getString("rto").equals("1")) {
                            if(jObj.getString("nami").equals("null")) {
                                ll.removeAllViews();
                                nama1 = jObj.getString("nama");
                                LatLng ll1 = new LatLng(Double.parseDouble(jObj.getString("lat")), Double.parseDouble(jObj.getString("long")));
                                nama2 = jObj.getString("name");
                                LatLng ll2 = new LatLng(Double.parseDouble(jObj.getString("tal")), Double.parseDouble(jObj.getString("ngol")));

                                View view = getLayoutInflater().inflate(R.layout.respond_rto, ll, false);
                                btnRespond = (Button) view.findViewById(R.id.btnRespond);
                                TextView txtv = (TextView) view.findViewById(R.id.txtLok12);
                                txtv.setText(nama1 + " - " + nama2);

                                btnRespond.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        respondAction();
                                    }
                                });
                                ll.addView(view);
                                sendOnChannel1();
                                mMap.clear();

                                mMap.addMarker(new MarkerOptions().position(ll1).title(nama1).icon(BitmapDescriptorFactory.fromBitmap(towerred)));
                                mMap.addMarker(new MarkerOptions().position(ll2).title(nama2).icon(BitmapDescriptorFactory.fromBitmap(towerred)));

                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(ll1);
                                builder.include(ll2);
                                LatLngBounds bounds = builder.build();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 8));
                            }else{
                                mMap.clear();
                            }
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
    public void respondAction (){
        ll.removeAllViews();
        View viewukur = getLayoutInflater().inflate(R.layout.pengukuran, ll, false);
        btnUkur= (Button) viewukur.findViewById(R.id.btnUkur);
        TextView txtv = (TextView) viewukur.findViewById(R.id.txtLok13);
        txtv.setText(nama1+" - "+nama2);
        btnUkur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FM = getActivity().getSupportFragmentManager();
                FT = FM.beginTransaction();
                FT.replace(R.id.containerView2, new AddTagFragment()).commit();
            }
        });
        ll.addView(viewukur);
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
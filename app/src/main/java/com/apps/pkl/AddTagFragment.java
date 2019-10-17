package com.apps.pkl;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.pkl.ApiHelper.BaseApiAdd;
import com.apps.pkl.ApiHelper.BaseApiService;
import com.apps.pkl.ApiHelper.UtilsApi;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class AddTagFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap2;
    EditText desInfo;
    SupportMapFragment mFragment;
    TextView txL, texLL;
    ImageView priv;
    Button submitbtn;
    ImageButton currentLoct, captImg;
    Geocoder geocode;
    List<Address> addresses = null;
    LocationManager localmanager;
    Marker selected;
    Double getLt,getLn;
    BaseApiAdd mApiAdd;
    Spinner kabel , core;

    private static final int CAMERA_REQUEST = 21 ;
    private static final int MY_CAMERA_PERMISSION_CODE = 22;
    public static final String URL = "https://telkom-pkl.000webhostapp.com/api/";
    private ProgressDialog progress;


    public AddTagFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_tag, container, false);
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);

        mFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapuser);
        mFragment.getMapAsync(this);

        txL = root.findViewById(R.id.txLoct);
        texLL = root.findViewById(R.id.ltlng);
        currentLoct= root.findViewById(R.id.getLL);
        priv=root.findViewById(R.id.imgPriv);
        desInfo=root.findViewById(R.id.desInfo);

         kabel = root.findViewById(R.id.spinnerKabel);
        String[] itemsKabel = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapterKabel = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemsKabel);
        kabel.setAdapter(adapterKabel);

        core = root.findViewById(R.id.spinnerCore);
        String[] itemCore = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapterCore = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, itemCore);
        core.setAdapter(adapterCore);

        currentLoct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localmanager = (LocationManager) getActivity().getSystemService(getContext().LOCATION_SERVICE);
                if (checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext()
                        ,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location loc = localmanager.getLastKnownLocation(localmanager.NETWORK_PROVIDER);
                onLocationChanged(loc);
            }
        });

        geocode = new Geocoder(getContext(), Locale.getDefault());

        captImg=root.findViewById(R.id.captCam);
        captImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }else{
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
        submitbtn = root.findViewById(R.id.btnSubmit);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertConfirm();
            }
        });
        return root;
    }

    public void alertConfirm(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Peringatan!");
        alertDialogBuilder
                .setMessage("Data tidak bisa diubah lagi, Yakin melanjutkan?")
                .setCancelable(false)
                .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        addTag();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void addTag(){
        mApiAdd = UtilsApi.getAPIAdd();

        //membuat progress dialog
        progress = new ProgressDialog(getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data
//        String kabe = kabel.getSelectedItem().toString();
//        String cor = core.getSelectedItem().toString();
//        String des= desInfo.getText().toString();
//        String latt= getLt.toString();
//        String lng= getLn.toString();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        BaseApiService api = retrofit.create(BaseApiService.class);
//        Call<ResponseBody> call = api.addTag(latt,lng,kabe,cor,des);
        mApiAdd.addTag(-7.813989, 8110.360533, "ABC", "7hv","Pedot brooo").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progress.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")) {
                            Toast.makeText(getContext(), "Terkirim", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), ActivityTime.class));
                        } else {
                            Toast.makeText(getContext(), "Gagal Kirim", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    progress.dismiss();
                    Toast.makeText(getContext(), "Respon Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progress.dismiss();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap2 = googleMap;
        mMap2.setMyLocationEnabled(true);

        int height = 50;
        int width = 50;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.ic_tower);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap icon = Bitmap.createScaledBitmap(b, width, height, false);

        LatLng pugeran = new LatLng(-7.813989, 110.360533);
        mMap2.addMarker(new MarkerOptions().position(pugeran).title("Telkom Pugeran").icon(BitmapDescriptorFactory.fromBitmap(icon)));

        LatLng central = new LatLng(-7.7867067,110.3725283);
        mMap2.addMarker(new MarkerOptions().position(central).title("Telkom Central").icon(BitmapDescriptorFactory.fromBitmap(icon)));

        LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
        builder2.include(pugeran);
        builder2.include(central);
        LatLngBounds bounds = builder2.build();
        mMap2.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,20));

        mMap2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (selected!=null){
                    selected.remove();
                }
                selected = mMap2.addMarker(new MarkerOptions().position(point).title("Marker terpilih"));
                texLL.setText("Lat. : "+point.latitude+"\nLng. : "+point.longitude);
                getLt=point.latitude;
                getLn=point.longitude;
                try{
                    addresses=geocode.getFromLocation(point.latitude,point.longitude,1);
                    String address = addresses.get(0).getAddressLine(0);
                    txL.setText(""+address);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        texLL.setText("Lat. : "+lat+"\nLng. : "+lng);
        if(selected!=null){
            selected.remove();
        }
        try{
            addresses=geocode.getFromLocation(lat,lng,1);
            String address = addresses.get(0).getAddressLine(0);
            txL.setText(""+address);
            selected = mMap2.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Lokasi Terkini"));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            priv.setImageBitmap(photo);
        }
    }
}

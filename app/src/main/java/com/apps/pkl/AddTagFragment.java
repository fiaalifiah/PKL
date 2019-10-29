package com.apps.pkl;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    Bitmap photo;
    Button submitbtn;
    ImageButton currentLoct, captImg;
    Geocoder geocode;
    List<Address> addresses = null;
    LocationManager localmanager;
    Marker selected;
    public static Double getLt, getLn;
    BaseApiAdd mApiAdd;
    boolean check = true;
    Spinner kabel, core;
    Uri filePath;

    String ImageName = "image_name" ;
    String ImagePath = "image_path" ;
    String ImageUpload ="https://telkom-pkl.000webhostapp.com/api/uploadImg.php" ;
    private static final int CAMERA_REQUEST = 21;
    private static final int MY_CAMERA_PERMISSION_CODE = 22;
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
        currentLoct = root.findViewById(R.id.getLL);
        priv = root.findViewById(R.id.imgPriv);
        desInfo = root.findViewById(R.id.desInfo);

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
                if (checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && checkSelfPermission(getContext()
                        , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 21);
                }
                Location loc = localmanager.getLastKnownLocation(localmanager.NETWORK_PROVIDER);
                onLocationChanged(loc);
            }
        });

        geocode = new Geocoder(getContext(), Locale.getDefault());

        captImg = root.findViewById(R.id.captCam);
        captImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
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

    public void alertConfirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Peringatan!");
        alertDialogBuilder
                .setMessage("Data tidak bisa diubah lagi, Yakin melanjutkan?")
                .setCancelable(false)

                .setPositiveButton("Iya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        startActivity(new Intent(getActivity(), ActivityTime.class));

                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addTag();

                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
//    public String getPath(Uri uri) {
//        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getActivity().getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }

    public void addTag() {
        mApiAdd = UtilsApi.getAPIAdd();

//        ImageUpload();
        //membuat progress dialog
        progress = new ProgressDialog(getContext());
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();

        //mengambil data

//        final String path = getPath(filePath);
        String kabe = kabel.getSelectedItem().toString();
        String cor = core.getSelectedItem().toString();
        String des = desInfo.getText().toString();

        mApiAdd.addTag( getLt, getLn, kabe, cor, des).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    progress.dismiss();
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("error").equals("false")) {
                            Toast.makeText(getContext(), "Terkirim", Toast.LENGTH_SHORT).show();
//                            try {
//                                String uploadId = UUID.randomUUID().toString();
//
//                                //Creating a multi part request
//                                new MultipartUploadRequest(getContext(), uploadId, "https://telkom-pkl.000webhostapp.com/api/addtag.php")
//                                        .addFileToUpload(path, "image") //Adding file
//                                        .setNotificationConfig(new UploadNotificationConfig())
//                                        .setMaxRetries(2)
//                                        .startUpload();
//                            } catch (Exception exc) {
//                                Toast.makeText( getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
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
                } else {
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
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_tower);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap icon = Bitmap.createScaledBitmap(b, width, height, false);

        LatLng pugeran = new LatLng(-7.813989, 110.360533);
        mMap2.addMarker(new MarkerOptions().position(pugeran).title("Telkom Pugeran").icon(BitmapDescriptorFactory.fromBitmap(icon)));

        LatLng central = new LatLng(-7.7867067, 110.3725283);
        mMap2.addMarker(new MarkerOptions().position(central).title("Telkom Central").icon(BitmapDescriptorFactory.fromBitmap(icon)));

        LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
        builder2.include(pugeran);
        builder2.include(central);
        LatLngBounds bounds = builder2.build();
        mMap2.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));

        mMap2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (selected != null) {
                    selected.remove();
                }
                selected = mMap2.addMarker(new MarkerOptions().position(point).title("Marker terpilih"));
                texLL.setText("Lat. : " + point.latitude + "\nLng. : " + point.longitude);
                getLt = point.latitude;
                getLn = point.longitude;
                try {
                    addresses = geocode.getFromLocation(point.latitude, point.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    txL.setText("" + address);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        getLt = location.getLatitude();
        getLn = location.getLongitude();
        texLL.setText("Lat. : " + getLt + "\nLng. : " + getLn);
        if (selected != null) {
            selected.remove();
        }
        try {
            addresses = geocode.getFromLocation(getLt, getLn, 1);
            String address = addresses.get(0).getAddressLine(0);
            txL.setText("" + address);
            selected = mMap2.addMarker(new MarkerOptions().position(new LatLng(getLt, getLn)).title("Lokasi Terkini"));
        } catch (IOException e) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        filePath = data.getData();
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            priv.setImageBitmap(photo);
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
    }

    public void ImageUpload(){
        ByteArrayOutputStream byteArrayOutputStreamObject ;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);
        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                progress = ProgressDialog.show(getContext(),"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
//                progress.dismiss();
                Log.e("debug", "onFailure: ERROR > " + string1);
                Toast.makeText(getContext(),string1,Toast.LENGTH_LONG).show();
                priv.setImageResource(android.R.color.transparent);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put(ImageName, "Sebelum");
                HashMapParams.put(ImagePath, ConvertImage);
                String FinalData = imageProcessClass.ImageHttpRequest(ImageUpload, HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }
    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
        }

    }


}
}
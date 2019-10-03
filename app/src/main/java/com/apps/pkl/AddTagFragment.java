package com.apps.pkl;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import androidx.fragment.app.Fragment;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class AddTagFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap2;
    SupportMapFragment mFragment;
    TextView txL, texLL, txlll;
    ImageButton currentLoct;
    Geocoder geocode;
    List<Address> addresses = null;

    public AddTagFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_tag, container, false);

        mFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapuser);
        mFragment.getMapAsync(this);

        txL = root.findViewById(R.id.txLoct);
        texLL = root.findViewById(R.id.ltlng);
        txlll = root.findViewById(R.id.txLot);
        currentLoct= root.findViewById(R.id.getLL);

        currentLoct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager localmanager;
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
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap2 = googleMap;
        mMap2.setMyLocationEnabled(true);
        LatLng pugeran = new LatLng(-7.813989, 110.360533);
        mMap2.addMarker(new MarkerOptions().position(pugeran).title("Telkom Pugeran"));

        LatLng central = new LatLng(-7.7867067,110.3725283);
        mMap2.addMarker(new MarkerOptions().position(central).title("Telkom Central"));

        LatLngBounds.Builder builder2 = new LatLngBounds.Builder();
        builder2.include(pugeran);
        builder2.include(central);
        LatLngBounds bounds = builder2.build();
        mMap2.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,20));

        mMap2.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap2.clear();
                mMap2.addMarker(new MarkerOptions().position(point).title(""+point));
                texLL.setText("Lat. : "+point.latitude+"\nLng. : "+point.longitude);
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
        txlll.setText(lat+"lat . ."+lng);
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
}

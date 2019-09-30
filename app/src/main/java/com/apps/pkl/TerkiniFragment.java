package com.apps.pkl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import androidx.fragment.app.Fragment;



/**
 * A simple {@link Fragment} subclass.
 */
public class TerkiniFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList markerPoints= new ArrayList();
    SupportMapFragment mapFragment;


    public TerkiniFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_terkini, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    return rootView;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng wates = new LatLng(-7.872954, 110.1440916);
        mMap.addMarker(new MarkerOptions().position(wates).title("Telkom Wates"));

        LatLng central = new LatLng(-7.7866644,110.304687);
        mMap.addMarker(new MarkerOptions().position(central).title("Telkom Central"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(wates);
        builder.include(central);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }
}

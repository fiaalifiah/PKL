package com.apps.pkl;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.gms.maps.SupportMapFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataKaryawanFragment extends Fragment {


    public DataKaryawanFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_datakaryawan, container, false);
        return rootView;
    }
    public void pindahactivity(){
        loadfrag(new DetailKaryawan() );
    }
    public void loadfrag(Fragment fr){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerView, new DetailKaryawan()).commit();
    }
}

package com.apps.pkl;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserHomeFragment extends Fragment {

    public  static TabLayout tabLayout2;
    public  static ViewPager viewPager2;
    public  static int int_items2= 2;



    public UserHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_user_home,null);
        tabLayout2=(TabLayout)v.findViewById(R.id.tabs2);
        viewPager2=(ViewPager)v.findViewById(R.id.viewpager2);
        //set an adpater

        viewPager2.setAdapter(new UserMyAdapter( getChildFragmentManager()));

        tabLayout2.post(new Runnable() {
            @Override
            public void run() {
                tabLayout2.setupWithViewPager(viewPager2);
            }
        });
        return v;
    }

}

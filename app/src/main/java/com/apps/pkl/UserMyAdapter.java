package com.apps.pkl;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import static com.apps.pkl.UserHomeFragment.int_items2;

/**
 * Created by Admin on 3/1/2017.
 */

public class UserMyAdapter  extends FragmentPagerAdapter {


    public UserMyAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AddTagFragment();
            case 1:
                return new SemuaFragment();

        }
        return null;
    }

    @Override
    public int getCount() {


        return int_items2;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Tag Lokasi";
            case 1:
                return "Rating Karayawan";
        }
        return null;
    }
}

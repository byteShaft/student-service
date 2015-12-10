package com.byteshaft.studentservice.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new ViewMessageFragment();
            case 1:
                // Games fragment activity
                return new AskDoctorFragment();
            case 2:
                // Movies fragment activity
                return new ViewAnnouncementFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

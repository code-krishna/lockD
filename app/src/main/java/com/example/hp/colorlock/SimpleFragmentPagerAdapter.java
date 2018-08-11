package com.example.hp.colorlock;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AYUSH on 27-03-2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context=context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MyNetworksFragment();
        } else{
            return new Joined_networks_fragment();
        }

    }
    @Override
    public String getPageTitle(int position) {
        // Generate title based on item position
        if (position == 0) {
            return "My networks";
        } else {
            return "Joined networks";

        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}


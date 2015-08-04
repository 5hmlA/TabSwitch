package com.yun.tabswitch.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yun.tabswitch.fragment.FragmentFactory;


public class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        return FragmentFactory.createFragment(index);
    }

    @Override
    public int getCount() {
        return 5;
    }

}

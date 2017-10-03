package com.example.administrator.myclass.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/3.
 */

public class MyViewPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList=new ArrayList<>();


    public MyViewPagerFragmentAdapter(FragmentManager fm, List<Fragment> mList) {
        super(fm);
        this.mFragmentList=mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList !=null ? mFragmentList.size(): 0;
    }



}

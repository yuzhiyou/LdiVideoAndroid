package com.ldi.android.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ldi.android.Activitys.Fragments.GuideFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/21.
 */
public class GuidePageAdapter extends FragmentPagerAdapter {

    private List<GuideFragment> mFragments = new ArrayList<>();

    public GuidePageAdapter(FragmentManager fm, List<GuideFragment> mFragments) {
        super(fm);
        this.mFragments=mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

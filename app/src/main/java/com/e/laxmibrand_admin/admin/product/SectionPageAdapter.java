package com.e.laxmibrand_admin.admin.product;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentStatePagerAdapter { // FragmentPagerAdapter {

    // This array list will gonna add the fragment one after another
    private final List<Fragment> mFragmentList = new ArrayList<>();
    // This list of type string is for the title of the tab
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
     //   Fragment fragment = mFragmentList.get(i);
        Fragment fragment = new DemoObjectFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putString(DemoObjectFragment.ARG_OBJECT, getPageTitle(i).toString() );
        fragment.setArguments(args);
       // return fragment;
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
            return "Category " + (position + 1);

    }

    @Override
    public int getCount() {
        return 20;
    }

}

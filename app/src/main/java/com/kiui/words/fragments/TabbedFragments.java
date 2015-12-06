package com.kiui.words.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiui.words.R;

import java.util.Locale;

/**
 * Created by ying on 06/04/2015.
 */
public class TabbedFragments extends Fragment {

    SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String TAG = TabbedFragments.class.getSimpleName();

    ViewPager mViewPager;

    public static TabbedFragments newInstance() {
        return new TabbedFragments();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //obtienes la view que contiene el layout de un tab
        View v = inflater.inflate(R.layout.tab_layout, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        //obtienes el viewpager "pager" del layout tab
        mViewPager = (ViewPager) v.findViewById(R.id.pager);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        return v;
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position) {
                case 0:
                    fragment = new Home();
                    break;
                case 1:
                    fragment = new Play();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.home).toUpperCase(l);
                case 1:
                    return getString(R.string.play).toUpperCase(l);
            }
            return null;
        }
    }
}


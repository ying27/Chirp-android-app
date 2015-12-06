package com.kiui.words.fragments.help;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiui.words.R;
import com.kiui.words.fragments.TabbedFragments;

import java.util.Locale;

public class TabbedHelp  extends android.support.v4.app.Fragment {

    SectionsPagerAdapter mSectionsPagerAdapter;

    public static final String TAG = TabbedFragments.class.getSimpleName();

    ViewPager mViewPager;

    public static TabbedHelp newInstance() {
        return new TabbedHelp();
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
        public android.support.v4.app.Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position) {
                case 0:
                    fragment = new LanguageHelpFragment();
                    break;
                case 1:
                    fragment = new WordsHelpFragment();
                    break;

                case 2:
                    fragment = new TranslationHelpFragment();
                    break;

                case 3:
                    fragment = new ImportExportFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "LANGUAGES";
                case 1:
                    return "WORDS";
                case 2:
                    return "TRANSLATIONS";
                case 3:
                    return "IMPORT/EXPORT";
            }
            return null;
        }
    }
}


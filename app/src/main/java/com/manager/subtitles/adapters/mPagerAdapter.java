package com.manager.subtitles.adapters;

import android.content.Context;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.manager.subtitles.fragment.SubFragment;
import com.manager.subtitles.fragment.testfragment;
import com.manager.subtitles.model.SubModel;

import java.util.ArrayList;

public class mPagerAdapter extends FragmentPagerAdapter {
    public Context context;
    private static final String[] TAB_TITLES = new String[]{"EN", "AR", "FR"};
    private String idfile;
    public mPagerAdapter(Context context,FragmentManager fm, String idfile) {
        super(fm);
        this.context=context;
        this.idfile=idfile;
    }

    @Override
        public int getCount() {
            return 3;
        }

    @Override
    public Fragment getItem(int position) {
       SubFragment fragment = SubFragment.newInstance(idfile,TAB_TITLES[position]);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}


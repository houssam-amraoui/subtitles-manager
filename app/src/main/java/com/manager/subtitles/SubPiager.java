package com.manager.subtitles;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.manager.subtitles.adapters.mPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.manager.subtitles.sqlite.Sql;

public class SubPiager extends AppCompatActivity {
    ViewPager viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subviewer);

        String idfile = getIntent().getExtras().getString(Sql.File);

        mPagerAdapter sectionsPagerAdapter = new mPagerAdapter(this, getSupportFragmentManager(),idfile);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}

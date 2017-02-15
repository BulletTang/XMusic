package com.zixuan.xmusic.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.zixuan.xmusic.R;
import com.zixuan.xmusic.ui.netfragment.AlbumFragment;
import com.zixuan.xmusic.ui.netfragment.BillboardFragment;
import com.zixuan.xmusic.ui.netfragment.NetPlaylistFragment;
import com.zixuan.xmusic.ui.netfragment.RadioFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager tab_viewpager;
    private TabLayout remote_tabs;
    private String[] tabTitles = {"推荐专辑","歌单","电台","音乐榜"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tab_viewpager = (ViewPager) findViewById(R.id.tab_viewpager);
        remote_tabs = (TabLayout) findViewById(R.id.remote_tabs);

        setupViewPagerAndTab();

    }

    private void setupViewPagerAndTab() {

        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.addFragment(new AlbumFragment(), tabTitles[0]);
        tabViewPagerAdapter.addFragment(new NetPlaylistFragment(), tabTitles[1]);
        tabViewPagerAdapter.addFragment(new RadioFragment(), tabTitles[2]);
        tabViewPagerAdapter.addFragment(new BillboardFragment(), tabTitles[3]);

        if (tab_viewpager != null){
            tab_viewpager.setAdapter(tabViewPagerAdapter);
            tab_viewpager.setCurrentItem(0);
            tab_viewpager.setOffscreenPageLimit(3);
        }

        if(remote_tabs != null && tab_viewpager != null){
            remote_tabs.setupWithViewPager(tab_viewpager);
        }

    }


    static class TabViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        TabViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

    }

}

package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.xinlan.imclient.R;
import com.xinlan.imclient.fragment.ContactsFragment;
import com.xinlan.imclient.fragment.MainTabFragment;
import com.xinlan.imclient.fragment.RecentListFragment;
import com.xinlan.imclient.fragment.SettingFragment;
import com.xinlan.imclient.widget.BottomIndicator;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

import java.util.ArrayList;
import java.util.List;

/**
 *  app main view
 */
public class MainActivity extends TActivity {

    public static void start(Activity context) {
        Intent it = new Intent(context, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(it);
    }

    protected ViewPager mMainViewPager;

    protected RecentListFragment mRecentFragment;
    protected ContactsFragment mContactsFragment;
    protected SettingFragment mSettingFragment;
    protected MainAdapter mMainAdapter;

    private List<MainTabFragment> mFrags = new ArrayList<MainTabFragment>(4);
    protected BottomIndicator mBottomIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();
        initAdapter();
        initActions();
    }

    protected void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void initView(){
        mMainViewPager = findViewById(R.id.main_view_pager);
    }

    public void initAdapter(){
        mRecentFragment = new RecentListFragment();
        mContactsFragment = new ContactsFragment();
        mSettingFragment = new SettingFragment();

        mFrags.add(mRecentFragment);
        mFrags.add(mContactsFragment);
        mFrags.add(mSettingFragment);

        mMainAdapter = new MainAdapter(getSupportFragmentManager());
        mMainViewPager.setAdapter(mMainAdapter);
        mMainViewPager.setOffscreenPageLimit(3);
    }

    public void initActions(){
        mBottomIndicator = BottomIndicator.create((ViewGroup) findViewById(R.id.bottom_container));
        mBottomIndicator.installView();
        mBottomIndicator.setPageView(mMainViewPager);

        mBottomIndicator.select(0);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("on Destory");
    }

    @Override
    public void onReceivedMsg(Bean bean) {
    }

    private final class MainAdapter extends FragmentPagerAdapter{
        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            return mFrags.get(index);
        }

        @Override
        public int getCount() {
            return mFrags.size();
        }
    }//end inner class

}// end class

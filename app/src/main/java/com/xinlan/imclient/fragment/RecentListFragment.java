package com.xinlan.imclient.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;

/**
 * 最近联系人
 *
 */
public class RecentListFragment extends MainTabFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recent_list , container , false);
        return rootView;
    }

}//end class

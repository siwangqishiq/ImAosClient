package com.xinlan.imclient.activity;

import android.os.Bundle;

import com.xinlan.imclient.R;

public class MainActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    void onReceivedMsg() {

    }
}

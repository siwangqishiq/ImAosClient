package com.xinlan.imclient.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;

/**
 *
 */
public class WelcomeActivity extends TActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


    }

    private void skipToMain(Intent it) {

    }

    @Override
    void onReceivedMsg(Bean bean) {
    }
}//end class

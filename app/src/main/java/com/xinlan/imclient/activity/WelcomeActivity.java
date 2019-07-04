package com.xinlan.imclient.activity;

import android.content.Intent;
import android.os.Bundle;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.core.TActivity;

/**
 *
 */
public class WelcomeActivity extends TActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(IMClient.getInstance().isLogin()){
            skipToMain(null);
        }else{
            skipLogin();
        }
    }

    private void skipToMain(Intent it) {
    }

    /**
     * 跳转至登录页
     */
    private void skipLogin(){
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void onReceivedMsg(Bean bean) {
    }
}//end class

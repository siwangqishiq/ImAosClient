package com.xinlan.imclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class TActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivedMsg(Bean bean){
        LogUtil.log("get bean " + bean);
        onReceivedMsg(bean);
    }

    abstract void onReceivedMsg(Bean bean);
}//end class

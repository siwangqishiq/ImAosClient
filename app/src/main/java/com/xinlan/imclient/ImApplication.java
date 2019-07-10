package com.xinlan.imclient;

import android.app.Application;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.xinlan.imclient.activity.WelcomeActivity;
import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.util.ProcessUtil;

public class ImApplication extends MultiDexApplication {
    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        IMClient.Options option = new IMClient.Options();
        option.enterClass = WelcomeActivity.class;
        option.iconId = R.drawable.ic_launcher;

        IMClient.getInstance().init(this , option);
        if(ProcessUtil.isMainProcess(this)){
            initApplication();
        }
    }

    private void initApplication(){
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}//end class

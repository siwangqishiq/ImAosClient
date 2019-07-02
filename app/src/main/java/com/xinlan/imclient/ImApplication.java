package com.xinlan.imclient;

import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;
import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.util.ProcessUtil;

public class ImApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        IMClient.getInstance().init(this);
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

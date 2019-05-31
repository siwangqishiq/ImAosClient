package com.xinlan.imclient;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.xinlan.imsdk.core.CoreService;
import com.xinlan.imsdk.util.ProcessUtil;

public class ImApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        if(ProcessUtil.isMainProcess(this)){//主进程
            startCoreService();
        }else{ // core 进程
        }
    }

    private void startCoreService(){
        Intent it = new Intent(this, CoreService.class);
        startService(it);
    }
}//end class

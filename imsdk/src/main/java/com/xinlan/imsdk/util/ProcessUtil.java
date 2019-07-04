package com.xinlan.imsdk.util;

import android.app.ActivityManager;
import android.content.Context;

import com.xinlan.imsdk.core.CoreService;

import java.util.ArrayList;

public class ProcessUtil {
    public static final String CORE_SERVICE_NAME = CoreService.class.getName();

    public static boolean isMainProcess(Context ctx){
        return  ctx.getPackageName().equals(getCurrentProcessName(ctx));
    }

    public static String getCurrentProcessName(Context ctx) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }

    /**
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if ("".equals(ServiceName) || ServiceName == null)
            return false;

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) activityManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

}// end class

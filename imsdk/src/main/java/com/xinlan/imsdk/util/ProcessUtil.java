package com.xinlan.imsdk.util;

import android.app.ActivityManager;
import android.content.Context;

public class ProcessUtil {
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
}// end class

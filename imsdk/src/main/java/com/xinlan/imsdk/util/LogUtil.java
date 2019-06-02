package com.xinlan.imsdk.util;

import android.util.Log;

public final class LogUtil {
    public static boolean isDebug = true;
    private static final String TAG = "imclient";

    public static void log(final String msg){
        if(isDebug){
            Log.i(TAG , msg);
        }
    }

    public static void loge(final String msg){
        if(isDebug){
            Log.e(TAG , msg);
        }
    }
}

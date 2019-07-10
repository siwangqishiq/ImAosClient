package com.xinlan.imclient.util;

import android.content.Context;

import com.xinlan.imclient.ImApplication;
import com.xinlan.imclient.widget.YXWToast;

public class ToastUtil {
    
    private static Context checkContext(Context ctx) {
        if (ctx == null) {
            ctx = ImApplication.application;
        }
        return ctx.getApplicationContext();
    }
    
    public static void show(final String msg) {
        show(null, msg);
    }

    public static void show(Context ctx, final int resId) {
        final Context context = checkContext(ctx);
        show(context, context.getString(resId));
    }

    public static void show(Context ctx, final String msg) {
        final Context context = checkContext(ctx);
        YXWToast.getInstance().showShort(msg, context);
    }
    
}
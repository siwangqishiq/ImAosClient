package com.xinlan.imclient.widget;

import android.content.Context;
import android.widget.Toast;


public class YXWToast {
    private static final String TAG = "YXWToast";
    
    private static YXWToast instance;
    public static MToast toast;
    private final Object lock = new Object();

    public static YXWToast getInstance() {
        if (instance == null)
            instance = new YXWToast();
        return instance;
    }

    public void showLong(String content, Context context) {
        show(content, Toast.LENGTH_LONG, context);

    }

    public void showLong(int rid, Context context) {
        show(context.getString(rid), Toast.LENGTH_LONG, context);
    }


    public void showShort(String content, Context context) {
        show(content, Toast.LENGTH_SHORT, context);
    }

    public void showShort(int rid, Context context) {
        show(context.getString(rid), Toast.LENGTH_SHORT, context);
    }


    private void show(String content, int time, Context context) {

        try {
            synchronized (lock) {
                stop();
                toast = MToast.makeText(context, content, time);
                toast.show();
            }
        } catch (Exception e) {
        }
    }

    private void stop() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

}

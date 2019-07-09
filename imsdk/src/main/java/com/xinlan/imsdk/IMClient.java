package com.xinlan.imsdk;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.xinlan.imsdk.core.CoreService;
import com.xinlan.imsdk.http.HttpClient;
import com.xinlan.imsdk.util.LogUtil;
import com.xinlan.imsdk.util.ProcessUtil;

import org.greenrobot.eventbus.EventBus;

public class IMClient {
    private static IMClient instance;

    private RemoteConListener mConListener;
    private Messenger mSerivceMessenger;
    private Messenger mUIMessenger;
    private RemoteHandler mActivityHandler;
    private String account;


    public static class Options{
        public Class<? extends Activity> enterClass;
        public int iconId;
    }

    private Options options;
    private IMClient(){
        initClient();
    }

    protected void initClient(){
        mActivityHandler = new RemoteHandler();
        mConListener = new RemoteConListener();
    }

    public static IMClient getInstance(){
        if(instance == null){
            synchronized (IMClient.class){
                if(instance == null){
                    instance = new IMClient();
                }
            }
        }
        return instance;
    }

    public Options getOptions(){
        return options;
    }

    public void init(final Application ctx , Options op){
        options = op;
        if(ProcessUtil.isMainProcess(ctx)){//主UI进程
            startCoreService(ctx);

            ctx.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    HttpClient.removeCallback(activity , null);
                }
            });
        }else{ // core 进程

        }
    }

    public void startCoreService(Context context){
        if(ProcessUtil.isServiceRunning(context , CoreService.NAME))
            return;

        if(!isLogin())//未登录
            return;

        Intent it = new Intent(context, CoreService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(it);
        }else{
            context.startService(it);
        }
        // bind
        context.bindService(it , mConListener , Context.BIND_AUTO_CREATE);
    }

    private class RemoteHandler extends Handler{
        public RemoteHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            // System.out.println("ui client  received msg " + msg);
            LogUtil.log(msg.toString());
            Bean bean = new Bean();
            Bundle bundle = (Bundle) msg.obj;
            bean.content = bundle.getString("content");
            EventBus.getDefault().post(bean);
        }
    }

    private class RemoteConListener implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            LogUtil.log("RemoteConListener onServiceConnected " + name);
            mSerivceMessenger = new Messenger(binder);
            buidRemoteConnection();
        }

        private void buidRemoteConnection(){
            if(mSerivceMessenger == null)
                return;

            mUIMessenger = new Messenger(mActivityHandler);

            Message buildRemoteMsg = new Message();
            buildRemoteMsg.what = CoreService.REMOTE_MESSAGE_BIND;
            buildRemoteMsg.replyTo = mUIMessenger;
            try {
                mSerivceMessenger.send(buildRemoteMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.log("RemoteConListener onServiceDisconnected " + name);
            mUIMessenger = null;
            mSerivceMessenger = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            LogUtil.log("RemoteConListener onBindingDied " + name);
        }

        @Override
        public void onNullBinding(ComponentName name) {
            LogUtil.log("RemoteConListener onNullBinding " + name);
        }
    }

    public boolean isLogin(){
        return false;
    }

    public String getAccount(){
        return account;
    }
} // end class

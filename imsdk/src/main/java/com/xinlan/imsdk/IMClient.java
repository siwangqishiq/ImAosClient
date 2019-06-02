package com.xinlan.imsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.xinlan.imsdk.core.CoreService;
import com.xinlan.imsdk.core.Remote;
import com.xinlan.imsdk.util.LogUtil;
import com.xinlan.imsdk.util.ProcessUtil;

import org.greenrobot.eventbus.EventBus;

public class IMClient {
    static {
        Remote r = new Remote();
    }
    private static IMClient instance;

    private  RemoteConListener mConListener;
    private Messenger mSerivceMessenger;
    private RemoteHandler mActivityHandler;
    private Messenger mActivityMessenger;

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

    public void init(final Context ctx){
        if(ProcessUtil.isMainProcess(ctx)){//主UI进程
            startCoreService(ctx);
        }else{ // core 进程

        }
    }

    private void startCoreService(Context context){
        Intent it = new Intent(context, CoreService.class);
        context.startService(it);
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

            mActivityMessenger = new Messenger(mActivityHandler);

            Message buildRemoteMsg = new Message();
            buildRemoteMsg.what = CoreService.REMOTE_MESSAGE_BIND;
            buildRemoteMsg.replyTo = mActivityMessenger;
            try {
                mSerivceMessenger.send(buildRemoteMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.log("RemoteConListener onServiceDisconnected " + name);
            mActivityMessenger = null;
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
} // end class

package com.xinlan.imsdk.core;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.xinlan.imsdk.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoreService extends Service {
    public static final int REMOTE_MESSAGE_BIND = 1;

    public static ExecutorService mThreadPool = Executors.newFixedThreadPool(1);

    private boolean isRun = false;

    private Messenger mMessenger;
    private Messenger mActivityMessenger;
    private Handler mUiHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            System.out.println("service received " + msg);
            switch (msg.what){
                case REMOTE_MESSAGE_BIND:
                    mActivityMessenger = msg.replyTo;
                    break;
            }//end switch
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.log("core service create");
        mMessenger = new Messenger(mUiHandler);
        isRun = true;

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                coreMainLoop();
            }
        });
    }

    private void coreMainLoop() {
        Socket socket = null;
        try {
            socket = new Socket(Config.IM_SERVER, Config.IM_PORT);
            byte[] buf = new byte[1024];
            while (isRun) {
                try {
                    LogUtil.log("core service main loop run");

                    OutputStream output = socket.getOutputStream();
                    byte[] askData = Policy.TIME_SERVER_ASK.getBytes(Charset.defaultCharset());
                    output.write(askData, 0, askData.length);

                    InputStream input = socket.getInputStream();
                    final int len = input.read(buf);
                    String str = new String(buf, 0, len);
                    System.out.println("get Ack = " + str);
                    sendRemoteMsg(1, 10, str);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } // end while
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("core service onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger == null ? null : mMessenger.getBinder();
    }

    public void sendRemoteMsg(int what, int action, String content) {
        // Remote remote = new Remote(what, action, content.getBytes());
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("content" , content);

        msg.what = what;
        msg.obj = bundle;
        try {
            mActivityMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            LogUtil.loge(e.toString());
        }
    }

    @Override
    public void onDestroy() {
        isRun = false;
    }
}

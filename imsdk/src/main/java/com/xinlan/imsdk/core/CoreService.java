package com.xinlan.imsdk.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;

import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.core.receiver.CoreServiceAuxBroadcast;
import com.xinlan.imsdk.util.LogUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoreService extends Service {
    public static final String NAME = CoreService.class.getName();

    public static final int NOTIFICATION_ID = "CoreService".hashCode();

    public static final int REMOTE_MESSAGE_BIND = 1;

    public static ExecutorService mThreadPool = Executors.newFixedThreadPool(1);

    private boolean isRun = false;

    private Messenger mMessenger;
    private Messenger mUIMessenger;
    private Handler mUiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LogUtil.log("service received " + msg);
            //System.out.println("service received " + msg);
            switch (msg.what) {
                case REMOTE_MESSAGE_BIND:
                    mUIMessenger = msg.replyTo;
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

    /**
     * 展示常驻图标
     */
    private void showNotification() {
        String channelId = "corechannelId";
        IMClient.Options options = IMClient.getInstance().getOptions();
        Notification.Builder builder = null;
        Intent notificationIntent = new Intent(this, options.enterClass);
        PendingIntent pendIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, channelId);
            setNotificationChannel(channelId);
        }
        builder.setSmallIcon(options.iconId).setContentText("Hello World!").setOngoing(true)
                .setContentTitle("聊天服务").setContentIntent(pendIntent);
        startForeground(NOTIFICATION_ID, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setNotificationChannel(String channelId) {
        final NotificationChannel channel = new NotificationChannel(channelId, "channel",
                NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.createNotificationChannel(channel);
    }

    int count = 0;
    private void coreMainLoop() {
        while (isRun) {
            LogUtil.log("core service is running ... " + count);
            LogUtil.log("" + IMClient.getInstance().isLogin()+"  " +IMClient.getInstance().getUid());
            count++;
            try {
                Thread.sleep(2000);
                for (int i = 0; i < 10000; i++) {
                    double a = Math.sin(i);
                    double b = Math.sin(i + 1);
                    double c = a + b;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }//end while
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("core service onStartCommand");
        showNotification();
        return START_STICKY;
    }

//    private void coreMainLoop() {
//        Socket socket = null;
//        try {
//            socket = new Socket(Config.IM_SERVER, Config.IM_PORT);
//            byte[] buf = new byte[1024];
//            while (isRun) {
//                try {
//                    LogUtil.log("core service main loop run");
//
//                    OutputStream output = socket.getOutputStream();
//                    byte[] askData = Policy.TIME_SERVER_ASK.getBytes(Charset.defaultCharset());
//                    output.write(askData, 0, askData.length);
//
//                    InputStream input = socket.getInputStream();
//                    final int len = input.read(buf);
//                    String str = new String(buf, 0, len);
//                    System.out.println("get Ack = " + str);
//                    sendRemoteMsg(1, 10, str);
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } // end while
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (socket != null) {
//                try {
//                    socket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger == null ? null : mMessenger.getBinder();
    }

    public void sendRemoteMsg(int what, int action, String content) {
        // Remote remote = new Remote(what, action, content.getBytes());
        Message msg = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);

        msg.what = what;
        msg.obj = bundle;
        try {
            mUIMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            LogUtil.loge(e.toString());
        }
    }

    @Override
    public void onDestroy() {
        isRun = false;
        LogUtil.log("core service has destoryed");
        Intent it = new Intent(CoreServiceAuxBroadcast.ACTION);
        it.setPackage(getPackageName());
        sendBroadcast(it);
    }
}

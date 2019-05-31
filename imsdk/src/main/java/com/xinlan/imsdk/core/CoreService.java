package com.xinlan.imsdk.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xinlan.imsdk.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CoreService extends Service {
    public static ExecutorService mThreadPool = Executors.newFixedThreadPool(1);

    private boolean isRun = false;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.log("core service create");
        isRun = true;

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                coreMainLoop();
            }
        });
    }

    private void coreMainLoop() {
            while (isRun) {
                try {
                    Socket socket = new Socket(Config.IM_SERVER, Config.IM_PORT);
                    LogUtil.log("core service main loop run");
//                    socket.getOutputStream().write("Hi Server".getBytes());
//                    socket.getOutputStream().flush();
                    InputStreamReader inputReader = new InputStreamReader(socket.getInputStream());
                    BufferedReader bufReader = new BufferedReader(inputReader);

                    System.out.println("get Ack = " + bufReader.readLine());
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
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
        return null;
    }

    @Override
    public void onDestroy() {
        isRun = false;
    }
}

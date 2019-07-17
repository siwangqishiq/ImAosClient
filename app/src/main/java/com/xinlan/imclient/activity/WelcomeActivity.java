package com.xinlan.imclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.xinlan.imclient.R;
import com.xinlan.imclient.widget.UserAccount;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.core.TActivity;
import com.xinlan.imsdk.http.HttpClient;

/**
 *
 */
public class WelcomeActivity extends TActivity {
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if(IMClient.getInstance().isLogin() && UserAccount.instance.isValidate()){
            autoLogin();
        }else{
            skipLogin();
        }
    }

    /**
     * 触发自动登录
     */
    private void autoLogin(){
        HttpClient.sendPostRequest("autologin" , null , this , new HttpClient.ICallback<String>(){
            @Override
            public void onError(int errorCode, Exception e) {
                skipLogin();
            }

            @Override
            public void onSuccess(String token) {
                UserAccount.sharedInstance().saveToken(token);

                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        skipToMain(null);
                    }
                },2000);
            }
        } , String.class);
    }

    private void skipToMain(Intent it) {
        MainActivity.start(this);
        finish();
    }

    /**
     * 跳转至登录页
     */
    private void skipLogin(){
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void onReceivedMsg(Bean bean) {
    }
}//end class

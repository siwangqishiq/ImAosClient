package com.xinlan.imclient.widget;

import android.text.TextUtils;

import com.xinlan.imclient.model.User;
import com.xinlan.imsdk.IMClient;

public class UserAccount {
    private UserAccount(){
    }

    public String token;
    public String uid;

    public int sex;
    public String avatar;
    public User rawUser;

    public static UserAccount instance = new UserAccount();
    public static UserAccount sharedInstance(){
        return instance;
    }

    public void initAccount(){
        User user = AppPreferences.readAccount();
        if(user != null){
            uid = String.valueOf(user.getUid());
            sex = user.getSex();
            avatar = user.getAvatar();
            rawUser = user;

            token = AppPreferences.getToken();
        }
    }

    public void setValue(User user){
        uid = String.valueOf(user.getUid());
        sex = user.getSex();
        avatar = user.getAvatar();
        token = user.getToken();

        IMClient.getInstance().setToken(token);
        AppPreferences.saveAccount(user);
        AppPreferences.saveToken(token);
    }

    public void saveToken(final String token){
        this.token = token;
        IMClient.getInstance().setToken(token);
        AppPreferences.saveToken(token);
    }

    public void clearValue(){
        uid = null;
        sex = -1;
        avatar = null;
        token =null;
        rawUser = null;
        AppPreferences.saveAccount(null);
        AppPreferences.saveToken(null);
    }

    public boolean isValidate(){
        return !TextUtils.isEmpty(token) && !TextUtils.isEmpty(uid);
    }
}

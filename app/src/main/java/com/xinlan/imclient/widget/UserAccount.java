package com.xinlan.imclient.widget;

import com.xinlan.imclient.model.User;

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
            rawUser = user;

            token = user.getToken();
            uid = String.valueOf(user.getUid());
            sex = user.getSex();
            avatar = user.getAvatar();
        }
    }

    public void setValue(User user){
        uid = String.valueOf(user.getUid());
        sex = user.getSex();
        avatar = user.getAvatar();
        token = user.getToken();

        AppPreferences.saveAccount(user);
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
}

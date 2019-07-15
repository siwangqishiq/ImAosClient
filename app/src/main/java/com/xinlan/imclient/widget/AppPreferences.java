package com.xinlan.imclient.widget;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xinlan.imclient.model.User;

public class AppPreferences {
    public static final String SP_ACCOUNT_INFO = "_account_info";
    public static final String SP_AUTH_TOKEN = "_auth_token";

    public static void saveAccount(User user){
        if(user == null){
            SharePreUtil.saveString(SP_ACCOUNT_INFO , null);
        }else{
            SharePreUtil.saveString(SP_ACCOUNT_INFO , JSONObject.toJSONString(user));
        }
    }

    public static User readAccount(){
        try{
            String rawStr = SharePreUtil.getString(SP_ACCOUNT_INFO);
            if(TextUtils.isEmpty(rawStr))
                return null;
            return JSON.parseObject(rawStr , User.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void saveToken(String token){
        SharePreUtil.accountSaveString(SP_AUTH_TOKEN , token);
    }

    public static String getToken(){
        if(!TextUtils.isEmpty(UserAccount.sharedInstance().token))
            return UserAccount.sharedInstance().token;

        return SharePreUtil.getAccountString(SP_AUTH_TOKEN);
    }
}

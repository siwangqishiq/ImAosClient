package com.xinlan.imclient.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public final class SharePreUtil {
    private static SharedPreferences sharedPreferences;

    private static final String DIV = "#";

    public static void initSp(Context ctx){
        sharedPreferences =  ctx.getSharedPreferences(ctx.getPackageName() , Context.MODE_PRIVATE);
    }

    public static boolean accountSaveString(String key , String value){
        final String curAccount = UserAccount.sharedInstance().uid;
        if(TextUtils.isEmpty(curAccount))
            return false;
        return sharedPreferences.edit().putString(curAccount + DIV + key , value).commit();
    }

    public static String getAccountString(String key){
        final String curAccount = UserAccount.sharedInstance().uid;
        if(TextUtils.isEmpty(curAccount))
            return null;
        return sharedPreferences.getString(curAccount + DIV + key , null);
    }

    public static boolean accountSaveInt(String key , int value){
        final String curAccount = UserAccount.sharedInstance().uid;
        if(TextUtils.isEmpty(curAccount))
            return false;
        return sharedPreferences.edit().putInt(curAccount + DIV + key , value).commit();
    }

    public static boolean accountSaveBool(String key , boolean value){
        final String curAccount = UserAccount.sharedInstance().uid;
        if(TextUtils.isEmpty(curAccount))
            return false;
        return sharedPreferences.edit().putBoolean(curAccount + DIV + key , value).commit();
    }

    public static boolean saveString(String key , String value){
        return sharedPreferences.edit().putString(key , value).commit();
    }

    public static String getString(String key){
        return sharedPreferences.getString(key , null);
    }

    public static boolean saveInt(String key , int value){
        return sharedPreferences.edit().putInt(key , value).commit();
    }

    public static boolean saveBool(String key , boolean value){
        return sharedPreferences.edit().putBoolean(key , value).commit();
    }
}// end class

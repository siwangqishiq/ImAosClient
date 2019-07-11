package com.xinlan.imclient.model;

public class User {
    public static final int SEX_FEMALE = 0; //女
    public static final int SEX_MALE = 1; //男

    private String account;
    private String avatar;
    private long uid;
    private String pwd;
    private int sex;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", avatar='" + avatar + '\'' +
                ", uid=" + uid +
                ", pwd='" + pwd + '\'' +
                ", sex=" + sex +
                '}';
    }
}

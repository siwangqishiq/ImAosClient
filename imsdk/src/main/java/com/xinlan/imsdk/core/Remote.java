package com.xinlan.imsdk.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Remote implements Serializable, Parcelable {
    public int what;
    public int action;
    public byte[] data;

    public Remote() {
    }

    public Remote(int what, int action, byte[] data) {
        this.what = what;
        this.action = action;
        this.data = data;
    }

    protected Remote(Parcel in) {
        what = in.readInt();
        action = in.readInt();
        in.readByteArray(data);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(what);
        dest.writeInt(action);
        dest.writeByteArray(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static  final Creator<Remote> CREATOR = new Creator<Remote>() {
        @Override
        public Remote createFromParcel(Parcel in) {
            return new Remote(in);
        }

        @Override
        public Remote[] newArray(int size) {
            return new Remote[size];
        }
    };

    @Override
    public String toString() {
        return "Remote{" +
                "what=" + what +
                ", action=" + action +
                ", data=" + data +
                '}';
    }
}//end class

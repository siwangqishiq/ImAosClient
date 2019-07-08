package com.xinlan.imsdk.http;

public interface IUpload {
    interface Callback{
        void onSuccess(final String uplodaUrl , final String filepath);
        void onError(int code,Exception e);
    }

    
}

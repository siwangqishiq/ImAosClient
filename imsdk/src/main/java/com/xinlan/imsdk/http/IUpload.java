package com.xinlan.imsdk.http;

import java.io.File;

public interface IUpload {
    interface Callback{
        void onSuccess(final String uplodaUrl , final String filepath);
        void onError(int code, final String filepath);
    }

    interface UpdateProgress{
        void onUpdate(String filepath  , long current , long total);
    }

    void uploadImage(File imageFile , Callback callback);

    void observerUploadProgress(UpdateProgress progressCallback , boolean register);
}//end class

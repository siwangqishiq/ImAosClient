package com.xinlan.imsdk.http;

import android.text.TextUtils;

import com.upyun.library.common.ParallelUploader;
import com.upyun.library.common.Params;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadImpl implements IUpload {

    private static final String SPACE = "sqimage";
    private static final String OPERATER = "luluxiu";
    private static final String PASSWORD = "PD6fckKA6CfP5QI6213L43hiHjNNQqBF";

    private static final String IMG_URL = "http://res.panyi.xyz/";

    private ParallelUploader parallelUploader;
    private UpProgressListener listener;
    private List<UpdateProgress> updateListeners = new ArrayList<UpdateProgress>(2);
    private Map<String, String> imgParams;

    public UploadImpl() {
        parallelUploader = new ParallelUploader(SPACE, OPERATER, UpYunUtils.md5(PASSWORD));
    }

    @Override
    public void uploadImage(final File imageFile, final Callback callback) {
        if (imageFile == null)
            return;

        final String fileUrl = createCloudFileUrl(imageFile);

        Map<String, String> params = new HashMap<>();
        String extension = findFileSuffix(imageFile.getName());
        if(!TextUtils.isEmpty(extension)){
            params.put(Params.CONTENT_TYPE , extension);
        }
        parallelUploader.upload(imageFile, createCloudFileUrl(imageFile), params, new UpCompleteListener() {
            @Override
            public void onComplete(boolean isSuccess, String result) {
                if (callback != null) {
                    if (isSuccess) {
                        callback.onSuccess(IMG_URL + fileUrl, imageFile.getAbsolutePath());
                    } else {
                        callback.onError(-1, imageFile.getAbsolutePath());
                    }
                }//end if
            }
        });
    }


    @Override
    public void observerUploadProgress(final UpdateProgress progressCallback, boolean register) {
        if(register){
            parallelUploader.setOnProgressListener(new UpProgressListener() {
                @Override
                public void onRequestProgress(long bytesWrite, long contentLength) {
                    if(progressCallback != null){
                        progressCallback.onUpdate(null , bytesWrite , contentLength);
                    }
                }
            });
        }else{
            parallelUploader.setOnProgressListener(null);
        }
    }

    private static String createCloudFileUrl(File file) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "img_" + timeStamp + "_" + file.getName();
        return imageFileName;
    }

    public static String findFileSuffix(String filename){
        if(TextUtils.isEmpty(filename))
            return null;

        String extension = null;
        int index = filename.lastIndexOf('.');
        if (index> 0) {
            extension = filename.substring(index+1);
        }
        return extension;
    }
}//end class

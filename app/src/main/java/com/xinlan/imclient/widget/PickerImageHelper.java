package com.xinlan.imclient.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import com.xinlan.imclient.R;
import com.xinlan.imclient.activity.SelectAblumActivity;
import com.xinlan.imclient.config.RequestCode;
import com.xinlan.imclient.ui.CustomDialog;
import com.xinlan.imclient.util.FileUtils;
import com.xinlan.imsdk.http.HttpClient;
import com.xinlan.imsdk.http.IUpload;
import com.xinlan.imsdk.ui.UrlImageView;

import java.io.File;
import java.io.IOException;

/**
 * 选取图片 从相册 或 相机拍摄
 */
public class PickerImageHelper {
    private static final String TAG = PickerImageHelper.class.getSimpleName();
    private FragmentActivity mContext;
    private String mTakePhotoFilePath;

    private UrlImageView mUrlImageView;
    private ProgressBar mLoadingView;

    public PickerImageHelper(FragmentActivity ctx) {
        mContext = ctx;
        mUrlImageView = mContext.findViewById(R.id.avatar);
        mLoadingView = mContext.findViewById(R.id.loading_view);
    }

    public void selectAvatar() {
        final CustomDialog dialog = new CustomDialog();
        dialog.addItem(mContext.getString(R.string.picker_from_ablum), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerFromAblum();
            }
        });
        dialog.addItem(mContext.getString(R.string.picker_from_camera), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerFromCamera();
            }
        });
        dialog.show(mContext.getSupportFragmentManager(), TAG);
    }

    public void pickerFromCamera() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, RequestCode.PERMISSION_TAKE_PHOTO);
        }
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = FileUtils.createImageFile(mContext);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        "com.xinlan.imclient.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mTakePhotoFilePath = photoFile.getAbsolutePath();
                mContext.startActivityForResult(takePictureIntent, RequestCode.RESULT_TAKE_PHOTO);
            }
        } catch (IOException ex) {
        }
    }

    /**
     * 打开相册
     */
    private void openAblum(){
        SelectAblumActivity.startForResult(mContext , RequestCode.RESULT_OPEN_ABLUM);
    }

    public void pickerFromAblum() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    RequestCode.PERMISSION_SELCT_ABLUM);
        }else{
            openAblum();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == RequestCode.PERMISSION_TAKE_PHOTO && grantAllPermissions(grantResults) ){
            takePhoto();
        }else if(requestCode == RequestCode.PERMISSION_SELCT_ABLUM && grantAllPermissions(grantResults)){
            openAblum();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == RequestCode.RESULT_TAKE_PHOTO  && !TextUtils.isEmpty(mTakePhotoFilePath)){//拍摄照片返回
            File file = new File(mTakePhotoFilePath);
            //System.out.println("file size = " +file.length() +"   " +file.getAbsolutePath());

            if(mLoadingView != null){
                mLoadingView.setVisibility(View.VISIBLE);
            }
            HttpClient.getUploader().uploadImage(file , new IUpload.Callback(){
                @Override
                public void onSuccess(String uplodaUrl, String filepath) {
                    //System.out.println("上传成功 = " +uplodaUrl);
                    if(mUrlImageView != null){
                        mUrlImageView.loadImageThumb(uplodaUrl, new UrlImageView.ILoadListener() {
                            @Override
                            public void onSuccess() {
                                if(mLoadingView != null){
                                    mLoadingView.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailed() {
                                if(mLoadingView != null){
                                    mLoadingView.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }

                @Override
                public void onError(int code, String filepath) {
                    //System.out.println("失败 = " +filepath);
                    if(mLoadingView != null){
                        mLoadingView.setVisibility(View.GONE);
                    }
                }
            });

            HttpClient.getUploader().observerUploadProgress(new IUpload.UpdateProgress(){
                @Override
                public void onUpdate(String filepath, long current, long total) {
                    //System.out.println(current + " / " + total);
                }
            },true);
        }
    }

    private static boolean grantAllPermissions(int[] grantResults){
        if(grantResults == null)
            return false;
        for(int result : grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }//end for each
        return true;
    }
}//end class
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
import com.xinlan.imclient.R;
import com.xinlan.imclient.config.RequestCode;
import com.xinlan.imclient.ui.CustomDialog;
import com.xinlan.imclient.util.FileUtils;
import com.xinlan.imsdk.http.HttpClient;
import com.xinlan.imsdk.http.IUpload;

import java.io.File;
import java.io.IOException;

/**
 * 选取图片 从相册 或 相机拍摄
 */
public class PickerImageHelper {
    private static final String TAG = PickerImageHelper.class.getSimpleName();
    private FragmentActivity mContext;
    private String mTakePhotoFilePath;

    public PickerImageHelper(FragmentActivity ctx) {
        mContext = ctx;
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

    public void pickerFromAblum() {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == RequestCode.PERMISSION_TAKE_PHOTO && grantAllPermissions(grantResults) ){
            takePhoto();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == RequestCode.RESULT_TAKE_PHOTO  && !TextUtils.isEmpty(mTakePhotoFilePath)){//拍摄照片返回
            File file = new File(mTakePhotoFilePath);
            System.out.println("file size = " +file.length() +"   " +file.getAbsolutePath());

            HttpClient.getUploader().uploadImage(file , new IUpload.Callback(){

                @Override
                public void onSuccess(String uplodaUrl, String filepath) {
                    System.out.println("上传成功 = " +uplodaUrl);
                }

                @Override
                public void onError(int code, String filepath) {
                    System.out.println("失败 = " +filepath);
                }
            });

            HttpClient.getUploader().observerUploadProgress(new IUpload.UpdateProgress(){
                @Override
                public void onUpdate(String filepath, long current, long total) {
                    System.out.println(current + " / " + total);
                }
            },true);
        }
    }

    /**
     * 根据文件Uri获取路径
     */
    public static String getFilePathByFileUri(Context context, Uri uri) {
        String filePath = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return filePath;
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

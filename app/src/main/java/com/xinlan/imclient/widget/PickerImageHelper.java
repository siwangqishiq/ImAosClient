package com.xinlan.imclient.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.xinlan.imclient.R;
import com.xinlan.imclient.activity.RegisterActivity;
import com.xinlan.imclient.config.RequestCode;
import com.xinlan.imclient.ui.CustomDialog;
import com.xinlan.imclient.util.FileUtils;

import java.io.File;

/**
 * 选取图片 从相册 或 相机拍摄
 */
public class PickerImageHelper {
    private static final String TAG = PickerImageHelper.class.getSimpleName();
    private FragmentActivity mContext;
    private Uri mPhotoUri;

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
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = FileUtils.genEditFile();;
        if (photoFile != null) {
            mPhotoUri = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            mContext.startActivityForResult(intent, RequestCode.RESULT_TAKE_PHOTO);
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

        if(requestCode == RequestCode.RESULT_TAKE_PHOTO && mPhotoUri != null){//拍摄照片返回
            File file = new File(mPhotoUri.getPath());
            System.out.println("file size = " +file.length() +"   " +file.getAbsolutePath());
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

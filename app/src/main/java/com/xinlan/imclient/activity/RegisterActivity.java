package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xinlan.imclient.R;
import com.xinlan.imclient.ui.CustomDialog;
import com.xinlan.imclient.widget.PickerImageHelper;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

/**
 * 注册新账号
 */
public class RegisterActivity extends TActivity implements View.OnClickListener {

    public static void start(Activity activity){
        Intent it = new Intent(activity , RegisterActivity.class);
        activity.startActivity(it);
    }

    private ImageView mAvatarView;

    private PickerImageHelper mPickerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolbar();
        initView();
    }

    protected void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.register);
    }

    private void initView(){
        mAvatarView = findViewById(R.id.avatar);
        mAvatarView.setOnClickListener(this);

        mPickerHelper = new PickerImageHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceivedMsg(Bean bean) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.avatar://选择头像
                mPickerHelper.selectAvatar();
                break;
        }//end switch
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mPickerHelper != null){
            mPickerHelper.onRequestPermissionsResult(requestCode , permissions , grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mPickerHelper != null){
            mPickerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}//end class

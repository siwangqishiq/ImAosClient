package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xinlan.imclient.R;
import com.xinlan.imclient.widget.CustomDialog;
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
    }

    private void initView(){
        mAvatarView = findViewById(R.id.avatar);
        mAvatarView.setOnClickListener(this);
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
            case R.id.avatar:
                selectAvatar();
                break;
        }//end switch
    }

    public void selectAvatar(){
        final CustomDialog dialog = new CustomDialog();
        dialog.addItem("从相册选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this,"从相册选择",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.addItem("拍摄", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this,"拍摄",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getSupportFragmentManager() , "dialog");
    }
}//end class

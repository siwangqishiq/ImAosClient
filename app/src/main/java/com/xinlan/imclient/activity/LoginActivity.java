package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

/**
 * 登录界面
 */
public class LoginActivity extends TActivity {

    public static void start(Activity activity){
        Intent it = new Intent(activity , LoginActivity.class);
        activity.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_create_new) {
            createAccountAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 注册新账号
     */
    protected void createAccountAction(){
        RegisterActivity.start(this);
    }

    @Override
    public void onReceivedMsg(Bean bean) {

    }
}//end class

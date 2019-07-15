package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.xinlan.imclient.R;
import com.xinlan.imclient.model.User;
import com.xinlan.imclient.util.ToastUtil;
import com.xinlan.imclient.widget.UserAccount;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.core.TActivity;
import com.xinlan.imsdk.http.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录界面
 */
public class LoginActivity extends TActivity implements View.OnClickListener{
    private static final String EXTRA_ACCOUNT = "account";
    private static final String EXTRA_PWD = "pwd";

    public static void start(Activity activity){
        Intent it = new Intent(activity , LoginActivity.class);
        activity.startActivity(it);
    }

    public static void start(Activity activity , String account , String pwd){
        Intent it = new Intent(activity , LoginActivity.class);
        it.putExtra(EXTRA_ACCOUNT , account);
        it.putExtra(EXTRA_PWD , pwd);
        activity.startActivity(it);
    }

    private EditText mAccountText;
    private EditText mPwdText;
    private View mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar();
        initView();
        parseIntent(getIntent());
    }

    protected void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.login);
    }

    private void initView(){
        mLoginBtn = findViewById(R.id.submit_btn);
        mPwdText = findViewById(R.id.user_password);
        mAccountText = findViewById(R.id.user_name);

        mLoginBtn.setOnClickListener(this);
    }

    protected void parseIntent(Intent intent){
        if(intent == null)
            return;

        String defAccount = intent.getStringExtra(EXTRA_ACCOUNT);
        if(!TextUtils.isEmpty(defAccount)){
            mAccountText.setText(defAccount);
        }

        String defPwd = intent.getStringExtra(EXTRA_PWD);
        if(!TextUtils.isEmpty(defPwd)){
            mPwdText.setText(defPwd);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
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

    protected void doLogin(){
        final String account = mAccountText.getText().toString().trim();
        final String pwd = mPwdText.getText().toString().trim();

        Map<String , Object> params = new HashMap<String, Object>();
        params.put("account" , account);
        params.put("pwd" , pwd);
        HttpClient.sendPostRequest("login" , params , this , new HttpClient.ICallback<User>() {
            @Override
            public void onError(int errorCode, Exception e) {
                ToastUtil.show(LoginActivity.this , e.getMessage());
            }

            @Override
            public void onSuccess(User resp) {
                onLoignSccess(resp);
            }
        }, User.class);
    }

    public void onLoignSccess(User user){
        if(user == null)
            return;

        UserAccount.sharedInstance().setValue(user);
        MainActivity.start(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_btn:
                doLogin();
                break;
        }//end switch
    }

}//end class

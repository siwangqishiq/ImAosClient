package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.xinlan.imclient.R;
import com.xinlan.imclient.model.User;
import com.xinlan.imclient.ui.CustomDialog;
import com.xinlan.imclient.util.CheckUtil;
import com.xinlan.imclient.util.ToastUtil;
import com.xinlan.imclient.widget.PickerImageHelper;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;
import com.xinlan.imsdk.http.HttpClient;
import com.xinlan.imsdk.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册新账号
 */
public class RegisterActivity extends TActivity implements View.OnClickListener {

    public static void start(Activity activity) {
        Intent it = new Intent(activity, RegisterActivity.class);
        activity.startActivity(it);
    }

    private ImageView mAvatarView;

    private PickerImageHelper mPickerHelper;

    private View mRegisterBtn;
    private EditText mUserNameText;
    private EditText mPwdText;
    private RadioButton mMaleSelctBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolbar();
        initView();
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.register);
    }

    private void initView() {
        mAvatarView = findViewById(R.id.avatar);
        mAvatarView.setOnClickListener(this);

        mRegisterBtn = findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(this);

        mUserNameText = findViewById(R.id.user_name);
        mPwdText = findViewById(R.id.user_password);
        mMaleSelctBtn = findViewById(R.id.male_item);

        mPickerHelper = new PickerImageHelper(this, R.drawable.avatar_default);
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
        switch (v.getId()) {
            case R.id.avatar://选择头像
                mPickerHelper.selectAvatar();
                break;
            case R.id.register_btn://注册
                register();
                break;
        }//end switch
    }

    private void register() {
        String account = mUserNameText.getText().toString().trim();
        String pwd = mPwdText.getText().toString().trim();
        int male = mMaleSelctBtn.isChecked() ? User.SEX_MALE : User.SEX_FEMALE;
        String avatar = mPickerHelper.getImageUrl();

        if (!validateInputs(account, pwd)) {
            return;
        }

        doRegisterUser(account, pwd, male, avatar);
    }

    private void doRegisterUser(final String account, final String pwd, final int male, final String avatar) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("pwd", pwd);
        params.put("avatar", avatar);
        params.put("male", male);

        HttpClient.sendPostRequest("createUser", params, this, new HttpClient.ICallback<User>() {
            @Override
            public void onError(int errorCode, Exception e) {
                LogUtil.loge(e.toString());
                ToastUtil.show(e.getMessage());
            }

            @Override
            public void onSuccess(User user) {
                if (user.getUid() > 0) {
                    LogUtil.log("注册成功 = " + user);
                    ToastUtil.show(RegisterActivity.this , R.string.register_success);
                    LoginActivity.start(RegisterActivity.this ,user.getAccount() , pwd);
                    RegisterActivity.this.finish();
                }
            }
        }, User.class);
    }

    private boolean validateInputs(String account, String pwd) {
        if (TextUtils.isEmpty(account)) {
            ToastUtil.show(this, R.string.error_account_empty);
            return false;
        }

        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show(this, R.string.error_pwd_empty);
            return false;
        }

        if (!CheckUtil.checkAccountMark(account)) {
            ToastUtil.show(this, R.string.error_account_fatal);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPickerHelper != null) {
            mPickerHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPickerHelper != null) {
            mPickerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}//end class

package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xinlan.imclient.R;
import com.xinlan.imclient.widget.UserAccount;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

public class MainActivity extends TActivity {

    public static void start(Activity context) {
        Intent it = new Intent(context, MainActivity.class);
        context.startActivity(it);
    }

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.text);
        mText.setText(UserAccount.sharedInstance().uid);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    public void onReceivedMsg(Bean bean) {
        mText.setText(bean.content);
    }
}

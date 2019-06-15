package com.xinlan.imclient.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.http.HttpRequestHelper;

import java.util.HashMap;

public class MainActivity extends TActivity {
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.text);


        HashMap<String,Object> params = new HashMap<String , Object>();
        params.put("account","siwangqishiq");
        params.put("pwd","111111");
        HttpRequestHelper.sendPostRequest("createUser" , params);
    }

    @Override
    void onReceivedMsg(Bean bean) {
        mText.setText(bean.content);
    }
}

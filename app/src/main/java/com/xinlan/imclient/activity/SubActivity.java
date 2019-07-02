package com.xinlan.imclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.http.HttpRequestClient;
import com.xinlan.imsdk.model.User;

public class SubActivity extends TActivity {
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        mText = findViewById(R.id.text);

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest(){
        HttpRequestClient.sendPostRequest("getuser", null, this,
                new HttpRequestClient.ICallback<User>() {
                    @Override
                    public void onError(int errorCode, Exception e) {
                        mText.setText("Error = " +errorCode);
                    }

                    @Override
                    public void onSuccess(User user) {
                        mText.setText(user.getName());
                    }
                } , User.class);
    }

    @Override
    void onReceivedMsg(Bean bean) {
    }
}

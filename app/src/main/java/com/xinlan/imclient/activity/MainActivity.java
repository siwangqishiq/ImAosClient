package com.xinlan.imclient.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;

public class MainActivity extends TActivity {
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.text);
    }

    @Override
    void onReceivedMsg(Bean bean) {
        mText.setText(bean.content);
    }
}

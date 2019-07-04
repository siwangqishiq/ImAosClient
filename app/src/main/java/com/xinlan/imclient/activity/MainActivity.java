package com.xinlan.imclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xinlan.imclient.R;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

public class MainActivity extends TActivity {
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mText = findViewById(R.id.text);

        findViewById(R.id.test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it  = new Intent(MainActivity.this , SubTestActivity.class);
                MainActivity.this.startActivity(it);
            }
        });
    }

    @Override
    public void onReceivedMsg(Bean bean) {
        mText.setText(bean.content);
    }
}

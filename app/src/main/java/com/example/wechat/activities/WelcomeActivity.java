package com.example.wechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.wechat.R;

public class WelcomeActivity extends BaseActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },5000);
    }
}

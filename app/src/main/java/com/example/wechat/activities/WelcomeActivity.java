package com.example.wechat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
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

                SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
                String token = prefs.getString("token", "");
                if(token.equals("")){
                    Intent intent = new Intent(WelcomeActivity.this, StartPageActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },5000);
    }
}

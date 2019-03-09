package com.example.wechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class StartPageActivity extends AppCompatActivity {

    private Button alreadyAccount;
    private Button needAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        alreadyAccount=findViewById(R.id.already_account);
        needAccount=findViewById(R.id.need_account);
    }
}

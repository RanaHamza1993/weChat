package com.example.wechat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wechat.R;

public class StartPageActivity extends BaseActivity {

    private Button alreadyAccount;
    private Button needAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        alreadyAccount=findViewById(R.id.already_account);
        needAccount=findViewById(R.id.need_account);

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartPageActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        needAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(StartPageActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

     //   showInfoMessage("Hello");
    }
}

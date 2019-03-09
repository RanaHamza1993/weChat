package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    private Toolbar toolbar;
    FirebaseAuth mAuth;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar=findViewById(R.id.login_toolbar);
        mAuth=FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_pwd);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String em=email.getText().toString();
                String pwd=password.getText().toString();
                loginUser(em,pwd);
            }
        });
    }

    private void loginUser(String em, String pwd) {

        if(em.isEmpty()) {
            showErrorMessage("Invalid Email");
            return;
        }
        if(pwd.isEmpty())
            showErrorMessage("Invalid Password");
        else{
            showDialog("Loging into your account","Please wait while e are logging into your account");
            mAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        showErrorMessage("Invalid Email or passowrd");
                    }
                    dismissDialog();
                }
            });
        }
    }
}

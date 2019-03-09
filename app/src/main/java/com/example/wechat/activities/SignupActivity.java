package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
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

public class SignupActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button register;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loadingBar=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Signup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        name = findViewById(R.id.name);
        password = findViewById(R.id.pwd);
        email = findViewById(R.id.email);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nm = name.getText().toString();
                String pwd = password.getText().toString();
                String em = email.getText().toString();
                RegisterAccount(nm, em, pwd);
            }
        });
    }

    private void RegisterAccount(String name, String email, String password) {

        if (name.isEmpty())
            showErrorMessage("Please enter valid name");
           // Toasty.error(this, "Please enter valid name", Toast.LENGTH_LONG, true).show();

            if (email.isEmpty())
                showErrorMessage("Please enter valid email");
                //Toasty.error(this, "Please enter valid email", Toast.LENGTH_LONG, true).show();

                if (password.isEmpty())
                    showErrorMessage("Please enter valid password");
                   // Toasty.error(this, "Please enter valid password", Toast.LENGTH_LONG, true).show();
                else{
                    showDialog("Creating new Account","wait, while we are creating account for you");
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                               // Toasty.success(SignupActivity.this, "Register Successfully", Toast.LENGTH_LONG, true).show();

                                showSuccessMessage("Register Successfully");
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else{
                               // Toasty.error(SignupActivity.this,"Not register successfully",Toast.LENGTH_SHORT,true).show();
                            showErrorMessage("Not register successfully");
                            }
                            dismissDialog();
                        }
                    });
                }


    }
}

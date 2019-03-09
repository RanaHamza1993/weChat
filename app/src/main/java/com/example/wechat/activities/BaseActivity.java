package com.example.wechat.activities;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    void showSuccessMessage(String message){
        Toasty.success(this,message, Toast.LENGTH_SHORT,true).show();
    }
    void showInfoMessage(String message){
        Toasty.info(this,message, Toast.LENGTH_SHORT,true).show();
    }
    void showErrorMessage(String message){
        Toasty.error(this,message, Toast.LENGTH_SHORT,true).show();
    }
    void showDialog(String title,String message){
        loadingBar=new ProgressDialog(this);
        loadingBar.setTitle(title);
        loadingBar.setMessage(message);
        loadingBar.show();
    }
    void dismissDialog(){
        if(loadingBar.isShowing())
            loadingBar.dismiss();
    }
}

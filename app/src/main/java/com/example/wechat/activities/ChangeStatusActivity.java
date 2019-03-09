package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeStatusActivity extends BaseActivity {

    FirebaseAuth mAuth;
    DatabaseReference dbReference;

    private Toolbar toolbar;
    private EditText status;
    private Button publishStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);
        toolbar=findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();
        dbReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        status=findViewById(R.id.status_input);
        publishStatus=findViewById(R.id.change_status_btn);
        status.setText(getIntent().getExtras().get("user_status").toString());


        publishStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stat=status.getText().toString();
                updateStatus(stat);
            }
        });


    }

    private void updateStatus(String stat) {
        if(stat.isEmpty())
            showErrorMessage("Please Enter a valid status");
        else{
            showDialog("Status Changing","Please wait while updating status");

            dbReference.child("user_status").setValue(stat).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    dismissDialog();
                    onBackPressed();
                }
            });
        }
    }
}

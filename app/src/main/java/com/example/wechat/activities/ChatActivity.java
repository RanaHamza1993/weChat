package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.utils.LastSeenTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends BaseActivity {

    private Toolbar toolbar;
    private DatabaseReference userDataReference;
    private FirebaseAuth mAuth;
    String receiver_id;
    String receiver_name;
    TextView userName;
    TextView lastSeen;
    CircleImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiver_id=getIntent().getExtras().get("receiver_id").toString();
        receiver_name=getIntent().getExtras().get("receiver_name").toString();
        showInfoMessage(receiver_name);
        showInfoMessage(receiver_id);

        toolbar=findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Chats");

        mAuth=FirebaseAuth.getInstance();
        userDataReference= FirebaseDatabase.getInstance().getReference().child("Users").child(receiver_id);

        ActionBar actionBar=getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View acbarview=layoutInflater.inflate(R.layout.chat_bar,null);
        actionBar.setCustomView(acbarview);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        userName=findViewById(R.id.display_name);
        lastSeen=findViewById(R.id.last_seen);
        userImage=findViewById(R.id.chat_profile_pic);
        userName.setText(receiver_name);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("isOnline").getValue().toString().equals("Online")) {
                    lastSeen.setText("Online");
                }
                else {
                    LastSeenTime lastSeenTime=new LastSeenTime();
                    Long last_seen=Long.parseLong(dataSnapshot.child("isOnline").getValue().toString());
                    String lasteSeen=lastSeenTime.getTimeAgo(last_seen);
                    lastSeen.setText(lasteSeen);
                }
                if(dataSnapshot.child("user_thumb_image").getValue().toString().equals("default_profile"))
                    Glide.with(getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(userImage);
                else
                    Glide.with(getApplicationContext()).load(dataSnapshot.child("user_thumb_image").getValue().toString()).placeholder(R.drawable.default_profile).into(userImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

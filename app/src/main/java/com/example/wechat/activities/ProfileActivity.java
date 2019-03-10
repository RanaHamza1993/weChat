package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView name;
    private TextView status;
    private Button sendRequest;
    private Button declineRequest;
    private DatabaseReference userDataReference;
    private DatabaseReference firendRequestReference;
    private DatabaseReference firendsference;
    private DatabaseReference notificationReference;
    private FirebaseAuth mAuth;
    Map userMap=new HashMap();
    String receiver_uid="";
    String muid="";
    String currentState;
    private CircleImageView userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentState="not_friends";
        mAuth=FirebaseAuth.getInstance();
        receiver_uid=getIntent().getExtras().get("uid").toString();
        muid=mAuth.getCurrentUser().getUid();
        userDataReference= FirebaseDatabase.getInstance().getReference().child("Users").child(receiver_uid);
        firendRequestReference=FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        notificationReference=FirebaseDatabase.getInstance().getReference().child("Notifications");
        firendRequestReference.keepSynced(true);
        notificationReference.keepSynced(true);
        firendsference=FirebaseDatabase.getInstance().getReference().child("Friends");
        firendsference.keepSynced(true);
        toolbar=findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        userProfile=findViewById(R.id.profile_image);
        name=findViewById(R.id.user_name);
        status=findViewById(R.id.user_status);
        sendRequest=findViewById(R.id.send_request);
        declineRequest=findViewById(R.id.decline_request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                name.setText(dataSnapshot.child("user_name").getValue().toString());
                status.setText(dataSnapshot.child("user_status").getValue().toString());
                if(dataSnapshot.child("user_thumb_image").getValue().toString().equals("default_profile"))
                    Glide.with(getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(userProfile);
                else
                    Glide.with(getApplicationContext()).load(dataSnapshot.child("user_thumb_image").getValue().toString()).placeholder(R.drawable.default_profile).into(userProfile);


                firendRequestReference.child(muid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(receiver_uid)) {
                                String req_type = dataSnapshot.child(receiver_uid).child("request_type").getValue()
                                        .toString();

                                if (req_type.equals("sent")) {
                                    currentState = "request_sent";
                                    sendRequest.setText("Cancel Request");

                                } else if (req_type.equals("received")) {
                                    currentState = "request_received";
                                    sendRequest.setText("Accept this Request");
                                    declineRequest.setVisibility(View.VISIBLE);
                                }


                            }

                        else{
                            firendsference.child(muid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(dataSnapshot.hasChild(receiver_uid)){

                                            currentState="friends";
                                            sendRequest.setText("UnFriend this Person");

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(muid.equals(receiver_uid)) {
            sendRequest.setVisibility(View.GONE);
            declineRequest.setVisibility(View.GONE);
        }
        else {
            sendRequest.setVisibility(View.VISIBLE);
            declineRequest.setVisibility(View.VISIBLE);
        }


        declineRequest.setVisibility(View.GONE);
            sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendRequest.setEnabled(false);


                    if (currentState.equals("not_friends")) {

                        sendFriendRequest();
                    }
                    if (currentState.equals("request_sent")) {

                        cancelFriendRequest();
                    }
                    if (currentState.equals("request_received")) {
                        acceptFriendRequest();
                    }
                    if (currentState.equals("friends")) {
                        sendRequest.setEnabled(true);
                        unFriend();
                    }

            }
        });

            declineRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    declineFriendRequest();
                }
            });
    }

    private void declineFriendRequest() {
        firendRequestReference.child(muid).child(receiver_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firendRequestReference.child(receiver_uid).child(muid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        sendRequest.setText("Send Request");
                        currentState="not_friends";
                        declineRequest.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    private void unFriend() {

        firendsference.child(muid).child(receiver_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firendsference.child(receiver_uid).child(muid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        currentState="not_friends";
                        sendRequest.setText("Send Request");
                        declineRequest.setVisibility(View.GONE);
                    }

                });
            }
        });
    }



    private void acceptFriendRequest() {
        Calendar date=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveCurrentDate=currentDate.format(date.getTime());
        firendsference.child(muid).child(receiver_uid).setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firendsference.child(receiver_uid).child(muid).setValue(saveCurrentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        firendRequestReference.child(muid).child(receiver_uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    firendRequestReference.child(receiver_uid).child(muid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                sendRequest.setEnabled(true);
                                                currentState = "firends";
                                                sendRequest.setText("UnFriend this person");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void cancelFriendRequest() {

        firendRequestReference.child(muid).child(receiver_uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    firendRequestReference.child(receiver_uid).child(muid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                sendRequest.setEnabled(true);
                                currentState = "not_friends";
                                sendRequest.setText("Send Request");
                                declineRequest.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendFriendRequest() {
        firendRequestReference.child(muid).child(receiver_uid).child("request_type")
        .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    firendRequestReference.child(receiver_uid).child(muid).child("request_type")
                            .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            HashMap<String,String> notificationsMap=new HashMap<String, String>();
                            notificationsMap.put("from",muid);
                            notificationsMap.put("type","request");
                            notificationReference.child(receiver_uid).push().setValue(notificationsMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        sendRequest.setEnabled(true);
                                        currentState = "request_sent";
                                        sendRequest.setText("Cancel Request");
                                        declineRequest.setVisibility(View.GONE);
                                    }
                                }
                            });


                            }
                        }
                    });
                }
            }
        });
    }

}

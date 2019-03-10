package com.example.wechat.utils;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;

public class MyChatOfline extends Application {

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso build=builder.build();
        build.setIndicatorsEnabled(true);
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);
        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        if(currentUser!=null){

            String mid=mAuth.getCurrentUser().getUid();
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Users")
            .child(mid);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    databaseReference.child("isOnline").onDisconnect().setValue(false);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{

        }
    }

}

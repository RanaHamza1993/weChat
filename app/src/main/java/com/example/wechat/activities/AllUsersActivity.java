package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.model.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    RecyclerView allusersRecycler;
    DatabaseReference dbreference;
   FirebaseRecyclerAdapter<UserModel,UsersViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        toolbar=findViewById(R.id.allusers_toolbar);
       // mAuth= FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        dbreference= FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        allusersRecycler=findViewById(R.id.allusers_recycler);
        allusersRecycler.setLayoutManager(new LinearLayoutManager(this));
      //  allusersRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();


        Query query=dbreference.child("Users");
        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)
                        .build();
       firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<UserModel, UsersViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull UserModel userModel) {

                usersViewHolder.userName.setText(userModel.getUser_name());
                usersViewHolder.userStatus.setText(userModel.getUser_status());
                if(userModel.getUser_image().equals("default_profile"))
                Glide.with(getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(usersViewHolder.userImg);
                else
                    Glide.with(getApplicationContext()).load(userModel.getUser_image()).placeholder(R.drawable.default_profile).into(usersViewHolder.userImg);


            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user_item,parent,false);
                return new UsersViewHolder(v);
            }
        };

        allusersRecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userImg;
        TextView userName;
        TextView userStatus;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg=itemView.findViewById(R.id.user_image);
            userName=itemView.findViewById(R.id.user_name);
            userStatus=itemView.findViewById(R.id.user_status);
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();

    }
}

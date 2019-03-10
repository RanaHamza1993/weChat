package com.example.wechat.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.example.wechat.activities.AllUsersActivity;
import com.example.wechat.activities.ProfileActivity;
import com.example.wechat.model.FriendsModel;
import com.example.wechat.model.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {


    private RecyclerView friendsRecycler;
    private DatabaseReference friendsReference;
    private FirebaseAuth mAuth;
    private View v;
    private String uid;
    private DatabaseReference usersReference;
    FirebaseRecyclerAdapter<FriendsModel, FriendsFragment.UsersViewHolder> firebaseRecyclerAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_friends, container, false);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        friendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(uid);
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        usersReference.keepSynced(true);
        friendsRecycler = v.findViewById(R.id.friends_recycler);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        friendsReference.keepSynced(true);
        return v;
    }


    @Override
    public void onStart() {
        super.onStart();


        Query query = friendsReference;
        FirebaseRecyclerOptions<FriendsModel> options =
                new FirebaseRecyclerOptions.Builder<FriendsModel>()
                        .setQuery(query, FriendsModel.class)
                        .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FriendsModel, FriendsFragment.UsersViewHolder>
                (options) {
            @Override
            protected void onBindViewHolder(@NonNull final FriendsFragment.UsersViewHolder usersViewHolder, final int position, @NonNull FriendsModel friendsModel) {

             //   usersViewHolder.userName.setText(userModel.getUser_name());
               usersViewHolder.userStatus.setText(friendsModel.getDate());
               String muid=getRef(position).getKey();
               usersReference.child(muid).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       usersViewHolder.userName.setText(dataSnapshot.child("user_name").getValue().toString());
                       if(dataSnapshot.child("user_thumb_image").getValue().toString().equals("default_profile"))
                           Glide.with(getActivity().getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(usersViewHolder.userImg);
                       else
                           Glide.with(getActivity().getApplicationContext()).load(dataSnapshot.child("user_thumb_image").getValue().toString()).placeholder(R.drawable.default_profile).into(usersViewHolder.userImg);


                       Boolean isOnline=(boolean) dataSnapshot.child("isOnline").getValue();

                       if(isOnline)
                           usersViewHolder.onlineStatus.setVisibility(View.VISIBLE);
                       else
                           usersViewHolder.onlineStatus.setVisibility(View.GONE);

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });
//                if (userModel.getUser_image().equals("default_profile"))
//                    Glide.with(getActivity().getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(usersViewHolder.userImg);
//                else
//                    Glide.with(getActivity().getApplicationContext()).load(userModel.getUser_thumb_image()).
//                            placeholder(R.drawable.default_profile).into(usersViewHolder.userImg);


                usersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getRef(position).getKey();
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra("uid", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public FriendsFragment.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user_item, parent, false);
                return new FriendsFragment.UsersViewHolder(v);
            }
        };

        friendsRecycler.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();

    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImg;
        TextView userName;
        TextView userStatus;
        ImageView onlineStatus;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userStatus = itemView.findViewById(R.id.user_status);
            onlineStatus=itemView.findViewById(R.id.online_status);
        }


    }
}
package com.example.wechat.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechat.R;
import com.example.wechat.activities.ChatActivity;
import com.example.wechat.activities.ProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    ProgressDialog loadingBar;
    private DatabaseReference usersReference;
    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return null;
    }

    void showSuccessMessage(String message){
        Toasty.success(getContext(),message, Toast.LENGTH_SHORT,true).show();
    }
    void showInfoMessage(String message){
        Toasty.info(getContext(),message, Toast.LENGTH_SHORT,true).show();
    }
    void showErrorMessage(String message){
        Toasty.error(getContext(),message, Toast.LENGTH_SHORT,true).show();
    }
    void showDialog(String title,String message){
        loadingBar=new ProgressDialog(getContext());
        loadingBar.setTitle(title);
        loadingBar.setMessage(message);
        loadingBar.show();
    }
    void dismissDialog(){
        if(loadingBar.isShowing())
            loadingBar.dismiss();
    }

    public void showDialog(String title, CharSequence options[], String username, String uid, DataSnapshot dataSnapshot){

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Select Options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {

                if(position==0){
                    Intent intent=new Intent(getContext(), ProfileActivity.class);
                    intent.putExtra("uid",uid);
                    startActivity(intent);
                }else if(position==1){

                    if(dataSnapshot.child("isOnline").exists()) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("receiver_id", uid);
                        intent.putExtra("receiver_name", username);
                        startActivity(intent);
                    }
                    else{
                        usersReference.child(uid).child("isOnline").setValue(ServerValue.TIMESTAMP).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if(dataSnapshot.child("isOnline").exists()) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("receiver_id", uid);
                                    intent.putExtra("receiver_name", username);
                                    startActivity(intent);
                                }

                            }
                        });
                    }

                }

            }
        });
        builder.show();
    }
}

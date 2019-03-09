package com.example.wechat.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import de.hdodenhof.circleimageview.CircleImageView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wechat.R;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SettingsActivity extends BaseActivity {

    private static final int ImageRequestCode =1 ;
    private static final int PermissionRequestCode =2 ;
    private Toolbar toolbar;
    private TextView name;
    private TextView status;
    private Button changeImage;
    private Button changeStatus;
    private DatabaseReference userDataReference;
    private FirebaseAuth mAuth;
    private StorageReference databasestorage;
    String uid="";

    private CircleImageView userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        userDataReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        databasestorage= FirebaseStorage.getInstance().getReference().child("Profile_IMages");
        toolbar=findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        userProfile=findViewById(R.id.profile_image);
        name=findViewById(R.id.user_name);
        status=findViewById(R.id.user_status);
        changeImage=findViewById(R.id.change_image);
        changeStatus=findViewById(R.id.change_status);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });
       // name.setText(userDataReference.child(uid).child("user_name").get);
        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nam=dataSnapshot.child("user_name").getValue().toString();
                String stat=dataSnapshot.child("user_status").getValue().toString();
                String img=dataSnapshot.child("user_image").getValue().toString();
                String thumbImg=dataSnapshot.child("user_thumb_image").getValue().toString();
                name.setText(nam);
                status.setText(stat);
                if(img.equals("default_profile"))
                    Glide.with(getApplicationContext()).load(R.drawable.default_profile).placeholder(R.drawable.default_profile).into(userProfile);
                else
                Glide.with(getApplicationContext()).load(img).placeholder(R.drawable.default_profile).into(userProfile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,ImageRequestCode);
            }
        });

        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(SettingsActivity.this,ChangeStatusActivity.class);
                intent.putExtra("user_status",status.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImageRequestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {


            Uri imageUri=data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                StorageReference filePath=databasestorage.child(uid+".jpg");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            showSuccessMessage("Image uploaded Successfully");
                            Task<Uri> uriTask=task.getResult().getStorage().getDownloadUrl();
                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    userDataReference.child("user_image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            showSuccessMessage("Image updated successfully");
                                        }

                                    });
                                }
                            });


                        } else{

                            showErrorMessage("Error occured while uploading");
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
//            CropImage.activity(imageUri)
//                    .start(this);

    }

    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(SettingsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

         //   Toast.makeText(MainActivity.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(SettingsActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 2:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                   // Toast.makeText(SettingsActivity.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                   // Toast.makeText(MainActivity.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}

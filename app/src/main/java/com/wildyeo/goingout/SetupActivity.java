package com.wildyeo.goingout;

//import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText Username, FullName, CountryName;
    private Button SaveInformationButton;
    private CircleImageView ProfileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private String currentUserID;
    private FirebaseStorage storageRef;
    private StorageReference UserProfileImageRef;
    //private ProgressDialog LoadingBar;
    String Tag = "SU";
    final static int galleryPic = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        mAuth = FirebaseAuth.getInstance();
        /*get user id */
        currentUserID = mAuth.getCurrentUser().getUid();
        /* get reference to cloud bucket */
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        Username = (EditText) findViewById(R.id.setup_username);
        FullName = (EditText) findViewById(R.id.setup_fullname);
        CountryName = (EditText) findViewById(R.id.setup_country_name);
        SaveInformationButton = (Button) findViewById(R.id.setup_information_button);
        ProfileImage = (CircleImageView) findViewById(R.id.setup_profile_image);
        //LoadingBar = new ProgressDialog(this);


        SaveInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAccountSetupInformation();
            }
        });

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                /* Only getting images */
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, galleryPic);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPic && resultCode==RESULT_OK && data != null)
        {

            Uri ImageUri = data.getData();
            /* show activity of imported Android Image Cropper */
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            /* Image Cropper is opened successfully */
            if(requestCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();

                /* setting reference to profile picture*/
                StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                /* put cropped image into storage */
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SetupActivity.this,"Your profile image is stored successfully to Firebase Storage", Toast.LENGTH_SHORT);

                            final String downloadUrl = task.getResult().getMetadata().getReference().getDownloadUrl().toString();

                            /*set profile's profile image */
                            UsersRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful()){

                                        Toast.makeText(SetupActivity.this, "Profile image set to profile", Toast.LENGTH_SHORT).show();
                                        
                                    }
                                    else{
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SetupActivity.this, "Error Occurred: " + message, Toast.LENGTH_LONG);
                                    }
                                }
                            });
                        }
                    }
                });


            }

            else{
                Toast.makeText(SetupActivity.this, "Error Occurred: Image could not be cropped. Please, try again.", Toast.LENGTH_LONG);

            }
        }
    }

    private void SaveAccountSetupInformation() {
        Log.v(Tag, "In SaveAccountSetUp");
        String username = Username.getText().toString();
        String fullname = FullName.getText().toString();
        String country = CountryName.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please write your username", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname)){
            Toast.makeText(this, "Please write your fullname", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(country)){
            Toast.makeText(this, "Please write your country", Toast.LENGTH_SHORT).show();
        }
        else{
            Log.v(Tag, "Creating hashmap");
            /*LoadingBar.setTitle("Saving information");
            LoadingBar.setMessage("Please wait while we are creating your new account.");
            LoadingBar.show();
            LoadingBar.setCanceledOnTouchOutside(true);*/
            HashMap userMap = new HashMap();
            userMap.put("username",username);
            userMap.put("fullname",fullname);
            userMap.put("country",country);
            userMap.put("status","Hey there I am using Going Out. Developed by Luis Solorzano.");
            userMap.put("gender","none");
            userMap.put("dob","none");
            userMap.put("relationshipstatus","none");
            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Log.v(Tag, "Updating children");
                    if(task.isSuccessful()){

                        SendUserToMainActivty();
                        Toast.makeText(SetupActivity.this, "Your account has been successfully created",Toast.LENGTH_LONG);
                        //LoadingBar.dismiss();
                        Log.v(Tag, "Task was successful");
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Error Occurred: " + message, Toast.LENGTH_LONG);
                       // LoadingBar.dismiss();
                        Log.v(Tag, "Task was unsuccessful:"+message);
                    }

                }
            });
        }




    }

    private void SendUserToMainActivty() {
        Intent setupIntent = new Intent(SetupActivity.this, MainActivity.class);
        Log.v(Tag, "Sending to main activity");
        /* add flags so that user cannot press back button without first hitting logout button */
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();
    }
}

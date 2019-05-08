package com.wildyeo.goingout;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private Button CreateAccountButton;
    private EditText UserEmail, UserPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Initialize firebase app before trying to get auth */
        FirebaseApp.initializeApp(this);


        mAuth = FirebaseAuth.getInstance();

        CreateAccountButton = (Button) findViewById(R.id.login_create_account_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        LoginButton = (Button) findViewById(R.id.login_button);

        LoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                AllowingUserToLogin();
            }
        } );
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendUserToRegisterActivity();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        /* if user is already login, then send to main activity*/
        if(currentUser != null){//user is not authenticated need to send to loginActivity
            SendUserToMainActivity();
        }


    }

    private void AllowingUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter your email...", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your password...", Toast.LENGTH_SHORT);
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(LoginActivity.this,"You are logged in successfully", Toast.LENGTH_SHORT);
                    }
                    else{
                        String message = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this,"Error occurred: " + message, Toast.LENGTH_SHORT);
                    }
                }
            });
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);

        /* add flags so that user cannot press back button without first hitting logout button */
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    // Remove verbose log
    private void SendUserToRegisterActivity()
    {

        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        String Tag = "Intent Activity";
        Log.v(Tag, "Going from Login activity to Register Activity");
        startActivity(registerIntent);

    }
}

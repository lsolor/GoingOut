package com.wildyeo.goingout;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {


    //UI references
    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;
    private ProgressDialog LoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.register_create_account_button);
        LoadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });

        mAuth.getInstance();

        }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();

        //check to see that user
        if(TextUtils.isEmpty(email)) {


            Toast.makeText(this,"Please enter your email...",Toast.LENGTH_SHORT);

        }
        else if(TextUtils.isEmpty(password)) {


            Toast.makeText(this,"Please enter your password...",Toast.LENGTH_SHORT);

        }
        else if(TextUtils.isEmpty(confirmPassword)) {


            Toast.makeText(this,"Please confirm your password...",Toast.LENGTH_SHORT);

        }

        else if(!password.equals(confirmPassword)){

            Toast.makeText(this,"Your passwords do not match.",Toast.LENGTH_SHORT);

        }
        else {
            LoadingBar.setTitle("Creating new account");
            LoadingBar.setMessage("Please wait while we are creating your new account.");
            LoadingBar.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "You are authenticated successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error occurred:" + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }







    }
}

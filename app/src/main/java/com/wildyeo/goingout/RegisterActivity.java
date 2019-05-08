package com.wildyeo.goingout;

//import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {


    //UI references
    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private FirebaseAuth mAuth;
    //private ProgressDialog LoadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        String RegisterTag = "Register Activity";
        Log.v(RegisterTag, "Made it to Register Activity");

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.register_create_account_button);
       // LoadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });


        }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);

        /* add flags so that user cannot press back button without first hitting logout button */
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();
        Toast.makeText(this,"making",Toast.LENGTH_SHORT);
        //check to see that user
        String CreateAccountTag = "Create Account";
        Log.v(CreateAccountTag, "Inside of CreateNewAccount module");
        if(TextUtils.isEmpty(email)) {


            Toast.makeText(this,"Please enter your email...",Toast.LENGTH_SHORT);
            Log.v(CreateAccountTag, "Empty Email");
        }
        else if(TextUtils.isEmpty(password)) {


            Toast.makeText(this,"Please enter your password...",Toast.LENGTH_SHORT);
            Log.v(CreateAccountTag, "Empty password");
        }
        else if(TextUtils.isEmpty(confirmPassword)) {


            Toast.makeText(this,"Please confirm your password...",Toast.LENGTH_SHORT);
            Log.v(CreateAccountTag, "Empty confirm password");
        }

        else if(!password.equals(confirmPassword)){

            Toast.makeText(this,"Your passwords do not match.",Toast.LENGTH_SHORT);
            Log.v(CreateAccountTag, "Passwords don't match");
        }
        else {
            //LoadingBar.setTitle("Creating new account");
            //LoadingBar.setMessage("Please wait while we are creating your new account.");
            //LoadingBar.show();
            //LoadingBar.setCanceledOnTouchOutside(true);
            Log.v(CreateAccountTag, "All credentials are valid");
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String CreateButtonTag = "Create Button";
                            Log.v(CreateButtonTag, "trying to create account");
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "You are authenticated successfully", Toast.LENGTH_SHORT).show();
                               // LoadingBar.dismiss();
                                Log.v(CreateButtonTag, "successfully created an account");
                            }
                            else{
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error occurred:" + message, Toast.LENGTH_SHORT).show();
                               // LoadingBar.dismiss();
                                Log.v(CreateButtonTag, "successfully created an account");
                            }
                            Log.v(CreateButtonTag, "end of trying to create account");
                        }
                    });

        }
        Log.v(CreateAccountTag, "End of create account");







    }
}

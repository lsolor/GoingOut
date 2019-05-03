package com.wildyeo.goingout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private Button LoginButton;
    private Button CreateAccountButton;
    private EditText UserEmail, UserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CreateAccountButton = (Button) findViewById(R.id.login_create_account_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        LoginButton = (Button) findViewById(R.id.login_button);

        LoginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                AttemptToLogin();
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

    private void AttemptToLogin() {
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
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

package com.wildyeo.goingout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {


    //UI references
    private AutoCompleteTextView mEmailView;
    private EditText mPassword1;
    private EditText mPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPassword1 = (EditText) findViewById(R.id.confirm_password_1);
        mPassword1 = (EditText) findViewById(R.id.confirm_password_2);


        Button mRegisterButton = (Button) findViewById(R.id.create_account_button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });


        }

    private void CreateNewAccount() {
        String email = mEmailView.getText().toString();
        String password = mPassword1.getText().toString();
        String confirm_password = mPassword2.getText().toString();

        //check to see that user
        if(TextUtils.isEmpty(email)) {


            Toast.makeText(this,"Please enter your email...",Toast.LENGTH_SHORT);

        }
        else if(TextUtils.isEmpty(password)) {


            Toast.makeText(this,"Please enter your password...",Toast.LENGTH_SHORT);

        }
        else if(TextUtils.isEmpty(confirm_password)) {


            Toast.makeText(this,"Please confirm your password...",Toast.LENGTH_SHORT);

        }

        if(!password.equals(confirm_password)){

            Toast.makeText(this,"Your passwords do not match.",Toast.LENGTH_SHORT);



    }


    ;




    }
}

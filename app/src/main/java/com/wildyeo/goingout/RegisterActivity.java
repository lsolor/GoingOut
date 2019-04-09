package com.wildyeo.goingout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wildyeo.goingout.ui.register.RegisterFragment;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RegisterFragment.newInstance())
                    .commitNow();
        }
    }
}

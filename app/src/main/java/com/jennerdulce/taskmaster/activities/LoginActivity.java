package com.jennerdulce.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.jennerdulce.taskmaster.R;

public class LoginActivity extends AppCompatActivity {

    public final static String TAG =  "jennerdulce_taskmaster_loginactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginSignupButton = findViewById(R.id.loginSignupButton);
        loginSignupButton.setOnClickListener(view -> {
                    Intent signupActivityIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(signupActivityIntent);
                });

        Button loginButton = findViewById(R.id.signupVerifySubmitButton);
        loginButton.setOnClickListener(view -> {
            EditText loginUsernameEditText = findViewById(R.id.loginVerifyUsernameEditText);
            String username = loginUsernameEditText.getText().toString();
            EditText loginPasswordEditText = findViewById(R.id.loginVerifyCodeEditText);
            String password = loginPasswordEditText.getText().toString();

            Amplify.Auth.signIn(username,
                    password,
                    success ->
                    {
                        Log.i(TAG, "Login succeeded: " + success.toString());
                        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Login failed: " + failure.toString());
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Could not log in that user!", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}
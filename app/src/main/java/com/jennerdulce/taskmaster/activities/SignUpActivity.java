package com.jennerdulce.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.jennerdulce.taskmaster.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    public final static String TAG =  "jennerdulce_taskmaster_signupactivity";
    public final static String USERNAME_TAG =  "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUpButton = findViewById(R.id.signupVerifySubmitButton);
        signUpButton.setOnClickListener(view -> {
            EditText signUpUsernameEditText = findViewById(R.id.loginVerifyUsernameEditText);
            String username = signUpUsernameEditText.getText().toString();
            EditText signUpPasswordEditText = findViewById(R.id.loginVerifyCodeEditText);
            String password = signUpPasswordEditText.getText().toString();
            Amplify.Auth.signUp(username,
                    password,
                    AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), username)
                            .build(),
                    success -> {
                        Log.i(TAG, "Signup succeeded: " + success.toString());
                        Intent signUpVerifyActivityIntent = new Intent(SignUpActivity.this, SignUpVerifyActivity.class);
                        signUpVerifyActivityIntent.putExtra(USERNAME_TAG, username);
                        startActivity(signUpVerifyActivityIntent);
                    },
                    failure -> {
                        Log.i(TAG, "Signup failed: " + failure.toString());
                        runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Could not sign up that user!", Toast.LENGTH_SHORT).show());
                    });
        });
    }
}
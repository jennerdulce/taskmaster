package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.SignUpActivity.USERNAME_TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.jennerdulce.taskmaster.R;

public class SignUpVerifyActivity extends AppCompatActivity {

    public final static String TAG =  "jennerdulce_taskmaster_signupverifyactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_verify);

        Intent intent = getIntent();
        String username = intent.getStringExtra(USERNAME_TAG);
        EditText loginUsernameEditText = findViewById(R.id.loginVerifyCodeEditText);

        Button signUpVerifySubmitButton = findViewById(R.id.signupVerifySubmitButton);
        signUpVerifySubmitButton.setOnClickListener(view -> {
            EditText signUpVerifyUsernameEditText = findViewById(R.id.loginVerifyUsernameEditText);
            signUpVerifyUsernameEditText.setText(username);
            String username2 = signUpVerifyUsernameEditText.getText().toString();

            EditText signUpVerificationCodeEditText = findViewById(R.id.loginVerifyCodeEditText);
            String verificationCode = signUpVerificationCodeEditText.getText().toString();

            Amplify.Auth.confirmSignUp(username2,
                    verificationCode,
                    success ->
                    {
                        Log.i(TAG, "Verification succeeded: " + success.toString());
                        Intent loginActivityIntent = new Intent(SignUpVerifyActivity.this, LoginActivity.class);
                        startActivity(loginActivityIntent);
                    },
                    failure ->
                    {
                        Log.i(TAG, "Verification failed: " + failure.toString());
                        runOnUiThread(() -> Toast.makeText(SignUpVerifyActivity.this, "Could not verify that user!", Toast.LENGTH_SHORT).show());
                    }
            );
        });
    }
}
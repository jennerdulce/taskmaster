package com.jennerdulce.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jennerdulce.taskmaster.R;

public class UserSettingsActivity extends AppCompatActivity {
    public final static String USERNAME_KEY = "username";

    // In order to used saved "state"
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferencesEditor = sharedPreferences.edit();

        // Accesses stored values and sets the value for the PlainText
        EditText usernameInputText = findViewById(R.id.usernameInputPlainText);
        String username = sharedPreferences.getString(USERNAME_KEY, "");
        usernameInputText.setText(username);

        Button userSettingsSaveButton = findViewById(R.id.userSettingsSaveButton);
        userSettingsSaveButton.setOnClickListener(view -> {
            String changeUsername = usernameInputText.getText().toString();
            sharedPreferencesEditor.putString(USERNAME_KEY, changeUsername);
            sharedPreferencesEditor.apply();
        });
    }
}
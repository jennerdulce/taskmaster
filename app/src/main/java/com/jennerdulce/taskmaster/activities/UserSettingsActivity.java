package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.MainActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.AssignedTeam;
import com.jennerdulce.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserSettingsActivity extends AppCompatActivity {
    public final static String USERNAME_KEY = "username";
    public final static String TEAMNAME_KEY = "teamname";

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

        Spinner teamSpinner = findViewById(R.id.taskDetailTeamSpinner);

        CompletableFuture<List<AssignedTeam>> assignedTeamCompletableFuture = new CompletableFuture<>();
        List<AssignedTeam> teams = new ArrayList<>();

        Amplify.API.query(
                ModelQuery.list(AssignedTeam.class),
                success -> {
                    if (success.hasData())
                    {
                        for (AssignedTeam assignedTeam : success.getData())
                        {
                            teams.add(assignedTeam);
                        }
                        assignedTeamCompletableFuture.complete(teams);
                    }
                },
                failure -> {
                    Log.i(TAG, "Failed");
                    assignedTeamCompletableFuture.complete(null);
                }
        );

        List<AssignedTeam> listOfTeams = null;
        try
        {
            listOfTeams = assignedTeamCompletableFuture.get();
        }
        catch (InterruptedException ie)
        {
            Log.i(TAG, "InterruptedException while getting assigned team: " + ie.getMessage());
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException ee)
        {
            Log.i(TAG, "ExecutionException while getting assigned team:  " + ee.getMessage());
        }

        List<String> teamNames = new ArrayList<>();

        for (AssignedTeam team : listOfTeams){
            teamNames.add(team.getTeamName());
        }

        teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));
        List<AssignedTeam> listOfTeams2 = listOfTeams;

        Button userSettingsSaveButton = findViewById(R.id.userSettingsSaveButton);
        userSettingsSaveButton.setOnClickListener(view -> {
            String chosenTeam = teamSpinner.getSelectedItem().toString();
            AssignedTeam assignedTeam = null;
            for (AssignedTeam team : listOfTeams2){
                if(chosenTeam.equals(team.getTeamName())){
                    assignedTeam = team;
                }
            }

            String changeUsername = usernameInputText.getText().toString();
            sharedPreferencesEditor.putString(USERNAME_KEY, changeUsername);
            sharedPreferencesEditor.putString(TEAMNAME_KEY, assignedTeam.getTeamName());
            sharedPreferencesEditor.apply();
        });
    }
}
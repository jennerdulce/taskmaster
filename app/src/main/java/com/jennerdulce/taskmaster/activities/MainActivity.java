package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.UserSettingsActivity.TEAMNAME_KEY;
import static com.jennerdulce.taskmaster.activities.UserSettingsActivity.USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.AssignedTeam;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String TASK_ID_STRING = "taskId";
    public final static String TAG = "jdd_taskmaster";
    public final static String TEAM_UNKNOWN_NAME = "Team Unknown";
    protected static SharedPreferences sharedPreferences;
    protected static Resources res;

    TaskRecyclerViewAdapter taskRecyclerViewAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthUser currentUser = Amplify.Auth.getCurrentUser();
        if(currentUser == null){
            Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginActivityIntent);
        }

        // Goes into DB and retrieves Items
        Amplify.API.query(
                ModelQuery.list(TaskItem.class),
                success -> {
                    List<TaskItem> taskItemList = new ArrayList<>();
                    String teamname = sharedPreferences.getString(TEAMNAME_KEY, "");
                    if(success.hasData()){
                        for (TaskItem taskItem : success.getData()){
                            if(!teamname.equals("")) {
                                if (taskItem.getAssignedTeam().getTeamName().equals(teamname)) {
                                    taskItemList.add(taskItem);
                                }
                            }
                        }
                    }
                    runOnUiThread(() -> {
                        ((TextView) findViewById(R.id.mainPageTeamTextView)).setText(teamname);
                        taskRecyclerViewAdapter.setTaskItemList(taskItemList);
                    });
                },
                failure -> {
                    Log.i(TAG, "Failed");
                }
        );


        res = getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        List<TaskItem> taskItemList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.taskListRecyclerView);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(this, taskItemList);
        recyclerView.setAdapter(taskRecyclerViewAdapter);

        // Add Task Intent Button
        Button addTaskButton = (Button) findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(addTaskIntent);
        });

        // All Tasks Intent Button
        Button allTasksButton = (Button) findViewById(R.id.allTasksButton);
        allTasksButton.setOnClickListener(view -> {
            Intent allTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(allTasksIntent);
        });

        // User Settings Intent Button
        ImageView userSettingsButton = (ImageView) findViewById(R.id.userSettingsImageView);
        userSettingsButton.setOnClickListener(view -> {
            Intent userSettingsIntent = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(userSettingsIntent);
        });

        // Signup Button
        Button mainSignUpButton = (Button) findViewById(R.id.mainSignUpButton);
        mainSignUpButton.setOnClickListener(view -> {
            Intent signUpActivityIntent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(signUpActivityIntent);
        });

        // Logout Button
        Button mainLogoutButton = (Button) findViewById(R.id.mainLogoutButton);
        mainLogoutButton.setOnClickListener(view -> {
            Amplify.Auth.signOut(
                    ()-> {Log.i(TAG, "Logout succeeded!");},
                    failure -> {Log.i(TAG, "Logout failed: " + failure.toString());}
            );
            Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginActivityIntent);
        });
    }

    // onResume lifecycle
    @Override
    protected void onResume(){
        super.onResume();
        String teamname = sharedPreferences.getString(TEAMNAME_KEY, "");
        Amplify.API.query(
                ModelQuery.list(TaskItem.class),
                success -> {
                    List<TaskItem> taskItemList = new ArrayList<>();
                    if(success.hasData()){
                        for (TaskItem taskItem : success.getData()){
                            if(!teamname.equals("")) {
                                if (taskItem.getAssignedTeam().getTeamName().equals(teamname)) {
                                    taskItemList.add(taskItem);
                                }
                            }
                        }
                    }
                    runOnUiThread(() -> {
                        taskRecyclerViewAdapter.setTaskItemList(taskItemList);
                        taskRecyclerViewAdapter.notifyDataSetChanged();
                    });
                },
                failure -> {
                    Log.i(TAG, "Failed");
                }
        );
        String username = "";
        AuthUser currentUser = Amplify.Auth.getCurrentUser();  // TODO: Use actual nickname here instead
        if (currentUser != null) {
            username = currentUser.getUsername();
        }
//        String username = sharedPreferences.getString(USERNAME_KEY, "");
        if(!username.equals("")){
            // This line finds the saved String ID from the strings.xml file and instantiate at the '%1$s' at the second parameter
            ((TextView) findViewById(R.id.welcomeUsernameMessageTextView)).setText(res.getString(R.string.WelcomeUsername, username));
        }

        if(!teamname.equals("")){
            // This line finds the saved String ID from the strings.xml file and instantiate at the '%1$s' at the second parameter
            ((TextView) findViewById(R.id.mainPageTeamTextView)).setText(teamname);
        }
    }
}
package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.UserSettingsActivity.TEAMNAME_KEY;
import static com.jennerdulce.taskmaster.activities.UserSettingsActivity.USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.AssignedTeam;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String TASK_NAME_STRING = "taskName";
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

        // Create Teams
//        AssignedTeam assignedTeam1 = AssignedTeam.builder()
//                .teamName("GOLD")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(assignedTeam1),
//                success -> Log.i(TAG, "Succeeded"),
//                failure -> Log.i(TAG, "Failed")
//        );
//
//        AssignedTeam assignedTeam2 = AssignedTeam.builder()
//                .teamName("BLUE")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(assignedTeam2),
//                success -> Log.i(TAG, "Succeeded"),
//                failure -> Log.i(TAG, "Failed")
//        );
//        AssignedTeam assignedTeam3 = AssignedTeam.builder()
//                .teamName("RED")
//                .build();
//        Amplify.API.mutate(
//                ModelMutation.create(assignedTeam3),
//                success -> Log.i(TAG, "Succeeded"),
//                failure -> Log.i(TAG, "Failed")
//        );

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
                            } else {
                                taskItemList.add(taskItem);
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
                            } else {
                                taskItemList.add(taskItem);
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

        String username = sharedPreferences.getString(USERNAME_KEY, "");

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
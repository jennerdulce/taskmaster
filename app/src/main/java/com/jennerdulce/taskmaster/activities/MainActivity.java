package com.jennerdulce.taskmaster.activities;

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
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String TASK_NAME_STRING = "taskName";
    public final static String TASK_ID_STRING = "taskId";
    public final static String TAG = "jdd_taskmaster";
    protected static SharedPreferences sharedPreferences;
    protected static Resources res;

    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Goes into DB and retrieves Items
        Amplify.API.query(
                ModelQuery.list(TaskItem.class),
                success -> {
                    List<TaskItem> taskItemList = new ArrayList<>();
                    for (TaskItem taskItem : success.getData()){
                        taskItemList.add(taskItem);
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

        Amplify.API.query(
                ModelQuery.list(TaskItem.class),
                success -> {
                    List<TaskItem> taskItemList = new ArrayList<>();
                    for (TaskItem taskItem : success.getData()){
                        taskItemList.add(taskItem);
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
    }
}
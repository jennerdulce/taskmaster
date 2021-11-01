package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.UserSettingsActivity.USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;
import com.jennerdulce.taskmaster.database.TaskmasterDatabase;
import com.jennerdulce.taskmaster.models.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final static String TASK_NAME_STRING = "taskName";
    public final static String TASK_ID_STRING = "taskId";
    public final static String DATABASE_INSTANCE_NAME = "jdd_taskmaster_db";
    protected static SharedPreferences sharedPreferences;
    protected static Resources res;

    TaskmasterDatabase taskmasterDatabase;
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Database
        taskmasterDatabase = Room.databaseBuilder(getApplicationContext(), TaskmasterDatabase.class, DATABASE_INSTANCE_NAME)
                .allowMainThreadQueries()
                .build();

        List<TaskItem> taskItemList = taskmasterDatabase.taskItemDao().findAll();
        // RecycleView
//        List<TaskItem> taskItemList = new ArrayList<>();
//        taskItemList.add(new TaskItem("Study", "Finish your assignments."));
//        taskItemList.add(new TaskItem("Gym", "Leg day."));
//        taskItemList.add(new TaskItem("Cook", "Meal prep"));

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

//        // Sample Task One Task Details Intent
//        TextView sampleTaskOne = findViewById(R.id.sampleTaskOne);
//        sampleTaskOne.setOnClickListener(view -> {
//            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//            taskDetailsIntent.putExtra(TASK_NAME_STRING, sampleTaskOne.getText());
//            startActivity(taskDetailsIntent);
//        });
//
//        // Sample Task Two Task Details Intent
//        TextView sampleTaskTwo = findViewById(R.id.sampleTaskTwo);
//        sampleTaskTwo.setOnClickListener(view -> {
//            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//            taskDetailsIntent.putExtra(TASK_NAME_STRING, sampleTaskTwo.getText());
//            startActivity(taskDetailsIntent);
//        });
//
//        // Sample Task Three Task Details Intent
//        TextView sampleTaskThree = findViewById(R.id.sampleTaskThree);
//        sampleTaskThree.setOnClickListener(view -> {
//            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailActivity.class);
//            taskDetailsIntent.putExtra(TASK_NAME_STRING, sampleTaskThree.getText());
//            startActivity(taskDetailsIntent);
//        });
    }

    // onResume lifecycle
    @Override
    protected void onResume(){
        super.onResume();
        // Get preferences
        List<TaskItem> taskItemList2 = taskmasterDatabase.taskItemDao().findAll();
        taskRecyclerViewAdapter.setTaskItemList(taskItemList2);
        taskRecyclerViewAdapter.notifyDataSetChanged();
        String username = sharedPreferences.getString(USERNAME_KEY, "");

        if(!username.equals("")){
            // This line finds the saved String ID from the strings.xml file and instantiate at the '%1$s' at the second parameter
            ((TextView) findViewById(R.id.welcomeUsernameMessageTextView)).setText(res.getString(R.string.WelcomeUsername, username));
        }
    }
}
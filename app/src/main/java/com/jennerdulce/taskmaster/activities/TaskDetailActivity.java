package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.MainActivity.DATABASE_INSTANCE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;
import com.jennerdulce.taskmaster.database.TaskmasterDatabase;
import com.jennerdulce.taskmaster.models.TaskItem;

public class TaskDetailActivity extends AppCompatActivity {

    TaskmasterDatabase taskmasterDatabase;
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Bring in Database
        taskmasterDatabase = Room.databaseBuilder(getApplicationContext(), TaskmasterDatabase.class, DATABASE_INSTANCE_NAME)
                .allowMainThreadQueries()
                .build();

        // Handle Intent
        Intent intent = getIntent();
        long taskId = intent.getLongExtra(MainActivity.TASK_ID_STRING, -1);
        TaskItem taskItem = taskmasterDatabase.taskItemDao().findById(taskId);
        if(taskItem != null){
            TextView taskDetailsHeader = findViewById(R.id.taskDetailsHeaderTextView);
            TextView taskDetailsDescription = findViewById(R.id.taskDetailsDescTextView);
            taskDetailsHeader.setText(taskItem.taskName);
            taskDetailsDescription.setText(taskItem.body);
        }
    }
}
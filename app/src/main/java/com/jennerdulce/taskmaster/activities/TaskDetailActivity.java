package com.jennerdulce.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jennerdulce.taskmaster.R;

public class TaskDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Handle Intent
        Intent intent = getIntent();
        String taskName = intent.getExtras().get(MainActivity.TASK_NAME_STRING).toString();
        if(taskName != null){
            TextView taskDetailsHeader = findViewById(R.id.taskDetailsHeaderTextView);
            taskDetailsHeader.setText(taskName);
        }
    }
}
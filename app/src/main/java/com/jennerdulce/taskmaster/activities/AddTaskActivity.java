package com.jennerdulce.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jennerdulce.taskmaster.R;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = (Button) findViewById(R.id.addTaskFormButton);
        addTaskButton.setOnClickListener(view -> {
            TextView addTaskSubmitted = (TextView) findViewById(R.id.addTaskSubmittedTextView);
            addTaskSubmitted.setText("Submitted!");
        });
    }
}
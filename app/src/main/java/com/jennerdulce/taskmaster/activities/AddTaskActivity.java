package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.MainActivity.DATABASE_INSTANCE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;
import com.jennerdulce.taskmaster.database.TaskmasterDatabase;
import com.jennerdulce.taskmaster.models.StatusEnum;
import com.jennerdulce.taskmaster.models.TaskItem;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity {

    TaskmasterDatabase taskmasterDatabase;
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskmasterDatabase = Room.databaseBuilder(getApplicationContext(), TaskmasterDatabase.class, DATABASE_INSTANCE_NAME)
                .allowMainThreadQueries()
                .build();

        Intent intent = getIntent();
        long taskId = intent.getLongExtra(MainActivity.TASK_ID_STRING, -1);
        Spinner taskStatusSpinner = findViewById(R.id.taskStatusSpinner);
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StatusEnum.values()));
        TaskItem taskItem = taskmasterDatabase.taskItemDao().findById(taskId);
        int spinnerPosition = getSpinnerIndex(taskStatusSpinner, taskItem.state.toString());
        taskStatusSpinner.setSelection(spinnerPosition);

        Button addTaskButton = (Button) findViewById(R.id.addTaskFormButton);
        addTaskButton.setOnClickListener(view -> {
//            TextView addTaskSubmitted = (TextView) findViewById(R.id.addTaskSubmittedTextView);
//            addTaskSubmitted.setText("Submitted!");
            EditText taskNameEditText = findViewById(R.id.myTaskPlainText);
            EditText taskDescriptionEditText = findViewById(R.id.taskDescriptionPlainText);
            String taskNamePlainTextString = taskNameEditText.getText().toString();
            String taskBodyPlainTextString = taskDescriptionEditText.getText().toString();
            TaskItem taskItem2 = new TaskItem(taskNamePlainTextString, taskBodyPlainTextString);
            taskItem2.state = StatusEnum.valueOf(taskStatusSpinner.getSelectedItem().toString());
            long newTaskId = taskmasterDatabase.taskItemDao().insert(taskItem2);
            List<TaskItem> taskItemList2 = taskmasterDatabase.taskItemDao().findAll();
            taskRecyclerViewAdapter.setTaskItemList(taskItemList2);
            taskRecyclerViewAdapter.notifyDataSetChanged();
        });
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck){
        for (int i = 0;i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)){
                return i;
            }
        }
        return 0;
    }
}
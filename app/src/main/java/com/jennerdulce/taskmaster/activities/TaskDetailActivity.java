package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.AddTaskActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Handle Intent
        Intent intent = getIntent();
        String taskId = intent.getStringExtra(MainActivity.TASK_ID_STRING);

        Amplify.API.query(
                ModelQuery.list(TaskItem.class),
                success -> {
                    TaskItem taskItem = null;
                    List<TaskItem> taskItemList = new ArrayList<>();
                    for (TaskItem currentTaskItem : success.getData()){
                        if(taskId.equals(currentTaskItem.getId()))
                        {
                            taskItem = currentTaskItem;
                            break;
                        }
                    }
                    TaskItem taskItemInfo = taskItem;
                    runOnUiThread(() -> {
                        TextView taskDetailsHeader = findViewById(R.id.taskDetailsHeaderTextView);
                        TextView taskDetailsDescription = findViewById(R.id.taskDetailsDescTextView);
                        TextView taskDetailsTeamTextView = findViewById(R.id.taskDetailsTeamTextView);
                        taskDetailsHeader.setText(taskItemInfo.getTaskName());
                        taskDetailsTeamTextView.setText(taskItemInfo.getAssignedTeam().getTeamName());
                        taskDetailsDescription.setText(taskItemInfo.getBody());
                    });
                },
                failure -> {
                    Log.i(TAG, "Failed");
                }
        );
    }
}
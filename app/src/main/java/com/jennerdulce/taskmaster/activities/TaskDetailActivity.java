package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.AddTaskActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TaskDetailActivity extends AppCompatActivity {

    public final static String TAG =  "jennerdulce_taskmaster_taskdetailactivity";
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Handle Intent
        Intent intent = getIntent();
        String taskId = intent.getStringExtra(MainActivity.TASK_ID_STRING);

        CompletableFuture<TaskItem> taskItemCompletableFuture = new CompletableFuture<>();

        Amplify.API.query(
                ModelQuery.list(TaskItem.class),
                success -> {
                    TaskItem taskItem = null;
                    for (TaskItem currentTaskItem : success.getData()){
                        if(taskId.equals(currentTaskItem.getId()))
                        {
                            taskItem = currentTaskItem;
                            break;
                        }
                    }

                    getImageFileFromS3(taskItem.getTaskImageKey());
                    TaskItem taskItemInfo = taskItem;
                    runOnUiThread(() -> {
                        TextView taskDetailsHeader = findViewById(R.id.taskDetailsHeaderTextView);
                        TextView taskDetailsDescription = findViewById(R.id.taskDetailsDescTextView);
                        TextView taskDetailsTeamTextView = findViewById(R.id.taskDetailsTeamTextView);
                        taskDetailsHeader.setText(taskItemInfo.getTaskName());
                        taskDetailsTeamTextView.setText(taskItemInfo.getAssignedTeam().getTeamName());
                        taskDetailsDescription.setText(taskItemInfo.getBody());
                    });
                    taskItemCompletableFuture.complete(taskItemInfo);
                },
                failure -> {
                    Log.i(TAG, "Failed");
                    taskItemCompletableFuture.complete(null);
                }
        );
    }

    protected void getImageFileFromS3(String s3ImageKey){
        if (s3ImageKey != null){
            Amplify.Storage.downloadFile(
                    s3ImageKey,
                    new File(getApplicationContext().getFilesDir(), s3ImageKey),
                    success -> {
                        Log.i(TAG, "Success. Image file downloaded from S3 successfully with key : " + success.getFile().getName());
                        runOnUiThread(() -> {
                            ImageView taskItemImageView = findViewById(R.id.taskItemImageView);
                            taskItemImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                            });
                        },
                    failure -> {
                        Log.i(TAG, "Failed. Image file was not downloaded from S3. Key : " + s3ImageKey + "\n and error: " + failure.getMessage(), failure);
                    }
            );
        }
    }
}
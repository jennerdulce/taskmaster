package com.jennerdulce.taskmaster.activities;

import static com.jennerdulce.taskmaster.activities.MainActivity.TAG;
import static com.jennerdulce.taskmaster.activities.MainActivity.TEAM_UNKNOWN_NAME;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.AssignedTeam;
import com.amplifyframework.datastore.generated.model.TaskItem;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.adapters.TaskRecyclerViewAdapter;
import com.jennerdulce.taskmaster.models.StatusEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "jdd_taskmaster_add_task";

    TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TaskItem taskItem = null;
        Spinner taskStatusSpinner = findViewById(R.id.taskStatusSpinner);
        Spinner teamSpinner = findViewById(R.id.teamSpinner);
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StatusEnum.values()));
        if(taskItem != null){
            int spinnerPosition = getSpinnerIndex(taskStatusSpinner, taskItem.getStatus().toString());
            taskStatusSpinner.setSelection(spinnerPosition);
        }
        int taskStatusSpinnerChoice = taskStatusSpinner.getSelectedItemPosition();
        taskStatusSpinnerChoice.set

        Button addTaskButton = (Button) findViewById(R.id.addTaskFormButton);
        addTaskButton.setOnClickListener(view -> {
//            TextView addTaskSubmitted = (TextView) findViewById(R.id.addTaskSubmittedTextView);
//            addTaskSubmitted.setText("Submitted!");
            EditText taskNameEditText = findViewById(R.id.myTaskPlainText);
            EditText taskDescriptionEditText = findViewById(R.id.taskDescriptionPlainText);
            String taskNamePlainTextString = taskNameEditText.getText().toString();
            String taskBodyPlainTextString = taskDescriptionEditText.getText().toString();

            CompletableFuture<List<AssignedTeam>> assignedTeamCompletableFuture = new CompletableFuture<>();
            List<AssignedTeam> teams = new ArrayList<>();
            Amplify.API.query(
                    ModelQuery.list(AssignedTeam.class),
                    success -> {
                        if (success.hasData())
                        {
                            for (AssignedTeam assignedTeam : success.getData())
                            {
                                teams.add(assignedTeam);
                            }
                            assignedTeamCompletableFuture.complete(teams);
                        }
                    },
                    failure -> {
                        Log.i(TAG, "Failed");
                        assignedTeamCompletableFuture.complete(null);
                    }
            );

            List<AssignedTeam> listOfTeams = null;
            try
            {
                listOfTeams = assignedTeamCompletableFuture.get();
            }
            catch (InterruptedException ie)
            {
                Log.i(TAG, "InterruptedException while getting assigned team: " + ie.getMessage());
                Thread.currentThread().interrupt();
            }
            catch (ExecutionException ee)
            {
                Log.i(TAG, "ExecutionException while getting assigned team:  " + ee.getMessage());
            }

            // Select Team
            teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfTeams));
            int teamSpinnerPosition = teamSpinner.getItemIdAtPosition();


            TaskItem taskItem2 = TaskItem.builder()
                    .assignedTeam(assignedTeamUnknown)
                    .taskName(taskNamePlainTextString)
                    .body(taskBodyPlainTextString)
                    .status(taskStatusSpinnerChoice)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(taskItem2),
                    success -> {
                        Log.i(TAG, "Succeeded");
                        List<TaskItem> taskList = taskRecyclerViewAdapter.getTaskList();
                        taskList.add(taskItem2);
                        taskRecyclerViewAdapter.notifyDataSetChanged();
                    },
                    failure -> Log.i(TAG, "Failed")
            );

//            TaskItem taskItem2 = new TaskItem(taskNamePlainTextString, taskBodyPlainTextString);
//            taskItem2.state = StatusEnum.valueOf(taskStatusSpinner.getSelectedItem().toString());
//            long newTaskId = taskmasterDatabase.taskItemDao().insert(taskItem2);
            long newTaskId = 0;
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
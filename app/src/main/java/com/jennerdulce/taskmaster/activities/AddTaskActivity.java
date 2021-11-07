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

        // Select Spinner
        Spinner taskStatusSpinner = findViewById(R.id.taskStatusSpinner);
        Spinner teamSpinner = findViewById(R.id.teamSpinner);
        // Put values into spinner
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StatusEnum.values()));
        // Default Selection for spinner
        int spinnerPosition = getSpinnerIndex(taskStatusSpinner, StatusEnum.NEW_TASK.toString());
        taskStatusSpinner.setSelection(spinnerPosition);

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

            List<String> teamNames = new ArrayList<>();

            for (AssignedTeam team : listOfTeams){
                teamNames.add(team.getTeamName());
            }

            teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));
        List<AssignedTeam> listOfTeams2 = listOfTeams;

        Button addTaskButton = (Button) findViewById(R.id.addTaskFormButton);
        addTaskButton.setOnClickListener(view -> {
            TextView addTaskSubmitted = (TextView) findViewById(R.id.addTaskSubmittedTextView);
            addTaskSubmitted.setText("Submitted!");
            String chosenStatus = StatusEnum.fromString(taskStatusSpinner.getSelectedItem().toString()).toString();
            String chosenTeam = teamSpinner.getSelectedItem().toString();
            AssignedTeam assignedTeam = null;
            for (AssignedTeam team : listOfTeams2){
                if(chosenTeam.equals(team.getTeamName())){
                    assignedTeam = team;
                }
            }

            EditText taskNameEditText = findViewById(R.id.myTaskPlainText);
            EditText taskDescriptionEditText = findViewById(R.id.taskDescriptionPlainText);
            String taskNamePlainTextString = taskNameEditText.getText().toString();
            String taskBodyPlainTextString = taskDescriptionEditText.getText().toString();

            TaskItem taskItem2 = TaskItem.builder()
                    .assignedTeam(assignedTeam)
                    .taskName(taskNamePlainTextString)
                    .body(taskBodyPlainTextString)
                    .status(chosenStatus)
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
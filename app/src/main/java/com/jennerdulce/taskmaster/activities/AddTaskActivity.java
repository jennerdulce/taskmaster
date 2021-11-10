package com.jennerdulce.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {
    public final static String TAG = "jdd_taskmaster_add_task";

    TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    AssignedTeam assignedTeam = null;
    String chosenStatus = null;
    String chosenTeam = null;
    String taskNamePlainTextString = null;
    String taskBodyPlainTextString = null;
    String awsImageKey = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText taskNameEditText = findViewById(R.id.myTaskEditText);
        EditText taskDescriptionEditText = findViewById(R.id.taskDescriptionPlainText);

        // Needed here
        activityResultLauncher = getImagePickingActivityResultLauncher();

        // Select Spinner
        Spinner taskStatusSpinner = findViewById(R.id.taskStatusSpinner);
        Spinner teamSpinner = findViewById(R.id.teamSpinner);

        // Put values into spinner
        taskStatusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, StatusEnum.values()));

        // Default Selection for spinner
        int spinnerPosition = getSpinnerIndex(taskStatusSpinner, StatusEnum.NEW_TASK.toString());
        taskStatusSpinner.setSelection(spinnerPosition);

        // Retrieve list of teams for Team spinner
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

            // Retrieves Team name as strings
            for (AssignedTeam team : listOfTeams){
                teamNames.add(team.getTeamName());
            }

            teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));
        List<AssignedTeam> listOfTeams2 = listOfTeams;

        Button addTaskButton = (Button) findViewById(R.id.addTaskFormButton);
        addTaskButton.setOnClickListener(view -> {
            TextView addTaskSubmitted = (TextView) findViewById(R.id.addTaskSubmittedTextView);
            addTaskSubmitted.setText("Submitted!");
            chosenStatus = StatusEnum.fromString(taskStatusSpinner.getSelectedItem().toString()).toString();
            chosenTeam = teamSpinner.getSelectedItem().toString();
            taskNamePlainTextString = taskNameEditText.getText().toString();
            taskBodyPlainTextString = taskDescriptionEditText.getText().toString();

            for (AssignedTeam team : listOfTeams2){
                if(chosenTeam.equals(team.getTeamName())){
                    assignedTeam = team;
                }
            }
            saveTaskItemToDb();
        });

        Button addTaskUploadImageButton = (Button) findViewById(R.id.addTaskUploadImageButton);
        addTaskUploadImageButton.setOnClickListener(view -> {
            selectFileAndSaveToS3();
        });
    }

    protected void saveTaskItemToDb(){
        TaskItem taskItem2 = TaskItem.builder()
                .assignedTeam(assignedTeam)
                .taskName(taskNamePlainTextString)
                .body(taskBodyPlainTextString)
                .status(chosenStatus)
                .taskImageKey(awsImageKey)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(taskItem2),
                success -> {
                    Log.i(TAG, "Succeeded");
                },
                failure -> Log.i(TAG, "Failed")
        );
    }

    protected void selectFileAndSaveToS3(){
        // Grab file from filepicker
        Intent imageFilePickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickIntent.setType("*/*"); // Allows one kind of category of file
        // Restrictions
        imageFilePickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpg", "image/png"});
        // Brings you to photos app on your phone
//        ActivityResultLauncher<Intent> activityResultLauncher = getImagePickingActivityResultLauncher();
        activityResultLauncher.launch(imageFilePickIntent);
    }

    protected ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>()
                {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // Check to see if result is okay and start processing
                        if(result.getResultCode() == Activity.RESULT_OK){
                            if(result.getData() != null){ // returns an intent
                                Uri pickedImageFileUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                    String pickedImageFilename = getFilenameFromUri(pickedImageFileUri);;
                                    Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename);
                                } catch (FileNotFoundException fnfe){
                                    Log.e(TAG, "File not found File picker: " + fnfe.getMessage(), fnfe);
                                }
                            }
                        }
                    }
                }
        );
        return imagePickingActivityResultLauncher;
    }

    // Make getFilenameFromUri Method
    @SuppressLint("Range")
    protected String getFilenameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null))
            {
                if (cursor != null && cursor.moveToFirst())
                {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private int getSpinnerIndex(Spinner spinner, String stringValueToCheck){
        for (int i = 0;i < spinner.getCount(); i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(stringValueToCheck)){
                return i;
            }
        }
        return 0;
    }

    protected void uploadInputStreamToS3(InputStream pickedImageFileInputStream, String pickedImageFilename){
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageFileInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in getting uploaded file to S3. Key is: " + success.getKey());
                    awsImageKey = success.getKey();
                },
                failure -> {
                    Log.e(TAG, "Failed in getting uploaded file to S3. Key is: " + pickedImageFilename + " with error: " + failure.getMessage(), failure);
                }
        );
    }
}
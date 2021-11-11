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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    // For creating new task
    TaskRecyclerViewAdapter taskRecyclerViewAdapter;
    ActivityResultLauncher<Intent> activityResultLauncher;
    AssignedTeam assignedTeam = null;
    String chosenStatus = null;
    String chosenTeam = null;
    String taskNamePlainTextString = null;
    String taskBodyPlainTextString = null;
    String awsImageKey = null;
    InputStream pickedImageInputStream = null;

    // For image picker
    Uri pickedImageFileUri;
    String pickedImageFilename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Needed here
        activityResultLauncher = getImagePickingActivityResultLauncher();

        // Explicit intent (share)
        Intent intent = getIntent();
        if((intent.getType() != null) && (intent.getType().startsWith("image/"))){
            Uri incomingFileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (incomingFileUri != null){
                try{
                    pickedImageFileUri = incomingFileUri;
                    pickedImageFilename = getFilenameFromUri(incomingFileUri);
                    InputStream incomingImageFileInputStream = getContentResolver().openInputStream(incomingFileUri);
                    ImageView previewShareImageView = findViewById(R.id.previewShareImageView);
                    previewShareImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                } catch (FileNotFoundException fnfe){
                    Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                }
            }
        }

        EditText taskNameEditText = findViewById(R.id.myTaskEditText);
        EditText taskDescriptionEditText = findViewById(R.id.taskDescriptionPlainText);

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
            saveToS3AndDatabase();
        });

        Button addTaskUploadImageButton = (Button) findViewById(R.id.addTaskUploadImageButton);
        addTaskUploadImageButton.setOnClickListener(view -> {
            selectImage();
        });
    }

    protected void saveToDbOnly(){

    }

    protected void selectImage(){
        // Grab file from filepicker
        Intent imageFilePickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageFilePickIntent.setType("*/*"); // Allows one kind of category of file
        imageFilePickIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpg", "image/png"});
        activityResultLauncher.launch(imageFilePickIntent);
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

    protected void saveToS3AndDatabase(){
        if(pickedImageFilename == null){
            runOnUiThread(() -> {
                Toast.makeText(AddTaskActivity.this, "Please select a file before submitting!", Toast.LENGTH_SHORT).show();
            });
        } else {
            InputStream pickedImageInputStream = null;
            try {
                pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
            } catch (FileNotFoundException fnfe){
                Log.e(TAG, "Could not get input stream from preview image! " + fnfe.getMessage(), fnfe);
            }
            uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename);
        }
    }

    protected void uploadInputStreamToS3(InputStream pickedImageFileInputStream, String pickedImageFilename){
        Amplify.Storage.uploadInputStream(
                pickedImageFilename,
                pickedImageFileInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in getting uploaded file to S3. Key is: " + success.getKey());
                    awsImageKey = success.getKey();
                    saveTaskItemToDb();
                },
                failure -> {
                    Log.e(TAG, "Failed in getting uploaded file to S3. Key is: " + pickedImageFilename + " with error: " + failure.getMessage(), failure);
                }
        );
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
                                pickedImageFileUri = result.getData().getData();
                                try {
                                    pickedImageFilename = getFilenameFromUri(pickedImageFileUri);;
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                    Log.i(TAG, "Succeeded in getting input stream from file on phone! Filename is: " + pickedImageFilename);
                                    ImageView previewShareImageView = findViewById(R.id.previewShareImageView);
                                    previewShareImageView.setImageBitmap(BitmapFactory.decodeStream(pickedImageInputStream));
//                                    ImageView taskItemImageView = findViewById(R.id.taskItemImageView);
//                                    taskItemImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
//                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename);
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
}
package com.jennerdulce.taskmaster.adapters;

import static com.jennerdulce.taskmaster.activities.MainActivity.TASK_ID_STRING;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.TaskItem;
import com.jennerdulce.taskmaster.R;
import com.jennerdulce.taskmaster.activities.TaskDetailActivity;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskItemViewHolder> {
    AppCompatActivity associatedActivity;
    List<TaskItem> taskItemList;

    public TaskRecyclerViewAdapter(AppCompatActivity associatedActivity, List<TaskItem> taskItemList) {
        this.associatedActivity = associatedActivity;
        this.taskItemList = taskItemList;
    }

    @NonNull
    @Override
    public TaskItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_item, parent, false);
        TaskItemViewHolder taskItemViewHolder = new TaskItemViewHolder(fragment);
        return taskItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskItemViewHolder holder, int position) {
        TaskItem taskItem = taskItemList.get(position);
        View taskItemFragment = holder.itemView;
        TextView currentItemFragmentTextView =   taskItemFragment.findViewById(R.id.currentItemFragmentTextView);
        String taskItemString = "" + taskItem.getTaskName() + "\n" +
                "Status: " + taskItem.getStatus() + "\n" +
                "Team: " + taskItem.getAssignedTeam().getTeamName();
        currentItemFragmentTextView.setText(taskItemString);

        holder.itemView.setOnClickListener(view -> {
            Intent taskFragmentIntent = new Intent(associatedActivity, TaskDetailActivity.class);
            taskFragmentIntent.putExtra(TASK_ID_STRING, taskItem.getId());
            associatedActivity.startActivity(taskFragmentIntent);
        });
    }

    @Override
    public int getItemCount() {
        return taskItemList.size();
    }

    public List<TaskItem> getTaskList() {return taskItemList; }

    public void setTaskItemList(List<TaskItem> taskItemList){
        this.taskItemList = taskItemList;
    }

    public static class TaskItemViewHolder extends RecyclerView.ViewHolder {
        public TaskItemViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}

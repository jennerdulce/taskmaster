package com.jennerdulce.taskmaster.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TaskItem {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String taskName;
    public String body;
    public StatusEnum state;

    public TaskItem(String taskName, String body) {
        this.taskName = taskName;
        this.body = body;
        this.state = StatusEnum.NEW_TASK;
    }

    @Override
    @NonNull
    public String toString() {
        return "" +
                taskName + '\n' +
                body + '\n' +
                state + '\n';
    }
}

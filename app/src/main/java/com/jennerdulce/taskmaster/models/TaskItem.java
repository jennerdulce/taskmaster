package com.jennerdulce.taskmaster.models;

public class TaskItem {
    public String taskName;
    public String body;
    public String state;

    public TaskItem(String taskName, String body, String state) {
        this.taskName = taskName;
        this.body = body;
        this.state = state;
    }

    @Override
    public String toString() {
        return "" +
                taskName + '\n' +
                body + '\n' +
                state + '\n';
    }
}

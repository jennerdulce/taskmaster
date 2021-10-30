package com.jennerdulce.taskmaster.models;

public enum StatusEnum {
    NEW_TASK("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In progress"),
    COMPLETE("Complete");

    private final String statusString;

    StatusEnum(String statusString){
        this.statusString = statusString;
    }

    public String getStatusString() {
        return statusString;
    }
}

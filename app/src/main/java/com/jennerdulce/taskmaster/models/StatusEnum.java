package com.jennerdulce.taskmaster.models;

public enum StatusEnum {
    NEW_TASK("New"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In progress"),
    COMPLETE("Complete");

    private final String statusString;

    @Override
    public String toString()
    {
        return statusString;
    }

    StatusEnum(String statusString){

        this.statusString = statusString;
    }

    public static StatusEnum fromString(String statusText) {
        for (StatusEnum taskStatus : StatusEnum.values()) {
            if (taskStatus.statusString.equalsIgnoreCase(statusText)) {
                return taskStatus;
            }
        }
        return null;
    }
}

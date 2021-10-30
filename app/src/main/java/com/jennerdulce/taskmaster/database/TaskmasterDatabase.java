package com.jennerdulce.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.jennerdulce.taskmaster.models.TaskItem;

@Database(entities = {TaskItem.class}, version = 1)
@TypeConverters({TaskmasterDatabaseConverters.class})
public abstract class TaskmasterDatabase extends RoomDatabase {
    public abstract TaskItemDao taskItemDao();
}

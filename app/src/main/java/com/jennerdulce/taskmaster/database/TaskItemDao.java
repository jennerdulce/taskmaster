package com.jennerdulce.taskmaster.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jennerdulce.taskmaster.models.TaskItem;
import java.util.List;

@Dao
public interface TaskItemDao {

    @Insert
    long insert(TaskItem taskItem);

    @Query("Select * FROM TaskItem")
    List<TaskItem> findAll();

    @Query("SELECT  * FROM TaskItem WHERE id = :id")
    TaskItem findById(long id);

    @Update
    void update(TaskItem taskItem);
}

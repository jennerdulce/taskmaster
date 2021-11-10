package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;
import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the TaskItem type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TaskItems")
@Index(name = "byOurTask", fields = {"assignedTeamId"})
public final class TaskItem implements Model {
  public static final QueryField ID = field("TaskItem", "id");
  public static final QueryField TASK_NAME = field("TaskItem", "taskName");
  public static final QueryField BODY = field("TaskItem", "body");
  public static final QueryField STATUS = field("TaskItem", "status");
  public static final QueryField ASSIGNED_TEAM = field("TaskItem", "assignedTeamId");
  public static final QueryField TASK_IMAGE_KEY = field("TaskItem", "taskImageKey");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String taskName;
  private final @ModelField(targetType="String") String body;
  private final @ModelField(targetType="String") String status;
  private final @ModelField(targetType="AssignedTeam", isRequired = true) @BelongsTo(targetName = "assignedTeamId", type = AssignedTeam.class) AssignedTeam assignedTeam;
  private final @ModelField(targetType="String") String taskImageKey;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getTaskName() {
      return taskName;
  }
  
  public String getBody() {
      return body;
  }
  
  public String getStatus() {
      return status;
  }
  
  public AssignedTeam getAssignedTeam() {
      return assignedTeam;
  }
  
  public String getTaskImageKey() {
      return taskImageKey;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private TaskItem(String id, String taskName, String body, String status, AssignedTeam assignedTeam, String taskImageKey) {
    this.id = id;
    this.taskName = taskName;
    this.body = body;
    this.status = status;
    this.assignedTeam = assignedTeam;
    this.taskImageKey = taskImageKey;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TaskItem taskItem = (TaskItem) obj;
      return ObjectsCompat.equals(getId(), taskItem.getId()) &&
              ObjectsCompat.equals(getTaskName(), taskItem.getTaskName()) &&
              ObjectsCompat.equals(getBody(), taskItem.getBody()) &&
              ObjectsCompat.equals(getStatus(), taskItem.getStatus()) &&
              ObjectsCompat.equals(getAssignedTeam(), taskItem.getAssignedTeam()) &&
              ObjectsCompat.equals(getTaskImageKey(), taskItem.getTaskImageKey()) &&
              ObjectsCompat.equals(getCreatedAt(), taskItem.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), taskItem.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTaskName())
      .append(getBody())
      .append(getStatus())
      .append(getAssignedTeam())
      .append(getTaskImageKey())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TaskItem {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("taskName=" + String.valueOf(getTaskName()) + ", ")
      .append("body=" + String.valueOf(getBody()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("assignedTeam=" + String.valueOf(getAssignedTeam()) + ", ")
      .append("taskImageKey=" + String.valueOf(getTaskImageKey()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static AssignedTeamStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static TaskItem justId(String id) {
    return new TaskItem(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      taskName,
      body,
      status,
      assignedTeam,
      taskImageKey);
  }
  public interface AssignedTeamStep {
    BuildStep assignedTeam(AssignedTeam assignedTeam);
  }
  

  public interface BuildStep {
    TaskItem build();
    BuildStep id(String id);
    BuildStep taskName(String taskName);
    BuildStep body(String body);
    BuildStep status(String status);
    BuildStep taskImageKey(String taskImageKey);
  }
  

  public static class Builder implements AssignedTeamStep, BuildStep {
    private String id;
    private AssignedTeam assignedTeam;
    private String taskName;
    private String body;
    private String status;
    private String taskImageKey;
    @Override
     public TaskItem build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TaskItem(
          id,
          taskName,
          body,
          status,
          assignedTeam,
          taskImageKey);
    }
    
    @Override
     public BuildStep assignedTeam(AssignedTeam assignedTeam) {
        Objects.requireNonNull(assignedTeam);
        this.assignedTeam = assignedTeam;
        return this;
    }
    
    @Override
     public BuildStep taskName(String taskName) {
        this.taskName = taskName;
        return this;
    }
    
    @Override
     public BuildStep body(String body) {
        this.body = body;
        return this;
    }
    
    @Override
     public BuildStep status(String status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep taskImageKey(String taskImageKey) {
        this.taskImageKey = taskImageKey;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String taskName, String body, String status, AssignedTeam assignedTeam, String taskImageKey) {
      super.id(id);
      super.assignedTeam(assignedTeam)
        .taskName(taskName)
        .body(body)
        .status(status)
        .taskImageKey(taskImageKey);
    }
    
    @Override
     public CopyOfBuilder assignedTeam(AssignedTeam assignedTeam) {
      return (CopyOfBuilder) super.assignedTeam(assignedTeam);
    }
    
    @Override
     public CopyOfBuilder taskName(String taskName) {
      return (CopyOfBuilder) super.taskName(taskName);
    }
    
    @Override
     public CopyOfBuilder body(String body) {
      return (CopyOfBuilder) super.body(body);
    }
    
    @Override
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder taskImageKey(String taskImageKey) {
      return (CopyOfBuilder) super.taskImageKey(taskImageKey);
    }
  }
  
}

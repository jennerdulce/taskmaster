package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the AssignedTeam type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "AssignedTeams")
public final class AssignedTeam implements Model {
  public static final QueryField ID = field("AssignedTeam", "id");
  public static final QueryField TEAM_NAME = field("AssignedTeam", "teamName");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String") String teamName;
  private final @ModelField(targetType="TaskItem") @HasMany(associatedWith = "assignedTeam", type = TaskItem.class) List<TaskItem> taskItems = null;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getTeamName() {
      return teamName;
  }
  
  public List<TaskItem> getTaskItems() {
      return taskItems;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private AssignedTeam(String id, String teamName) {
    this.id = id;
    this.teamName = teamName;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      AssignedTeam assignedTeam = (AssignedTeam) obj;
      return ObjectsCompat.equals(getId(), assignedTeam.getId()) &&
              ObjectsCompat.equals(getTeamName(), assignedTeam.getTeamName()) &&
              ObjectsCompat.equals(getCreatedAt(), assignedTeam.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), assignedTeam.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTeamName())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("AssignedTeam {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("teamName=" + String.valueOf(getTeamName()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static BuildStep builder() {
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
  public static AssignedTeam justId(String id) {
    return new AssignedTeam(
      id,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      teamName);
  }
  public interface BuildStep {
    AssignedTeam build();
    BuildStep id(String id);
    BuildStep teamName(String teamName);
  }
  

  public static class Builder implements BuildStep {
    private String id;
    private String teamName;
    @Override
     public AssignedTeam build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new AssignedTeam(
          id,
          teamName);
    }
    
    @Override
     public BuildStep teamName(String teamName) {
        this.teamName = teamName;
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
    private CopyOfBuilder(String id, String teamName) {
      super.id(id);
      super.teamName(teamName);
    }
    
    @Override
     public CopyOfBuilder teamName(String teamName) {
      return (CopyOfBuilder) super.teamName(teamName);
    }
  }
  
}
package com.example.todolist.dto;

import com.example.todolist.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

@DueDateNotBeforeCreation(groups = OnUpdate.class)
@Schema(description = "Данные для обновления задачи")
public class TaskUpdateDto {

  @Size(min = 3, max = 100, groups = OnUpdate.class)
  private String title;

  @Size(max = 500, groups = OnUpdate.class)
  private String description;

  private Boolean completed;

  @FutureOrPresent(groups = OnUpdate.class)
  private LocalDate dueDate;

  private Priority priority;

  @Size(max = 5, groups = OnUpdate.class)
  private Set<String> tags;

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public Priority getPriority() {
    return priority;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public void setPriority(Priority priority) {
    this.priority = priority;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }
}

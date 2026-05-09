package com.example.todolist.dto;

import com.example.todolist.model.Priority;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

public class TaskCreateDto {

  private String title;
  private String description;
  private LocalDate dueDate;
  private Priority priority;
  private Set<String> tags;

  // Геттеры
  public String getTitle() { return title; }
  public String getDescription() { return description; }
  public LocalDate getDueDate() { return dueDate; }
  public Priority getPriority() { return priority; }
  public Set<String> getTags() { return tags; }

  // Сеттеры
  public void setTitle(String title) { this.title = title; }
  public void setDescription(String description) { this.description = description; }
  public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
  public void setPriority(Priority priority) { this.priority = priority; }
  public void setTags(Set<String> tags) { this.tags = tags; }
}
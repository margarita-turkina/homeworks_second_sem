package com.example.todolist.dto;

import com.example.todolist.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Данные для создания задачи")
public class TaskCreateDto {

  @NotBlank(groups = OnCreate.class)
  @Size(min = 3, max = 100, groups = OnCreate.class)
  @Schema(example = "Купить продукты")
  private String title;

  @Size(max = 500, groups = OnCreate.class)
  @Schema(example = "Молоко, хлеб")
  private String description;

  @FutureOrPresent(groups = OnCreate.class)
  private LocalDate dueDate;

  @NotNull(groups = OnCreate.class)
  private Priority priority;

  @Size(max = 5, groups = OnCreate.class)
  private Set<String> tags;

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
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

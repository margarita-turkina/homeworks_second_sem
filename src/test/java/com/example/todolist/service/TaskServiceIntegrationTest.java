package com.example.todolist.service;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskRepository taskRepository;

  @Test
  void bulkCompleteTasks_rollsBackWhenTaskMissing() {
    Task existing = taskService.createTask(new Task("Existing", "Desc"));
    existing.setPriority(Priority.MEDIUM);
    taskRepository.save(existing);

    Long existingId = existing.getId();

    assertThatThrownBy(() -> taskService.bulkCompleteTasks(List.of(existingId, 999_999L)))
        .isInstanceOf(TaskNotFoundException.class);

    Task reloaded = taskRepository.findById(existingId).orElseThrow();
    assertThat(reloaded.isCompleted()).isFalse();
  }

  @Test
  void bulkCompleteTasks_completesAllWhenIdsExist() {
    Task first = taskService.createTask(new Task("First", "Desc"));
    Task second = taskService.createTask(new Task("Second", "Desc"));

    taskService.bulkCompleteTasks(List.of(first.getId(), second.getId()));

    assertThat(taskRepository.findById(first.getId()).orElseThrow().isCompleted()).isTrue();
    assertThat(taskRepository.findById(second.getId()).orElseThrow().isCompleted()).isTrue();
  }

  @Test
  void getAllTasksWithAttachments_loadsAttachmentsWithoutNPlusOne() {
    Task task = taskService.createTask(new Task("With files", "Desc"));
    Task reloaded = taskService.getAllTasksWithAttachments().stream()
        .filter(t -> t.getId().equals(task.getId()))
        .findFirst()
        .orElseThrow();

    assertThat(reloaded.getAttachments()).isNotNull();
  }
}

package com.example.todolist.service;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Transactional
  public Task createTask(Task task) {
    if (task.getPriority() == null) {
      task.setPriority(com.example.todolist.model.Priority.MEDIUM);
    }
    if (task.getTags() == null) {
      task.setTags(new java.util.HashSet<>());
    }
    return taskRepository.save(task);
  }

  @Transactional(readOnly = true)
  public Task getTaskById(Long id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));
  }

  @Transactional(readOnly = true)
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @Transactional(readOnly = true)
  public List<Task> getAllTasksWithAttachments() {
    return taskRepository.findAllWithAttachments();
  }

  @Transactional
  public Task updateTask(Long id, Task task) {
    Task existing = taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id));
    existing.setTitle(task.getTitle());
    existing.setDescription(task.getDescription());
    existing.setCompleted(task.isCompleted());
    existing.setDueDate(task.getDueDate());
    existing.setPriority(task.getPriority());
    existing.setTags(task.getTags());
    return taskRepository.save(existing);
  }

  @Transactional
  public void deleteTask(Long id) {
    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException(id);
    }
    taskRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<Task> findByCompletedAndPriority(boolean completed, com.example.todolist.model.Priority priority) {
    return taskRepository.findByCompletedAndPriority(completed, priority);
  }

  @Transactional(readOnly = true)
  public List<Task> findTasksDueWithinNext7Days() {
    LocalDate endDate = LocalDate.now().plusDays(7);
    return taskRepository.findTasksDueWithinDays(endDate);
  }

  @Transactional(
      rollbackFor = Exception.class,
      propagation = Propagation.REQUIRED
  )
  public void bulkCompleteTasks(List<Long> ids) {
    for (Long id : ids) {
      Task task = taskRepository.findById(id)
          .orElseThrow(() -> new TaskNotFoundException(id));
      task.setCompleted(true);
      taskRepository.save(task);
    }
  }
}

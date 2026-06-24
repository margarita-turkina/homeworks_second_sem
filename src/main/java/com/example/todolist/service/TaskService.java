package com.example.todolist.service;


import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final Map<Long, Task> taskCache = new ConcurrentHashMap<>();

  @Autowired
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @PostConstruct
  public void initCache() {
    System.out.println("=== @PostConstruct: Инициализация кэша ===");
    List<Task> allTasks = taskRepository.findAll();
    for (Task task : allTasks) {
      taskCache.put(task.getId(), task);
    }
    System.out.println("Загружено задач в кэш: " + taskCache.size());
  }

  @PreDestroy
  public void cleanup() {
    System.out.println("=== @PreDestroy: Очистка ресурсов ===");
    System.out.println("Кэш содержит задач: " + taskCache.size());
    taskCache.clear();
    System.out.println("Кэш очищен");
  }


  public Task createTask(Task task) {
    Task saved = taskRepository.save(task);
    taskCache.put(saved.getId(), saved);
    return saved;
  }

  public Task getTaskById(Long id) {
    Task cached = taskCache.get(id);
    if (cached != null) {
      return cached;
    }
    return taskRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Task не найдена с id: " + id));
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Task updateTask(Long id, Task task) {
    if (!taskRepository.existsById(id)) {
      throw new RuntimeException("Task не найдена с id: " + id);
    }
    task.setId(id);
    Task updated = taskRepository.update(task);
    taskCache.put(id, updated);
    return updated;
  }

  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
    taskCache.remove(id);
  }
}
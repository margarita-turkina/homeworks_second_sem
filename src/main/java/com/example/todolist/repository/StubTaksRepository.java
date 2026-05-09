package com.example.todolist.repository;

import com.example.todolist.model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StubTaksRepository implements TaskRepository {

  private final List<Task> stubData = new ArrayList<>();
  private Long nextId = 4L;

  public StubTaksRepository() {
    // Предзаполненные задачи для демонстрации
    stubData.add(new Task(1L, "Изучить Spring", "Прочитать документацию", false));
    stubData.add(new Task(2L, "Написать код", "Реализовать todo-list", false));
    stubData.add(new Task(3L, "Протестировать", "Написать unit тесты", true));
  }

  @Override
  public Task save(Task task) {
    Task newTask = new Task(nextId++, task.getTitle(), task.getDescription(), task.isCompleted());
    stubData.add(newTask);
    return newTask;
  }

  @Override
  public Optional<Task> findById(Long id) {
    return stubData.stream()
        .filter(task -> task.getId().equals(id))
        .findFirst();
  }

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(stubData);
  }

  @Override
  public Task update(Task task) {
    Optional<Task> existing = findById(task.getId());
    if (existing.isEmpty()) {
      throw new IllegalArgumentException("Task not found: " + task.getId());
    }
    stubData.remove(existing.get());
    stubData.add(task);
    return task;
  }

  @Override
  public void deleteById(Long id) {
    stubData.removeIf(task -> task.getId().equals(id));
  }

  @Override
  public boolean existsById(Long id) {
    return stubData.stream().anyMatch(task -> task.getId().equals(id));
  }
}
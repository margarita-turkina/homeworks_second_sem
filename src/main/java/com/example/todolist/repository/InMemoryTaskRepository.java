package com.example.todolist.repository;

import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {

  private final Map<Long, Task> storage = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public Task save(Task task) {
    Long newId = idGenerator.getAndIncrement();
    Task newTask = new Task(
        newId,
        task.getTitle(),
        task.getDescription(),
        task.isCompleted(),
        task.getCreatedAt() != null ? task.getCreatedAt() : LocalDateTime.now(),
        task.getDueDate(),
        task.getPriority() != null ? task.getPriority() : Priority.MEDIUM,
        task.getTags() != null ? task.getTags() : new HashSet<>()
    );
    storage.put(newId, newTask);
    return newTask;
  }

  @Override
  public Optional<Task> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public Task update(Task task) {
    if (!storage.containsKey(task.getId())) {
      throw new IllegalArgumentException("Task not found with id: " + task.getId());
    }
    storage.put(task.getId(), task);
    return task;
  }

  @Override
  public void deleteById(Long id) {
    storage.remove(id);
  }

  @Override
  public boolean existsById(Long id) {
    return storage.containsKey(id);
  }
}

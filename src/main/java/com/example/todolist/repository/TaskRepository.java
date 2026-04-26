package com.example.todolist.repository;

import com.example.todolist.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

  Task save(Task task);

  Optional<Task> findById(Long id);

  List<Task> findAll();

  Task update(Task task);

  void deleteById(Long id);

  boolean existsById(Long id);
}
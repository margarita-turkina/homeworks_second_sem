package com.example.todolist.controller;

import com.example.todolist.dto.*;
import com.example.todolist.model.Task;
import com.example.todolist.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @GetMapping
  public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
    List<Task> tasks = taskService.getAllTasks();
    List<TaskResponseDto> response = tasks.stream()
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
    Task task = taskService.getTaskById(id);
    return ResponseEntity.ok(taskMapper.toResponseDto(task));
  }

  @PostMapping
  public ResponseEntity<TaskResponseDto> createTask(
      @Validated(OnCreate.class) @RequestBody TaskCreateDto createDto) {
    Task task = taskMapper.toEntity(createDto);
    Task saved = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(taskMapper.toResponseDto(saved));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponseDto> updateTask(
      @PathVariable Long id,
      @Validated(OnUpdate.class) @RequestBody TaskUpdateDto updateDto) {
    Task existing = taskService.getTaskById(id);
    taskMapper.updateEntity(updateDto, existing);
    Task updated = taskService.updateTask(id, existing);
    return ResponseEntity.ok(taskMapper.toResponseDto(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    try {
      taskService.getTaskById(id);
      taskService.deleteTask(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
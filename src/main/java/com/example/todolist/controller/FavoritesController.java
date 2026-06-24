package com.example.todolist.controller;


import com.example.todolist.dto.TaskResponseDto;
import com.example.todolist.dto.TaskMapper;
import com.example.todolist.service.FavoritesService;
import com.example.todolist.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "Избранные задачи")
public class FavoritesController {

  private final FavoritesService favoritesService;
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @PostMapping("/{taskId}")
  @Operation(summary = "Добавить задачу в избранное")
  public ResponseEntity<Void> addFavorite(@PathVariable Long taskId, HttpSession session) {
    favoritesService.addFavorite(taskId, session);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> removeFavorite(@PathVariable Long taskId, HttpSession session) {
    favoritesService.removeFavorite(taskId, session);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<List<TaskResponseDto>> getFavorites(HttpSession session) {
    Set<Long> favoriteIds = favoritesService.getFavorites(session);
    List<TaskResponseDto> favorites = favoriteIds.stream()
        .map(taskService::getTaskById)
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(favorites);
  }
}
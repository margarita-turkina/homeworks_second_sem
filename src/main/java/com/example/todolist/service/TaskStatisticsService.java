package com.example.todolist.service;

import com.example.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TaskStatisticsService {

  private final TaskRepository primaryRepository;

  private final TaskRepository stubRepository;

  @Autowired
  public TaskStatisticsService(
      TaskRepository primaryRepository,
      @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
    this.primaryRepository = primaryRepository;
    this.stubRepository = stubRepository;
  }

  public void printStatistics() {
    int primaryCount = primaryRepository.findAll().size();
    int stubCount = stubRepository.findAll().size();

    System.out.println("=== Статистика репозиториев ===");
    System.out.println("Primary (InMemory): " + primaryCount + " задач");
    System.out.println("Stub (заглушка): " + stubCount + " задач");
    System.out.println("Разница: " + Math.abs(primaryCount - stubCount));
  }
}
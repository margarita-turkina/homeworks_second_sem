package com.example.todolist.service;

import com.example.todolist.model.Priority;
import com.example.todolist.model.Task;
import com.example.todolist.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TaskStatisticsJdbcServiceTest {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskStatisticsJdbcService statisticsService;

  @Test
  void getTasksCountByPriority_returnsGroupedCounts() {
    Task low = new Task("Low", "Desc");
    low.setPriority(Priority.LOW);
    low.setCreatedAt(LocalDateTime.now());
    taskRepository.save(low);

    Task high1 = new Task("High 1", "Desc");
    high1.setPriority(Priority.HIGH);
    high1.setCreatedAt(LocalDateTime.now());
    taskRepository.save(high1);

    Task high2 = new Task("High 2", "Desc");
    high2.setPriority(Priority.HIGH);
    high2.setCreatedAt(LocalDateTime.now());
    taskRepository.save(high2);

    assertThat(statisticsService.getTasksCountByPriority())
        .filteredOn(dto -> dto.getPriority() == Priority.HIGH)
        .singleElement()
        .satisfies(dto -> assertThat(dto.getCount()).isEqualTo(2));

    assertThat(statisticsService.getTasksCountByPriority())
        .filteredOn(dto -> dto.getPriority() == Priority.LOW)
        .singleElement()
        .satisfies(dto -> assertThat(dto.getCount()).isEqualTo(1));
  }
}

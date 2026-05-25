package com.example.todolist.service;

import com.example.todolist.dto.PriorityCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatisticsService {

  private final TaskStatisticsJdbcService jdbcStatisticsService;

  public List<PriorityCountDto> getTasksCountByPriority() {
    return jdbcStatisticsService.getTasksCountByPriority();
  }
}

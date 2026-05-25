package com.example.todolist.service;

import com.example.todolist.dto.PriorityCountDto;
import com.example.todolist.model.Priority;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatisticsJdbcService {

  private static final String COUNT_BY_PRIORITY_SQL = """
      SELECT priority, COUNT(*) AS task_count
      FROM tasks
      GROUP BY priority
      ORDER BY priority
      """;

  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<PriorityCountDto> priorityCountRowMapper = (rs, rowNum) ->
      new PriorityCountDto(Priority.valueOf(rs.getString("priority")), rs.getLong("task_count"));

  public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<PriorityCountDto> getTasksCountByPriority() {
    return jdbcTemplate.query(COUNT_BY_PRIORITY_SQL, priorityCountRowMapper);
  }
}

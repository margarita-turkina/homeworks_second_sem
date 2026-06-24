package com.example.todolist.config;

import com.example.todolist.repository.StubTaksRepository;
import com.example.todolist.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class Config {

  @Bean
  public TaskRepository stubTaskRepository() {
    return new StubTaksRepository();
  }

  @Bean
  @RequestScope
  public RequestScopedBean requestScopedBean() {
    return new RequestScopedBean();
  }

  public static class RequestScopedBean {
    private final String requestId;
    private final LocalDateTime startTime;

    public RequestScopedBean() {
      this.requestId = UUID.randomUUID().toString();
      this.startTime = LocalDateTime.now();
      System.out.println("Создан RequestScopedBean для запроса: " + requestId);
    }

    public String getRequestId() { return requestId; }
    public LocalDateTime getStartTime() { return startTime; }
  }
}
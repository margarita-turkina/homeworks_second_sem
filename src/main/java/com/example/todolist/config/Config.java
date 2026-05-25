package com.example.todolist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class Config {

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

    public String getRequestId() {
      return requestId;
    }

    public LocalDateTime getStartTime() {
      return startTime;
    }
  }
}

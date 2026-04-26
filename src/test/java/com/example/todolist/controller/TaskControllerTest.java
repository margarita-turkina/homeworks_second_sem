package com.example.todolist.controller;

import com.example.todolist.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/tasks";
  }

  // Позитивные тесты
  @Test
  public void testCreateTask_Success() {
    Task newTask = new Task("Тест", "Описание");
    ResponseEntity<Task> response = restTemplate.postForEntity(getBaseUrl(), newTask, Task.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }

  @Test
  public void testGetAllTasks_Success() {
    ResponseEntity<Task[]> response = restTemplate.getForEntity(getBaseUrl(), Task[].class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void testGetTaskById_Success() {
    Task newTask = new Task("Найти", "По ID");
    ResponseEntity<Task> createResponse = restTemplate.postForEntity(getBaseUrl(), newTask, Task.class);
    Long taskId = createResponse.getBody().getId();

    ResponseEntity<Task> getResponse = restTemplate.getForEntity(getBaseUrl() + "/" + taskId, Task.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  // Негативные тесты
  @Test
  public void testGetTaskById_NotFound() {
    ResponseEntity<Task> response = restTemplate.getForEntity(getBaseUrl() + "/99999", Task.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testUpdateTask_NotFound() {
    Task updatedTask = new Task("Не существует", "Нет такой");
    ResponseEntity<Task> response = restTemplate.exchange(
        getBaseUrl() + "/99999",
        org.springframework.http.HttpMethod.PUT,
        new org.springframework.http.HttpEntity<>(updatedTask),
        Task.class
    );
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testDeleteTask_NotFound() {
    ResponseEntity<Void> response = restTemplate.exchange(
        getBaseUrl() + "/99999",
        org.springframework.http.HttpMethod.DELETE,
        null,
        Void.class
    );
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
package com.example.todolist.controller;

import com.example.todolist.dto.*;
import com.example.todolist.model.Priority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;

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

  @Test
  public void testCreateTask_Success() {
    TaskCreateDto createDto = new TaskCreateDto();
    createDto.setTitle("Тестовая задача");
    createDto.setDescription("Описание");
    createDto.setPriority(Priority.MEDIUM);
    createDto.setDueDate(LocalDate.now().plusDays(7));

    ResponseEntity<TaskResponseDto> response = restTemplate.postForEntity(
        getBaseUrl(), createDto, TaskResponseDto.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getId()).isNotNull();
  }

  @Test
  public void testGetAllTasks_Success() {
    ResponseEntity<TaskResponseDto[]> response = restTemplate.getForEntity(
        getBaseUrl(), TaskResponseDto[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  public void testGetTaskById_NotFound() {
    ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
        getBaseUrl() + "/99999", ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testDeleteTask_NotFound() {
    ResponseEntity<Void> response = restTemplate.exchange(
        getBaseUrl() + "/99999",
        HttpMethod.DELETE,
        null,
        Void.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
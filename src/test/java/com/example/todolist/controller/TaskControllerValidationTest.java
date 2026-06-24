package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.model.Priority;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerValidationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createTask_withBlankTitle_returns400() throws Exception {
    TaskCreateDto dto = new TaskCreateDto();
    dto.setTitle("ab");
    dto.setPriority(Priority.MEDIUM);

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.title").exists());
  }

  @Test
  void createTask_withPastDueDate_returns400() throws Exception {
    TaskCreateDto dto = new TaskCreateDto();
    dto.setTitle("Valid title");
    dto.setPriority(Priority.MEDIUM);
    dto.setDueDate(LocalDate.now().minusDays(1));

    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.dueDate").exists());
  }

  @Test
  void updateTask_withDueDateBeforeCreation_returns400() throws Exception {
    String createResponse = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "title": "Task for update",
                  "priority": "MEDIUM",
                  "dueDate": "%s"
                }
                """.formatted(LocalDate.now().plusDays(5))))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Long id = objectMapper.readTree(createResponse).get("id").asLong();

    mockMvc.perform(put("/api/tasks/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "dueDate": "%s"
                }
                """.formatted(LocalDate.now().minusDays(1))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.details.dueDate").exists());
  }
}

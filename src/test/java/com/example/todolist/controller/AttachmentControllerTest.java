package com.example.todolist.controller;

import com.example.todolist.dto.TaskCreateDto;
import com.example.todolist.model.Priority;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AttachmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void uploadAndListAttachment_success() throws Exception {
    TaskCreateDto createDto = new TaskCreateDto();
    createDto.setTitle("Task with file");
    createDto.setPriority(Priority.MEDIUM);
    createDto.setDueDate(LocalDate.now().plusDays(3));

    String createResponse = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    JsonNode task = objectMapper.readTree(createResponse);
    Long taskId = task.get("id").asLong();

    MockMultipartFile file = new MockMultipartFile(
        "file",
        "notes.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "hello attachment".getBytes()
    );

    mockMvc.perform(multipart("/api/tasks/" + taskId + "/attachments").file(file))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.fileName").value("notes.txt"))
        .andExpect(jsonPath("$.size").value(16));

    mockMvc.perform(get("/api/tasks/" + taskId + "/attachments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].fileName").value("notes.txt"));
  }

  @Test
  void uploadAttachment_forMissingTask_returns404() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "notes.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "data".getBytes()
    );

    mockMvc.perform(multipart("/api/tasks/99999/attachments").file(file))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").exists());
  }
}

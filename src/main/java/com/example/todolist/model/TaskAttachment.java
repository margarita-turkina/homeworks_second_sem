package com.example.todolist.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskAttachment {
  private Long id;
  private Long taskId;
  private String fileName;
  private String storedFileName;  // UUID имя на диске
  private String contentType;
  private long size;
  private LocalDateTime uploadedAt;
}
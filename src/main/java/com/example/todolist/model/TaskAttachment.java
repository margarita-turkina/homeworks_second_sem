package com.example.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_attachments")
@Getter
@Setter
public class TaskAttachment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", nullable = false)
  @JsonIgnore
  private Task task;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "stored_file_name", nullable = false)
  private String storedFileName;

  @Column(name = "content_type", length = 100)
  private String contentType;

  @Column(name = "size_bytes", nullable = false)
  private long size;

  @Column(name = "uploaded_at", nullable = false)
  private LocalDateTime uploadedAt;

  public Long getTaskId() {
    return task != null ? task.getId() : null;
  }
}

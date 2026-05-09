package com.example.todolist.controller;

import com.example.todolist.model.TaskAttachment;
import com.example.todolist.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AttachmentController {

  private final AttachmentService attachmentService;

  @PostMapping("/tasks/{taskId}/attachments")
  public ResponseEntity<TaskAttachment> uploadAttachment(
      @PathVariable Long taskId,
      @RequestParam("file") MultipartFile file) throws IOException {
    TaskAttachment attachment = attachmentService.storeAttachment(taskId, file);
    return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
  }

  @GetMapping("/attachments/{attachmentId}")
  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) throws IOException {
    Resource resource = attachmentService.loadAsResource(attachmentId);
    TaskAttachment attachment = attachmentService.getAttachment(attachmentId)
        .orElseThrow(() -> new RuntimeException("Not found"));

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
        .body(resource);
  }

  @DeleteMapping("/attachments/{attachmentId}")
  public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws IOException {
    attachmentService.deleteAttachment(attachmentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/tasks/{taskId}/attachments")
  public ResponseEntity<List<TaskAttachment>> getTaskAttachments(@PathVariable Long taskId) {
    List<TaskAttachment> attachments = attachmentService.getAttachmentsByTaskId(taskId);
    return ResponseEntity.ok(attachments);
  }
}
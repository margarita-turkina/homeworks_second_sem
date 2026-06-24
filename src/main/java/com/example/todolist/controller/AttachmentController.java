package com.example.todolist.controller;

import com.example.todolist.dto.AttachmentResponseDto;
import com.example.todolist.model.TaskAttachment;
import com.example.todolist.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Attachments", description = "Вложения к задачам")
public class AttachmentController {

  private final AttachmentService attachmentService;

  @PostMapping("/tasks/{taskId}/attachments")
  @Operation(summary = "Загрузить файл к задаче")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Файл загружен"),
      @ApiResponse(responseCode = "404", description = "Задача не найдена")
  })
  public ResponseEntity<AttachmentResponseDto> uploadAttachment(
      @PathVariable Long taskId,
      @RequestParam("file") MultipartFile file) throws IOException {
    TaskAttachment attachment = attachmentService.storeAttachment(taskId, file);
    return ResponseEntity.status(HttpStatus.CREATED).body(AttachmentResponseDto.from(attachment));
  }

  @GetMapping("/attachments/{attachmentId}")
  @Operation(summary = "Скачать файл")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Файл найден"),
      @ApiResponse(responseCode = "404", description = "Файл не найден")
  })
  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) throws IOException {
    Resource resource = attachmentService.loadAsResource(attachmentId);
    TaskAttachment attachment = attachmentService.getAttachment(attachmentId)
        .orElseThrow();

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
        .header(HttpHeaders.CONTENT_TYPE, attachment.getContentType())
        .body(resource);
  }

  @DeleteMapping("/attachments/{attachmentId}")
  @Operation(summary = "Удалить файл")
  public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws IOException {
    attachmentService.deleteAttachment(attachmentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/tasks/{taskId}/attachments")
  @Operation(summary = "Список вложений задачи")
  public ResponseEntity<List<AttachmentResponseDto>> getTaskAttachments(@PathVariable Long taskId) {
    List<AttachmentResponseDto> attachments = attachmentService.getAttachmentsByTaskId(taskId).stream()
        .map(AttachmentResponseDto::from)
        .toList();
    return ResponseEntity.ok(attachments);
  }
}

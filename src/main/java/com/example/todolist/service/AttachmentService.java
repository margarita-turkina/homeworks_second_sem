package com.example.todolist.service;

import com.example.todolist.exception.AttachmentNotFoundException;
import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.model.TaskAttachment;
import com.example.todolist.repository.TaskAttachmentRepository;
import com.example.todolist.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;
  private final TaskRepository taskRepository;

  @Value("${app.upload-dir:uploads}")
  private String uploadDir;

  public TaskAttachment storeAttachment(Long taskId, MultipartFile file) throws IOException {
    if (!taskRepository.existsById(taskId)) {
      throw new TaskNotFoundException(taskId);
    }

    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String originalName = file.getOriginalFilename();
    String storedName = UUID.randomUUID() + "_" + originalName;
    Path filePath = uploadPath.resolve(storedName);

    try (InputStream inputStream = file.getInputStream()) {
      Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    TaskAttachment attachment = new TaskAttachment();
    attachment.setTaskId(taskId);
    attachment.setFileName(originalName);
    attachment.setStoredFileName(storedName);
    attachment.setContentType(file.getContentType());
    attachment.setSize(file.getSize());
    attachment.setUploadedAt(LocalDateTime.now());

    return attachmentRepository.save(attachment);
  }

  public Optional<TaskAttachment> getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId);
  }

  public Resource loadAsResource(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));

    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(filePath.toUri());

    if (resource.exists() && resource.isReadable()) {
      return resource;
    }
    throw new AttachmentNotFoundException(attachmentId);
  }

  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new AttachmentNotFoundException(attachmentId));

    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Files.deleteIfExists(filePath);

    attachmentRepository.deleteById(attachmentId);
  }

  public List<TaskAttachment> getAttachmentsByTaskId(Long taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new TaskNotFoundException(taskId);
    }
    return attachmentRepository.findByTaskId(taskId);
  }
}

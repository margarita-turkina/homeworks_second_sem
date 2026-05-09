package com.example.todolist.service;


import com.example.todolist.model.TaskAttachment;
import com.example.todolist.repository.TaskAttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;

  @Value("${app.upload-dir:uploads}")
  private String uploadDir;

  public TaskAttachment storeAttachment(Long taskId, MultipartFile file) throws IOException {
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String originalName = file.getOriginalFilename();
    String storedName = UUID.randomUUID().toString() + "_" + originalName;
    Path filePath = uploadPath.resolve(storedName);

    file.transferTo(filePath.toFile());

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
        .orElseThrow(() -> new RuntimeException("Attachment not found"));

    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Resource resource = new UrlResource(filePath.toUri());

    if (resource.exists() && resource.isReadable()) {
      return resource;
    }
    throw new RuntimeException("File not found");
  }

  public void deleteAttachment(Long attachmentId) throws IOException {
    TaskAttachment attachment = attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));

    Path filePath = Paths.get(uploadDir).resolve(attachment.getStoredFileName());
    Files.deleteIfExists(filePath);

    attachmentRepository.deleteById(attachmentId);
  }

  public List<TaskAttachment> getAttachmentsByTaskId(Long taskId) {
    return attachmentRepository.findByTaskId(taskId);
  }
}
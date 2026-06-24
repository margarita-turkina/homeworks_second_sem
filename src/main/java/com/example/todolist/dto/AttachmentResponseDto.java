package com.example.todolist.dto;

import com.example.todolist.model.TaskAttachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Метаданные вложения")
public class AttachmentResponseDto {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "report.pdf")
  private String fileName;

  @Schema(example = "1024")
  private long size;

  private LocalDateTime uploadedAt;

  public static AttachmentResponseDto from(TaskAttachment attachment) {
    AttachmentResponseDto dto = new AttachmentResponseDto();
    dto.setId(attachment.getId());
    dto.setFileName(attachment.getFileName());
    dto.setSize(attachment.getSize());
    dto.setUploadedAt(attachment.getUploadedAt());
    return dto;
  }
}

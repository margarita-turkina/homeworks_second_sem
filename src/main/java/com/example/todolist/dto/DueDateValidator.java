package com.example.todolist.dto;

import com.example.todolist.exception.TaskNotFoundException;
import com.example.todolist.service.TaskService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDate;
import java.util.Map;

@Component
public class DueDateValidator implements ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {

  @Autowired
  private TaskService taskService;

  @Override
  public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
    if (dto.getDueDate() == null) {
      return true;
    }

    Long taskId = extractTaskIdFromRequest();
    if (taskId == null) {
      return true;
    }

    try {
      LocalDate createdDate = taskService.getTaskById(taskId).getCreatedAt().toLocalDate();
      if (dto.getDueDate().isBefore(createdDate)) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Due date cannot be before creation date (" + createdDate + ")")
            .addPropertyNode("dueDate")
            .addConstraintViolation();
        return false;
      }
      return true;
    } catch (TaskNotFoundException ex) {
      return true;
    }
  }

  private Long extractTaskIdFromRequest() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes == null) {
      return null;
    }

    Object uriVariables = attributes.getRequest()
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    if (!(uriVariables instanceof Map<?, ?> variables)) {
      return null;
    }

    Object id = variables.get("id");
    if (id == null) {
      return null;
    }

    return Long.valueOf(id.toString());
  }
}

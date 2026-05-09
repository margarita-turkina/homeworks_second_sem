package com.example.todolist.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DueDateValidator implements ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {

  @Override
  public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
    if (dto.getDueDate() == null) {
      return true;
    }
    return true;
  }
}

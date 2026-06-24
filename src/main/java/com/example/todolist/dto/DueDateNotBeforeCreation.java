package com.example.todolist.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DueDateValidator.class)
public @interface DueDateNotBeforeCreation {
  String message() default "Due date cannot be before creation date";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
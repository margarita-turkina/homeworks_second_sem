package com.example.todolist.exception;

import com.example.todolist.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final Environment environment;

  public GlobalExceptionHandler(Environment environment) {
    this.environment = environment;
  }

  @ExceptionHandler(TaskNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTaskNotFound(
      TaskNotFoundException ex, HttpServletRequest request) {
    return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request, null);
  }

  @ExceptionHandler(AttachmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAttachmentNotFound(
      AttachmentNotFoundException ex, HttpServletRequest request) {
    return buildError(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request, null);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    Map<String, Object> details = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error ->
        details.put(error.getField(), error.getDefaultMessage())
    );
    ex.getBindingResult().getGlobalErrors().forEach(error ->
        details.put(error.getObjectName(), error.getDefaultMessage())
    );
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed", request, details);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {
    Map<String, Object> details = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            violation -> violation.getMessage(),
            (first, second) -> first
        ));
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", "Validation failed", request, details);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParameter(
      MissingServletRequestParameterException ex, HttpServletRequest request) {
    Map<String, Object> details = Map.of("parameter", ex.getParameterName());
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request, details);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleUnreadableMessage(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    return buildError(HttpStatus.BAD_REQUEST, "Bad Request", "Malformed JSON request", request, null);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoHandlerFound(
      NoHandlerFoundException ex, HttpServletRequest request) {
    return buildError(HttpStatus.NOT_FOUND, "Not Found", "No handler found for " + ex.getHttpMethod()
        + " " + ex.getRequestURL(), request, null);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request) {
    String message = isProdProfile()
        ? "An unexpected error occurred"
        : ex.getMessage();
    return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", message, request, null);
  }

  private ResponseEntity<ErrorResponse> buildError(
      HttpStatus status,
      String error,
      String message,
      HttpServletRequest request,
      Map<String, Object> details) {
    ErrorResponse body = ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(status.value())
        .error(error)
        .message(message)
        .path(request.getRequestURI())
        .details(details)
        .build();
    return ResponseEntity.status(status).body(body);
  }

  private boolean isProdProfile() {
    return Arrays.asList(environment.getActiveProfiles()).contains("prod");
  }
}

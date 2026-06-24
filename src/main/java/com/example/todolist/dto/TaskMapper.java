package com.example.todolist.dto;

import com.example.todolist.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "completed", constant = "false")
  @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
  Task toEntity(TaskCreateDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  void updateEntity(TaskUpdateDto dto, @MappingTarget Task task);

  TaskResponseDto toResponseDto(Task task);
}

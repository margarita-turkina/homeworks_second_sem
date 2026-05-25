package com.example.todolist.dto;

import com.example.todolist.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriorityCountDto {

  private Priority priority;
  private long count;
}

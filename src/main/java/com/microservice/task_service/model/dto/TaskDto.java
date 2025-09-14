package com.microservice.task_service.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {

    private Long id;
    private String title;
    private LocalDateTime dueDate;
    private String taskStatus;
    private String taskPriority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

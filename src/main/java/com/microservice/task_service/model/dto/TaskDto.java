package com.microservice.task_service.model.dto;

import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {

    private Long id;
    private String title;
    private LocalDateTime dueDate;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
}

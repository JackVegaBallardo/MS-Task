package com.microservice.task_service.model.dto;

import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskUpdateRequestDto {

    private String title;
    private Long createdBy;
    private TaskStatus taskStatus;
    private TaskPriority taskPriority;
    private LocalDateTime dueDate;
}

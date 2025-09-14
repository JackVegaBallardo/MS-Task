package com.microservice.task_service.model.dto;

import com.microservice.task_service.model.entity.TaskPriority;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskSaveRequestDto {

    private String title;
    private Long createdBy;
    private TaskPriority taskPriority;
    private LocalDateTime dueDate;
}

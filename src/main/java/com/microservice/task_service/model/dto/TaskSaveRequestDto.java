package com.microservice.task_service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskSaveRequestDto {

    @NotBlank(message = "El títutlo no puede estar vacío")
    private String title;

    @Positive(message = "El id del creador debe ser positivo")
    private Long createdBy;

    @NotNull(message = "El estado de la tarea es obligatoria")
    private TaskStatus taskStatus;

    @NotNull(message = "La prioridad de la tarea es obligatoria")
    private TaskPriority taskPriority;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm[:ss]")
    private LocalDateTime dueDate;
}

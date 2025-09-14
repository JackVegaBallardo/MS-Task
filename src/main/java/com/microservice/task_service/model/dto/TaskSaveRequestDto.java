package com.microservice.task_service.model.dto;

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

    @NotNull(message = "El id del creador no puede estar nulo")
    @Positive(message = "El id del creador debe ser positivo")
    private Long createdBy;

    @NotNull(message = "El estado de la tarea es obligatoria")
    private TaskStatus taskStatus;

    @NotNull(message = "La prioridad de la tarea es obligatoria")
    private TaskPriority taskPriority;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDateTime dueDate;
}

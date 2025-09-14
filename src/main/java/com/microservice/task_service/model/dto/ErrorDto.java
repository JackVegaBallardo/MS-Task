package com.microservice.task_service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDto {

    private String message;
    private Integer status;
    private LocalDateTime time;
    private String path;
    private Map<?,?> errors;
}

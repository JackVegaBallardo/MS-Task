package com.microservice.task_service.controller.advice;

import com.microservice.task_service.exception.TaskNotFoundException;
import com.microservice.task_service.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> handleTaskNotFoundException (TaskNotFoundException e){
        return ResponseEntity.notFound()
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest webRequest){

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldErrors.put(fe.getField(), fe.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDto.builder()
                        .message("Error en campos de validación")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .time(LocalDateTime.now())
                        .path(webRequest.getDescription(false).replace("uri=", ""))
                        .errors(fieldErrors)
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex, WebRequest webRequest){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorDto.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .time(LocalDateTime.now())
                        .path(webRequest.getDescription(false).replace("uri=",""))
                .build());
    }
}

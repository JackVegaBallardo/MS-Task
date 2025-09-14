package com.microservice.task_service.controller;

import com.microservice.task_service.model.dto.TaskDto;
import com.microservice.task_service.model.dto.TaskSaveRequestDto;
import com.microservice.task_service.model.dto.TaskUpdateRequestDto;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.microservice.task_service.mapper.TaskMapper.TASK_MAPPER;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@Valid @RequestBody TaskSaveRequestDto taskSaveRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(TASK_MAPPER.map(taskService.save(TASK_MAPPER.toEntity(taskSaveRequestDto))));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll(){
        return ResponseEntity.ok(TASK_MAPPER.map(taskService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(TASK_MAPPER.map(taskService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@Valid @RequestBody TaskUpdateRequestDto task, @PathVariable Long id){
        return ResponseEntity.ok(TASK_MAPPER.map(taskService.update(id, TASK_MAPPER.map(task))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

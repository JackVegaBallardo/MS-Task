package com.microservice.task_service.controller;

import com.microservice.task_service.client.UserDirectoryService;
import com.microservice.task_service.model.dto.TaskDto;
import com.microservice.task_service.model.dto.TaskSaveRequestDto;
import com.microservice.task_service.model.dto.TaskUpdateRequestDto;
import com.microservice.task_service.service.TaskService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static com.microservice.task_service.mapper.TaskMapper.TASK_MAPPER;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    
    private final UserDirectoryService userDirectoryService;

    public TaskController(TaskService taskService, UserDirectoryService userDirectoryService) {
        this.taskService = taskService;
        this.userDirectoryService=userDirectoryService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@Valid @RequestBody TaskSaveRequestDto taskSaveRequestDto,@AuthenticationPrincipal Jwt jwt){
         String sub = jwt.getSubject();             
        String issuer = jwt.getIssuer().toString(); 
        Long userId=userDirectoryService.resolveLocalUserIdOrThrow(issuer,sub);
        System.out.println("formado esPostCreate "+sub+issuer + "id:" +userId);
        taskSaveRequestDto.setCreatedBy(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(TASK_MAPPER.map(taskService.save(TASK_MAPPER.toEntity(taskSaveRequestDto))));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> findAll(){
        return ResponseEntity.ok(TASK_MAPPER.map(taskService.findAll()));
    }

    @GetMapping("/id")
    public ResponseEntity<TaskDto> findById (@AuthenticationPrincipal Jwt jwt){
        String sub = jwt.getSubject();             
        String issuer = jwt.getIssuer().toString(); 
        Long userId=userDirectoryService.resolveLocalUserIdOrThrow(issuer,sub);
        System.out.println("formado es "+sub+issuer + "id:" +userId);
        return ResponseEntity.ok(TASK_MAPPER.map(taskService.findById(userId)));
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

    @GetMapping("/user")
    public ResponseEntity<List<TaskDto>> findByCreatedBy(@AuthenticationPrincipal Jwt jwt){
        String sub = jwt.getSubject();             
        String issuer = jwt.getIssuer().toString(); 
        Long userId=userDirectoryService.resolveLocalUserIdOrThrow(issuer,sub);
        System.out.println("formado es "+sub+issuer + "id:" +userId);
        return ResponseEntity.ok(TASK_MAPPER.map(taskService.findByCreatedBy(userId)));
    }
     @GetMapping("/joinable")
    public ResponseEntity<List<TaskDto>> joinablePublicTasks(@AuthenticationPrincipal Jwt jwt) {
        String sub = jwt.getSubject();             
        String issuer = jwt.getIssuer().toString();  
        List<TaskDto> taskDtoLists =TASK_MAPPER.map(taskService.listJoinablePublicTasksForMe(issuer, sub));
        return ResponseEntity.ok(taskDtoLists);
    }
}

package com.microservice.task_service.service;

import com.microservice.task_service.model.entity.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task);
    Task findById(Long id);
    Task update(Long id, Task task);
    void delete(Long id);
    List<Task> findAll();
}

package com.microservice.task_service.service;

import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskStatus;
import com.microservice.task_service.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task save(Task task) {
        task.setTaskStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Override
    public Task update(Long id, Task task) {
        return taskRepository.findById(id).map(t -> {
            t.setUpdated(true);
            t.setTitle(task.getTitle());
            t.setDueDate(task.getDueDate());
            t.setCreatedBy(task.getCreatedBy());
            t.setTaskStatus(task.getTaskStatus());
            return taskRepository.save(t);
        }).orElseThrow();
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }
}

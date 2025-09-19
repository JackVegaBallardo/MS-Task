package com.microservice.task_service.service;

import com.microservice.task_service.client.UserServiceClient;
import com.microservice.task_service.exception.TaskNotFoundException;
import com.microservice.task_service.model.dto.MeTestResponse;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskStatus;
import com.microservice.task_service.model.entity.TaskVisibility;
import com.microservice.task_service.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    private final  UserServiceClient userServiceClient;

    public TaskServiceImpl(TaskRepository taskRepository, UserServiceClient userServiceClient) {
        this.taskRepository = taskRepository;
        this.userServiceClient= userServiceClient;
    }

    @Override
    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {

        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    @Transactional
    public Task update(Long id, Task task) {
        return taskRepository.findById(id).map(t -> {
            t.setUpdated(true);
            t.setTitle(task.getTitle());
            t.setDueDate(task.getDueDate());
            t.setCreatedBy(task.getCreatedBy());
            t.setTaskStatus(task.getTaskStatus());
            t.setTaskPriority(task.getTaskPriority());
            return taskRepository.save(t);
        }).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if(!taskRepository.existsById(id)){
            throw new TaskNotFoundException();
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByCreatedBy(Long createdBy) {
        return taskRepository.findByCreatedBy(createdBy);
    }
    @Transactional(readOnly = true)
    public List<Task> listJoinablePublicTasksForMe(String kcIss, String kcSub) {
        MeTestResponse me = userServiceClient.test(kcIss, kcSub);
        Long myId = me.localUserId();
        List<Long> friendIds = userServiceClient.getMyFriendIds();
        if (friendIds == null || friendIds.isEmpty()) {
            return Collections.emptyList();
        }

        return taskRepository.findJoinablePublicTasksFromFriends(myId, friendIds,TaskVisibility.PUBLIC);
    }
}

package com.microservice.task_service.integration.service;

import com.microservice.task_service.exception.TaskNotFoundException;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import com.microservice.task_service.service.TaskService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    void findByIdTest(){
        Long id = 1L;

        Task task = taskService.findById(id);

        assertNotNull(task);
        assertEquals("Updated Task", task.getTitle());
    }

    @Test
    void findByIdNotFoundTest(){
        Long id = 99L;
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(id));
    }

    @Test
    void saveTaskTest(){
        Task task = new Task();
        task.setTitle("Created Task");
        task.setCreatedBy(2L);
        task.setTaskStatus(TaskStatus.PENDING);
        task.setTaskPriority(TaskPriority.MEDIUM);
        task.setDueDate(LocalDateTime.now().plusDays(5));
        task.setCreatedAt(LocalDateTime.now().minusDays(1));
        task.setUpdatedAt(LocalDateTime.now());

        Task taskSaved = taskService.save(task);

        assertNotNull(taskSaved);
        assertEquals("Created Task", taskSaved.getTitle());
        assertEquals(2L, taskSaved.getCreatedBy());
    }

    @Test
    void updateTaskTest(){
        Long id = 1L;
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task");
        task.setCreatedBy(100L);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setTaskPriority(TaskPriority.MEDIUM);
        task.setDueDate(LocalDateTime.now().plusDays(5));
        task.setCreatedAt(LocalDateTime.now().minusDays(1));
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdated(true);

        Task taskUpdated = taskService.update(id, task);

        assertNotNull(taskUpdated);
        assertEquals("Updated Task", taskUpdated.getTitle());
        assertTrue(taskUpdated.getUpdated());
    }

    @Test
    void updateTaskNotFoundTest(){
        Long id = 99L;
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Updated Task");
        task.setCreatedBy(100L);
        task.setTaskStatus(TaskStatus.IN_PROGRESS);
        task.setTaskPriority(TaskPriority.MEDIUM);
        task.setDueDate(LocalDateTime.now().plusDays(5));
        task.setCreatedAt(LocalDateTime.now().minusDays(1));
        task.setUpdatedAt(LocalDateTime.now());
        task.setUpdated(true);

        assertThrows(TaskNotFoundException.class, () -> taskService.update(id, task));
    }

    @Test
    void deleteTest(){
        Long id = 2L;
        taskService.delete(id);
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(id));
    }

    @Test
    void deleteTaskNotFoundTest(){
        Long id = 99L;
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(id));
    }
}

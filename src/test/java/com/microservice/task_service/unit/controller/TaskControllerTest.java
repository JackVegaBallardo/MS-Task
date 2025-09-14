package com.microservice.task_service.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.task_service.controller.TaskController;
import com.microservice.task_service.exception.TaskNotFoundException;
import com.microservice.task_service.model.dto.TaskSaveRequestDto;
import com.microservice.task_service.model.dto.TaskUpdateRequestDto;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import com.microservice.task_service.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private ObjectMapper objectMapper;
    private Task sampleTask;
    private TaskSaveRequestDto taskSaveRequestDto;
    private TaskUpdateRequestDto taskUpdateRequestDto;

    @BeforeEach
    void init(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        sampleTask = createSampleTask();
        taskSaveRequestDto = createTaskSaveRequestDto();
        taskUpdateRequestDto = createTaskUpdateRequestDto();
    }

    @Test
    void saveTaskTest() throws Exception{

        when(taskService.save(any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskSaveRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"))
                .andExpect(jsonPath("$.taskPriority").value("HIGH"));

        verify(taskService, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskTest() throws Exception{
        Long id = 1L;
        Task updatedTask = createUpdatedTask();

        when(taskService.update(eq(id), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.taskStatus").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.taskPriority").value("MEDIUM"));

        verify(taskService, times(1)).update(eq(id), any(Task.class));
    }

    @Test
    void updateTaskNotFoundTest() throws Exception{
        Long id = 99L;

        when(taskService.update(eq(id), any(Task.class))).thenThrow(new TaskNotFoundException());

        mockMvc.perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskUpdateRequestDto)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).update(eq(id), any(Task.class));
    }

    @Test
    void deleteTest() throws Exception{
        Long id = 1L;
        doNothing().when(taskService).delete(id);

        mockMvc.perform(delete("/api/tasks/{id}",id))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(id);
    }

    @Test
    void deleteTaskNotFoundTest() throws Exception{
        Long id = 99L;
        doThrow(new TaskNotFoundException()).when(taskService).delete(id);

        mockMvc.perform(delete("/api/tasks/{id}",id))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).delete(id);
    }

    @Test
    void findByIdTest() throws Exception{
        Long id = 1L;
        when(taskService.findById(id)).thenReturn(sampleTask);

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"))
                .andExpect(jsonPath("$.taskPriority").value("HIGH"));

        verify(taskService, times(1)).findById(id);
    }

    @Test
    void findByIdTaskNotFoundTest() throws Exception{
        Long id = 99L;
        when(taskService.findById(id)).thenThrow(new TaskNotFoundException());
        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isNotFound());
        verify(taskService, times(1)).findById(id);
    }

    private Task createSampleTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setCreatedBy(100L);
        task.setTaskStatus(TaskStatus.PENDING);
        task.setTaskPriority(TaskPriority.HIGH);
        task.setDueDate(LocalDateTime.now().plusDays(7));
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }

    private Task createUpdatedTask() {
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
        return task;
    }

    private TaskSaveRequestDto createTaskSaveRequestDto() {
        TaskSaveRequestDto dto = new TaskSaveRequestDto();
        dto.setTitle("Test Task");
        dto.setCreatedBy(100L);
        dto.setTaskPriority(TaskPriority.HIGH);
        dto.setDueDate(LocalDateTime.now().plusDays(7));
        return dto;
    }

    private TaskUpdateRequestDto createTaskUpdateRequestDto() {
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto();
        dto.setTitle("Updated Task");
        dto.setCreatedBy(100L);
        dto.setTaskStatus(TaskStatus.IN_PROGRESS);
        dto.setTaskPriority(TaskPriority.MEDIUM);
        dto.setDueDate(LocalDateTime.now().plusDays(5));
        return dto;
    }
}

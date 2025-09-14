package com.microservice.task_service.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microservice.task_service.exception.TaskNotFoundException;
import com.microservice.task_service.model.dto.TaskSaveRequestDto;
import com.microservice.task_service.model.dto.TaskUpdateRequestDto;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @BeforeEach
    void init(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void saveTaskTest() throws Exception{

        TaskSaveRequestDto dto = new TaskSaveRequestDto();
        dto.setTitle("Test Task");
        dto.setCreatedBy(100L);
        dto.setTaskPriority(TaskPriority.HIGH);
        dto.setDueDate(LocalDateTime.now().plusDays(7));
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"))
                .andExpect(jsonPath("$.taskPriority").value("HIGH"));
    }

    @Test
    void updateTaskTest() throws Exception{
        Long id = 1L;
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto();
        dto.setTitle("Updated Task");
        dto.setCreatedBy(100L);
        dto.setTaskStatus(TaskStatus.IN_PROGRESS);
        dto.setTaskPriority(TaskPriority.MEDIUM);
        dto.setDueDate(LocalDateTime.now().plusDays(5));
        mockMvc.perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.taskStatus").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.taskPriority").value("MEDIUM"));

    }

    @Test
    void updateTaskNotFoundTest() throws Exception{
        Long id = 99L;
        TaskUpdateRequestDto dto = new TaskUpdateRequestDto();
        dto.setTitle("Updated Task");
        dto.setCreatedBy(100L);
        dto.setTaskStatus(TaskStatus.IN_PROGRESS);
        dto.setTaskPriority(TaskPriority.MEDIUM);
        dto.setDueDate(LocalDateTime.now().plusDays(5));
        mockMvc.perform(put("/api/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTest() throws Exception{
        Long id = 2L;

        mockMvc.perform(delete("/api/tasks/{id}",id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTaskNotFoundTest() throws Exception{
        Long id = 99L;

        mockMvc.perform(delete("/api/tasks/{id}",id))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdTest() throws Exception{
        Long id = 1L;

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Configurar cobertura de pruebas"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"))
                .andExpect(jsonPath("$.taskPriority").value("HIGH"));
    }

    @Test
    void findCreatedByTest() throws Exception{
        Long id = 1L;

        mockMvc.perform(get("/api/tasks/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Configurar cobertura de pruebas"))
                .andExpect(jsonPath("$[0].taskStatus").value("PENDING"))
                .andExpect(jsonPath("$[0].taskPriority").value("HIGH"));
    }

    @Test
    void findAllTest() throws Exception{

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Configurar cobertura de pruebas"))
                .andExpect(jsonPath("$[0].taskStatus").value("PENDING"))
                .andExpect(jsonPath("$[0].taskPriority").value("HIGH"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Escribir pruebas unitarias del servicio"))
                .andExpect(jsonPath("$[1].taskStatus").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[1].taskPriority").value("MEDIUM"));

    }

    @Test
    void findByIdTaskNotFoundTest() throws Exception{
        Long id = 99L;
        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }
}

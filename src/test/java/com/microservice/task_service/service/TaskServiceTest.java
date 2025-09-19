package com.microservice.task_service.service;

import com.microservice.task_service.client.UserServiceClient;
import com.microservice.task_service.exception.TaskNotFoundException;
import com.microservice.task_service.model.dto.MeTestResponse;
import com.microservice.task_service.model.entity.Task;
import com.microservice.task_service.model.entity.TaskPriority;
import com.microservice.task_service.model.entity.TaskStatus;
import com.microservice.task_service.model.entity.TaskVisibility;
import com.microservice.task_service.repository.TaskRepository;
import com.microservice.task_service.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;

    @BeforeEach
    void init(){
        task = new Task();
        task.setId(1L);
        task.setTitle("Titulo 1");
        task.setCreatedBy(1L);
        task.setTaskPriority(TaskPriority.LOW);
        task.setTaskStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDateTime.now());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void findAllTest(){
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> tasks = taskService.findAll();

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    void findByIdTest(){
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task taskFound = taskService.findById(1L);

        assertNotNull(taskFound);
        assertEquals("Titulo 1", taskFound.getTitle());
        verify(taskRepository).findById(1L);

    }

    @Test
    void findByIdTaskNotFoundTest(){
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, ()-> taskService.findById(2L));
        verify(taskRepository).findById(2L);

    }

    @Test
    void saveTaskTest(){

        Task newTask = new Task();
        task.setTitle("Titulo 1");
        task.setTaskPriority(TaskPriority.LOW);
        task.setTaskStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDateTime.now());
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task taskCreated = taskService.save(newTask);

        assertNotNull(taskCreated);
        assertEquals(1L, taskCreated.getId());
        assertEquals("Titulo 1", taskCreated.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void deleteTest(){
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.delete(1L);

        verify(taskRepository).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTaskNotFoundTest(){
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.delete(99L));
        verify(taskRepository).existsById(99L);
        verify(taskRepository, never()).deleteById(99L);
    }

    @Test
    void updateTaskTest(){

        Task taskToUpdate = new Task();
        taskToUpdate.setId(1L);
        taskToUpdate.setTaskPriority(TaskPriority.LOW);
        taskToUpdate.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskToUpdate.setDueDate(LocalDateTime.now());
        taskToUpdate.setTitle("Titulo actualizado");
        taskToUpdate.setCreatedBy(2L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task taskUpdated = taskService.update(1L, taskToUpdate);

        assertEquals("Titulo actualizado", taskUpdated.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, taskUpdated.getTaskStatus());
        assertEquals(2L, taskUpdated.getCreatedBy());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTaskNotFoundTest(){

        Task taskToUpdate = new Task();
        taskToUpdate.setTaskPriority(TaskPriority.LOW);
        taskToUpdate.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskToUpdate.setDueDate(LocalDateTime.now());
        taskToUpdate.setTitle("Titulo actualizado");
        taskToUpdate.setCreatedBy(2L);
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.update(99L, taskToUpdate));
        verify(taskRepository).findById(99L);
    }

    @Test
    void findByCreatedByTest(){
        Long createdBy = 1L;
        when(taskRepository.findByCreatedBy(createdBy)).thenReturn(List.of(task));

        List<Task> list = taskService.findByCreatedBy(createdBy);

        assertEquals(1, list.size());
    }

    @Test
    void listJoinableTasksNullFriendsTest() {
        String kcIss = "https://kc/realms/master";
        String kcSub = "sub-1";

        MeTestResponse me = mock(MeTestResponse.class);
        when(me.localUserId()).thenReturn(11L);
        when(userServiceClient.test(kcIss, kcSub)).thenReturn(me);

        when(userServiceClient.getMyFriendIds()).thenReturn(null);

        List<Task> result = taskService.listJoinablePublicTasksForMe(kcIss, kcSub);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userServiceClient).test(kcIss, kcSub);
        verify(userServiceClient).getMyFriendIds();
        verifyNoInteractions(taskRepository);
    }

    @Test
    void listJoinableTasksEmptyFriendsTest() {
        String kcIss = "https://kc/realms/master";
        String kcSub = "sub-1";

        MeTestResponse me = mock(MeTestResponse.class);
        when(me.localUserId()).thenReturn(11L);
        when(userServiceClient.test(kcIss, kcSub)).thenReturn(me);

        when(userServiceClient.getMyFriendIds()).thenReturn(List.of());

        List<Task> result = taskService.listJoinablePublicTasksForMe(kcIss, kcSub);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userServiceClient).test(kcIss, kcSub);
        verify(userServiceClient).getMyFriendIds();
        verifyNoInteractions(taskRepository);
    }

    @Test
    void listJoinableTasksWithFriendsTest() {
        String kcIss = "https://kc/realms/master";
        String kcSub = "sub-3";

        MeTestResponse me = mock(MeTestResponse.class);
        when(me.localUserId()).thenReturn(11L);
        when(userServiceClient.test(kcIss, kcSub)).thenReturn(me);

        List<Long> friendIds = List.of(2L, 3L, 5L);
        when(userServiceClient.getMyFriendIds()).thenReturn(friendIds);

        when(taskRepository.findJoinablePublicTasksFromFriends(11L, friendIds, TaskVisibility.PUBLIC))
                .thenReturn(List.of(task));

        List<Task> result = taskService.listJoinablePublicTasksForMe(kcIss, kcSub);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(task.getId(), result.get(0).getId());

        verify(userServiceClient).test(kcIss, kcSub);
        verify(userServiceClient).getMyFriendIds();
        verify(taskRepository).findJoinablePublicTasksFromFriends(11L, friendIds, TaskVisibility.PUBLIC);
    }
}

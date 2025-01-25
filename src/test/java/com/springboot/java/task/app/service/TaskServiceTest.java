package com.springboot.java.task.app.service;

import com.springboot.java.task.app.model.Task;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.repository.TaskRepository;
import com.springboot.java.task.app.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TaskService taskService;

    private User user1;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        user1 = User.builder().id(1L).username("username1").email("test@test.com").password("qwerty123").build();
        task1 = Task.builder().id(1L).title("Task 1").description("Description 1").completed(false).dueDate(LocalDate.now().plusDays(7)).owner(user1).build();
        task2 = Task.builder().id(2L).title("Task 2").description("Description 2").completed(true).dueDate(LocalDate.now()).owner(user1).build();
        task3 = Task.builder().id(3L).title("Task 3").description("Description 3").completed(true).dueDate(null).owner(user1).build();
    }
    @Test
    void TaskService_GetTaskById() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));

        Task result = taskService.getTaskById(1L);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void TaskService_GetTaskById_TaskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> taskService.getTaskById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void TaskService_IsTaskOwner() {
        boolean result = taskService.isTaskOwner(task1, user1);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void TaskService_SaveTask() {
        taskService.save(task1);
        verify(taskRepository).save(task1);
    }

    @Test
    void TaskService_DeleteTask() {
        when(taskRepository.existsById(task1.getId())).thenReturn(true);
        taskService.delete(task1.getId());
        verify(taskRepository).deleteById(task1.getId());
    }
    @Test
    void TaskService_DeleteTask_TaskNotFound() {
        when(taskRepository.existsById(task1.getId())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> taskService.delete(task1.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Task not found");
    }
}
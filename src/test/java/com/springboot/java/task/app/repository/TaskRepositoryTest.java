package com.springboot.java.task.app.repository;

import com.springboot.java.task.app.model.Role;
import com.springboot.java.task.app.model.Task;
import com.springboot.java.task.app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;


    String password = "qwerty123";
    User user1 = User.builder().username("username1").email("test@test.com").password(password).role(Role.USER).build();
    User user2 = User.builder().username("username2").email("test2@test.com").password(password).role(Role.USER).build();

    Task task1 = Task.builder().title("Task 1").description("Description 1").completed(false).dueDate(LocalDate.now().plusDays(7)).owner(user1).build();
    Task task2 = Task.builder().title("Task 2").description("Description 2").completed(true).dueDate(LocalDate.now()).owner(user2).build();
    Task task3 = Task.builder().title("Task 3").description("Description 3").completed(true).dueDate(null).owner(user1).build();
    Task task4 = Task.builder().title("Task 4").description("Description 4").completed(true).dueDate(LocalDate.now().plusDays(27)).owner(user2).build();
    Task task5 = Task.builder().title("Task 5").description("Description 5").completed(false).dueDate(LocalDate.now().plusDays(17)).owner(user1).build();
    Task task6 = Task.builder().title("Task 6").description("Description 6").completed(false).dueDate(LocalDate.now().plusDays(37)).owner(user2).build();
    Task task7 = Task.builder().title("Task 7").description("Description 7").completed(false).dueDate(LocalDate.now().plusDays(47)).owner(user1).build();

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
        System.out.println("Cleaned db");
        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    public void TaskRepository_SaveOne() {
        Task savedTask = taskRepository.save(task2);

        Assertions.assertThat(savedTask).isNotNull();
        Assertions.assertThat(savedTask.getId()).isGreaterThan(0);
    }

    @Test
    public void TaskRepository_SaveMany() {
        List<Task> tasks = List.of(task2, task3, task4);
        List<Task> savedTasks = taskRepository.saveAll(tasks);

        Assertions.assertThat(savedTasks).isNotNull();
        Assertions.assertThat(savedTasks.size()).isEqualTo(3);
    }

    @Test
    public void TaskRepository_GetById() {
        Task savedTask = taskRepository.save(task5);
        Task foundTask = taskRepository.findById(savedTask.getId()).get();

        Assertions.assertThat(foundTask).isNotNull();
        Assertions.assertThat(foundTask.getId()).isEqualTo(savedTask.getId());
    }

    @Test
    public void TaskRepository_GetAll() {
        List<Task> tasks = List.of(task2, task3, task4, task5, task7);
        List<Task> savedTasks = taskRepository.saveAll(tasks);
        List<Task> foundTasks = taskRepository.findAll();

        Assertions.assertThat(foundTasks).isNotNull();
        Assertions.assertThat(foundTasks.size()).isEqualTo(5);
    }

    @Test
    public void TaskRepository_Delete() {
        List<Task> tasks = List.of(task2, task3, task4);
        List<Task> savedTasks = taskRepository.saveAll(tasks);
        taskRepository.deleteById(tasks.getFirst().getId());
        List<Task> foundTasks = taskRepository.findAll();

        Assertions.assertThat(foundTasks).isNotNull();
        Assertions.assertThat(foundTasks.size()).isEqualTo(2);
    }

    @Test
    public void TaskRepository_Update() {
        Task savedTask = taskRepository.save(task5);
        savedTask.setTitle("Updated title");
        Task updatedTask = taskRepository.save(savedTask);

        Assertions.assertThat(updatedTask).isNotNull();
        Assertions.assertThat(updatedTask.getId()).isEqualTo(savedTask.getId());
        Assertions.assertThat(updatedTask.getTitle()).isEqualTo("Updated title");
    }

    @Test
    public void TaskRepository_FindByOwnerId_Test() {
        List<Task> savedTasks = taskRepository.saveAll(List.of(task1, task2, task3, task4, task5, task6, task7));
        List<Task> savedTask = taskRepository.findByOwnerId(user1.getId());

        Assertions.assertThat(savedTask).isNotNull();
        Assertions.assertThat(savedTask.size()).isEqualTo(4);

    }
}
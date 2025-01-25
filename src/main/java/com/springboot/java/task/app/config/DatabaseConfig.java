package com.springboot.java.task.app.config;

import com.springboot.java.task.app.model.Role;
import com.springboot.java.task.app.model.Task;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.repository.TaskRepository;
import com.springboot.java.task.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DatabaseConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Bean
    CommandLineRunner dbCommandLineRunner(UserRepository userRepository, TaskRepository taskRepository) {
        return args -> {
            LocalDateTime date = LocalDateTime.now();
            String password = passwordEncoder.encode("qwerty123");
            User user1 = User.builder().username("username1").email("asd@asd.asd").password(password).role(Role.USER).build();
            User user2 = User.builder().username("username2").email("asd2@asd.asd").password(password).role(Role.USER).build();

            userRepository.saveAll(List.of(user1, user2));
            Task task1 = Task.builder().title("Task 1").description("Description 1").completed(false).dueDate(LocalDate.now().plusDays(7)).owner(user1).build();
            Task task2 = Task.builder().title("Task 2").description("Description 2").completed(true).dueDate(LocalDate.now()).owner(user2).build();
            Task task3 = Task.builder().title("Task 3").description("Description 3").completed(true).dueDate(null).owner(user1).build();
            Task task4 = Task.builder().title("Task 4").description("Description 4").completed(true).dueDate(LocalDate.now().plusDays(27)).owner(user2).build();
            Task task5 = Task.builder().title("Task 5").description("Description 5").completed(false).dueDate(LocalDate.now().plusDays(17)).owner(user1).build();
            Task task6 = Task.builder().title("Task 6").description("Description 6").completed(false).dueDate(LocalDate.now().plusDays(37)).owner(user2).build();
            Task task7 = Task.builder().title("Task 7").description("Description 7").completed(false).dueDate(LocalDate.now().plusDays(47)).owner(user1).build();

            taskRepository.saveAll(List.of(task1, task2,task3,  task4, task5, task6, task7));
        };
    }
}

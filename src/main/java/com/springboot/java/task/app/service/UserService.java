package com.springboot.java.task.app.service;

import com.springboot.java.task.app.dto.TaskDTO;
import com.springboot.java.task.app.dto.UserDTO;
import com.springboot.java.task.app.dto.UserTasksDTO;
import com.springboot.java.task.app.model.Task;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtService jwtService;


    // DTO = Data Transfer Object
    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getUpdatedAt(), user.getCreatedAt());
    }
    private List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream()
                .map(this::convertToDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    private UserTasksDTO convertToUserTasksDTO(User user) {
        List<Task> tasks = taskService.getAllTasksByOwnerId(user.getId());
        List<TaskDTO> tasksDTO = taskService.convertToDTOList(tasks);
        return new UserTasksDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getUpdatedAt(), user.getCreatedAt(),tasksDTO);
    }
    private List<UserTasksDTO> convertToUserTasksDTOList(List<User> users) {
        return userRepository.findAll().stream().map(this::convertToUserTasksDTO).collect(java.util.stream.Collectors.toList());
    }

    // Internal
    public User getUserByToken(String token) {
        String username = jwtService.extractUsername(token.substring(7));
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // External
    public UserDTO getUserByTokenDTO(String token) {
        return convertToDTO(getUserByToken(token));
    }
    public UserDTO getUserByUsernameDTO(String username) {
        return convertToDTO(getUserByUsername(username));
    }
    public List<UserDTO> getAllUsersDTO() {
        return convertToDTOList(userRepository.findAll());
    }
    public List<UserTasksDTO> getAllUsersWithTasksDTO() {
        return convertToUserTasksDTOList(userRepository.findAll());
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        System.out.println(passwordEncoder.encode(oldPassword));
        System.out.println(oldPassword);
        System.out.println(user.getPassword());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        } else {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }
}
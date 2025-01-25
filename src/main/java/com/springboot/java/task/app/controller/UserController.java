package com.springboot.java.task.app.controller;

import com.springboot.java.task.app.dto.UserDTO;
import com.springboot.java.task.app.service.JwtService;
import com.springboot.java.task.app.service.TaskService;
import com.springboot.java.task.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtService jwtService;


    @GetMapping("/user/")
    public UserDTO getUserById(@RequestHeader("Authorization") String token) {
        return userService.getUserByTokenDTO(token);
    }

    @PostMapping("/users/")
    public List<? extends UserDTO> getAllUsers(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        if ("user_tasks".equals(type)) {
            return userService.getAllUsersWithTasksDTO();
        } else {
            return userService.getAllUsersDTO();
        }
    }

    @PutMapping("/change-password/")
    public String changePassword(@RequestHeader("Authorization") String token,
                                 @RequestParam String old_password,
                                 @RequestParam String new_password){
        String username = jwtService.extractUsername(token.substring(7));
        userService.changePassword(username, old_password, new_password);

        return "Successfully changed password";
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "Successfully deleted user";
    }
}
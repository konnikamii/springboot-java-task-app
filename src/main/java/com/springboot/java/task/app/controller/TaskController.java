package com.springboot.java.task.app.controller;

import com.springboot.java.task.app.dto.TaskDTO;
import com.springboot.java.task.app.model.Task;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.service.TaskService;
import com.springboot.java.task.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskController {
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;

    // Get all tasks
    @PostMapping("/tasks/")
    public Map<String, Object> getAllTasks(@RequestHeader("Authorization") String token,
                                           @RequestBody Map<String, Object> request) {
        User user = userService.getUserByToken(token);
        int page = (int) request.get("page");
        int pageSize = (int) request.get("page_size");
        String sortBy = (String) request.get("sort_by");
        String sortType = (String) request.get("sort_type");
        Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortBy);
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, sort);
        Page<TaskDTO> taskPage = taskService.getAllTasksByOwnerIdDTO(user.getId(), pageRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("total_tasks", taskPage.getTotalElements());
        response.put("tasks", taskPage.getContent());

        return response;
    }

    // Get task by id
    @GetMapping("/task/{id}")
    public TaskDTO getTaskById(@RequestHeader("Authorization") String token,
                               @PathVariable Long id) {
        User user = userService.getUserByToken(token);
        Task task = taskService.getTaskById(id);
        if (!taskService.isTaskOwner(task, user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return taskService.convertToDTO(task);
    }

    // Create task
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/task/")
    public String createTask(@RequestHeader("Authorization") String token,
                             @Valid @RequestBody Task task) {
        User user = userService.getUserByToken(token);
        task.setOwner(user);
        taskService.save(task);
        return "Successfully created task";
    }

    // Update task
    @PutMapping("/task/{id}")
    public String updateTask(@RequestHeader("Authorization") String token,
                             @PathVariable Long id,
                             @Valid @RequestBody Task updatedTask) {
        User user = userService.getUserByToken(token);
        Task existingTask = taskService.getTaskById(id);
        if (!taskService.isTaskOwner(existingTask, user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.getCompleted());
        existingTask.setDueDate(updatedTask.getDueDate());
        taskService.save(existingTask);
        return "Successfully updated task";
    }

    // Delete task
    @DeleteMapping("/task/{id}")
    public String deleteTask(@RequestHeader("Authorization") String token,
                             @PathVariable Long id) {
        User user = userService.getUserByToken(token);
        Task task = taskService.getTaskById(id);
        if (!taskService.isTaskOwner(task, user)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        taskService.delete(id);
        return "Successfully deleted task";
    }
}
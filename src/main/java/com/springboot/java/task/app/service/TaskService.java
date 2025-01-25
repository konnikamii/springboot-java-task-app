package com.springboot.java.task.app.service;

import com.springboot.java.task.app.dto.TaskDTO;
import com.springboot.java.task.app.model.Task;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    // DTO = Data Transfer Object
    public TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getCompleted(), task.getDueDate(), task.getUpdatedAt(), task.getCreatedAt());
    }
    public List<TaskDTO> convertToDTOList(List<Task> tasks) {
        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Internal
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
    }
    public List<Task> getAllTasksByOwnerId(Long owner_id) {
        return taskRepository.findByOwnerId(owner_id);
    }

    // External
    public TaskDTO getTaskByIdDTO(Long id) {
        return convertToDTO(getTaskById(id));
    }
    public List<TaskDTO> getAllTasksByOwnerIdDTO(Long owner_id) {
        return convertToDTOList(getAllTasksByOwnerId(owner_id));
    }
    public Page<TaskDTO> getAllTasksByOwnerIdDTO(Long ownerId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllByOwnerId(ownerId, pageable);
        return tasks.map(this::convertToDTO);
    }


    public boolean isTaskOwner(Task task, User user) {
        return Objects.equals(task.getOwner().getId(), user.getId());
    }
    public void save(Task task) {
        taskRepository.save(task);
    }
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not founds");
        }
        taskRepository.deleteById(id);
    }

}
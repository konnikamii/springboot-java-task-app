package com.springboot.java.task.app.dto;

import com.springboot.java.task.app.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTasksDTO extends UserDTO {
    private List<TaskDTO> tasks;

    public UserTasksDTO(Long id, String username, String email, Role role, LocalDateTime updatedAt, LocalDateTime createdAt, List<TaskDTO> tasks) {
        super(id, username, email, role, updatedAt, createdAt);
        this.tasks = tasks;
    }
}

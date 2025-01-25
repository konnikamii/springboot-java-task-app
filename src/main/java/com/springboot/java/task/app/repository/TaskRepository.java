package com.springboot.java.task.app.repository;

import com.springboot.java.task.app.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerId(Long ownerId);

    Page<Task> findAllByOwnerId(Long ownerId, Pageable pageable);
}

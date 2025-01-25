package com.springboot.java.task.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springboot.java.task.app.dto.TaskDTO;
import com.springboot.java.task.app.model.Task;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.logging.Logger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;
    private static final Logger logger = Logger.getLogger(UserControllerIntegrationTest.class.getName());

    @BeforeEach
    void setUp() throws Exception {
        token = obtainAccessToken("username1", "qwerty123");
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/login/")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Map<String, String> responseMap = new ObjectMapper().readValue(response, Map.class);
        return responseMap.get("access_token");
    }


    @Test
    @Order(1)
    public void TaskControllerIntegration_GetAllTasks() throws Exception {
        Map<String, Object> request = Map.of(
                "page", 1,
                "page_size", 10,
                "sort_by", "createdAt",
                "sort_type", "ASC"
        );
        String requestJson = new ObjectMapper().writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = new ObjectMapper().readValue(responseContent, Map.class);
        Assertions.assertThat(responseMap).isNotNull();
        Assertions.assertThat(responseMap.get("total_tasks")).isNotNull();
        Assertions.assertThat(responseMap.get("tasks")).isNotNull();
    }

    @Test
    @Order(2)
    public void TaskControllerIntegration_GetTaskById() throws Exception {
        Long taskId = 3L;

        MvcResult result = mockMvc.perform(get("/api/task/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TaskDTO task = objectMapper.readValue(responseContent, TaskDTO.class);
        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isEqualTo(taskId);
    }

    @Test
    @Order(3)
    public void TaskControllerIntegration_CreateTask() throws Exception {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Task Description");
        task.setCompleted(false);
        task.setDueDate(null);

        String taskJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(task);

        MvcResult result = mockMvc.perform(post("/api/task/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Assertions.assertThat(responseContent).isNotNull();
    }

    @Test
    @Order(4)
    public void TaskControllerIntegration_UpdateTask() throws Exception {
        Long taskId = 3L;
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Task Description");
        updatedTask.setCompleted(true);
        updatedTask.setDueDate(null);

        String taskJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(updatedTask);

        MvcResult result = mockMvc.perform(put("/api/task/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Assertions.assertThat(responseContent).isNotNull();
    }

    @Test
    @Order(5)
    public void TaskControllerIntegration_DeleteTask() throws Exception {
        Long taskId = 1L;

        MvcResult result = mockMvc.perform(delete("/api/task/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        Assertions.assertThat(responseContent).isNotNull();
    }

}
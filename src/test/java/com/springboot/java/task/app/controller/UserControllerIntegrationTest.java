package com.springboot.java.task.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springboot.java.task.app.dto.UserDTO;
import com.springboot.java.task.app.dto.UserTasksDTO;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Logger;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

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
    public void UserControllerIntegration_GetUserById() throws Exception {
        mockMvc.perform(get("/api/user/")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(2)
    public void UserControllerIntegration_GetAllUsers() throws Exception {
        Map<String, String> request = Map.of("type", "default");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // Perform the POST request and check the response status
        MvcResult result = mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response content
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = result.getResponse().getContentAsString();
        UserDTO[] users = objectMapper.readValue(responseContent, UserDTO[].class);
        Arrays.sort(users, Comparator.comparing(UserDTO::getUsername));

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.length).isEqualTo(2);
        Assertions.assertThat(users[0].getUsername()).isEqualTo("username1");
        Assertions.assertThat(users[1].getUsername()).isEqualTo("username2");
    }

    @Test
    @Order(3)
    public void UserControllerIntegration_GetAllUsersWithTasks() throws Exception {
        Map<String, String> request = Map.of("type", "user_tasks");
        String requestJson = new ObjectMapper().writeValueAsString(request);

        // Perform the POST request and check the response status
        MvcResult result = mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        // Parse the response content
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String responseContent = result.getResponse().getContentAsString();
        UserTasksDTO[] users = objectMapper.readValue(responseContent, UserTasksDTO[].class);
        Arrays.sort(users, Comparator.comparing(UserTasksDTO::getUsername));

        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.length).isEqualTo(2);
        Assertions.assertThat(users[0].getUsername()).isEqualTo("username1");
        Assertions.assertThat(users[1].getUsername()).isEqualTo("username2");
    }

    @Test
    @Order(4)
    public void UserControllerIntegration_ChangePassword() throws Exception {
        // Prepare the request parameters
        String oldPassword = "qwerty123";
        String newPassword = "newpassword123";

        // Perform the PUT request and check the response status
        MvcResult result = mockMvc.perform(put("/api/change-password/")
                        .param("old_password", oldPassword)
                        .param("new_password", newPassword)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        // Check the response content
        String responseContent = result.getResponse().getContentAsString();
        Assertions.assertThat(responseContent).isEqualTo("Successfully changed password");
        // Reset the password back to the original value
        mockMvc.perform(put("/api/change-password/")
                        .param("old_password", newPassword)
                        .param("new_password", oldPassword)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
    }
}
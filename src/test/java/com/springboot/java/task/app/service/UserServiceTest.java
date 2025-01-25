package com.springboot.java.task.app.service;

import com.springboot.java.task.app.model.Role;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    String password = "qwerty123";
    String username1 = "username1";
    User user1 = User.builder().id(1L).username(username1).email("test@test.com").password(password).role(Role.USER).build();

    @Test
    public void UserService_GetUserByUsername() {

        when(userRepository.findByUsername(username1)).thenReturn(Optional.of(user1));

        User foundUser = userService.getUserByUsername(username1);

        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserService_DeleteUserById() {
        when(userRepository.existsById(user1.getId())).thenReturn(true);
        doNothing().when(userRepository).deleteById(user1.getId());

        assertAll(() -> userService.deleteUser(user1.getId()));
    }

    @Test
    public void UserService_ChangePassword() {
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user1));
        when(passwordEncoder.matches(oldPassword, user1.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(oldPassword)).thenReturn("encodedOldPassword");
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.changePassword(user1.getUsername(), oldPassword, newPassword);

        verify(userRepository).save(user1);
        assertEquals("encodedNewPassword", user1.getPassword());
        Assertions.assertThat(user1.getPassword()).isNotEqualTo(oldPassword);
    }

}
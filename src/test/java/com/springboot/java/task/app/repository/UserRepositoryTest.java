package com.springboot.java.task.app.repository;

import com.springboot.java.task.app.model.Role;
import com.springboot.java.task.app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    String password = "qwerty123";
    User user1 = User.builder().username("username1").email("test@test.com").password(password).role(Role.USER).build();
    User user2 = User.builder().username("username2").email("test2@test.com").password(password).role(Role.USER).build();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        System.out.println("Deleted all users");
    }

    @Test
    public void UserRepository_SaveOne() {
        User savedUser = userRepository.save(user1);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_SaveMany() {
        List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));

        Assertions.assertThat(savedUsers).isNotNull();
        Assertions.assertThat(savedUsers.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_GetById() {
        List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));
        User user = userRepository.findById(savedUsers.getFirst().getId()).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getId()).isEqualTo(savedUsers.getFirst().getId());
    }

    @Test
    public void UserRepository_GetByUsername() {
        List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));
        User user = userRepository.findByUsername(savedUsers.getFirst().getUsername()).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo(savedUsers.getFirst().getUsername());
    }

    @Test
    public void UserRepository_GetByEmail() {
        List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));
        User user = userRepository.findByEmail(savedUsers.getFirst().getEmail()).orElse(null);

        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getEmail()).isEqualTo(savedUsers.getFirst().getEmail());
    }

    @Test
    public void UserRepository_DeleteById() {
        List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));
        userRepository.deleteById(savedUsers.getFirst().getId());
        List<User> usersTasks = userRepository.findAll();

        Assertions.assertThat(userRepository.findById(savedUsers.getFirst().getId())).isEmpty();
        Assertions.assertThat(usersTasks.size()).isEqualTo(1);
    }

    @Test
    public void UserRepository_Update() {
        List<User> savedUsers = userRepository.saveAll(List.of(user1, user2));
        User user = savedUsers.getFirst();
        user.setUsername("newUsername");
        user.setEmail("newEmail");
        user.setPassword("newPassword");
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        User updatedUser = userRepository.findById(user.getId()).orElse(null);

        Assertions.assertThat(updatedUser).isNotNull();
        Assertions.assertThat(updatedUser.getUsername()).isEqualTo("newUsername");
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("newEmail");
        Assertions.assertThat(updatedUser.getPassword()).isEqualTo("newPassword");
        Assertions.assertThat(updatedUser.getRole()).isEqualTo(Role.ADMIN);
    }
}
package com.foodweek.demo;

import com.foodweek.demo.model.User;
import com.foodweek.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPerformCrudOnUser() {
        // CREATE
        User user = new User();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setPassword("securepassword");
        User savedUser = userRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getUsername()).isEqualTo("alice");

        // READ
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("alice@example.com");

        // UPDATE
        savedUser.setEmail("updated@example.com");
        User updatedUser = userRepository.save(savedUser);
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");

        // DELETE
        userRepository.deleteById(updatedUser.getId());
        Optional<User> deletedUser = userRepository.findById(updatedUser.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void shouldFindByUsernameAndEmail() {
        // Préparer un user
        User user = new User();
        user.setUsername("bob");
        user.setEmail("bob@example.com");
        user.setPassword("pass");
        userRepository.save(user);

        // findByUsername
        Optional<User> byUsername = userRepository.findByUsername("bob");
        assertThat(byUsername).isPresent();
        assertThat(byUsername.get().getEmail()).isEqualTo("bob@example.com");

        // findByEmail
        Optional<User> byEmail = userRepository.findByEmail("bob@example.com");
        assertThat(byEmail).isPresent();
        assertThat(byEmail.get().getUsername()).isEqualTo("bob");
    }
}

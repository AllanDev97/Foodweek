package com.foodweek.demo;

import com.foodweek.demo.controller.JsonWebTokenController;
import com.foodweek.demo.dto.UserDTO;
import com.foodweek.demo.model.User;
import com.foodweek.demo.repository.UserRepository;
import com.foodweek.demo.security.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonWebTokenControllerUnitTest {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private JsonWebTokenController controller;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtUtil = mock(JwtUtil.class);
        passwordEncoder = mock(PasswordEncoder.class);

        controller = new JsonWebTokenController(userRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void shouldReturnTokenOnValidLogin() {
        // Given
        User user = new User();
        user.setUsername("admin");
        user.setPassword("hashed-password");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("admin", "hashed-password")).thenReturn(true);
        when(jwtUtil.generateToken("admin")).thenReturn("fake-token");

        UserDTO login = new UserDTO();
        login.setUsername("admin");
        login.setPassword("admin");

        // When
        Map<String, String> response = controller.login(login);

        // Then
        assertTrue(response.containsKey("token"));
        assertEquals("fake-token", response.get("token"));
    }

    @Test
    void shouldReturnErrorOnInvalidLogin() {
        // Given
        User user = new User();
        user.setUsername("admin");
        user.setPassword("hashed-password");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed-password")).thenReturn(false);

        UserDTO login = new UserDTO();
        login.setUsername("admin");
        login.setPassword("wrongpassword");

        // When
        Map<String, String> response = controller.login(login);

        // Then
        assertTrue(response.containsKey("error"));
        assertEquals("Identifiants invalides", response.get("error"));
    }
}


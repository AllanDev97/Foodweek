package com.foodweek.demo.controller;

import com.foodweek.demo.model.User;
import com.foodweek.demo.dto.UserDTO;
import com.foodweek.demo.repository.UserRepository;
import com.foodweek.demo.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class JsonWebTokenController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public JsonWebTokenController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserDTO authRequest) {
        Map<String, String> response = new HashMap<>();

        // Vérifier si l'admin existe, sinon le créer
        Optional<User> adminOptional = userRepository.findByUsername("admin");

        if (adminOptional.isEmpty()) {
            String hashedPassword = passwordEncoder.encode("admin");

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(hashedPassword);

            userRepository.save(adminUser);
        }

        // Vérifier si l'utilisateur existe
        Optional<User> playerOptional = userRepository.findByUsername(authRequest.getUsername());

        if (playerOptional.isPresent()) {
            User player = playerOptional.get();

            if (passwordEncoder.matches(authRequest.getPassword(), player.getPassword())) {
                String token = jwtUtil.generateToken(player.getUsername());
                response.put("token", token);
                return response;
            }
        }

        response.put("error", "Identifiants invalides");
        return response;
    }
}


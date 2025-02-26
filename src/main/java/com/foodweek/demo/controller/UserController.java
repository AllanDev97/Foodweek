package com.foodweek.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.foodweek.demo.dto.UserDTO;
import com.foodweek.demo.model.User;
import com.foodweek.demo.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 🔹 Route pour créer un utilisateur avec un mot de passe haché
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        // Vérifier si l'email ou le username existent déjà
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Le nom d'utilisateur existe déjà.");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("L'email est déjà utilisé.");
        }

        // Hacher le mot de passe
        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        // Créer un nouvel utilisateur
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(hashedPassword); // Stocker le mot de passe haché

        // Enregistrer en base de données
        userRepository.save(newUser);

        return ResponseEntity.ok("Utilisateur créé avec succès.");
    }
}


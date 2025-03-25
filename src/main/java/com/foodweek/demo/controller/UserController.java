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

    // CREATE : Créer un utilisateur
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Le nom d'utilisateur existe déjà.");
        }
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("L'email est déjà utilisé.");
        }

        String hashedPassword = passwordEncoder.encode(userDTO.getPassword());

        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(hashedPassword);

        userRepository.save(newUser);
        return ResponseEntity.ok("Utilisateur créé avec succès.");
    }

    // READ : Récupérer un utilisateur par son ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // READ ALL : Récupérer tous les utilisateurs
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // UPDATE : Mettre à jour un utilisateur
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();

        // Vérifie si le nouveau username ou email est déjà utilisé par un autre utilisateur
        if (!user.getUsername().equals(userDTO.getUsername()) &&
            userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Le nom d'utilisateur est déjà pris.");
        }

        if (!user.getEmail().equals(userDTO.getEmail()) &&
            userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("L'email est déjà utilisé.");
        }

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            String hashedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(hashedPassword);
        }

        userRepository.save(user);
        return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
    }

    // DELETE : Supprimer un utilisateur
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès.");
    }
}



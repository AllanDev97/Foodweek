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

    private final UserRepository playerRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //  Définition correcte

    @Autowired
    public JsonWebTokenController(UserRepository playerRepository, JwtUtil jwtUtil) {
        this.playerRepository = playerRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody UserDTO authRequest) {
        Map<String, String> response = new HashMap<>();

        //  Vérifier si l'admin existe, sinon le créer
        Optional<User> adminOptional = playerRepository.findByUsername("admin");

        if (adminOptional.isEmpty()) {
            String hashedPassword = passwordEncoder.encode("admin"); // Hachage sécurisé du mot de passe

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(hashedPassword);

            playerRepository.save(adminUser); //  Création d'un admin si inexistant
        }

        //  Vérifier si l'utilisateur existe
        Optional<User> playerOptional = playerRepository.findByUsername(authRequest.getUsername());

        if (playerOptional.isPresent()) {
            User player = playerOptional.get();

            //  Vérification du mot de passe haché
            if (passwordEncoder.matches(authRequest.getPassword(), player.getPassword())) {
                String token = jwtUtil.generateToken(player.getUsername()); //  Plus de `NullPointerException`
                response.put("token", token);
                return response;
            }
        }

        //  Authentification échouée
        response.put("error", "Identifiants invalides");
        return response;
    }
}

package com.example.zwergigel.controller;

import com.example.zwergigel.entity.User;
import com.example.zwergigel.repository.UserRepository;
import com.example.zwergigel.service.TokenService;
import com.example.zwergigel.util.Secure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody Map<String, String> dto) {
        final User user = new User(dto.get("login"), Secure.encode(dto.get("password")),dto.get("name"));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<UUID> signIn(@RequestBody Map<String, String> dto) {
        return userRepository.findByLoginAndPassword(dto.get("login"), Secure.encode(dto.get("password")))
            .map(tokenService::add)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}

package com.example.demo.user;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.dto.RegisterRequestDTO;
import com.example.demo.user.dto.UserResponseDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")

public class UserController {
    private final UserService userService;
    private final UserRepository repository;

    public UserController(UserRepository repository, UserService userService) {
        this.userService = userService;
        this.repository = repository;
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        UserResponseDTO response = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public List<UserResponseDTO> listAll() {
        return repository.findAll()
            .stream()
            .map(user -> new UserResponseDTO(user.getId(), user.getEmail(), user.getRole().name()))
            .toList();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") UUID id) {
        return repository.findById(id).orElse(null);
    }

    // TODO: tratar de erro da página
    
}

package com.example.demo.user;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")

public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }
    @GetMapping
    public List<User> listAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable("id") UUID id) {
        return repository.findById(id).orElse(null);
    }

    // TODO: tratar de erro da página
    
}

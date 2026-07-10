package com.example.demo.user;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.user.dto.LoginRequestDTO;
import com.example.demo.user.dto.LoginResponseDTO;
import com.example.demo.user.dto.RegisterRequestDTO;
import com.example.demo.user.dto.UserResponseDTO;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") //
    public List<UserResponseDTO> listAll() {
        return repository.findAll()
            .stream()
            .map(user -> new UserResponseDTO(user.getId(), user.getEmail(), user.getRole().name()))
            .toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable("id") UUID id) {
        User target = repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("email não encontrado"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String requesterEmail = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream()
        .anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = target.getEmail().equals(requesterEmail);

        if (!isAdmin && !isOwner) {
            throw new org.springframework.security.authorization.AuthorizationDeniedException("No hay permisso para acessar perdón.");
        }
        return new UserResponseDTO(target.getId(), target.getEmail(), target.getRole().name());
    }
}

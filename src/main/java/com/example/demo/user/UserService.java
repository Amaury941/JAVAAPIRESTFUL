package com.example.demo.user;

import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.security.JwtService;
import com.example.demo.user.dto.LoginRequestDTO;
import com.example.demo.user.dto.LoginResponseDTO;
import com.example.demo.user.dto.RegisterRequestDTO;
import com.example.demo.user.dto.UserResponseDTO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    // injections
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        User user = new User();
        user.setId(null);
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.VIEWER); // role padrão, ninguém se autopromove a ADMIN aqui

        User saved = userRepository.save(user);

        return new UserResponseDTO(
            saved.getId(),
            saved.getEmail(),
            saved.getRole().name()
        );
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
    User user = userRepository.findByEmail(dto.email())
        .orElseThrow(() -> new InvalidCredentialsException("Email ou senha inválidos"));

    if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
        throw new InvalidCredentialsException("Email ou senha inválidos");
    }

    String token = jwtService.generateToken(user.getEmail());
    return new LoginResponseDTO(token);
    }
}
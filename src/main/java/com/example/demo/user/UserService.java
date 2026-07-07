package com.example.demo.user;

import com.example.demo.user.dto.RegisterRequestDTO;
import com.example.demo.user.dto.UserResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(RegisterRequestDTO dto) throws EmailAlreadyExistsException {
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email já cadastrado");
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER); // role padrão, ninguém se autopromove a ADMIN aqui

        User saved = userRepository.save(user);

        return new UserResponseDTO(
            saved.getId(),
            saved.getEmail(),
            saved.getRole().name()
        );
    }
}
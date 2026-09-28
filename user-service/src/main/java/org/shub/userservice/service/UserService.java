package org.shub.userservice.service;

import org.shub.userservice.Interfaces.IUserService;
import org.shub.userservice.dto.AuthResponse;
import org.shub.userservice.dto.LoginRequest;
import org.shub.userservice.dto.RegisterRequest;
import org.shub.userservice.entity.User;
import org.shub.userservice.exception.DuplicateEmailException;
import org.shub.userservice.exception.InvalidCredentialsException;
import org.shub.userservice.repository.UserRepository;
import org.shub.userservice.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email is already registered");
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedBy(email);

        User saved = userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(saved), saved.getName(), saved.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        return new AuthResponse(jwtService.generateToken(user), user.getName(), user.getId());
    }
}

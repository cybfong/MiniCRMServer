package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.dto.RegisterRequest;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.model.UserCredential;
import com.oji.mini_crm_server.repo.UserCredentialRepository;
import com.oji.mini_crm_server.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserCredentialRepository userCredentialRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {

        // 1. Check username
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 2. Check email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 3. Create User
        User user = new User();

        user.setUserName(request.getUserName());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(true);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        user = userRepository.save(user);

        // 4. Hash the plain-text password
        String passwordHash = passwordEncoder.encode(request.getPassword());

        // 5. Create UserCredential
        UserCredential credential = new UserCredential();

        credential.setPasswordHash(passwordHash);
        credential.setUser(user);
        credential.setCreatedAt(now);
        credential.setUpdatedAt(now);

        userCredentialRepository.save(credential);

        // 6. Return the created user
        return user;
    }
}

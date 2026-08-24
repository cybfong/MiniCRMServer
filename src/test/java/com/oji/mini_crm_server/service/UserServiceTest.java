package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.dto.RegisterRequest;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.model.UserCredential;
import com.oji.mini_crm_server.repo.UserCredentialRepository;
import com.oji.mini_crm_server.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void register_success_shouldCreateUserAndCredential() {
        RegisterRequest req = new RegisterRequest("u1", "Full", "u1@example.com", "pass", null);

        when(userRepository.findByUserName("u1")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("u1@example.com")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("pass")).thenReturn("hash");

        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(100L);
            u.setCreatedAt(LocalDateTime.now());
            u.setUpdatedAt(LocalDateTime.now());
            return u;
        });

        User created = userService.register(req);

        assertThat(created.getId()).isEqualTo(100L);
        verify(userCredentialRepository, times(1)).save(any(UserCredential.class));
    }

    @Test
    void register_whenUserNameExists_shouldThrow() {
        RegisterRequest req = new RegisterRequest("u1", "Full", "u1@example.com", "pass", null);
        when(userRepository.findByUserName("u1")).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () -> userService.register(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenEmailExists_shouldThrow() {
        RegisterRequest req = new RegisterRequest("u1", "Full", "u1@example.com", "pass", null);
        when(userRepository.findByUserName("u1")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("u1@example.com")).thenReturn(Optional.of(new User()));
        assertThrows(RuntimeException.class, () -> userService.register(req));
        verify(userRepository, never()).save(any());
    }
}


package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrentUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_success_returnsUser() {
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("u1");

        SecurityContext sc = org.mockito.Mockito.mock(SecurityContext.class);
        when(sc.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(sc);

        User u = new User();
        u.setUserName("u1");
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findByUserName("u1")).thenReturn(Optional.of(u));

        CurrentUserService svc = new CurrentUserService(userRepository);
        User got = svc.getCurrentUser();
        assertThat(got.getUserName()).isEqualTo("u1");
    }

    @Test
    void getCurrentUser_noAuth_throws() {
        SecurityContextHolder.clearContext();
        CurrentUserService svc = new CurrentUserService(userRepository);
        assertThrows(IllegalStateException.class, svc::getCurrentUser);
    }
}


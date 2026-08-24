package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.model.UserCredential;
import com.oji.mini_crm_server.repo.UserCredentialRepository;
import com.oji.mini_crm_server.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCredentialRepository userCredentialRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Test
    void loadUserByUsername_success_returnsUserDetails() {
        User u = new User();
        u.setUserName("u1");
        u.setFullName("Full");
        u.setEmail("u1@example.com");
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());

        UserCredential cred = new UserCredential();
        cred.setPasswordHash("h");
        cred.setUser(u);

        when(userRepository.findByUserName("u1")).thenReturn(Optional.of(u));
        when(userCredentialRepository.findByUser(u)).thenReturn(Optional.of(cred));

        UserDetails ud = myUserDetailsService.loadUserByUsername("u1");
        assertThat(ud.getUsername()).isEqualTo("u1");
    }

    @Test
    void loadUserByUsername_userNotFound_throws() {
        when(userRepository.findByUserName("x")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsService.loadUserByUsername("x"));
    }
}


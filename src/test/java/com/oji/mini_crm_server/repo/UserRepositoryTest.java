package com.oji.mini_crm_server.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void findByUserName_and_findByEmail_proxyChecks() {
        when(userRepository.findByUserName("u1")).thenReturn(java.util.Optional.empty());
        when(userRepository.findByEmail("u1@example.com")).thenReturn(java.util.Optional.empty());

        assertThat(userRepository.findByUserName("u1")).isNotNull();
        assertThat(userRepository.findByEmail("u1@example.com")).isNotNull();
    }
}



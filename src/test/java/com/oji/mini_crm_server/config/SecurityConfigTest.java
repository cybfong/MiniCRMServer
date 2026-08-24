package com.oji.mini_crm_server.config;

import org.junit.jupiter.api.Test;

import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityConfigTest {

    @Test
    void passwordEncoder_shouldEncodeAndMatch() {
        SecurityConfig cfg = new SecurityConfig(null, null);
        PasswordEncoder encoder = cfg.passwordEncoder();

        String raw = "secret";
        String encoded = encoder.encode(raw);

        assertThat(encoder.matches(raw, encoded)).isTrue();
    }
}


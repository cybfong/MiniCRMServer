package com.oji.mini_crm_server.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    @Test
    void generateAndValidateToken_shouldWork() {
        JwtService svc = new JwtService();
        String token = svc.generateToken("alice");
        String extracted = svc.extractUserName(token);
        assertThat(extracted).isEqualTo("alice");

        UserDetails ud = User.withUsername("alice").password("x").roles("USER").build();
        boolean valid = svc.validateToken(token, ud);
        assertThat(valid).isTrue();
    }
}


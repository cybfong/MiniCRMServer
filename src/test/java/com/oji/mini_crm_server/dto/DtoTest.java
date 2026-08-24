package com.oji.mini_crm_server.dto;

import com.oji.mini_crm_server.model.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DtoTest {

    @Test
    void registerRequest_gettersAndSetters() {
        RegisterRequest r = new RegisterRequest("u1", "Full", "u1@example.com", "pw", Role.USER);
        assertThat(r.getUserName()).isEqualTo("u1");
        assertThat(r.getFullName()).isEqualTo("Full");
        assertThat(r.getEmail()).isEqualTo("u1@example.com");
        assertThat(r.getPassword()).isEqualTo("pw");
        assertThat(r.getRole()).isEqualTo(Role.USER);

        r.setUserName("u2");
        assertThat(r.getUserName()).isEqualTo("u2");
    }

    @Test
    void loginRequest_gettersAndSetters() {
        LoginRequest l = new LoginRequest("user", "secret");
        assertThat(l.getUserName()).isEqualTo("user");
        assertThat(l.getPassword()).isEqualTo("secret");

        l.setPassword("x");
        assertThat(l.getPassword()).isEqualTo("x");
    }
}


package com.oji.mini_crm_server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelsUnitTest {

    @Test
    void role_and_customerStatus_enums() {
        assertThat(Role.ADMIN.name()).isEqualTo("ADMIN");
        assertThat(CustomerStatus.ACTIVE.name()).isEqualTo("ACTIVE");
    }

    @Test
    void userCredential_gettersSetters() {
        UserCredential c = new UserCredential();
        c.setPasswordHash("h");
        User u = new User();
        u.setUserName("uu");
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());
        c.setUser(u);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());

        assertThat(c.getPasswordHash()).isEqualTo("h");
        assertThat(c.getUser()).isEqualTo(u);
    }

    @Test
    void userPrincipal_behaviour() {
        User u = new User();
        u.setUserName("alice");
        u.setRole(Role.ADMIN);
        u.setEnabled(true);

        UserCredential cred = new UserCredential();
        cred.setPasswordHash("phash");

        UserPrincipal principal = new UserPrincipal(u, cred);

        assertThat(principal.getUsername()).isEqualTo("alice");
        assertThat(principal.getPassword()).isEqualTo("phash");
        assertThat(principal.getAuthorities()).hasAtLeastOneElementOfType(org.springframework.security.core.authority.SimpleGrantedAuthority.class);
        assertThat(principal.getAuthorities()).anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        assertThat(principal.isEnabled()).isTrue();
    }
}


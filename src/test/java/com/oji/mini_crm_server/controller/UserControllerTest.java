package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.dto.LoginRequest;
import com.oji.mini_crm_server.dto.RegisterRequest;
import com.oji.mini_crm_server.model.Role;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.service.JwtService;
import com.oji.mini_crm_server.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class UserControllerTest {

    @org.mockito.Mock
    private UserService userService;

    @org.mockito.Mock
    private JwtService jwtService;

    @org.mockito.Mock
    private AuthenticationManager authenticationManager;

    @org.mockito.InjectMocks
    private UserController controller;

    @org.junit.jupiter.api.Test
    void register_shouldReturnUser() {
        RegisterRequest req = new RegisterRequest("u1", "Full", "u1@example.com", "pass", Role.USER);
        User saved = new User();
        saved.setUserName("u1");

        when(userService.register(any(RegisterRequest.class))).thenReturn(saved);

        User result = controller.register(req);
        org.assertj.core.api.Assertions.assertThat(result.getUserName()).isEqualTo("u1");
    }

    @org.junit.jupiter.api.Test
    void login_success_shouldReturnToken() {
        LoginRequest req = new LoginRequest("u1", "pass");

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("u1");

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken("u1")).thenReturn("token-123");

        String token = controller.login(req);
        org.assertj.core.api.Assertions.assertThat(token).isEqualTo("token-123");
    }

    @org.junit.jupiter.api.Test
    void login_failure_shouldReturnFailMessage() {
        LoginRequest req = new LoginRequest("u1", "bad");

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);

        when(authenticationManager.authenticate(any())).thenReturn(auth);

        String res = controller.login(req);
        org.assertj.core.api.Assertions.assertThat(res).isEqualTo("Login fail");
    }
}


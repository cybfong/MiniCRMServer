package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.dto.LoginRequest;
import com.oji.mini_crm_server.dto.RegisterRequest;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.service.JwtService;
import com.oji.mini_crm_server.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/api/user/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/api/user/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName()
                        , loginRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authentication.getName());
        } else {
            return "Login fail";
        }
    }
}

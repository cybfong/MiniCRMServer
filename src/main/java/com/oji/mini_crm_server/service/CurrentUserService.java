package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        String userName = authentication.getName();

        if (userName == null || userName.isBlank()) {
            throw new IllegalStateException("Authenticated user name is missing");
        }

        return userRepository.findByUserName(userName).orElseThrow(() -> new IllegalStateException("Authenticated " + "user not found: " + userName));
    }
}

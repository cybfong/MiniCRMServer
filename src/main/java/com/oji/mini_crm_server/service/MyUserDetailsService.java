package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.model.UserCredential;
import com.oji.mini_crm_server.model.UserPrincipal;
import com.oji.mini_crm_server.repo.UserCredentialRepository;
import com.oji.mini_crm_server.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserCredentialRepository userCredentialRepository;

    public MyUserDetailsService(UserRepository userRepository, UserCredentialRepository userCredentialRepository) {
        this.userRepository = userRepository;
        this.userCredentialRepository = userCredentialRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        User user =
                userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException("User " +
                        "not" + " found: " + userName));

        UserCredential userCredential =
                userCredentialRepository.findByUser(user).orElseThrow(() -> new UsernameNotFoundException("User " +
                        "credentials not found for username: " + userName));

        return new UserPrincipal(user, userCredential);
    }
}

package com.oji.mini_crm_server.repo;

import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByUser(User user);
}

package com.oji.mini_crm_server.repo;

import com.oji.mini_crm_server.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByDeletedFalse();

    Optional<Contact> findByDeletedFalseAndId(Long contactId);

    boolean existsByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndDeletedFalseAndIdNot(String email, Long contactId);

}
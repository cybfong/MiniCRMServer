package com.oji.mini_crm_server.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTest {

    @Test
    void user_entity_gettersSetters() {
        User u = new User();
        u.setUserName("u1");
        u.setFullName("Full");
        u.setEmail("u1@example.com");
        u.setRole(Role.ADMIN);
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());

        assertThat(u.getUserName()).isEqualTo("u1");
        assertThat(u.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void customer_contact_note_relations() {
        User u = new User();
        u.setUserName("creator");
        u.setCreatedAt(LocalDateTime.now());
        u.setUpdatedAt(LocalDateTime.now());

        Customer c = new Customer();
        c.setCustomerName("Co");
        c.setIndustry("X");
        c.setCountry("US");
        c.setStatus(CustomerStatus.ACTIVE);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        c.setCreatedBy(u);

        Contact ct = new Contact();
        ct.setFirstName("Jane");
        ct.setLastName("D");
        ct.setCustomer(c);

        Note n = new Note();
        n.setNoteText("n1");
        n.setCustomer(c);
        n.setCreatedBy(u);

        assertThat(ct.getCustomer()).isEqualTo(c);
        assertThat(n.getCustomer()).isEqualTo(c);
        assertThat(c.getCreatedBy()).isEqualTo(u);
    }
}


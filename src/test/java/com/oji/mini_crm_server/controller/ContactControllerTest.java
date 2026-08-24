package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Contact;
import com.oji.mini_crm_server.service.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

    @org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
    public static class Unit {

        @org.mockito.Mock
        private ContactService contactService;

        @org.mockito.InjectMocks
        private ContactController controller;

        @org.junit.jupiter.api.Test
        void getContacts_shouldReturnList() {
            Contact c = new Contact();
            c.setId(1L);
            c.setFirstName("Jane");
            c.setLastName("Doe");
            c.setEmail("jane@example.com");
            c.setCreatedAt(LocalDateTime.now());
            c.setUpdatedAt(LocalDateTime.now());

            when(contactService.getAllContacts()).thenReturn(List.of(c));

            var res = controller.getContacts();
            org.assertj.core.api.Assertions.assertThat(res).hasSize(1);
            org.assertj.core.api.Assertions.assertThat(res.get(0).getEmail()).isEqualTo("jane@example.com");
        }

        @org.junit.jupiter.api.Test
        void createContact_shouldReturnSaved() {
            Contact req = new Contact();
            req.setFirstName("Sam");
            req.setLastName("S");

            Contact saved = new Contact();
            saved.setId(7L);
            saved.setFirstName("Sam");

            when(contactService.createContact(any(Contact.class))).thenReturn(saved);

            Contact out = controller.createContact(req);
            org.assertj.core.api.Assertions.assertThat(out.getId()).isEqualTo(7L);
        }

        @org.junit.jupiter.api.Test
        void deleteContact_returnsCorrectMessage() {
            when(contactService.deleteContact(3L)).thenReturn(true);
            when(contactService.deleteContact(4L)).thenReturn(false);

            String ok = controller.deleteContact(3L);
            String notFound = controller.deleteContact(4L);

            org.assertj.core.api.Assertions.assertThat(ok).isEqualTo("Contact deleted successfully");
            org.assertj.core.api.Assertions.assertThat(notFound).isEqualTo("Contact not found");
        }
    }
}


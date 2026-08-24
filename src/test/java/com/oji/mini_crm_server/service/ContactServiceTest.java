package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.Contact;
import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.repo.ContactRepository;
import com.oji.mini_crm_server.repo.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ContactService contactService;

    @Test
    void createContact_whenEmailExists_shouldThrow() {
        Contact c = new Contact();
        c.setEmail("a@b.com");
        when(contactRepository.existsByEmailAndDeletedFalse("a@b.com")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> contactService.createContact(c));
    }

    @Test
    void createContact_withoutCustomer_shouldThrow() {
        Contact c = new Contact();
        c.setEmail("x@y.com");
        when(contactRepository.existsByEmailAndDeletedFalse("x@y.com")).thenReturn(false);
        assertThrows(RuntimeException.class, () -> contactService.createContact(c));
    }

    @Test
    void createContact_success_shouldSave() {
        Contact c = new Contact();
        c.setEmail("ok@ok.com");
        Customer cust = new Customer();
        cust.setId(2L);
        c.setCustomer(new Customer());
        c.getCustomer().setId(2L);

        when(contactRepository.existsByEmailAndDeletedFalse("ok@ok.com")).thenReturn(false);
        when(customerRepository.findByDeletedFalseAndId(2L)).thenReturn(Optional.of(cust));
        when(contactRepository.save(any(Contact.class))).thenAnswer(i -> {
            Contact cc = i.getArgument(0);
            cc.setId(9L);
            return cc;
        });

        Contact out = contactService.createContact(c);
        assertThat(out.getId()).isEqualTo(9L);
    }

    @Test
    void deleteContact_notFound_returnsFalse() {
        when(contactRepository.findByDeletedFalseAndId(3L)).thenReturn(Optional.empty());
        boolean res = contactService.deleteContact(3L);
        assertThat(res).isFalse();
    }

    @Test
    void deleteContact_found_marksDeleted() {
        Contact c = new Contact();
        c.setId(4L);
        c.setDeleted(false);
        when(contactRepository.findByDeletedFalseAndId(4L)).thenReturn(Optional.of(c));

        boolean res = contactService.deleteContact(4L);
        assertThat(res).isTrue();
        assertThat(c.isDeleted()).isTrue();
        verify(contactRepository, times(1)).save(c);
    }
}


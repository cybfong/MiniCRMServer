package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.model.CustomerStatus;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.repo.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

// ...existing code... (no LocalDateTime required here)
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_shouldSetAuditAndSave() {
        User u = new User();
        u.setId(1L);
        u.setUserName("svc");

        when(currentUserService.getCurrentUser()).thenReturn(u);
        when(customerRepository.existsByCustomerNameAndDeletedFalse(any())).thenReturn(false);

        Customer toCreate = new Customer();
        toCreate.setCustomerName("NewCo");
        toCreate.setIndustry("X");
        toCreate.setCountry("Y");
        toCreate.setStatus(CustomerStatus.LEAD);

        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> {
            Customer c = i.getArgument(0);
            c.setId(100L);
            return c;
        });

        Customer saved = customerService.createCustomer(toCreate);

        assertThat(saved.getId()).isEqualTo(100L);
        assertThat(saved.getCreatedBy()).isEqualTo(u);
        assertThat(saved.getUpdatedBy()).isEqualTo(u);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_whenExists_shouldThrow() {
        when(customerRepository.existsByCustomerNameAndDeletedFalse(any())).thenReturn(true);

        Customer toCreate = new Customer();
        toCreate.setCustomerName("Exists");

        assertThrows(RuntimeException.class, () -> customerService.createCustomer(toCreate));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomer_whenNotFound_shouldReturnFalse() {
        when(customerRepository.findByDeletedFalseAndId(1L)).thenReturn(Optional.empty());

        boolean result = customerService.deleteCustomer(1L);
        assertThat(result).isFalse();
    }

    @Test
    void deleteCustomer_whenFound_shouldMarkDeletedAndSave() {
        User u = new User();
        u.setId(2L);
        when(currentUserService.getCurrentUser()).thenReturn(u);

        Customer existing = new Customer();
        existing.setId(5L);
        existing.setDeleted(false);
        when(customerRepository.findByDeletedFalseAndId(5L)).thenReturn(Optional.of(existing));

        boolean result = customerService.deleteCustomer(5L);
        assertThat(result).isTrue();
        assertThat(existing.isDeleted()).isTrue();
        assertThat(existing.getUpdatedBy()).isEqualTo(u);
        verify(customerRepository, times(1)).save(existing);
    }
}



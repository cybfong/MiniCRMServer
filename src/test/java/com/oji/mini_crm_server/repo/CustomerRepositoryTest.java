package com.oji.mini_crm_server.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerRepositoryTest {

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void existsByCustomerNameAndDeletedFalse_proxyCheck() {
        when(customerRepository.existsByCustomerNameAndDeletedFalse("X"))
                .thenReturn(true);

        boolean exists = customerRepository.existsByCustomerNameAndDeletedFalse("X");
        assertThat(exists).isTrue();
    }
}



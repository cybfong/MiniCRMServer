package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.model.CustomerStatus;
import com.oji.mini_crm_server.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class CustomerControllerTest {

    @org.mockito.Mock
    private CustomerService customerService;

    @org.mockito.InjectMocks
    private CustomerController controller;

    @org.junit.jupiter.api.Test
    void getCustomers_shouldReturnList() {
        Customer c = new Customer();
        c.setId(10L);
        c.setCustomerName("Acme");
        c.setIndustry("Tech");
        c.setCountry("US");
        c.setStatus(CustomerStatus.ACTIVE);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());

        when(customerService.getAllCustomers()).thenReturn(List.of(c));

        var result = controller.getCustomers();
        org.assertj.core.api.Assertions.assertThat(result).hasSize(1);
        org.assertj.core.api.Assertions.assertThat(result.get(0).getCustomerName()).isEqualTo("Acme");
    }

    @org.junit.jupiter.api.Test
    void createCustomer_shouldReturnCreated() {
        Customer req = new Customer();
        req.setCustomerName("NewCo");
        req.setIndustry("X");
        req.setCountry("Y");
        req.setStatus(CustomerStatus.LEAD);

        Customer saved = new Customer();
        saved.setId(20L);
        saved.setCustomerName("NewCo");

        when(customerService.createCustomer(org.mockito.ArgumentMatchers.any(Customer.class))).thenReturn(saved);

        Customer result = controller.createCustomer(req);
        org.assertj.core.api.Assertions.assertThat(result.getId()).isEqualTo(20L);
    }
}



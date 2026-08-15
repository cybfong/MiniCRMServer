package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/customers")
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/api/customer/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }
}

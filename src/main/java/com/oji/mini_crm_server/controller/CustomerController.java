package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.service.CustomerService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/customers/{customerId}")
    public Customer getCustomerById(@PathVariable("customerId") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @PostMapping("/api/customers")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/api/customers/{customerId}")
    public Customer updateCustomer(@PathVariable("customerId") Long customerId, @RequestBody Customer customer) {
        return customerService.updateCustomer(customerId, customer);
    }

    @DeleteMapping("/api/customers/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") Long customerId) {
        return customerService.deleteCustomer(customerId) ? "Customer deleted successfully" : "Customer not found";
    }
}

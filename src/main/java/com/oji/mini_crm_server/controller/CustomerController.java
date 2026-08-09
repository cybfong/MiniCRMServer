package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/api/customers")
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }
}

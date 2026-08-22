package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.model.User;
import com.oji.mini_crm_server.repo.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CurrentUserService currentUserService;

    public CustomerService(CustomerRepository customerRepository, CurrentUserService currentUserService) {
        this.customerRepository = customerRepository;
        this.currentUserService = currentUserService;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findByDeletedFalse();
    }

    public Customer getCustomer(Long customerId) {
        return customerRepository.findByDeletedFalseAndId(customerId).orElseThrow(() -> new RuntimeException(
                "Customer not found: " + customerId));
    }

    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByCustomerNameAndDeletedFalse(customer.getCustomerName())) {

            throw new RuntimeException("Customer already exists: " + customer.getCustomerName());
        }

        User currentUser = currentUserService.getCurrentUser();
        customer.setCreatedBy(currentUser);
        customer.setUpdatedBy(currentUser);

        LocalDateTime now = LocalDateTime.now();
        customer.setCreatedAt(now);
        customer.setUpdatedAt(now);

        customer.setDeleted(false);

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long customerId, Customer customer) {
        Customer existingCustomer =
                customerRepository.findByDeletedFalseAndId(customerId).orElseThrow(() -> new RuntimeException(
                        "Customer not found: " + customerId));

        User currentUser = currentUserService.getCurrentUser();

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setIndustry(customer.getIndustry());
        existingCustomer.setWebsite(customer.getWebsite());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setCountry(customer.getCountry());
        existingCustomer.setStatus(customer.getStatus());

        existingCustomer.setUpdatedBy(currentUser);
        existingCustomer.setUpdatedAt(LocalDateTime.now());

        return customerRepository.save(existingCustomer);
    }

    // Soft delete
    @Transactional
    public boolean deleteCustomer(Long customerId) {
        Optional<Customer> customerOptional = customerRepository.findByDeletedFalseAndId(customerId);

        if (customerOptional.isEmpty()) {
            return false;
        }

        Customer customer = customerOptional.get();

        User currentUser = currentUserService.getCurrentUser();

        customer.setDeleted(true);
        customer.setUpdatedBy(currentUser);
        customer.setUpdatedAt(LocalDateTime.now());

        customerRepository.save(customer);

        return true;
    }
}

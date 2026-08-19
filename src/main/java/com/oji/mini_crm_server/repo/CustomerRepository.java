package com.oji.mini_crm_server.repo;

import com.oji.mini_crm_server.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByDeletedFalse();
    Optional<Customer> findByDeletedFalseAndId(Long customerId);

    boolean existsByCustomerNameAndDeletedFalse(String customerName);
}

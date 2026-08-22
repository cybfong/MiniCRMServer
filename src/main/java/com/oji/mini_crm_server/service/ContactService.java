package com.oji.mini_crm_server.service;

import com.oji.mini_crm_server.model.Contact;
import com.oji.mini_crm_server.model.Customer;
import com.oji.mini_crm_server.repo.ContactRepository;
import com.oji.mini_crm_server.repo.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final CustomerRepository customerRepository;

    public ContactService(ContactRepository contactRepository, CustomerRepository customerRepository) {
        this.contactRepository = contactRepository;
        this.customerRepository = customerRepository;
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findByDeletedFalse();
    }

    public Contact getContact(Long contactId) {
        return contactRepository.findByDeletedFalseAndId(contactId).orElseThrow(() -> new RuntimeException("Contact " + "not found with id: " + contactId));
    }

    @Transactional
    public Contact createContact(Contact contact) {
        if (contact.getEmail() != null && !contact.getEmail().isBlank() && contactRepository.existsByEmailAndDeletedFalse(contact.getEmail())) {
            throw new RuntimeException("Contact already exists with email: " + contact.getEmail());
        }

        if (contact.getCustomer() == null || contact.getCustomer().getId() == null) {
            throw new RuntimeException("Customer is required");
        }

        Long customerId = contact.getCustomer().getId();

        Customer customer =
                customerRepository.findByDeletedFalseAndId(customerId).orElseThrow(() -> new RuntimeException(
                        "Customer not found: " + customerId));

        contact.setCustomer(customer);

        LocalDateTime now = LocalDateTime.now();
        contact.setCreatedAt(now);
        contact.setUpdatedAt(now);

        contact.setDeleted(false);

        return contactRepository.save(contact);
    }

    @Transactional
    public Contact updateContact(Long contactId, Contact contact) {
        Contact existingContact =
                contactRepository.findByDeletedFalseAndId(contactId).orElseThrow(() -> new RuntimeException("Contact "
                        + "not found with id: " + contactId));

        if (contact.getEmail() != null && !contact.getEmail().isBlank() && contactRepository.existsByEmailAndDeletedFalseAndIdNot(contact.getEmail(), contactId)) {
            throw new RuntimeException("Contact already exists with email: " + contact.getEmail());
        }

        if (contact.getCustomer() == null || contact.getCustomer().getId() == null) {
            throw new RuntimeException("Customer is required");
        }

        Long customerId = contact.getCustomer().getId();

        Customer customer =
                customerRepository.findByDeletedFalseAndId(customerId).orElseThrow(() -> new RuntimeException(
                        "Customer not found: " + customerId));

        existingContact.setCustomer(customer);

        existingContact.setEmail(contact.getEmail());
        existingContact.setFirstName(contact.getFirstName());
        existingContact.setLastName(contact.getLastName());
        existingContact.setPhone(contact.getPhone());
        existingContact.setJobTitle(contact.getJobTitle());

        existingContact.setUpdatedAt(LocalDateTime.now());

        return contactRepository.save(existingContact);
    }

    // Soft delete
    @Transactional
    public boolean deleteContact(Long contactId) {
        Optional<Contact> contactOptional = contactRepository.findByDeletedFalseAndId(contactId);

        if (contactOptional.isEmpty()) {
            return false;
        }

        Contact contact = contactOptional.get();
        contact.setDeleted(true);
        contact.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(contact);

        return true;
    }
}

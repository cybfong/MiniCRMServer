package com.oji.mini_crm_server.controller;

import com.oji.mini_crm_server.model.Contact;
import com.oji.mini_crm_server.service.ContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/api/contacts")
    public List<Contact> getContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/api/contacts/{contactId}")
    public Contact getContactById(@PathVariable("contactId") Long contactId) {
        return contactService.getContact(contactId);
    }

    @PostMapping("/api/contacts")
    public Contact createContact(@RequestBody Contact contact) {
        return contactService.createContact(contact);
    }

    @PutMapping("/api/contacts/{contactId}")
    public Contact updateContact(@PathVariable("contactId") Long contactId, @RequestBody Contact contact) {
        return contactService.updateContact(contactId, contact);
    }

    @DeleteMapping("/api/contacts/{contactId}")
    public String deleteContact(@PathVariable("contactId") Long contactId) {
        return contactService.deleteContact(contactId) ? "Contact deleted successfully" : "Contact not found";
    }

}

package com.oji.mini_crm_server.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String industry;

    private String website;
    private String phone;
    private String email;
    private String address;
    private String city;

    @Column(nullable = false)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonManagedReference("customer-created-by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonManagedReference("customer-updated-by")
    private User updatedBy;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference("customer-contacts")
    private List<Contact> contacts;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference("customer-notes")
    private List<Note> notes;
}

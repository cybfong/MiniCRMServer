package com.oji.mini_crm_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String jobTitle;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
}

package com.oji.mini_crm_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String customerName;
    private String industry;
    private String website;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String country;
    private String status;
    private boolean deleted;
    private Date createdAt;
    private Date updatedAt;
    private String createdBy;
    private String updatedBy;
}

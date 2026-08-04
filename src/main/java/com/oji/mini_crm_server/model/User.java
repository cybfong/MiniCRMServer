package com.oji.mini_crm_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;
    private boolean enabled;
    private Date createdAt;
    private Date updatedAt;
}

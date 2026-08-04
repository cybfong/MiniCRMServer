package com.oji.mini_crm_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int customerId;
    private String noteText;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;
}

package com.example.demo.user;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String email;
    // getter & setters
    public UUID getId() {return id;}
    public String getEmail() {return email;}

    
}

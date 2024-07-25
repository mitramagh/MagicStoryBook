package com.example.magicstorybook.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Use a non-reserved table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Story> stories;

    // Getters and setters
}

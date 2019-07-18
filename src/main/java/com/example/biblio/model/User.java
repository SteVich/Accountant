package com.example.biblio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username", "email"
        })})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Size(min = 4, max = 60, message = "Minimum username length: 4 characters")
    @Column(name = "username", nullable = false)
    private String username;

    @Email
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(min = 4, message = "Minimum password length: 4 characters")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "roles", nullable = false)
    List<Role> roles;
}


package ru.ssau.tk.maths.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user") // user — зарезервированное слово в некоторых СУБД, поэтому app_user
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    //@Column(name = "password_hash", nullable = false)
    private String password_hash;

    @Column(nullable=false)
    private String role = "ROLE_USER";

    // constructors
    public AppUser() {}
    public AppUser(String username, String passwordHash, String role) {
        this.username = username;
        this.password_hash = passwordHash;
        this.role = role;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return password_hash; }
    public void setPasswordHash(String passwordHash) { this.password_hash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

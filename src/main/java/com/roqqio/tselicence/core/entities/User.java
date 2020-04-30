package com.roqqio.tselicence.core.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
public final class User implements Serializable {
    @Column(name = "created")
    private LocalDateTime created;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Column(name = "active")
    private boolean active;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "modified")
    private LocalDateTime modified;
    @Column(name = "password")
    private String password;
    @Column(name = "username")
    private String username;
    @Column(name = "user_role")
    private String userRole;

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setIsActive(boolean isActive) {
        this.active = isActive;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}

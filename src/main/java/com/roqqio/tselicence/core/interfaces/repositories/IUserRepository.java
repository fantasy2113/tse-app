package com.roqqio.tselicence.core.interfaces.repositories;

import com.roqqio.tselicence.core.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    void activateUser(User user);

    boolean createUser(String username, String plainPassword, String userRole);

    void deactivateUser(User user);

    Optional<User> get(String username);

    Optional<User> get(long id);

    List<User> all();

    void updateLastLogin(User user);
}

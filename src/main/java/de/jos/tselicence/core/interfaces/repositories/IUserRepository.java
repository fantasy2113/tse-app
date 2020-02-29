package de.jos.tselicence.core.interfaces.repositories;

import de.jos.tselicence.core.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    void activateUser(User user);

    void createUser(String username, String plainPassword, String userRole);

    void deactivateUser(User user);

    Optional<User> get(String username);

    Optional<User> get(long id);

    List<User> all();

    void updateLastLogin(User user);
}

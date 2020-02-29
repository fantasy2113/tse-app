package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.User;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.core.interfaces.security.ICrypt;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component(Comp.USER_REP)
public class UserRepository implements IUserRepository {
    private final UserHiRepository userHiRepository;
    private final ICrypt crypt;

    @Autowired
    public UserRepository(@Qualifier(Comp.USER_HI_REP) UserHiRepository userHiRepository, @Qualifier(Comp.CRYPT) ICrypt crypt) {
        this.userHiRepository = userHiRepository;
        this.crypt = crypt;
    }

    @Override
    public void activateUser(User user) {
    }

    @Override
    public void createUser(String username, String plainPassword, String userRole) {
        userHiRepository.save(initUser(username, plainPassword, userRole));
    }

    @Override
    public void deactivateUser(User user) {
    }

    @Override
    public Optional<User> get(String username) {
        return userHiRepository.findByUsername(username);
    }

    @Override
    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        userHiRepository.save(user);
    }

    @Override
    public Optional<User> get(long id) {
        return userHiRepository.findById(id);
    }

    @Override
    public List<User> all() {
        return IterableUtils.toList(userHiRepository.findAll());
    }

    private User initUser(final String username, final String plainTextPassword, final String userRole) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setPassword(crypt.hashPassword(plainTextPassword));
        user.setUsername(username);
        user.setUserRole(userRole.toUpperCase());
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setIsActive(true);
        return user;
    }
}

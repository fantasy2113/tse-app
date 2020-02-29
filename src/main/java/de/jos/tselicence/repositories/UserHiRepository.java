package de.jos.tselicence.repositories;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(Comp.USER_HI_REP)
interface UserHiRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(@Param("username") String username);
}

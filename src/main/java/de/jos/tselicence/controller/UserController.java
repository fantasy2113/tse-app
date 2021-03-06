package de.jos.tselicence.controller;

import de.jos.tselicence.core.Comp;
import de.jos.tselicence.core.interfaces.repositories.IUserRepository;
import de.jos.tselicence.security.WithAuthentication;
import de.jos.tselicence.security.WithIpRestriction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends Controller {
    private final IUserRepository userRepository;

    @Autowired
    public UserController(@Qualifier(Comp.USER_REP) IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @WithIpRestriction
    @WithAuthentication
    @PostMapping("internal/user/save")
    public ResponseEntity<Void> save(@RequestParam String userName, @RequestParam String plainPassword, @RequestParam String userRole) {
        if (userRepository.createUser(userName, plainPassword, userRole)) {
            return statusOk();
        }
        return status404NotFound();
    }
}

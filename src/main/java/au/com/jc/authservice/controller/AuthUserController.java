package au.com.jc.authservice.controller;

import au.com.jc.authservice.model.AuthUser;
import au.com.jc.authservice.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class AuthUserController {

    @Autowired
    AuthUserRepository authUserRepository;

    @GetMapping(value = "/{name}",
            produces = "application/json")
    public ResponseEntity<?> addAuthUser(@PathVariable String name) {
        AuthUser user = new AuthUser();
        user.setUsername(name);
        AuthUser savedUser = authUserRepository.saveAndFlush(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }
}

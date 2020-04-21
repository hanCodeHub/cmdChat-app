package com.handev.inChat.controller;


import com.handev.inChat.model.User;
import com.handev.inChat.model.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserAccountController {

    @Autowired
    UserRepo userRepo;

    /**
     * Returns details of current signed-in user in a Map.
     * @see <a href="https://spring.io/guides/tutorials/spring-boot-oauth2/">Spring Boot OAuth2</a>
     * @param principal the user object signed in via OAuth2
     */
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", principal.getAttribute("name"));
        map.put("username", principal.getAttribute("login"));
        return map;
    }

    /**
     * Obtains the current authenticated user and returns its name
     * @return the name of the user if logged in
     */
    @GetMapping("/ping-user")
    public String pingUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        return currentUserName;
    }

    /**
     * Handles User creation on login and saves them to DB
     */
    @PostMapping("/user/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (userRepo.findByName(user.getName()) != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user already exists");

        User savedUser = userRepo.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

}

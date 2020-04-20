package com.handev.inChat.controller;


import com.handev.inChat.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class UserController {

    /**
     * Handles OAuth2 sign-in of user, saving it to DB with encoded password.
     * @see <a href="https://spring.io/guides/tutorials/spring-boot-oauth2/">Spring Boot OAuth2</a>
     * @param principal the user object signed in via OAuth2
     */
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }


    /**
     * Obtains the current authenticated user and returns its name
     * @return the name of the user if logged in
     */
    @GetMapping("/ping-user")
    public ResponseEntity<User> pingUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        User thisUser = new User(currentUserName);
        return new ResponseEntity<>(thisUser, HttpStatus.OK);
    }
}

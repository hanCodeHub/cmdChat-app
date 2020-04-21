package com.handev.inChat.controller;

import com.handev.inChat.model.User;
import com.handev.inChat.model.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * Listens for and handles authentication events
 * @author Han Xu
 */
@Component
public class AuthEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEventListener.class);

    @Autowired
    UserRepo userRepo;

    /**
     * Listens for successful authentication, and registers user in DB.
     * @param authSuccessEvent successful auth event
     */
    @EventListener
    public void handleAuthSuccess(AuthenticationSuccessEvent authSuccessEvent){
        // obtains the OAuthClient username and id from the current authenticated user
        var principal = (OAuth2User) authSuccessEvent.getAuthentication().getPrincipal();
        var oauthUserName = principal.getAttributes().get("login").toString();
        var oauthUserId = (Integer) principal.getAttributes().get("id");
        LOGGER.info("OAuth user signed in: " + oauthUserName);

        // saves user if not exist already, updates its old name otherwise
        User savedUser = userRepo.findByOauthClientId(oauthUserId);
        if (savedUser == null) {
            var user = new User(oauthUserName, oauthUserId);
            userRepo.save(user);
        } else if (!savedUser.getName().equals(oauthUserName)) {
            savedUser.setName(oauthUserName);
        }

        // TODO: 4/20/20 obtain avatar_url and add it to User to display next to messages
    }

    /**
     * Logs the failed authentication event
     * @param authFailEvent authentication failure event
     */
    @EventListener
    public void handleAuthFail(AbstractAuthenticationFailureEvent authFailEvent){
        var principal = authFailEvent.getAuthentication().getPrincipal();

        LOGGER.info("OAuth2 authentication failed: " + principal);
    }
}

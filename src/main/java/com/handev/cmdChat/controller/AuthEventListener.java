package com.handev.cmdChat.controller;

import com.handev.cmdChat.model.User;
import com.handev.cmdChat.model.UserBuilder;
import com.handev.cmdChat.model.UserRepo;
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
 *
 * @author Han Xu
 */
@Component
public class AuthEventListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthEventListener.class);

  @Autowired UserRepo userRepo;

  /**
   * Listens for successful authentication, and registers user in DB.
   *
   * @param authSuccessEvent successful auth event
   */
  @EventListener
  public void handleAuthSuccess(AuthenticationSuccessEvent authSuccessEvent) {
    // obtains the OAuthClient username and id from the current authenticated user
    OAuth2User principal = (OAuth2User) authSuccessEvent.getAuthentication().getPrincipal();
    String oauthUserName = principal.getAttributes().get("login").toString();
    Integer oauthUserId = (Integer) principal.getAttributes().get("id");
    LOGGER.info("OAuth user signed in: " + oauthUserName);

    User savedUser = userRepo.findByOauthClientId(oauthUserId);
    // saves user if not exists
    if (savedUser == null) {
      User user = new UserBuilder(oauthUserName).withOauthClientId(oauthUserId).build();

      userRepo.save(user);
      // updates user's old name if different
    } else if (!savedUser.getName().equals(oauthUserName)) {
      savedUser.setName(oauthUserName);
    }

    // TODO: 4/20/20 obtain avatar_url and add it to User to display next to messages
  }

  /**
   * Logs the failed authentication event.
   *
   * @param authFailEvent authentication failure event
   */
  @EventListener
  public void handleAuthFail(AbstractAuthenticationFailureEvent authFailEvent) {
    Object principal = authFailEvent.getAuthentication().getPrincipal();

    LOGGER.info("OAuth2 authentication failed: " + principal);
  }
}

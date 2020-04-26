package com.handev.cmdChat.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Test class for custom JPA queries defined in UserRepo. DataJpaTest annotation will auto create
 * tables and drop them after testing.
 */
@DataJpaTest
class UserRepoTest {

  @Autowired private UserRepo userRepo;

  /** Inserts a User before each test. */
  @BeforeEach
  void setUp() {
    User user = new UserBuilder("Han").build();
    user.setOauthClientId(98654);
    try {
      userRepo.save(user);
    } catch (DataIntegrityViolationException exception) {
      System.out.println(exception.getCause().toString());
    }
  }

  /** Tests the retrieval of a User from repository by name. */
  @Test
  void findByName() {
    User savedUser = userRepo.findByName("Han");
    Assertions.assertEquals("Han", savedUser.getName());
  }

  /** Tests the retrieval of a User from repository by oauthClientId. */
  @Test
  void findByOauthClientId() {
    User savedUser = userRepo.findByOauthClientId(98654);
    Assertions.assertEquals(savedUser.getOauthClientId(), savedUser.getOauthClientId());
  }
}

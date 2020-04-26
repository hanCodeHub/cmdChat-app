package com.handev.cmdChat;

import com.handev.cmdChat.controller.PublicChatController;
import com.handev.cmdChat.controller.UserAccountController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Main test class for when application context loads.
 *
 * @see <a href="https://spring.io/guides/gs/testing-web/">spring web testing</a>
 */
@SpringBootTest
class CmdChatApplicationTests {

  @Autowired private PublicChatController pubChatController;

  @Autowired private UserAccountController userAccountController;

  /** Tests that the PublicChatController is in the application context. */
  @Test
  void publicChatController() {
    assertThat(pubChatController).isNotNull();
  }

  /** Tests that the UserAccountController is in the application context. */
  @Test
  void userAccountController() {
    assertThat(userAccountController).isNotNull();
  }
}

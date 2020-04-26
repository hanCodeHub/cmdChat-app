package com.handev.cmdChat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TextMessageBuilder objects.
 */
class TextMessageBuilderTest {

    private TextMessageBuilder builder;

    /**
     * Sets up a new builder before each test.
     */
    @BeforeEach
    void setUp() {
        builder = new TextMessageBuilder(MessageState.CHAT);
    }

    /**
     * Tests the building of a User with content property.
     */
    @Test
    void withContent() {
        TextMessage testMessage = builder.withContent("hello world").build();
        assertEquals("hello world", testMessage.getContent());
        assertNull(testMessage.getSender());
        assertNull(testMessage.getDateTime());
        assertNull(testMessage.getUser());
    }

    /**
     * Tests the building of a TextMessage with sender property.
     */
    @Test
    void withSender() {
        TextMessage testMessage = builder.withSender("Han").build();
        assertEquals("Han", testMessage.getSender());
    }

    /**
     * Tests the building of a TextMessage with datetime property.
     */
    @Test
    void withDateTime() {
        LocalDateTime dtNow = LocalDateTime.now();
        TextMessage testMessage = builder.withDateTime(dtNow).build();
        assertEquals(dtNow, testMessage.getDateTime());
    }

    /**
     * Tests the building of a TextMessage with User property.
     */
    @Test
    void withUser() {
        // tests for hashcode equivalence
        User testUser = new User();
        int userCode = testUser.hashCode();
        TextMessage testMessage = builder.withUser(testUser).build();
        assertEquals(userCode, testMessage.getUser().hashCode());
    }

    /**
     * Tests the building of a TextMessage with all properties.
     */
    @Test
    void build() {
        LocalDateTime dtNow = LocalDateTime.now();
        User testUser = new User();
        TextMessage testMessage = builder.withUser(testUser)
                        .withDateTime(dtNow)
                        .withSender("Han")
                        .withContent("Hello")
                        .build();
        assertNotNull(testMessage.getUser());
        assertNotNull(testMessage.getSender());
        assertNotNull(testMessage.getDateTime());
        assertNotNull(testMessage.getContent());
    }
}
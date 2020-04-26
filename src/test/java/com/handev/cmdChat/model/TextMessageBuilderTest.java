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
        var testMessage = builder.withContent("hello world").build();
        assertEquals("hello world", testMessage.getContent());
        assertNull(testMessage.getSender());
        assertNull(testMessage.getDateTime());
        assertNull(testMessage.getUser());
    }

    @Test
    void withSender() {
        var testMessage = builder.withSender("Han").build();
        assertEquals("Han", testMessage.getSender());
    }

    @Test
    void withDateTime() {
        var dtNow = LocalDateTime.now();
        var testMessage = builder.withDateTime(dtNow).build();
        assertEquals(dtNow, testMessage.getDateTime());
    }

    @Test
    void withUser() {
        var testUser = new User();
        var userCode = testUser.hashCode();
        var testMessage = builder.withUser(testUser).build();
        assertEquals(userCode, testMessage.getUser().hashCode());
    }

    @Test
    void build() {
        var dtNow = LocalDateTime.now();
        var testUser = new User();
        var testMessage = builder.withUser(testUser)
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
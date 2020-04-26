package com.handev.cmdChat.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for UserBuilder objects.
 */
class UserBuilderTest {

    private UserBuilder builder;

    /**
     * Sets up a new builder before each test.
     */
    @BeforeEach
    void setUp() {
        builder = new UserBuilder("test user");
    }

    /**
     * Tests the building of a User with oauthClientId property.
     */
    @Test
    void withOauthClientId() {
        User testUser = builder.withOauthClientId(123).build();
        assertEquals(123, testUser.getOauthClientId());
        assertNull(testUser.getMessages());
        assertNull(testUser.getPassword());
    }

    /**
     * Tests the building of a User with password property.
     */
    @Test
    void withPassword() {
        User testUser = builder.withPassword("StrongPassword").build();
        assertEquals("StrongPassword", testUser.getPassword());
        assertNull(testUser.getOauthClientId());
        assertNull(testUser.getMessages());
    }

    /**
     * Tests the building of a User with messages property.
     */
    @Test
    void withMessages() {
        TextMessage testMessage1 = new TextMessage();
        TextMessage testMessage2 = new TextMessage();
        int code1 = testMessage1.hashCode();
        int code2 = testMessage2.hashCode();

        List<TextMessage> messages = new ArrayList<>(
                Arrays.asList(testMessage1, testMessage2)
        );
        User testUser = builder.withMessages(messages).build();
        // tests for hashcode equality of original TextMessages
        assertEquals(code1, testUser.getMessages().get(0).hashCode());
        assertEquals(code2, testUser.getMessages().get(1).hashCode());
    }

    /**
     * Tests the building of a User with all properties.
     */
    @Test
    void build() {
        TextMessage testMessage1 = new TextMessage();
        TextMessage testMessage2 = new TextMessage();
        List<TextMessage> messages = new ArrayList<>(
                Arrays.asList(testMessage1, testMessage2)
        );

        User testUser = builder
                        .withMessages(messages)
                        .withPassword("StrongPassword")
                        .withOauthClientId(123)
                        .build();
        assertNotNull(testUser.getMessages());
        assertNotNull(testUser.getOauthClientId());
        assertNotNull(testUser.getPassword());
    }

}
package com.handev.inChat.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Autoconfigures User repository with underlying H2 database.
 * Basic CRUD functionality included
 * @author Han Xu
 */
public interface UserRepo extends JpaRepository<User, Integer> {

    /* custom query to find a User by name */
    @Query(value = "SELECT * FROM users WHERE name = ?1", nativeQuery = true)
    User findByName(String name);
}

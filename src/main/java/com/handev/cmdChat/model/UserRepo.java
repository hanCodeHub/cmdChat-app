package com.handev.cmdChat.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Autoconfigures User repository with underlying H2 database.
 * Basic CRUD functionality included
 * @author Han Xu
 */
@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    /* custom query to find a User by name */
    @Query(value = "SELECT * FROM users WHERE name = ?1", nativeQuery = true)
    User findByName(String name);

    /* custom query to find a User by oauthClientId */
    @Query(value = "SELECT * FROM users WHERE oauth_client_id = ?1", nativeQuery = true)
    User findByOauthClientId(Integer oauthClientId);
}

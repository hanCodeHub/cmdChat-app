package com.handev.inChat.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Autoconfigures TextMessage repository with underlying H2 database.
 * Basic CRUD functionality included
 * @author Han Xu
 */
@Repository
public interface TextMessageRepo extends JpaRepository<TextMessage, Integer> {

}

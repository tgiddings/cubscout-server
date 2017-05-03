package com.robocubs4205.cubscout.model.access;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User getUserById(long id);
    User getUserByUsername(String username);
}

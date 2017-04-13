package com.robocubs4205.cubscout.model.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long>{
    User getUserById(long id);
    User getUserByUsername(String username);
}

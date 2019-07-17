package com.womencancode.demoapp.Repository;

import com.womencancode.demoapp.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByNameIgnoreCase(String name);
}

package com.womencancode.demoapp.Repository;

import com.womencancode.demoapp.Model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByNameIgnoreCase(String name);
}

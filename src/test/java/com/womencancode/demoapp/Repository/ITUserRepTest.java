package com.womencancode.demoapp.Repository;

import com.womencancode.demoapp.Model.Role;
import com.womencancode.demoapp.Model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@Profile("it")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITUserRepTest {

    private static final String USER_1_name = "User 1";
    private static final String USER_1_lastName = "User 1 LastName";
    private static final String USER_1_username = "User1Username";
    private static final String USER_1_email = "email@test.com";
    private static final String USER_1_roleName = "Role 1";
    private static final LocalDate USER_1_birthDate = LocalDate.now();

    private static final String NEW_USER_name = "NewUser";
    private static final String NEW_USER_lastName = "NewUser LastName";
    private static final String NEW_USER_username = "NewUser1Username";
    private static final String NEW_USER_email = "NewEmail@test.com";
    private static final String NEW_USER_roleName = "NewRole";
    private static final LocalDate NEW_USER_birthDate = LocalDate.of(1997, Month.MAY, 15);

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    UserRepository repository;

    private List<String> idsToDelete = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        if (!mongoOperations.collectionExists(User.class)) {
            mongoOperations.createCollection(User.class);
        }
    }

    @After
    public void tearDown() {
        mongoOperations.remove(Query.query(Criteria.where("id").in(idsToDelete)), User.class);
        idsToDelete.clear();
    }

    @Test
    public void whenInsertingUser_thenUserIsInserted() {
        //given
        User user = createUser(USER_1_name,USER_1_lastName,USER_1_username,USER_1_email,USER_1_birthDate,USER_1_roleName);

        // when
        String id = mongoOperations.insert(user).getId();
        idsToDelete.add(id);

        //then
        User expectedUser = mongoOperations.findById(id, User.class);
        assertThat(user, is(expectedUser));
    }



    @Test
    public void givenUserExists_whenSavingUser_thenUserIsUpdated() {
        //given
        User user = createUser(USER_1_name,USER_1_lastName,USER_1_username,USER_1_email,USER_1_birthDate,USER_1_roleName);
        String id = mongoOperations.insert(user).getId();
        idsToDelete.add(id);

        // when
        user = repository.findByNameIgnoreCase("user 1").get();
        user.setName(NEW_USER_name);
        repository.save(user);

        //then
        User savedUser = mongoOperations.findById(id, User.class);
        assertThat(NEW_USER_name, is(savedUser.getName()));
    }

    @Test
    public void givenUserExists_whenDeletingUser_thenUserIsRemoved() {
        //given
        User user = createUser(USER_1_name,USER_1_lastName,USER_1_username,USER_1_email,USER_1_birthDate,USER_1_roleName);
        String id = mongoOperations.insert(user).getId();

        // when
        repository.deleteById(id);

        //then
        User dbUser = mongoOperations.findById(id, User.class);
        assertNull(dbUser);
    }


    private User createUser(String name, String lastName, String username, String email, LocalDate birthDate, String roleName) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setBirthDate(birthDate);
        Role role = createRole(roleName);
        user.setRole(role);

        return user;
    }

    private Role createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }


}

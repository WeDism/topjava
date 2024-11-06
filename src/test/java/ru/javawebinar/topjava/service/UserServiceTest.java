package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private UserService service;

    @Test
    public void create() {
        User created = this.service.create(UserTestData.getNew());
        Integer newId = created.getId();
        User newUser = UserTestData.getNew();
        newUser.setId(newId);
        UserTestData.assertMatch(created, newUser);
        UserTestData.assertMatch(this.service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() {
        Assert.assertThrows(DataAccessException.class, () ->
                this.service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        this.service.delete(UserTestData.USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> this.service.get(UserTestData.USER_ID));
    }

    @Test
    public void deletedNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> this.service.delete(UserTestData.NOT_FOUND));
    }

    @Test
    public void get() {
        User user = this.service.get(UserTestData.USER_ID);
        UserTestData.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> this.service.get(UserTestData.NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = this.service.getByEmail("admin@gmail.com");
        UserTestData.assertMatch(user, UserTestData.admin);
    }

    @Test
    public void update() {
        User updated = UserTestData.getUpdated();
        this.service.update(updated);
        UserTestData.assertMatch(this.service.get(UserTestData.USER_ID), UserTestData.getUpdated());
    }

    @Test
    public void getAll() {
        List<User> all = this.service.getAll();
        UserTestData.assertMatch(all, UserTestData.admin, UserTestData.guest, UserTestData.user);
    }
}
package ru.javawebinar.topjava.service;

import org.junit.Assume;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService service;
    @Autowired
    private CacheManager cacheManager;
    @Autowired(required = false)
    protected JpaUtil jpaUtil;

    @Before
    public void setup() {
        if (super.isNotJdbcProfile()) {
            cacheManager.getCache("users").clear();
            jpaUtil.clear2ndLevelHibernateCache();
        }
    }

    @Test
    public void create() {
        User created = service.create(UserTestData.getNew());
        int newId = created.id();
        User newUser = UserTestData.getNew();
        newUser.setId(newId);
        UserTestData.USER_MATCHER.assertMatch(created, newUser);
        UserTestData.USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        service.delete(UserTestData.ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(UserTestData.NOT_FOUND));
    }

    @Test
    public void get() {
        User user = service.get(UserTestData.ADMIN_ID);
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.admin);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(UserTestData.NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.admin);
    }

    @Test
    public void a1update() {
        User updated = UserTestData.getUpdated();
        service.update(updated);
        UserTestData.USER_MATCHER.assertMatch(service.get(UserTestData.USER_ID), UserTestData.getUpdated());
    }

    @Test
    public void a2getAll() {
        List<User> all = service.getAll();
        UserTestData.USER_MATCHER.assertMatch(all, UserTestData.admin, UserTestData.guest, UserTestData.user);
    }

    @Test
    public void createWithException() {
        Assume.assumeTrue(super.isNotJdbcProfile());
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }
}
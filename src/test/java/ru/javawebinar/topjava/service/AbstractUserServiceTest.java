package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService service;
    @Autowired
    private CacheManager cacheManager;
    @Autowired(required = false)
    protected JpaUtil jpaUtil;

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
        if (super.isNotJdbcProfile()) {
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
    public void update() {
        User updated = UserTestData.getUpdated();
        service.update(updated);
        UserTestData.USER_MATCHER.assertMatch(service.get(UserTestData.USER_ID), UserTestData.getUpdated());
        UserTestData.USER_MATCHER.assertMatch(service.getAll(), UserTestData.admin, UserTestData.guest, UserTestData.getUpdated());
    }

    @Test
    public void getAll() {
        List<User> all = service.getAll();
        UserTestData.USER_MATCHER.assertMatch(all, UserTestData.admin, UserTestData.guest, UserTestData.user);
    }

    @Test
    public void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }

    @Test
    public void getByNotExistEmail() {
        Assert.assertThrows(NotFoundException.class, () -> service.getByEmail(UserTestData.notFound.getEmail()));
    }

    @Test
    public void getByEmailWithoutUserRole() {
        User user = service.getByEmail("guest@gmail.com");
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.guest);
    }

    @Test
    public void getUserWithoutRole() {
        User user = service.get(UserTestData.GUEST_ID);
        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.guest);
    }

    @Test
    public void deleteAllRoles() {
        User updated = new User(UserTestData.admin);
        updated.setRoles(List.of());
        service.update(updated);
        Assertions.assertThat(service.get(UserTestData.ADMIN_ID).getRoles()).isEqualTo(EnumSet.noneOf(Role.class));
    }

    @Test
    public void addAllRoles() {
        User updated = new User(UserTestData.admin);
        updated.setRoles(List.of());
        service.update(updated);
        updated.setRoles(UserTestData.admin.getRoles());
        service.update(updated);
        UserTestData.USER_MATCHER.assertMatch(service.get(UserTestData.ADMIN_ID), UserTestData.admin);
    }

    @Test
    public void updateGuestRoles() {
        User updated = new User(UserTestData.guest);
        updated.setRoles(List.of(Role.USER));
        service.update(updated);
        Assert.assertEquals(service.get(UserTestData.GUEST_ID).getRoles(), EnumSet.of(Role.USER));
    }

    @Test
    public void addUserRole() {
        User updated = new User(UserTestData.user);
        updated.setRoles(List.of(Role.USER, Role.ADMIN));
        service.update(updated);
        Assert.assertEquals(service.get(UserTestData.USER_ID).getRoles(), EnumSet.of(Role.USER, Role.ADMIN));
    }
}
package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.EnumSet;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void createValidationTest() {
        Assert.assertThrows(ConstraintViolationException.class, () -> {
            User aNew = UserTestData.getNew();
            aNew.setEmail("");
            service.create(aNew);
        });
    }

    @Test
    public void getByNotExistEmail() {
        Assert.assertThrows(NotFoundException.class, () -> service.getByEmail("not_exist@gmail.com"));
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
    public void updateRemoveAllRoles() {
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
        Assert.assertEquals(EnumSet.copyOf(updated.getRoles()), EnumSet.of(Role.USER));
    }

    @Test
    public void addUserRole() {
        User updated = new User(UserTestData.user);
        updated.setRoles(List.of(Role.USER, Role.ADMIN));
        service.update(updated);
        Assert.assertEquals(EnumSet.copyOf(updated.getRoles()), EnumSet.of(Role.USER, Role.ADMIN));
    }
}
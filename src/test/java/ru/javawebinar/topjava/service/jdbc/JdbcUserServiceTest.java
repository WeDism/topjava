package ru.javawebinar.topjava.service.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void createValidationTest() {
        Assert.assertThrows(RuntimeException.class, () -> {
            User aNew = UserTestData.getNew();
            aNew.setEmail("");
            service.create(aNew);
        });
    }
}
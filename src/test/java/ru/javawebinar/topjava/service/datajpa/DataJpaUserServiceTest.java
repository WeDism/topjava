package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.common.UserCommonServiceTest;

import java.util.LinkedHashSet;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MEALS_MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserCommonServiceTest {
    @Test
    public void getWithMeals() {
        User user = super.service.getWithMeals(USER_ID);
        user.setMeals(new LinkedHashSet<>(user.getMeals()));
        USER_MEALS_MATCHER.assertMatch(user, UserTestData.userWithMeals);
    }
}

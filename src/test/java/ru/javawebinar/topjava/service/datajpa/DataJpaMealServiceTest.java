package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.common.MealCommonServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealCommonServiceTest {
    @Test
    public void getWithMeals() {
        Meal meal = super.service.getWithUser(MealTestData.MEAL1_ID, UserTestData.USER_ID);
        MealTestData.MEAL_MATCHER.assertMatch(meal, MealTestData.meal1);
        UserTestData.USER_MATCHER.assertMatch(meal.getUser(), UserTestData.user);
    }

    @Test
    public void getWithUserNotOwn() {
        Assert.assertThrows(NotFoundException.class, () -> super.service.getWithUser(MealTestData.MEAL1_ID, UserTestData.GUEST_ID));
    }

    @Test
    public void getAllUserNotOwn() {
        List<Meal> all = super.service.getAll(UserTestData.GUEST_ID);
        Assert.assertNotNull(all);
        Assert.assertEquals(all.size(), 0);
    }

    @Test
    public void getWithUserNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> super.service.getWithUser(MealTestData.NOT_FOUND, UserTestData.USER_ID));
    }
}

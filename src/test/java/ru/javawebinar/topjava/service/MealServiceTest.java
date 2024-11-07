package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = {"classpath:db/populateDB.sql"}, config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = this.service.get(MealTestData.MEAL_ID_BREAKFAST_1_USER, UserTestData.USER_ID);
        MealTestData.assertMatch(meal, MealTestData.breakfast1User);
    }

    @Test
    public void getNotExisting() {
        Assert.assertThrows(NotFoundException.class, () -> this.service.get(MealTestData.MEAL_ID_NOT_EXISTING, UserTestData.USER_ID));
    }

    @Test
    public void foreignGet() {
        Assert.assertThrows(NotFoundException.class, () -> this.service.get(MealTestData.MEAL_ID_BREAKFAST_1_USER, UserTestData.ADMIN_ID));
    }

    @Test
    public void delete() {
        this.service.delete(MealTestData.MEAL_ID_BREAKFAST_1_USER, UserTestData.USER_ID);
        Assert.assertThrows(NotFoundException.class, () -> this.service.get(MealTestData.MEAL_ID_BREAKFAST_1_USER, UserTestData.USER_ID));
    }

    @Test
    public void foreignDelete() {
        Assert.assertThrows(NotFoundException.class, () -> this.service.delete(MealTestData.MEAL_ID_BREAKFAST_1_USER, UserTestData.ADMIN_ID));
    }

    @Test
    public void deleteNotExisting() {
        Assert.assertThrows(NotFoundException.class, () -> this.service.delete(MealTestData.MEAL_ID_NOT_EXISTING, UserTestData.USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 1);
        List<Meal> betweenInclusive = this.service.getBetweenInclusive(startDate, endDate, UserTestData.USER_ID);
        MealTestData.assertMatch(betweenInclusive, MealTestData.mealsUserFirstDay);
    }

    @Test
    public void getBetweenInclusiveWithEmptyBorders() {
        List<Meal> betweenInclusive = this.service.getBetweenInclusive(null, null, UserTestData.USER_ID);
        MealTestData.assertMatch(betweenInclusive, MealTestData.mealsUserAllDay);
    }

    @Test
    public void getAll() {
        List<Meal> all = this.service.getAll(UserTestData.USER_ID);
        MealTestData.assertMatch(all, MealTestData.mealsUserAllDay);
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        this.service.update(updated, UserTestData.USER_ID);
        MealTestData.assertMatch(this.service.get(MealTestData.MEAL_ID_LUNCH_2_USER, UserTestData.USER_ID), MealTestData.getUpdated());
    }

    @Test
    public void foreignUpdate() {
        Assert.assertThrows(NotFoundException.class, () -> {
            Meal updated = MealTestData.getUpdated();
            this.service.update(updated, UserTestData.ADMIN_ID);
        });
    }

    @Test
    public void duplicateDataUpdate() {
        Assert.assertThrows(DuplicateKeyException.class, () -> {
            this.service.update(MealTestData.getDuplicateUpdated(), UserTestData.USER_ID);
        });
    }

    @Test
    public void create() {
        Meal created = this.service.create(MealTestData.getNew(), UserTestData.USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(this.service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Assert.assertThrows(DuplicateKeyException.class, () -> {
            this.service.create(MealTestData.getDuplicateDataTimeMeal(), UserTestData.USER_ID);
        });
    }
}
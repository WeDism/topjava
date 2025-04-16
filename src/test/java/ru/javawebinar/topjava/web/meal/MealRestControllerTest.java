package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.user;

public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEALTO_MATCHER.contentJson(MealsUtil.getTos(MealTestData.meals, user.getCaloriesPerDay())));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL + AbstractMealController.ID, MealTestData.MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_MATCHER
                        .contentJson(MealTestData.meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(MealRestController.REST_URL + AbstractMealController.ID, MealTestData.meal4.id()))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertThrows(NotFoundException.class, () -> mealService.get(MealTestData.meal4.id(), USER_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(MealRestController.REST_URL)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());
        Meal created = MealTestData.MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MealTestData.MEAL_MATCHER.assertMatch(created, newMeal);
        MealTestData.MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(MealRestController.REST_URL + AbstractMealController.ID, MealTestData.MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        MealTestData.MEAL_MATCHER.assertMatch(mealService.get(MealTestData.MEAL1_ID, USER_ID), updated);
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL + MealRestController.FILTER)
                .param("startDate", "2020-01-30")
                .param("startTime", "10:30")
                .param("endDate", "2020-01-31")
                .param("endTime", "17:30"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEALTO_MATCHER.contentJson(
                        MealsUtil.getTos(MealTestData.meals, user.getCaloriesPerDay()).stream()
                                .filter(meal -> meal.getId().equals(MealTestData.meal2.id()) || meal.getId().equals(MealTestData.meal6.id())).collect(Collectors.toList())));
    }

    @Test
    void getBetweenWithNullValues() throws Exception {
        perform(MockMvcRequestBuilders.get(MealRestController.REST_URL + MealRestController.FILTER)
                .param("startDate", "")
                .param("startTime", "")
                .param("endDate", "")
                .param("endTime", ""))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEALTO_MATCHER.contentJson(
                        MealsUtil.getTos(MealTestData.meals, user.getCaloriesPerDay())));
    }
}

package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.Collection;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public Collection<Meal> getAll() {
        log.info("getAll");
        return this.mealService.getAll();
    }

    public Meal get(int id, int userId) {
        log.info("get {} with userId={}", id, userId);
        return this.mealService.get(id, userId);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        return this.mealService.create(meal);
    }

    public void delete(int id, int userId) {
        log.info("delete {} with userId={}", id, userId);
        this.mealService.delete(id, userId);
    }

    public void update(Meal meal, int userId) {
        log.info("update {} with userId={}", meal, userId);
        ValidationUtil.assureIdConsistent(meal, userId);
        this.mealService.update(meal, userId);
    }

    public Collection<Meal> getAllByUser(int userId) {
        log.info("getAllByUser {}", userId);
        return this.mealService.getAllByUser(userId);
    }

}
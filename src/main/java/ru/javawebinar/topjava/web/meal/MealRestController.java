package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealConverter;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService mealService;

    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {} with userId={}", id, userId);
        return this.mealService.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("create {} with userId={}", meal, userId);
        ValidationUtil.checkNew(meal);
        return this.mealService.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {} with userId={}", id, userId);
        this.mealService.delete(id, userId);
    }

    public void update(Meal meal, int userId) {
        log.info("update {} with userId={}", meal, userId);
        this.mealService.update(meal, userId);
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll {}", userId);
        return MealConverter.toTos(this.mealService.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByFilter(LocalTime startTime, LocalTime endTime, LocalDate startDate, LocalDate endDate) {
        int userId = SecurityUtil.authUserId();
        log.info("getAllByFilter {}", userId);
        LocalTime startTimeComputed = startTime == null ? LocalTime.MIN : startTime;
        LocalTime endTimeComputed = endTime == null ? LocalTime.MAX : endTime;
        LocalDate startDateComputed = startDate == null ? LocalDate.MIN : startDate;
        LocalDate endDateComputed = endDate == null ? LocalDate.MAX : endDate;
        return MealConverter.toTos(this.mealService.getAllByFilter(userId, startDateComputed, endDateComputed),
                startTimeComputed, endTimeComputed, SecurityUtil.authUserCaloriesPerDay());
    }
}
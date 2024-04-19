package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

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

    public void update(Meal meal) {
        int userId = SecurityUtil.authUserId();
        log.info("update {} with userId={}", meal, userId);
        this.mealService.update(meal, userId);
    }

    public Collection<MealTo> getAllByUser() {
        int userId = SecurityUtil.authUserId();
        log.info("getAllByUser {}", userId);
        return MealsUtil.getTos(this.mealService.getAllByUser(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Collection<MealTo> getAllByUserWithDateTimeFiltered(String startDateText, String startTimeText,
                                                               String endDateText, String endTimeText) {
        LocalDate startDate = ObjectUtils.isEmpty(startDateText) ? LocalDate.MIN : LocalDate.parse(startDateText);
        LocalDate endDate = ObjectUtils.isEmpty(endDateText) ? LocalDate.MAX : LocalDate.parse(endDateText);
        LocalTime startTime = ObjectUtils.isEmpty(startTimeText) ? LocalTime.MIN : LocalTime.parse(startTimeText);
        LocalTime endTime = ObjectUtils.isEmpty(endTimeText) ? LocalTime.MAX : LocalTime.parse(endTimeText);
        int userId = SecurityUtil.authUserId();
        log.info("getAllByUser {}", userId);
        return MealsUtil.getTos(this.mealService.getAllByUserWithFilter(userId, startTime, endTime, startDate, endDate),
                MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }
}
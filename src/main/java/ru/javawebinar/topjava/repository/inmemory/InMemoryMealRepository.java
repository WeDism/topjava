package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealSampleData;
import ru.javawebinar.topjava.util.MealsFiltrationHandler;
import ru.javawebinar.topjava.util.UserSampleData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    /**
     * {userid: {mealId: meal}}
     */
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealSampleData.meals1.forEach((meal -> this.save(meal, UserSampleData.FIRST_USER)));
        MealSampleData.meals2.forEach((meal -> this.save(meal, UserSampleData.SECOND_USER)));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            int mealId = this.counter.incrementAndGet();
            Map<Integer, Meal> userMeals = this.repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
            userMeals.put(mealId, ((Meal) meal.setId(mealId)));
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> userMeals = this.repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        userMeals.replace(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = this.repository.get(userId);
        return userMeals != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = this.repository.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeals = this.repository.get(userId);
        return userMeals != null
                ? userMeals.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    @Override
    public List<Meal> getAllFilteredEntity(int userId,
                                           LocalTime startTime, LocalTime endTime,
                                           LocalDate startDate, LocalDate endDate, int caloriesPerDay) {
        Map<Integer, Meal> userMeals = this.repository.get(userId);
        return userMeals == null
                ? Collections.emptyList()
                : MealsFiltrationHandler.getFilteredEntity(userMeals.values(), caloriesPerDay, startTime, endTime, startDate, endDate);
    }
}


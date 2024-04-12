package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.SampleData;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    /**
     * {userid: {mealId: meal}}
     */
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.meals1.forEach((meal -> this.save(meal, SampleData.FIRST_USER)));
        MealsUtil.meals2.forEach((meal -> this.save(meal, SampleData.SECOND_USER)));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            int mealId = this.counter.incrementAndGet();
            meal.setUserId(userId).setId(mealId);
            this.repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
            this.repository.get(userId).put(mealId, meal);
            return meal;
        }
        // handle case: update, but not present in storage
        this.repository.computeIfAbsent(userId, k -> new ConcurrentHashMap<>());
        return this.repository.get(userId).replace(meal.getId(), meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return this.get(id, userId) != null && this.repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = this.repository.get(userId).get(id);
        if (meal != null && userId != meal.getUserId())
            return null;
        return meal;
    }

    @Override
    public Collection<Meal> getAllByUser(int userId) {
        return this.repository.get(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getAllByUserWithFilter(int userId,
                                                   LocalTime startTime, LocalTime endTime,
                                                   LocalDate startDate, LocalDate endDate) {
        Collection<Meal> mealCollection = this.repository.get(userId).values();
        Predicate<Meal> filterTime = meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
        Predicate<Meal> filterDate = meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate);
        return mealCollection.stream()
                .filter(filterDate.and(filterTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}


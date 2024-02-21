package ru.javawebinar.topjava.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public enum InMemoryMealRepository implements MealRepository {
    INSTANCE;

    private final static Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);

    InMemoryMealRepository() {
        MealsUtil.getMealList().forEach(this::create);
    }

    public List<Meal> getAll() {
        return this.meals.values().stream().map(Meal::new).collect(Collectors.toList());
    }

    @Override
    public Meal getById(int id) {
        Meal meal = this.meals.get(id);
        return meal != null ? new Meal(meal) : null;
    }

    @Override
    public void delete(int id) {
        this.meals.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        Meal newMeal = new Meal(meal);
        Meal replacedMeal = this.meals.replace(meal.getId(), newMeal);
        if (replacedMeal == null) {
            log.warn("Meal with id {} is not present", meal.getId());
            return null;
        }
        return newMeal;
    }

    @Override
    public Meal create(Meal meal) {
        int id = this.currentId.incrementAndGet();
        Meal newMeal = new Meal(id, meal);
        this.meals.put(id, newMeal);
        return new Meal(newMeal);
    }
}

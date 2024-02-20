package ru.javawebinar.topjava.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public enum InMemoryMealRepository implements MealRepository {
    INSTANCE;

    private final static Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger currentId = new AtomicInteger(0);
    private final ReentrantLock reentrantLock = new ReentrantLock();

    InMemoryMealRepository() {
        MealsUtil.getMealList().forEach(this::create);
    }

    public List<Meal> getAll() {
        return this.meals.values().stream().map(Meal::new).collect(Collectors.toList());
    }

    @Override
    public Meal getById(int id) {
        Meal meal = this.meals.get(id);
        if (meal != null)
            return new Meal(meal);
        else return null;
    }

    @Override
    public void delete(int id) {
        this.meals.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        Meal replacedMeal = this.meals.replace(meal.getId(), new Meal(meal));
        if (replacedMeal == null)
            log.warn("Meal with id {} is not present", meal.getId());
        return null;
    }

    @Override
    public Meal create(Meal meal) {
        int id = this.currentId.incrementAndGet();
        return this.meals.put(id, new Meal(id, meal));
    }
}

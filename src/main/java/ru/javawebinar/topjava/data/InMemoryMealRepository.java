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
        if (this.meals.containsKey(id))
            return new Meal(this.meals.get(id));
        else return null;
    }

    @Override
    public synchronized void delete(int id) {
        this.meals.remove(id);
    }

    @Override
    public synchronized Meal update(Meal meal) {
        Meal replace = this.meals.replace(meal.getId(), new Meal(meal));
        if (replace == null)
            log.warn("Meal with id {} is not present", meal.getId());
        return null;
    }

    @Override
    public synchronized Meal create(Meal meal) {
        int id = this.currentId.incrementAndGet();
        this.meals.put(id, new Meal(id, meal));
        return this.meals.get(id);
    }
}

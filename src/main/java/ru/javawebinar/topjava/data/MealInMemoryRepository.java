package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public enum MealInMemoryRepository implements MealRepository {
    INSTANCE;

    private final Map<Integer, Meal> meals =
            new ConcurrentHashMap<>(
                    new LinkedHashMap<>(
                            MealsUtil.getMealList().stream().collect(Collectors.toMap(Meal::getId, meal -> meal))));
    private final AtomicInteger currentId = new AtomicInteger(meals.keySet().stream().mapToInt(e -> e).max().orElse(0));

    public List<Meal> getAll() {
        return this.meals.values().stream().map(Meal::new).collect(Collectors.toList());
    }

    @Override
    public Meal getAll(int id) {
        return this.meals.get(id);
    }

    @Override
    public void delete(int id) {
        this.meals.remove(id);
    }

    @Override
    public Meal update(Meal meal) {
        if (meal.getId() == null)
            return null;
        this.meals.put(meal.getId(), new Meal(meal));
        return this.meals.get(meal.getId());
    }

    @Override
    public Meal create(Meal meal) {
        int id = this.currentId.incrementAndGet();
        this.meals.put(id, new Meal(id, meal));
        return this.meals.get(id);
    }
}

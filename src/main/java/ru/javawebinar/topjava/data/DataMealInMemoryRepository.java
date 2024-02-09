package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public enum DataMealInMemoryRepository implements DataMealRepository {
    INSTANCE;

    private final Map<Integer, Meal> MEALS =
            Collections.synchronizedMap(
                    new LinkedHashMap<>(
                            MealsUtil.getMealList().stream().collect(Collectors.toMap(Meal::getId, meal -> meal))));
    private final AtomicInteger currentId = new AtomicInteger(MEALS.keySet().stream().mapToInt(e -> e).max().orElse(0));

    public Map<Integer, Meal> get() {
        return this.MEALS.values().stream().map(Meal::new).collect(Collectors.toMap(Meal::getId, meal -> meal));
    }

    @Override
    public Meal get(int id) {
        return this.MEALS.get(id);
    }

    @Override
    public void delete(int id) {
        this.MEALS.remove(id);
    }

    @Override
    public void update(Meal meal) {
        this.MEALS.put(meal.getId(), new Meal(meal));
    }

    @Override
    public void create(Meal meal) {
        int id = this.currentId.incrementAndGet();
        this.MEALS.put(id, new Meal(id, meal));
    }
}

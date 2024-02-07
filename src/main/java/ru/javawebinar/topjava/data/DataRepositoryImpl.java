package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum DataRepositoryImpl implements DataRepository {
    INSTANCE;

    private final Set<Meal> meals = Collections.synchronizedSet(new HashSet<>(MealsUtil.getMealList()));

    public Set<Meal> getMeals() {
        return meals;
    }
}

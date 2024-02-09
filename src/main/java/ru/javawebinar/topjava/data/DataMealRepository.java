package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.util.Map;

public interface DataMealRepository {
    Map<Integer, Meal> get();

    Meal get(int id);

    void delete(int id);

    void update(Meal meal);

    void create(Meal meal);
}

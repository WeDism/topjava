package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    List<Meal> getAll();

    Meal getById(int id);

    void delete(int id);

    Meal update(Meal meal);

    Meal create(Meal meal);
}

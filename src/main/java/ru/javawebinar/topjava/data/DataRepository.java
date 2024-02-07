package ru.javawebinar.topjava.data;

import ru.javawebinar.topjava.model.Meal;

import java.util.Set;

public interface DataRepository {
    Set<Meal> getMeals();
}

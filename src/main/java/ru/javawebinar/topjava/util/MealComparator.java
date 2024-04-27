package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;

public final class MealComparator {
    private MealComparator() {
    }

    public static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime).reversed();
}

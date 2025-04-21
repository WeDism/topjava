package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

public class MealToConverter {
    private MealToConverter() {
    }

    public static MealTo convert(Meal meal, boolean excess) {
        return new MealTo(meal.id(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}

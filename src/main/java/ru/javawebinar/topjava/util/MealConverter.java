package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.util.Collection;
import java.util.List;

public final class MealConverter {
    private MealConverter() {
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static MealTo createTo(Meal meal) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getExcess());
    }

    public static Meal updateEntity(Meal meal, boolean excess) {
        return new Meal(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<MealTo> toTos(Collection<Meal> meals, int caloriesPerDay) {
        return MealsFiltrationHandler.filterByPredicate(meals, caloriesPerDay, meal -> true, meal -> true, MealConverter::createTo);
    }
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public final class MealConverter {
    private MealConverter() {
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<MealTo> toTos(Collection<Meal> meals, int caloriesPerDay) {
        return MealsFiltrationHandler.filterByPredicate(meals, caloriesPerDay, meal -> true, meal -> true, MealConverter::createTo);
    }

    public static List<MealTo> toTos(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Predicate<Meal> filterTime = meal -> DateTimeComparator.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
        return MealsFiltrationHandler.filterByPredicate(meals, caloriesPerDay, filterTime, meal -> true, MealConverter::createTo);
    }
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MealConverter {
    private MealConverter() {
    }

    public static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }

    public static List<MealTo> toTos(Collection<Meal> meals, int caloriesPerDay) {
        return MealConverter.filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> toTos(Collection<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Predicate<Meal> filterTime = meal -> DateTimeComparator.isBetweenHalfOpen(meal.getTime(), startTime, endTime);
        return MealConverter.filterByPredicate(meals, caloriesPerDay, filterTime);
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals,
                                                  int caloriesPerDay,
                                                  Predicate<Meal> filterTime) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );
        return meals.stream()
                .filter(filterTime)
                .map(meal -> MealConverter.createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
